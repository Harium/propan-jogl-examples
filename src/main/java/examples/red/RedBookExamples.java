package examples.red;

import com.harium.propan.Propan;
import com.harium.propan.core.context.ApplicationGL;

public class RedBookExamples extends Propan {

	public RedBookExamples() {
		super(1024,576);
	}

	// Main program
	public static void main(String[] args) {

		RedBookExamples examples = new RedBookExamples();		
		examples.init();
	}

	@Override
	public ApplicationGL startApplication() {

		initialSetup("../../");

		//return new Example1(w, h);
		return new Example2(w, h);
	}

}
