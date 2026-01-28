// sun.vert
uniform mat4 transform;
attribute vec4 vertex;
attribute vec4 color;
attribute vec3 normal;

varying vec4 vertColor;
varying vec3 vertNormal;
varying vec3 vertPosition;

void main() {
  vertColor = color;
  vertNormal = normal;
  vertPosition = vec3(transform * vertex);
  gl_Position = transform * vertex;
}
