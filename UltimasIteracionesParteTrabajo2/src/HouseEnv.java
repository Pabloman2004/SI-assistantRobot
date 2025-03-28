import jason.asSyntax.*;
import jason.environment.Environment;
import jason.environment.grid.Location;
import java.util.logging.Logger;

public class HouseEnv extends Environment {

	// common literals
	private static final String ARRAYAG[] = { "robot", "owner","auxiliar"};
	private static final Literal of = Literal.parseLiteral("open(fridge)");
	private static final Literal clf = Literal.parseLiteral("close(fridge)");
	private static final Literal oc = Literal.parseLiteral("open(cabinet)");
	private static final Literal clc = Literal.parseLiteral("close(cabinet)");

	private static final Literal af = Literal.parseLiteral("at(robot,fridge)");
	private static final Literal ao = Literal.parseLiteral("at(robot,owner)");
	private static final Literal ad = Literal.parseLiteral("at(robot,delivery)");
	private static final Literal adbat2 = Literal.parseLiteral("at(robot,lDoorBath2)");
	private static final Literal adh = Literal.parseLiteral("at(robot,lDoorHome)");
	private static final Literal adk1 = Literal.parseLiteral("at(robot,lDoorKit1)");
	private static final Literal adk2 = Literal.parseLiteral("at(robot,lDoorKit2)");
	private static final Literal ads1 = Literal.parseLiteral("at(robot,lDoorSal1)");
	private static final Literal ads2 = Literal.parseLiteral("at(robot,lDoorSal2)");
	private static final Literal adb1 = Literal.parseLiteral("at(robot,lDoorBed1)");
	private static final Literal adbat1 = Literal.parseLiteral("at(robot,lDoorBath1)");
	private static final Literal adob3 = Literal.parseLiteral("at(robot,lDoorBed3)");
	private static final Literal adob2 = Literal.parseLiteral("at(robot,lDoorBed2)");
	private static final Literal ac = Literal.parseLiteral("at(robot,cabinet)");
	private static final Literal aw = Literal.parseLiteral("at(robot,washer)");
	private static final Literal ar = Literal.parseLiteral("at(robot,retrete)");
	
	
	private static final Literal or = Literal.parseLiteral("at(owner,retrete)");
	private static final Literal oaf = Literal.parseLiteral("at(owner,fridge)");
	private static final Literal oac1 = Literal.parseLiteral("at(owner,chair1)");
	private static final Literal oac2 = Literal.parseLiteral("at(owner,chair2)");
	private static final Literal oac3 = Literal.parseLiteral("at(owner,chair3)");
	private static final Literal oac4 = Literal.parseLiteral("at(owner,chair4)");
	private static final Literal oasf = Literal.parseLiteral("at(owner,sofa)");
	private static final Literal oad = Literal.parseLiteral("at(owner,delivery)");
	private static final Literal oab1 = Literal.parseLiteral("at(owner,bed1)");
	private static final Literal oab2 = Literal.parseLiteral("at(owner,bed2)");
	private static final Literal oab3 = Literal.parseLiteral("at(owner,bed3)");
	private static final Literal oac = Literal.parseLiteral("at(owner,cabinet)");
	private static final Literal oaw = Literal.parseLiteral("at(owner,washer)");
	private static Calendar calendar;
	private static Logger logger = Logger.getLogger(HouseEnv.class.getName());
	private static Bateria bateriaRobot;
	private static Bateria bateriaAuxiliar;


	HouseModel model; // the model of the grid

	@Override
	public void init(String[] args) {
		model = new HouseModel();
		calendar = new Calendar(this);
		bateriaRobot = new Bateria("robot");
		bateriaAuxiliar = new Bateria("auxiliar");
		if (args.length == 1 && args[0].equals("gui")) {
			HouseView view = new HouseView(model);
			model.setView(view);
		}
		updatePercepts();
	}

	void updateAgentsPlace() {
		// get the robot location
		for(int i=0;i<ARRAYAG.length;i++)
		{
				Location lAg = model.getAgPos(i);
				String RoomAg = model.getRoom(lAg);
				addPercept(ARRAYAG[i], Literal.parseLiteral("atRoom(" + RoomAg + ")"));
				addPercept(ARRAYAG[(i+1)%2], Literal.parseLiteral("atRoom("+ARRAYAG[i]+"," + RoomAg + ")"));	
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
	void updatePercepts() {
		// clear the percepts of the agents
		clearPercepts("robot");
		clearPercepts("owner");
		clearPercepts("auxiliar");
		clearPercepts("proveedor");
		updateAgentsPlace();
		updateThingsPlace();

		Location lRobot = model.getAgPos(0);
		Location lOwner = model.getAgPos(1);
		Location lAuxiliar = model.getAgPos(2);
		for(int i=0;i<ARRAYAG.length;i++)
		{
			Location lAgent=model.getAgPos(i);
			if (lAgent.distanceEuclidean(model.lFridge) < 2) {
				addPercept(ARRAYAG[i],  Literal.parseLiteral("at("+ARRAYAG[i]+",fridge)"));
				//System.out.println("[robot] is at fridge.");
			}
	
			if (lAgent.distanceEuclidean(lOwner) < 2 && i!=1) {
				addPercept(ARRAYAG[i],  Literal.parseLiteral("at("+ARRAYAG[i]+",owner)"));
				//System.out.println("[robot] is at owner.");
	
			}
			if (lAgent.distanceEuclidean(lRobot) < 2 && i!=0) {
				addPercept(ARRAYAG[i],  Literal.parseLiteral("at("+ARRAYAG[i]+",robot)"));
				//System.out.println("[robot] is at owner.");
	
			}
			if (lAgent.distanceEuclidean(lAuxiliar) < 2 && i!=2) {
				addPercept(ARRAYAG[i],  Literal.parseLiteral("at("+ARRAYAG[i]+",auxiliar)"));
				//System.out.println("[robot] is at owner.");
	
			}if(lAgent.distanceEuclidean(model.lCargadorRobot) == 0){
				addPercept(ARRAYAG[i],  Literal.parseLiteral("at("+ARRAYAG[i]+",cargadorRobot)"));
			}
			if(lAgent.distanceEuclidean(model.lCargadorAuxiliar) == 0){
				addPercept(ARRAYAG[i],  Literal.parseLiteral("at("+ARRAYAG[i]+",cargadorAuxiliar)"));
			}
			if (lAgent.distanceEuclidean(model.lCabinet) < 2) {
				//addPercept("robot", ac);
				//addPercept("owner", ac);
				for(int j=0;j<ARRAYAG.length;j++)
				{
					addPercept(ARRAYAG[j],  Literal.parseLiteral("at("+ARRAYAG[i]+",cabinet)"));
				}
				//System.out.println("[robot] is at cabinet.");
	
			}
	
			if (lAgent.distanceEuclidean(model.lDeliver) < 2) {
				addPercept(ARRAYAG[i],  Literal.parseLiteral("at("+ARRAYAG[i]+",delivery)"));
				//System.out.println("[robot] is at delivery.");
	
			}
			if(lAgent.distanceEuclidean(model.lRetrete)==0)
			{
				//addPercept(ARRAYAG[i],ar);
				addPercept(ARRAYAG[i],  Literal.parseLiteral("at("+ARRAYAG[i]+",retrete)"));
				//System.out.println("[robot] is at retrete.");
			}
			if (lAgent.distanceEuclidean(model.lWasher) < 2) {
				//addPercept(ARRAYAG[i], aw);
				addPercept(ARRAYAG[i],  Literal.parseLiteral("at("+ARRAYAG[i]+",washer)"));
				//System.out.println("[robot] is at washer.");
	
			}
		}
/*
		if (lRobot.distanceEuclidean(model.lFridge) < 2) {
			addPercept("robot", af);
			//System.out.println("[robot] is at fridge.");
		}

		if (lOwner.distanceEuclidean(model.lFridge) < 2) {
			addPercept("owner", oaf);
			//System.out.println("[owner] is at fridge.");
		}

		if (lRobot.distanceEuclidean(lOwner) < 2) {
			addPercept("robot", ao);
			//System.out.println("[robot] is at owner.");

		}

		if (lRobot.distanceEuclidean(model.lCabinet) < 2) {
			addPercept("robot", ac);
			addPercept("owner", ac);
			//System.out.println("[robot] is at cabinet.");

		}

		if (lRobot.distanceEuclidean(model.lDeliver) < 2) {
			addPercept("robot", ad);
			//System.out.println("[robot] is at delivery.");

		}
		if(lRobot.distanceEuclidean(model.lRetrete)==0)
		{
			addPercept("robot",ar);
			//System.out.println("[robot] is at retrete.");
		}
		if (lRobot.distanceEuclidean(model.lWasher) < 2) {
			addPercept("robot", aw);
			//System.out.println("[robot] is at washer.");

		}
		if (lOwner.distanceEuclidean(model.lWasher) < 2) {
			addPercept("owner", oaw);
			//System.out.println("[owner] is at washer.");

		}
*/
		if (lOwner.distance(model.lChair1) == 0) {
			addPercept("owner", oac1);
			//System.out.println("[owner] is at Chair1.");
		}

		if (lOwner.distance(model.lChair2) == 0) {
			addPercept("owner", oac2);
			//System.out.println("[owner] is at Chair2.");
		}

		if (lOwner.distance(model.lChair3) == 0) {
			addPercept("owner", oac3);
			//System.out.println("[owner] is at Chair3.");
		}

		if (lOwner.distance(model.lChair4) == 0) {
			addPercept("owner", oac4);
			//System.out.println("[owner] is at Chair4.");
		}
		if (lOwner.distance(model.lBed1) == 0) {
			addPercept("owner", oab1);
			//System.out.println("[owner] is at Bed1.");
		}
		if (lOwner.distance(model.lBed2) == 0) {
			addPercept("owner", oab2);
			//System.out.println("[owner] is at Bed2.");
		}
		if (lOwner.distance(model.lBed3) == 0) {
			addPercept("owner", oab3);
			//System.out.println("[owner] is at Bed3.");
		}
		/*
		if (lOwner.distanceEuclidean(model.lCabinet) < 2) {
			addPercept("owner", oac);
			addPercept("robot", oac);
			//System.out.println("[owner] is at cabinet.");
		}
*/
		if (lOwner.distance(model.lSofa) == 0) {
			addPercept("owner", oasf);
			//System.out.println("[owner] is at Sofa.");
		}
/*
		if (lOwner.distanceEuclidean(model.lDeliver) < 2) {
			addPercept("owner", oad);
			//System.out.println("[owner] is at delivery.");

		}
		if (lOwner.distanceEuclidean(model.lRetrete)==0)
		{
			addPercept("owner",or);
			//System.out.println("[owner] is at retrete.");
		}
*/
		//System.out.println("[env] Son las " + calendar.getHora() + ":00");
		for (int i = 0; i < ARRAYAG.length; i++) {
			addPercept(ARRAYAG[i], Literal.parseLiteral("hour(" + calendar.getHora() + ")"));
			if (Integer.parseInt(calendar.getHora()) < 8) {
				addPercept(ARRAYAG[i], Literal.parseLiteral("noche"));
			} else {
				addPercept(ARRAYAG[i], Literal.parseLiteral("dia"));
			}
			if(i==0 || i==2)
			{
				addPercept(ARRAYAG[i],Literal.parseLiteral("bateria("+model.getBateriaRobot(i)+")"));
			}
		}
		for (int i = 0; i < model.getOwnerDrugs().size(); i++) {
			addPercept("owner", Literal.parseLiteral("has(owner," + model.getOwnerDrugs().get(i) + ")"));
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

		} else if (action.equals(clf)) { // clf = close(fridge)
			result = model.closeFridge();
		} else if (action.equals(oc)) { // of = open(fridge)
			result = model.openCabinet();

		} else if (action.equals(clc)) { // clf = close(fridge)
			result = model.closeCabinet();
		} else if (action.getFunctor().equals("move_towards")) {
			Location dest = model.getLocation(action.getTerm(0).toString());
			result = model.moveTowards(agNum, dest);
		} else if (action.getFunctor().equals("apartar")) {
			result = model.apartar(agNum);
		}
		else if (action.getFunctor().equals("takeDrug")) {
			String drug = action.getTerm(1).toString();
			result = model.takeDrug(agNum, drug);
		} else if (action.getFunctor().equals("handDrug")) {
			String drug = action.getTerm(0).toString();
			result = model.handDrug(agNum);
			model.getOwnerDrugs().add(drug);
		} else if (action.getFunctor().equals("consume")) {
			model.getOwnerDrugs().remove(action.getTerm(0).toString());
			result = true;
		} else if (action.getFunctor().equals("comprobarConsumo")) {
			String drug = action.getTerm(0).toString();
			int num = Integer.parseInt(action.getTerm(1).toString());
			result = model.comprobarConsumo(drug, num);
		} else if (action.getFunctor().equals("updatePercepts")) {
			result = true;
		} else if (action.getFunctor().equals("deliver") && ag.equals("proveedor")) {
			// wait 4 seconds to finish "deliver"
			try {
				String drug = action.getTerm(0).toString();
				int qtd = Integer.parseInt(action.getTerm(1).toString());
				result = model.deliver(drug,qtd);
			} catch (Exception e) {
				logger.info("Failed to execute action deliver!" + e);
			}

		}else if (action.getFunctor().equals("getDelivery") && ag.equals("auxiliar")) {
			try {
				String drug = action.getTerm(0).toString();
				result = model.getDelivery(drug);
				model.setAuxiliarCargar(1);
			} catch (Exception e) {
				logger.info("Failed to execute action deliver!" + e);
			}
		}else if (action.getFunctor().equals("addDrug") && ag.equals("auxiliar")) {
				String drug = action.getTerm(0).toString();
				int qtd=Integer.parseInt(action.getTerm(1).toString());
				result=model.addDrug(drug,qtd);
				model.setAuxiliarCargar(0);
		}else if (action.getFunctor().equals("cargar") && ag.equals("robot")) {

			bateriaRobot.increase();
			model.increaseBateriaRobot(agNum);
			result = true;
	} else if (action.getFunctor().equals("cargar") && ag.equals("auxiliar")) {

		bateriaAuxiliar.increase();
		model.increaseBateriaRobot(agNum);
		result = true; 
	} else if (action.getFunctor().equals("setBateria") && ag.equals("owner")) {
		String agente=action.getTerm(0).toString();
		bateriaAuxiliar.setBar(200);
		model.setBateriaRobot(agente,200);
		result = true; 
			
	}else if (action.getFunctor().equals("getCost")) {
		Location dest = model.getLocation(action.getTerm(0).toString());
		Location cargador = agNum == 0 ? model.lCargadorRobot : model.lCargadorAuxiliar;
		AStar path = new AStar(model, agNum, model.getAgPos(agNum), dest);
		AStar pathCarga = new AStar(model, agNum, dest, cargador);
		int costCarga = pathCarga.getCost();
		int cost = (path.getCost());
		int costeTotal = cost + costCarga;
		if( (int)(costeTotal * 1.5) > model.getBateriaRobot(agNum) ){
			result = false;
		}else{
			result =  true;
		}
		
		
	}
	else {
			logger.info("Failed to execute action " + action);
		}
		if (result) {
			if(agNum==0 || agNum==2)
			{
				model.reduceBateriaRobot(agNum);
				if(agNum == 0){
					bateriaRobot.reduce();
				}
				if(agNum == 2){
					bateriaAuxiliar.reduce();
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
