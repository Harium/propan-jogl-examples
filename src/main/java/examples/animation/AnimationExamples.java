package examples.animation;

import com.harium.propan.Propan;
import com.harium.propan.core.context.ApplicationGL;

public class AnimationExamples extends Propan {

    public AnimationExamples() {
        super(1024, 576);
    }

    // Main program
    public static void main(String[] args) {
        AnimationExamples engine = new AnimationExamples();
        engine.setTitle("Animation Example");
        engine.init();
    }

    @Override
    public ApplicationGL startApplication() {

        initialSetup("");

        return new AnimationApplication(w, h);
    }

}
