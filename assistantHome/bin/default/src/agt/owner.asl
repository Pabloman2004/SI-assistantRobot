/* Initial goals */
!pauta_medicamentos.

// Pautas: nombre, hora, frecuencia.
pauta(paracetamol,8,6).
pauta(ibuprofeno,12,6).
pauta(lorazepam,22,23).
pauta(aspirina,8,8).
pauta(amoxicilina,15,2).

// El robot controla los horarios y actualiza las pautas tras una consumición.
+pauta(M,H,F)[source(robot)] <- 
    .abolish(pauta(M,H-F,_)).

// Enviamos al owner las pautas iniciales.
+!pauta_medicamentos 
<- 
    .findall(pauta(A,B,C),.belief(pauta(A,B,C)),L);
    for (.member(I,L)) {
        .send(enfermera,tell,I);
    }.

// El owner cambia las pautas aleatoriamente e informa al robot.
+!cambiarPauta(T) 
<- 
    .findall(pauta(M,H,F),.belief(pauta(M,H,F)),L);
    .print("Reseteando medicinas:");
    for (.member(pauta(M,H,F),L)) {
        if (not H==T) {
            .random([0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23],X);
            if (not X==T) {
                .abolish(pauta(M,H,F));
                .print("Nueva pauta: [",M,",",X,",",F,"]");
                +pauta(M,X,F);
                .send(enfermera,tell,pautaNueva(M,X,F));
            }
        }
    }.

// Simulamos comportamiento aleatorio.
+!simulate_behaviour[source(self)] 
<- 
    .random(X); 
    .wait(3000*X + 5000); // Espera un tiempo aleatorio
    if (X < 0.5) {
        .random([delivery,fridge,washer,retrete],Y);
        .print("Voy a un sitio ",Y);
        !go_at(owner,Y);
    } elif (X < 0.7) {
        .random([chair1,chair2,chair3,chair4,sofa],Y);
        .print("Voy a sentarme en ",Y);
        !go_at(owner,Y);
        .print("Me siento");
    } else {
        .random([bed1,bed2,bed3],Y);
        .print("Voy a echarme una siesta en ",Y);
        !go_at(owner,Y);
        .print("Me acuesto");
    };
    !simulate_behaviour.

// Si es hora de la pauta, el owner puede decidir ir por la medicina.
+hour(H) 
<- 
    .random(X);
    if (X < 0.9) {
        .print("Voy a por medicinas");
        .findall(pauta(M,H,F),.belief(pauta(M,H,F)),L);
        if (not L == []) {
            if (not .intend(tomarMedicina(L)) & not quieto) {
                .print(L);
                !tomarMedicina(L);
            }
        }
    }.

// Cuando decide tomar medicinas, detiene su comportamiento.
+!tomarMedicina(L)[source(self)] 
<- 
    if (.intend(simulate_behaviour)) {
        .drop_intention(simulate_behaviour);
    };
    !tomar(owner,L);
    !simulate_behaviour.

// Va al cabinet, toma la medicina e informa al robot.
+!tomar(owner,L)[source(self)] 
<- 
    !go_at(owner,cabinet);
    if (not at(robot,cabinet) & not quieto) {
        open(cabinet);
        .send(robot,achieve,comprueba(L));
        for (.member(pauta(M,H,F),L)) {
            .abolish(pauta(M,H,F));
            takeDrug(owner,M);
            .print("He cogido la medicina ",M);
            .send(robot,tell,comprobarConsumo(M));
        };
        for (.member(pauta(M,H,F),L)) {
            handDrug(M);
        };
        close(cabinet);
    }.

// Si el robot se adelanta, el owner espera.
+quieto 
<- 
    .print("Espero por mis medicinas");
    if (.intend(tomarMedicina(_))) {
        .drop_intention(tomarMedicina(_));
    };
    if (not .intend(simulate_behaviour)) {
        !simulate_behaviour;
    }.

// Movimiento básico con prioridad.
+!go_at(owner,P)[source(self)] : at(owner,P) 
<- 
    .print("He llegado a ",P).

+!go_at(owner,P)[source(self)] : not at(owner,P)
<- 
    move_towards(P);
    !go_at(owner,P).

-!go_at(owner,P)[source(self)] 
<- 
    .send(robot,tell,aparta);
    !go_at(owner,P).

// Cuando tiene la medicina en la mano, la consume.
+has(owner,A) : true 
<- 
    !consume(A).

+!consume(A)[source(self)] : not .intend(consume(A))
<- 
    .print("Voy a tomar ",A);
    consume(A);
    .wait(2000);
    .print("He tomado ",A).
