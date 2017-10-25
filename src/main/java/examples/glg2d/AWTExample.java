package examples.glg2d;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JFrame;

import org.jogamp.glg2d.GLG2DCanvas;
import org.jogamp.glg2d.GLG2DPanel;

public class AWTExample {
	public static void main(String[] args) {
		JFrame frame = new JFrame("GLG2D AWT Example");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(300, 300));

		JComponent comp = Example.createComponent();

		GLG2DCanvas canvas = new GLG2DCanvas();
		
		frame.setContentPane(canvas);

		frame.pack();
		frame.setVisible(true);
		
	}
}
