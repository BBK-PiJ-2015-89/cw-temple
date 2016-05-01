package student;

import game.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * A path object that allows a path of the man to be stored to ensure we can retrace it. All paths will start with the same tiles, like a file system
 * therefore, to go from one tile to another which is not a neighbour we can use two paths and find where they meet, and go back to that place on the current
 * path and then follow the new path to the new node. Every time a node is seen its path is added to the list.
 */
public class Path {

    private int goldCount;
    private int timeTaken;
    private List<Node> path = new ArrayList<>();

    public Path(int gold, int time, List<Node> path){
        goldCount = gold; //paths total gold count
        timeTaken = time; //total time it took to traverse this path to (edge weights added together)
        this.path = path;
    }


    public int getGoldCount() {
        return goldCount;
    }

    public int getTimeTaken() {
        return timeTaken;
    }

    public List<Node> getPath() {
        return path;
    }

    public void setGoldCount(int goldCount) {
        this.goldCount = goldCount;
    }

    public void setTimeTaken(int timeTaken) {
        this.timeTaken = timeTaken;
    }

    @Override
    public String toString() {
        return path.toString();
    }
}

