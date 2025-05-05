package domotic;

import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Area;
import jason.environment.grid.Location;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
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
	public static final int CARGADOR = 8192;
	// the grid size
	public static final int GSize = 12; // Cells
	public static final int GridSize = 1080; // Width

	private boolean fridgeOpen = false; // whether the fridge is open
	private boolean cabinetOpen = false; // whether the cabinet is open
	private int carryingDrugs = 0;

	private ArrayList<String> ownerDrugs = new ArrayList<>();

	HashMap<String, Integer> medicamentos = new HashMap<>();
	HashMap<String, Integer> caducidad = new HashMap<>();
	HashMap<String, Integer> deliveredMedicamentos = new HashMap<>();
	private String medMentira = null;

	/*private int deliveredParacetamol = 0;
	private int deliveredIbuprofeno = 0;
	private int deliveredLorazepam = 0;
	private int deliveredAspirina = 0;
	private int deliveredAmoxicilina = 0;*/
	
	private int ownerMove=0;
	private int auxiliarCargar=0;
	
	private List<Integer> bateriaRobots;

	// Initialization of the objects Location on the domotic home scene
	public Location lSofa = new Location(GSize / 2, GSize - 2);
	public Location lChair1 = new Location(GSize / 2 + 2, GSize - 3);
	public Location lChair3 = new Location(GSize / 2 - 1, GSize - 3);
	public Location lChair2 = new Location(GSize / 2 + 1, GSize - 4);
	public Location lChair4 = new Location(GSize / 2, GSize - 4);
	public Location lDeliver = new Location(0, GSize-1);
	public Location lWasher = new Location(GSize / 2, 0);
	public Location lFridge = new Location(3, 0);
	public Location lTable = new Location(GSize / 2, GSize - 3);
	public Location lVacio = new Location(0, 0);
	public Location lBed2 = new Location(GSize + 2, 0);
	public Location lBed3 = new Location(GSize * 2 - 3, 0);
	public Location lBed1 = new Location(GSize + 1, GSize * 3 / 4);
	public Location lCabinet = new Location(10, 0);
	public Location lCargadorRobot = new Location(8, 0);
	

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
	public int getGsize(){
		return GSize;
	}
	
	public List<String> getOwnerDrugs()
	{
		return ownerDrugs;
	}

	public int getCarryingDrugs()
	{
		return carryingDrugs;
	}
	
	public int getAvailableMedicamento(String drug) {		
		return medicamentos.get(drug);
	}

	public void setMedicamentos(String medicamento, int valor)
	{
		medicamentos.put(medicamento, valor);
	}
	public void setDeliveredMedicamentos(String medicamento, int valor)
	{
		deliveredMedicamentos.put(medicamento, valor);
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
	public void setBateriaRobot(int i, int valor)
	{
		bateriaRobots.set(i,valor);
	}
	public void reduceBateriaRobot(int i)
	{
		if(bateriaRobots.get(i)>0)
			bateriaRobots.set(i,bateriaRobots.get(i)-1);		
			
	}


	public void setMedMentira(String medMentira)
	{
		
		this.medMentira=medMentira;
	}

	public String getMedMentira()
	{
		return medMentira;
	}

	public void increaseBateriaRobot(int i)
	{
		int current = bateriaRobots.get(i);
		int maxBattery = 288;
		if (current < maxBattery) {
    		bateriaRobots.set(i, Math.min(current + 10, maxBattery));
		}
	}

	public void setCaducidad(String medicamento, int valor)
	{
		caducidad.put(medicamento, valor);
		System.out.println("Caducidad de " + medicamento + ": " + caducidad.get(medicamento));
	}

	public int getCaducidad(String medicamento)
	{
		return caducidad.get(medicamento);
	}

	public void reduceCaducidad(){
		for (String medicamento : caducidad.keySet()) {
			int valor = caducidad.get(medicamento);
			if (valor > 0) {
				caducidad.put(medicamento, valor - 1);
			}
		}
	}

	public HouseModel() {
		// create a GSize x 2GSize grid with 3 mobile agent
		super(2 * GSize, GSize, 3);
		bateriaRobots=new ArrayList<>();
		
		//mirar porque si pongo estrictamente menor no funciona.
		for(int i=0;i<=2;i++)
		{
			bateriaRobots.add(i);
			//bateriaRobots.set(i,100);
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
		add(CARGADOR, lCargadorRobot);
		add(CARGADOR,lVacio);

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
	}

	boolean closeCabinet() {
		cabinetOpen = false;
		return true;
	}

	boolean canMoveTo(int Ag, int x, int y) {
		if (Ag < 1) {// el agente 0 es la enfermera
			return (isFree(x, y) && !hasObject(WASHER, x, y) && !hasObject(TABLE, x, y) && !hasObject(BED, x, y) &&
					!hasObject(SOFA, x, y) && !hasObject(CABINET, x, y) && !hasObject(CHAIR, x, y) && !hasObject(FRIDGE, x, y));
		} else { // este agente es el owner
			return (isFree(x, y) && !hasObject(WASHER, x, y) && !hasObject(CABINET, x, y) && !hasObject(TABLE, x, y) && !hasObject(FRIDGE, x, y) && !hasObject(DELIVER, x, y));
		}
	}

	public int getOwnerMove() {
		return ownerMove;
	}

	
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

	// moveTowards usando algoritmo A*
	public boolean moveTowards(int Ag, Location dest) {

		AStar path = new AStar(this, Ag, getAgPos(Ag), dest);
		Location nextMove = path.getNextMove();
		//if (nextMove == null) {
		//	System.out.println("No se encontró un camino válido para " + Ag + " hacia " + dest);
		//}
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


	public void reduceAvailableDrug(String drug) {

		medicamentos.put(drug, medicamentos.get(drug)-1);
		if(medicamentos.get(drug) <= 0) {
			System.out.println("Medicina agotada. Rellenando...");
			addDrug(drug,20);
		}
	}

	public boolean takeDrug(int ag, String drug) {
		if (ag == 0) {// la coge la enfermera
			if (cabinetOpen && getAvailableMedicamento(drug) > 0) {
				reduceAvailableDrug(drug);
				carryingDrugs++;
				return true;
			} else {
				if (cabinetOpen) {
					System.out.println("The cabinet is opened. ");
				}
				;
				if (getAvailableMedicamento(drug) > 0) {
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
			if (getAvailableMedicamento(drug) > 0) {
				reduceAvailableDrug(drug);
				return true;
			} else {
				if (cabinetOpen) {
					System.out.println("The cabinet is opened. ");
				}
				;
				if (getAvailableMedicamento(drug) > 0) {
					System.out.println("The fridge has enough drug. ");
				}
				;
				return false;
			}
		}

	}

	public boolean addDrug(String drug, int qtd) {
		medicamentos.put(drug, qtd);
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

	public boolean comprobarConsumo(String drug, int num){
		return getAvailableMedicamento(drug) == num;
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
			case "doorHome":
				dest = lDoorHome;
				break;

			case "cargadorRobot": 
				dest = lCargadorRobot;                  
				break; 
			case "vacio":
				dest = lVacio;                  
				break;
		}
		return dest;
	}






	public boolean deliver(String drug, int qtd) {
		/*switch (drug) {
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
		}*/
		deliveredMedicamentos.put(drug, deliveredMedicamentos.get(drug) + qtd);
		return true;
	}

	public boolean getDelivery(String drug) {
		/*switch (drug) {
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
		}*/
		deliveredMedicamentos.put(drug, 0);
		return true;
	}


}