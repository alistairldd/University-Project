


void salleFin(){
  textureMode(NORMAL);
  beginShape(QUAD);
  texture(stone);
  vertex(0, 0, 0, 0, 1); // mur sud
  vertex(14*tailleCube, 0, 0, 0, 0);
  vertex(14*tailleCube, 0, 2*tailleCube, 1,0);
  vertex(0, 0, 2*tailleCube, 1, 1);
  
  vertex(14*tailleCube, 0, 0, 0, 1); // mur ouest
  vertex(14*tailleCube, 14*tailleCube, 0, 0, 0);
  vertex(14*tailleCube, 14*tailleCube, 2*tailleCube, 1, 0);
  vertex(14*tailleCube, 0, 2*tailleCube, 1, 1);
  
  vertex(14*tailleCube, 2*tailleCube, 0, 0, 1); // mur nord
  vertex(0, 14*tailleCube, 0, 0, 0);
  vertex(0, 14*tailleCube, 2*tailleCube, 1, 0);
  vertex(14*tailleCube, 14*tailleCube, 2*tailleCube, 1, 1);
  
  vertex(0, 14*tailleCube, 0, 0, 1); // mur est
  vertex(0, 0, 0, 0, 0);
  vertex(0, 0, 2*tailleCube, 1, 0);
  vertex(0, 14*tailleCube, 2*tailleCube, 1, 1);
  endShape();  
}


void lingot(){
  pushMatrix();
  textureMode(NORMAL);
  float largeur = 100;    // largeur à la base du lingot
  float hauteur = 40;     // hauteur du lingot
  float profondeur = 50;  // profondeur à la base du lingot
  float biseau = 0.8;     // biseau du lingot
  
  // Paramètres du piédestal
  float baseL = 140;      // largeur du piédestal
  float baseP = 80;       // profondeur du piédestal
  float baseH = 30;       // hauteur du piédestal
  
  float piedL = 20;
  float piedP = 20;
  float piedH = 300;
    
  rotateX(-PI/4);
  rotateY(frameCount*0.05);
  fill(80); // couleur grise
  beginShape(QUADS);
  
  // dessous
  vertex(-baseL/2, baseH/2, -baseP/2);
  vertex(baseL/2, baseH/2, -baseP/2);
  vertex(baseL/2, baseH/2, baseP/2);
  vertex(-baseL/2, baseH/2, baseP/2);
  
  // dessus
  vertex(-baseL/2, -baseH/2, -baseP/2);
  vertex(baseL/2, -baseH/2, -baseP/2);
  vertex(baseL/2, -baseH/2, baseP/2);
  vertex(-baseL/2, -baseH/2, baseP/2);
  
  // côtés
  vertex(-baseL/2, baseH/2, -baseP/2);
  vertex(baseL/2, baseH/2, -baseP/2);
  vertex(baseL/2, -baseH/2, -baseP/2);
  vertex(-baseL/2, -baseH/2, -baseP/2);
  
  vertex(-baseL/2, baseH/2, baseP/2);
  vertex(baseL/2, baseH/2, baseP/2);
  vertex(baseL/2, -baseH/2, baseP/2);
  vertex(-baseL/2, -baseH/2, baseP/2);
  
  vertex(-baseL/2, baseH/2, -baseP/2);
  vertex(-baseL/2, baseH/2, baseP/2);
  vertex(-baseL/2, -baseH/2, baseP/2);
  vertex(-baseL/2, -baseH/2, -baseP/2);
  
  vertex(baseL/2, baseH/2, -baseP/2);
  vertex(baseL/2, baseH/2, baseP/2);
  vertex(baseL/2, -baseH/2, baseP/2);
  vertex(baseL/2, -baseH/2, -baseP/2);
  
  endShape();
  
  // ---- Lingot ----
  translate(0, -baseH/2 - hauteur/2, 0);
  
  fill(255, 215, 0); // couleur or
  
  float topW = largeur * biseau;
  float topP = profondeur * biseau;
  
  beginShape(QUADS);
  texture(or);
  // dessous
  vertex(-largeur/2, hauteur/2, -profondeur/2, 0, 0);
  vertex(largeur/2, hauteur/2, -profondeur/2, 1, 0);
  vertex(largeur/2, hauteur/2, profondeur/2, 0, 1);
  vertex(-largeur/2, hauteur/2, profondeur/2, 1, 1);
  
  // dessus
  vertex(-topW/2, -hauteur/2, -topP/2, 0, 0);
  vertex(topW/2, -hauteur/2, -topP/2, 1, 0);
  vertex(topW/2, -hauteur/2, topP/2, 0, 1);
  vertex(-topW/2, -hauteur/2, topP/2, 1, 1);
  
  // côtés
  vertex(-largeur/2, hauteur/2, -profondeur/2, 0, 0);
  vertex(largeur/2, hauteur/2, -profondeur/2, 0, 1);
  vertex(topW/2, -hauteur/2, -topP/2, 1, 0);
  vertex(-topW/2, -hauteur/2, -topP/2, 1, 1);
  
  vertex(-largeur/2, hauteur/2, profondeur/2, 0, 0);
  vertex(largeur/2, hauteur/2, profondeur/2, 1, 0);
  vertex(topW/2, -hauteur/2, topP/2, 0, 1);
  vertex(-topW/2, -hauteur/2, topP/2, 1, 1);
  
  vertex(-largeur/2, hauteur/2, -profondeur/2, 0, 0);
  vertex(-largeur/2, hauteur/2, profondeur/2, 1, 0);
  vertex(-topW/2, -hauteur/2, topP/2, 0, 1);
  vertex(-topW/2, -hauteur/2, -topP/2, 1, 1);
  
  vertex(largeur/2, hauteur/2, -profondeur/2, 0, 0);
  vertex(largeur/2, hauteur/2, profondeur/2, 1, 0);
  vertex(topW/2, -hauteur/2, topP/2, 0, 1);
  vertex(topW/2, -hauteur/2, -topP/2, 1, 1);
  
  endShape();
  //pied
  //fill(80);
  //beginShape(QUAD);
  //vertex(-piedL, 0, piedP);
  //vertex(-piedL, piedH, piedP);
  //vertex(piedL, piedH, piedP);
  //vertex(piedL, 0, piedP);
  
  //vertex(-piedL, 0, piedP);
  //vertex(-piedL, piedH, piedP);
  //vertex(-piedL, piedH, -piedP);
  //vertex(-piedL, 0, -piedP);
  
  //vertex(-piedL, 0, -piedP);
  //vertex(-piedL, piedH, -piedP);
  //vertex(piedL, piedH, -piedP);
  //vertex(piedL, 0, -piedP);
  
  //vertex(piedL, 0, -piedP);
  //vertex(piedL, piedH, -piedP);
  //vertex(piedL, piedH, piedP);
  //vertex(piedL, 0, piedP);
  
  //endShape();
  popMatrix();
}
