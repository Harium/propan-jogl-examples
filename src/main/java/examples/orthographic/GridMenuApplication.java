package examples.orthographic;

import static com.jogamp.opengl.GL.GL_LINEAR;
import static com.jogamp.opengl.GL.GL_TEXTURE_2D;
import static com.jogamp.opengl.GL.GL_TEXTURE_MAG_FILTER;
import static com.jogamp.opengl.GL.GL_TEXTURE_MIN_FILTER;

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
import com.harium.propan.loader.TextureLoader;

import com.jogamp.opengl.util.texture.Texture;

public class GridMenuApplication extends ApplicationGL {

	private Texture marker;
	private Texture active;
	
	private Camera camera;
	
	private int markerCount = 6;
	private double tileSize = 5;
	
	protected float mx = 0;
	protected float my = 0;

	protected boolean click = false;
	
	private List<Boolean> activeMarkers = new ArrayList<>(markerCount);

	public GridMenuApplication(int w, int h) {
		super(w, h);
	}

	@Override
	public void init(Graphics3D graphics) {
		for (int i = 0; i < markerCount; i++) {
			activeMarkers.add(false);
		}
		
		BufferedImage markImage = ImageLoader.getInstance().getImage("mark.png");
		BufferedImage activeMarkImage = ImageLoader.getInstance().getImage("active_mark.png");
		marker = TextureLoader.getInstance().loadTexture(markImage);
		active = TextureLoader.getInstance().loadTexture(activeMarkImage);
		
		activeMarkers.add(3, true);
	}
	
	@Override
	public void load() {
		camera = new Camera(0, 10f, 0.001f);
		
		loading = 100;
	}
	
	protected void lookCamera(Graphics3D graphics) {
		AWTGraphics3D g = (AWTGraphics3D) graphics;
		GL2 gl = g.getGL2();
		GLU glu = g.getGLU();
		
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();

		double targetx = 0;
		double targety = 0;
		double targetz = 0;
		
		glu.gluLookAt( camera.position.x, camera.position.y, camera.position.z, targetx, targety, targetz, 0, 1, 0 );
	}
	
	protected void drawFloor(GL2 gl) {
		gl.glColor3d(1,1,1);

		gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

		drawGrid(gl,200,120);
	}

	private void drawGrid(GL2 gl, double x, double y) {
		
		double spacing = 0.1;
		Texture texture = marker;
		
		int count = 0;
		for(int i=0;i<3;i++) {
			if (!activeMarkers.get(count)) {
				texture = marker;
			} else {
				texture = active;
			}
			
			//Upper Row
			drawTile(gl, -1.5+i+(i*spacing), -1, tileSize, texture);
			count++;
		}
		
		for(int i=0;i<3;i++) {
			if (!activeMarkers.get(count)) {
				texture = marker;
			} else {
				texture = active;
			}
			
			//Lower Row
			drawTile(gl, -1.5+i+(i*spacing), spacing, tileSize, texture);
			count++;
		}
	}

	private void drawTile(GL2 gl, double x, double y, double tileSize, Texture texture) {

		texture.enable(gl);
		texture.bind(gl);
		
		gl.glBegin(GL2.GL_QUADS);

		//(0,0)
		gl.glTexCoord2d(0, 0);
		gl.glVertex3d(x*tileSize, 0, y*tileSize);

		//(1,0)
		gl.glTexCoord2d(1, 0);
		gl.glVertex3d(x*tileSize+tileSize, 0, y*tileSize);

		//(1,1)
		gl.glTexCoord2d(1, 1);
		gl.glVertex3d(x*tileSize+tileSize, 0, y*tileSize+tileSize);

		//(0,1)
		gl.glTexCoord2d(0, 1);
		gl.glVertex3d(x*tileSize, 0, y*tileSize+tileSize);

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
		
		g.setColor(Color.YELLOW);
		g.fillRect(10, 10, w, 60);
	}
	
	@Override
	public void display(Graphics3D graphics) {
		AWTGraphics3D g = (AWTGraphics3D) graphics;
		GL2 gl = g.getGL().getGL2();
		
		//Transform by Camera
		g.updateCamera(camera);

		//Draw Scene
		drawFloor(gl);

		gl.glFlush();
	}
	
}