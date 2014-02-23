#version 330

in vec3 position;
in vec2 textureCoords;

uniform mat4 world;

out vec2 textureCoordinates;

void main() {

	gl_Position = world * vec4(position, 1.0);

	textureCoordinates = textureCoords;
}
