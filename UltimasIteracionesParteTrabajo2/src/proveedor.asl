qtd(20).

// plan to achieve the goal "order" for agent Ag
+!order(Products)[source(Ag)] : true
  <-
    ?qtd(A);
     .wait(4000);
     .send(Ag, achieve, delivered(Products,A));
     for(.member(Product, Products)){
        deliver(Product,A);
	    .random([10],X);
       .send(Ag, tell, caduca(Product,X));
     }.
     

