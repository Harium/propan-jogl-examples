package examples.logo;

import com.harium.propan.core.context.ApplicationGL;
import com.harium.propan.core.graphics.AWTGraphics3D;
import com.harium.propan.core.graphics.Graphics3D;
import com.harium.propan.core.loader.MeshLoader;
import com.harium.propan.core.model.Model;
import com.harium.propan.graphics.ModelInstance;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

public class LogoShowApplication extends ApplicationGL {

    private double a = 0;
    private ModelInstance logo;

    public LogoShowApplication(int width, int height) {
        super(width, height);
    }

    @Override
    public void init(Graphics3D graphics) {
        //Init 3d Stuff
        AWTGraphics3D g = (AWTGraphics3D) graphics;
        GL2 gl = g.getGL2(); // get the OpenGL graphics context

        gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f); // set background (clear) color
        gl.glClearDepth(1.0f);      // set clear depth value to farthest
        gl.glEnable(GL.GL_DEPTH_TEST); // enables depth testing
        gl.glDepthFunc(GL.GL_LEQUAL);  // the type of depth test to do
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST); // best perspective correction
        gl.glShadeModel(GL2.GL_SMOOTH); // blends colors nicely, and smoothes out lighting

        //Load logo model
        Model logoVBO = MeshLoader.getInstance().loadModel("logo/logo.obj");
        logo = new ModelInstance(logoVBO);

        loading = 100;
    }

    @Override
    public void load() {
        loading = 50;
    }

    @Override
    public void display(Graphics3D graphics) {
        AWTGraphics3D g = (AWTGraphics3D) graphics;
        GL2 gl = g.getGL2();  // get the OpenGL 2 graphics context
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT); // clear color and depth buffers
        gl.glLoadIdentity();  // reset the model-view matrix

        gl.glTranslatef(-20.0f, -20.0f, -80.0f); // translate into the screen

        // Rotate Logo
        a += 0.5;
        gl.glRotated(a, 0, 1, 0);

        gl.glScaled(5, 5, 5);

        logo.diffuseRender(gl);
    }

    @Override
    public void reshape(Graphics3D graphics, int x, int y, int width, int height) {
        AWTGraphics3D g = (AWTGraphics3D) graphics;
        GL2 gl = g.getGL2(); // get the OpenGL graphics context
        GLU glu = g.getGLU();

        if (height == 0) height = 1;   // prevent divide by zero
        float aspect = (float) width / height;

        // Set the view port (display area) to cover the entire window
        gl.glViewport(0, 0, width, height);

        // Setup perspective projection, with aspect ratio matches viewport
        gl.glMatrixMode(GL2.GL_PROJECTION);  // choose projection matrix
        gl.glLoadIdentity();             // reset projection matrix

        glu.gluPerspective(45.0, aspect, 0.1, 1000.0); // fovy, aspect, zNear, zFar

        // Enable the model-view transform
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity(); // reset
    }

}
