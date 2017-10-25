package examples.collision;

import com.harium.propan.core.context.ApplicationGL;
import com.harium.propan.core.graphics.AWTGraphics3D;
import com.harium.propan.graphics.Cone;
import com.harium.propan.graphics.Cylinder;
import com.badlogic.gdx.math.collision.Ray;
import com.harium.etyl.commons.event.KeyEvent;
import com.harium.etyl.commons.event.PointerEvent;
import com.harium.etyl.commons.graphics.Color;
import com.harium.propan.core.graphics.Graphics3D;
import com.harium.propan.core.view.FlyView;
import com.harium.propan.math.Intersector;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import examples.simple.StandardExample;

public class ConeCollision extends ApplicationGL {

    protected int mx = 0;
    protected int my = 0;

    protected boolean click = false;

    protected FlyView view;

    protected static final int NONE = -1;

    protected boolean drawRay = true;
    protected double tileSize = 1;
    protected int colide = NONE;
    protected int selected = NONE;

    private Cone cone;
    private Cylinder cylinder;

    public ConeCollision(int w, int h) {
        super(w, h);
    }

    @Override
    public void init(Graphics3D graphics) {
        AWTGraphics3D g = (AWTGraphics3D) graphics;
        view = new FlyView(0, 3.6f, -10);
        view.getAim().setAngleY(180);

        cone = new Cone(6, 6, 3);
        cylinder = new Cylinder(6, 6, 3);
        cylinder.transform.translate(8, 0, 0);

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
        StandardExample.standardScene(graphics, x, y, w, h);
    }

    public void updateMouse(PointerEvent event) {
        mx = event.getX();
        my = event.getY();
    }

    @Override
    public void updateKeyboard(KeyEvent event) {
        view.updateKeyboard(event);
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
        StandardExample.drawAxis(gl, 100);

        //Draw Grid
        g.setColor(Color.BLACK);
        g.drawGrid(1, 150, 150);


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

        g.setColor(Color.BLUE);
        cone.draw(g);

        if (Intersector.intersectRayCylinderFast(ray, cylinder)) {
            g.setColor(Color.YELLOW);
        } else {
            g.setColor(Color.GREEN);
        }
        cylinder.draw(g);

        gl.glFlush();
    }

}
