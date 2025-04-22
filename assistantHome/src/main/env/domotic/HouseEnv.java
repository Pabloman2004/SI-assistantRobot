package domotic;

import jason.asSyntax.*;
import jason.environment.Environment;
import jason.environment.grid.Location;
import java.util.logging.Logger;

public class HouseEnv extends Environment {

	// common literals
	public static final String ARRAYAG[] = { "enfermera", "owner", "auxiliar" }; //Array de agentes. Facilitará la gestión de estos cuando haya que añadir más.
	public static final Literal of = Literal.parseLiteral("open(fridge)");
	public static final Literal clf = Literal.parseLiteral("close(fridge)");
	public static final Literal oc = Literal.parseLiteral("open(cabinet)");
	public static final Literal clc = Literal.parseLiteral("close(cabinet)");

	// public static final Literal oc = Literal.parseLiteral("open(cabinet)");
	// public static final Literal clc = Literal.parseLiteral("close(cabinet)");
	public static final Literal gd = Literal.parseLiteral("get(drug)");
	public static final Literal hd = Literal.parseLiteral("hand_in(drug)");
	public static final Literal sd = Literal.parseLiteral("sip(drug)");
	public static final Literal hob = Literal.parseLiteral("has(owner,drug)");

	public static final Literal ef = Literal.parseLiteral("at(enfermera,fridge)");
	public static final Literal eo = Literal.parseLiteral("at(enfermera,owner)");
	public static final Literal ed = Literal.parseLiteral("at(enfermera,delivery)");
	public static final Literal adbat2 = Literal.parseLiteral("at(enfermera,lDoorBath2)");
	public static final Literal adh = Literal.parseLiteral("at(enfermera,lDoorHome)");
	public static final Literal adk1 = Literal.parseLiteral("at(enfermera,lDoorKit1)");
	public static final Literal adk2 = Literal.parseLiteral("at(enfermera,lDoorKit2)");
	public static final Literal ads1 = Literal.parseLiteral("at(enfermera,lDoorSal1)");
	public static final Literal ads2 = Literal.parseLiteral("at(enfermera,lDoorSal2)");
	public static final Literal adb1 = Literal.parseLiteral("at(enfermera,lDoorBed1)");
	public static final Literal adbat1 = Literal.parseLiteral("at(enfermera,lDoorBath1)");
	public static final Literal adob3 = Literal.parseLiteral("at(enfermera,lDoorBed3)");
	public static final Literal adob2 = Literal.parseLiteral("at(enfermera,lDoorBed2)");
	public static final Literal ac = Literal.parseLiteral("at(robot,cabinet)");

	public static final Literal oaf = Literal.parseLiteral("at(owner,fridge)");
	public static final Literal oac1 = Literal.parseLiteral("at(owner,chair1)");
	public static final Literal oac2 = Literal.parseLiteral("at(owner,chair2)");
	public static final Literal oac3 = Literal.parseLiteral("at(owner,chair3)");
	public static final Literal oac4 = Literal.parseLiteral("at(owner,chair4)");
	public static final Literal oasf = Literal.parseLiteral("at(owner,sofa)");
	public static final Literal oad = Literal.parseLiteral("at(owner,delivery)");
	public static final Literal oab1 = Literal.parseLiteral("at(owner,bed1)");
	public static final Literal oab2 = Literal.parseLiteral("at(owner,bed2)");
	public static final Literal oab3 = Literal.parseLiteral("at(owner,bed3)");
	public static final Literal oac = Literal.parseLiteral("at(owner,cabinet)");
	public static final Literal oaw = Literal.parseLiteral("at(owner,washer)");

	static Logger logger = Logger.getLogger(HouseEnv.class.getName());
	private static Calendar calendar;
	private static int bateriaRobot;
	private static int bateriaAuxiliar;
	HouseModel model; // the model of the grid
	int lastDay;

	@Override
	public void init(String[] args) {
		model = new HouseModel();
		calendar = new Calendar();
		bateriaRobot = model.getGsize() * model.getGsize() * 2;
		bateriaAuxiliar = model.getGsize() * model.getGsize() * 2;
		model.setBateriaRobot(0,model.getGsize() * model.getGsize() * 2); // numero de celdas 12 e alto (Gsize) 24 de ancho
		model.setBateriaRobot(2,model.getGsize() * model.getGsize() * 2);
		lastDay = 0;
		if (args.length == 1 && args[0].equals("gui")) {
			HouseView view = new HouseView(model);
			model.setView(view);
		}

		updatePercepts();
	}

	void updateAgentsPlace() {
		// get the robot location
		for (int i = 0; i < ARRAYAG.length; i++) {
			Location lAg = model.getAgPos(i);
			String RoomAg = model.getRoom(lAg);
			addPercept(ARRAYAG[i], Literal.parseLiteral("atRoom(" + RoomAg + ")"));
			addPercept(ARRAYAG[(i + 1) % 2], Literal.parseLiteral("atRoom(" + ARRAYAG[i] + "," + RoomAg + ")"));
		}
	}

	void updateThingsPlace() {
		// get the location of the things in the house
		String fridgePlace = model.getRoom(model.lFridge);
		addPercept(Literal.parseLiteral("atRoom(fridge, " + fridgePlace + ")"));
		String sofaPlace = model.getRoom(model.lSofa);
		addPercept(Literal.parseLiteral("atRoom(sofa, " + sofaPlace + ")"));
		String chair1Place = model.getRoom(model.lChair1);
		addPercept(Literal.parseLiteral("atRoom(chair1, " + chair1Place + ")"));
		String chair2Place = model.getRoom(model.lChair2);
		addPercept(Literal.parseLiteral("atRoom(chair2, " + chair2Place + ")"));
		String chair3Place = model.getRoom(model.lChair3);
		addPercept(Literal.parseLiteral("atRoom(chair3, " + chair3Place + ")"));
		String chair4Place = model.getRoom(model.lChair4);
		addPercept(Literal.parseLiteral("atRoom(chair4, " + chair4Place + ")"));
		String deliveryPlace = model.getRoom(model.lDeliver);
		addPercept(Literal.parseLiteral("atRoom(delivery, " + deliveryPlace + ")"));
		String bed1Place = model.getRoom(model.lBed1);
		addPercept(Literal.parseLiteral("atRoom(bed1, " + bed1Place + ")"));
		String bed2Place = model.getRoom(model.lBed2);
		addPercept(Literal.parseLiteral("atRoom(bed2, " + bed2Place + ")"));
		String bed3Place = model.getRoom(model.lBed3);
		addPercept(Literal.parseLiteral("atRoom(bed3, " + bed3Place + ")"));
	}

	/** creates the agents percepts based on the HouseModel */
	void updatePercepts() {
		// clear the percepts of the agents
		clearPercepts("enfermera");
		clearPercepts("owner");
		clearPercepts("auxiliar");

		updateAgentsPlace();
		updateThingsPlace();

		Location lRobot = model.getAgPos(0);
		Location lOwner = model.getAgPos(1);
		Location lAuxiliar = model.getAgPos(2);

		for (int i = 0; i < ARRAYAG.length; i++) {
			Location lAgent = model.getAgPos(i);
			if (lAgent.distance(model.lFridge) < 2) {
				addPercept(ARRAYAG[i], Literal.parseLiteral("at(" + ARRAYAG[i] + ",fridge)"));
			}

			if (lAgent.distance(lOwner) < 2 && i != 1) {
				addPercept(ARRAYAG[i], Literal.parseLiteral("at(" + ARRAYAG[i] + ",owner)"));
			}

			if (lAgent.distance(lRobot) < 2 && i != 0) {
				addPercept(ARRAYAG[i], Literal.parseLiteral("at(" + ARRAYAG[i] + ",robot)"));

			}
			if (lAgent.distance(lAuxiliar) < 2 && i!=2) {
				addPercept(ARRAYAG[i],  Literal.parseLiteral("at("+ARRAYAG[i]+",auxiliar)"));
				//System.out.println("[robot] is at owner.");	
			}

			if(lAgent.distanceEuclidean(model.lCargadorRobot) == 0){
				addPercept(ARRAYAG[i],  Literal.parseLiteral("at("+ARRAYAG[i]+",cargadorRobot)"));
			}

			if (lAgent.distance(model.lCabinet) < 2) {
				for (int j = 0; j < ARRAYAG.length; j++) {
					addPercept(ARRAYAG[j], Literal.parseLiteral("at(" + ARRAYAG[i] + ",cabinet)"));
				}
			}
			if (lAgent.distance(model.lDeliver) < 2) {
				addPercept(ARRAYAG[i], Literal.parseLiteral("at(" + ARRAYAG[i] + ",delivery)"));

			}
			if (lAgent.distance(model.lWasher) < 2) {
				addPercept(ARRAYAG[i], Literal.parseLiteral("at(" + ARRAYAG[i] + ",washer)"));

			}
		}

		if (lOwner.distance(model.lChair1) == 0) {
			addPercept("owner", oac1);
		}

		if (lOwner.distance(model.lChair2) == 0) {
			addPercept("owner", oac2);
		}

		if (lOwner.distance(model.lChair3) == 0) {
			addPercept("owner", oac3);
		}

		if (lOwner.distance(model.lChair4) == 0) {
			addPercept("owner", oac4);
		}
		if (lOwner.distance(model.lBed1) == 0) {
			addPercept("owner", oab1);
		}
		if (lOwner.distance(model.lBed2) == 0) {
			addPercept("owner", oab2);
		}
		if (lOwner.distance(model.lBed3) == 0) {
			addPercept("owner", oab3);
		}
		if (lOwner.distance(model.lSofa) == 0) {
			addPercept("owner", oasf);
		}

		for (int i = 0; i < ARRAYAG.length; i++) {
			addPercept(ARRAYAG[i], Literal.parseLiteral("hour(" + calendar.getHora() + ")"));
			if(i==0 || i==2)
			{
				addPercept(ARRAYAG[i],Literal.parseLiteral("bateria("+model.getBateriaRobot(i)+")"));
			}
		}

		for (int i = 0; i < model.getOwnerDrugs().size(); i++) {
			addPercept("owner", Literal.parseLiteral("has(owner," + model.getOwnerDrugs().get(i) + ")"));
		}


		//tengo que hacer esta comprobacion para que no me actualice la perepcion cada vez que se ejecute el updatePercept, sino solo cuando cambie el dia
		//Funciona pero no se porque si hago debug no veo la percepcion de dia en owner
		if(this.lastDay != calendar.getDia()) {
			for (int i = 0; i < ARRAYAG.length; i++) {
					addPercept("owner", Literal.parseLiteral("day(" + calendar.getDia() + ")"));
					
				}
			this.lastDay = calendar.getDia();	
		}



	}

	@Override
	public boolean executeAction(String ag, Structure action) {
		int agNum = -1;
		int it = -1;
		while (++it < ARRAYAG.length && agNum == -1) {
			if (ARRAYAG[it].equals(ag)) {
				agNum = it;
			}
		}

		boolean result = false;
		if (action.getFunctor().equals("sit") && ag.equals("owner")) {
			String l = action.getTerm(0).toString();
			Location dest = null;
			switch (l) {
				case "chair1":
					dest = model.lChair1;
					break;
				case "chair2":
					dest = model.lChair2;
					break;
				case "chair3":
					dest = model.lChair3;
					break;
				case "chair4":
					dest = model.lChair4;
					break;
				case "sofa":
					dest = model.lSofa;
					break;
			}
			result = model.sit(1, dest);

		} else if (action.equals(of)) { // of = open(fridge)
			result = model.openFridge();
		} 
		else if (action.equals(clf)) { // clf = close(fridge)
			result = model.closeFridge();
		} 
		else if (action.equals(oc)) { // oc = open(cabinet)
			result = model.openCabinet();
		}
		 else if (action.equals(clc)) { // clc = close(cabinet)
			result = model.closeCabinet();
		} 
		else if (action.getFunctor().equals("move_towards")) {
			Location dest = model.getLocation(action.getTerm(0).toString());
			result = model.moveTowards(agNum, dest);
		} 
		else if (action.getFunctor().equals("apartar")) {
			result = model.apartar(agNum);
		} 
		else if (action.getFunctor().equals("takeDrug")) {
			String drug = action.getTerm(1).toString();
			result = model.takeDrug(agNum, drug);
		} 
		else if (action.getFunctor().equals("handDrug")) {
			String drug = action.getTerm(0).toString();
			result = model.handDrug(agNum);
			model.getOwnerDrugs().add(drug);
		} 
		else if (action.getFunctor().equals("consume")) {
			model.getOwnerDrugs().remove(action.getTerm(0).toString());
			result = true;
		}
		else if (action.getFunctor().equals("comprobarConsumo")) {
			String drug = action.getTerm(0).toString();
			int num = Integer.parseInt(action.getTerm(1).toString());
			result = model.comprobarConsumo(drug, num);
		}
		else if (action.getFunctor().equals("updatePercepts")) {
			result = true;
		}
		else if (action.getFunctor().equals("addDrug") && ag.equals("auxiliar")) {
			String drug = action.getTerm(0).toString();
			int qtd=Integer.parseInt(action.getTerm(1).toString());
			result=model.addDrug(drug,qtd);
			model.setAuxiliarCargar(0);
		}
		else if (action.getFunctor().equals("cargar") && ag.equals("enfermera")) {

			bateriaRobot+=1;
			model.increaseBateriaRobot(agNum);
			result = true;
		}
		else if (action.getFunctor().equals("cargar") && ag.equals("auxiliar")) {

			bateriaAuxiliar+=1;
			model.increaseBateriaRobot(agNum);
			result = true; 
		} 
		//else if (action.getFunctor().equals("setBateria") && ag.equals("owner")) {
		//	String agente=action.getTerm(0).toString();
		//	model.setBateriaRobot(agente,200);
		//	result = true; 
		
		//}

		else if (action.getFunctor().equals("getCost")) {
			Location dest = model.getLocation(action.getTerm(0).toString());
			Location cargador =  model.lCargadorRobot;
			AStar path = new AStar(model, agNum, model.getAgPos(agNum), dest);
			AStar pathCarga = new AStar(model, agNum, dest, cargador);
			int costCarga = pathCarga.getCost();
			int cost = (path.getCost());
			int costeTotal = cost + costCarga;
			System.out.println("Coste total: " + costeTotal);
			if( (int)(costeTotal * 1.5) > model.getBateriaRobot(agNum) ){
				result = false;
			}else{
				result =  true;
			}

		
	
	
		}
		/* no funciona
		else if (action.getFunctor().equals("mostrarBateria")){
			String mensaje = agNum == 0 
    			? "Batería enfermera: " + model.getBateriaRobot(agNum) 
    			: "Batería auxiliar: " + model.getBateriaRobot(agNum);

			System.out.println(mensaje);
			result = true;		
		}
		*/
		
			
		else {
			logger.info("Failed to execute action " + action);
		}
		if (result) {
			if(agNum==0 || agNum==2)
			{
				model.reduceBateriaRobot(agNum);
				if(model.getBateriaRobot(agNum)%10  == 0){
					System.out.println("Batería " + ARRAYAG[agNum] + ": " + model.getBateriaRobot(agNum));
				}
			}
			updatePercepts();
			try {
				Thread.sleep(200);
			} catch (Exception e) {
			}
		}
		return result;
	}

}