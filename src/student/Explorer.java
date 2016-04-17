package student;

import game.*;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Explorer {

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

            //System.out.println("Going from path: " + oldPath);
            //System.out.println("Going to path: " + newPath);

            for (int j = oldPath.size() - 2; j >= shared - 1; j--) { // moving up tree to find shared node.
                //System.out.println("Reverting to: " + oldPath.get(j));
                state.moveTo(oldPath.get(j));
            }

            for (int j = shared; j < newPath.size(); j++) { //moving from shared node to destination.
                //System.out.println("Moving to: " + newPath.get(j));
                state.moveTo(newPath.get(j));
            }

            enqueueNeighbours(q, seen, newPath, state); //queue new neighbours at destination.
            oldPath = newPath; //store new path as old to use in next iteration.
        }
    }

    void enqueueNeighbours(PriorityQueue<List<Long>> q, Set<Long> seen, List<Long> path, ExplorationState state) {
        for (NodeStatus node : state.getNeighbours()) {
            if (seen.contains(node.getId())) {// if we have seen neighbour before, skip this neighbour.
                continue;
            }

            List<Long> nodePath = new ArrayList<>(path); // create copy of exsisting path and add neighbour to end.
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
        //TODO: Escape from the cavern before time runs out
        PriorityQueue<Path> q = new PriorityQueueImpl<>();
        Node currentLocation = state.getCurrentNode();
        List<Path> finishedRoutes = new CopyOnWriteArrayList<>();
        Collection<Node> seen = new HashSet<>();
        List<Node> initialPath = new ArrayList<>(); //empty list for initial path
        initialPath.add(currentLocation); //add root of tree
        q.add(new Path(0, 0, initialPath), 0); //all with have same gold and count from here so not point adding.
        // while (q.size()>0){
        // }

        searchSolution(state, q, finishedRoutes, seen, false);
       /* while (q.size()>0) {
                //System.out.println(q.size() + " queue size");
                Path newPath = q.poll(); //pull path of queue
                Node current = newPath.getPath().get(newPath.getPath().size() - 1); //change current node to last one on path pulled off queue.
                addingNeighbours(state, q, finishedRoutes, newPath, current, current.getNeighbours()); //add the neighbours to the queue, if neighbour is an exit, add it and show it as a finished queue.
        }*/
        //System.out.println("finishedRouteSize: " + finishedRoutes.size());
        if (finishedRoutes.size() == 0) {
            //System.out.println("Starting process of recall");
            PriorityQueue<Path> pq = new PriorityQueueImpl<>();
            Collection<Node> seenSecond = new HashSet<>();
            pq.add(new Path(0, 0, initialPath), 0); //all with have same gold and count from here so not point adding.
            searchSolution(state, pq, finishedRoutes, seenSecond, true);
        }
        //System.out.println("finishedRouteSize: " + finishedRoutes.size());
            Path bestPath = findBestRoute(finishedRoutes);
            //System.out.println("Expected Gold: " + bestPath.getGoldCount() + " Expected TimeTaken: " + bestPath.getTimeTaken());
            for (int i = 1; i < bestPath.getPath().size(); i++) {
                //System.out.println("moving to: " + bestPath.getPath().get(i).getId() + " from: " + state.getCurrentNode().getId());
                state.moveTo(bestPath.getPath().get(i));
                if (state.getCurrentNode().getTile().getGold() > 0) {
                    state.pickUpGold();
                }
            }
    }

    private void searchSolution(EscapeState state, PriorityQueue<Path> q, List<Path> finishedRoutes, Collection<Node> seen, boolean emergencyExit) {
        while (q.size() > 0) {
            //System.out.println(q.size() + " queue size");
            Path newPath = q.poll(); //pull path of queue
            Node current = newPath.getPath().get(newPath.getPath().size() - 1); //change current node to last one on path pulled off queue.
            addingNeighbours(state, q, finishedRoutes, newPath, current, current.getNeighbours(), seen, emergencyExit); //add the neighbours to the queue, if neighbour is an exit, add it and show it as a finished queue.
        }
    }

    //check Seed : -4606386759928623632
    private void addingNeighbours(EscapeState state, PriorityQueue<Path> q, List<Path> finishedRoutes, Path newPath, Node current, Collection<Node> nbrs, Collection<Node> seen, boolean emergencyExit) {
        for (Node nbr : nbrs) {
            Path originalPath = (new Path(newPath.getGoldCount(), newPath.getTimeTaken(), new ArrayList<>(newPath.getPath())));
            ////System.out.println("Neighbour " + nbr.getId() + " Exit " + state.getExit().getId());
            Edge thisEdge = current.getEdge(nbr);
            if (originalPath.getTimeTaken() + thisEdge.length > state.getTimeRemaining()) {
                ////System.out.println("Kill");
                continue;
            }

            if (seen.contains(nbr)) {
                continue;
            }
            if (originalPath.getPath().contains(nbr)) {
                //System.out.println("Loop");
                //continue;
                for (int i = originalPath.getPath().size() - 1; i >=0; i--) {
                    Collection<Node> tempNbrs = originalPath.getPath().get(i).getNeighbours();
                    for (Node tnbr : tempNbrs) {
                        if (originalPath.getPath().contains(tnbr)) {
                            seen.add(tnbr);
                        } else {
                            continue;
                        }
                    }
                }
            }
            if (nbr.equals(state.getExit())) {
                ////System.out.println("equals exit");
                updateGoldAndTimeTaken(nbr, originalPath, thisEdge);
                finishedRoutes.add(originalPath); //if the neighbour we select is the exit, then this route has finished.
                continue;
            }
            //System.out.println("Adding to path before " + originalPath.getTimeTaken());
            updateGoldAndTimeTaken(nbr, originalPath, thisEdge);
            ////System.out.println("Adding to path after " + originalPath.getTimeTaken());
            ////System.out.println("AFTER" + originalPath.getPath());
            //System.out.println(originalPath.timeTaken);
            if (emergencyExit) {
                q.add(originalPath, originalPath.timeTaken);
            } else {
                q.add(originalPath, -originalPath.timeTaken);
            }
        }
    }

    private void updateGoldAndTimeTaken(Node nbr, Path originalPath, Edge thisEdge) {
        int newGold = nbr.getTile().getGold();
        int oldGoldCount = originalPath.getGoldCount();
        originalPath.setGoldCount(oldGoldCount + newGold);
        int oldTime = originalPath.getTimeTaken(); // retrieve current time taken
        originalPath.setTimeTaken(oldTime + thisEdge.length);
        //System.out.println("OldTime: " + oldTime + "additionalTime: " + thisEdge.length + " = " + originalPath.getTimeTaken() + " total Time" );
        originalPath.getPath().add(nbr);
    }

    private Path findBestRoute(List<Path> successfulRoutes) {
        int maxIndex = 0;
        int maxScore = 0;
        for (int i = 0; i < successfulRoutes.size(); i++) {
            //System.out.println(successfulRoutes.get(i).getGoldCount() + "GoldCount");
            if (successfulRoutes.get(i).getGoldCount() > maxScore) {
                maxIndex = i;
                maxScore = successfulRoutes.get(i).getGoldCount();
            }
        }

        return successfulRoutes.get(maxIndex);


    }

}