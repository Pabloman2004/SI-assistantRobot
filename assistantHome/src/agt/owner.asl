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





!pauta_medicamentos.
!simulate_behaviour.

pauta(paracetamol,8,6).
pauta(ibuprofeno,12,6).
pauta(lorazepam,22,23).
pauta(aspirina,1,2).
pauta(amoxicilina,15,2).

pauta_nueva(brainal,20,12).
pauta_nueva(benadryl,20,12).
pauta_nueva(jarabe,20,12).


+pauta(M,H,F)[source(robot)] <- .abolish(pauta(M,H-F,_)).
+!pauta_medicamentos 
   <- .findall(pauta(A,B,C),.belief(pauta(A,B,C)),L);
   	  for(.member(I,L))
	  {
	  	.send(enfermera,tell,I);
	  }.
//Owner cambia las pautas,para ello utiliza numeros aleatorios e informa al robot.

+!simulate_behaviour[source(self)] 
   <- .random(X); .wait(3000*X + 5000); // wait for a random time
    if(X < 0.5){
    .random([delivery,fridge,washer,cabinet],Y);
    .print("Voy a un sitio ",Y);
    !go_at(owner,Y);
    }
    elif(X < 0.7){
    .random([chair1,chair2,chair3,chair4,sofa],Y);
    .print("Voy a sentarme en ",Y);
    !go_at(owner,Y);
    //sit(Y);
    .print("Me siento");
    }else{
    .random([bed1,bed2,bed3],Y);
    .print("Voy a echarme una siesta en ",Y);
    !go_at(owner,Y);
    .print("Me acuesto");
    }
    !simulate_behaviour.

@hour[atomic] // para que no se pueda ejecutar otra cosa si se esta ejecutando esta regla
+hour(H) <-
   .random(X);

   if(X < 0.7){   	  
      .findall(pauta(M,H,F),.belief(pauta(M,H,F)),L);
      if(not L == []){
      if(not .intend(tomarMedicina(L)) & not quieto){
		.print("Voy a por medicinas ");
      !tomarMedicina(L);
      }
     }
	 else{
		.print("No es hora de la medicina");
	 }
   }.

@day[atomic] // para que no se pueda ejecutar otra cosa si se esta ejecutando esta regla
+day(D)<-
   // 15% de probabilidad de cambiar las pautas 
   .random(X); // Genera un número aleatorio X
   if( X < 0.9){
   .print("Añadiendo medicamento a la pauta");
   .findall(pauta_nueva(M,H,F),pauta_nueva(M,H,F),L);
   .random(L,Nuevo); //se escoge un medicamento aleatorio de la lista de medicamentos nuevos
   .findall(Nuevo,.belief(pauta(M,H,F)),L2); //se comprueba si ya existe la pauta en la base de conocimiento
      if(L2 == []){ // si esta vacia significa que ya existe la pauta
         .print("Ya existe la pauta ",Nuevo);
         !mostrarPautaActual;
      }
      else{ // si no existe la pauta se añade a la base de conocimiento
         Nuevo = pauta_nueva(M, H, F); // Nuevo antes de esta linea contiene pauta_nueva(y los valores dependiendo de la pauta random que se cogio) con esta linea lo que hacemos ess poder acceder a esos valores
         .send(self, tell, pauta(M,H,F)); // se manda a si mismo la creencia de la nueva pauta
         .send(enfermera, tell, pauta(M,H,F)); // le manda al robot la nueva pauta
         !mostrarPautaActual;
      }
      
      
      
   }
   
   elif( X < 0.01) { // 20% de posibilidad de cambiar las pautas
   .findall(pauta(M, H, F), .belief(pauta(M, H, F)), L); // Encuentra todas las pautas

      if (not L == []) {
          // Extrae solo los nombres de los medicamentos (M)
          .findall(M, .member(pauta(M, _, _), L), Medicines); // Crea una lista con solo los nombres de los medicamentos

         .random(Medicines, M); // Escoge un medicamento aleatorio
         .print("Medicamento eliminado: ", M);
         .send(enfermera, tell, medicamentoEliminado(M)); // Informa al robot
         for(.member(pauta(M, H, F), L)) {
               .abolish(pauta(M,H,F)); // Elimina la pauta de la base de conocimiento
            }
            !mostrarPautaActual;
            }
         else {
         !curado;
      }


}.


+!curado <- .print("Paciente curado").
+!mostrarPautaActual <- .findall(pauta(M,H,F),.belief(pauta(M,H,F)),L);
   .print("Pautas actuales: ",L).

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
   !tomar(owner,L).
   

+!tomar(owner,L)[source(self)]
   <- !go_at(owner,cabinet);
      if(not at(enfermera,cabinet) & not quieto){
         open(cabinet);
		 .send(enfermera,achieve,comprueba(L));
         for(.member(pauta(M,H,F),L))
         {
            .abolish(pauta(M,H,F));
            !go_at(owner,cabinet);
    		   takeDrug(owner,M);
            .print("He cogido la medicina ", M);
            .send(enfermera, tell, comprobarConsumo(M));
         };
         for(.member(pauta(M,H,F),L))
         {
            handDrug(M);
         }
         close(cabinet);
		 !simulate_behaviour
      }.
// Initially Owner could be: sit, opening the door, waking up, walking, ...
//!sit.   			
//!check_bored. 

//+!init <- !sit ||| !open ||| !walk ||| !wakeup ||| !check_bored.


+espera <- 
	if(.intend(simulate_behaviour))
	{
		.drop_intention(simulate_behaviour);
		!simulate_behaviour;
	};
	.abolish(espera). 

+quieto
	<- .print("Espero por mis medicinas");
		if(.intend(tomarMedicina(_))){
			.drop_intention(tomarMedicina(_));
		}
		if(not .intend(simulate_behaviour) & not durmiendo){
			!simulate_behaviour;
		}.



/*
+day(D) <- 
   .random(X); // Genera un número aleatorio X
   .print("funciona bien? ",X);//comprobacion para ver cuando entra por aqui 
   if(X < 0.2) { // 20% de posibilidad de cambiar las pautas
   .findall(pauta(M, H, F), .belief(pauta(M, H, F)), L); // Encuentra todas las pautas

      if (not L == []) {
          // Extrae solo los nombres de los medicamentos (M)
          .findall(M, .member(pauta(M, _, _), L), Medicines); // Crea una lista con solo los nombres de los medicamentos

         .random(Medicines, M); // Escoge un medicamento aleatorio
         .print("Medicamento eliminado: ", M);
         .send(enfermera, tell, medicamentoEliminado(M)); // Informa al robot
         for(.member(pauta(M, H, F), L)) {
               .abolish(pauta(M,H,F)); // Elimina la pauta de la base de conocimiento
            }
            !mostrarPautaActual;
            }
         else {
         !curado;
       }
 
 }.

+!curado <- .print("Paciente curado").

+!mostrarPautaActual <- .findall(pauta(M,H,F),.belief(pauta(M,H,F)),L);
   .print("Pautas actuales: ",L).

+!curado <- 
.print("Estoy curado, no necesito medicinas").
*/
