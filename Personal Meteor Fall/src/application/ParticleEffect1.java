package application;


import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.BlurType;
import javafx.scene.paint.Color;


public class ParticleEffect1 extends ParticleEffect {

	GameParticleID id;
	BlendMode blend;
	Color color;
	Point2D velocity;
	double radius;
	double decay;
	float x;
	float y;
	float r;
	float velX;
	float velY;
	float velR;
	float lifeTime = 1.0f;
	double width;
	double height;
	boolean isAlive = false;
	boolean removable = false;
	GameParticleManager particleManager;
	Game game;

	int depth = 85;
	
	public ParticleEffect1(Game game, Color color, float x, float y, double expireTime, Point2D velocity, double radius, BlendMode blend) {
		this.blend = blend;
		this.color = color;
		this.velocity = velocity;
		this.radius = radius;
		this.decay = 0.016 /expireTime;
		this.x = x;
		this.y = y;
		this.particleManager = game.getParticleManager();
		this.game = game;	
		if(Settings.ADD_GLOW){
		borderGlow.setOffsetY(0f);
		borderGlow.setOffsetX(0f);
		borderGlow.setColor(Color.ORANGE);
		borderGlow.setWidth(depth);
		borderGlow.setHeight(depth);
		borderGlow.setBlurType(BlurType.ONE_PASS_BOX);
		borderGlow.setSpread(0.2);
		}
	}
	public void updateUI(){
		x += velocity.getX();
		y += velocity.getY();
		lifeTime -= decay;
	}
	public boolean isAlive() {
		return lifeTime>0;
	}
	public void draw(GraphicsContext gc) {
		gc.setGlobalAlpha(lifeTime);
		gc.setGlobalBlendMode(blend);
		gc.setFill(Color.rgb(255, 190, 0,0.2));
		gc.fillOval(x, y-8, radius-8, radius+16);
		gc.setFill(color);
		gc.fillOval(x, y, radius, radius);
	//	if(Settings.ADD_GLOW)
	//	gc.setEffect(borderGlow);


		
	}	

	
}

