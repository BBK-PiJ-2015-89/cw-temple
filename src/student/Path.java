package student;

import game.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by graemewilkinson on 16/04/16.
 */
public class Path {

    int goldCount;
    int timeTaken;
    List<Node> path = new ArrayList<>();

    public Path(int gold, int time, List<Node> path){
        goldCount = gold;
        timeTaken = time;
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
}
