void boussole(float x, float y){
  
  pushMatrix();
  
  ortho();
  
  rotateY(PI);
  rotateX(PI/2);
  
  translate(x, y);

  
  
  fill(58,29,0);
  stroke(200,173,127);
  strokeWeight(4);
  
  sphere(20);
  
  noStroke();
  circle(0,0,100);
  
    
  fill(255,0,0);
  beginShape(TRIANGLE);
  vertex(-10,0);
  vertex(10,0);
  vertex(0,-40);
  endShape();

  float angle = atan2(dirX, dirY);

  rotateZ(angle);
 
  
  
  fill(255,0,0);
  textSize(20);
  text("N", 0, -40);
  
  rotateZ(PI/2);
  
  fill(0);
  textSize(20);
  text("E", 0, -40);
  
  rotateZ(PI/2);
  

  textSize(20);
  text("S", 0, -40);
  
  rotateZ(PI/2);
  

  textSize(20);
  text("W", 0, -40);

  
  popMatrix();
  resetMatrix();
}
