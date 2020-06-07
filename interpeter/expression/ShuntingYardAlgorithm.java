package model.interpeter.expression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class ShuntingYardAlgorithm {
	// Returns true if the received string's value is double.
	public static boolean isDouble(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch(NumberFormatException e) { return false; }
	}
	
	// Returns Variable from the Interpeter's symbolTable, if exist.
	private static Variable getVariable(String name, HashMap<String, Variable> symbolTable) { return symbolTable.get(name); }
	
	// Remove operators from the received array.
	private static void removeOperators(ArrayList<String> split) {
		for(int i = 0; i < split.size(); i++) {
			if(Arrays.asList("+", "-").contains(split.get(i))) {
				int cnt = 1; 
				int startIndex = i;
				boolean flag = !split.get(i++).equals("-"); // True if equals to +.
				
				while(Arrays.asList("+","-").contains(split.get(i))) {
					if(flag && split.get(i).equals("-")) { flag = false; } //+- -+
					else if(!flag && split.get(i).equals("-")) { flag = true; } //--
					cnt++; 
					i++;
				}
				
				for(int j = 0; j < cnt; j++) {
					split.remove(startIndex);
				}
				
				if(startIndex == 0 || split.get(startIndex - 1).equals("(") || Arrays.asList("*","/").contains(split.get(startIndex - 1))) {
					if(!flag) split.add(startIndex, "-");
				}
				else split.add(startIndex, flag ? "+" : "-");
			}
		}
	}
	
	// Checks & deals with minuses.  
	private static void checkMinuses(List<String> split) { 
		for(int i = 0; i < split.size(); i++) {
			if(split.get(i).equals("-")) {
				// Checks if (-) is in the beginning.
				if(i == 0) { 
					split.remove(i);
					split.set(0, "-" + split.get(0));
				}
				else if(Arrays.asList("*","/","(").contains(split.get(i - 1))) {
					split.remove(i);
					split.set(i, "-" + split.get(i));
				}
			}
		}
	}
	
	// Replaces Variables with Variables's double value.
	private static void replaceVariables(List<String> list, HashMap<String, Variable> symbolTable) {
		Variable v;
		for (int i = 0; i < list.size(); i++) {
			if((v = getVariable(list.get(i), symbolTable)) != null) {
				if(v.calculate() >= 0) 
					list.set(i, String.valueOf(v.calculate()));
				else {
					list.add(i++, "-");
					list.set(i, String.valueOf(-v.calculate()));
				}
			}
		}
	}
	
	// Creates from in-fix string expression into post-fix string expression.
	private static String infixToPostfix(List<String> split, HashMap<String, Variable> symbolTable) {
		
		Stack<String> stack = new Stack<>();
		Queue<String> queue = new LinkedList<>();
		ArrayList<String> splitStr = new ArrayList<>(split);
		
		replaceVariables(splitStr, symbolTable); 
		removeOperators(splitStr); 
		checkMinuses(splitStr);
		
		for(int i = 0; i < splitStr.size(); i++) {
			if(isDouble(splitStr.get(i))) {
				queue.add(splitStr.get(i));
			}
			else {
				switch(splitStr.get(i)) {
				case "/":
				case "*":
					while(!stack.isEmpty() && (!stack.peek().equals("(")) && (!stack.peek().equals("+") && (!stack.peek().equals("-")))) {
						queue.add(stack.pop());
					}
					stack.push(splitStr.get(i));
					break;
				case "+":
				case "-":
					while(!stack.isEmpty() && (!stack.peek().equals("("))) {
						queue.add(stack.pop());
					}
					stack.push(splitStr.get(i));
					break;
				case "(":
					stack.push("(");
					break;
				case ")":
					while(!stack.isEmpty() && (!stack.peek().equals("("))) {
						queue.add(stack.pop());
					}
					if(!stack.isEmpty()) stack.pop();
					break;
				}
			}
		}
		
		while(!stack.isEmpty()) {
			queue.add(stack.pop());
		}
		
		StringBuilder sb = new StringBuilder();
		for(String str : queue) { sb.append(str).append(","); }
		return sb.toString();
	}
	
	// Calculate post-fix string expression.
		private static double calculatePostfix(String postfix) {
			Stack<Expression> stackExp = new Stack<>();
			String[] expressions = postfix.split(",");
			
			for(String str : expressions) {
				if(isDouble(str)) {
					stackExp.push(new Number(Double.parseDouble(str)));
				}
				else {
					Expression right = stackExp.pop();
					Expression left = stackExp.pop();
					
					switch(str) {
					case "+": stackExp.add(new Plus(left, right)); break;
					case "-": stackExp.add(new Minus(left, right)); break;
					case "*": stackExp.add(new Mul(left, right)); break;
					case "/": stackExp.add(new Div(left, right)); break;
					}
				}
			}
			return Math.floor(stackExp.pop().calculate() * 1000) / 1000;
		}
	
	// Executes the Shunting-Yard algorithm.
	public static double execute(List<String> expression, HashMap<String, Variable> symbolTable) {
		String postFixExpression = infixToPostfix(expression, symbolTable);
		return(calculatePostfix(postFixExpression));
	}
}