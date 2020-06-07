package model.test.milestone_4;

import model.interpeter.Interpeter;

public class MyInterpreter {

	public static int interpret(String[] lines){
		
		Interpeter interpeter = new Interpeter();
		return (int)interpeter.interpret(lines);
	}
}
