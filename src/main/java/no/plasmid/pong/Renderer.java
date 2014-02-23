package no.plasmid.pong;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import no.plasmid.pong.im.Renderable;
import no.plasmid.pong.im.Vertex;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.NVMultisampleFilterHint;
import org.lwjgl.util.glu.GLU;

public class Renderer {

	private int shaderProgramId;
	
	private int positionAttributeId;
	private int textureCoordsAttributeId;
	
	private int worldMatrixUniformId;
	private FloatBuffer worldMatrixUniformBuffer;
	private int samplerUniformId;
	
	/**
	 * Initialize the rendering system
	 */
	public void initializeRenderer() {
		GL11.glViewport(0, 0, Configuration.WINDOW_WIDTH, Configuration.WINDOW_HEIGTH);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glEnable(GL11.GL_CULL_FACE);

		//Enable multisample anti aliasing
		GL11.glEnable(GL13.GL_MULTISAMPLE);
		GL11.glHint(NVMultisampleFilterHint.GL_MULTISAMPLE_FILTER_HINT_NV, GL11.GL_NICEST);
		
		//Enable texturing
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		checkGL();
		
		/*
		 * Shaders
		 */
		//Load the source
		String vertexSource;
		String fragmentSource;
		try {
			vertexSource = loadTextFile("/shaders/vertex.shader");
			fragmentSource = loadTextFile("/shaders/fragment.shader");
		} catch (FileNotFoundException e) {
			throw new IllegalStateException("Error when opening shader file", e);
		}
		
		//Create the program
		shaderProgramId = GL20.glCreateProgram();
		if (0 == shaderProgramId) {
			throw new IllegalStateException("Could not create shader");
		}
		//Compile the sources
		compileShader(shaderProgramId, vertexSource, GL20.GL_VERTEX_SHADER);
		compileShader(shaderProgramId, fragmentSource, GL20.GL_FRAGMENT_SHADER);
		
		checkGL();

		//Link the program
		GL20.glLinkProgram(shaderProgramId);
		int linkStatus = GL20.glGetProgrami(shaderProgramId, GL20.GL_LINK_STATUS);
		if (0 == linkStatus) {
			String error = GL20.glGetShaderInfoLog(shaderProgramId, 2048);
			throw new IllegalStateException("Error when linking shader: " + error);
		}
		
		checkGL();

		//Validate the program
		GL20.glValidateProgram(shaderProgramId);
    int validationStatus = GL20.glGetProgrami(shaderProgramId, GL20.GL_VALIDATE_STATUS);
    if (0 == validationStatus) {
        String validationError = GL20.glGetProgramInfoLog(shaderProgramId, 2048);
        throw new IllegalStateException("Error found when validating shader: " + validationError);
    }
		checkGL();
		
		//Find the ID of the position vertex attribute
		positionAttributeId = GL20.glGetAttribLocation(shaderProgramId, "position");
		if (-1 == positionAttributeId) {
			throw new IllegalStateException("Could not find ID of position vertex attribute");
		}
		
		//Find the ID of the texture coords attribute
		textureCoordsAttributeId = GL20.glGetAttribLocation(shaderProgramId, "textureCoords");
		if (-1 == textureCoordsAttributeId) {
			throw new IllegalStateException("Could not find ID of texture coords vertex attribute");
		}
		
		//Find the ID of the world transformation matrix uniform
		worldMatrixUniformId = GL20.glGetUniformLocation(shaderProgramId, "world");
		if (-1 == worldMatrixUniformId) {
			throw new IllegalStateException("Could not find ID of world transformation matrix uniform");
		}

		//Find the ID of the sampler uniform (the texture sampler)
		samplerUniformId = GL20.glGetUniformLocation(shaderProgramId, "sampler");
		if (-1 == samplerUniformId) {
			throw new IllegalStateException("Could not find ID of sampler uniform");
		}
		
		//Create the buffer used to upload world matrix to OpenGL
		worldMatrixUniformBuffer = BufferUtils.createFloatBuffer(16);
	}
	
	public void render(Collection<Renderable> renderables) {
		//Clear the display
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		//Enable depth test
		GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
		
		//Use the shader
    GL20.glUseProgram(shaderProgramId);

    //Enable vertex attributes "position" and "textureCoords" 
    GL20.glEnableVertexAttribArray(positionAttributeId);
    GL20.glEnableVertexAttribArray(textureCoordsAttributeId);

    for (Renderable renderable : renderables) {
      //Set translation values
      renderable.getTranslationMatrix().store(worldMatrixUniformBuffer);
      worldMatrixUniformBuffer.rewind();
      GL20.glUniformMatrix4(worldMatrixUniformId, false, worldMatrixUniformBuffer);
      
  		//Bind texture
      GL13.glActiveTexture(GL13.GL_TEXTURE0);
  		GL11.glBindTexture(GL11.GL_TEXTURE_2D, renderable.getTexture().getOpenGLlTextureId());
      GL20.glUniform1i(samplerUniformId, 0);
      
      //Bind data buffer
      GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, renderable.getBufferId());
  		GL20.glVertexAttribPointer(positionAttributeId, 3, GL11.GL_FLOAT, false, 5 * 4, 0);	//Each vertex is 5 floats
  		GL20.glVertexAttribPointer(textureCoordsAttributeId, 2, GL11.GL_FLOAT, false, 5 * 4, 3 * 4);	//Each vertex is 5 floats, texture coords starts at 3
  		
  		//Draw
  		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, renderable.getVertices().size());
    }
		
		//Disable vertex attribute arrays
		GL20.glDisableVertexAttribArray(positionAttributeId);
		GL20.glDisableVertexAttribArray(textureCoordsAttributeId);
	}
	
	public void registerRenderable(Renderable renderable) {
		List<Vertex> vertices = renderable.getVertices();
		renderable.setBufferId(GL15.glGenBuffers());
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, renderable.getBufferId());
		//Reserve enough space for position + texture coordinates. Data will be interlaced
		FloatBuffer data = BufferUtils.createFloatBuffer((vertices.size() * 3) + (vertices.size() * 2));
		for (Vertex vertex : vertices) {
			vertex.store(data);
		}
		data.rewind();
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);

		checkGL();
	}
	
	public int registerTexture(int imagePixelFormat, int width, int height, ByteBuffer imageData) {
		int textureId = GL11.glGenTextures();
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, imagePixelFormat, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, imageData);
		
		return textureId;
	}
	
	/**
	 * Check for OpenGL error, and throw exception if any are found
	 */
	private void checkGL() {
		final int code = GL11.glGetError();
		if (code != 0) {
			final String errorString = GLU.gluErrorString(code);
			final String message = "OpenGL error (" + code + "): " + errorString;
			throw new IllegalStateException(message);
		}
	}
	
	private void compileShader(int shaderProgramId, String source, int type) {
		int shaderId = GL20.glCreateShader(type);
		if (0 == shaderId) {
			throw new IllegalStateException("Could not create shader!");
		}
		
		GL20.glShaderSource(shaderId, source);
		GL20.glCompileShader(shaderId);
		int compileStatus = GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS);
		if (0 == compileStatus) {
			String error = GL20.glGetShaderInfoLog(shaderId, 2048);
			throw new IllegalStateException("Error when compiling shader: " + error);
		}
		
		GL20.glAttachShader(shaderProgramId, shaderId);
	}
	
	private String loadTextFile(String fileName) throws FileNotFoundException {
		URL fileURL = Renderer.class.getResource(fileName);
		if (fileURL == null) {
			throw new FileNotFoundException("Could not find shader source file " + fileName);
		}
		
		StringBuilder text = new StringBuilder();
		String NL = System.getProperty("line.separator");
		FileInputStream fis;
		Scanner scanner = null;
		try {
			fis = new FileInputStream(fileURL.getFile());
			scanner = new Scanner(fis);
			
			while (scanner.hasNextLine()) {
				text.append(scanner.nextLine() + NL);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
		
		return text.toString();
	}
	
}
