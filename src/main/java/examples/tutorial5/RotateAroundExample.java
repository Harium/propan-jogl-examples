package examples.tutorial5;	

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.harium.etyl.commons.graphics.Color;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

import com.harium.propan.linear.Camera;
import com.harium.etyl.commons.event.KeyEvent;
import com.harium.etyl.commons.event.PointerEvent;
import com.harium.etyl.loader.image.ImageLoader;
import com.harium.propan.core.context.ApplicationGL;
import com.harium.propan.core.graphics.AWTGraphics3D;
import com.harium.propan.core.graphics.Graphics3D;
import com.harium.propan.graphics.CustomBillboard;
import com.harium.propan.loader.TextureLoader;
import com.harium.propan.util.DirectionUtil;

import com.badlogic.gdx.math.Vector3;
import com.jogamp.opengl.util.texture.Texture;

public class RotateAroundExample extends ApplicationGL {

	private Camera camera;

	private CustomBillboard billboard;
	private Texture texture;
	
	private Vector3 sphereCenter;
	private Vector3 rotationAxis;

	private int markerCount = 3;

	protected float mx = 0;
	protected float my = 0;

	protected boolean click = false;

	private List<Boolean> activeMarkers = new ArrayList<Boolean>(markerCount);

	public RotateAroundExample(int w, int h) {
		super(w, h);
	}

	@Override
	public void init(Graphics3D graphics) {
		for (int i = 0; i < markerCount; i++) {
			activeMarkers.add(false);
		}

		billboard = new CustomBillboard(5,5,new Vector3(2,0,0));
		billboard.turnTo(new Vector3(0,2,0));

		BufferedImage spriteImage = ImageLoader.getInstance().getImage("active_mark.png");		
		texture = TextureLoader.getInstance().loadTexture(spriteImage);

		activeMarkers.add(1, true);
		
		sphereCenter = new Vector3(billboard.center).add(0,1,0);
		rotationAxis = DirectionUtil.directionOrthogonal(sphereCenter, billboard.center);
		
		billboard.center.setZero();
	}

	@Override
	public void load() {
		camera = new Camera(0, 15, 0.001f);
		loading = 100;
	}

	@Override
	public void reshape(Graphics3D graphics, int x, int y, int width, int height) {
		AWTGraphics3D g = (AWTGraphics3D) graphics;
		GL2 gl = g.getGL2();
		GLU glu = g.getGLU();

		gl.glViewport (x, y, width, height);

		gl.glMatrixMode(GL2.GL_PROJECTION);

		gl.glLoadIdentity();

		float aspect = (float)width / (float)height; 

		glu.gluPerspective(60, aspect, 1, 100);

		gl.glMatrixMode(GL2.GL_MODELVIEW);

		gl.glLoadIdentity();

	}

	@Override
	public void update(long now) {

	}

	@Override
	public void updateKeyboard(KeyEvent event) {

	}

	@Override
	public void updateMouse(PointerEvent event) {

	}

	@Override
	public void preDisplay(Graphics3D graphics) {
		AWTGraphics3D g = (AWTGraphics3D) graphics;
		GL2 gl = g.getGL2();

		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl.glClearColor(1f, 1f, 1f, 1);
		gl.glEnable(GL.GL_DEPTH_TEST);
	}
	
	float angle = 2;

	@Override
	public void display(Graphics3D graphics) {
		AWTGraphics3D g = (AWTGraphics3D) graphics;
		GL2 gl = g.getGL().getGL2();
		g.setColor(Color.WHITE);

		//Transform by Camera
		g.updateCamera(camera);

		//Draw Billboard
		texture.enable(gl);
		texture.bind(gl);
		g.drawBillboard(billboard);
		texture.disable(gl);
		
		DirectionUtil.rotateAround(sphereCenter, billboard.center, rotationAxis, angle);
		
		billboard.turnTo(sphereCenter);
		
		gl.glPushMatrix();
		g.setColor(Color.BLUE);
		g.drawSphere(1, sphereCenter.x, sphereCenter.y, sphereCenter.z);
		gl.glPopMatrix();

		gl.glFlush();
	}

}