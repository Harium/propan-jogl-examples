package examples.collision;


import com.harium.propan.core.context.ApplicationGL;
import com.harium.propan.core.graphics.AWTGraphics3D;
import com.badlogic.gdx.math.collision.Ray;
import com.harium.etyl.commons.event.KeyEvent;
import com.harium.etyl.commons.event.MouseEvent;
import com.harium.etyl.commons.event.PointerEvent;
import com.harium.etyl.commons.graphics.Color;
import com.harium.propan.core.graphics.Graphics3D;
import com.harium.propan.core.view.FlyView;
import com.harium.propan.linear.OrientedBoundingBox;
import com.harium.propan.math.Intersector;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import examples.simple.StandardExample;

import java.util.ArrayList;
import java.util.List;

public class OrientedBoxCollision extends ApplicationGL {

    protected int mx = 0;
    protected int my = 0;

    protected boolean click = false;

    protected FlyView view;

    protected static final int NONE = -1;

    protected boolean drawRay = false;
    protected double tileSize = 1;
    protected int colide = NONE;
    protected int selected = NONE;

    private List<OrientedBoundingBox> cubes;

    public OrientedBoxCollision(int w, int h) {
        super(w, h);
    }

    @Override
    public void init(Graphics3D graphics) {
        AWTGraphics3D g = (AWTGraphics3D) graphics;
        view = new FlyView(30, 1.6f, 0);
        view.getAim().setAngleY(160);

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

        cubes = new ArrayList<OrientedBoundingBox>();
        cubes.add(new OrientedBoundingBox(1).translate(20, 1, 7));
        cubes.add(new OrientedBoundingBox(1).translate(22, 1, 7));
        cubes.add(new OrientedBoundingBox(1).translate(24, 1, 7));
        cubes.add(new OrientedBoundingBox(1).translate(26, 1, 7));
        cubes.add(new OrientedBoundingBox(1).translate(28, 1, 7));
        cubes.add(new OrientedBoundingBox(1).translate(30, 1, 7));
        cubes.add(new OrientedBoundingBox(1).translate(32, 1, 7));
    }

    @Override
    public void reshape(Graphics3D g, int x, int y, int width, int height) {
        StandardExample.standardScene(g, x, y, width, height);
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
        AWTGraphics3D g = (AWTGraphics3D) graphics;
        updateControls(0);

        GL2 gl = g.getGL().getGL2();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        gl.glClearColor(1f, 1f, 1f, 1);

        //Transform by Aim
        g.aimCamera(view.getAim());

        //Draw Scene
        //StandardExample.drawAxis(gl, 100);

        Ray ray = g.getCameraRay(mx, my);

        if (colide >= 0) {
            gl.glColor3d(1.0, 0.0, 0.0);
        } else {
            gl.glColor3d(0.0, 0.0, 1.0);
        }

        if (drawRay) {
            float raySize = 500;
            StandardExample.drawRay(gl, ray, view, raySize);
        }

        colide = NONE;

        for (int i = 0; i < cubes.size(); i++) {

            OrientedBoundingBox cube = cubes.get(i);

            if (Intersector.intersectRayBounds(ray, cube) > 0) {
                colide = i;
                if (!click) {
                    g.setColor(Color.YELLOW);
                } else {
                    selected = i;
                    g.setColor(Color.BLUE);
                }

            } else {
                g.setColor(Color.GREEN);
            }

            if (selected == i) {
                g.setColor(Color.BLUE);
            }

            gl.glPushMatrix();
            gl.glMultMatrixf(cube.transform.val, 0);
            g.drawBoundingBox(cube);
            gl.glPopMatrix();
        }


        gl.glFlush();

    }

    public void updateControls(long now) {
        //stone.rotateY(10);
        cubes.get(6).rotateY(0.5f);
        //stone.transform.translate(10, 0, 0);

        view.update(now);
    }

}