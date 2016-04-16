package student;

import game.*;

import java.util.*;
import java.util.stream.Collectors;

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

            System.out.println("Going from path: " + oldPath);
            System.out.println("Going to path: " + newPath);

            for (int j = oldPath.size() - 2; j >= shared - 1; j--) { // moving up tree to find shared node.
                System.out.println("Reverting to: " + oldPath.get(j));
                state.moveTo(oldPath.get(j));
            }

            for (int j = shared; j < newPath.size(); j++) { //moving from shared node to destination.
                System.out.println("Moving to: " + newPath.get(j));
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
    public void escape(EscapeState state) {
        //TODO: Escape from the cavern before time runs out
        Collection<Node> allNodes = state.getVertices();
        Queue<Path> q = new ArrayDeque<>();
        Node currentLocation = state.getCurrentNode();
        Tile currentTile = currentLocation.getTile();
        int currentRow = currentTile.getRow();
        int currentColumn = currentTile.getColumn();
        Queue<List<Node>> finishedRoutes = new ArrayDeque<>();

        List<Node> initialPath = new ArrayList<>(); //empty list for initial path
        initialPath.add(currentLocation); //add root of tree
        enqueueNeighboursEscape(allNodes, currentTile, currentRow, currentColumn);
        int goldGained = currentTile.getGold();
        q.add(new Path(goldGained, 0, initialPath));

        while (q.size()>0){
            Path newPath = q.poll();
            Node current = newPath.(newPath.size()-1);
            Collection<Node> nbrs = getNeighbours(allNodes, current.getTile(), current.getTile().getRow(), current.getTile().getColumn());
            for(Node nbr : nbrs){
                if(newPath.contains(nbr)){
                    continue; //if we have no where left to visit then the path will be deleted from the queue.
                }
                List<Node> nodePath = new ArrayList<>(newPath);  // create copy of existing path and add neighbour to end.
                if(newPath.contains(state.getExit())){
                    nodePath.add(nbr);
                    finishedRoutes.add(nodePath); //if the neighbour we select is the exit, then this route has finished.
                    continue;
                }
                nodePath.add(nbr);
                q.add(nodePath);
            }
        }
    }











    private void enqueueNeighboursEscape(Collection<Node> allNodes, Tile currentTile, int currentRow, int currentColumn) {

        Collection<Node> nbrs = getNeighbours(allNodes, currentTile, currentRow, currentColumn);


    }

    private Collection<Node> getNeighbours(Collection<Node> allNodes, Tile currentTile, int currentRow, int currentColumn) {

        return allNodes.stream().filter(e -> (e.getTile().getRow() == currentRow - 1 && e.getTile().getColumn() == currentColumn) || (e.getTile().getRow() == currentRow + 1 && e.getTile().getColumn() == currentColumn) || (e.getTile().getColumn() == currentTile.getColumn() - 1 && e.getTile().getRow() == currentRow)
                    || (e.getTile().getColumn() == currentTile.getColumn() + 1 && e.getTile().getRow() == currentRow) && e.getTile().getType().equals(Tile.Type.FLOOR)).collect(Collectors.toList());
    }
}