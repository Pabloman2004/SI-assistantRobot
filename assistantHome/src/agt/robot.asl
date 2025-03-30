/* Initial beliefs and rules */  

// initially, robot is free
free.

cantidad(paracetamol,20).
cantidad(ibuprofeno,20).
cantidad(aspirina,20).
cantidad(lorazepam,20).
cantidad(amoxicilina,20).

//Owner le indica al robot la nueva pauta de medicinas.
+pautaNueva(M,H,F)[source(owner)]<-.abolish(pauta(M,_,_));+pauta(M,H,F);.abolish(pautaNueva(M,H,F)).

!simulate_behaviour.   
/* Plans */
//La simulación del robot es bastante sencilla,se desplaza a 3 posiciones.
+!simulate_behaviour[source(self)] 
   <- .random(X); .wait(3000*X + 5000); // wait for a random time
    
    .random([delivery,fridge,washer,cabinet],Y);
    .print("Voy a un sitio ",Y);
    !go_at(enfermera,Y);
    !simulate_behaviour.

+!go_at(enfermera,P)[source(self)] : at(enfermera,P) <- .print("He llegado a ",P).
+!go_at(enfermera,P)[source(self)] : not at(enfermera,P) 
  <- move_towards(P);
     !go_at(enfermera,P).
-!go_at(enfermera,P)[source(self)]<- .print("Estoy apartando");apartar;.wait(5);!go_at(enfermera,P).



answer(Request, "It will be nice to check the weather forecast, don't?.") :-
	.substring("tiempo", Request).  
	
answer(Request, "I don't understand what are you talking about.").

bringDrug(Ag) :- cantidad(D, not L == 0).

orderDrug(Ag) :-cantidad(D, L == 0 ).  

/* Plans */

+!has(Ag, paracetamol)[source(Ag)] : 
	bringDrug(Ag) & free[source(self)] <- 
		.println("FIRST RULE ====================================");
		.wait(1000);
		//!at(enfermera, owner); 
    	-free[source(self)];      
		!at(enfermera, fridge);
		
		open(fridge); // Change it by an internal operation similar to fridge.open
		get(paracetamol);    // Change it by a set of internal operations that recognize the drug an take it
		              // maybe it need to take other products and change their place in the fridge
		close(fridge);// Change it by an internal operation similar to fridge.close
		!at(enfermera, Ag);
		hand_in(paracetamol);// In this case this operation could be external or internal their intention
		              // is to inform that the owner has the drug in his hand and could begin to drink
		?has(Ag, paracetamol);  // If the previous action is completed then a perception from environment must update
		                 // the beliefs of the robot
						 
		// remember that another drug has been consumed
		.date(YY, MM, DD); .time(HH, NN, SS);
		+consumed(YY, MM, DD, HH, NN, SS, paracetamol, Ag);
		+free[source(self)].  

// This rule was changed in order to find the deliver in a different location 
// The door could be a good place to get the order and then go to the fridge
// and when the drug is there update the beliefs

+!has(Ag, L)[source(Ag)] :
   	orderDrug(Ag) & free[source(self)] <- 
		.println("SECOND RULE ====================================");
		.wait(1000);
   		-free[source(self)]; 
		!at(enfermera, fridge);
		.send(repartidor, achieve, order(D, 5)); 
		!at(enfermera, delivery);     // go to deliver area and wait there.
		.wait(delivered);
		!at(enfermera, fridge);       // go to fridge 
		deliver(Product,5);
		+available(drug, fridge); 
		+free[source(self)];
		.println("Trying to bring drug after order it");
		!has(Ag, drug)[source(Ag)].               

// A different rule provided to not block the agent with contradictory petitions

+!has(Ag, drug)[source(Ag)] :
   	not free[source(self)] <- 
		.println("THIRD RULE ====================================");
		.println("The robot is busy and cann't attend the order now."); 
		.wait(4000);
		!has(Ag, drug).   
		
+!has(Ag, drug)[source(Ag)] 
   :  too_much(drug, Ag) & limit(drug, L) <-
      	.println("FOURTH RULE ====================================");
		.wait(1000);
		.concat("The Department of Health does not allow me to give you more than ", L,
                " drugs a day! I am very sorry about that!", M);
		.send(Ag, tell, msg(M)).

// If some problem appears, we manage it by informing the intention that fails 
// and the goal is trying to satisfy. Of course we can provide or manage the fail
// better by using error annotations. Remember examples on slides when introducing
// intentions as a kind of exception      

-!has(Name, P) <-
//   :  true
// No condition is the same that a constant true condition
	.println("FIFTH RULE ====================================");
	.wait(1000);
	.current_intention(I);
    .println("Failed to achieve goal: !has(", Name, " , ", P, ").");
	.println("Current intention is: ", I).

+!at(Ag, P) : at(Ag, P) <- 
	.println(Ag, " is at ",P);
	.wait(500).
+!at(Ag, P) : not at(Ag, P) <- 
	.println("Going to ", P, " <=======================");  
	.wait(200);
	!go(P);                                        
	.println("Checking if is at ", P, " ============>");
	!at(Ag, P).                                                    
	                                                                        
// when the supermarket makes a delivery, try the 'has' goal again
+delivered(drug, _Qtd, _OrderId)[source(repartidor)]
  :  true
  <- +delivered;
	 .wait(2000). 
	 
	 // Code changed from original example 
	 // +available(drug, fridge);
     // !has(owner, drug).

// When the fridge is opened, the drug stock is perceived
// and thus the available belief is updated
+stock(drug, 0)
   :  available(drug, fridge)
   <- -available(drug, fridge). 
   
+stock(drug, N)
   :  N > 0 & not available(drug, fridge)
   <- +available(drug, fridge).     
   
+chat(Msg)[source(Ag)] : answer(Msg, Answ) <-  
	.println("El agente ", Ag, " me ha chateado: ", Msg);
	.send(Ag, tell, msg(Answ)). 
                                     
+?time(T) : true
  <-  time.check(T).
