package il;

import java.io.*;
import java.util.*;

public class Main {

	
	static BufferedReader is;
	static Scanner scan;
	static char cin;
	static String code;
	static String FunctionName;

	static int memorylnt = 100;
	static char memory[] = new char[memorylnt];
	static int memorypnt;
	static int queryfield;
	static int loopCnt;
	static Map<String, Integer> jumpMarks;
	static List<Integer> jumpBack;
	static byte queryFlag;

	public static void main(String[] args) {
		code = "";
		cin = '\0';
		memorypnt = 0;
		queryfield = 0;
		loopCnt = 0;
		scan = new Scanner(System.in);
		is = new BufferedReader(new InputStreamReader(System.in));
		jumpMarks = new HashMap<String, Integer>();
		jumpBack = new ArrayList<Integer>();
		queryFlag = 0x0;
		
		for(int i = 0; i < memorylnt; i++)
			memory[i] = 0;
		
		System.out.println("Bitte Code eingeben: ");
		code = scan.nextLine();
		
		clearCode();
		
		runCode();
		
		scan.close();
	}
	
	private static int runCode() {
		boolean isTrue;
		isTrue = false;
		for(int i = 0; i < code.length(); i++) {
			switch(code.charAt(i)) {
			case '>':
				if(memorypnt <= 101)
					memorypnt++;
				break;
			case '<':
				if(memorypnt != 0)
					memorypnt--;
				break;
			case '+':
				memory[memorypnt] = (char) (memory[memorypnt] + 1);
				break;
			case '-':
				memory[memorypnt] = (char) (memory[memorypnt] - 1);
				break;
			case '.':
				System.out.print(memory[memorypnt]);
				break;
			case ',':
				try { cin = (char)is.read(); } catch (IOException e) {}
				memory[memorypnt] = cin;
				break;
			/*case '[':
				if(memory[memorypnt] == 0) {
					loopCnt++;
					while(i < code.length() && loopCnt > 0) {
						i++;
						if(code.charAt(i) == '[')
							loopCnt++;
						else if(code.charAt(i) == ']')
							loopCnt--;
					}
				}
				break;
			case ']':
				if(memory[memorypnt] != 0) {
					loopCnt++;
					while(i < code.length() && loopCnt > 0) {
						i--;
						if(code.charAt(i) == '[')
							loopCnt--;
						else if(code.charAt(i) == ']')
							loopCnt++;
					}
				}
				break;*/
			case ':':
				break;
			case '/':
				if(jumpBack.size() != 0) {
					i = jumpBack.get((jumpBack.size()-1));
					jumpBack.remove((jumpBack.size()-1));
				}
				break;
			case '|':
				switch(queryFlag) {
				case 0x0:
					if(queryfield == memory[memorypnt]) {
						isTrue = true;
					}
					break;
				case 0x1:
					if(queryfield < memory[memorypnt]) {
						isTrue = true;
					}
					break;
				case 0x2:
					if(queryfield > memory[memorypnt]) {
						isTrue = true;
					}
					break;
				};
				queryFlag = 0x0;
				if(isTrue) {
					FunctionName = getFunctionName(i, false);
					if(jumpMarks.containsKey(FunctionName)) {
						i = jumpMarks.get(FunctionName);
					}
				}
				isTrue = false;
				break;
			case '~':
				if(code.charAt(i - 1) == '!')
				{
					queryFlag = 0x2;
				}
				else
				{
					queryFlag = 0x1;
				}
				break;
			case '!':
				if(code.charAt(i + 1) != '~')
				{
					memory[memorypnt] = (char) ~(memory[memorypnt]);
				}
				break;
			case '=':
				queryfield = memory[memorypnt];
				break;
			case '}':
				FunctionName = getFunctionName(i, false);
				if(jumpMarks.containsKey(FunctionName)) {
					jumpBack.add(i++);
					i = jumpMarks.get(FunctionName);
				}
				
				break;
			};
		}
		/*
		 * 
		 * : = function
		 * / = jump back
		 * | = test and jump to, as default testing is equal, inv: not equal
		 * ~ = bigger as, inv: smaller as
		 * ! = inverter
		 * = = safe field content for testing
		 * } = jump to
		 * 
		 * 
		 * !!! Only a compiler instruction !!!
		 * 
		 * # = include
		 * 
		 * */
			
		return 0;
		
	}
	
	private static void clearCode() {
		
		String cleanCode;
		String functionName;
		cleanCode = "";
		functionName = "";
		
		for(int i = 0; i < code.length(); i++) {
			
			switch(code.charAt(i)) {
			case '>':
			case '<':
			case '+':
			case '-':
			case '.':
			case ',':
			//case '[':
			//case ']':
			case '/':
			case '~':
			case '!':
			case '=':
				cleanCode = cleanCode + code.charAt(i);
				break;
			case '|':
			case '}': 
				functionName = getFunctionName(i, false);
				cleanCode = cleanCode + code.charAt(i) + functionName;
				break;
			case ':':
				functionName = getFunctionName(i, true);
				jumpMarks.put(functionName, (cleanCode.length()-1));
			case '#':
			default:
				cleanCode = cleanCode + "";
			};
			
		}
		
		code = cleanCode;
	}
	
	private static String getFunctionName(int i, boolean for_backWard) {

		String functionName = "";
		boolean loopFunctionName = true;
		
		int j;
		
		if(for_backWard) {
			j = i - 1;
		} else {
			j = i + 1;
		}
		
		while(j >= 0 && j < code.length() && loopFunctionName == true) {
			switch(code.charAt(j)) {
				case '>':
				case '<':
				case '+':
				case '-':
				case '.':
				case ',':
				//case '[':
				//case ']':
				case '/':
				case '|':
				case '~':
				case '!':
				case '=':
				case '}':
				case ':':
				case '#':
				case ' ':
					loopFunctionName = false;
					break;
				default:
					if(for_backWard) {
						functionName = code.charAt(j) + functionName;
					} else {
						functionName = functionName + code.charAt(j);
					}
			};
			if(for_backWard) {
				j--;
			} else {
				j++;
			}
		}
		return functionName;
	}
	
}