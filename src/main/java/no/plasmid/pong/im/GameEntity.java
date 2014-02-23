package no.plasmid.pong.im;

import org.lwjgl.util.vector.Vector3f;

public class GameEntity extends Renderable {

	private BoundingBox boundingBox;
	
	public GameEntity() {
		super();
		
		this.boundingBox = new BoundingBox();
	}
	
	public void translate(Vector3f translation) {
		super.translate(translation);
		
		boundingBox.top += translation.y;
		boundingBox.bottom += translation.y;
		boundingBox.left += translation.x;
		boundingBox.right += translation.x;
	}
	
	public BoundingBox getBoundingBox() {
		return boundingBox;
	}
	
}
