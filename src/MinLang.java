import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

public class MinLang {
	
	static Stack<LinkedList<String>> back_stack = new Stack<LinkedList<String>>();
	static Stack<LinkedList<String>> front_stack = new Stack<LinkedList<String>>();
	static HashMap<String, String> variable_definitions = new HashMap<String, String>();
	static boolean end_app = false;
	
	public static void eval(LinkedList<String> cmd) {
		if(cmd.get(0).equals("def")) {
			if(cmd.size() != 4) {
				errorPrintOut(cmd, 1);
			}
			if(!cmd.get(1).matches("([a-z]|[A-Z])[^ \n\t]*")) {
				errorPrintOut(cmd, 2);
			}
			String tmp = cmd.get(3);
			if(variable_definitions.containsKey(tmp)) {
				variable_definitions.put(cmd.get(1), variable_definitions.get(tmp));
			} else {
				variable_definitions.put(cmd.get(1), tmp);
			}
			
		} else if(cmd.get(0).equals("step")) {
			if(cmd.size() != 2) {
				errorPrintOut(cmd, 1);
			}
			int num_of_steps = 0;
			String tmp = cmd.get(1);	
			if(variable_definitions.containsKey(tmp)) {
				tmp = variable_definitions.get(tmp);
			}
			if(tmp.matches("-?\\d+\\.?\\d*")) {
				if(tmp.matches("-?\\d+")) {
					num_of_steps = Integer.parseInt(tmp);
				} else {
					num_of_steps = (int) Double.parseDouble(tmp);
				}
				num_of_steps = Integer.parseInt(tmp);
				if(num_of_steps < 0) {
					if(Math.abs(num_of_steps) >= back_stack.size())
						errorPrintOut(cmd, 3);
					for(int i=1; i>num_of_steps; i--) {
						front_stack.push(back_stack.pop());
					}
				} else {
					if(num_of_steps > front_stack.size())
						errorPrintOut(cmd, 3);
					for(int i=1; i<num_of_steps; i++) {
						back_stack.push(front_stack.pop());
					}
				}
			} else {
				errorPrintOut(cmd, 4);
			}
			
		} else if(cmd.get(0).equals("cmp")) {
			if(cmd.size() != 4) {
				errorPrintOut(cmd, 1);
			}
			if(front_stack.size() < 3) {
				errorPrintOut(cmd, 6);
			}
			String lhs = "";
			String rhs = "";
			if(variable_definitions.containsKey(cmd.get(1)))
				lhs = variable_definitions.get(cmd.get(1));
			else
				lhs = cmd.get(1);
			if(variable_definitions.containsKey(cmd.get(3)))
				rhs = variable_definitions.get(cmd.get(3));
			else
				rhs = cmd.get(3);
			if(!lhs.equals(rhs))
				back_stack.push(front_stack.pop());
			
		} else if(cmd.get(0).equals("out")) {
			if(cmd.size() < 1) {
				errorPrintOut(cmd, 1);
			}
			if(cmd.size() != 1) {
				for(int i=1; i<cmd.size(); i++) {
					if(cmd.get(i).equals("\\n"))
						System.out.println();
					else if(cmd.get(i).equals("\\t"))
						System.out.print("\t");
					else if(variable_definitions.containsKey(cmd.get(i)))
						System.out.print(variable_definitions.get(cmd.get(i)));
					else
						System.out.print(cmd.get(i));
				}
			} else {
				System.out.println();
			}
			
		} else if(cmd.get(0).equals("inc")) {
			if(cmd.size() != 2) {
				errorPrintOut(cmd, 1);
			}
			if(variable_definitions.containsKey(cmd.get(1))) {
				String tmp = variable_definitions.get(cmd.get(1));
				if(tmp.matches("-?\\d+")) {
					variable_definitions.put(cmd.get(1), (Integer.parseInt(tmp)+1)+"");
				} else if(tmp.matches("-?\\d+\\.\\d*")) {
					variable_definitions.put(cmd.get(1), (Double.parseDouble(tmp)+1)+"");
				} else {
					errorPrintOut(cmd, 4);
				}
			}
		
		} else if(cmd.get(0).equals("dec")) {
			if(cmd.size() != 2) {
				errorPrintOut(cmd, 1);
			}
			if(variable_definitions.containsKey(cmd.get(1))) {
				String tmp = variable_definitions.get(cmd.get(1));
				if(tmp.matches("-?\\d+")) {
					variable_definitions.put(cmd.get(1), (Integer.parseInt(tmp)-1)+"");
				} else if(tmp.matches("-?\\d+\\.\\d*")) {
					variable_definitions.put(cmd.get(1), (Double.parseDouble(tmp)-1)+"");
				} else {
					errorPrintOut(cmd, 4);
				}
			}
			
		} else if(cmd.get(0).equals("end")) {
			if(cmd.size() > 1) {
				errorPrintOut(cmd, 1);
			}
			end_app = true;
			System.exit(0);
		} else {
			errorPrintOut(cmd, 5);
		}
	}
	
	public static void errorPrintOut(LinkedList<String> cmd, int mod) {
		if(mod == 1) {
			System.out.print("Syntax error (incorrect number of arguments): ");
		} if(mod == 2) {
			System.out.print("Syntax error (incorrect variable name): ");
		} if(mod == 3) {
			System.out.print("Error (to many steps): ");
		} if(mod == 4) {
			System.out.print("Error (incorrect variable type): ");
		} if(mod == 5) {
			System.out.println("Syntax error (unknown syntax): ");
		} if(mod == 6) {
			System.out.println("Error (insufficient room for the cmp instruction: ");
		}
		for(String s: cmd)
			System.out.print(s+" ");
		System.out.println();
		System.exit(0);
		
	}
	
	public static LinkedList<String> tokenize(String inp) {
		LinkedList<String> out = new LinkedList<String>();
		StringBuilder strb = new StringBuilder();
		boolean is_quote = false;
		for(int i=0; i<inp.length(); i++) {
			char c = inp.charAt(i);
			if(c == '\"') {
				if(is_quote) {
					is_quote = false;
				} else {
					is_quote = true;
				}
			} else if(c == ' ' && !is_quote) {
				if(!strb.toString().isEmpty()) {
					out.add(strb.toString());
					strb = new StringBuilder();
				}
			} else {
				strb.append(c);
			}
		}
		if(!strb.toString().isEmpty())
			out.add(strb.toString());
		return out;
	}
	
	public static void main(String[] args) {		
		File file = new File("");
		String input;
		if(args.length > 0) {
			file = new File(args[0]);
			System.out.println("Running file: "+file.toString());
		} else {
			System.out.println("No file specified.");
			System.exit(0);
		}
		try {
			BufferedReader brdr = new BufferedReader(new FileReader(file.getPath()));
			while((input = brdr.readLine()) != null) {
				if(input.length() > 0) {
					back_stack.push(tokenize(input));
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		while(!back_stack.isEmpty())
			front_stack.push(back_stack.pop());
		LinkedList<String> cmd;
		while(!end_app && front_stack.size() > 0) {
			cmd = front_stack.peek();
			back_stack.push(front_stack.pop());
			eval(cmd);
		}		
	}
}
