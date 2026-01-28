void keyPressed(){
  println(keyCode);
  if (keyCode == 10){
    inLab = !inLab;
  }
  
  if (keyCode == 69){
    if (nextLab) {
      if (idLab >= 4) {posLab();} // nextLab = false;
      if (idLab == 3) {lab = laby4; idLab += 1; posLab(); posMX = 30; posMY = 30; animMomie = 0; nextLab = false;} 
      if (idLab == 2) {lab = laby3; idLab += 1; posLab(); posMX = 30; posMY = 30; animMomie = 0; nextLab = false;}
      if (idLab == 1) {lab = laby2; idLab += 1; posLab(); posMX = 30; posMY = 30; animMomie = 0; nextLab = false;} 
      
    }
  }
  
  if (keyCode == 38){ // pour aller tout droit
    if (inLab) {
      if (anim2 == 0 && animStyle ==0) animStyle = 1; 
    }
    else animOut = 1; 
  }
  
  
    if (keyCode == 37){ // pour tourner à gauche
      if (inLab){
        if (anim2 == 0 && animStyle != 2){
          animStyle = 2;
          saveX = dirX;
          saveY = dirY;
          objX = dirY;
          objY = -dirX;
          anim2 = 20;
        }

        //tmp = dirX;
        //dirX = dirY;
        //dirY = -tmp;
        
      }
      else {
        float tmp = dirXOut;
        dirXOut = dirYOut;
        dirYOut = -tmp;      
      }
    }
    if (keyCode == 39){ // pour tourner à droite
      if (inLab){
        if (anim2 == 0 && animStyle != 3) {
        animStyle = 3;
        saveX = dirX;
        saveY = dirY;
        objX = -dirY;
        objY = dirX;
        anim2 =20;
        //float tmp = dirX;
        //dirX = -dirY;
        //dirY = tmp;
      }
    }
    else {
      float tmp = dirXOut;
      dirXOut = -dirYOut;
      dirYOut = tmp;      
    }
  }
}

void keyReleased(){
  if (keyCode == 38) {
    if (inLab) {
      if(animStyle ==1) animStyle = 0;
    } else animOut = 0; 
  }
}

void posLab(){
  posX = 30;
  posY = 0;
  dirX = 0;
  dirY = 1;
}

void deplacement(labyrintheRes laby){
  posXLab = round((posX - step*dirX-5*dirX)/(tailleCube*2));
  posYLab = round((posY - step*dirY-5*dirY)/(tailleCube*2));
  nextXLab = int(posXLab + int(dirX));
  nextYLab = int(posYLab + int(dirY));
  
  if (animStyle == 1 && nextXLab >= 0 && nextXLab < laby.size && nextYLab >= 0 && nextYLab < laby.size && laby.labyrinthe[nextYLab][nextXLab] != '#'){
    casePos(laby, posYLab, posXLab);
    anim = 20; 
    posX += step*dirX;
    posY += step*dirY;
  }
}

void movOut(){

  if (animOut == 1 && posYOut+5*dirYOut <= -20 && posXOut + 5*dirXOut >= -60 && posXOut + 5*dirXOut <= 500 && posYOut + 5*dirYOut >= -500) {
    anim = 20;
    
    posXOut += step*dirXOut;
    posYOut += step*dirYOut;
    
  }
}

void casePos(labyrintheRes laby, int y, int x){
  laby.cases[y][x] = 1;
  if (y >= 1 && x >= 1)                laby.cases[y-1][x-1] = 1;
  if (y>=1 && x < laby.size)          laby.cases[y-1][x+1] = 1;
  if (y<=laby.size && x >= 1)          laby.cases[y+1][x-1] = 1;
  if (y<=laby.size && x < laby.size)  laby.cases[y+1][x+1] = 1;
  if (y >= 1)                          laby.cases[y-1][x] = 1;
  if (x >= 1)                          laby.cases[y][x-1] = 1;
  if (y < laby.size)                  laby.cases[y+1][x] = 1;
  if (x <= 1)                          laby.cases[y][x+1] = 1;
}
