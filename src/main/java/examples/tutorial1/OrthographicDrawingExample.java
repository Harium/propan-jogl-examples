package examples.tutorial1;

import com.harium.etyl.commons.graphics.Color;
import com.harium.etyl.core.graphics.Graphics;
import com.harium.etyl.layer.ImageLayer;
import com.harium.propan.core.context.ApplicationGL;
import com.harium.propan.core.graphics.Graphics3D;

public class OrthographicDrawingExample extends ApplicationGL {

	public OrthographicDrawingExample(int width, int height) {
		super(width, height);
	}
	
	private ImageLayer layer;
	
	@Override
	public void init(Graphics3D graphics) {
		//Init 3d Stuff
	}

	@Override
	public void load() {
		layer = new ImageLayer("active_mark.png");
		
		loading = 100;
	}
	
	@Override
	public void display(Graphics3D graphics) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reshape(Graphics3D graphics, int x, int y, int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.BLUE);
		g.fillRect(0, 0, w, h);
		
		layer.simpleDraw(g);
	}
	
}
