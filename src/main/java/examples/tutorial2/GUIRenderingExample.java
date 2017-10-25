package examples.tutorial2;

import com.harium.etyl.commons.event.Action;
import com.harium.etyl.commons.event.GUIEvent;
import com.harium.etyl.commons.graphics.Color;
import com.harium.etyl.core.graphics.Graphics;
import com.harium.etyl.ui.Button;
import com.harium.etyl.ui.label.TextLabel;
import com.harium.etyl.ui.UI;
import com.harium.propan.core.context.ApplicationGL;
import com.harium.propan.core.graphics.Graphics3D;

public class GUIRenderingExample extends ApplicationGL {

	public GUIRenderingExample(int width, int height) {
		super(width, height);
	}
	
	private Color color = Color.WHITE;
	
	@Override
	public void init(Graphics3D graphics) {
		//Init 3d Stuff		
	}

	@Override
	public void load() {
		
		//Load 2D and 3D stuff
		Button exit = new Button(w/2-200/2, h/2-60/2, 200, 60);
		exit.setLabel(new TextLabel("BUTTON"));
		exit.addAction(GUIEvent.MOUSE_LEFT_BUTTON_DOWN, new Action(this, "changeColor"));
		exit.addAction(GUIEvent.MOUSE_RIGHT_BUTTON_DOWN, new Action(this, "clearColor"));
		
		UI.add(exit);
				
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
		
	public void changeColor() {
		this.color = Color.BLACK;
	}
	
	public void clearColor() {
		this.color = Color.WHITE;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillRect(0, 0, w, h);
	}
	
}
