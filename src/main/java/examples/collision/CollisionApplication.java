package examples.collision;

import com.harium.propan.core.context.ApplicationGL;
import com.harium.propan.core.graphics.AWTGraphics3D;
import com.harium.propan.loader.TextureLoader;
import com.harium.etyl.commons.event.MouseEvent;
import com.harium.etyl.commons.event.PointerEvent;
import com.harium.etyl.loader.image.ImageLoader;
import com.harium.propan.core.graphics.Graphics3D;
import com.harium.propan.linear.Camera;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import examples.simple.StandardExample;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static com.jogamp.opengl.GL.*;

public class CollisionApplication extends ApplicationGL {

    private Camera camera;

    private Texture background;
    private Texture marker;
    private Texture active;

    private int markerCount = 5;
    private float tileSize = 5.5f;

    protected float mx = 0;
    protected float my = 0;

    protected boolean click = false;

    int area = 0;

    private List<Boolean> activeMarkers = new ArrayList<>(markerCount);

    public CollisionApplication(int w, int h) {
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

        background = TextureLoader.getInstance().loadTexture(ImageLoader.getInstance().getImage("background.jpg"));

        activate(3);
    }

    private void activate(int index) {
        activeMarkers.set(index, true);
    }

    private void unactivate(int index) {
        activeMarkers.set(index, false);
    }

    private void toggle(int index) {
        activeMarkers.set(index, !activeMarkers.get(index));
    }

    @Override
    public void load() {
        camera = new Camera(0, 15f, 0.001f);

        loading = 100;
    }

    protected void drawFloor(GL2 gl) {
        gl.glColor3d(1, 1, 1);

        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        drawBackground(gl);

        drawGrid(gl, 200, 120);
    }

    private void drawBackground(GL2 gl) {
        background.enable(gl);
        background.bind(gl);

        float scale = 2.2f;
        float tileSizeW = 16 * scale;
        float tileSizeH = 9 * scale;

        float x = -0.5f;
        float y = -0.5f;

        gl.glBegin(GL2.GL_QUADS);

        //(0,0)
        gl.glTexCoord2d(0, 0);
        gl.glVertex3d(x * tileSizeW, 0, y * tileSizeH);

        //(1,0)
        gl.glTexCoord2d(1, 0);
        gl.glVertex3d(x * tileSizeW + tileSizeW, 0, y * tileSizeH);

        //(1,1)
        gl.glTexCoord2d(1, 1);
        gl.glVertex3d(x * tileSizeW + tileSizeW, 0, y * tileSizeH + tileSizeH);

        //(0,1)
        gl.glTexCoord2d(0, 1);
        gl.glVertex3d(x * tileSizeW, 0, y * tileSizeH + tileSizeH);

        gl.glEnd();

        background.disable(gl);
    }

    private void drawGrid(GL2 gl, double x, double y) {

        double spacing = 0.1;
        Texture texture = marker;

        int count = 0;
        for (int i = 0; i < markerCount; i++) {
            if (!activeMarkers.get(count)) {
                texture = marker;
            } else {
                texture = active;
            }

            float size = tileSize;

            if (count == area) {
                size = tileSize * 1.05f;
            }

            drawTile(gl, -3 + i + (i * spacing), -.5, size, texture);
            count++;
        }
    }

    private void drawTile(GL2 gl, double x, double y, double tileSize, Texture texture) {

        texture.enable(gl);
        texture.bind(gl);

        gl.glBegin(GL2.GL_QUADS);

        //(0,0)
        gl.glTexCoord2d(0, 0);
        gl.glVertex3d(x * tileSize, 0, y * tileSize);

        //(1,0)
        gl.glTexCoord2d(1, 0);
        gl.glVertex3d(x * tileSize + tileSize, 0, y * tileSize);

        //(1,1)
        gl.glTexCoord2d(1, 1);
        gl.glVertex3d(x * tileSize + tileSize, 0, y * tileSize + tileSize);

        //(0,1)
        gl.glTexCoord2d(0, 1);
        gl.glVertex3d(x * tileSize, 0, y * tileSize + tileSize);

        gl.glEnd();

        texture.disable(gl);
    }

    @Override
    public void reshape(Graphics3D graphics, int x, int y, int width, int height) {
        StandardExample.standardScene(graphics, x, y, width, height);
    }

    @Override
    public void updateMouse(PointerEvent event) {

        if (event.getX() < w) {
            area = event.getX() / (w / markerCount);
        }

        if (event.isButtonDown(MouseEvent.MOUSE_BUTTON_LEFT)) {
            toggle(area);
        }
    }

    @Override
    public void preDisplay(Graphics3D graphics) {
        AWTGraphics3D g = (AWTGraphics3D) graphics;
        GL2 gl = g.getDrawable().getGL().getGL2();

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