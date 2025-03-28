/* Initial goals */
//Sirve para enviar al owner pautas iniciales.
!pauta_medicamentos.

//Pautas:nombre,hora,frecuencia.
pauta(paracetamol,8,6).
pauta(ibuprofeno,12,6).
pauta(lorazepam,22,23).
pauta(aspirina,8,8).
pauta(amoxicilina,15,2).


//El robot es quien controla los horarios,es decir,actualiza tras una consumici�n,por lo tanto debe indicarle al owner la nueva hora.
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
					.send(robot,tell,pautaNueva(M,X,F));
				}
			}
		}.
//Cuando es de noche,el owner se va a dormir y suspende su simulaci�n de comportamiento.El owner tambien reetear� las pautas.	
+noche : not durmiendo & hour(T) <-
	if(.intend(simulate_behaviour))
	{
		.drop_intention(simulate_behaviour);
	};
	//Cambiamos pautas
	!cambiarPauta(T);
    .random([bed3,bed2,bed1],Y);
	.print("Es de noche, voy a dormir a ",Y);
	!go_at(owner,Y);
    +durmiendo.
//Al ser de d�a el owner se despierta.
+dia : durmiendo
<-	-durmiendo;
	.print("He despertado");
	!simulate_behaviour.
//Simulamos comportamiento,utilizamos la aleatoriedad para distribuirlo en 3 casos:1)Moverse a elementos actuables2)Sentarse3)Siesta.
+!simulate_behaviour[source(self)] : not durmiendo
   <- .random(X); .wait(3000*X + 5000); // wait for a random time
     if(X < 0.5){
      .random([delivery,fridge,washer,retrete],Y);
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
//El owner si es hora de la pauta,tiene una probabilidad A(P(A)=0.2) de ir por la pauta.
+hour(H) : dia<-
   .random(X);
   if(X < 0.9){
   	  .print("Voy a por medicinas");
      .findall(pauta(M,H,F),.belief(pauta(M,H,F)),L);
      if(not L == []){
      if(not .intend(tomarMedicina(L)) & not quieto){
         .print(L);
         !tomarMedicina(L);
      }
     }
   }.
	
//Owner va por las medicinas,por tanto suspende su comportamiento.
//@tomarMedicina[atomic]
+!tomarMedicina(L)[source(self)]<-
   if(.intend(simulate_behaviour)){
      .drop_intention(simulate_behaviour);
   }
   !tomar(owner,L);
   !simulate_behaviour.
//Owner va hacia el cabinet,selecciona las medicinas y las toma.En su conclusi�n,informa al robot de la consumici�n.
+!tomar(owner,L)[source(self)]
   <- !go_at(owner,cabinet);
      if(not at(robot,cabinet) & not quieto){
         open(cabinet);
		 .send(robot,achieve,comprueba(L));
         for(.member(pauta(M,H,F),L))
         {
            .abolish(pauta(M,H,F));
      	   takeDrug(owner,M);
            .print("He cogido la medicina", M);
            .send(robot, tell, comprobarConsumo(M));
         };
         for(.member(pauta(M,H,F),L))
         {
            handDrug(M);
         }
         close(cabinet);
      }.
//Cuando el robot le da las medicinas en la mano,le pide al owner que se quede quieto.
+espera :not durmiendo<- 
	if(.intend(simulate_behaviour))
	{
		.drop_intention(simulate_behaviour);
		!simulate_behaviour;
	};
	.abolish(espera).
//El robot resuelve la condici�n de carrera de ir al cabinet inform�ndole que ya que el ha llegado primero el se las dar�.
+quieto
   <- .print("Espero por mis medicinas");
      if(.intend(tomarMedicina(_))){
         .drop_intention(tomarMedicina(_));
      }
      if(not .intend(simulate_behaviour) & not durmiendo){
         !simulate_behaviour;
      }.
//Desplazamiento,la coregla -go_at,sirve para indicar que no hay camino,en nuestro mundo significa que hay un agente delante por lo que a trav�s de prioridades le pide apartar.(Al ser solo 2 se hizo directamente).
+!go_at(owner,P)[source(self)] : at(owner,P) <- .print("He llegado a ",P).
+!go_at(owner,P)[source(self)] : not at(owner,P)
  <- move_towards(P);
     !go_at(owner,P).
-!go_at(owner,P)[source(self)]
<- .send(robot,tell,aparta);
   !go_at(owner,P).
//Owner tiene medicina en la mano y procede a consumirla.
+has(owner,A) : true
   	<-!consume(A).
//Consumici�n de la medicina.
+!consume(A)[source(self)] : not .intend(consume(A))
   <- 
   .print("Voy a tomar ",A);
   consume(A);
   .wait(2000);
   .print("He tomado ",A).
@sinBateria[atomic]  
+!sinBateria[source(A)]<-
	.print("Voy darle bateria");
	!go_at(Owner,A);
	setBateria(A).