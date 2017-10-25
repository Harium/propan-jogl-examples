package examples.stamp;

import com.harium.propan.Propan;
import com.harium.propan.core.context.ApplicationGL;

public class StampExample extends Propan {

    public StampExample() {
        super(1024, 576);
    }

    // Main program
    public static void main(String[] args) {
        StampExample engine = new StampExample();
        engine.setTitle("Stamp Example");
        engine.init();
    }

    @Override
    public ApplicationGL startApplication() {
        initialSetup("");

        return new StampApplication(w, h);
    }

}
