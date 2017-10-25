package examples.simple;

import com.harium.propan.Propan;
import com.harium.propan.core.context.ApplicationGL;

public class SimpleExamples extends Propan {

    public SimpleExamples() {
        //super(640,480);
        super(1024, 576);
    }

    // Main program
    public static void main(String[] args) {
        SimpleExamples engine = new SimpleExamples();
        engine.setTitle("Propan Examples");
        engine.setIcon("cross_blue.png");
        engine.init();
    }

    @Override
    public ApplicationGL startApplication() {

        initialSetup("");

        return new ConeDrawing(w, h);
    }

}
