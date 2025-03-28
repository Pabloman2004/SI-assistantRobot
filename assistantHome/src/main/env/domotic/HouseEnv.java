package domotic;

import jason.asSyntax.*; // Ensure java.lang.String is not excluded
import jason.environment.Environment;
import jason.environment.grid.Location;
import java.util.logging.Logger;

public class HouseEnv extends Environment {

    // common literals
    public static final Literal of   = Literal.parseLiteral("open(fridge)");
    public static final Literal clf  = Literal.parseLiteral("close(fridge)");
    public static final Literal gb   = Literal.parseLiteral("get(drug)");
    public static final Literal hb   = Literal.parseLiteral("hand_in(drug)");
    public static final Literal sb   = Literal.parseLiteral("sip(drug)");
    public static final Literal hob  = Literal.parseLiteral("has(owner,drug)");

    public static final Literal af   = Literal.parseLiteral("at(enfermera,fridge)");
    public static final Literal ao   = Literal.parseLiteral("at(enfermera,owner)");
    public static final Literal ad   = Literal.parseLiteral("at(enfermera,delivery)");
	
    public static final Literal oaf  = Literal.parseLiteral("at(owner,fridge)");
    public static final Literal oac1 = Literal.parseLiteral("at(owner,chair1)");
    public static final Literal oac2 = Literal.parseLiteral("at(owner,chair2)");
    public static final Literal oac3 = Literal.parseLiteral("at(owner,chair3)");
    public static final Literal oac4 = Literal.parseLiteral("at(owner,chair4)");
    public static final Literal oasf = Literal.parseLiteral("at(owner,sofa)");
    public static final Literal oad  = Literal.parseLiteral("at(owner,delivery)");

    static Logger logger = Logger.getLogger(HouseEnv.class.getName());

    HouseModel model; // the model of the grid

	public enum medicamentos {
		Simvastatina,
		Aspirina,
		Omeprazol,
		LexotiroxinaSódica,
		Ramipril
	}

    @Override
    public void init(String[] args) {
        model = new HouseModel();

        if (args.length == 1 && args[0].equals("gui")) {
            HouseView view  = new HouseView(model);
            model.setView(view);
        }

        updatePercepts();
    }
	
    void updateAgentsPlace() {
		// get the robot location
        Location lRobot = model.getAgPos(0);
		// get the robot room location
		String RobotPlace = model.getRoom(lRobot);
		addPercept("enfermera", Literal.parseLiteral("atRoom("+RobotPlace+")"));//añade a las creencias del agente enfermera la localización de la habitación en la que se encuentra
        addPercept("owner", Literal.parseLiteral("atRoom(enfermera,"+RobotPlace+")"));//añade a las creencias del agente owner la localización de la habitación en la que se encuentra el agente enfermera
		// get the owner location
        Location lOwner = model.getAgPos(1);
		// get the owner room location
		String OwnerPlace = model.getRoom(lOwner);
		addPercept("owner", Literal.parseLiteral("atRoom("+OwnerPlace+")"));//añade a las creencias del agente owner la localización de la habitación en la que se encuentra el mismmo  
        addPercept("enfermera", Literal.parseLiteral("atRoom(owner,"+OwnerPlace+")"));//añade a las creencias del agente enfermera la localización de la habitación en la que se encuentra el agente owner
		
		if (lRobot.distance(model.lDoorHome)==0 	||
			lRobot.distance(model.lDoorKit1)==0 	||
			lRobot.distance(model.lDoorKit2)==0 	||
			lRobot.distance(model.lDoorSal1)==0 	||
			lRobot.distance(model.lDoorSal2)==0 	||
			lRobot.distance(model.lDoorBath1)==0 	||
			lRobot.distance(model.lDoorBath2)==0 	||
			lRobot.distance(model.lDoorBed1)==0 	||
			lRobot.distance(model.lDoorBed2)==0 	||
			lRobot.distance(model.lDoorBed3)==0 	  ) {
			addPercept("enfermera", Literal.parseLiteral("atDoor"));//creo que lo que quiere decir es que si enfrente tiene un obstaculo le pasa la creencia de que esta enfrente de un obstaculo lo que no se porque le pasa atDoor
		}; 
		
		if (lOwner.distance(model.lDoorHome)==0 	||
			lOwner.distance(model.lDoorKit1)==0 	||
			lOwner.distance(model.lDoorKit2)==0 	||
			lOwner.distance(model.lDoorSal1)==0 	||
			lOwner.distance(model.lDoorSal2)==0 	||
			lOwner.distance(model.lDoorBath1)==0 	||
			lOwner.distance(model.lDoorBath2)==0 	||
			lOwner.distance(model.lDoorBed1)==0 	||
			lOwner.distance(model.lDoorBed2)==0 	||
			lOwner.distance(model.lDoorBed3)==0 	  ) {
			addPercept("owner", Literal.parseLiteral("atDoor"));//lo mismo para owner
		}; 		
		
	}
    
	//este metodo sirve para pasarle al robot las posiciones en la que estan los obstaculos
    void updateThingsPlace() {
		// get the fridge location
		String fridgePlace = model.getRoom(model.lFridge);
		addPercept(Literal.parseLiteral("atRoom(fridge, "+fridgePlace+")"));
		String sofaPlace = model.getRoom(model.lSofa);
		addPercept(Literal.parseLiteral("atRoom(sofa, "+sofaPlace+")")); 
		String chair1Place = model.getRoom(model.lChair1);
		addPercept(Literal.parseLiteral("atRoom(chair1, "+chair1Place+")"));
		String chair2Place = model.getRoom(model.lChair2);
		addPercept(Literal.parseLiteral("atRoom(chair2, "+chair2Place+")"));
		String chair3Place = model.getRoom(model.lChair3);
		addPercept(Literal.parseLiteral("atRoom(chair3, "+chair3Place+")"));
		String chair4Place = model.getRoom(model.lChair4);
		addPercept(Literal.parseLiteral("atRoom(chair4, "+chair4Place+")"));
		String deliveryPlace = model.getRoom(model.lDeliver);
		addPercept(Literal.parseLiteral("atRoom(delivery, "+deliveryPlace+")"));
		String bed1Place = model.getRoom(model.lBed1);
		addPercept(Literal.parseLiteral("atRoom(bed1, "+bed1Place+")"));
		String bed2Place = model.getRoom(model.lBed2);
		addPercept(Literal.parseLiteral("atRoom(bed2, "+bed2Place+")"));
		String bed3Place = model.getRoom(model.lBed3);
		addPercept(Literal.parseLiteral("atRoom(bed3, "+bed3Place+")"));
	}
	                                                    
    /** creates the agents percepts based on the HouseModel */
	//tengo que comproabr cada cuanto se ejecuta este metodo si se ejecuta cada vez que se mueve el robot o el owner con ir cambiando dinamicamente el medicamento
	//si cada medicamento se toma el mismo numero de veces valdria
    void updatePercepts() {
        // clear the percepts of the agents
        clearPercepts("enfermera");
        clearPercepts("owner");
		
		updateAgentsPlace();
		updateThingsPlace(); 
		
		Location lRobot = model.getAgPos(0);
		Location lOwner = model.getAgPos(1);
		
        if (lRobot.distance(model.lFridge)<2) {
            addPercept("enfermera", af);
        } 
		
        if (lOwner.distance(model.lFridge)<2) {
            addPercept("owner", oaf);
        } 
		
        if (lRobot.distance(lOwner)==1) {                                                     
            addPercept("enfermera", ao);
        }

        if (lRobot.distance(model.lDeliver)==1) {
            addPercept("enfermera", ad);
        }

        if (lOwner.distance(model.lChair1)==0) {
            addPercept("owner", oac1);
			System.out.println("[owner] is at Chair1.");
        }

        if (lOwner.distance(model.lChair2)==0) {
            addPercept("owner", oac2);
			System.out.println("[owner] is at Chair2.");
        }

        if (lOwner.distance(model.lChair3)==0) {
            addPercept("owner", oac3);
			System.out.println("[owner] is at Chair3.");
        }

        if (lOwner.distance(model.lChair4)==0) {                            
            addPercept("owner", oac4);
			System.out.println("[owner] is at Chair4.");
        }
                                                                            
        if (lOwner.distance(model.lSofa)==0) {
            addPercept("owner", oasf);
			System.out.println("[owner] is at Sofa.");
        }

        if (lOwner.distance(model.lDeliver)==0) {
            addPercept("owner", oad);
        }

        // add drug "status" the percepts

        if (model.fridgeOpen) {
            addPercept("enfermera", Literal.parseLiteral("stock(drug,"+model.medicamentosDisponible(medicamentos.Simvastatina.name())+")"));//cantidad de medicamentos en la nevera deja la nevera abierta hay que hacer que se cierre
			addPercept("owner", Literal.parseLiteral("stock(drug,"+model.medicamentosDisponible(medicamentos.Simvastatina.name())+")"));
		}
        if (model.sipCount > 0) {
            addPercept("enfermera", hob);
            addPercept("owner", hob);
        }
    }

	public String pautaMedicamento(){
		// tiene que devolver el medicamento que deberia tomar el owner
		return "Take one tablet daily.";
	}



    @Override
    public boolean executeAction(String ag, Structure action) {
        
		System.out.println("["+ag+"] doing: "+action); 
		//java.util.List<Literal> perceptsOwner = consultPercepts("owner");
		//java.util.List<Literal> perceptsRobot = consultPercepts("enfermera");  
		//System.out.println("[owner] has the following percepts: "+perceptsOwner);
		//System.out.println("[enfermera] has the following percepts: "+perceptsRobot);
        
		boolean result = false;
        if (action.getFunctor().equals("sit")) {
            String l = action.getTerm(0).toString();
            Location dest = null;
			switch (l) {
				case "chair1": dest = model.lChair1; 
				break;
				case "chair2": dest = model.lChair2;  
				break;     
				case "chair3": dest = model.lChair3; 
				break;
				case "chair4": dest = model.lChair4; 
				break;
				case "sofa": dest = model.lSofa; 
				break;
			};
			try {
				if (ag.equals("enfermera")) {
					System.out.println("[enfermera] is sitting");
					result = model.sit(0,dest);
				} else {
					System.out.println("[owner] is sitting");
					result = model.sit(1,dest);
				}
			} catch (Exception e) {
            	e.printStackTrace();
			}     
        } else if (action.equals(of)) { // of = open(fridge)
            result = model.openFridge();

        } else if (action.equals(clf)) { // clf = close(fridge)
            result = model.closeFridge();
                                                                    
        } else if (action.getFunctor().equals("move_towards")) {
            String l = action.getTerm(0).toString();
            Location dest = null;
			switch (l) {
				case "fridge": dest = model.lFridge; 
				break;
				case "owner": dest = model.getAgPos(1);  
				break;     
				case "delivery": dest = model.lDeliver;  
				break;     
				case "chair1": dest = model.lChair1; 
				break;
				case "chair2": dest = model.lChair2; 
				break;
				case "chair3": dest = model.lChair3; 
				break;
				case "chair4": dest = model.lChair4; 
				break;
				case "sofa": dest = model.lSofa; 
				break;
				case "washer": dest = model.lWasher; 
				break;
				case "table": dest = model.lTable; 
				break;
				case "doorBed1": dest = model.lDoorBed1; 
				break;            
				case "doorBed2": dest = model.lDoorBed2; 
				break;
				case "doorBed3": dest = model.lDoorBed3; 
				break;
				case "doorKit1": dest = model.lDoorKit1; 
				break;
				case "doorKit2": dest = model.lDoorKit2; 
				break;
				case "doorSal1": dest = model.lDoorSal1; 
				break;
				case "doorSal2": dest = model.lDoorSal2; 
				break;
				case "doorBath1": dest = model.lDoorBath1; 
				break;
				case "doorBath2": dest = model.lDoorBath2;                  
				break; 
            }
            try {
                if (ag.equals("enfermera")) {
					result = model.moveTowards(0, dest);
				} else {
					result = model.moveTowards(1, dest);
				}
            } catch (Exception e) {
                e.printStackTrace();
            }     
//aqui va a haber que pasarle el tipo de medicamento que se tiene que tomar, pero hay que crear un metodo entiendo que en esta clase que 
//mida el tiempo en el que se tomo el ultimo medicamento y si ha pasado el tiempo para que se lo pueda tomar
        } else if (action.equals(gb)) {
            result = model.getDrug("Simvastatina");

        } else if (action.equals(hb)) {
            result = model.handInDrug();

        } else if (action.equals(sb)) {
            result = model.sipDrug();

        } else if (action.getFunctor().equals("deliver")) {
            // wait 4 seconds to finish "deliver"
            try {
                result = model.addDrug( (int)((NumberTerm)action.getTerm(1)).solve());
                Thread.sleep(4000);
            } catch (Exception e) {
                logger.info("Failed to execute action deliver!"+e);
            }

        } else {
            logger.info("Failed to execute action "+action);
        }

        if (result) {
            updatePercepts();
            try {
                Thread.sleep(200);
            } catch (Exception e) {}
        }
        return result;
    }

}
