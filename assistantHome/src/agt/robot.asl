/* Creencias iniciales y reglas */
sinBateria :- false.  % Eliminamos la gestión de la batería.
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
   for(.member(pauta(M, H, F), L)) {
      handDrug(M);
      !reducirCantidad(M);
      .print("Le he dado ", M);
   }
   .send(owner, untell, "Medicina entregada");

/* Plan de actualización de pauta */
+!resetearPauta(M)[source(self)] : pauta(M, H, F) 
<-
   .abolish(pauta(M, H, F));
   Y = H + F >= 24 ? H + F - 24 : H + F;
   +pauta(M, Y, F);
   .send(owner, tell, pauta(M, Y, F)).

/* Reducción de cantidad de medicamentos */
+!reducirCantidad(M)[source(self)] : cantidad(M, H) <- .abolish(cantidad(M, H)); +cantidad(M, H - 1).

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

/* Reglas de desplazamiento */
+!comprobar_bateria_movimiento(Destino)
   <- .print("Puedo moverme a ", Destino);
      !go_at(robot, Destino).

+!go_at(robot, P)[source(self)] : not at(robot, P)
  <- move_towards(P);
     !go_at(robot, P).
