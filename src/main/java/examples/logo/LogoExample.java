package examples.logo;

import com.harium.propan.Propan;
import com.harium.propan.core.context.ApplicationGL;
import examples.simple.ConeDrawing;

public class LogoExample extends Propan {

    public LogoExample() {
        super(1024, 576);
    }

    // Main program
    public static void main(String[] args) {
        LogoExample engine = new LogoExample();
        engine.setTitle("Logo Examples");
        engine.init();
    }

    @Override
    public ApplicationGL startApplication() {
        initialSetup("");
        return new LogoApplication(w, h);
        //return new LogoShowApplication(w, h);
    }

}
