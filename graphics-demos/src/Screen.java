import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Screen extends JPanel implements KeyListener{
	private static final int RADIUS = 320; 
	private BufferedImage sprite;
	private int spriteX = 0, spriteY = 0;
	private int groundLevel;
	private int vx = 0, vy = 0;
	private double rotationAngle = 0; //Angle sprite must be rotated (radians)
	private boolean onLoop = false;
	private boolean goingDown = false;
	public Screen(){
		super();
		loadAllResources();
		this.setFocusable(true);
		this.addKeyListener(this);
	}
	
	public void loadAllResources(){
		try {
			sprite = loadImage("./res/sri walk.png");
		} 
		catch (IOException e) {
			System.out.println("Could not load one or more resources.");
			//e.printStackTrace();
		}
	}
	
	public static BufferedImage loadImage(String path) throws IOException{
		try{
			File image = new File(path);
			BufferedImage i = ImageIO.read(image);
			return i;
		}
		catch(Exception ex){
			ex.printStackTrace();
			throw new IOException();
		}
	}
	
	public void imposeGravity(){
		if(onLoop) return;
		int groundLevel = getHeight() - 50;
		if(spriteY + sprite.getHeight(null) < groundLevel){
			vy += 1;
			spriteY += vy;
		}
		else{
			spriteY = groundLevel - sprite.getHeight(null);
			vy = 0;
		}
	}
	
	private void moveLeft(){
		vx += 1;
		spriteX -= vx;
		if(spriteX < 0) spriteX = 0;
	}
	
	private void moveRight(){
		if(onLoop) runLoop(); 
		else{
			vx += 1;
			spriteX += vx;
			if(spriteX > getWidth()) spriteX = getWidth();
			if(spriteX > getWidth()/2) onLoop = true;
		}
	}
	
	private void runLoop(){
		if(goingDown) spriteY += 10;
		else spriteY -= 10;
		int centerY = groundLevel/2;
		int centerX = getWidth() / 2;
		int dy = spriteY - centerY;
		int dx = (int) (Math.sqrt((RADIUS * RADIUS) - (dy * dy)));
		spriteX = centerX + dx - 2 * sprite.getHeight(null);
		if(goingDown) spriteX = centerX - dx;
		if(dy < -RADIUS){
			dy = -RADIUS; 
			goingDown = true;
		}
		else if(dy > RADIUS) goingDown = false;
		rotationAngle = -Math.acos((double)dy/RADIUS);
		if(goingDown) rotationAngle = Math.acos((double)dy/RADIUS);
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		groundLevel = getHeight() - 50;
		g.drawLine(0, groundLevel, getWidth(), groundLevel); //Draw ground
		g.drawOval(getWidth()/2 - RADIUS, groundLevel - 2 * RADIUS, 2 * RADIUS - sprite.getHeight(), 2 * RADIUS);//Draw loop centered at (getWidth()/2, groundLevel/2
		//Code to rotate image
		int anchorX = sprite.getWidth(null)/2, anchorY = sprite.getHeight(null)/2;
		AffineTransform t = AffineTransform.getRotateInstance(rotationAngle, anchorX, anchorY);
		AffineTransformOp op = new AffineTransformOp(t, AffineTransformOp.TYPE_BILINEAR);
		g.drawImage(op.filter(sprite, null), spriteX, spriteY, null);
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		int keycode = arg0.getKeyCode();
		switch(keycode){
		case KeyEvent.VK_RIGHT:
			moveRight(); break;
		case KeyEvent.VK_LEFT:
			if(onLoop) break;
			moveLeft();
		default: break;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
