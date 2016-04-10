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
    Collection<Long> seen = new LinkedHashSet<>();
    Queue<Long> queue = new ArrayDeque<>();

    public void explore(ExplorationState state) {
        exp(state);
    }


    public void exp(ExplorationState state) {
        if (!queue.isEmpty()) {
            long popped = queue.remove();
            state.moveTo(popped);
            seen.add(popped);
        }

        Collection<NodeStatus> nbrs2 = state.getNeighbours();
        Comparator<NodeStatus> byDistance = NodeStatus::compareTo;
        List<NodeStatus> ordered = nbrs2.stream().sorted(byDistance).collect(Collectors.toList());
        for (int i = 0; i < ordered.size(); i++) {
            if (ordered.get(i).getDistanceToTarget()-1 <state.getDistanceToTarget() && !seen.contains(ordered.get(i).getId()))
                queue.add(ordered.get(i).getId());
        }
        if (state.getDistanceToTarget() != 0) {
            exp(state);
        }
    }






        /*Collection<Long> seenide = new LinkedHashSet<>();//seen in deadend list
        Collection<Long> seenStuck = new LinkedHashSet<>();//seen in deadend for stuck list
        int i = 1;
        while (state.getDistanceToTarget() != 0) {
            long currentLocation = state.getCurrentLocation();
            boolean normalpath = false;
            Collection<NodeStatus> nbrs = state.getNeighbours();
            Comparator<NodeStatus> byDistance = NodeStatus::compareTo;
            List<NodeStatus> ordered = nbrs.stream().sorted(byDistance).collect(Collectors.toList());
            int distance = Integer.MAX_VALUE;
            long id = -1L;
            for (NodeStatus anOrdered : ordered) { //normal path
                if (anOrdered.getDistanceToTarget()-1 < distance && !seen.contains(anOrdered.getId())) {
                    id = anOrdered.getId();
                    distance = anOrdered.getDistanceToTarget();
                    seen.add(anOrdered.getId());
                    normalpath = true;
                    break;
                }
            }
            if (normalpath) {
                System.out.println("current location: " + currentLocation);
                System.out.println("moving to location: " + id);
                state.moveTo(id);
                seen.add(currentLocation);
            }
            if (!normalpath) {
                for (NodeStatus anOrdered : ordered) {
                    if (seen.contains(anOrdered.getId())) {
                        id = anOrdered.getId();
                        seen.remove(currentLocation);
                        normalpath = true;
                        state.moveTo(id);
                        break;
                    }
                }*/









            /*if (!normalpath) {
                seenide.add(currentLocation);
                for (NodeStatus anOrdered : ordered) {
                    if (!seenide.contains(anOrdered.getId())) {
                        id = anOrdered.getId();
                    }
                }
                if (id == -1) {
                    System.out.println("finalcountdown");
                    for (NodeStatus anOrdered : ordered) {
                        seenide.clear();
                        seenide.add(currentLocation);
                        if (!seenide.contains(anOrdered.getId())) {
                            id = anOrdered.getId();
                        }
                    }
                }*/
                /*System.out.println("current location alt: " + currentLocation);
                System.out.println("moving to location alt" + id);
                state.moveTo(id);*/

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

            //won't pass -s 631980657864787992
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


    //}
    // }

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
