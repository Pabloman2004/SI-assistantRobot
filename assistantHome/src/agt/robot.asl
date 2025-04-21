/* Initial beliefs and rules */  

// initially, robot is free
free.

cantidad(paracetamol,20).
cantidad(ibuprofeno,20).
cantidad(aspirina,20).
cantidad(lorazepam,20).
cantidad(amoxicilina,20).

//Owner le indica al robot la nueva pauta de medicinas.

!simulate_behaviour.   
/* Plans */
//La simulación del robot es bastante sencilla,se desplaza a 3 posiciones.
+!simulate_behaviour[source(self)] 
   <- .random(X); .wait(3000*X + 5000); // wait for a random time
    
    .random([delivery,fridge,washer,cabinet],Y);
    .print("Voy a un sitio ",Y);
    !comprobar_bateria_movimiento(Y);
    !simulate_behaviour.

+!go_at(enfermera,P)[source(self)] : at(enfermera,P) <- .print("He llegado a ",P).
+!go_at(enfermera,P)[source(self)] : not at(enfermera,P) & bateria(X) & X>0
  <- move_towards(P);
     !go_at(enfermera,P).
-!go_at(enfermera,P)[source(self)]<- apartar;.wait(5);!go_at(enfermera,P).
@sinBateria[atomic]
-!go_at(robot,P)[source(self)]:bateria(X) & X>=0<-.print("No tengo bateria").

//no se modicifica ya que se comprueba la bateria en entregarMedicina
+hour(H)<-
	.findall(pauta(M,H,F),.belief(pauta(M,H,F)),L);
     if(not L == []){
      if(not .intend(entregarMedicina(L))){
         .print("Hora de la medicina");
         !entregarMedicina(L);
      }
     }.

//hay que modificarlo para tener en cuenta la bateria
+!entregarMedicina(L)[source(self)]<-
	.abolish(disponibilidad);
		//if(.intend(simulate_behaviour)){
			//.drop_intention(simulate_behaviour);
		//}
		.print("Las entrego yo");
		!bring(owner,L).
		//!simulate_behaviour;
	
//metodo que no hay que modificar
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

//modificar para comprobar bateria
@medicina[atomic]
+!bring(owner,L)[source(self)]
   <- 
      if(not at(owner,cabinet) & not .belief(comprobarConsumo(_))){
         .send(owner, tell, quieto);
         open(cabinet);
         for(.member(pauta(M,H,F),L))
         {
            takeDrug(enfermera,M);
            !reducirCantidad(M);
            !go_at(enfermera,cabinet);
            .print("He cogido ", M);
         };
         close(cabinet);
		 .send(owner,tell,espera);
         for(.member(pauta(M,H,F),L))
         {       
         !go_at(enfermera,owner);
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
      }.
	  
//no se modifica
@recibir[atomic]
+!recibir(L)<-
		
		 .send(owner,tell,espera);
         for(.member(pauta(M,H,F),L))
         {
		 	!reducirCantidad(M);
		 	.print("Le he dado ", M);
            handDrug(M);
			!resetearPauta(M);
         }.

//no se modifica
@comprueba[atomic]
+!comprueba(L)<-
		 .findall(M,.belief(comprobarConsumo(M)),X);
         for(.member(M,X)){
		 	.print("Compruebo el consumo de ",M);
            !comprobarConsumo(M)
		 }.

// no se modifica
+!comprobarConsumo(M)[source(self)] : cantidad(M,H) <- 
   .drop_intention(simulate_behaviour);//deja de hacer lo que estaba haciendo
   !go_at(enfermera,cabinet);
   open(cabinet);
	!comprobar(M,H);
   close(cabinet).

//si le manda el owner el mensaje de que ha tomado la medicina, el robot lo comprueba.
+comprobarConsumo(M)[source(owner)] : cantidad(M,H)<- 
   .print("Comprobando consumo ",M);
   !go_at(enfermera,cabinet);
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
	.print("No ha cogido ",M,"!");
	close(cabinet).


+aparta[source(owner)] <- .print("Debo de apartarme");-aparta.


+delivered(drug, _Qtd, _OrderId)[source(repartidor)]
  :  true
  <- +delivered;
	 .wait(2000). 
	 
+medicamentoEliminado(M)<-
   .abolish(pauta(M,H,F));
   .abolish(cantidad(M,H));
   .print("Medicamento eliminado de la pauta: ",M).

+!cargar <-
   .print("Voy a cargar");
   !go_at(enfermera,cargadorRobot);
   !cargando.

+!cargando :at(enfermera,cargadorRobot)<-
   ?bateria(X);
   if(X<99){
      cargar;
      .print("Estoy cargando. ",X);
      
      .wait(500);
      !cargando;
   }else{
      .print("He acabado de cargar.")
   }.
-!cargando
	<-.print("Ya no puedo cargar").

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