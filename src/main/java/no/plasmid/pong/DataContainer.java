package no.plasmid.pong;

import java.io.FileNotFoundException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.plasmid.pong.im.BoundingBox;
import no.plasmid.pong.im.GUIElement;
import no.plasmid.pong.im.GameEntity;
import no.plasmid.pong.im.Renderable;
import no.plasmid.pong.im.Texture;
import no.plasmid.pong.im.Vertex;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class DataContainer {

	private static final float paddleSpeed = 1.0f;
	private static final float ballStartSpeed = 1.0f;
	private static final NumberFormat scoreNumberFormat = NumberFormat.getInstance();
	static {
		scoreNumberFormat.setMinimumIntegerDigits(3);
		scoreNumberFormat.setMaximumIntegerDigits(3);

	}

	/*
	 * Other services
	 */
	private Renderer renderer;
	private InputHandler inputHandler;
	private SoundSystem soundSystem;
	
	/*
	 * List of renderables
	 */
	private List<Renderable> allRenderables;
	
	/*
	 * Game entities
	 */
	private GameEntity playerPaddle;
	private GameEntity cpuPaddle;
	private GameEntity ball;
	private Vector3f ballDirection;
	
	/*
	 * GUI elements
	 */
	private GUIElement cpuScoreBoard;
	private GUIElement playerScoreBoard;
	
	/*
	 * Game values
	 */
	private boolean gameRunning;
	
	private int playerScore;
	private int cpuScore;
	
	/*
	 * Texture coordinates used to render numbers
	 */
	private Map<Character, Vector2f[]> numbersTextureCoordinates;
	
	/*
	 * Sounds
	 */
	private int pingSoundId;
	private int pongSoundId;
	private int wallSoundId;
	private int uahSoundId;
	
	private int padSourceId;
	private int wallSourceId;
	private int uahSourceId;
	
	public void initializeData(Renderer renderer, InputHandler inputHandler, SoundSystem soundSystem) {
		this.renderer = renderer;
		this.inputHandler = inputHandler;
		this.soundSystem = soundSystem;
		
		allRenderables = new ArrayList<Renderable>();

    //Test loading of textures
    TextureLoader textureLoader = new TextureLoader(renderer);
    //White dot for coloring
    Texture whiteTexture = null;
    //Ball texture
    Texture ballTexture = null;
    //Player label
    Texture playerLabelTexture = null;
    //CPU label
    Texture cpuLabelTexture = null;
    //Numbers texture
    Texture numbersTexture = null;
    try {
    	whiteTexture = textureLoader.loadTexture("/img/white.png");
    	ballTexture = textureLoader.loadTexture("/img/ball.png");
      playerLabelTexture = textureLoader.loadTexture("/img/playerLabel.png");
      cpuLabelTexture = textureLoader.loadTexture("/img/cpuLabel.png");
      numbersTexture = textureLoader.loadTexture("/img/numbers.png");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  
    numbersTextureCoordinates = new HashMap<Character, Vector2f[]>();
    numbersTextureCoordinates.put('1', new Vector2f[]{
    		new Vector2f(0.0f, 0.5f),
    		new Vector2f(0.2f, 0.5f),
    		new Vector2f(0.0f, 0.0f),
    		new Vector2f(0.2f, 0.5f),
    		new Vector2f(0.2f, 0.0f),
    		new Vector2f(0.0f, 0.0f),
    });
    numbersTextureCoordinates.put('2', new Vector2f[]{
    		new Vector2f(0.2f, 0.5f),
    		new Vector2f(0.4f, 0.5f),
    		new Vector2f(0.2f, 0.0f),
    		new Vector2f(0.4f, 0.5f),
    		new Vector2f(0.4f, 0.0f),
    		new Vector2f(0.2f, 0.0f),
    });
    numbersTextureCoordinates.put('3', new Vector2f[]{
    		new Vector2f(0.4f, 0.5f),
    		new Vector2f(0.6f, 0.5f),
    		new Vector2f(0.4f, 0.0f),
    		new Vector2f(0.6f, 0.5f),
    		new Vector2f(0.6f, 0.0f),
    		new Vector2f(0.4f, 0.0f),
    });
    numbersTextureCoordinates.put('4', new Vector2f[]{
    		new Vector2f(0.6f, 0.5f),
    		new Vector2f(0.8f, 0.5f),
    		new Vector2f(0.6f, 0.0f),
    		new Vector2f(0.8f, 0.5f),
    		new Vector2f(0.8f, 0.0f),
    		new Vector2f(0.6f, 0.0f),
    });
    numbersTextureCoordinates.put('5', new Vector2f[]{
    		new Vector2f(0.8f, 0.5f),
    		new Vector2f(1.0f, 0.5f),
    		new Vector2f(0.8f, 0.0f),
    		new Vector2f(1.0f, 0.5f),
    		new Vector2f(1.0f, 0.0f),
    		new Vector2f(0.8f, 0.0f),
    });
    numbersTextureCoordinates.put('6', new Vector2f[]{
    		new Vector2f(0.0f, 1.0f),
    		new Vector2f(0.2f, 1.0f),
    		new Vector2f(0.0f, 0.5f),
    		new Vector2f(0.2f, 1.0f),
    		new Vector2f(0.2f, 0.5f),
    		new Vector2f(0.0f, 0.5f),
    });
    numbersTextureCoordinates.put('7', new Vector2f[]{
    		new Vector2f(0.2f, 1.0f),
    		new Vector2f(0.4f, 1.0f),
    		new Vector2f(0.2f, 0.5f),
    		new Vector2f(0.4f, 1.0f),
    		new Vector2f(0.4f, 0.5f),
    		new Vector2f(0.2f, 0.5f),
    });
    numbersTextureCoordinates.put('8', new Vector2f[]{
    		new Vector2f(0.4f, 1.0f),
    		new Vector2f(0.6f, 1.0f),
    		new Vector2f(0.4f, 0.5f),
    		new Vector2f(0.6f, 1.0f),
    		new Vector2f(0.6f, 0.5f),
    		new Vector2f(0.4f, 0.5f),
    });
    numbersTextureCoordinates.put('9', new Vector2f[]{
    		new Vector2f(0.6f, 1.0f),
    		new Vector2f(0.8f, 1.0f),
    		new Vector2f(0.6f, 0.5f),
    		new Vector2f(0.8f, 1.0f),
    		new Vector2f(0.8f, 0.5f),
    		new Vector2f(0.6f, 0.5f),
    });
    numbersTextureCoordinates.put('0', new Vector2f[]{
    		new Vector2f(0.8f, 1.0f),
    		new Vector2f(1.0f, 1.0f),
    		new Vector2f(0.8f, 0.5f),
    		new Vector2f(1.0f, 1.0f),
    		new Vector2f(1.0f, 0.5f),
    		new Vector2f(0.8f, 0.5f),
    });
    
		List<Vertex> vertices;
    BoundingBox boundingBox;
		
		/*
		 * Create player paddle 
		 */
		playerPaddle = new GameEntity();
		vertices = playerPaddle.getVertices();
		vertices.add(new Vertex(new Vector3f(-0.02f, -0.1f, 0.0f), new Vector2f(0.0f, 0.0f)));
		vertices.add(new Vertex(new Vector3f(0.02f, -0.1f, 0.0f), new Vector2f(1.0f, 0.0f)));
		vertices.add(new Vertex(new Vector3f(-0.02f, 0.1f, 0.0f), new Vector2f(0.0f, 1.0f)));

		vertices.add(new Vertex(new Vector3f(0.02f, -0.1f, 0.0f), new Vector2f(1.0f, 0.0f)));
		vertices.add(new Vertex(new Vector3f(0.02f, 0.1f, 0.0f), new Vector2f(1.0f, 1.0f)));
		vertices.add(new Vertex(new Vector3f(-0.02f, 0.1f, 0.0f), new Vector2f(0.0f, 1.0f)));
	
		boundingBox = playerPaddle.getBoundingBox();
		boundingBox.top = 0.1f;
		boundingBox.bottom = -0.1f;
		boundingBox.left = -0.02f;
		boundingBox.right = 0.02f;
		
		renderer.registerRenderable(playerPaddle);
		
		playerPaddle.translate(new Vector3f(0.9f, 0.0f, 0.0f));
		playerPaddle.setTexture(whiteTexture);
		allRenderables.add(playerPaddle);
		
		/*
		 * Create CPU paddle
		 */
		cpuPaddle = new GameEntity();
		vertices = cpuPaddle.getVertices();
		vertices.add(new Vertex(new Vector3f(-0.02f, -0.1f, 0.0f), new Vector2f(0.0f, 0.0f)));
		vertices.add(new Vertex(new Vector3f(0.02f, -0.1f, 0.0f), new Vector2f(1.0f, 0.0f)));
		vertices.add(new Vertex(new Vector3f(-0.02f, 0.1f, 0.0f), new Vector2f(0.0f, 1.0f)));

		vertices.add(new Vertex(new Vector3f(0.02f, -0.1f, 0.0f), new Vector2f(1.0f, 0.0f)));
		vertices.add(new Vertex(new Vector3f(0.02f, 0.1f, 0.0f), new Vector2f(1.0f, 1.0f)));
		vertices.add(new Vertex(new Vector3f(-0.02f, 0.1f, 0.0f), new Vector2f(0.0f, 1.0f)));
		
		boundingBox = cpuPaddle.getBoundingBox();
		boundingBox.top = 0.1f;
		boundingBox.bottom = -0.1f;
		boundingBox.left = -0.02f;
		boundingBox.right = 0.02f;

		renderer.registerRenderable(cpuPaddle);
		
		cpuPaddle.translate(new Vector3f(-0.9f, 0.0f, 0.0f));
		cpuPaddle.setTexture(whiteTexture);
		allRenderables.add(cpuPaddle);

		/*
		 * Create ball
		 */
		ball = new GameEntity();
		vertices = ball.getVertices();
		vertices.add(new Vertex(new Vector3f(-0.03f, -0.03f, 0.0f), new Vector2f(0.0f, 0.0f)));
		vertices.add(new Vertex(new Vector3f(0.03f, -0.03f, 0.0f), new Vector2f(1.0f, 0.0f)));
		vertices.add(new Vertex(new Vector3f(-0.03f, 0.03f, 0.0f), new Vector2f(0.0f, 1.0f)));

		vertices.add(new Vertex(new Vector3f(0.03f, -0.03f, 0.0f), new Vector2f(1.0f, 0.0f)));
		vertices.add(new Vertex(new Vector3f(0.03f, 0.03f, 0.0f), new Vector2f(1.0f, 1.0f)));
		vertices.add(new Vertex(new Vector3f(-0.03f, 0.03f, 0.0f), new Vector2f(0.0f, 1.0f)));
		
		boundingBox = ball.getBoundingBox();
		boundingBox.top = 0.03f;
		boundingBox.bottom = -0.03f;
		boundingBox.left = -0.03f;
		boundingBox.right = 0.03f;

		renderer.registerRenderable(ball);
		
		ball.setTexture(ballTexture);
		allRenderables.add(ball);
				
		/*
		 * Create divider
		 */
		GUIElement divider = new GUIElement();
		vertices = divider.getVertices();
		
		float dividerPos = -0.95f;
		float horizontalDividerOffset = 0.8f;
		for (int i = 0; i < 10; i++) {
			vertices.add(new Vertex(new Vector3f(-0.01f, dividerPos, 0.0f), new Vector2f(0.0f, 0.0f)));
			vertices.add(new Vertex(new Vector3f(0.01f, dividerPos, 0.0f), new Vector2f(1.0f, 0.0f)));
			vertices.add(new Vertex(new Vector3f(-0.01f, dividerPos + 0.1f, 0.0f), new Vector2f(0.0f, 1.0f)));

			vertices.add(new Vertex(new Vector3f(0.01f, dividerPos, 0.0f), new Vector2f(1.0f, 0.0f)));
			vertices.add(new Vertex(new Vector3f(0.01f, dividerPos + 0.1f, 0.0f), new Vector2f(1.0f, 1.0f)));
			vertices.add(new Vertex(new Vector3f(-0.01f, dividerPos + 0.1f, 0.0f), new Vector2f(0.0f, 1.0f)));
			
			vertices.add(new Vertex(new Vector3f(dividerPos, horizontalDividerOffset - 0.01f, 0.0f), new Vector2f(0.0f, 0.0f)));
			vertices.add(new Vertex(new Vector3f(dividerPos + 0.1f, horizontalDividerOffset - 0.01f, 0.0f), new Vector2f(1.0f, 0.0f)));
			vertices.add(new Vertex(new Vector3f(dividerPos, horizontalDividerOffset + 0.01f, 0.0f), new Vector2f(0.0f, 1.0f)));

			vertices.add(new Vertex(new Vector3f(dividerPos, horizontalDividerOffset + 0.01f, 0.0f), new Vector2f(0.0f, 1.0f)));
			vertices.add(new Vertex(new Vector3f(dividerPos + 0.1f, horizontalDividerOffset - 0.01f, 0.0f), new Vector2f(1.0f, 0.0f)));
			vertices.add(new Vertex(new Vector3f(dividerPos + 0.1f, horizontalDividerOffset + 0.01f, 0.0f), new Vector2f(0.0f, 1.0f)));
			
			dividerPos += 0.2f;
		}

		renderer.registerRenderable(divider);
		divider.setTexture(whiteTexture);
		allRenderables.add(divider);
	
		/*
		 * Create player label
		 */
		GUIElement playerLabel = new GUIElement();
		vertices = playerLabel.getVertices();
		vertices.add(new Vertex(new Vector3f(0.5f, 0.85f, 0.0f), new Vector2f(0.0f, 1.0f)));
		vertices.add(new Vertex(new Vector3f(0.95f, 0.85f, 0.0f), new Vector2f(1.0f, 1.0f)));
		vertices.add(new Vertex(new Vector3f(0.5f, 0.95f, 0.0f), new Vector2f(0.0f, 0.0f)));

		vertices.add(new Vertex(new Vector3f(0.95f, 0.85f, 0.0f), new Vector2f(1.0f, 1.0f)));
		vertices.add(new Vertex(new Vector3f(0.95f, 0.95f, 0.0f), new Vector2f(1.0f, 0.0f)));
		vertices.add(new Vertex(new Vector3f(0.5f, 0.95f, 0.0f), new Vector2f(0.0f, 0.0f)));
		
		renderer.registerRenderable(playerLabel);
		
		playerLabel.setTexture(playerLabelTexture);
		allRenderables.add(playerLabel);

		/*
		 * Create score board for player
		 */
		playerScoreBoard = new GUIElement();
		playerScoreBoard.setTexture(numbersTexture);
		allRenderables.add(playerScoreBoard);
		
		/*
		 * Create CPU label
		 */
		GUIElement cpuLabel = new GUIElement();
		vertices = cpuLabel.getVertices();
		vertices.add(new Vertex(new Vector3f(-0.95f, 0.85f, 0.0f), new Vector2f(0.0f, 1.0f)));
		vertices.add(new Vertex(new Vector3f(-0.5f, 0.85f, 0.0f), new Vector2f(1.0f, 1.0f)));
		vertices.add(new Vertex(new Vector3f(-0.95f, 0.95f, 0.0f), new Vector2f(0.0f, 0.0f)));

		vertices.add(new Vertex(new Vector3f(-0.5f, 0.85f, 0.0f), new Vector2f(1.0f, 1.0f)));
		vertices.add(new Vertex(new Vector3f(-0.5f, 0.95f, 0.0f), new Vector2f(1.0f, 0.0f)));
		vertices.add(new Vertex(new Vector3f(-0.95f, 0.95f, 0.0f), new Vector2f(0.0f, 0.0f)));
		
		renderer.registerRenderable(cpuLabel);
		
		cpuLabel.setTexture(cpuLabelTexture);
		allRenderables.add(cpuLabel);
		
		/*
		 * Create score board for CPU
		 */
		cpuScoreBoard = new GUIElement();
		cpuScoreBoard.setTexture(numbersTexture);
		allRenderables.add(cpuScoreBoard);
		
		/*
		 * Initialize score boards
		 */
		updateScoreBoards();
		
		/*
		 * Load sounds
		 */
		try {
			pingSoundId = soundSystem.loadWavFile("/sound/ping.wav");
			pongSoundId = soundSystem.loadWavFile("/sound/pong.wav");
			wallSoundId = soundSystem.loadWavFile("/sound/wall.wav");
			uahSoundId = soundSystem.loadWavFile("/sound/uah.wav");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*
		 * Prepare sound sources
		 */
		padSourceId = soundSystem.createSource();
		wallSourceId = soundSystem.createSource();
		uahSourceId = soundSystem.createSource();
		
		/*
		 * Prepare sound listener
		 */
		soundSystem.initializeListener();
	}
	
	public void cleanupData() {
		soundSystem.deleteSource(padSourceId);
		soundSystem.deleteSource(wallSourceId);
		soundSystem.deleteSource(uahSourceId);
	}

	public void initializeGame() {
		//Put the ball in the middle
		ball.translate(new Vector3f(-ball.getTranslationMatrix().m30, -ball.getTranslationMatrix().m31, 0.0f));
		
		//Give the ball a nudge
		ballDirection = new Vector3f();
		ballDirection.x = ballStartSpeed;
		ballDirection.y = 0.5f - ((float)Math.random());
		//Set game to running
		gameRunning = true;
	}
	
	private Vector3f ballTranslation = new Vector3f();
	public void updateData(Long deltaTime) {
		/*
		 * Update game entities
		 */
		if (inputHandler.getKeyStatus()[Keyboard.KEY_UP] && playerPaddle.getTranslationMatrix().m31 < 1.0f) {
			//User pressed key up
			playerPaddle.translate(new Vector3f(0.0f, (paddleSpeed * deltaTime) / 1000.0f, 0.0f));
		}
		if (inputHandler.getKeyStatus()[Keyboard.KEY_DOWN] && playerPaddle.getTranslationMatrix().m31 > -1.0f) {
			//User pressed key down
			playerPaddle.translate(new Vector3f(0.0f, -(paddleSpeed * deltaTime) / 1000.0f, 0.0f));
		}
		if (gameRunning) {
			//Move ball
			ballTranslation.x = (ballDirection.x * deltaTime) / 1000.0f;
			ballTranslation.y = (ballDirection.y * deltaTime) / 1000.0f;
			ball.translate(ballTranslation);
			
			/*
			 * Check for collisions
			 */
			if (collisionOccured(ball, playerPaddle)) {
				ballDirection.x = -ballDirection.x;
				ballDirection.y += ((ball.getTranslationMatrix().m31 - playerPaddle.getTranslationMatrix().m31) * 3);
				ballDirection.x *= 1.02;
				ballTranslation.x = (ballDirection.x * deltaTime) / 1000.0f;
				ballTranslation.y = (ballDirection.y * deltaTime) / 1000.0f;
				ball.translate(ballTranslation);
				soundSystem.playSound(padSourceId, pingSoundId);
			}
			if (collisionOccured(ball, cpuPaddle)) {
				ballDirection.x = -ballDirection.x;
				ballDirection.y += ((ball.getTranslationMatrix().m31 - cpuPaddle.getTranslationMatrix().m31) * 3);
				ballDirection.x *= 1.02;
				ballTranslation.x = (ballDirection.x * deltaTime) / 1000.0f;
				ballTranslation.y = (ballDirection.y * deltaTime) / 1000.0f;
				ball.translate(ballTranslation);
				soundSystem.playSound(padSourceId, pongSoundId);
			}

			/*
			 * Check if ball hit the edge of the screen
			 */
			if (ball.getBoundingBox().top > 1.0f) {
				ballDirection.y = -ballDirection.y;
				ballTranslation.x = (ballDirection.x * deltaTime) / 1000.0f;
				ballTranslation.y = (ballDirection.y * deltaTime) / 1000.0f;
				ball.translate(ballTranslation);
				soundSystem.playSound(wallSourceId, wallSoundId);
			}
			if (ball.getBoundingBox().bottom < -1.0f) {
				ballDirection.y = -ballDirection.y;
				ballTranslation.x = (ballDirection.x * deltaTime) / 1000.0f;
				ballTranslation.y = (ballDirection.y * deltaTime) / 1000.0f;
				ball.translate(ballTranslation);
				soundSystem.playSound(wallSourceId, wallSoundId);
			}
			if (ball.getBoundingBox().left < -1.0f) {
				gameRunning = false;
				playerScore++;
				updateScoreBoards();
				soundSystem.playSound(uahSourceId, uahSoundId);
			}
			if (ball.getBoundingBox().right > 1.0f) {
				gameRunning = false;
				cpuScore++;
				updateScoreBoards();
				soundSystem.playSound(uahSourceId, uahSoundId);
			}
			
			/*
			 * Run AI
			 */
			if (ball.getTranslationMatrix().m31 > cpuPaddle.getTranslationMatrix().m31) {
				cpuPaddle.translate(new Vector3f(0.0f, (paddleSpeed * deltaTime) / 1000.0f, 0.0f));
			}
			if (ball.getTranslationMatrix().m31 < cpuPaddle.getTranslationMatrix().m31) {
				cpuPaddle.translate(new Vector3f(0.0f, (-paddleSpeed * deltaTime) / 1000.0f, 0.0f));
			}
		} else {
			if (inputHandler.getKeyStatus()[Keyboard.KEY_SPACE]) {
				initializeGame();
			}
		}
		
		/*
		 * Update GUI elements
		 */
		//Only need to update when scoring occurs
	}
	
	public List<Renderable> getAllRenderables() {
		return allRenderables;
	}
	
	private boolean collisionOccured(GameEntity entity1, GameEntity entity2) {
		boolean rc = false;
		
		BoundingBox box1 = entity1.getBoundingBox();
		BoundingBox box2 = entity2.getBoundingBox();

		boolean OutsideTop = box1.bottom > box2.top;
		boolean OutsideBottom = box1.top < box2.bottom;
		boolean OutsideLeft = box1.right < box2.left;
		boolean OutsideRight = box1.left > box2.right;
		rc = !(OutsideBottom || OutsideTop || OutsideLeft || OutsideRight);

		return rc;
	}
	
	private void updateScoreBoards() {
		char[] cpuScoreChars = scoreNumberFormat.format(cpuScore).toCharArray();
		
		Vector2f[] textureCoords;
		/*
		 * CPU score board
		 */
		List<Vertex> vertices = cpuScoreBoard.getVertices();
		vertices.clear();
		
		//100s
		textureCoords = numbersTextureCoordinates.get(cpuScoreChars[0]);
		vertices.add(new Vertex(new Vector3f(-0.5f, 0.85f, 0.0f), textureCoords[0]));
		vertices.add(new Vertex(new Vector3f(-0.4f, 0.85f, 0.0f), textureCoords[1]));
		vertices.add(new Vertex(new Vector3f(-0.5f, 0.95f, 0.0f), textureCoords[2]));

		vertices.add(new Vertex(new Vector3f(-0.4f, 0.85f, 0.0f), textureCoords[3]));
		vertices.add(new Vertex(new Vector3f(-0.4f, 0.95f, 0.0f), textureCoords[4]));
		vertices.add(new Vertex(new Vector3f(-0.5f, 0.95f, 0.0f), textureCoords[5]));
		
		//10s
		textureCoords = numbersTextureCoordinates.get(cpuScoreChars[1]);
		vertices.add(new Vertex(new Vector3f(-0.4f, 0.85f, 0.0f), textureCoords[0]));
		vertices.add(new Vertex(new Vector3f(-0.3f, 0.85f, 0.0f), textureCoords[1]));
		vertices.add(new Vertex(new Vector3f(-0.4f, 0.95f, 0.0f), textureCoords[2]));

		vertices.add(new Vertex(new Vector3f(-0.3f, 0.85f, 0.0f), textureCoords[3]));
		vertices.add(new Vertex(new Vector3f(-0.3f, 0.95f, 0.0f), textureCoords[4]));
		vertices.add(new Vertex(new Vector3f(-0.4f, 0.95f, 0.0f), textureCoords[5]));
		
		//1s
		textureCoords = numbersTextureCoordinates.get(cpuScoreChars[2]);
		vertices.add(new Vertex(new Vector3f(-0.3f, 0.85f, 0.0f), textureCoords[0]));
		vertices.add(new Vertex(new Vector3f(-0.2f, 0.85f, 0.0f), textureCoords[1]));
		vertices.add(new Vertex(new Vector3f(-0.3f, 0.95f, 0.0f), textureCoords[2]));

		vertices.add(new Vertex(new Vector3f(-0.2f, 0.85f, 0.0f), textureCoords[3]));
		vertices.add(new Vertex(new Vector3f(-0.2f, 0.95f, 0.0f), textureCoords[4]));
		vertices.add(new Vertex(new Vector3f(-0.3f, 0.95f, 0.0f), textureCoords[5]));
		
		renderer.registerRenderable(cpuScoreBoard);

		/*
		 * Player score board
		 */
		char[] playerScoreChars = scoreNumberFormat.format(playerScore).toCharArray();

		vertices = playerScoreBoard.getVertices();
		vertices.clear();
		
		//100s
		textureCoords = numbersTextureCoordinates.get(playerScoreChars[0]);
		vertices.add(new Vertex(new Vector3f(0.2f, 0.85f, 0.0f), textureCoords[0]));
		vertices.add(new Vertex(new Vector3f(0.3f, 0.85f, 0.0f), textureCoords[1]));
		vertices.add(new Vertex(new Vector3f(0.2f, 0.95f, 0.0f), textureCoords[2]));

		vertices.add(new Vertex(new Vector3f(0.3f, 0.85f, 0.0f), textureCoords[3]));
		vertices.add(new Vertex(new Vector3f(0.3f, 0.95f, 0.0f), textureCoords[4]));
		vertices.add(new Vertex(new Vector3f(0.2f, 0.95f, 0.0f), textureCoords[5]));
		
		//10s
		textureCoords = numbersTextureCoordinates.get(playerScoreChars[1]);
		vertices.add(new Vertex(new Vector3f(0.3f, 0.85f, 0.0f), textureCoords[0]));
		vertices.add(new Vertex(new Vector3f(0.4f, 0.85f, 0.0f), textureCoords[1]));
		vertices.add(new Vertex(new Vector3f(0.3f, 0.95f, 0.0f), textureCoords[2]));

		vertices.add(new Vertex(new Vector3f(0.4f, 0.85f, 0.0f), textureCoords[3]));
		vertices.add(new Vertex(new Vector3f(0.4f, 0.95f, 0.0f), textureCoords[4]));
		vertices.add(new Vertex(new Vector3f(0.3f, 0.95f, 0.0f), textureCoords[5]));
		
		//1s
		textureCoords = numbersTextureCoordinates.get(playerScoreChars[2]);
		vertices.add(new Vertex(new Vector3f(0.4f, 0.85f, 0.0f), textureCoords[0]));
		vertices.add(new Vertex(new Vector3f(0.5f, 0.85f, 0.0f), textureCoords[1]));
		vertices.add(new Vertex(new Vector3f(0.4f, 0.95f, 0.0f), textureCoords[2]));

		vertices.add(new Vertex(new Vector3f(0.5f, 0.85f, 0.0f), textureCoords[3]));
		vertices.add(new Vertex(new Vector3f(0.5f, 0.95f, 0.0f), textureCoords[4]));
		vertices.add(new Vertex(new Vector3f(0.4f, 0.95f, 0.0f), textureCoords[5]));
		
		renderer.registerRenderable(playerScoreBoard);
	}
	
}
