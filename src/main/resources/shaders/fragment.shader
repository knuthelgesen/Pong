#version 330

in vec2 textureCoordinates;

uniform sampler2D sampler;

out vec4 FragColor;

void main() {
	FragColor = texture2D(sampler, textureCoordinates.st);
}
