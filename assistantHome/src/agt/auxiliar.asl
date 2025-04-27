




+!comprobar_bateria_movimiento(Destino)
   <- getCost(Destino);
      // una vez
      .print("Tengo bateria suficiente, puedo ir ");
      //mostrar_bateria;
      !go_at(enfermera,Destino).

-!comprobar_bateria_movimiento(Destino)
   <- .print("No tengo bateria suficiente, voy a cargar");
      !cargar;
      !go_at(enfermera,Destino).


+!go_at(enfermera,P)[source(self)] : at(auxiliar,P) <- .print("He llegado a ",P).
+!go_at(enfermera,P)[source(self)] : not at(auxiliar,P) & bateria(X) & X>0
  <- move_towards(P);
     !go_at(auxiliar,P).
-!go_at(auxiliar,P)[source(self)]<- apartar;.wait(5);!go_at(auxiliar,P).
@sinBateria[atomic]
-!go_at(auxiliar,P)[source(self)]:bateria(X) & X>=0<-.print("No tengo bateria").