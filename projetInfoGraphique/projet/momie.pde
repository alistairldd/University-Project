int posMXLab;
int posMYLab;
int nextMXLab;
int nextMYLab;

float animMomie=0;

void deplaceMomie(labyrintheRes laby){
  posMXLab = round((posMX - step*dirMX)/(tailleCube*2));
  posMYLab = round((posMY - step*dirMY)/(tailleCube*2));

  
  ArrayList<int[]> directionsValides = new ArrayList<int[]>();
  
  int[][] directions = {{0,-1}, {0,1}, {1,0}, {-1,0}};
  
  for (int[] d:directions){
    if (posMXLab + d[0] > 0 && posMXLab + d[0] < laby.size && posMYLab + d[1] > 0 && posMXLab + d[1] < laby.size){ 

      if (laby.labyrinthe[posMYLab + d[1]][posMXLab + d[0]] != '#'){
        if (dirMX + d[0] == 0 && dirMY + d[1] == 0){
          directionsValides.add(d); 
        }else{
          for(int i = 0; i < 25; i++){  
          directionsValides.add(d); 
          }
        }
      }
    }
  }
  if (!directionsValides.isEmpty()){
    int[] dir = directionsValides.get( int(random(directionsValides.size())) );
    dirMX = dir[0];
    dirMY = dir[1];
    animMomie = 30;
  }
}


void strip(float r){

  beginShape(QUADS);
  for (float i = -2*PI; i < 2*PI; i += PI/10){
     vertex(r*cos(i), 0, r*sin(i));
     vertex(r*cos(i), .5, r*sin(i));
     vertex(r*cos(i+PI/10), .5, r*sin(i+PI/10));
     vertex(r*cos(i+PI/10), 0, r*sin(i+PI/10));
  }
  endShape();
}

void bras(float r){
  
  
 rotateX(PI/2);
 pushMatrix();
 for(int i = 0; i < 13; i++){
   stroke(1);
   fill(169,156,120);
   strip(r);
   pushMatrix();
   fill(40);
   translate(0,0, 0.3);
   noStroke();
   strip(.5);
   popMatrix();
   translate(0,.5);

 }
  translate(0,0,.3);
  noStroke();  
  sphere(0.5);
 
 
 popMatrix();
 pushMatrix();
 translate(6,0);
  for(int i = 0; i < 13; i++){
   fill(169,156,120);
   stroke(1);
   strip(r);
   pushMatrix();
   fill(40);
   translate(0,0, 0.3);
   noStroke();
   strip(.5);
   popMatrix();
   translate(0,.5);
 }
  translate(0,0,.3);
  noStroke();  
  sphere(0.5);
  popMatrix();
}

void oeil(){
  fill(20);
  pushMatrix();
  beginShape(TRIANGLE);
  vertex(0,0);
  vertex(0,-1);
  vertex(-2,-.5);
  vertex(0, 0);
  vertex(0,-1);
  vertex(2, -.5);
  endShape();
  fill(255,255,0);
  ellipse(0, -.5, .4, 1);
  translate(5,0);
  fill(20);
  beginShape(TRIANGLE);
  vertex(0,0);
  vertex(0,-1);
  vertex(-2,-.5);
  vertex(0, 0);
  vertex(0,-1);
  vertex(2, -.5);
  endShape();  
    fill(255,255,0);
  ellipse(0, -.5, .4, 1);  
  popMatrix();

}

void couronne(float r){
  fill(220,200,50);
  pushMatrix();
  rotateZ(PI/18);
  strip(r);
  translate(0, -.5); 
  strip(r);
  stroke(1);
  beginShape(TRIANGLES);
  for (float i = -2*PI; i < 2*PI; i += PI/5){
   vertex(r*cos(i), 0, r*sin(i));
   vertex(r*cos(i+PI/20), -4, r*sin(i+PI/20));
   vertex(r*cos(i+PI/10), 0, r*sin(i+PI/10));
  }
  endShape();
  popMatrix();
}


void momie(){
 fill(169,156,120);
 pushMatrix();
 stroke(0);
 strokeWeight(1);
 float bodyR = 2;
 int nbBody = 40;
 
 float headR = 4;
 int nbHead = 20;
 float r;
 
 for(int i = 0; i < nbBody + nbHead; i++){
   pushMatrix();
   if (i < nbBody){
     float i2 = i*PI/nbBody;
     r = bodyR + 2*abs(cos(PI/2 + i2));
     translate(0, -.5*i);
     strip(r);
   }
   else {
     float i2 = i*PI/nbHead;
     r = headR - 2 * abs(cos(i2));
     translate(0,-.5*i);
     strip(r);
     
   }
   popMatrix();
 }
 pushMatrix();
 //noStroke();
 translate(0,-25);
 fill(200,0,0);
 sphere(3);
 popMatrix();
 pushMatrix();
 translate(-2.5, -25,4);
 stroke(14);
 oeil();
 popMatrix();
 pushMatrix();
 translate(.3,-27.5);
 couronne(3);
 popMatrix();
 translate(-3, -15);
 bras(.8);
 popMatrix();
}
