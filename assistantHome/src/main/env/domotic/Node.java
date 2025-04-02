package domotic;

public class Node {
    Node parent;
    int col;
    int row;
    int gCost; //Coste de movimiento
    int hCost; //Coste heuristico
    int fCost; //Coste total (gCost + hCost)
    boolean start; //Nodo inicial
    boolean goal; //Nodo objetivo
    boolean solid; //Nodo solido (no transitable)
    boolean open; //Nodo abierto (en la lista de nodos abiertos)
    boolean checked; //Nodo revisado (en la lista de nodos revisados)

    public Node(int col, int row){
        this.col = col;
        this.row = row;
    }

    //Metodos para definir el estado de un nodo (veanse los comentarios anteriores)
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

