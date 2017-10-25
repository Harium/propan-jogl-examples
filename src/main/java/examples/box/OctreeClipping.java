package examples.box;

import com.harium.etyl.commons.graphics.Color;
import com.harium.propan.core.graphics.Graphics3D;
import com.harium.propan.core.loader.MeshLoader;
import com.harium.propan.core.model.Face;
import com.harium.propan.core.model.Model;
import com.harium.propan.linear.BoundingBox3D;
import com.harium.propan.storage.octree.Octree;
import com.harium.propan.storage.octree.OctreeNode;
import com.harium.propan.storage.octree.VolumeOctree;
import com.harium.etyl.commons.event.KeyEvent;
import com.harium.etyl.core.graphics.Graphics;
import com.harium.etyl.linear.Point3D;
import com.harium.propan.core.context.ApplicationGL;
import com.harium.propan.core.graphics.AWTGraphics3D;
import com.harium.propan.graphics.ModelInstance;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;

import java.util.Set;

public class OctreeClipping extends ApplicationGL {

    private Octree<Face> octree;

    private Model bunnyVBO;
    private ModelInstance bunny;

    private double angleY = 0;

    private boolean rotate = true;

    private BoundingBox3D clippingBox;
    private Set<Face> intersectedFaces;

    public OctreeClipping(int width, int height) {
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

        //Load bunny model
        bunnyVBO = MeshLoader.getInstance().loadModel("bunny.obj");
        bunny = new ModelInstance(bunnyVBO);
        bunny.setColor(Color.GHOST_WHITE);

        octree = new VolumeOctree<Face>(bunnyVBO.getBoundingBox());

        for (Face face : bunnyVBO.getFaces()) {
            Point3D centroid = bunnyVBO.centroid(face);
            octree.add(centroid, face);
        }

        clippingBox = new BoundingBox3D(bunnyVBO.getBoundingBox());
        clippingBox.getMinPoint().offsetX(-0.1);
        clippingBox.getMaxPoint().offsetX(-0.1);

        intersectedFaces = octree.getData(clippingBox);
        System.out.println("Size: " + bunnyVBO.getFaces().size());
        System.out.println("Size: " + intersectedFaces.size());

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

        gl.glTranslatef(0.0f, 0.0f, -15.0f); // translate into the screen
        gl.glScaled(15, 15, 15);
        gl.glRotated(angleY, 0, 1, 0);

        //Draw Bounding Box
        gl.glColor3f(1, 0, 0);
        g.drawBoundingBox(clippingBox);

        //Draw Bunny Model
        bunny.draw(gl, intersectedFaces);

        gl.glColor3f(0, 1, 1);
        renderOctree(g, octree);

        //Rotate Model
        if (rotate) {
            angleY += 1;
        }
    }

    public void renderOctree(Graphics3D graphics, Octree<?> tree) {

        OctreeNode<?> root = tree.getRoot();

        renderOctreeNode(graphics, root);
    }

    public void renderOctreeNode(Graphics3D graphics, OctreeNode<?> node) {
        AWTGraphics3D g = (AWTGraphics3D) graphics;
        g.drawBoundingBox(node.getBox());

        for (OctreeNode<?> child : node.getChildrenNodes()) {
            renderOctreeNode(g, child);
        }
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

    public void updateKeyboard(KeyEvent event) {
        if (event.isKeyDown(KeyEvent.VK_SPACE)) {
            rotate = !rotate;
        } else if (event.isKeyUp(KeyEvent.VK_SPACE)) {
            rotate = !rotate;
        }
    }

    @Override
    public void draw(Graphics g) {

    }

}