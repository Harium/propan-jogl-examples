package examples.custom;

import static com.jogamp.opengl.GL.GL_LINEAR;
import static com.jogamp.opengl.GL.GL_TEXTURE_2D;
import static com.jogamp.opengl.GL.GL_TEXTURE_MAG_FILTER;
import static com.jogamp.opengl.GL.GL_TEXTURE_MIN_FILTER;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

import com.harium.propan.linear.Camera;
import com.harium.etyl.commons.event.KeyEvent;
import com.harium.etyl.commons.event.MouseEvent;
import com.harium.etyl.commons.event.PointerEvent;
import com.harium.etyl.core.graphics.Graphics;
import com.harium.etyl.linear.Point3D;
import com.harium.propan.core.context.ApplicationGL;
import com.harium.propan.core.graphics.AWTGraphics3D;
import com.harium.propan.core.graphics.Graphics3D;
import com.harium.propan.loader.TextureLoader;

import com.jogamp.opengl.util.texture.Texture;

public class RadialMarkerApplication extends ApplicationGL {

	private Texture marker;
	
	private Camera camera;
	
	protected float mx = 0;
	protected float my = 0;

	protected boolean click = false;
	
	private double angleX = 0;
	
	private double angleY = 0;
	
	private double angleZ = 0;
	
	private Point3D originMarker;
	private Point3D leftMarker;
	
	private double angleXY = 0;
	private double angleXZ = 0;
	private double angleYZ = 0;
	
	double tileSize = 5;

	public RadialMarkerApplication(int w, int h) {
		super(w, h);
	}

	@Override
	public void init(Graphics3D graphics) {
		
		//marker = TextureLoader.getInstance().loadTexture("/mark.png");
		
		BufferedImage image = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 200, 200);
		
		g.setColor(Color.RED);
		g.fillOval(50, 50, 100, 100);
		
		g.setColor(Color.BLACK);
		
		int stroke = 8;
		
		g.setStroke(new BasicStroke(stroke));
		g.drawRect(stroke, stroke, 200-stroke*2, 200-stroke*2);
		
		marker = TextureLoader.getInstance().loadTexture(image);
	}
	
	@Override
	public void load() {
		
		camera = new Camera(0,0,10);
		
		originMarker = new Point3D(0, 0);
		
		leftMarker = new Point3D(-2, -1.5);
		
		loading = 100;
	}
	
	/*protected void lookCamera(Graphics3D g) {
		GL2 gl = g.getGL2();
		GLU glu = g.getGLU();
		
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();

		double targetx = 0;
		double targety = 0;
		double targetz = 0;
		
		glu.gluLookAt( camera.getX(), camera.getY(), camera.getZ(), targetx, targety, targetz, 0, 1, 0 );
	}*/
	
	protected void drawFloor(GL2 gl) {

		gl.glColor3d(1,1,1);

		gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

		drawGrid(gl,200,120);

	}

	private void drawGrid(GL2 gl, double x, double y) {
		
		drawTile(gl, originMarker.getX(), originMarker.getY(), tileSize, marker);
		
	}

	//Modified to draw tile from center
	private void drawTile(GL2 gl, double x, double y, double tileSize, Texture texture) {

		texture.enable(gl);
		texture.bind(gl);
		
		gl.glBegin(GL2.GL_QUADS);

		//(0,0)
		gl.glTexCoord2d(0, 0);
		gl.glVertex3d(x*tileSize-tileSize/2, 0, y*tileSize-tileSize/2);

		//(1,0)
		gl.glTexCoord2d(1, 0);
		gl.glVertex3d(x*tileSize-tileSize/2+tileSize, 0, y*tileSize-tileSize/2);

		//(1,1)
		gl.glTexCoord2d(1, 1);
		gl.glVertex3d(x*tileSize+tileSize-tileSize/2, 0, y*tileSize+tileSize-tileSize/2);

		//(0,1)
		gl.glTexCoord2d(0, 1);
		gl.glVertex3d(x*tileSize-tileSize/2, 0, y*tileSize+tileSize-tileSize/2);

		gl.glEnd();
		
		texture.disable(gl);
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
	
	private boolean upArrow = false;
	private boolean downArrow = false;
	private boolean leftArrow = false;
	private boolean rightArrow = false;
	
	@Override
	public void update(long now) {
		
		if(upArrow) {
			angleX += 1;
		} else if(downArrow) {
			angleX -= 1;
		}
		
		if(leftArrow) {
			angleZ += 1;
		} else if(rightArrow) {
			angleZ -= 1;
		}
		
	}
	
	@Override
	public void updateKeyboard(KeyEvent event) {
		
		if(event.isKeyDown(KeyEvent.VK_UP_ARROW)) {
			
			upArrow = true;
			
		} else if (event.isKeyUp(KeyEvent.VK_UP_ARROW)) {
			
			upArrow = false;
		}
				
		if(event.isKeyDown(KeyEvent.VK_DOWN_ARROW)) {
			
			downArrow = true;
			
		} else if(event.isKeyUp(KeyEvent.VK_DOWN_ARROW)) {
			
			downArrow = false;
		}
		
		if(event.isKeyDown(KeyEvent.VK_LEFT_ARROW)) {
			
			leftArrow = true;
			
		} else if(event.isKeyUp(KeyEvent.VK_LEFT_ARROW)) {
			
			leftArrow = false;
		}
		
		if(event.isKeyDown(KeyEvent.VK_RIGHT_ARROW)) {
			
			rightArrow = true;
			
		} else if (event.isKeyUp(KeyEvent.VK_RIGHT_ARROW)) {
			
			rightArrow = false;
		}
		
		if(event.isKeyDown(KeyEvent.VK_VIRGULA)) {
			
			angleY += 5;
			
		} else if(event.isKeyDown(KeyEvent.VK_PONTO)) {
			
			angleY -= 5;
			
		}
	}
	
	@Override
	public void updateMouse(PointerEvent event) {

		mx = event.getX();
		my = event.getY();

		if(event.isButtonDown(MouseEvent.MOUSE_BUTTON_LEFT)) {
			camera.position.z += 0.1f;
			click = true;
		}

		if(event.isButtonUp(MouseEvent.MOUSE_BUTTON_LEFT)) {
			camera.position.z -= 0.1f;
			click = false;
		}
	}

	@Override
	public void preDisplay(Graphics3D graphics) {
		AWTGraphics3D g = (AWTGraphics3D) graphics;
		GL2 gl = g.getGL2();

		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl.glClearColor(1f, 1f, 1f, 1);
		
		g.setColor(Color.YELLOW);
		g.fillRect(10, 10, w, 60);
	}
	
	@Override
	public void display(Graphics3D graphics) {
		AWTGraphics3D g = (AWTGraphics3D) graphics;
		GL2 gl = g.getGL2();
				
		resetScene(graphics);
		//Draw Scene
		drawFloor(gl);
		
		resetScene(graphics);
		
		angleXY = leftMarker.angleXY(originMarker);
		angleXZ = leftMarker.angleXZ(originMarker);
		angleYZ = leftMarker.angleYZ(originMarker);
		
		//Angle XY turn in Z
		gl.glRotated(angleXY, 0, 0, 1);
		
		drawTile(gl, leftMarker.getX(), leftMarker.getY(), tileSize, marker);

		gl.glFlush();

	}

	protected void resetScene(Graphics3D graphics) {
		AWTGraphics3D g = (AWTGraphics3D) graphics;
		GL2 gl = g.getGL2();
		
		g.updateCamera(camera);
		
		gl.glRotated(90, 1, 0, 0);
		
		gl.glRotated(angleX, 1, 0, 0);
		gl.glRotated(angleY, 0, 1, 0);
		gl.glRotated(angleZ, 0, 0, 1);
	}
	

	@Override
	public void draw(Graphics g) {

		//Draw Gui
		g.setColor(Color.WHITE);
		g.drawStringShadow("Scene", 20, 20, Color.BLACK);
		
		g.drawStringShadow("AngleX: "+(angleX-5), 20, 40, Color.BLACK);
		g.drawStringShadow("AngleY: "+(angleY), 20, 60, Color.BLACK);
		g.drawStringShadow("AngleZ: "+(angleZ), 20, 80, Color.BLACK);
		
		g.drawStringShadow("AngleXY: "+(angleXY), 20, 100, Color.BLACK);
		g.drawStringShadow("AngleXZ: "+(angleXZ), 20, 130, Color.BLACK);
		g.drawStringShadow("AngleYZ: "+(angleYZ),20, 160, Color.BLACK);
	}
	
}