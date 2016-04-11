package student;

public class MazeNode {

    private Long parent;
    private Long node;

    public MazeNode(long node, long parent){
        this.parent = parent;
        this.node = node;
    }

    public long getParent(){
        return parent;
    }

    public long getNode(){
        return node;
    }
}
