package student;

import game.*;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Explorer {

    private static final int START_POSITION = 1;

    /**
     * Explore the cavern, trying to find the orb in as few steps as possible.
     * Once you find the orb, you must return from the function in order to pick
     * it up. If you continue to move after finding the orb rather
     * than returning, it will not count.
     * If you return from this function while not standing on top of the orb,
     * it will count as a failure.
     * <p>
     * There is no limit to how many steps you can take, but you will receive
     * a score bonus multiplier for finding the orb in fewer steps.
     * <p>
     * At every step, you only know your current tile's ID and the ID of all
     * open neighbor tiles, as well as the distance to the orb at each of these tiles
     * (ignoring walls and obstacles).
     * <p>
     * To get information about the current state, use functions
     * getCurrentLocation(),
     * getNeighbours(), and
     * getDistanceToTarget()
     * in ExplorationState.
     * You know you are standing on the orb when getDistanceToTarget() is 0.
     * <p>
     * Use function moveTo(long id) in ExplorationState to move to a neighboring
     * tile by its ID. Doing this will change state to reflect your new position.
     * <p>
     * A suggested first implementation that will always find the orb, but likely won't
     * receive a large bonus multiplier, is a depth-first search.
     *
     * @param state the information available at the current state
     */

    public void explore(ExplorationState state) {
        Set<Long> seen = new HashSet<>(); //set of neighbours seen/added to queue
        PriorityQueue<List<Long>> q = new PriorityQueueImpl<>(); //queue containing paths to prioritised by distance

        List<Long> initialPath = new ArrayList<>(); //empty list for initial path
        initialPath.add(state.getCurrentLocation()); //add root of tree
        seen.add(state.getCurrentLocation()); // mark the start location as seen
        enqueueNeighbours(q, seen, initialPath, state); //add the neighbours to the queue (not seen and mark as seen).


        List<Long> oldPath = initialPath; //start from root for paths.

        while (state.getDistanceToTarget() > 0) {// while we are not sat on the orb.
            List<Long> newPath = q.poll(); //deqeue next path closest to orb

            int shared = 0;
            while (shared < oldPath.size() && shared < newPath.size() && oldPath.get(shared).equals(newPath.get(shared))) { //count number of shared nodes on both paths
                shared++;
            }

            for (int j = oldPath.size() - 2; j >= shared - 1; j--) { // moving up tree to find shared node.
                state.moveTo(oldPath.get(j));
            }

            for (int j = shared; j < newPath.size(); j++) { //moving from shared node to destination.
                state.moveTo(newPath.get(j));
            }

            enqueueNeighbours(q, seen, newPath, state); //queue new neighbours at destination.
            oldPath = newPath; //store new path as old to use in next iteration.
        }
    }

    /**
     * enqueueNeighbours adds the paths to neighbours to the queue.
     * @param q current queue
     * @param seen neighbours we have already seen (so not point adding them again)
     * @param path the path to the current neighbours we are adding
     * @param state the current game state
     */
    void enqueueNeighbours(PriorityQueue<List<Long>> q, Set<Long> seen, List<Long> path, ExplorationState state) {
        for (NodeStatus node : state.getNeighbours()) {
            if (seen.contains(node.getId())) {// if we have seen neighbour before, skip this neighbour.
                continue;
            }

            List<Long> nodePath = new ArrayList<>(path); // create copy of existing path and add neighbour to end.
            nodePath.add(node.getId());

            q.add(nodePath, node.getDistanceToTarget()); //adding path created to queue.
            seen.add(node.getId()); //adding neighbour to seen set.
        }
    }

    /**
     * Escape from the cavern before the ceiling collapses, trying to collect as much
     * gold as possible along the way. Your solution must ALWAYS escape before time runs
     * out, and this should be prioritized above collecting gold.
     * <p>
     * You now have access to the entire underlying graph, which can be accessed through EscapeState.
     * getCurrentNode() and getExit() will return you Node objects of interest, and getVertices()
     * will return a collection of all nodes on the graph.
     * <p>
     * Note that time is measured entirely in the number of steps taken, and for each step
     * the time remaining is decremented by the weight of the edge taken. You can use
     * getTimeRemaining() to get the time still remaining, pickUpGold() to pick up any gold
     * on your current tile (this will fail if no such gold exists), and moveTo() to move
     * to a destination node adjacent to your current node.
     * <p>
     * You must return from this function while standing at the exit. Failing to do so before time
     * runs out or returning from the wrong location will be considered a failed run.
     * <p>
     * You will always have enough time to escape using the shortest path from the starting
     * position to the exit, although this will not collect much gold.
     *
     * @param state the information available at the current state
     */
    public void escape(EscapeState state) throws InterruptedException {
        PriorityQueue<Path> q = new PriorityQueueImpl<>();
        Node currentLocation = state.getCurrentNode();
        List<Path> finishedRoutes = new CopyOnWriteArrayList<>();
        Collection<Node> seen = new HashSet<>();
        List<Node> initialPath = new ArrayList<>(); //empty list for initial path
        initialPath.add(currentLocation); //add root of tree
        q.add(new Path(0, 0, initialPath), 0); //all with have same gold and count from here so not point adding.

        getPathAndLastNode(state, q, finishedRoutes, seen, false);

        if (finishedRoutes.size() == 0) { //then we did not find an escape, so we reverse the priority queue which gives us a definite escape.
           //instantiate a new set of everything to start again.
            PriorityQueue<Path> pq = new PriorityQueueImpl<>();
            Collection<Node> seenSecond = new HashSet<>();
            pq.add(new Path(0, 0, initialPath), 0); //all will have same gold and count from here so not point adding.
            getPathAndLastNode(state, pq, finishedRoutes, seenSecond, true); // true == emergencyExit
        }

            Path bestPath = findBestRoute(finishedRoutes); //returns route with most gold, could be 0.

        if (state.getCurrentNode().getTile().getGold()>0){ // pick up gold on current tile if there is some.
            state.pickUpGold();
        }
        for (int i = START_POSITION; i < bestPath.getPath().size(); i++) { //follow path from start
                state.moveTo(bestPath.getPath().get(i));
                if (state.getCurrentNode().getTile().getGold() > 0) {
                    state.pickUpGold();
                }
            }
    }

    /**
     * This method pulls the last path on the queue, it then takes the final node on the path and calls the addNeighbours method.
     * @param state game state
     * @param q current queue
     * @param finishedRoutes routes that have found the exit, may be 0.
     * @param seen nodes that have been seen before, therefore already mapped.
     * @param emergencyExit indicator if first method failed or not.
     */
    private void getPathAndLastNode(EscapeState state, PriorityQueue<Path> q, List<Path> finishedRoutes, Collection<Node> seen, boolean emergencyExit) {
        while (q.size() > 0) {
            Path newPath = q.poll(); //pull path of queue
            Node current = newPath.getPath().get(newPath.getPath().size() - 1); //change current node to last one on path pulled off queue.
            addingNeighbours(state, q, finishedRoutes, newPath, current, current.getNeighbours(), seen, emergencyExit); //add the neighbours to the queue, if neighbour is an exit, add it and show it as a finished queue.
        }
    }

    /**
     * This method adds the neighbour's path to the queue, excluding those that result in a longer duration than time remaining or those seen before.
     * the emergencyExit status allows for a method prioritising the shorter queues to executed first, meaning that we will find the shortest route, which is sometimes
     * required if we run out of options on the longer routes.
     *
     * If we go down a dead end, then we take a harsh approach and wipe out any paths that will follow any route to that dead end.
     *
     * @param state game state
     * @param q current queue of paths
     * @param finishedRoutes current queue of finished routes
     * @param newPath latest path pulled off the queue
     * @param current node at the end of the latest path pulled off.
     * @param nbrs neighbours of the current node.
     * @param seen nodes that have been seen leading to a dead end.
     * @param emergencyExit indicator if first method failed or not.
     */
    private void addingNeighbours(EscapeState state, PriorityQueue<Path> q, List<Path> finishedRoutes, Path newPath, Node current, Collection<Node> nbrs, Collection<Node> seen, boolean emergencyExit) {
        for (Node nbr : nbrs) {
            Path originalPath = (new Path(newPath.getGoldCount(), newPath.getTimeTaken(), new ArrayList<>(newPath.getPath()))); //create a duplicate of the current path
            Edge thisEdge = current.getEdge(nbr); //edge to get to this neighbour to calculate time taken.
            if (originalPath.getTimeTaken() + thisEdge.length > state.getTimeRemaining()) { //if path will take us over time remaining, delete.
                continue;
            }

            if (seen.contains(nbr)) { //if path leads to a neighbour that lead us to a dead end then delete.
                continue;
            }
            if (originalPath.getPath().contains(nbr) && nbr.getId()!=current.getId()) { //if path's neighbour has been seen before, but is not the last visited then wipe the path route from all paths.
                for (int i = 0; i < originalPath.getPath().size() ; i++) {
                    seen.add(newPath.getPath().get(i));
                }
            }
            if (nbr.equals(state.getExit())) {
                updatePathStats(nbr, originalPath, thisEdge);
                finishedRoutes.add(originalPath); //if the neighbour we select is the exit, then this route has finished.
                continue;
            }
            updatePathStats(nbr, originalPath, thisEdge);
            if (emergencyExit) {
                q.add(originalPath, originalPath.getTimeTaken());
            } else {
                q.add(originalPath, -originalPath.getTimeTaken());
            }
        }
    }

    /**
     * This method updates the total gold and time taken for a path to run. It also adds to the new node to the path.
     * @param nbr the new node that has been added to the list.
     * @param originalPath the original path.
     * @param thisEdge the edge leading to this nbr node.
     */
    private void updatePathStats(Node nbr, Path originalPath, Edge thisEdge) {
        int newGold = nbr.getTile().getGold();
        int oldGoldCount = originalPath.getGoldCount();
        originalPath.setGoldCount(oldGoldCount + newGold); //add new gold value
        int oldTime = originalPath.getTimeTaken(); // retrieve current time taken
        originalPath.setTimeTaken(oldTime + thisEdge.length); //add new timetaken value
        originalPath.getPath().add(nbr); //add the new nbr node to the path.
    }

    /**
     * A method that finds the most successful path based on Gold count and returns the path.
     * @param successfulRoutes list containing all successful routes.
     * @return Path with most gold
     */
    private Path findBestRoute(List<Path> successfulRoutes) {
        int maxIndex = 0;
        int maxScore = 0;
        for (int i = 0; i < successfulRoutes.size(); i++) {
            if (successfulRoutes.get(i).getGoldCount() > maxScore) { //if gold count is more than a previous gold count, add it to the list.
                maxIndex = i;
                maxScore = successfulRoutes.get(i).getGoldCount();
            }
        }
        return successfulRoutes.get(maxIndex); //return path with most gold.
    }

}