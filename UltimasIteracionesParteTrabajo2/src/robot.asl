/* Initial beliefs and rules */
// initially, I believe that there is some beer in the fridge
//Cantidad inicial de medicamentos,asumiremos que en esta simulación 50 es stock ilimitado.
sinBateria:- bateria(X) & X<=0.
cantidad(paracetamol,20).
cantidad(ibuprofeno,20).
cantidad(aspirina,20).
cantidad(lorazepam,20).
cantidad(amoxicilina,20).
+newAvailability(M,Qtd)<--cantidad(M,_);+cantidad(M,Qtd);.abolish(newAvailability(M,Qtd)).
//El robot activa su simulación tanto día como noche
!simulate_behaviour.   
/* Plans */
//La simulación del robot es bastante sencilla,se desplaza a 3 posiciones.
+!simulate_behaviour[source(self)]
   <- .random(X);
   .wait(3000*X + 5000);
   ?bateria(BAT);
   ALPHA =  1 - (BAT-50)/200;
      if(X < ALPHA & BAT < 180){
         if(not .intend(go_at(robot,Y))){
            !cargar;         
         }
      }
      else{
          // wait for a random time
         .random([delivery,fridge,washer],Y);
         if(not .intend(go_at(robot,Y))){
	         .print("Voy a un sitio ",Y);
            !comprobar_bateria_movimiento(Y);
         }
      }
      !simulate_behaviour.
//Desplazamiento,la coregla -go_at,sirve para indicar que no hay camino,en nuestro mundo significa que hay un agente delante por lo que a trav�s de prioridades le pide apartar.(Al ser solo 2 se hizo directamente).
+!go_at(robot,P)[source(self)] : at(robot,P) <- .print("He llegado a ",P).
+!go_at(robot,P)[source(self)] : not at(robot,P)  & bateria(X) & X>0
  <- move_towards(P);
     !go_at(robot,P).
-!go_at(robot,P)[source(self)]:bateria(X) & X>0<- .print("Estoy apartando");apartar;.wait(5);!go_at(robot,P).
@sinBateria[atomic]
-!go_at(robot,P)[source(self)]:bateria(X) & X>=0<-.print("No tengo bateria");.send(owner,achieve,sinBateria);while(sinBateria){.wait(1000)};!go_at(robot,P).

//Owner le indica al robot la nueva pauta de medicinas.
+pautaNueva(M,H,F)[source(owner)]<-.abolish(pauta(M,_,_));+pauta(M,H,F);.abolish(pautaNueva(M,H,F)).

//Robot comprueba en cada hora si tiene que tomar una medicina,en dicho caso entregara la lista de medicinas requerida.
+hour(H)<-
	.findall(pauta(M,H,F),.belief(pauta(M,H,F)),L);
     if(not L == []){
      if(not .intend(entregarMedicina(L))){
         .print("Hora de la medicina");
         !entregarMedicina(L);
      }
     }.
//Robot suspende su simulación de comportamiento y lanza un plan más prioritario que es llevarle las medicinas al owner.
+!entregarMedicina(L)[source(self)]<-
    ?bateria(X);
	.send(auxiliar,askOne,bateria(Z),bateria(Y))  ||| .wait(5000);
    .send(auxiliar,askOne,disponibilidad,F) ||| .wait(500);//El auxiliar en un ATOMIC no nos va responder,debemos tener una condici�n de salida
   .abolish(disponibilidad);
   if(X>(Y-25) | not F == disponibilidad[source(auxiliar)])//Robot si tiene m�s de 50 de bateria |  no est� disponible el auxiliar
   {
	   //if(.intend(simulate_behaviour)){
		  //.drop_intention(simulate_behaviour);
	   //}
	   .print("Las entrego yo");
	   !bring(owner,L);
	   //!simulate_behaviour;
   }
   else
   {
   	.print("Me las trae el auxiliar");
   	.send(auxiliar,achieve,traer(robot,L));
   }.
//Robot calcula la nueva hora que se debe tomar el medicamento dado y se lo informa al owner.
+!resetearPauta(M)[source(self)] : pauta(M,H,F) 
<- 
   .abolish(pauta(M,H,F));
    //Y = H+F;
    if(H+F>= 24){
        Y = H+F-24;
    }
	else
	{
		Y=H+F
	}
   +pauta(M,Y,F);
   .send(owner,tell,pauta(M,Y,F)).
//Robot modifica su conocimiento de la cantidad que hab�a de un medicamento dado.
+!reducirCantidad(M)[source(self)] : cantidad(M,H) <- .abolish(cantidad(M,H)); +cantidad(M,H-1).

//Robot ir� por las medicinas y se las dar� al owner en la mano,en el caso de que hubiese condici�n de carrera y llegase el owner,comprobar� el consumo.
@medicina[atomic]
+!bring(owner,L)[source(self)]
   <- !comprobar_bateria_movimiento(cabinet);
      if(not at(owner,cabinet) & not .belief(comprobarConsumo(_))){
         .send(owner, tell, quieto);
         open(cabinet);
         for(.member(pauta(M,H,F),L))
         {
            takeDrug(robot,M);
            !reducirCantidad(M);
            .print("He cogido ", M);
         };
         close(cabinet);
         !comprobar_bateria_movimiento(owner);
		 .send(owner,tell,espera);
         for(.member(pauta(M,H,F),L))
         {
		 	.print("Le he dado ", M);
            handDrug(M);
			!resetearPauta(M);
         }
         .send(owner, untell, quieto);
      }else{
	  	.wait(2000);
         .findall(M,.belief(comprobarConsumo(M)),X);
         for(.member(M,X)){
		 	.print("Compruebo el consumo de ",M);
            !comprobarConsumo(M);
            .abolish(comprobarConsumo(M));
         }
      };
	  !reponer.
@recibir[atomic]
+!recibir(L)<-
		!comprobar_bateria_movimiento(owner);
		 .send(owner,tell,espera);
         for(.member(pauta(M,H,F),L))
         {
		 	!reducirCantidad(M);
		 	.print("Le he dado ", M);
            handDrug(M);
			!resetearPauta(M);
         }
		 !reponer.
@comprueba[atomic]

+!comprueba(L)<-
		.send(auxiliar,tell,noHagasNada);//No puese ser unAchieve por atomicidad de traer en auxiliar
		!comprobar_bateria_movimiento(cabinet);
		.wait(200);
		 .findall(M,.belief(comprobarConsumo(M)),X);
         for(.member(M,X)){
		 	.print("Compruebo el consumo de ",M);
            !comprobarConsumo(M);
            .abolish(comprobarConsumo(M));
         }
		 !reponer.
+!reponer<-
	.print("entro");
	.findall(M,.belief(pauta(M,H,F)) & cantidad(M,0),L);
	if(not L==[])
	{
		.print("Tenemos que reponer las medicinas");
		.send(auxiliar,achieve,reponer(L));
	}.
//Regla de error
-!bring(_,_)[source(self)]
   :  true
   <- .print("").
//Robot comprueba que el owner ha cogido el medicamento del cabinet.
+!comprobarConsumo(M)[source(self)] : cantidad(M,H) <- 
   open(cabinet);
	!comprobar(M,H);
   close(cabinet).
//Robot revisa en su conocimiento si hay diferencia entre lo que ve en el cabinet y lo que sab�a que hab�a(-1).
+!comprobar(M,H)[source(self)]  <-
	comprobarConsumo(M,H);
	.print("Es verdad que ha cogido ",M);
	!reducirCantidad(M);
   !resetearPauta(M).
//Robot se da cuenta que el owner no ha tomado la medicina,(no trataremos que el owner lo mienta).
-!comprobar(M,H)[source(self)]  <-
	.print("No ha cogido",M,"!");
	close(cabinet).

//El robot sabe que tiene que apartar,en nuestro código al ser 2 agentes se ha simplificado y se ha trarado en el go_at,en presencia de más agentes se debería tratar como se ve aquí.
+aparta[source(owner)] <- .print("Debo de apartarme");-aparta.

+!cargar <-
   .print("Voy a cargar");
   !go_at(robot,cargadorRobot);
   !cargando.

+!cargando :at(robot,cargadorRobot)<-
   ?bateria(X);
   if(X<199){
      cargar;
      .print("Estoy cargando.");
      .wait(500);
      !cargando;
   }else{
      .print("He acabado de cargar.")
   }.
-!cargando
	<-.print("Ya no puedo cargar").
+!comprobar_bateria_movimiento(Destino)
   <- getCost(Destino);
      .print("Tengo bateria suficiente, puedo ir");
      !go_at(robot,Destino).

-!comprobar_bateria_movimiento(Destino)
   <- .print("No tengo bateria suficiente, voy a cargar");
      !cargar;
      !go_at(robot,Destino).
