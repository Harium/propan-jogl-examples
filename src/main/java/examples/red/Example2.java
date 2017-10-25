package examples.red;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

import com.harium.propan.core.context.ApplicationGL;
import com.harium.propan.core.graphics.AWTGraphics3D;
import com.harium.propan.core.graphics.Graphics3D;

/**
 * Based on example found at: https://www3.ntu.edu.sg/home/ehchua/programming/opengl/JOGL2.0.html
 * The example had a bug, the author forgot to translate the triangle 
 */
public class Example2 extends ApplicationGL {

	private float angle = 0.0f;

	public Example2(int w, int h) {
		super(w, h);
	}

	@Override
	public void load() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(Graphics3D graphics) {
		AWTGraphics3D g = (AWTGraphics3D) graphics;
		GL2 gl = g.getGL2(); // get the OpenGL graphics context

		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // set background (clear) color
		gl.glClearDepth(1.0f);      // set clear depth value to farthest
		gl.glEnable(GL.GL_DEPTH_TEST); // enables depth testing
		gl.glDepthFunc(GL.GL_LEQUAL);  // the type of depth test to do
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST); // best perspective correction
		gl.glShadeModel(GL2.GL_SMOOTH); // blends colors nicely, and smoothes out lighting
	}

	@Override
	public void reshape(Graphics3D graphics, int x, int y, int width, int height) {
		AWTGraphics3D g = (AWTGraphics3D) graphics;
		GL2 gl = g.getGL2(); // get the OpenGL graphics context
		GLU glu = g.getGLU();

		if (height == 0) height = 1;   // prevent divide by zero
		float aspect = (float)width / height;

		// Set the view port (display area) to cover the entire window
		gl.glViewport(0, 0, width, height);

		// Setup perspective projection, with aspect ratio matches viewport
		gl.glMatrixMode(GL2.GL_PROJECTION);  // choose projection matrix
		gl.glLoadIdentity();             // reset projection matrix

		glu.gluPerspective(45.0, aspect, 0.1, 100.0); // fovy, aspect, zNear, zFar

		// Enable the model-view transform
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity(); // reset
	}

	@Override
	public void display(Graphics3D graphics) {
		render(graphics);
		update();
	}

	// Render a triangle
	private void render(Graphics3D graphics) {
		AWTGraphics3D g = (AWTGraphics3D) graphics;
		// Get the OpenGL graphics context
		GL2 gl = g.getGL2();
		// Clear the color and the depth buffers
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		// Reset the view (x, y, z axes back to normal)
		gl.glLoadIdentity();

		gl.glTranslatef(0.0f, 0.0f, -6.0f); // translate into the screen

		// Draw a triangle
		float sin = (float)Math.sin(angle);
		float cos = (float)Math.cos(angle);
		gl.glBegin(GL.GL_TRIANGLES);
		gl.glColor3f(1.0f, 0.0f, 0.0f);   // Red
		gl.glVertex2d(-cos, -cos);
		gl.glColor3f(0.0f, 1.0f, 0.0f);   // Green
		gl.glVertex2d(0.0f, cos);
		gl.glColor3f(0.0f, 0.0f, 1.0f);   // Blue
		gl.glVertex2d(sin, -sin);
		gl.glEnd();
	}

	// Update the angle of the triangle after each frame
	private void update() {
		angle += 0.01f;
	}

}
