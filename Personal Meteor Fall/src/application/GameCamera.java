package application;


public class GameCamera {
	private float x;
	private float y;
	
	public GameCamera(float x, float y){
		this.x = x;
		this.y = y;
	}
	public void followPlayer(GameObject player){
		// = (float) (-player.getY() + Settings.HEIGHT-300);
		x = (float) (-player.getX() + Settings.WIDTH-600);
	}
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}


}
