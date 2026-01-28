// avant dernière case : 570 570 10
// dernière case : 576 570 10

class labyrintheRes {
  char[][] labyrinthe;
  char[][][] sides;
  char[][] cases;
  int size;
  
  labyrintheRes(char[][] lab, char[][][] s, char[][] c, int size){
    this.labyrinthe = lab;
    this.sides = s;
    this.cases = c;
    this.size = size;
  }
}

labyrintheRes createLab(int size){
  char[][] lab = new char[size][size];
  char[][][] sides = new char[size][size][4];
  char[][] cases = new char[size][size];

  randomSeed(size);
  
  int todig = 0;
  for (int j=0; j<size; j++) {
    for (int i=0; i<size; i++) {
      sides[j][i][0] = 0;
      sides[j][i][1] = 0;
      sides[j][i][2] = 0;
      sides[j][i][3] = 0;
      if (j%2==1 && i%2==1) {
        lab[j][i] = '.';
        todig ++;
      } else
        lab[j][i] = '#';
    }
  }
  int gx = 1;
  int gy = 1;
  while (todig>0 ) {
    int oldgx = gx;
    int oldgy = gy;
    int alea = floor(random(0, 4)); // selon un tirage aleatoire
    if      (alea==0 && gx>1)          gx -= 2; // le fantome va a gauche
    else if (alea==1 && gy>1)          gy -= 2; // le fantome va en haut
    else if (alea==2 && gx<size-2) gx += 2; // .. va a droite
    else if (alea==3 && gy<size-2) gy += 2; // .. va en bas

    if (lab[gy][gx] == '.') {
      todig--;
      lab[gy][gx] = ' ';
      lab[(gy+oldgy)/2][(gx+oldgx)/2] = ' ';
    }
  }

  lab[0][1]                   = ' '; // entree
  lab[size-2][size-1] = ' '; // sortie
  
  
  for (int j=1; j<size-1; j++) {
    for (int i=1; i<size-1; i++) {
      if (lab[j][i]==' ') {
        if (lab[j-1][i]=='#' && lab[j+1][i]==' ' &&
          lab[j][i-1]=='#' && lab[j][i+1]=='#')
          sides[j-1][i][0] = 1;// c'est un bout de couloir vers le haut 
        if (lab[j-1][i]==' ' && lab[j+1][i]=='#' &&
          lab[j][i-1]=='#' && lab[j][i+1]=='#')
          sides[j+1][i][3] = 1;// c'est un bout de couloir vers le bas 
        if (lab[j-1][i]=='#' && lab[j+1][i]=='#' &&
          lab[j][i-1]==' ' && lab[j][i+1]=='#')
          sides[j][i+1][1] = 1;// c'est un bout de couloir vers la droite
        if (lab[j-1][i]=='#' && lab[j+1][i]=='#' &&
          lab[j][i-1]=='#' && lab[j][i+1]==' ')
          sides[j][i-1][2] = 1;// c'est un bout de couloir vers la gauche
      }
    }
  }
  return new labyrintheRes(lab, sides, cases, size);
}

labyrintheRes lab;
labyrintheRes laby1;
labyrintheRes laby2;
labyrintheRes laby3;
labyrintheRes laby4;
labyrintheRes laby5;
labyrintheRes laby6;
labyrintheRes laby7;

void setup() { 
  stone = loadImage("stones.jpg");
  sol = loadImage("sol.jpeg");
  plafond = loadImage("plafond.jpg");
  sable = loadImage("sable.jpg");
  or = loadImage("or.jpg");
  toit = loadImage("toit.jpg");

  textAlign(CENTER, CENTER);
  textSize(100);
  frameRate(20);
  size(1000, 1000, P3D);
  laby1 = createLab(17);
  laby2 = createLab(15);
  laby3 = createLab(13);
  laby4 = createLab(11);
  laby5 = createLab(9);
  laby6 = createLab(7);
  laby7 = createLab(5);
  lab = laby1;
  
  int lig = 1000/20;
  int col = 1000/20;
  
  for (int i = 0; i < lig -1; i++){
    for (int j = 0; j < col -1; j++){
      desert[j][i] = map(noise(i/25., j/25.), 0, 1, -20, 21);
    }
  }

}

void pyramide(){
  
   pushMatrix();
   grandLaby(laby1);
   translate(2*tailleCube, 2*tailleCube, 2*tailleCube);
   grandLaby(laby2);
   translate(2*tailleCube, 2*tailleCube, 2*tailleCube);
   grandLaby(laby3);
   translate(2*tailleCube, 2*tailleCube, 2*tailleCube);
   grandLaby(laby4);
   translate(2*tailleCube, 2*tailleCube, 2*tailleCube);
   grandLaby(laby5);
   translate(2*tailleCube, 2*tailleCube,2*tailleCube);
   grandLaby(laby6);
   translate(2*tailleCube, 2*tailleCube,2*tailleCube);
   grandLaby(laby7);
   translate(-tailleCube,-tailleCube,2*tailleCube);
   
  
  

  

   beginShape(TRIANGLE); // pyramide
   textureMode(REPEAT);
   texture(toit);
   tint(170,150,50);
   
   // mur sud
   vertex(0,0,0, 0, 0);
   vertex(5*tailleCube, 0, 0, 1, 0);
   vertex(5*tailleCube, 3*tailleCube, 5*tailleCube, 1, 1);
   
   vertex(5*tailleCube, 0, 0, 0, 0);
   vertex(5*tailleCube, 3*tailleCube, 5*tailleCube, 0, 1);
   vertex(5*2*tailleCube, 0, 0, 1, 0);
   
   // mur est
   vertex(0, 0, 0, 1, 0);
   vertex(0, 5*tailleCube, 0, 0, 0);
   vertex(5*tailleCube, 5*tailleCube, 3*tailleCube, 1, 0);
   
   vertex(0, 5*tailleCube, 0 , 1, 0);
   vertex(5*tailleCube, 5*tailleCube, 3*tailleCube, 1, 1);
   vertex(0, 5*2*tailleCube, 0, 0, 0);
   
   // mur ouest
   vertex(5*2*tailleCube, 0, 0);
   vertex(5*tailleCube, 5*tailleCube, 3*tailleCube);
   vertex(5*2*tailleCube, 5*tailleCube, 0);
   
   vertex(5*2*tailleCube, 0, 0);
   vertex(5*tailleCube, 5*tailleCube, 3*tailleCube);
   vertex(5*2*tailleCube, 5*2*tailleCube, 0);
   
   // mur nord
   vertex(5*2*tailleCube, 5*2*tailleCube, 0);
   vertex(5*tailleCube, 5*tailleCube, 3*tailleCube);
   vertex(5*tailleCube, 5*2*tailleCube, 0);
   
   vertex(5*tailleCube, 5*2*tailleCube, 0);
   vertex(5*tailleCube, 5*tailleCube, 3*tailleCube);
   vertex(0, 5*2*tailleCube, 0);   
   endShape();
   popMatrix();
   
   pushMatrix();
   translate(-15,-14.5);
   fill(80);
   tint(80);
   murPyr();
   translate(2*tailleCube, 2*tailleCube, 2*tailleCube);
   tint(216,167,30);
   murPyr();
   translate(2*tailleCube, 2*tailleCube, 2*tailleCube);
   murPyr();
   translate(2*tailleCube, 2*tailleCube, 2*tailleCube);
   murPyr();
   translate(2*tailleCube, 2*tailleCube, 2*tailleCube);
   murPyr();
   translate(2*tailleCube, 2*tailleCube, 2*tailleCube);
   murPyr();   
   translate(2*tailleCube, 2*tailleCube, 2*tailleCube);
   murPyr(); 
   popMatrix();  
}

void murPyr(){
   beginShape(QUAD); // entrée
   texture(stone);
   vertex(2*tailleCube, 0, 0, 0, 0);
   vertex(4*tailleCube, 0, 0, 1, 0);
   vertex(4*tailleCube, 0, 2*tailleCube, 1, 1);
   vertex(2*tailleCube, 0, 2*tailleCube, 0, 1);
   endShape();
}

void grandLaby(labyrintheRes laby){
  textureMode(NORMAL);
  for (int j=0; j<laby.size; j++) {
    for (int i=0; i<laby.size; i++) {
      pushMatrix();
      translate(i*tailleCube*2,j*tailleCube*2);
      if (laby.labyrinthe[j][i] == '#'){
        tint(216,167,30);
        afficheMurs(laby, tailleCube, i, j);
        translate(0,0,tailleCube);
        afficheMurs(laby, tailleCube, i, j);
      }
      else{
        fill(120);
        afficheSol(tailleCube);
        affichePlafond(tailleCube);
      }
      popMatrix();
    }
  }
}

void petitLaby(labyrintheRes laby){
  int posXLabPetit = round(posX/3.76);
  int posYLabPetit = round(posY/3.76);
  
  int posMXLabPetit = round(posMX / 3.76);
  int posMYLabPetit = round(posMY/3.76);
  
  pushMatrix();
  rotateX(PI/2);
  hint(DISABLE_DEPTH_TEST);
  ortho();
  translate(350,350);
  
  for (int j=0; j<laby.size; j++) {
    for (int i=0; i<laby.size; i++) {

      pushMatrix();
      translate(i*taillePetitCube, j*taillePetitCube);
      if (laby.labyrinthe[j][i] == '#' && laby.cases[j][i] == 1){
        fill(map(i, 0., float(LAB_SIZE), 0.,200.), map(j, 0, float(LAB_SIZE), 0, 200), 200);
        box(taillePetitCube);
      } else if (laby.labyrinthe[j][i] != '#' && laby.cases[j][i] == 1){
        fill(50);
        box(taillePetitCube);
      }
      popMatrix();
    }
  }
  

  fill(255,0,0);
  circle(posMXLabPetit, posMYLabPetit, 8);
    fill(0,255,0);
  circle(posXLabPetit, posYLabPetit, 8); // affichage position joueur dans labyrinthe
  float angle = atan2(dirY, dirX);
  noStroke();
  beginShape(TRIANGLE);
  vertex(posXLabPetit, posYLabPetit);
  vertex(posXLabPetit + 10 * cos(angle + PI/8), posYLabPetit + 10*sin(angle + PI/8));
  vertex(posXLabPetit + 10 * cos(angle - PI/8), posYLabPetit + 10*sin(angle - PI/8));
  endShape();
  stroke(1);
  hint(ENABLE_DEPTH_TEST);
  popMatrix();
}




void escalier(){
  textureMode(NORMAL);
  stroke(1);
  float r = 16;
  pushMatrix();
  
  rotateX(-PI/2);
 
  
  beginShape(QUAD_STRIP);
  texture(stone);
  for (int i2= -10; i2 <= 0; i2++){
    float i = i2/PI;
    float u = map(i2, -10,0,0,1);
    vertex(r*cos(i), 0, -r*sin(i),u,0);
    vertex(r*cos(i), -2*r, -r*sin(i),u,1);
  }
  endShape();
  
  beginShape(QUAD);
  texture(stone);
  vertex(0,0, 0, 0);
  vertex(r,0, 1, 0);
  vertex(r,-2*r, 1, 1);
  vertex(0,-2*r, 0, 1);
  endShape();
  beginShape(TRIANGLE);
  texture(stone);
  for (int i2= -10; i2 <= -1; i2++){
    float i = i2/PI;
    vertex(0,-r-r*cos(i),0, 0, 0);
    vertex(r*cos(i), -r -r*cos(i), -r*sin(i), 1, 1);
    vertex(r*cos(i+PI/10), -r-r*cos(i), -r*sin(i+PI/10), 1,0);
  }
  endShape();
  beginShape(QUAD);
  texture(stone);
  for (int i2= -10; i2 <= -1; i2++){
    float i = i2/PI;
    vertex(0,-r-r*cos(i),0, 0, 0);
    vertex(0, -r-r*cos(i+PI/10), 0, 0, 1);
    vertex(r*cos(i+PI/10), -r-r*cos(i+PI/10), -r*sin(i+PI/10), 1, 1);
    vertex(r*cos(i+PI/10), -r-r*cos(i),- r*sin(i+PI/10), 1, 0);
  }
  endShape();
  
  beginShape(TRIANGLE);
  fill(0);
  for (int i2= -10; i2 <= -1; i2++){
    float i = i2/PI;
    vertex(0,-2*r,0, 0, 0);
    vertex(r*cos(i), -2*r, -r*sin(i), 1, 1);
    vertex(r*cos(i+PI/10), -2*r, -r*sin(i+PI/10), 1,0);
  }
  endShape();
  popMatrix();
}


void afficheSol(float tailleCube){
  
  beginShape(QUADS);
  texture(sol);
  vertex(-tailleCube, -tailleCube, 0,       0,0);
  vertex(-tailleCube, tailleCube, 0,        0,1);
  vertex(tailleCube, tailleCube, 0,         1,1);
  vertex(tailleCube, -tailleCube, 0,        1,0);
  endShape();
}

void affichePlafond(float tailleCube){
  beginShape(QUADS);
  texture(plafond);
  vertex(-tailleCube, -tailleCube, 2*tailleCube, 0, 0);
  vertex(-tailleCube, tailleCube, 2*tailleCube, 0, 1);
  vertex(tailleCube, tailleCube, 2*tailleCube, 1, 1);
  vertex(tailleCube, -tailleCube, 2*tailleCube, 1, 0);
  endShape();
}

void afficheMurs(labyrintheRes laby, float tailleCube, int i, int j){
  beginShape(QUADS);
  texture(stone);
  //mur nord
  
  
  if (j == 0 || laby.labyrinthe[j-1][i] != '#'){
    
    vertex(-tailleCube, -tailleCube, 0,               0, 0);
    vertex(-tailleCube, -tailleCube, tailleCube,      0, 1);
    vertex(tailleCube, -tailleCube, tailleCube,       1, 1);
    vertex(tailleCube, -tailleCube, 0,                1, 0);

  }
  
  //mur est
  if (i == laby.size-1 || laby.labyrinthe[j][i+1] != '#'){
    vertex(tailleCube, -tailleCube, 0,                0, 0);
    vertex(tailleCube, -tailleCube, tailleCube,       0, 1);
    vertex(tailleCube, tailleCube, tailleCube,        1, 1);
    vertex(tailleCube, tailleCube, 0,                 1, 0);

  }
  
  //mur sud
  if (j == laby.size-1 || laby.labyrinthe[j+1][i] != '#'){
    vertex(tailleCube, tailleCube, 0,                1, 1);
    vertex(tailleCube, tailleCube, tailleCube,       1, 0);
    vertex(-tailleCube, tailleCube, tailleCube,      0, 0);
    vertex(-tailleCube, tailleCube, 0,               0, 1);
    //fill(120);
  }
  
  //mur ouest
  if (i == 0 || laby.labyrinthe[j][i-1] != '#'){
    vertex(-tailleCube, -tailleCube, 0,              1, 1);
    vertex(-tailleCube, -tailleCube, tailleCube,     1, 0);
    vertex(-tailleCube, tailleCube, tailleCube,      0, 0);
    vertex(-tailleCube, tailleCube, 0,               0, 1);
    //fill(120);
   
  }
 endShape();
}
