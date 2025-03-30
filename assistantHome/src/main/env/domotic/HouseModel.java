package domotic;

import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Area;
import jason.environment.grid.Location;
import java.util.ArrayList;
import java.util.List;
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
	public static final int WALLV = 2048;
	public static final int CABINET = 4096;

	// the grid size
	public static final int GSize = 12; // Cells
	public final int GridSize = 1080; // Width

	private boolean fridgeOpen = false; // whether the fridge is open
	private boolean cabinetOpen = false; // whether the cabinet is open
	private int carryingDrugs = 0; // numero de medicamentos que lleva el robot
	// estas variables no la necesitamos porque cada medicamento tiene su cantidad
	// disponible en el hashmap

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

	// variable ownerMove
	private int ownerMove=0;
	// esta variable en principio tampoco se necesita a menos que de un medicamento
	// se vaya a tomar varias
	// dosis (no creo)

	// int availableDrugs = 2; // how many drugs are available

	// Initialization of the objects Location on the domotic home scene
	public Location lSofa = new Location(GSize / 2, GSize - 2);
	public Location lChair1 = new Location(GSize / 2 + 2, GSize - 3);
	public Location lChair3 = new Location(GSize / 2 - 1, GSize - 3);
	public Location lChair2 = new Location(GSize / 2 + 1, GSize - 4);
	public Location lChair4 = new Location(GSize / 2, GSize - 4);
	public Location lDeliver = new Location(0, GSize - 1);
	public Location lWasher = new Location(GSize / 3, 0);
	public Location lFridge = new Location(2, 0);
	public Location lTable = new Location(GSize / 2, GSize - 3);
	public Location lBed2 = new Location(GSize + 2, 0);
	public Location lBed3 = new Location(GSize * 2 - 3, 0);
	public Location lBed1 = new Location(GSize + 1, GSize * 3 / 4);
	public Location lCabinet = new Location(10, 0);

	// Initialization of the doors location on the domotic home scene
	public Location lDoorHome = new Location(0, GSize - 1);
	public Location lDoorKit1 = new Location(0, GSize / 2);
	public Location lDoorKit2 = new Location(GSize / 2 + 1, GSize / 2 - 1);
	public Location lDoorSal1 = new Location(GSize / 4, GSize - 1);
	public Location lDoorSal2 = new Location(GSize + 1, GSize / 2);
	public Location lDoorBed1 = new Location(GSize - 1, GSize / 2);
	public Location lDoorBath1 = new Location(GSize - 1, GSize / 4 + 1);
	public Location lDoorBed3 = new Location(GSize * 2 - 1, GSize / 4 + 1);
	public Location lDoorBed2 = new Location(GSize + 1, GSize / 4 + 1);
	public Location lDoorBath2 = new Location(GSize * 2 - 4, GSize / 2 + 1);

	// Initialization of the area modeling the home rooms
	public static Area kitchen = new Area(0, 0, GSize / 2 + 1, GSize / 2 - 1);
	public static Area livingroom = new Area(GSize / 3, GSize / 2 + 1, GSize, GSize - 1);
	public static Area bath1 = new Area(GSize / 2 + 2, 0, GSize - 1, GSize / 3);
	public static Area bath2 = new Area(GSize * 2 - 3, GSize / 2 + 1, GSize * 2 - 1, GSize - 1);
	public static Area bedroom1 = new Area(GSize + 1, GSize / 2 + 1, GSize * 2 - 4, GSize - 1);
	public static Area bedroom2 = new Area(GSize, 0, GSize * 3 / 4 - 1, GSize / 3);
	public static Area bedroom3 = new Area(GSize * 3 / 4, 0, GSize * 2 - 1, GSize / 3);
	public static Area hall = new Area(0, GSize / 2 + 1, GSize / 4, GSize - 1);
	public static Area hallway = new Area(GSize / 2 + 2, GSize / 2 - 1, GSize * 2 - 1, GSize / 2);

	
	public List<String> getOwnerDrugs() {
		return ownerDrugs;
	}

	public int getAvailableParacetamol() {
		return availableParacetamol;
	}

	public int getAvailableIbuprofeno() {
		return availableIbuprofeno;
	}

	public int getAvailableLorazepam() {
		return availableLorazepam;
	}

	public int getAvailableAspirina() {
		return availableAspirina;
	}

	public int getAvailableAmoxicilina() {
		return availableAmoxicilina;
	}

	public int getCarryingDrugs() {
		return carryingDrugs;
	}

	/*
	 * Modificar el modelo para que la casa sea un conjunto de habitaciones
	 * Dar un codigo a cada habitación y vincular un Area a cada habitación
	 * Identificar los objetos de manera local a la habitación en que estén
	 * Crear un método para la identificación del tipo de agente existente
	 * Identificar objetos globales que precisen de un único identificador
	 */

	public HouseModel() {
		// create a GSize x 2GSize grid with 3 mobile agent
		super(2 * GSize, GSize, 2);

		// initial location of robot (column 3, line 3)
		// ag code 0 means the robot

		// Deberia haber una estacion de carga del robot, es esta?
		setAgPos(0, 19, 10);
		setAgPos(1, 23, 8);
		// setAgPos(2, GSize*2-1, GSize*3/5);

		// Do a new method to create literals for each object placed on
		// the model indicating their nature to inform agents their existence

		// initial location of fridge and owner
		add(FRIDGE, lFridge);
		add(WASHER, lWasher);
		add(DOOR, lDeliver);
		add(SOFA, lSofa);
		add(CHAIR, lChair2);
		add(CHAIR, lChair3);
		add(CHAIR, lChair4);
		add(CHAIR, lChair1);
		add(TABLE, lTable);
		add(BED, lBed1);
		add(BED, lBed2);
		add(BED, lBed3);
		add(CABINET, lCabinet);

		addWall(GSize / 2 + 1, 0, GSize / 2 + 1, GSize / 2 - 2);
		add(DOOR, lDoorKit2);
		// addWall(GSize/2+1, GSize/2-1, GSize/2+1, GSize-2);
		add(DOOR, lDoorSal1);

		addWall(GSize / 2 + 1, GSize / 4 + 1, GSize - 2, GSize / 4 + 1);
		// addWall(GSize+1, GSize/4+1, GSize*2-1, GSize/4+1);
		add(DOOR, lDoorBath1);
		// addWall(GSize+1, GSize*2/5+1, GSize*2-2, GSize*2/5+1);
		addWall(GSize + 2, GSize / 4 + 1, GSize * 2 - 2, GSize / 4 + 1);
		addWall(GSize * 2 - 6, 0, GSize * 2 - 6, GSize / 4);
		add(DOOR, lDoorBed1);

		addWall(GSize, 0, GSize, GSize / 4 + 1);
		// addWall(GSize+1, GSize/4+1, GSize, GSize/4+1);
		add(DOOR, lDoorBed2);

		addWall(1, GSize / 2, GSize - 1, GSize / 2);
		add(DOOR, lDoorKit1);
		add(DOOR, lDoorSal2);

		addWall(GSize / 4, GSize / 2 + 1, GSize / 4, GSize - 2);

		addWall(GSize, GSize / 2, GSize, GSize - 1);
		addWall(GSize * 2 - 4, GSize / 2 + 2, GSize * 2 - 4, GSize - 1);
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

	// tienen getRoomCenter no se porque

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
		if (Ag < 1) {// el agente 0 es la enfermera
			return (isFree(x, y) && !hasObject(WASHER, x, y) && !hasObject(TABLE, x, y) && !hasObject(BED, x, y) &&
					!hasObject(SOFA, x, y) && !hasObject(CABINET, x, y) && !hasObject(CHAIR, x, y) && !hasObject(FRIDGE, x, y));
		} else { // este agente es el paciente
			return (isFree(x, y) && !hasObject(WASHER, x, y) && !hasObject(CABINET, x, y) && !hasObject(TABLE, x, y) && !hasObject(FRIDGE, x, y));
		}
	}

	// metodo getOwnerMove()
	public int getOwnerMove() {
		return ownerMove;
	}

	// metodo calclaulateOwnerDir
	public void calculateOwnerDir(int Ag, Location dest) {
		if (Ag == 1) {
			Location ag = getAgPos(Ag);
			if (ag.x + 1 == dest.x) {
				ownerMove = 1;
			} else if (ag.x - 1 == dest.x) {
				ownerMove = 3;
			} else if (ag.y + 1 == dest.y) {
				ownerMove = 2;
			} else {
				ownerMove = 0;
			}
		}
	}

	// moveTowards usando Astar
	public boolean moveTowards(int Ag, Location dest) {

		AStar path = new AStar(this, Ag, getAgPos(Ag), dest);
		Location nextMove = path.getNextMove();
		if (nextMove != null) {
			calculateOwnerDir(Ag, nextMove);
			setAgPos(Ag, nextMove);
			return true;
		} else {
			for (int i = 0; i < agPos.length; i++) {
				if (i != Ag) {
					path.setNonSolidNode(getAgPos(i));
				}
			}
			nextMove = path.getNextMove();
			if (nextMove != null && isFree(nextMove)) {
				calculateOwnerDir(Ag, nextMove);
				setAgPos(Ag, nextMove);
				return true;
			}
		}
		return false;
	}

	public boolean apartar(int Ag) {
		Location loc = getAgPos(Ag);
		ArrayList<Location> dirs = new ArrayList<>();
		boolean aux;
		aux = canMoveTo(Ag, loc.x, loc.y - 1) ? dirs.add(new Location(loc.x, loc.y - 1)) : false;
		aux = canMoveTo(Ag, loc.x, loc.y + 1) ? dirs.add(new Location(loc.x, loc.y + 1)) : false;
		aux = canMoveTo(Ag, loc.x - 1, loc.y) ? dirs.add(new Location(loc.x - 1, loc.y)) : false;
		aux = canMoveTo(Ag, loc.x + 1, loc.y) ? dirs.add(new Location(loc.x + 1, loc.y)) : false;

		if (dirs.size() > 0) {
			int random = (int) (Math.random() * dirs.size());
			setAgPos(Ag, dirs.get(random));
		}
		return dirs.size() > 0;
	}

	int getAvailableDrug(String drug) {
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

	public void reduceAvailableDrug(String drug) {
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
		if (ag == 0) {// la coge la enfermera
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
		} else {// la coge el owner
			if (getAvailableDrug(drug) > 0) {
				reduceAvailableDrug(drug);
				return true;
			} else {
				if (cabinetOpen) {
					System.out.println("The cabinet is opened. ");
				}
				;
				if (getAvailableDrug(drug) > 0) {
					System.out.println("The fridge has enough drug. ");
				}
				;
				return false;
			}
		}

	}

	public boolean addDrug(String drug, int qtd) {
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

	boolean handDrug(int ag) {
		if (ag == 0) {
			if (carryingDrugs > 0) {
				carryingDrugs--;
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	public Location getLocation(String loc) {
		Location dest = null;
		switch (loc) {
			case "fridge":
				dest = lFridge;
				break;
			case "cabinet": 
				dest = lCabinet; 
				break;
			case "robot":
				dest = getAgPos(0);
				break;
			case "owner":
				dest = getAgPos(1);
				break;
			case "delivery":
				dest = lDeliver;
				break;
			case "chair1":
				dest = lChair1;
				break;
			case "chair2":
				dest = lChair2;
				break;
			case "chair3":
				dest = lChair3;
				break;
			case "chair4":
				dest = lChair4;
				break;
			case "bed1":
				dest = lBed1;
				break;
			case "bed2":
				dest = lBed2;
				break;
			case "bed3":
				dest = lBed3;
				break;
			case "sofa":
				dest = lSofa;
				break;
			case "washer":
				dest = lWasher;
				break;
			case "table":
				dest = lTable;
				break;
			case "doorBed1":
				dest = lDoorBed1;
				break;
			case "doorBed2":
				dest = lDoorBed2;
				break;
			case "doorBed3":
				dest = lDoorBed3;
				break;
			case "doorKit1":
				dest = lDoorKit1;
				break;
			case "doorKit2":
				dest = lDoorKit2;
				break;
			case "doorSal1":
				dest = lDoorSal1;
				break;
			case "doorSal2":
				dest = lDoorSal2;
				break;
			case "doorBath1":
				dest = lDoorBath1;
				break;
			case "doorBath2":
				dest = lDoorBath2;
				break;
		}
		return dest;
	}

}