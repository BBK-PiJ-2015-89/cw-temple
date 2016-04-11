package student;

import game.NodeStatus;

public class MazeNode {

    private Long parent;
    private NodeStatus node;

    public MazeNode(NodeStatus node, Long parent){
        this.parent = parent;
        this.node = node;
    }

    public long getParent(){
        return parent;
    }

    public NodeStatus getNode(){
        return node;
    }
}
