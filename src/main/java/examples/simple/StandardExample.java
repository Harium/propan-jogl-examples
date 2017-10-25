package examples.simple;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

import com.harium.propan.core.view.FlyView;
import com.harium.propan.core.graphics.AWTGraphics3D;
import com.harium.propan.core.graphics.Graphics3D;

public class StandardExample {

	public static void standardScene(Graphics3D graphics, int x, int y, int w, int h) {
		AWTGraphics3D g = (AWTGraphics3D) graphics;
		GL2 gl = g.getGL2();
		GLU glu = g.getGLU();

		gl.glViewport(x, y, w, h);

		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		
		double aspect = (double)w/(double)h;

		glu.gluPerspective(60.0, aspect, 0.1, 500.0);

		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
	
	public static void drawAxis(GL2 gl, float axisSize) {

		//Draw Axis
		gl.glLineWidth(2.5f);

		//Draw X Axis
		gl.glColor3f(1, 0, 0);
		gl.glBegin(GL.GL_LINES);
		gl.glVertex3f(0, 0, 0);
		gl.glVertex3f(axisSize, 0, 0);
		gl.glEnd();

		//Draw Y Axis
		gl.glColor3f(0, 1, 0);
		gl.glBegin(GL.GL_LINES);
		gl.glVertex3f(0, 0, 0);
		gl.glVertex3f(0, axisSize, 0);
		gl.glEnd();

		//Draw Z Axis
		gl.glColor3f(0, 0, 1);
		gl.glBegin(GL.GL_LINES);
		gl.glVertex3f(0, 0, 0);
		gl.glVertex3f(0, 0, axisSize);
		gl.glEnd();
	}
	
	public static void drawRay(GL2 gl, Ray ray, FlyView view, float size) {
		Vector3 v = new Vector3(ray.direction);
		v.scl(size);

		gl.glBegin(GL.GL_LINES);
		gl.glVertex3d(view.getX(), view.getY()-0.1, view.getZ());
		gl.glVertex3d(view.getX()+v.x, view.getY()+v.y, view.getZ()+v.z);
		gl.glEnd();
	}
	
}
