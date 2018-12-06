import java.util.*;
import java.math.BigInteger;
public class FractionCalculator {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		String inputString = "";
		//Repeats until the input is quit
		/*TEST:
		1 + 1 = 2
		1/2 - 3/4 = -1/4
		1_2/3 + 4/5 = 2_7/15
		1/-2 - 1/2 = -1
		4983/5436 * 7657/36356 = 84227/436272
		23/40 * 14/40 = 161/800
		0 * 2358 = 0
		1/2 * 3/5 = 3/10
		4 * 4 = 16
		63 * 968 = 60984
		-1 * 4/5 = = -4/5
		15/4 * 4/15 = 1
		1/3 * 3/5 = 1/5
		bob  Invalid Input!
		*/
		while(!inputString.equals("quit")) {
			System.out.println("Operation? (Frac1 Operator Frac2)");
			inputString = input.nextLine();
			if(inputString.contains(" ") && inputString.contains("+") || inputString.contains("-") || inputString.contains("*") || inputString.contains("-")) {
				//Splits up input into firstFraction, operator, secondFraction
				String[] splitInput = {inputString.substring(0, inputString.indexOf(' ')), inputString.substring(inputString.indexOf(' ') + 1, inputString.indexOf(' ') + 2), inputString.substring(inputString.indexOf(inputString.charAt(inputString.indexOf(' ') + 1) + " ") + 2)};
				//Turns Strings into int splitInputays
				int[] fracNum1 = encrypt(splitInput[0]);
				int[] fracNum2 = encrypt(splitInput[2]);
				//Does Math
				int[] answer = math(fracNum1, fracNum2, splitInput[1]);
				//Prints out result (simplified)
				System.out.println("Mixed: " + decrypt(mixify(fracNum1)) + " " + splitInput[1] + " " + decrypt(mixify(fracNum2)) + " = " + decrypt(simplify(answer)) + "\nImproper: " + decrypt(improperize(fracNum1)) + " " + splitInput[1] + " " + decrypt(improperize(fracNum2)) + " = " + decrypt(simplify(improperize(answer))));
			}
			else if(inputString.equals("quit"))
				System.out.println("Quitting...");
			else
				System.out.println("Invalid Input!");
		}
	}
	public static int[] encrypt(String str) {
		//Index 0 = whole number
		//Index 1 = numerator
		//Index 2 = denominator
		int[] fracNum = new int[3];
		if(str.contains("_")) {
			fracNum[0] = Integer.parseInt(str.substring(0, str.indexOf('_')));
			fracNum[1] = Integer.parseInt(str.substring(str.indexOf('_') + 1, str.indexOf('/')));
			fracNum[2] = Integer.parseInt(str.substring(str.indexOf('/') + 1, str.length()));
		}
		else if(str.contains("/")) {
			fracNum[1] = Integer.parseInt(str.substring(0, str.indexOf('/')));
			fracNum[2] = Integer.parseInt(str.substring(str.indexOf('/') + 1, str.length()));
		}
		else {
			fracNum[0] = Integer.parseInt(str);
			fracNum[2] = 1;
		}
		return fracNum;
	}
	public static String decrypt(int[] fracNum) {
		if(fracNum[0] != 0) {
			if(fracNum[1] != 0)
				return fracNum[0] + "_" + fracNum[1] + "/" + fracNum[2];
			else
				return "" + fracNum[0];
		}
		else if(fracNum[1] != 0)
			return fracNum[1] + "/" + fracNum[2];
		else
			return "0";
		}
	public static int[] math(int[] frac1, int[] frac2, String op) {
		//makes fraction improper
		int[] answer = new int[3];
		frac1[1] += frac1[0] * frac1[2];
		frac1[0] = 0;
		frac2[1] += frac2[0] * frac2[2];
		frac2[0] = 0;
		//System.out.println(Arrays.toString(frac1));
		//checks for negative denominators
		if(frac1[2] < 0) {
			frac1[1] *= -1;
			frac1[2] *= -1;
		}
		if(frac2[2] < 0) {
			frac2[1] *= -1;
			frac2[2] *= -1;
		}
		//addition
		if(op.equals("+")) {
			answer[1] = (frac1[1] * frac2[2]) + (frac2[1] * frac1[2]);
			answer[2] = frac1[2] * frac2[2];
		}
		//subtraction
		else if(op.equals("-")) {
			answer[1] = (frac1[1] * frac2[2]) - (frac2[1] * frac1[2]);
			answer[2] = frac1[2] * frac2[2];
		}
		//multiplication
		else if(op.equals("*")) {
			answer[1] = frac1[1] * frac2[1];
			answer[2] = frac1[2] * frac2[2];
		}
		//division
		else if(op.equals("/")) {
			answer[1] = frac1[1] * frac2[2];
			answer[2] = frac1[2] * frac2[1];
		}
		//re-mixes fractions (5/4 --> 1_1/4)
		if(Math.abs(answer[1]) >= answer[2] && answer[2] > 0) {
			answer[0] += answer[1]/answer[2];
			answer[1] = Math.abs(answer[1]%answer[2]);
			//System.out.println(Arrays.toString(answer));
		}
		//handles negative numerators
		if(answer[1] < 0 && answer[0] > 0) {
			answer[0] -= 1;
			answer[1] += answer[2];
			//System.out.println(Arrays.toString(answer));
		}
		return answer;
	}
	public static int[] simplify(int[] frac) {
	BigInteger prime = BigInteger.ONE;
	//Prevents negative negative numbers
	if(frac[0] < 0 && frac[1] < 0) {
		frac[0] *= -1;
		frac[1] *= -1;
	}
	//Simplifies fractions by checking every prime number
	while(prime.intValue() <= Math.abs(frac[2]) && prime.intValue() <= Math.abs(frac[1])) {
		prime = prime.nextProbablePrime();
		if(frac[1] % prime.intValue() == 0 && frac[2] % prime.intValue() == 0) {
			frac[1] /= prime.intValue();
			frac[2] /= prime.intValue();
			prime = BigInteger.ONE;
		}
	}
	return frac;
}
	public static int[] improperize(int[] frac) {
		//makes fraction improper
				int[] frac1 = frac;
				frac1[1] += frac1[0] * frac1[2];
				frac1[0] = 0;
				return frac1;
	}
	public static int[] mixify(int[] frac) {
		int[] answer = frac;
		if(Math.abs(answer[1]) >= answer[2] && answer[2] > 0) {
			answer[0] += answer[1]/answer[2];
			answer[1] = Math.abs(answer[1]%answer[2]);
			//System.out.println(Arrays.toString(answer));
		}
		//handles negative numerators
		if(answer[1] < 0 && answer[0] > 0) {
			answer[0] -= 1;
			answer[1] += answer[2];
			//System.out.println(Arrays.toString(answer));
		}
		return answer;
	}
}
