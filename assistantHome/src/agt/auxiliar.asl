
carga_rapida(3).


+!cargar <-
   .print("Voy a cargar");
   !go_at(enfermera,cargadorRobot);
   .random(X);
   if(X<0.2){
      !cargaRapida;
   }  
   !cargando.

+!cargando :at(enfermera,cargadorRobot)<-
   ?bateria(X);
   ?cargaMaxima(Y);//creencia que pasamos desde el Env
   if(X<Y){
      cargar;    
      .wait(517);
      !cargando;
   }else{
      .print("He acabado de cargar.")
   }.

-!cargando
	<-.print("Ya no puedo cargar").

+!cargaRapida[source(self)] : at(enfermera,cargadorRobot) & cargaRapida(X)<- 
   .print("Carga rapida");
   if(X==0){
      .abolish(cargaRapida(X));
      +cargaRapida(3);
      reducirCapacidad;
   }
      .print("La carga rapida se puede llevar a cabo");
      .abolish(cargaRapida(X));
      +cargaRapida(X-1);
      cargaRapidaEnv;
      .print("Carga rapida realizada").

+!comprobar_bateria_movimiento(Destino)
   <- getCost(Destino);
      // una vez
      .print("Tengo bateria suficiente, puedo ir ");
      //mostrar_bateria;
      !go_at(auxiliar,Destino).

-!comprobar_bateria_movimiento(Destino)
   <- .print("No tengo bateria suficiente, voy a cargar");
      !cargar;
      !go_at(auxiliar,Destino).

+!day [source(owner)] <-
   .wait(1000);
   
   .findall(caducidad(M,X),.belief(caducidad(M,X)),L);
   .print("Medicamentos caducados: ",L);
   for(.member(caducidad(Med,Old), L)) {
      
      if(Old == 0){
         !comprobar_bateria_movimiento(delivery);
         .print("Renovando ",Med);
         .print("He llegado a delivery, recogiendo ",Med);
         reponerMedicamento(Med);
         !comprobar_bateria_movimiento(cabinet);
         .print("Guardando ",Med);
         
      }
      
   }.

+!llevarMedicina(L) <-
   .print("Acercando la medicina al robot");
   
   !go_at(auxiliar,cabinet);
   open(cabinet);
   for(.member(pauta(M,H,F),L))
         {
            takeDrug(auxiliar,M);
            .print("He cogido ", M);
         };
   close(cabinet);
   !go_at(auxiliar,enfermera);
   if(not at(auxiliar,enfermera))
   {   
   .send(enfermera,tell,espera);
   }
   .send(enfermera,achieve, llevarOwner(owner,L));   
   !go_at(auxiliar,delivery).

+!go_at(auxiliar,P)[source(self)] : at(auxiliar,P) <- .print("He llegado a ",P).
+!go_at(auxiliar,P)[source(self)] : not at(auxiliar,P) & bateria(X) & X>0
   <- move_towards(P);
      !go_at(auxiliar,P).
-!go_at(auxiliar,P)[source(self)]<- apartar;.wait(5);!go_at(auxiliar,P).
@sinBateria[atomic]
-!go_at(auxiliar,P)[source(self)]:bateria(X) & X>=0<-.print("No tengo bateria").