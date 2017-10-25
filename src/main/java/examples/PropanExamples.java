package examples;

import com.harium.propan.Propan;
import com.harium.propan.core.context.ApplicationGL;
import examples.tutorial1.OrthographicDrawingExample;

public class PropanExamples extends Propan {

	public PropanExamples() {
		//super(640,480);
		super(1024,576);
	}

	// Main program
	public static void main(String[] args) {
		PropanExamples engine = new PropanExamples();
		engine.setTitle("Propan Examples");
		engine.setIcon("cross_blue.png");
		engine.init();
	}
	
	@Override
	public ApplicationGL startApplication() {
		initialSetup("");

        return new OrthographicDrawingExample(w, h);
		//return new GUIRenderingExample(w, h);

		//return new OrientedBoxCollision(w, h);

		//return new BillboardExample(w, h);
		//return new RotateAroundExample(w, h);
		//return new Perspective(w, h);
		//return new Ortographic(w, h);
		//return new MarkerApplication(w, h);
		//return new GridPerspective(w, h);

		//return new RadialMarkerApplication(w, h);
		//return new SkyboxExample(w, h);
		
		//return new OctreeRender(w, h);
		//return new OctreeClipping(w, h);
		//return new FrustrumRender(w, h);
		
		//return new CollisionApplication(w, h);
		//return new MeshExample(w, h);		
		//return new LightExample(w, h);
		//return new StampApplication(w, h);
		
		//return new PositionAxis(w, h);
		//return new RotationAxis(w, h);
		//return new ConeDrawing(w, h);
		//return new ConeCollision(w, h);
	}

}
