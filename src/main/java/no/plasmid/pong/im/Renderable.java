package no.plasmid.pong.im;

import java.util.ArrayList;
import java.util.List;


import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public abstract class Renderable {

	private Matrix4f translationMatrix;
	
	/**
	 * Vertices used to draw this entity.
	 */
	private List<Vertex> vertices;
	
	/**
	 * OpenGL buffer id.
	 */
	private int bufferId;
	
	/**
	 * Texture used when drawing.
	 */
	private Texture texture;
	
	public Renderable() {
		translationMatrix = new Matrix4f();
		translationMatrix.setIdentity();
		vertices = new ArrayList<Vertex>();
	}

	public Matrix4f getTranslationMatrix() {
		return translationMatrix;
	}
	
	public void translate(Vector3f translation) {
		translationMatrix.translate(translation);
	}
	
	public List<Vertex> getVertices() {
		return vertices;
	}

	public int getBufferId() {
		return bufferId;
	}

	public void setBufferId(int bufferId) {
		this.bufferId = bufferId;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}
		
}
