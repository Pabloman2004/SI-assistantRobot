//Desplazamiento,la coregla -go_at,sirve para indicar que no hay camino,en nuestro mundo significa que hay un agente delante por lo que a trav�s de prioridades le pide apartar.(Al ser solo 2 se hizo directamente).
pedido([],[]).
pedido([A|L],[A|L2]):- not reponiendo(A) & pedido(L,L2).//Lo tengo que reponer
pedido(L,[A|L2]):- pedido(L,L2).//Esta reponiendose no lo quiero
disponibilidad:- not .intend(delivered(P,Q)) & not reponiendo(_).
pararLaAccion:- not noHagasNada.
!comprobarcargar.
sinBateria:- bateria(X) & X<=0.
caduca(paracetamol,4).
caduca(amoxicilina,9).
caduca(ibuprofeno,14).
caduca(lorazepam,16).
caduca(aspirina,22).
//bateria(X) :- bateria(X).

+!go_at(auxiliar,P)[source(self)] : at(auxiliar,P)<- .print("He llegado a ",P).
+!go_at(auxiliar,P)[source(self)] : not at(auxiliar,P)  & bateria(X) & X>0
  <- move_towards(P);
     !go_at(auxiliar,P).
-!go_at(auxiliar,P)[source(self)]:bateria(X) & X>0<- .print("Estoy apartando");apartar;.wait(5);!go_at(auxiliar,P).
@sinBateria[atomic]
-!go_at(auxiliar,P)[source(self)]:bateria(X) & X>=0<-.print("No tengo bateria");.send(owner,achieve,sinBateria);while(sinBateria){.wait(1000)};!go_at(auxiliar,P).

+hour(H) : not horaAuxiliar(H)<-
	-horaAuxiliar(_);
	+horaAuxiliar(H);
	.findall(caduca(M,Q),caduca(M,Q),L);
	for(.member(caduca(M,Q),L))
	{
		R=Q-1;
		.abolish(caduca(M,Q));
		+caduca(M,R);
	}
	.findall(M,caduca(M,F) & F<=2 & not reponiendo(M),L2);
	if(not L2 == [])
	{	
		//!comprobar_bateria_movimiento(cabinet);
		.print("Van a caducar,solicito reponer las siguientes medicinas ",L2);
		!reponer(L2);
	}.
+!reponer(L): not reponiendo(_)
	<-.drop_intention(comprobarcargar);
	  .print("Robot solicita reponer ",L);
	  ?pedido(L1,L);
	  if(not L1 == [])
	  {
		  .send(proveedor,achieve,order(L1));
		  for(.member(M,L1))
		  {
			.abolish(caduca(M,_));
			+reponiendo(M);
		  };
	  };
	  !comprobarcargar.
+!reponer(L):reponiendo(T)<-?repondre(S);-repondre(_);+repondre([L|S]);.print("Ahora mismo no puedo reponer,quedará en la cola ",L).
+?repondre(A): A=[].//Asi añadimos vacío
+?pedido<-.print("Error al resolver").
@deliver[atomic]
+!delivered(Products,Qtd)[source(proveedor)]
  <- 
        .print("Ha llegado mercancia");
        !comprobar_bateria_movimiento(delivery);
        for(.member(Product,Products)){
            .print("Han llegado ", Qtd," ", Product);
            getDelivery(Product,Qtd);
            +carrying(Product, Qtd);
        }
  		.println("El auxiliar se dirige al cabinet");
		!comprobar_bateria_movimiento(cabinet);
        for(.member(Product,Products)){
			.print("He repuesto ",Product);
			-reponiendo(Product);
            addDrug(Product,Qtd);
            -carrying(Product,Qtd);
            .send(robot,tell,newAvailability(Product,Qtd));
        };
		.findall(A,repondre(A),LR1);
		if(not LR1== [])
		{
			.print(LR1);
			!!reponer(LR1);
		}.

@traer[atomic]
+!traer(robot,L)<-
	.abolish(noHagasNada);
	  !comprobar_bateria_movimiento(cabinet);
	  ?pararLaAccion;
      if(not at(owner,cabinet)){
         .send(owner, tell, quieto);
         open(cabinet);
         for(.member(pauta(M,H,F),L))
         {
            takeDrug(robot,M);
            .print("He cogido ", M);
         };
         close(cabinet);
         !comprobar_bateria_movimiento(robot);
		 .send(robot,tell,espera);
		 .send(robot,achieve,recibir(L));
			//!resetearPauta(M);
		 //!reponer;
         .send(owner, untell, quieto);
		 .print("He entregado al robot las medicinas");
      }else{
	  	.print("Ha llegado antes el owner,debe comprobarlo el robot");
      }.
-!traer(robot,L)<- .print("Robot me indica que deje de llevarle las medicinas").
+!comprobarcargar[source(self)]
   <- .random(X);
   .wait(3000*X + 5000);
   ?bateria(BAT);
      if(BAT < 195){
        !cargar;            
      }  
      !comprobarcargar.

+!cargar <-
   .print("Voy a cargar");
   !go_at(auxiliar,cargadorAuxiliar);
   !cargando.

+!cargando :at(auxiliar,cargadorAuxiliar)<-
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
+caduca(Product,X)[source(proveedor)]
	<- .abolish(caduca(Product,_));
		+caduca(Product,X).



+!comprobar_bateria_movimiento(Destino)
   <- getCost(Destino);
      .print("Tengo batería suficiente, puedo ir");
      !go_at(auxiliar,Destino).

-!comprobar_bateria_movimiento(Destino)
   <- .print("No tengo batería suficiente, voy a cargar");
      !cargar;
      !go_at(auxiliar,Destino).