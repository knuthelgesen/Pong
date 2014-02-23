package no.plasmid.pong.im;

import java.nio.FloatBuffer;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class Vertex {

	public Vector3f positionCoords;
	public Vector2f textureCoords;
	
	public Vertex(Vector3f positionCoords, Vector2f textureCoords) {
		this.positionCoords = positionCoords;
		this.textureCoords = textureCoords;
	}
	
	public void store(FloatBuffer buf) {
		positionCoords.store(buf);
		textureCoords.store(buf);
	}
	
}
