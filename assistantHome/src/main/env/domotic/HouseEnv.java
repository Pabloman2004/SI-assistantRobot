package domotic;

import jason.asSyntax.*;
import jason.environment.Environment;
import jason.environment.grid.Location;
import java.util.logging.Logger;

public class HouseEnv extends Environment {

	// common literals
	public static final String ARRAYAG[] = { "enfermera", "owner" };
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
	// public static final Literal ac =
	// Literal.parseLiteral("at(enfermera,cabinet)");

	public static final Literal oaf = Literal.parseLiteral("at(owner,fridge)");
	public static final Literal oac1 = Literal.parseLiteral("at(owner,chair1)");
	public static final Literal oac2 = Literal.parseLiteral("at(owner,chair2)");
	public static final Literal oac3 = Literal.parseLiteral("at(owner,chair3)");
	public static final Literal oac4 = Literal.parseLiteral("at(owner,chair4)");
	public static final Literal oasf = Literal.parseLiteral("at(owner,sofa)");
	public static final Literal oad = Literal.parseLiteral("at(owner,delivery)");
	private static final Literal oab1 = Literal.parseLiteral("at(owner,bed1)");
	private static final Literal oab2 = Literal.parseLiteral("at(owner,bed2)");
	private static final Literal oab3 = Literal.parseLiteral("at(owner,bed3)");
	private static final Literal oac = Literal.parseLiteral("at(owner,cabinet)");
	private static final Literal oaw = Literal.parseLiteral("at(owner,washer)");
	// public static final Literal oac = Literal.parseLiteral("at(owner,cabinet)");

	static Logger logger = Logger.getLogger(HouseEnv.class.getName());

	HouseModel model; // the model of the grid
	/*
	 * public enum medicamentos {
	 * Simvastatina,
	 * Aspirina,
	 * Omeprazol,
	 * LexotiroxinaSódica,
	 * Ramipril
	 * }
	 */

	@Override
	public void init(String[] args) {
		model = new HouseModel();

		if (args.length == 1 && args[0].equals("gui")) {
			HouseView view = new HouseView(model);
			model.setView(view);
		}

		updatePercepts();
	}
	/*
	 * METODO VIEJO
	 * 
	 * void updateAgentsPlace() {
	 * // get the robot location
	 * Location lRobot = model.getAgPos(0);
	 * // get the robot room location
	 * String RobotPlace = model.getRoom(lRobot);
	 * addPercept("enfermera",
	 * Literal.parseLiteral("atRoom("+RobotPlace+")"));//añade a las creencias del
	 * agente enfermera la localización de la habitación en la que se encuentra
	 * addPercept("owner",
	 * Literal.parseLiteral("atRoom(enfermera,"+RobotPlace+")"));//añade a las
	 * creencias del agente owner la localización de la habitación en la que se
	 * encuentra el agente enfermera
	 * // get the owner location
	 * Location lOwner = model.getAgPos(1);
	 * // get the owner room location
	 * String OwnerPlace = model.getRoom(lOwner);
	 * addPercept("owner", Literal.parseLiteral("atRoom("+OwnerPlace+")"));//añade a
	 * las creencias del agente owner la localización de la habitación en la que se
	 * encuentra el mismmo
	 * addPercept("enfermera",
	 * Literal.parseLiteral("atRoom(owner,"+OwnerPlace+")"));//añade a las creencias
	 * del agente enfermera la localización de la habitación en la que se encuentra
	 * el agente owner
	 * 
	 * if (lRobot.distance(model.lDoorHome)==0 ||
	 * lRobot.distance(model.lDoorKit1)==0 ||
	 * lRobot.distance(model.lDoorKit2)==0 ||
	 * lRobot.distance(model.lDoorSal1)==0 ||
	 * lRobot.distance(model.lDoorSal2)==0 ||
	 * lRobot.distance(model.lDoorBath1)==0 ||
	 * lRobot.distance(model.lDoorBath2)==0 ||
	 * lRobot.distance(model.lDoorBed1)==0 ||
	 * lRobot.distance(model.lDoorBed2)==0 ||
	 * lRobot.distance(model.lDoorBed3)==0 ) {
	 * addPercept("enfermera", Literal.parseLiteral("atDoor"));//creo que lo que
	 * quiere decir es que si enfrente tiene un obstaculo le pasa la creencia de que
	 * esta enfrente de un obstaculo lo que no se porque le pasa atDoor
	 * //Creo que pasa el atDoor para verificar si tiene puertas cerca
	 * };
	 * 
	 * if (lOwner.distance(model.lDoorHome)==0 ||
	 * lOwner.distance(model.lDoorKit1)==0 ||
	 * lOwner.distance(model.lDoorKit2)==0 ||
	 * lOwner.distance(model.lDoorSal1)==0 ||
	 * lOwner.distance(model.lDoorSal2)==0 ||
	 * lOwner.distance(model.lDoorBath1)==0 ||
	 * lOwner.distance(model.lDoorBath2)==0 ||
	 * lOwner.distance(model.lDoorBed1)==0 ||
	 * lOwner.distance(model.lDoorBed2)==0 ||
	 * lOwner.distance(model.lDoorBed3)==0 ) {
	 * addPercept("owner", Literal.parseLiteral("atDoor"));//lo mismo para owner
	 * };
	 * 
	 * }
	 */

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
		// get the fridge location
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
	// tengo que comproabr cada cuanto se ejecuta este metodo si se ejecuta cada vez
	// que se mueve el robot o el owner con ir cambiando dinamicamente el
	// medicamento
	// si cada medicamento se toma el mismo numero de veces valdria
	void updatePercepts() {
		// clear the percepts of the agents
		clearPercepts("enfermera");
		clearPercepts("owner");

		updateAgentsPlace();
		updateThingsPlace();

		Location lRobot = model.getAgPos(0);
		Location lOwner = model.getAgPos(1);

		for (int i = 0; i < ARRAYAG.length; i++) {
			Location lAgent = model.getAgPos(i);
			if (lAgent.distanceEuclidean(model.lFridge) < 2) {
				addPercept(ARRAYAG[i], Literal.parseLiteral("at(" + ARRAYAG[i] + ",fridge)"));
				// System.out.println("[robot] is at fridge.");
			}

			if (lAgent.distanceEuclidean(lOwner) < 2 && i != 1) {
				addPercept(ARRAYAG[i], Literal.parseLiteral("at(" + ARRAYAG[i] + ",owner)"));
				// System.out.println("[robot] is at owner.");

			}
			if (lAgent.distanceEuclidean(lRobot) < 2 && i != 0) {
				addPercept(ARRAYAG[i], Literal.parseLiteral("at(" + ARRAYAG[i] + ",robot)"));
				// System.out.println("[robot] is at owner.");

			}
			if (lAgent.distanceEuclidean(model.lCabinet) < 2) {
				// addPercept("robot", ac);
				// addPercept("owner", ac);
				for (int j = 0; j < ARRAYAG.length; j++) {
					addPercept(ARRAYAG[j], Literal.parseLiteral("at(" + ARRAYAG[i] + ",cabinet)"));
				}
				// System.out.println("[robot] is at cabinet.");
			}
			if (lAgent.distanceEuclidean(model.lDeliver) < 2) {
				addPercept(ARRAYAG[i], Literal.parseLiteral("at(" + ARRAYAG[i] + ",delivery)"));
				// System.out.println("[robot] is at delivery.");

			}
			if (lAgent.distanceEuclidean(model.lWasher) < 2) {
				// addPercept(ARRAYAG[i], aw);
				addPercept(ARRAYAG[i], Literal.parseLiteral("at(" + ARRAYAG[i] + ",washer)"));
				// System.out.println("[robot] is at washer.");

			}
		}

		if (lOwner.distance(model.lChair1) == 0) {
			addPercept("owner", oac1);
			// System.out.println("[owner] is at Chair1.");
		}

		if (lOwner.distance(model.lChair2) == 0) {
			addPercept("owner", oac2);
			// System.out.println("[owner] is at Chair2.");
		}

		if (lOwner.distance(model.lChair3) == 0) {
			addPercept("owner", oac3);
			// System.out.println("[owner] is at Chair3.");
		}

		if (lOwner.distance(model.lChair4) == 0) {
			addPercept("owner", oac4);
			// System.out.println("[owner] is at Chair4.");
		}
		if (lOwner.distance(model.lBed1) == 0) {
			addPercept("owner", oab1);
			// System.out.println("[owner] is at Bed1.");
		}
		if (lOwner.distance(model.lBed2) == 0) {
			addPercept("owner", oab2);
			// System.out.println("[owner] is at Bed2.");
		}
		if (lOwner.distance(model.lBed3) == 0) {
			addPercept("owner", oab3);
			// System.out.println("[owner] is at Bed3.");
		}
		if (lOwner.distance(model.lSofa) == 0) {
			addPercept("owner", oasf);
			// System.out.println("[owner] is at Sofa.");
		}

		for (int i = 0; i < model.getOwnerDrugs().size(); i++) {
			addPercept("owner", Literal.parseLiteral("has(owner," + model.getOwnerDrugs().get(i) + ")"));
		}

	}

	@Override
	public boolean executeAction(String ag, Structure action) {

		System.out.println("[" + ag + "] doing: " + action);
		int agNum = -1;
		int it = -1;
		while (++it < ARRAYAG.length && agNum == -1) {
			if (ARRAYAG[it].equals(ag)) {
				agNum = it;
			}
		}
		// java.util.List<Literal> perceptsOwner = consultPercepts("owner");
		// java.util.List<Literal> perceptsRobot = consultPercepts("enfermera");
		// System.out.println("[owner] has the following percepts: "+perceptsOwner);
		// System.out.println("[enfermera] has the following percepts: "+perceptsRobot);

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
		else if (action.equals(oc)) { // of = open(fridge)
			result = model.openCabinet();
		}
		 else if (action.equals(clc)) { // clf = close(fridge)
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
		else if (action.getFunctor().equals("deliver") && ag.equals("proveedor")) {
			// wait 4 seconds to finish "deliver"
			try {
				String drug = action.getTerm(0).toString();
				int qtd = Integer.parseInt(action.getTerm(1).toString());
				result = model.deliver(drug,qtd);
			} catch (Exception e) {
				logger.info("Failed to execute action deliver!" + e);
			}
		}else if (action.getFunctor().equals("addDrug") && ag.equals("auxiliar")) {
			String drug = action.getTerm(0).toString();
			int qtd=Integer.parseInt(action.getTerm(1).toString());
			result=model.addDrug(drug,qtd);
			
		}
		
		/*
		 * else if (action.getFunctor().equals("deliver") && ag.equals("enfermera")) {
		 * // wait 4 seconds to finish "deliver"
		 * try {
		 * String drug = action.getTerm(0).toString();
		 * int qtd = Integer.parseInt(action.getTerm(1).toString());
		 * result = model.deliver(drug,qtd);
		 * } catch (Exception e) {
		 * logger.info("Failed to execute action deliver!" + e);
		 * }
		 * 
		 * }
		 */

		/*
		 * else if (action.getFunctor().equals("getDelivery") && ag.equals("enfermera"))
		 * {
		 * try {
		 * String drug = action.getTerm(0).toString();
		 * result = model.getDelivery(drug);
		 * model.setAuxiliarCargar(1);
		 * } catch (Exception e) {
		 * logger.info("Failed to execute action deliver!" + e);
		 * }
		 * }
		 */
		/*
		 * else if (action.getFunctor().equals("addDrug") && ag.equals("enfermera")) {
		 * String drug = action.getTerm(0).toString();
		 * int qtd=Integer.parseInt(action.getTerm(1).toString());
		 * result=model.addDrug(drug,qtd);
		 * model.setAuxiliarCargar(0);
		 * }
		 */
		else {
			logger.info("Failed to execute action " + action);
		}
		if (result) {
			updatePercepts();
			try {
				Thread.sleep(200);
			} catch (Exception e) {
			}
		}
		return result;
	}

}