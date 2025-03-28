import jason.environment.grid.GridWorldModel;

import java.util.ArrayList;
import java.util.List;
import jason.environment.grid.Area;
import jason.environment.grid.Location;

//import jason.asSyntax.*;

/** class that implements the Model of Domestic Robot application */
public class HouseModel extends GridWorldModel {

	// constants for the grid objects
	public static final int COLUMN = 4;
	public static final int CHAIR = 8;
	public static final int SOFA = 16;
	public static final int FRIDGE = 32;
	public static final int WASHER = 64;
	public static final int DOOR = 128;
	public static final int CHARGER = 256;
	public static final int TABLE = 512;
	public static final int BED = 1024;
	public static final int DELIVER = 2048;
	public static final int CABINET = 4096;
	public static final int RETRETE = 8192;
	// the grid size
	public static final int GSize = 12; // Cells
	public static final int GridSize = 1080; // Width

	private boolean fridgeOpen = false; // whether the fridge is open
	private boolean cabinetOpen = false; // whether the cabinet is open
	private int carryingDrugs = 0;

	private ArrayList<String> ownerDrugs = new ArrayList<>();

	private int availableParacetamol = 20;
	private int availableIbuprofeno = 20;
	private int availableLorazepam = 20;
	private int availableAspirina = 20;
	private int availableAmoxicilina = 20;

	private int deliveredParacetamol = 0;
	private int deliveredIbuprofeno = 0;
	private int deliveredLorazepam = 0;
	private int deliveredAspirina = 0;
	private int deliveredAmoxicilina = 0;
	
	private int ownerMove=0;
	private int auxiliarCargar=0;
	
	private List<Integer> bateriaRobots;

	// Initialization of the objects Location on the domotic home scene
	public Location lSofa = new Location(GSize / 2, GSize - 2);
	public Location lChair1 = new Location(GSize / 2 + 2, GSize - 3);
	public Location lChair3 = new Location(GSize / 2 - 1, GSize - 3);
	public Location lChair2 = new Location(GSize / 2 + 1, GSize - 4);
	public Location lChair4 = new Location(GSize / 2, GSize - 4);
	public Location lDeliver = new Location(0, 10);
	public Location lWasher = new Location(GSize / 2, 0);
	public Location lFridge = new Location(3, 0);
	public Location lTable = new Location(GSize / 2, GSize - 3);
	public Location lVacio = new Location(GSize / 2 + 1, GSize - 3);
	public Location lBed2 = new Location(GSize + 2, 0);
	public Location lBed3 = new Location(GSize * 2 - 3, 0);
	public Location lBed1 = new Location(GSize + 1, GSize * 3 / 4);
	public Location lCabinet = new Location(10, 0);
	public Location lRetrete = new Location(23, 7);
	public Location lCargadorRobot = new Location(8, 0);
	public Location lCargadorAuxiliar = new Location(2,7);

	// Initialization of the doors location on the domotic home scene
	public Location lDoorHome = new Location(0, GSize - 1);
	public Location lDoorKit1 = new Location(0, GSize / 2);
	public Location lDoorKit2 = new Location(GSize / 2 + 1, GSize / 2 - 1);
	public Location lDoorSal1 = new Location(GSize / 4, GSize - 1);
	public Location lDoorSal2 = new Location(GSize / 2 + 2, GSize / 2);
	public Location lDoorBed1 = new Location(GSize + 1, GSize / 2);
	public Location lDoorBath1 = new Location(GSize - 1, GSize / 4 + 1);
	public Location lDoorBed3 = new Location(GSize * 2 - 1, GSize / 4 + 1);
	public Location lDoorBed2 = new Location(GSize + 1, GSize / 4 + 1);
	public Location lDoorBath2 = new Location(GSize * 2 - 4, GSize / 2 + 1);

	// Initialization of the area modeling the home rooms
	public static final Area kitchen = new Area(-1, -1, 7, 6);
    public static final Area livingroom = new Area(3, 6, 12, 11);
    public static final Area bath1 = new Area(7, 0, 12, 4);
    public static final Area bath2 = new Area(20, 6, 23, 12);
    public static final Area bedroom1 = new Area(12, 6, 20, 11);
    public static final Area bedroom2 = new Area(12, 0, 18, 4);
    public static final Area bedroom3 = new Area(18, 0, 23, 4);
    public static final Area hall = new Area(0, 6, 3, 11);
    public static final Area hallway = new Area(7, 4, 24, 6);
	/*
	 * Modificar el modelo para que la casa sea un conjunto de habitaciones
	 * Dar un codigo a cada habitación y vincular un Area a cada habitación
	 * Identificar los objetos de manera local a la habitación en que estén
	 * Crear un método para la identificación del tipo de agente existente
	 * Identificar objetos globales que precisen de un único identificador
	 */
	public List<String> getOwnerDrugs()
	{
		return ownerDrugs;
	}
	public int getAvailableParacetamol()
	{
		return availableParacetamol;
	}
	public int getAvailableIbuprofeno()
	{
		return availableIbuprofeno;
	}
	public int getAvailableLorazepam()
	{
		return availableLorazepam;
	}
	public int getAvailableAspirina()
	{
		return availableAspirina;
	}
	public int getAvailableAmoxicilina()
	{
		return availableAmoxicilina;
	}
	public int getAuxiliarCargar()
	{
		return auxiliarCargar;
	}
	public void setAuxiliarCargar(int valor)
	{
		auxiliarCargar=valor;
	}
	public int getBateriaRobot(int i)
	{
		return bateriaRobots.get(i);
	}
	public void setBateriaRobot(String ag,int valor)
	{
		if(ag.equals("robot"))
			bateriaRobots.set(0,valor);
		else if(ag.equals("auxiliar"))
			bateriaRobots.set(2,valor);
	}
	public void reduceBateriaRobot(int i)
	{
		if(bateriaRobots.get(i)>0)
			bateriaRobots.set(i,bateriaRobots.get(i)-1);
	}

	public void increaseBateriaRobot(int i)
	{
		if(bateriaRobots.get(i)>0)
			bateriaRobots.set(i,bateriaRobots.get(i)+10 >= 200 ? 200 : bateriaRobots.get(i)+10);
	}
	public HouseModel() {
		// create a GSize x 2GSize grid with 3 mobile agent
		super(2 * GSize, GSize, 3);
		bateriaRobots=new ArrayList<>();
		for(int i=0;i<=2;i++)
		{
			bateriaRobots.add(0);
		}
		// initial location of robot (column 3, line 3)
		// ag code 0 means the robot
		// setAgPos(0, 2, 4);
		setAgPos(0, 8, 1);
		setAgPos(1, 5, 9);
		setAgPos(2, 0, 0);
		// setAgPos(1, 4, 4);
		// setAgPos(2, GSize*2-1, GSize*3/5);

		// Do a new method to create literals for each object placed on
		// the model indicating their nature to inform agents their existence

		// initial location of fridge and owner
		add(FRIDGE, lFridge);
		add(WASHER, lWasher);
		add(DELIVER, lDeliver);
		add(SOFA, lSofa);
		add(CHAIR, lChair2);
		add(CHAIR, lChair3);
		add(CHAIR, lChair4);
		add(CHAIR, lChair1);
		add(TABLE, lTable);
		add(BED, lBed1);
		add(BED, lBed2);
		add(BED, lBed3);
		add(DELIVER, lDeliver);
		add(CABINET, lCabinet);
		add(RETRETE, lRetrete);
		add(RETRETE, lCargadorAuxiliar);
		add(RETRETE, lCargadorRobot);
		//add(VACIO,lVacio);

		addWall(GSize / 2 + 1, 0, GSize / 2 + 1, GSize / 2 - 2);
        add(DOOR, lDoorKit2);
        // addWall(GSize/2+1, GSize/2-1, GSize/2+1, GSize-2);
        add(DOOR, lDoorSal1);

        addWall(GSize / 2 + 1, GSize / 4 + 1, GSize - 2, GSize / 4 + 1);
        // addWall(GSize+1, GSize/4+1, GSize2-1, GSize/4+1);
        add(DOOR, lDoorBath1);
        // addWall(GSize+1, GSize2/5+1, GSize2-2, GSize2/5+1);
        addWall(GSize + 2, GSize / 4 + 1, GSize * 2 - 2, GSize / 4 + 1);
        addWall(GSize * 2 - 6, 0, GSize * 2 - 6, GSize / 4);
        add(DOOR, lDoorBed1);
		
        addWall(GSize, 0, GSize, GSize / 4 + 1);
        // addWall(GSize+1, GSize/4+1, GSize, GSize/4+1);
        add(DOOR, lDoorBed2);

        addWall(1, GSize / 2, GSize / 2 + 1, GSize / 2);
        add(DOOR, lDoorKit1); 
        add(DOOR, lDoorSal2); 

        addWall(GSize / 4, GSize / 2 + 1, GSize / 4, GSize - 2);

        addWall(GSize, GSize / 2, GSize, GSize - 1);
        addWall(GSize * 2 - 4, GSize / 2 + 2, GSize * 2 - 4, GSize - 1);
        addWall(GSize / 2 + 3, GSize / 2, GSize, GSize / 2);
        addWall(GSize + 2, GSize / 2, GSize * 2 - 1, GSize / 2);
        add(DOOR, lDoorBed3);
        add(DOOR, lDoorBath2);

	}
	public String getRoom(Location thing) {

		String byDefault = "kitchen";

		if (bath1.contains(thing)) {
			byDefault = "bath1";
		}
		;
		if (bath2.contains(thing)) {
			byDefault = "bath2";
		}
		;
		if (bedroom1.contains(thing)) {
			byDefault = "bedroom1";
		}
		;
		if (bedroom2.contains(thing)) {
			byDefault = "bedroom2";
		}
		;
		if (bedroom3.contains(thing)) {
			byDefault = "bedroom3";
		}
		;
		if (hallway.contains(thing)) {
			byDefault = "hallway";
		}
		;
		if (livingroom.contains(thing)) {
			byDefault = "livingroom";
		}
		;
		if (hall.contains(thing)) {
			byDefault = "hall";
		}
		;
		return byDefault;
	}
	Location getRoomCenter(String thing)
	{
		Location toret = kitchen.center();

		if (thing.equals("bath1")) {
			 toret =bath1.center();
		}
		;
		if (thing.equals("bath2")) {
			toret = bath2.center();
		}
		;
		if (thing.equals("bedroom1")) {
			 toret =bedroom1.center();
		}
		;
		if (thing.equals("bedroom2")) {
			toret =bedroom2.center();
		}
		;
		if (thing.equals("bedroom3")) {
			toret =bedroom3.center();    
		}
		;
		if (thing.equals("hallway")) {
			toret =hallway.center();
		}
		;
		if (thing.equals("livingroom")) {
			toret =livingroom.center();
		}
		;
		if (thing.equals("hall")) {
			toret =hall.center();
		}
		;
		return toret;
	}
	boolean sit(int Ag, Location dest) {
		Location loc = getAgPos(Ag);
		if (loc.isNeigbour(dest)) {
			setAgPos(Ag, dest);
		}
		;
		return true;
	}

	boolean openFridge() {
		if (!fridgeOpen) {
			fridgeOpen = true;
			return true;
		} else {
			return false;
		}
	}

	boolean closeFridge() {
		if (fridgeOpen) {
			fridgeOpen = false;
			return true;
		} else {
			return false;
		}
	}

	boolean openCabinet() {
		cabinetOpen = true;
		return true;
		// if (!cabinetOpen) {
		// 	cabinetOpen = true;
		// 	return true;
		// } else {
		// 	return true;
		// }
	}

	boolean closeCabinet() {
		cabinetOpen = false;
		return true;
		// if (cabinetOpen) {
		// 	cabinetOpen = false;
		// 	return true;
		// } else {
		// 	return false;
		// }
	}

	boolean canMoveTo(int Ag, int x, int y) {
		if(!(x<0 || x>=24 || y<0 || y>=12))
		{
			if(!(x==7 && y==9))
			{
				if (Ag < 1) {
					return (isFree(x, y) && !hasObject(WASHER, x, y) && !hasObject(TABLE, x, y) &&
							!hasObject(SOFA, x, y) && !hasObject(CHAIR, x, y) && !hasObject(FRIDGE, x, y) && !hasObject(BED, x, y)
							&& !hasObject(DELIVER, x, y) && !hasObject(CABINET, x, y)  &&!hasObject(CHAIR,x,y));
				} else {
					return (isFree(x, y) && !hasObject(WASHER, x, y) && !hasObject(TABLE, x, y) && !hasObject(FRIDGE, x, y)
							&& !hasObject(DELIVER, x, y) && !hasObject(CABINET, x, y));
				}
			}
		}
		
		return false;
	}
	public int getOwnerMove()
	{
		return ownerMove;
	}
	public void calculateOwnerDir(int Ag,Location dest)
	{
		if(Ag==1)
		{
			Location ag= getAgPos(Ag);
			if(ag.x+1==dest.x)
			{
				ownerMove=1;
			}
			else if(ag.x-1==dest.x)
			{
				ownerMove=3;
			}
			else if(ag.y+1==dest.y)
			{
				ownerMove=2;
			}
			else
			{
				ownerMove=0;
			}
		}
	}
	public boolean moveTowards(int Ag, Location dest) {
        
		AStar path = new AStar(this, Ag, getAgPos(Ag), dest);
		Location nextMove = path.getNextMove();
		if(nextMove != null)
		{
			calculateOwnerDir(Ag,nextMove);
			setAgPos(Ag, nextMove);
			return true;
		}else{
			for (int i = 0; i < agPos.length; i++) {
				if(i != Ag){
					path.setNonSolidNode(getAgPos(i));
				}
			}
			nextMove = path.getNextMove();
			if(nextMove != null && isFree(nextMove))
			{
				calculateOwnerDir(Ag,nextMove);
				setAgPos(Ag, nextMove);
				return true;
			}
		}
        return false;    
    }

	public boolean apartar(int Ag){
		Location loc = getAgPos(Ag);
		ArrayList<Location> dirs = new ArrayList<>();	
		boolean aux;
		aux = canMoveTo(Ag, loc.x, loc.y-1) ? dirs.add(new Location(loc.x, loc.y - 1)) : false;
		aux = canMoveTo(Ag, loc.x, loc.y + 1) ? dirs.add(new Location(loc.x, loc.y + 1)) : false;
		aux = canMoveTo(Ag, loc.x - 1, loc.y) ? dirs.add(new Location(loc.x - 1, loc.y)) : false;
		aux = canMoveTo(Ag, loc.x + 1, loc.y) ? dirs.add(new Location(loc.x + 1, loc.y)) : false;


		if(dirs.size() > 0){
			int random = (int) (Math.random() * dirs.size());
			setAgPos(Ag, dirs.get(random));
		}
		return dirs.size() > 0;
	}

	int getAvailableDrug(String drug){
		int toRet = 0;
		switch (drug) {
			case "paracetamol":
				toRet = availableParacetamol;
				break;
			case "ibuprofeno":
				toRet = availableIbuprofeno;
				break;
			case "lorazepam":
				toRet = availableParacetamol;
				break;
			case "aspirina":
				toRet = availableAspirina;
				break;
			case "amoxicilina":
				toRet = availableAmoxicilina;
				break;
			default:
				break;
		}
		return toRet;
	}

	public void reduceAvailableDrug(String drug){
		switch (drug) {
			case "paracetamol":
				availableParacetamol--;
				break;
			case "ibuprofeno":
				availableIbuprofeno--;
				break;
			case "lorazepam":
				availableParacetamol--;
				break;
			case "aspirina":
				availableAspirina--;
				break;
			case "amoxicilina":
				availableAmoxicilina--;
				break;
			default:
				break;
		}
	}

	public boolean takeDrug(int ag, String drug) {
		if(ag == 0 || ag==2){
			if (cabinetOpen && getAvailableDrug(drug) > 0) {
				reduceAvailableDrug(drug);
				carryingDrugs++;
				return true;
			} else {
				if (cabinetOpen) {
					System.out.println("The cabinet is opened. ");
				}
				;
				if (getAvailableDrug(drug) > 0) {
					System.out.println("The cabinet has enough drug. ");
				}
				;
				if (carryingDrugs == 0) {
					System.out.println("The robot is not carrying a Drug. ");
				}
				;
				return false;
			}
		}else{
			if (getAvailableDrug(drug) > 0){
				reduceAvailableDrug(drug);
				return true;
			}else {
				if (cabinetOpen) {
					System.out.println("The cabinet is opened. ");
				}
				;
				if (getAvailableDrug(drug) > 0) {
					System.out.println("The cabinet has enough drug. ");
				}
				;
				return false;
			}
		}
		
	}



	public boolean handDrug(int ag) {
		if(ag == 0){
			if (carryingDrugs > 0) {
				carryingDrugs--;
				return true;
			} else {
				return false;
			}
		}else{
			return true;
		}
	}

	public boolean comprobarConsumo(String drug, int num){
		return getAvailableDrug(drug) == num - 1;
	}

	public Location getLocation(String loc){
		Location dest = null;
		switch (loc) {
			case "fridge": dest = lFridge; 
			break;
			case "cabinet": dest = lCabinet; 
			break;
			case "robot": dest = getAgPos(0);  
			break;
			case "auxiliar": dest = getAgPos(2);  
			break;    
			case "owner": dest = getAgPos(1);  
			break;
			case "delivery": dest = lDeliver;  
			break;     
			case "chair1": dest = lChair1; 
			break;
			case "chair2": dest = lChair2; 
			break;
			case "chair3": dest = lChair3; 
			break;
			case "chair4": dest = lChair4; 
			break;
			case "bed1": dest = lBed1; 
			break;
			case "bed2": dest = lBed2; 
			break;
			case "bed3": dest = lBed3; 
			break;
			case "sofa": dest = lSofa; 
			break;
			case "washer": dest = lWasher; 
			break;
			case "table": dest = lTable; 
			break;
			case "doorBed1": dest = lDoorBed1; 
			break;            
			case "doorBed2": dest = lDoorBed2; 
			break;
			case "doorBed3": dest = lDoorBed3; 
			break;
			case "doorKit1": dest = lDoorKit1; 
			break;
			case "doorKit2": dest = lDoorKit2; 
			break;
			case "doorSal1": dest = lDoorSal1; 
			break;
			case "doorSal2": dest = lDoorSal2; 
			break;
			case "doorBath1": dest = lDoorBath1; 
			break;
			case "doorBath2": dest = lDoorBath2;                  
			break;
			case "retrete": dest = lRetrete;                  
			break; 
			case "cargadorRobot": dest = lCargadorRobot;                  
			break; 
			case "cargadorAuxiliar": dest = lCargadorAuxiliar;                  
			break; 
	}
	return dest;
}
	public boolean deliver(String drug, int qtd) {
		switch (drug) {
			case "paracetamol":
				deliveredParacetamol += qtd;
				break;
			case "ibuprofeno":
				deliveredIbuprofeno += qtd;
				break;
			case "lorazepam":
				deliveredLorazepam += qtd;
				break;
			case "aspirina":
				deliveredAspirina += qtd;
				break;
			case "amoxicilina":
				deliveredAmoxicilina += qtd;
				break;
			default:
				break;
		}
		return true;
	}

	public boolean getDelivery(String drug) {
		switch (drug) {
			case "paracetamol":
				deliveredParacetamol = 0;
				break;
			case "ibuprofeno":
				deliveredIbuprofeno = 0;
				break;
			case "lorazepam":
				deliveredLorazepam = 0;
				break;
			case "aspirina":
				deliveredAspirina = 0;
				break;
			case "amoxicilina":
				deliveredAmoxicilina = 0;
				break;
			default:
				break;
		}
		return true;
	}

	public boolean addDrug(String drug, int qtd){
		switch (drug) {
			case "paracetamol":
				availableParacetamol = qtd;
				break;
			case "ibuprofeno":
				availableIbuprofeno = qtd;
				break;
			case "lorazepam":
				availableLorazepam = qtd;
				break;
			case "aspirina":
				availableAspirina = qtd;
				break;
			case "amoxicilina":
				availableAmoxicilina = qtd;
				break;
			default:
				break;
		}
		return true;
	}
}
