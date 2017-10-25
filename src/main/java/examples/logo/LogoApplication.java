package examples.logo;

import com.badlogic.gdx.math.Vector3;
import com.harium.etyl.commons.event.KeyEvent;
import com.harium.etyl.commons.graphics.Color;
import com.harium.etyl.util.PathHelper;
import com.harium.propan.core.context.ApplicationGL;
import com.harium.propan.core.graphics.AWTGraphics3D;
import com.harium.propan.core.graphics.Graphics3D;
import com.harium.propan.core.material.OBJMaterial;
import com.harium.propan.core.model.Face;
import com.harium.propan.core.model.Group;
import com.harium.propan.core.model.Model;
import com.harium.propan.core.view.FlyView;
import com.harium.propan.core.writer.OBJWriter;
import com.harium.propan.graphics.ModelInstance;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import examples.simple.StandardExample;

import java.io.IOException;

public class LogoApplication extends ApplicationGL {
    protected int mx = 0;
    protected int my = 0;

    protected FlyView view;
    private ModelInstance logo;

    public LogoApplication(int w, int h) {
        super(w, h);
    }

    public void init(Graphics3D graphics) {
        view = new FlyView(0, 3.6f, 10);
        //view.getAim().setAngleY(180);

        Model model = new Model();

        // Left Part
        model.addVertex(new Vector3(3.0f, 0, 0));
        model.addVertex(new Vector3(0, 1.8f, 0));
        model.addVertex(new Vector3(0, 6.0f, 0));
        model.addVertex(new Vector3(3.0f, 7.8f, 0));

        model.addVertex(new Vector3(3.0f, 0, 1));
        model.addVertex(new Vector3(0, 1.8f, 1));
        model.addVertex(new Vector3(0, 6.0f, 1));
        model.addVertex(new Vector3(3.0f, 7.8f, 1));

        // Upper Right Part
        model.addVertex(new Vector3(4.2f, 0, 0));
        model.addVertex(new Vector3(7.2f, 1.8f, 0));
        model.addVertex(new Vector3(7.2f, 3.3f, 0));
        model.addVertex(new Vector3(4.2f, 3.3f, 0));

        model.addVertex(new Vector3(4.2f, 0, 1));
        model.addVertex(new Vector3(7.2f, 1.8f, 1));
        model.addVertex(new Vector3(7.2f, 3.3f, 1));
        model.addVertex(new Vector3(4.2f, 3.3f, 1));

        // Bottom Right Part
        model.addVertex(new Vector3(4.2f, 4.5f, 0));
        model.addVertex(new Vector3(7.2f, 4.5f, 0));
        model.addVertex(new Vector3(7.2f, 6.0f, 0));
        model.addVertex(new Vector3(4.2f, 7.8f, 0));

        model.addVertex(new Vector3(4.2f, 4.5f, 1));
        model.addVertex(new Vector3(7.2f, 4.5f, 1));
        model.addVertex(new Vector3(7.2f, 6.0f, 1));
        model.addVertex(new Vector3(4.2f, 7.8f, 1));

        for (Vector3 vector3 : model.getVertices()) {
            vector3.x = vector3.x - 3.9f;
        }

        Group left = new Group("left");
        Group right = new Group("right");

        // Left Side
        // Front
        left.getFaces().add(new Face(3).addVertexes(3, 1, 0));
        left.getFaces().add(new Face(3).addVertexes(3, 2, 1));
        // Back
        left.getFaces().add(new Face(3).addVertexes(4, 5, 7));
        left.getFaces().add(new Face(3).addVertexes(5, 6, 7));

        //Border
        left.getFaces().add(new Face(3).addVertexes(4, 0, 5));
        left.getFaces().add(new Face(3).addVertexes(0, 1, 5));
        left.getFaces().add(new Face(3).addVertexes(5, 1, 6));
        left.getFaces().add(new Face(3).addVertexes(1, 2, 6));
        left.getFaces().add(new Face(3).addVertexes(6, 2, 7));
        left.getFaces().add(new Face(3).addVertexes(2, 3, 7));
        left.getFaces().add(new Face(3).addVertexes(7, 3, 0));
        left.getFaces().add(new Face(3).addVertexes(0, 4, 7));

        // Right Side
        // Upper Front
        right.getFaces().add(new Face(3).addVertexes(8, 9, 11));
        right.getFaces().add(new Face(3).addVertexes(9, 10, 11));
        // Upper Back
        right.getFaces().add(new Face(3).addVertexes(15, 13, 12));
        right.getFaces().add(new Face(3).addVertexes(15, 14, 13));
        // Upper Border
        right.getFaces().add(new Face(3).addVertexes(13, 8, 12));
        right.getFaces().add(new Face(3).addVertexes(13, 9, 8));
        right.getFaces().add(new Face(3).addVertexes(14, 9, 13));
        right.getFaces().add(new Face(3).addVertexes(14, 10, 9));
        right.getFaces().add(new Face(3).addVertexes(15, 10, 14));
        right.getFaces().add(new Face(3).addVertexes(15, 11, 10));
        right.getFaces().add(new Face(3).addVertexes(8, 11, 15));
        right.getFaces().add(new Face(3).addVertexes(15, 12, 8));

        // Bottom Front
        right.getFaces().add(new Face(3).addVertexes(19, 17, 16));
        right.getFaces().add(new Face(3).addVertexes(19, 18, 17));
        // Bottom Back
        right.getFaces().add(new Face(3).addVertexes(20, 21, 23));
        right.getFaces().add(new Face(3).addVertexes(21, 22, 23));
        // Bottom Border
        right.getFaces().add(new Face(3).addVertexes(20, 16, 21));
        right.getFaces().add(new Face(3).addVertexes(16, 17, 21));
        right.getFaces().add(new Face(3).addVertexes(21, 17, 22));
        right.getFaces().add(new Face(3).addVertexes(17, 18, 22));
        right.getFaces().add(new Face(3).addVertexes(22, 18, 23));
        right.getFaces().add(new Face(3).addVertexes(18, 19, 23));
        right.getFaces().add(new Face(3).addVertexes(23, 19, 16));
        right.getFaces().add(new Face(3).addVertexes(16, 20, 23));

        model.getGroups().add(left);
        model.getGroups().add(right);

        OBJMaterial leftMaterial = new OBJMaterial();
        leftMaterial.setKd(new Vector3(0x0 / 255f, 0x71 / 255f, 0xbc / 255f));
        left.setMaterial(leftMaterial);

        OBJMaterial rightMaterial = new OBJMaterial();
        rightMaterial.setKd(new Vector3(0x29 / 255f, 0xab / 255f, 0xe2 / 255f));
        right.setMaterial(rightMaterial);

        try {
            new OBJWriter().writeVBO(model, PathHelper.currentDirectory() + "logo.obj");
        } catch (IOException e) {
            e.printStackTrace();
        }

        AWTGraphics3D g = (AWTGraphics3D) graphics;
        GL2 gl = g.getGL2(); // get the OpenGL graphics context

        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // set background (clear) color
        gl.glClearDepth(1.0f);      // set clear depth value to farthest
        gl.glEnable(GL.GL_DEPTH_TEST); // enables depth testing
        gl.glDepthFunc(GL.GL_LEQUAL);  // the type of depth test to do
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST); // best perspective correction
        gl.glShadeModel(GL2.GL_SMOOTH); // blends colors nicely, and smoothes out lighting

        logo = new ModelInstance(model);
    }

    public void reshape(Graphics3D graphics, int x, int y, int width, int height) {
        StandardExample.standardScene(graphics, x, y, width, height);
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
        g.setColor(Color.BLACK);
        g.drawGrid(1, 150, 150);

        logo.renderWireframe(gl);

        gl.glFlush();
    }
}
