import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
@SuppressWarnings("serial")
public class Main extends JPanel{
	public static void main(String[] args){
		prepareWindow();
		while(true){
			scrn.imposeGravity();
			scrn.repaint();
			try {
				Thread.sleep(1000/60);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void prepareWindow(){
		window = new JFrame("Demo 1");
		window.setSize(1080, 720);
		scrn = new Screen();
		window.setContentPane(scrn);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}
	
	
	private static JFrame window;
	private static Screen scrn;

}
