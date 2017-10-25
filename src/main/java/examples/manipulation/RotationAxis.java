package examples.manipulation;


import java.awt.Color;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;

import com.harium.propan.core.view.FlyView;
import com.harium.etyl.commons.event.KeyEvent;
import com.harium.etyl.commons.event.MouseEvent;
import com.harium.etyl.commons.event.PointerEvent;
import com.harium.etyl.core.graphics.Graphics;
import com.harium.propan.core.context.ApplicationGL;
import com.harium.propan.core.graphics.AWTGraphics3D;
import com.harium.propan.core.graphics.Graphics3D;

public class RotationAxis extends ApplicationGL {

	protected int mx = 0;
	protected int my = 0;
	
	protected boolean click = false;

	protected FlyView view;

	private static final int NONE = -1;
	private static final int AXIS_H = 4;
	private static final int AXIS_V = 5;

	boolean drawRay = false;
	boolean drawBoundingBoxes = true;
	boolean rotating = false;
	
	boolean turnRight = false;
	boolean turnLeft = false;
	float keyboardAngle = 0;
	float turnSpeed = 1;
	

	double tileSize = 1;
	int selected = NONE;
	int sx, sy;
	float value;
	float lastOffset;

	float axisSize = 100f;
	float axisWidth = 1.5f;
	float speed = 0.1f;

	BoundingBox vAxis;
	BoundingBox hAxis;
	
	private static final BoundingBox NO_AXIS = new BoundingBox();
	BoundingBox collisionAxis;

	Matrix4 transform = new Matrix4();
	
	Vector3 axis = new Vector3();

	public RotationAxis(int w, int h) {
		super(w, h);
	}

	@Override
	public void init(Graphics3D graphics) {
		view = new FlyView(30, 3.6f, 0);
		view.getAim().setAngleY(190);

		AWTGraphics3D g = (AWTGraphics3D) graphics;
		GL2 gl = g.getGL2(); // get the OpenGL graphics context

		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // set background (clear) color
		gl.glClearDepth(1.0f);      // set clear depth value to farthest
		gl.glEnable(GL.GL_DEPTH_TEST); // enables depth testing
		gl.glDepthFunc(GL.GL_LEQUAL);  // the type of depth test to do
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST); // best perspective correction
		gl.glShadeModel(GL2.GL_SMOOTH); // blends colors nicely, and smoothes out lighting

		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
		gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_DECAL);

		vAxis = new BoundingBox(new Vector3(-axisSize/8, -axisWidth/2, -axisSize/8), new Vector3(axisSize/8, axisWidth/2, axisSize/8));
		hAxis = new BoundingBox(new Vector3(-axisSize/8, -axisSize/8, -axisWidth/2), new Vector3(axisSize/8, axisSize/8, axisWidth/2));
		
	}

	private void drawAxis(GL2 gl) {

		//Draw Axis
		gl.glLineWidth(axisWidth);

		//Draw X Axis
		gl.glColor3d(1.0, 0.0, 0.0);
		gl.glBegin(GL.GL_LINES);
		gl.glVertex3d(0.0, 0.0, 0.0);
		gl.glVertex3d(axisSize, 0, 0);
		gl.glEnd();

		//Draw Y Axis
		gl.glColor3d(0.0, 1.0, 0.0);
		gl.glBegin(GL.GL_LINES);
		gl.glVertex3d(0.0, 0.0, 0.0);
		gl.glVertex3d(0, axisSize, 0);
		gl.glEnd();

		//Draw Z Axis
		gl.glColor3d(0.0, 0.0, 1.0);
		gl.glBegin(GL.GL_LINES);
		gl.glVertex3d(0.0, 0.0, 0.0);
		gl.glVertex3d(0, 0, axisSize);
		gl.glEnd();
	}
	
	private void drawOriginalAxis(GL2 gl) {

		//Draw Axis
		gl.glLineWidth(axisWidth*3);

		gl.glBegin(GL.GL_LINES);
		//Draw Rotation Axis
		gl.glColor3d(0.8, 0.2, 0.8);
		gl.glVertex3d(0.0, 0.0, 0.0);
		gl.glVertex3d(axis.x*axisSize, axis.y*axisSize, axis.z*axisSize);
		gl.glEnd();

	}
	
	private void drawRay(GL2 gl, Ray ray) {

		float axisSize = 50;

		Vector3 v = new Vector3(ray.direction);
		v.scl(axisSize);

		//Draw Camera Axis
		gl.glColor3d(0.0, 0.0, 1.0);

		gl.glBegin(GL.GL_LINES);
		gl.glVertex3d(view.getX(), 1, view.getZ());
		gl.glVertex3d(view.getX()+v.x, view.getY()+v.y, view.getZ()+v.z);
		gl.glEnd();
	}

	@Override
	public void reshape(Graphics3D graphics, int x, int y, int width, int height) {
		AWTGraphics3D g = (AWTGraphics3D) graphics;
		GL2 gl = g.getGL2();
		GLU glu = g.getGLU();

		gl.glViewport((int)x, (int)y, (int)w, (int)h);

		gl.glMatrixMode(GL2.GL_PROJECTION);

		gl.glLoadIdentity();

		glu.gluPerspective(60.0, (double) w / (double) h, 0.1, 500.0);

		gl.glMatrixMode(GL2.GL_MODELVIEW);

		gl.glLoadIdentity();
	}

	@Override
	public void updateKeyboard(KeyEvent event) {
		view.updateKeyboard(event);

		if(event.isKeyDown(KeyEvent.VK_R)) {
			drawRay = true;
		} else if(event.isKeyUp(KeyEvent.VK_R)) {
			drawRay = false;
		}
		
		if(event.isKeyDown(KeyEvent.VK_L)) {
			turnRight = true;
		} else if(event.isKeyUp(KeyEvent.VK_L)) {
			turnRight = false;
		}
		
		if(event.isKeyDown(KeyEvent.VK_K)) {
			turnLeft = true;
		} else if(event.isKeyUp(KeyEvent.VK_K)) {
			turnLeft = false;
		}
		
		if(event.isAnyKeyDown(KeyEvent.VK_CTRL_LEFT, KeyEvent.VK_CTRL_RIGHT)) {
			rotating = true;
		} else if(event.isAnyKeyUp(KeyEvent.VK_CTRL_LEFT, KeyEvent.VK_CTRL_RIGHT)) {
			rotating = false;
		}
	}

	public void updateMouse(PointerEvent event) {

		mx = event.getX();
		my = event.getY();

		if(event.isButtonDown(MouseEvent.MOUSE_BUTTON_LEFT)) {

			if(!click) {
				click = true;

				if (collisionAxis == vAxis) {
					selected = AXIS_H;
					sx = mx;
					sy = my;
					
					axis = new Vector3(Vector3.Y).scl(-1);
				} else if(collisionAxis == hAxis) {
					selected = AXIS_V;
					sx = mx;
					sy = my;
					
					axis = new Vector3(Vector3.Z);
				}
				
				value = 0;
				lastOffset = 0;
			}

		} else if(event.isButtonUp(MouseEvent.MOUSE_BUTTON_LEFT)) {
			click = false;
			selected = NONE;
		}

		if (click && selected != NONE) {

			int deltaX = mx-sx;
			int deltaY = my-sy;
			
			float offset = 0;
			
			if(turnRight||turnLeft) {
				offset = keyboardAngle;
			}
			
			if (selected == AXIS_H) {
				offset = (speed*deltaX);
			} else if (selected == AXIS_V) {
				offset = (speed*deltaY);
			}
			
			transform.rotate(axis, -lastOffset);
			transform.rotate(axis, -offset);
			
			lastOffset = -offset;
		}

		if(event.isButtonDown(MouseEvent.MOUSE_BUTTON_RIGHT)) {
			drawRay = true;
		} else if(event.isButtonUp(MouseEvent.MOUSE_BUTTON_RIGHT)) {
			drawRay = false;
		}
	}

	@Override
	public void display(Graphics3D graphics) {

		updateControls(0);
		AWTGraphics3D g = (AWTGraphics3D) graphics;
		GL2 gl = g.getGL().getGL2();

		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl.glClearColor(1f, 1f, 1f, 1);

		//Transform by Aim
		g.aimCamera(view.getAim());
		
		//Draw Scene
		g.setColor(Color.BLACK);
		g.drawGrid(1, 150, 150);
		
		gl.glPushMatrix();
		gl.glMultMatrixf(transform.val, 0);
		drawOriginalAxis(gl);
		drawAxis(gl);

		if (selected == AXIS_H || collisionAxis == vAxis) {
			gl.glColor3d(1.0, 1.0, 0.0);
		} else {
			gl.glColor3d(1.0, 0.0, 0.0);
		}
		g.drawBoundingBox(vAxis);

		if (selected == AXIS_V || collisionAxis == hAxis) {
			gl.glColor3d(1.0, 1.0, 0.0);
		} else {
			gl.glColor3d(0.0, 1.0, 0.0);
		}
		g.drawBoundingBox(hAxis);

		gl.glPopMatrix();

		Ray ray = g.getCameraRay(mx, my);
		if(drawRay) {
			drawRay(gl, ray);
		}
		
		if (Intersector.intersectRayBoundsFast(ray, vAxis, transform)) {
			collisionAxis = vAxis;
		} else if (Intersector.intersectRayBoundsFast(ray, hAxis, transform)) {
			collisionAxis = hAxis;
		} else if(selected == NONE) {
			collisionAxis = NO_AXIS;
		}

		gl.glFlush();
	}

	@Override
	public void draw(Graphics g) {

		//Draw Gui
		g.setColor(Color.WHITE);
		g.drawStringShadow("Scene", 20, 60, Color.BLACK);
		g.drawStringShadow(Double.toString(view.getAim().getAngleY()), 20, 80, Color.BLACK);

		//orangeAim.simpleDraw(g, mx-orangeAim.getW()/2, my-orangeAim.getH()/2);
	}

	public void updateControls(long now) {		
		view.update(now);
		
		if (rotating) {
			transform.rotate(axis, 1);
		}
		
		if (turnRight) {
			keyboardAngle += turnSpeed;
		} else if (turnLeft) {
			keyboardAngle -= turnSpeed;
		}
	}

}