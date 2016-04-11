package student;

import game.EscapeState;
import game.ExplorationState;
import game.NodeStatus;

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
        Collection<Long> seen = new LinkedHashSet<>();
        HashMap<Long, MazeNode> mazeNodes = new HashMap<>();
        Queue<Long> q = new ArrayDeque<>();

        q.add(state.getCurrentLocation());
        seen.add(state.getCurrentLocation());
        System.out.println("Start");


        while (state.getDistanceToTarget() != 0) {

            long currentLocation = state.getCurrentLocation();
            Collection<NodeStatus> nbrs = state.getNeighbours();

            seen.add(currentLocation);


            Comparator<NodeStatus> byDistance = NodeStatus::compareTo;

            List<NodeStatus> ordered = nbrs.stream().sorted(byDistance).collect(Collectors.toList());

            //normal q
/*if (!seen.contains(node.getId())) {
    move(state, seen, q, currentLocation, node);
    break;
}*//*if (currentLocation == state.getCurrentLocation()) {
    state.moveTo(q.pop());
}*/

            for (NodeStatus node : ordered) { //normal q
                    q.add(node.getId());
                    mazeNodes.put(node.getId(), new MazeNode(node, currentLocation));
            }
                Long next = q.poll();
                move(state, currentLocation, next, mazeNodes);
            }
        }

    private void move(ExplorationState state, long currentLocation, Long next, HashMap<Long, MazeNode> mazeNodes) {
        if(currentLocation == next) {
            return;
        }
        //List<NodeStatus> tempNbrs = state.getNeighbours().stream().filter(e -> e.getId()==next).collect(Collectors.toList());
        if(mazeNodes.get(next).getParent()==currentLocation){
            //System.out.println("path = " + path);
            System.out.println("Move from " + currentLocation + " to " + next);
            //path.add(currentLocation);
            System.out.println("MOVE TO NBR");
            state.moveTo(next);
            return;
        }
        MazeNode moveFrom = mazeNodes.get(currentLocation);
        MazeNode moveTo = mazeNodes.get(next);
        if(moveTo.getParent()==moveFrom.getParent()){
                state.moveTo(moveTo.getParent());
                move(state, state.getCurrentLocation(), next, mazeNodes);
            }else{
            List<Long> path = new ArrayList<>();
            retraceBack(state, mazeNodes, moveFrom, moveTo, path, next);
        }
        /*System.out.println("path = " + path);
        System.out.println("Move from " + currentLocation + " to " + (path.get(path.size()-1)));
        System.out.println("RETRACING 1");
        state.moveTo(path.get(path.size() - 2));
        System.out.println("RETRACING 2");
        path.remove(path.size()-1);
        move(state, state.getCurrentLocation(), next, path);*/

    }

    private void retraceBack(ExplorationState state, HashMap<Long, MazeNode> mazeNodes, MazeNode moveFrom, MazeNode moveTo, List<Long> path, Long next) {
        if (moveTo.getParent() != moveFrom.getParent()) {
            state.moveTo(moveFrom.getParent());
            path.add(moveTo.getParent());
            moveTo = mazeNodes.get(moveTo.getParent());
            moveFrom = mazeNodes.get(state.getCurrentLocation());
            retraceBack(state, mazeNodes, moveFrom, moveTo, path, next);
        }else{
            state.moveTo(moveFrom.getParent());
            move(state, state.getCurrentLocation(), next, mazeNodes);
        }
    }

  /*  private void move(ExplorationState state, Collection<Long> seen, Stack<Long> path, long currentLocation, NodeStatus node) {
        long id;
        id = node.getId();

        System.out.println("Moving from: " + state.getCurrentLocation());
        System.out.println("to: " + id);
        seen.add(id);
        state.moveTo(id);
        path.add(currentLocation);
    }
*/

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
    }
}
