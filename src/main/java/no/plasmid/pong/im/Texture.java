package no.plasmid.pong.im;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

public class Texture {

	private int openGLTextureId;
	
	private int pixelFormat; // Pixel format, OpenGL enumeration

	private int width;

	private int height;

	private ByteBuffer imageData;
	
	public Texture(int pixelFormat, int width, int height, ByteBuffer imageData, int openGLTextureId) {
		this.pixelFormat = pixelFormat;
		this.width = width;
		this.height = height;
		
		this.imageData = BufferUtils.createByteBuffer(imageData.capacity());
		this.imageData.put(imageData);

		this.openGLTextureId = openGLTextureId;
	}
	
	public int getOpenGLlTextureId() {
		return openGLTextureId;
	}
		
	public int getPixelFormat() {
		return pixelFormat;
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public ByteBuffer getImageData() {
		return imageData;
	}
	
}
