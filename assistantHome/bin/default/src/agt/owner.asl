/* Initial Beliefs */


/* Initial goals */

// Owner will simulate the behaviour of a person 
// We need to characterize their digital twin (DT)
// Owner must record the DT data periodically 
// Owner must access the historic data of such person
// Owner will act randomly according to some problems
// Owner will usually act with a behaviour normal
// Owner problems will be activated by some external actions
// Owner problems will randomly be activated on time
// Owner will dialog with the nurse robot 
// Owner will move randomly in the house by selecting places




!walk.

!wakeup.



pauta(paracetamol,8,6).
pauta(ibuprofeno,12,6).
pauta(lorazepam,22,23).
pauta(aspirina,8,8).
pauta(amoxicilina,15,2).

+pauta(M,H,F)[source(robot)] <- .abolish(pauta(M,H-F,_)).
+!pauta_medicamentos 
   <- .findall(pauta(A,B,C),.belief(pauta(A,B,C)),L);
   	  for(.member(I,L))
	  {
	  	.send(robot,tell,I);
	  }.
//Owner cambia las pautas,para ello utiliza n�meros aleatorios e informa al robot.
+!cambiarPauta(T)
	<-.findall(pauta(M,H,F),.belief(pauta(M,H,F)),L);
		.print("Reseteando medicinas:");
		for(.member(pauta(M,H,F),L))
		{
			if(not H==T)
			{
				.random([0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23],X);
				if(not X==T)
				{
					.abolish(pauta(M,H,F));
					.print("Nueva pauta:[",M,",",X,",",F,"]");
					+pauta(M,X,F);
					.send(enfermera,tell,pautaNueva(M,X,F));
				}
			}
		}.

+!simulate_behaviour[source(self)] 
   <- .random(X); .wait(3000*X + 5000); // wait for a random time
    if(X < 0.5){
    .random([delivery,fridge,washer,cabinet],Y);
    .print("Voy a un sitio ",Y);
    !go_at(owner,Y);
    }
    elif(X < 0.7){
    .random([chair1,chair2,chair3,chair4,sofa],Y);
    .print("Voy a sentarme en",Y);
    !go_at(owner,Y);
    //sit(Y);
    .print("Me siento");
    }else{
    .random([bed1,bed2,bed3],Y);
    .print("Voy a echarme una siesta en");
    !go_at(owner,Y);
    .print("Me acuesto");
    }
    !simulate_behaviour.

+!go_at(owner,P)[source(self)] : at(owner,P) <- .print("He llegado a ",P).
+!go_at(owner,P)[source(self)] : not at(owner,P)
  <- move_towards(P);
     !go_at(owner,P).
-!go_at(owner,P)[source(self)]
<- .send(enfermera,tell,aparta);
   !go_at(owner,P).

+!tomarMedicina(L)[source(self)]<-
   if(.intend(simulate_behaviour)){
      .drop_intention(simulate_behaviour);
   }
   !tomar(owner,L);
   !simulate_behaviour.

+!tomar(owner,L)[source(self)]
   <- !go_at(owner,cabinet);
      if(not at(enfermera,cabinet) & not quieto){
         open(cabinet);
		 .send(robot,achieve,comprueba(L));
         for(.member(pauta(M,H,F),L))
         {
            .abolish(pauta(M,H,F));
      	   takeDrug(owner,M);
            .print("He cogido la medicina", M);
            .send(enfermera, tell, comprobarConsumo(M));
         };
         for(.member(pauta(M,H,F),L))
         {
            handDrug(M);
         }
         close(cabinet);
      }.
// Initially Owner could be: sit, opening the door, waking up, walking, ...
//!sit.   			
//!check_bored. 

//+!init <- !sit ||| !open ||| !walk ||| !wakeup ||| !check_bored.

+!wakeup : .my_name(Ag) & not busy <-
	+busy;
	.println("Me he despertado jujuju");
    !simulate_behaviour;
	.wait(3000);
	-busy.
	
+!wakeup : .my_name(Ag) <-
	.println("Owner is doing something now, is not asleep");
	.wait(10000);
	!wakeup.
	
+!walk : .my_name(Ag) & not busy <- 
	+busy;  
	.println("Owner is not busy, is sit down on the sofa");
	.wait(500);
	!at(Ag,sofa);
	.wait(2000);
	//.println("Owner is walking at home"); 
	-busy.
	
+!walk : .my_name(Ag) & busy <-
	.println("Owner is doing something now and could not walk");
	.wait(6000);
	!walk.


 


+!at(Ag, P) : at(Ag, P) <- 
	.println("Owner is at ",P);
	.wait(5000).
+!at(Ag, P) : not at(Ag, P) <- 
	.println("Going to ", P);
	!go(P);                                        
	.println("Checking if is at ", P);
	!at(Ag, P).            
	                                                   
+!go(P) : atRoom(RoomAg) & atRoom(P, RoomAg) <-                             
	.println("Al estar en la misma habitación se debe mover directamente a: ", P);
	move_towards(P).  
+!go(P) : atRoom(RoomAg) & atRoom(P, RoomP) & not RoomAg == RoomP &
		  connect(RoomAg, RoomP, Door) & atDoor <-
	.println("Al estar en la puerta ", Door, " se dirige a ", P);                        
	move_towards(P); 
	!go(P).       
+!go(P) : atRoom(RoomAg) & atRoom(P, RoomP) & not RoomAg == RoomP &
		  connect(RoomAg, RoomP, Door) & not atDoor <-
	.println("Al estar en una habitación contigua se mueve hacia la puerta: ", Door);
	move_towards(Door); 
	!go(P).       
+!go(P) : atRoom(RoomAg) & atRoom(P, RoomP) & not RoomAg == RoomP &
		  not connect(RoomAg, RoomP, _) & connect(RoomAg, Room, DoorR) &
		  connect(Room, RoomP, DoorP) & not atDoor <-
	.println("Se mueve a: ", DoorR, " para ir a la habitación contigua, ", Room);
	move_towards(DoorR); 
	!go(P). 
+!go(P) : atRoom(RoomAg) & atRoom(P, RoomP) & not RoomAg == RoomP &
		  not connect(RoomAg, RoomP, _) & connect(RoomAg, Room, DoorR) &
		  connect(Room, RoomP, DoorP) & atDoor <-
	.println("Se mueve a: ", DoorP, " para ir a la habitación ", RoomP);
	move_towards(DoorP); 
	!go(P). 
+!go(P) : atRoom(RoomAg) & atRoom(P, RoomP) & not RoomAg == RoomP <-
	.println("Owner is at ", RoomAg,", that is not a contiguous room to ", RoomP);
	move_towards(P).                                                          
-!go(P) <- .println("Something goes wrong......").
	                                                                        
	
+!get(drug) : .my_name(Name) <- 
   Time = math.ceil(math.random(4000));
   .println("I am waiting ", Time, " ms. before asking the nurse robot for my medicine.");
   .wait(Time);
   .send(enfermera, achieve, has(Name, drug)).

+has(owner,drug) : true <-
   .println("Owner take the drug.");
   !take(drug).
-has(owner,drug) : true <-
   .println("Owner ask for drug. It is time to take it.");
   !get(drug).
                                       
// while I have drug, sip
+!take(drug) : has(owner, drug) <-
   sip(drug);
   .println("Owner is siping the drug.");
   !take(drug).
+!take(drug) : not has(owner, drug) <-
   .println("Owner has finished to take the drug.").//;
   //-asked(drug).

+msg(M)[source(Ag)] : .my_name(Name)
   <- .print(Ag, " send ", Name, " the message: ", M);
      -msg(M).