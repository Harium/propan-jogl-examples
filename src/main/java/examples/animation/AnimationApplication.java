package examples.animation;

import com.harium.propan.core.context.ApplicationGL;
import com.harium.propan.core.graphics.AWTGraphics3D;
import com.badlogic.gdx.math.Vector3;
import com.harium.etyl.commons.event.KeyEvent;
import com.harium.etyl.commons.graphics.Color;
import com.harium.etyl.core.graphics.Graphics;
import com.harium.propan.core.graphics.Graphics3D;
import com.harium.propan.core.loader.AnimationLoader;
import com.harium.propan.core.model.Joint;
import com.harium.propan.core.model.motion.Motion;
import com.harium.propan.core.view.FlyView;
import com.harium.propan.core.view.UEView;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

import java.io.File;
import java.util.List;

public class AnimationApplication extends ApplicationGL {

    protected FlyView view;
    private Motion motion;

    long startAnimation = 0;

    public AnimationApplication(int width, int height) {
        super(width, height);
    }

    @Override
    public void init(Graphics3D graphics) {
        AWTGraphics3D g = (AWTGraphics3D) graphics;
        GL2 gl = g.getGL2(); // get the OpenGL graphics context

        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // set background (clear) color
        gl.glClearDepth(1.0f);      // set clear depth value to farthest
        gl.glEnable(GL.GL_DEPTH_TEST); // enables depth testing
        gl.glDepthFunc(GL.GL_LEQUAL);  // the type of depth test to do
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST); // best perspective correction
        gl.glShadeModel(GL2.GL_SMOOTH); // blends colors nicely, and smoothes out lighting

        view = new UEView(-28, 8, 0);
        view.getAim().rotate(Vector3.Y, 90);

        //Load motion
        //String path = "02_01.bvh";
        String path = "anim.bvh";
        motion = AnimationLoader.getInstance().loadMotion(path);

        startAnimation = System.currentTimeMillis();
        loading = 100;
    }

    @Override
    public void load() {
        loading = 50;
    }

    @Override
    public void display(Graphics3D graphics) {
        AWTGraphics3D g = (AWTGraphics3D) graphics;
        view.update(0);

        GL2 gl = g.getGL2();  // get the OpenGL 2 graphics context
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT); // clear color and depth buffers
        gl.glClearColor(1f, 1f, 1f, 1);
        gl.glLoadIdentity();  // reset the model-view matrix

        //Transform by Aim
        g.aimCamera(view.getAim());

        g.setColor(Color.CYAN);

        updateAnimation();

        Joint root = motion.getArmature().getRoot();

        for (Joint js : root.getChildren()) {
            drawSkeleton(g, js);
        }
    }

    private void drawSkeleton(Graphics3D graphics, Joint root) {
        graphics.drawSphere(root.getPosition(), 0.1);
        for (Joint joint : root.getChildren()) {
            graphics.drawLine(root.getPosition(), joint.getPosition());
            drawSkeleton(graphics, joint);
        }
    }

    private void updateAnimation() {
        long now = System.currentTimeMillis();

        float FPS = 1 / motion.getFrameTime();
        int interval = (int) (1000 / FPS);

        int keyFrameIndex = (int) ((now - startAnimation) / interval);
        //keyFrameIndex /= 2;
        keyFrameIndex %= motion.getFrames();

        motion.animate(keyFrameIndex);
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

        glu.gluPerspective(45.0, aspect, 0.1, 100.0); // fovy, aspect, zNear, zFar

        // Enable the model-view transform
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity(); // reset
    }

    @Override
    public void draw(Graphics g) {

    }

    @Override
    public void updateKeyboard(KeyEvent event) {
        view.updateKeyboard(event);
    }

    @Override
    public void dropFiles(int x, int y, List<File> files) {
        String path = files.get(0).getAbsolutePath();
        motion = AnimationLoader.getInstance().loadMotion(path, true);
    }
}
