package student;

import game.NodeStatus;

public class MazeNode {

    private MazeNode parent;
    private NodeStatus node;

    public MazeNode(NodeStatus node, MazeNode parent){
        this.parent = parent;
        this.node = node;
    }

    public MazeNode getParent(){
        return parent;
    }

    public NodeStatus getNode(){
        return node;
    }
}
