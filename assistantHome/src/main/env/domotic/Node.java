package domotic;

public class Node {
    Node parent;
    int col;
    int row;
    int gCost;
    int hCost;
    int fCost;
    boolean start;
    boolean goal;
    boolean solid;
    boolean open;
    boolean checked;

    public Node(int col, int row){
        this.col = col;
        this.row = row;
    }

    public void setAsStart(){
        start = true;
    }

    public void setAsGoal(){
        goal = true;
    }

    public void setAsSolid(){
        solid = true;
    }

    public void setAsNonSolid(){
        solid = false;
    }

    public void setAsOpen(){
        open = true;
    }

    public void setAsChecked(){
        checked = true;
    }


    @Override
    public String toString() {
        return "[" + row + "," + col + "]";
    }
}

