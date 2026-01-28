int LAB_SIZE = 21;
char labyrinthe [][];
char sides [][][];
char cases [][];

PImage stone;
PImage sol;
PImage plafond;
PImage sable;
PImage or;
PImage toit;


float tailleCube = 15; // pour labyrinthe
float posX = 30, posY = 0, posZ = 10; // pos du joueur
float dirX = 0, dirY = 1; // direction du joueur

float posMX = 30, posMY = 30;
float dirMX = 1, dirMY = 0;

int scale = 100;

float step = 6.0; // taille du pas lors d'un déplacement

// var pr conversion pos joueur dans laby
float taillePetitCube = 8; // pour la map
int posXLab = 0; 
int posYLab = 1;
int nextXLab;
int nextYLab;

// var pour anim déplacement
float anim, anim2; // de 20 à 0
float zAnim = 0; // pour oscillation selon z-axis
int animStyle = 0; // 0 : aucune, 1 : translation, 2 : rotation G, 3 : rotation D
float tmp;
float saveX, saveY;
float objX, objY;

boolean inLab = false;


void updateCamera(){
  float factor = anim/5;
  float camX = posX - factor * dirX;
  float camY = posY - factor * dirY;
  float camZ = 12 + 1.2*cos(zAnim);  

  float targetX = camX + scale * dirX;
  float targetY = camY + scale * dirY;
  float targetZ = camZ; 
  
  camera(camX, camY, camZ,
       targetX, targetY, targetZ,
        0., 0., -1.);
}

float posXOut = 30, posYOut = -450, posZOut = 15;
float dirXOut = 0, dirYOut = 1;
float zAnimOut = 0;
int animOut = 0;


boolean nextLab = false;
int idLab = 1;

float[][] desert = new float[1000][1000];



void draw(){
 perspective(PI/3.0, float(width)/float(height), 1.0, 1500);
 
  if (idLab >3){
    background(120);
    camera();
    translate(width/2, height/2);
    lights();
    lingot();

    hint(DISABLE_DEPTH_TEST); // pour que le texte ne soit pas masqué
    translate(0, -200);
    fill(255);
    text("BRAVO", 0, 0, 10);
    hint(ENABLE_DEPTH_TEST);
  }
  else{
  if (!inLab){
  
    strokeWeight(1);
    camera(posXOut, posYOut, posZOut+1.2*cos(zAnimOut), posXOut + dirXOut*30, posYOut + dirYOut*30, posZOut +1.2*cos(zAnimOut), 0,0,-1);
    background(31, 71, 135);
    //lights();
    shader(yourShader);
    pushMatrix();
    translate(30,-100,100);
    directionalLight(189, 99, 50, 1, 1,-1);
    popMatrix();

    
    ambient(30, 30, 30);
    //pushMatrix(); si on veut observer la momie de dehors
    //translate(-50, -50, 10);
    //rotateX(-PI/2);
    //rotateY(PI);
    //momie();
    //popMatrix();
    
    pushMatrix(); // sol sable
    translate(-300, -500);
    
    textureWrap(REPEAT);
    float repeat = 10;
    beginShape(QUAD_STRIP);
    texture(sable);
    noStroke();
    int col = 1000/20;
    int lig = 1000/20;
    
    for(int i = 0; i < lig - 1; i++){
      for (int j = 0; j < col -1; j++){
        float u = (j / float(col - 1)) * repeat;
        float v1 = (i / float(lig - 1)) * repeat;
        float v2 = ((i + 1) / float(lig - 1)) * repeat;

        vertex(j * 20, i * 20, desert[j][i], u, v1);
        vertex(j * 20, (i + 1) * 20, desert[j][i + 1], u, v2);
      }
    }
    endShape();
    popMatrix();
   

   
   
   if (animOut == 1) zAnimOut += PI/6;
   
   movOut();

   pyramide();
   

  
   pushMatrix();
   noStroke();
   noLights();
   translate(-40,400, 100);
   PVector lightPos = new PVector(0, 0, 0); // Position du soleil
    yourShader.set("lightPos", new float[]{ lightPos.x, lightPos.y, lightPos.z });
   fill(150);
   sphere(55);
   popMatrix();


   if (posYOut >= -90 && posYOut <= 85 && posXOut>=25 && posXOut <= 35){ // condition entrée
      animOut = 0;
      anim=0;
      posYOut = -90;
      push();
      hint(DISABLE_DEPTH_TEST);
      fill(255,0,0);
      
      rotateY(PI);
      rotateX(PI/2);
      textSize(10);
      text("press enter", -30, -20, 0);
      hint(ENABLE_DEPTH_TEST);
      pop();
    }

    
  }
  
  else{
      background(40);

      pushMatrix();
      
      if (animStyle == 1) zAnim += PI/6;
      
      if (inLab) updateCamera(); 
  
      if (anim>0){
        anim-=4;
      }
  
      if (animStyle == 2){
        if (anim2 > 0){
          dirX = objX * (1-anim2/20) + saveX * (anim2/20);
          dirY = objY * (1-anim2/20) + saveY * (anim2/20);
          anim2 -= 2;
        }
        else if (anim2 ==0) {
          dirX = objX; dirY = objY; animStyle =0;;
        }
      }
      
      if (animStyle == 3){
        if (anim2>0){
    
          dirX = objX * (1-anim2/20) + saveX * (anim2/20);
          dirY = objY * (1-anim2/20) + saveY * (anim2/20);
          anim2 -= 2;
        }
        else if (anim2 == 0){
          dirX = objX; dirY = objY; animStyle=0; 
        }
      }
      if (anim2==0 && animStyle ==1) deplacement(lab);
      
      
      
      if (posX >= (lab.size - 3.5)*2*tailleCube && posY >= (lab.size - 3.5)*2*tailleCube){
        nextLab = true;
      }
      
      pushMatrix();
      translate(posMX, posMY);
      rotateX(-PI/2);
      
      //rotateY(PI);
      
      if (frameCount % 30 == 0){
        deplaceMomie(lab);
      }

      if(animMomie>0){
          
        posMX += dirMX;
        posMY += dirMY;
        animMomie-=1;
      }
      
      float angleM = atan2(dirMX, dirMY);
      rotateY(angleM);
      
      momie();
      
      popMatrix();
      
      
      lightFalloff(0.5, 0.02, 0.); 
      pointLight(255, 255, 255, posX, posY, posZ+5); // lumière sur le joueur
      
      
      grandLaby(lab); // affichage grand labyrinthe
      
      
      translate((lab.size-0.5) * 2 * tailleCube,(lab.size-2) * 2*tailleCube);
      rotateZ(-PI/2);
      escalier();
      popMatrix();

      noLights();
      petitLaby(lab); // affichage petit labyrinthe
  pushMatrix();
  hint(DISABLE_DEPTH_TEST);
  boussole(-width/2+75, height/2 - 120);
  hint(ENABLE_DEPTH_TEST);
  popMatrix();
    }

  }

} 
