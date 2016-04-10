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
        Stack<Long> path = new Stack<>();

        while (state.getDistanceToTarget() != 0) {

            long currentLocation = state.getCurrentLocation();
            Collection<NodeStatus> nbrs = state.getNeighbours();
            seen.add(currentLocation);
            Comparator<NodeStatus> byDistance = NodeStatus::compareTo;
            List<NodeStatus> ordered = nbrs.stream().sorted(byDistance).collect(Collectors.toList());

            for (NodeStatus node: ordered) { //normal path

                if (/*isCloser(state, node) && */!seen.contains(node.getId())) {
                    move(state, seen, path, currentLocation, node);
                    break;
                }

            }

//            if (currentLocation == state.getCurrentLocation()){
//                for (NodeStatus node: ordered) {
//                    if (!seen.contains(node.getId())) {
//                        move(state, seen, path, currentLocation, node);
//                        break;
//                    }
//
//                }
//            }

            if (currentLocation == state.getCurrentLocation()) {
                state.moveTo(path.pop());
            }

        }
    }

    private void move(ExplorationState state, Collection<Long> seen, Stack<Long> path, long currentLocation, NodeStatus node) {
        long id;
        id = node.getId();

        System.out.println("Moving from: " + state.getCurrentLocation());
        System.out.println("to: " + id);
        seen.add(id);
        state.moveTo(id);
        path.add(currentLocation);
    }

    private boolean isCloser(ExplorationState state, NodeStatus node) {
        return node.getDistanceToTarget() - 1 < state.getDistanceToTarget();
    }




            /*if (!normalpath){
                for (int j = 0; j <ordered.size() ; j++) {
                    seen.remove(ordered.get(j).getId());
                   }

                }*/
    //id = ordered.get(ordered.size()-1).getId();
            /*if (!normalpath){
                seenide.add(currentLocation);
                System.out.println("OFF NORMAL PATH");
                for (int j = 0; j < ordered.size(); j++) {
                    if (ordered.get(j).getDistanceToTarget() < distance && !seenide.contains(ordered.get(j).getId())) {
                        id = ordered.get(j).getId();
                        break;
                }
            }}

            //won't pass -s 631980657864787992 -s 1754181467924199018
            if(id == -1){
                seenStuck.add(currentLocation);
                System.out.println("SAVE");
                for (int j = 0; j < ordered.size(); j++) {
                    if (!seenStuck.contains(ordered.get(j).getId()) && ordered.get(j).getDistanceToTarget() > distance){
                        id = ordered.get(j).getId();
                        break;
                    }
            }}





            if (id == -1 || id == state.getCurrentLocation()){
                List<Long> seenList = new ArrayList<Long>(seen);
                Collections.sort(seenList);
                id = seenList.get(seenList.size()-i);
                System.out.println("value of i = " + i);
                i++;
            }*/


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
