package domotic;

import java.util.ArrayList;

import jason.environment.grid.Location;

public class AStar {

    final int maxCol;
    final int maxRow;

    Node[][] node;
    Node startNode, goalNode, currentNode;
    ArrayList<Node> openList = new ArrayList<>();
    ArrayList<Node> checkedList = new ArrayList<>();

    ArrayList<Location> path = new ArrayList<>();

    boolean goalReached = false;
    int step = 0;

    HouseModel hmodel;

    public AStar(HouseModel model, int ag, Location inicial, Location dest) {
        maxCol = HouseModel.GSize;
        maxRow = HouseModel.GSize * 2;
        node = new Node[maxCol][maxRow];
        int col = 0;
        int row = 0;
        while (col < maxCol && row < maxRow) {
            node[col][row] = new Node(col, row);
            col++;
            if (col == maxCol) {
                col = 0;
                row++;
            }
        }
        setStartNode(inicial);
        setGoalNode(dest);
        for (int i = 0; i < node.length; i++) {
            for (int j = 0; j < node[i].length; j++) {
                if (!model.canMoveTo(ag, j, i) && !new Location(j, i).equals(inicial)
                        && !new Location(j, i).equals(dest)) {
                    setSolidNode(new Location(j, i));
                }
            }
        }
        setCostOnNodes();
        autoSearch();

    }

    private void setStartNode(Location inicial) {
        node[inicial.y][inicial.x].setAsStart();
        startNode = node[inicial.y][inicial.x];
        currentNode = startNode;
    }

    private void setGoalNode(Location dest) {
        node[dest.y][dest.x].setAsGoal();
        goalNode = node[dest.y][dest.x];
    }

    private void setSolidNode(Location loc) {
        node[loc.y][loc.x].setAsSolid();
    }

    public void setNonSolidNode(Location loc){
        node[loc.y][loc.x].setAsNonSolid();
        setCostOnNodes();
        autoSearch();
    }

    private void setCostOnNodes() {
        int col = 0;
        int row = 0;
        while (col < maxCol && row < maxRow) {
            getCost(node[col][row]);
            col++;
            if (col == maxCol) {
                col = 0;
                row++;
            }
        }
    }

    private void getCost(Node node) {
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);

        node.gCost = xDistance + yDistance;

        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);

        node.hCost = xDistance + yDistance;

        node.fCost = node.gCost + node.hCost;
    }

    public void autoSearch() {
        while (goalReached == false && step < 300) {
            int col = currentNode.col;
            int row = currentNode.row;

            currentNode.setAsChecked();
            checkedList.add(currentNode);
            openList.remove(currentNode);
            if (row - 1 >= 0)
                openNode(node[col][row - 1]);
            if (col - 1 >= 0)
                openNode(node[col - 1][row]);
            if (row + 1 < maxRow)
                openNode(node[col][row + 1]);
            if (col + 1 < maxCol)
                openNode(node[col + 1][row]);
            /* 
            if (row - 1 >= 0 && col - 1 >= 0)
                openNode(node[col - 1][row - 1]);
            if (col - 1 >= 0 && row + 1 < maxRow)
                openNode(node[col - 1][row + 1]);
            if (row + 1 < maxRow && col + 1 < maxCol)
                openNode(node[col + 1][row + 1]);
            if (col + 1 < maxCol && row - 1 >= 0)
                openNode(node[col + 1][row -1]);
            */
            int bestNodeIndex = -1;
            int bestNodefCost = Integer.MAX_VALUE;
            for (int i = 0; i < openList.size(); i++) {
                if (openList.get(i).fCost < bestNodefCost) {
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).fCost;
                } else if (openList.get(i).fCost == bestNodefCost) {
                    if (openList.get(i).gCost < openList.get(bestNodeIndex).gCost) {
                        bestNodeIndex = i;
                        bestNodefCost = openList.get(i).fCost;
                    }
                }
            }
            if (bestNodeIndex == -1)
                return;
            currentNode = openList.get(bestNodeIndex);

            if (currentNode == goalNode) {
                goalReached = true;
                trackPath();
            }

        }
    }

    private void openNode(Node node) {
        if (node.open == false && node.solid == false && node.checked == false) {
            node.setAsOpen();
            node.parent = currentNode;
            openList.add(node);
        }
    }

    private void trackPath() {
        Node currentNode = goalNode;
        path.add(new Location(goalNode.row, goalNode.col));
        while (currentNode != startNode) {
            currentNode = currentNode.parent;
            if (currentNode != startNode) {
                path.add(new Location(currentNode.row, currentNode.col));
            }
        }
    }

    public int getCost(){
        return path.size();
    }

    public Location getNextMove() {
        if (path.size() == 0) {
            return null;
        }
        return path.get(path.size() - 1);
    }
}
