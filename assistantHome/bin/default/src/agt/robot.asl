/* Creencias iniciales y reglas */
sinBateria :- false. 
cantidad(paracetamol, 20).
cantidad(ibuprofeno, 20).
cantidad(aspirina, 20).
cantidad(lorazepam, 20).
cantidad(amoxicilina, 20).
+newAvailability(M, Qtd) <-- cantidad(M, _); +cantidad(M, Qtd);.
abolish(newAvailability(M, Qtd)).

/* Planes */
+!simulate_behaviour[source(self)]
   <- .random(X);
   .wait(3000*X + 5000);
   .random([delivery, fridge, washer], Y);
   if(not .intend(go_at(robot, Y))) {
       .print("Voy a un sitio ", Y);
       !comprobar_bateria_movimiento(Y);
   }
   !simulate_behaviour.

/* Desplazamiento */
+!go_at(robot, P)[source(self)] : at(robot, P) <- .print("He llegado a ", P).
+!go_at(robot, P)[source(self)] : not at(robot, P)
  <- move_towards(P);
     !go_at(robot, P).
-!go_at(robot,P)[source(self)]: X>0<- .print("Estoy apartando");apartar;.wait(5);!go_at(robot,P).

+pautaNueva(M,H,F)[source(owner)]<-.abolish(pauta(M,_,_));+pauta(M,H,F);.abolish(pautaNueva(M,H,F)).

/* Comprobaciones de medicina */
+hour(H) <-
    .findall(pauta(M, H, F), .belief(pauta(M, H, F)), L);
    if(not L == []) {
        if(not .intend(entregarMedicina(L))) {
            .print("Hora de la medicina");
            !entregarMedicina(L);
        }
    }.

/* Plan de entrega de medicina */
+!entregarMedicina(L)[source(self)] <-
   .send(owner, tell, "Estoy entregando la medicina");
   .print("Entregando medicinas");
   !bring(owner, L);
   .print("Le he dado ", M);   
   .send(owner, untell, "Medicina entregada");

/* Plan de actualización de pauta */
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
+!reducirCantidad(M)[source(self)] : cantidad(M,H) <- .abolish(cantidad(M,H)); +cantidad(M,H-1); +cantidad(M, H - 1).

@medicina[atomic]
+!bring(owner,L)[source(self)]
      if(not at(owner,fridge) & not .belief(comprobarConsumo(_))){
         .send(owner, tell, quieto);
         open(fridge);
         for(.member(pauta(M,H,F),L))
         {
            takeDrug(robot,M);
            !reducirCantidad(M);
            .print("He cogido ", M);
         };
         close(fridge);
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

/* Reposición de medicamentos */
+!reponer <-
   .print("Tenemos que reponer las medicinas");
   .findall(M, .belief(pauta(M, H, F)) & cantidad(M, 0), L);
   if(not L == []) {
      .send(owner, tell, "Reponer medicinas");
      for(.member(M, L)) {
         .print("Reponiendo ", M);
         +cantidad(M, 20);  % Reponemos 20 unidades por cada medicina
      }
   }
   


+!comprobarConsumo(M)[source(self)] : cantidad(M,H) <- 
   open(cabinet);
	!comprobar(M,H);
   close(cabinet).

+!comprobar(M,H)[source(self)]  <-
	comprobarConsumo(M,H);
	.print("Es verdad que ha cogido ",M);
	!reducirCantidad(M);
   !resetearPauta(M).

//Robot se da cuenta que el owner no ha tomado la medicina,(no trataremos que el owner lo mienta).
-!comprobar(M,H)[source(self)]  <-
	.print("No ha cogido",M,"!");
	close(cabinet).

+!go_at(robot, P)[source(self)] : not at(robot, P)
  <- move_towards(P);
     !go_at(robot, P).

