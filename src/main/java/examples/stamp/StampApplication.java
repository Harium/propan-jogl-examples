package examples.stamp;


import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.harium.etyl.commons.event.KeyEvent;
import com.harium.etyl.commons.event.MouseEvent;
import com.harium.etyl.commons.event.PointerEvent;
import com.harium.etyl.commons.graphics.Color;
import com.harium.etyl.core.graphics.Graphics;
import com.harium.propan.core.context.ApplicationGL;
import com.harium.propan.core.graphics.AWTGraphics3D;
import com.harium.propan.core.graphics.Graphics3D;
import com.harium.propan.core.loader.MeshLoader;
import com.harium.propan.core.model.Model;
import com.harium.propan.core.view.FlyView;
import com.harium.propan.graphics.ModelInstance;
import com.harium.propan.loader.TextureLoader;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.texture.Texture;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.jogamp.opengl.GL.*;

public class StampApplication extends ApplicationGL {

    private ModelInstance stone;
    private ModelInstance tree;
    private Texture floor;
    private List<ModelInstance> stones = new ArrayList<ModelInstance>();

    protected int mx = 0;
    protected int my = 0;

    protected boolean click = false;

    private FlyView view;

    private static final int NONE = -1;

    boolean drawRay = false;
    double tileSize = 1;
    int colide = NONE;
    int selected = NONE;

    private Model stoneMesh;

    Set<Vector3> bsp = new HashSet<Vector3>();
    private Vector3 position;

    public StampApplication(int w, int h) {
        super(w, h);
    }

    @Override
    public void init(Graphics3D graphics) {
        view = new FlyView(30, 1.6f, 0);
        view.getAim().setAngleY(180);
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

        stoneMesh = MeshLoader.getInstance().loadModel("stone/stone.obj");

        stone = new ModelInstance(stoneMesh);
        stone.setColor(Color.WHITE);
        stone.offsetX(35);
        stone.offsetY(0.5f);
        stone.offsetZ(7);
        stone.setScale(0.3f);

        tree = new ModelInstance(MeshLoader.getInstance().loadModel("bamboo/bamboo.obj"));
        tree.offsetX(30);
        tree.offsetY(0.5f);
        tree.offsetZ(7);
        tree.setColor(Color.WHITE);
        tree.setScale(1.5f);

        floor = TextureLoader.getInstance().loadTexture("mark.png");
    }

    protected void drawFloor(GL2 gl) {

        gl.glColor3d(1, 1, 1);

        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        drawGrid(gl, 200, 120);
    }

    protected void drawWall(GL2 gl) {

        gl.glColor3d(1, 1, 1);

        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        drawGrid(gl, 200, 120);
    }

    private void drawGrid(GL2 gl, double x, double y) {

        floor.enable(gl);
        floor.bind(gl);

        for (int j = 0; j < y; j++) {
            for (int i = 0; i < x; i++) {
                drawTile(gl, i, j, tileSize);
            }
        }

        floor.disable(gl);
    }

    private void drawTile(GL2 gl, double x, double y, double tileSize) {

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
    }

    private void drawAxis(GL2 gl) {

        double axisSize = 100;

        //Draw Axis
        gl.glLineWidth(2.5f);

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

    private void drawRay(GL2 gl, Ray ray) {

        float axisSize = 50;

        Vector3 v = new Vector3(ray.direction);
        v.scl(axisSize);

        //Draw Camera Axis
        if (colide >= 0) {
            gl.glColor3d(1.0, 0.0, 0.0);
        } else {
            gl.glColor3d(0.0, 0.0, 1.0);
        }

        gl.glBegin(GL.GL_LINES);
        gl.glVertex3d(view.getX(), 1, view.getZ());
        gl.glVertex3d(view.getX() + v.x, view.getY() + v.y, view.getZ() + v.z);
        gl.glEnd();
    }

    @Override
    public void reshape(Graphics3D graphics, int x, int y, int w, int h) {
        AWTGraphics3D g = (AWTGraphics3D) graphics;
        GL2 gl = g.getGL2();
        GLU glu = g.getGLU();

        gl.glViewport(x, y, w, h);

        gl.glMatrixMode(GL2.GL_PROJECTION);

        gl.glLoadIdentity();

        glu.gluPerspective(60.0, (double) w / (double) h, 0.1, 500.0);

        gl.glMatrixMode(GL2.GL_MODELVIEW);

        gl.glLoadIdentity();
    }

    @Override
    public void updateKeyboard(KeyEvent event) {
        view.updateKeyboard(event);

        if (event.isKeyDown(KeyEvent.VK_R)) {
            drawRay = true;
        } else if (event.isKeyUp(KeyEvent.VK_R)) {
            drawRay = false;
        }
    }

    public void updateMouse(PointerEvent event) {

        mx = event.getX();
        my = event.getY();

        if (event.isButtonDown(MouseEvent.MOUSE_BUTTON_LEFT)) {
            click = true;
        } else if (event.isButtonUp(MouseEvent.MOUSE_BUTTON_LEFT)) {
            click = false;
        }

        if (event.isButtonDown(MouseEvent.MOUSE_BUTTON_RIGHT)) {
            drawRay = true;
        } else if (event.isButtonUp(MouseEvent.MOUSE_BUTTON_RIGHT)) {
            drawRay = false;
        }
    }

    @Override
    public void display(Graphics3D graphics) {
        view.update(0);
        AWTGraphics3D g = (AWTGraphics3D) graphics;
        GL2 gl = g.getGL().getGL2();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glClearColor(1f, 1f, 1f, 1);

        //Transform by Aim
        g.aimCamera(view.getAim());

        //Draw Scene
        drawAxis(gl);
        drawFloor(gl);

        position = g.get3DPointerFromMouse(mx, my);

        int currentX = (int) (position.x / tileSize);
        int currentZ = (int) (position.z / tileSize);

        if (click) {
            if (!bsp.contains(position)) {
                //Add stone
                ModelInstance mesh = new ModelInstance(stoneMesh);
                mesh.setColor(Color.WHITE);
                mesh.offsetX(currentX + 1.4f);
                mesh.offsetY(0.5f);
                mesh.offsetZ(currentZ + 0.5f);
                mesh.setScale(0.3f);

                stones.add(mesh);
                bsp.add(position);
            }
        }

        if (drawRay) {
            drawRay(gl, g.getCameraRay(mx + 32, my));
            g.setColor(Color.PINK);
        } else {
            g.setColor(Color.GREEN);
        }
        drawTile(gl, currentX, currentZ, tileSize);

        //Draw Models
        //Start batch
        gl.glEnable(GL.GL_DEPTH_TEST);

        gl.glPushMatrix();
        stone.renderTextured(gl);
        gl.glPopMatrix();

        for (ModelInstance mesh : stones) {
            gl.glPushMatrix();
            mesh.renderTextured(gl);
            gl.glPopMatrix();
        }

        gl.glPushMatrix();
        gl.glDisable(GL.GL_CULL_FACE);
        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

        tree.renderTextured(gl);
        gl.glPopMatrix();

        //End batch
        gl.glDisable(GL.GL_DEPTH_TEST);

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

}