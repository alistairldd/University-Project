#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

uniform float time;
varying vec3 vertPosition;

void main() {
  // Couleur solaire pulsante
  float pulse = 0.5 + 0.5 * sin(time * 2.0 + vertPosition.y * 0.1);
  vec3 baseColor = mix(vec3(1.0, 0.4, 0.0), vec3(1.0, 0.6, 0.1), pulse);

  // Pas d'ombrage directionnel, lumière émise uniformément
  gl_FragColor = vec4(baseColor, 1.0);
  
}

