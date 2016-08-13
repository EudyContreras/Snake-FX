package com.EudyContreras.Snake.Utilities;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;


/**
 * This is a class which can be used to encrypt any sort of string,
 * the class provides a simple encryption program which uses a scrambler/adder/replacer/swapper.
 * The encrypted message is sent as an object which can then be casted
 * into a String LinkedList or a String array which can then be formated and decrypted.
 * If the message is infiltrated while traveling through a socket the thief
 * wont be able to see the content of the message without the key or this class.
 *
 * @author Eudy Contreras
 *
 */
/*
private static final char[] plain = {'A','B','C','D','E','F','G','H',
									 'I','J','K','L','M','N','O','P',
									 'Q','R','S','T','U','V','W','X',
									 'Y','Z','Ö','Ä','Å','0','1','2',
									 '3','4','5','6','7','8','9'};

private static final char[] key   = {'D','E','F','G','H','Ä','U','J',
									 'A','L','Z','Å','N','C','P','Q',
									 'W','S','T','Ö','I','V','R','X',
									 'Y','M','K','B','O','4','6','1',
									 '8','3','2','9','0','5','7'};

*/
public class Encrypter {

	private static final char[] added  ={'L','E','Z','G','H','Ä','U','J',
										 'A','D','F','Å','N','C','M','Y',
										 'W','S','B','Ö','I','V','R','1',
										 'Q','P','K','T','O','4','6','X',
										 '8','3','2','9','0','5','7'};


	private static final int operationCount = 16;

	/**
	 * This method will will used a simple key
	 * and encrypt a message
	 * @param message
	 * @return
	 */
	private static String obfuscate(String message) {
		String caseCode = convertCase(message);
		char[] caseBinary = new char[message.length()];
		for(int i = 0; i<caseCode.length(); i++) {
			if(caseCode.charAt(i) == 'A') {
				caseBinary[i] = '1';
			}
			else if(caseCode.charAt(i) == 'a') {
				caseBinary[i] = '0';
			}
		}
		char[] upperCaseMessage = new char[message.length()];
		for(int i = 0; i<message.length(); i++) {
			upperCaseMessage[i] = Character.toUpperCase(message.charAt(i));
		}
		char[] code = upperCaseMessage;
			for (int i = 0; i < code.length; i++) {
					 if(code[i] == 'A') {code[i] = 'D';}
				else if(code[i] == 'B') {code[i] = 'E';}
				else if(code[i] == 'C') {code[i] = 'F';}
				else if(code[i] == 'D') {code[i] = 'G';}
				else if(code[i] == 'E') {code[i] = 'H';}
				else if(code[i] == 'F') {code[i] = 'Ä';}
				else if(code[i] == 'G') {code[i] = 'U';}
				else if(code[i] == 'H') {code[i] = 'J';}
				else if(code[i] == 'I') {code[i] = 'A';}
				else if(code[i] == 'J') {code[i] = 'L';}
				else if(code[i] == 'K') {code[i] = 'Z';}
				else if(code[i] == 'L') {code[i] = 'Å';}
				else if(code[i] == 'M') {code[i] = 'N';}
				else if(code[i] == 'N') {code[i] = 'C';}
				else if(code[i] == 'O') {code[i] = 'P';}
				else if(code[i] == 'P') {code[i] = 'Q';}
				else if(code[i] == 'Q') {code[i] = 'W';}
				else if(code[i] == 'R') {code[i] = 'S';}
				else if(code[i] == 'S') {code[i] = 'T';}
				else if(code[i] == 'T') {code[i] = 'Ö';}
				else if(code[i] == 'U') {code[i] = 'I';}
				else if(code[i] == 'V') {code[i] = 'V';}
				else if(code[i] == 'W') {code[i] = 'R';}
				else if(code[i] == 'X') {code[i] = 'S';}
				else if(code[i] == 'Y') {code[i] = 'Y';}
				else if(code[i] == 'Z') {code[i] = 'M';}
				else if(code[i] == 'Ö') {code[i] = 'K';}
				else if(code[i] == 'Ä') {code[i] = 'B';}
				else if(code[i] == 'Å') {code[i] = 'O';}
				else if(code[i] == '0') {code[i] = '4';}
				else if(code[i] == '1') {code[i] = '6';}
				else if(code[i] == '2') {code[i] = '1';}
				else if(code[i] == '3') {code[i] = '8';}
				else if(code[i] == '4') {code[i] = '3';}
				else if(code[i] == '5') {code[i] = '2';}
				else if(code[i] == '6') {code[i] = '9';}
				else if(code[i] == '7') {code[i] = '0';}
				else if(code[i] == '8') {code[i] = '5';}
				else if(code[i] == '9') {code[i] = '7';}
				else {code[i] = code[i];}
			}
		return String.valueOf(code)+"¤¤¤¤"+String.valueOf(caseBinary);
	}

	/**
	 * this method will decypher and encrypted string message
	 * @param message
	 * @return
	 */
	private static String DecypherEncryption(String message) {
		String[] code_and_case = message.split("¤¤¤¤");
		String codeMessage;
		String caseMessage;
		if(code_and_case.length == 2) {
			codeMessage = code_and_case[0];
			caseMessage = code_and_case[1];
		}
		else {
			codeMessage = message;
			caseMessage = null;
		}

		char[] code = codeMessage.toCharArray();
			for (int i = 0; i < code.length; i++) {
				if	   (code[i] == 'D') {code[i] = 'A';}
				else if(code[i] == 'E') {code[i] = 'B';}
				else if(code[i] == 'F') {code[i] = 'C';}
				else if(code[i] == 'G') {code[i] = 'D';}
				else if(code[i] == 'H') {code[i] = 'E';}
				else if(code[i] == 'Ä') {code[i] = 'F';}
				else if(code[i] == 'U') {code[i] = 'G';}
				else if(code[i] == 'J') {code[i] = 'H';}
				else if(code[i] == 'A') {code[i] = 'I';}
				else if(code[i] == 'L') {code[i] = 'J';}
				else if(code[i] == 'Z') {code[i] = 'K';}
				else if(code[i] == 'Å') {code[i] = 'L';}
				else if(code[i] == 'N') {code[i] = 'M';}
				else if(code[i] == 'C') {code[i] = 'N';}
				else if(code[i] == 'P') {code[i] = 'O';}
				else if(code[i] == 'Q') {code[i] = 'P';}
				else if(code[i] == 'W') {code[i] = 'Q';}
				else if(code[i] == 'S') {code[i] = 'R';}
				else if(code[i] == 'T') {code[i] = 'S';}
				else if(code[i] == 'Ö') {code[i] = 'T';}
				else if(code[i] == 'I') {code[i] = 'U';}
				else if(code[i] == 'V') {code[i] = 'V';}
				else if(code[i] == 'R') {code[i] = 'W';}
				else if(code[i] == 'S') {code[i] = 'X';}
				else if(code[i] == 'Y') {code[i] = 'Y';}
				else if(code[i] == 'M') {code[i] = 'Z';}
				else if(code[i] == 'K') {code[i] = 'Ö';}
				else if(code[i] == 'B') {code[i] = 'Ä';}
				else if(code[i] == 'O') {code[i] = 'Å';}
				else if(code[i] == '4') {code[i] = '0';}
				else if(code[i] == '6') {code[i] = '1';}
				else if(code[i] == '1') {code[i] = '2';}
				else if(code[i] == '8') {code[i] = '3';}
				else if(code[i] == '3') {code[i] = '4';}
				else if(code[i] == '2') {code[i] = '5';}
				else if(code[i] == '9') {code[i] = '6';}
				else if(code[i] == '0') {code[i] = '7';}
				else if(code[i] == '5') {code[i] = '8';}
				else if(code[i] == '7') {code[i] = '9';}
				else {code[i] = code[i];}
			}
		if (code_and_case.length == 2) {
			for (int i = 0; i < caseMessage.length()-1; i++) {
				if (caseMessage.charAt(i) == '1') {
					code[i] = Character.toUpperCase(code[i]);
				} else if (caseMessage.charAt(i) == '0') {
					code[i] = Character.toLowerCase(code[i]);
				}
			}
		}
		return String.valueOf(code);
	}
	/**
	 * Further increases the encryption level of the message by adding random numbers and characters
	 * to the already encrypted message the process can then be reversed with an additional key.
	 * @param code: the encrypted message
	 * @return
	 */
	private static LinkedList<String> scrambler(String code) {
		Random rand1 = new Random();
		Random rand2 = new Random();
		Random rand3 = new Random();
		Random rand4 = new Random();
		String encryptedCode = Encrypter.obfuscate(code);
		String[] case_and_code = encryptedCode.split("¤¤¤¤");
		String cypher = case_and_code[0];
		String codeCase = case_and_code[1];
		LinkedList<String> cypherList = new LinkedList<>();
		char[] cypherCharArr = cypher.toCharArray();
		for (int index = 0; index < cypherCharArr.length; index++){
			cypherList.add(added[rand3.nextInt(added.length)]+""+added[rand2.nextInt(added.length)]+String.valueOf(cypherCharArr[index]) + added[rand4.nextInt(added.length)]+(rand1.nextInt(99 - 10+1)+10));
		}
		cypherList.addFirst(""+(rand1.nextInt(999 - 100 +1 )+100));
		cypherList.add(codeCase+rand1.nextInt(10));
		return swapper(cypherList);

	}
	/**
	 * This method will unscramble and rebuild a scrambled string
	 * @param cypherList
	 * @return
	 */
	private static String unScrambler(LinkedList<String> cypherList) {
		StringBuilder stringBuilder = new StringBuilder();
		LinkedList<String> cypherListOutput = new LinkedList<>();
		String caseCode = cypherList.get(cypherList.size()-1);
	for (int index = 0; index < cypherList.size()-1; index++) {
		cypherListOutput.add(String.valueOf(cypherList.get(index).toCharArray()[2]));
		if(index>=1)
		stringBuilder.append(cypherListOutput.get(index));
		continue;
	}
	char[] rawCode = (stringBuilder.toString()+"¤¤¤¤"+caseCode).toCharArray();
	char[] unscrambled = new char[rawCode.length-1];
	for(int i = 0; i<unscrambled.length; i++) {
		unscrambled[i] = rawCode[i];
	}
	return String.valueOf(unscrambled);
	}
	/**
	 * Swaps the content of the array the as many times as
	 * the operation count.
	 * @param code
	 * @return
	 */
	private static LinkedList<String> swapper(LinkedList<String> code) {
		LinkedList<String> output = new LinkedList<>();
		LinkedList<char[]> chars = new LinkedList<>();
		String[] swapArray = new String[code.size()];
		for (int index = 0; index < code.size(); index++) {
			swapArray[index] = code.get(index);
			chars.add(swapArray[index].toCharArray());
		}
		for (int iteration = 0; iteration < operationCount; iteration++) {
			for (int i = 1; i < chars.size() - 2; i++) {
				for (int r = 0; r < chars.get(i).length - 1; r++) {
					char tempChar = chars.get(i)[r];
					chars.get(i)[r] = chars.get(i + 1)[r];
					chars.get(i + 1)[r] = tempChar;
				}
			}
		}
		for (int i = 0; i < chars.size(); i++) {
			output.add(String.valueOf(chars.get(i)));
		}
		return output;
	}
	/**
	 * unswapps the content of the array in order to make it
	 * ready to be deciphered
	 * @param code
	 * @return
	 */
	private static LinkedList<String> unswapper(LinkedList<String> code) {
		LinkedList<String> output = new LinkedList<>();
		LinkedList<char[]> chars = new LinkedList<>();
		String[] swapArray = new String[code.size()];

		for (int index = 0; index < code.size(); index++) {
			swapArray[index] = code.get(index);
			chars.add(swapArray[index].toCharArray());
		}
		for (int iteration = 0; iteration < operationCount; iteration++) {
			for (int i = chars.size() - 3; i >= 1; i--) {
				for (int r = chars.get(i).length - 2; r >= 0; r--) {
					char tempChar = chars.get(i)[r];
					chars.get(i)[r] = chars.get(i + 1)[r];
					chars.get(i + 1)[r] = tempChar;
				}
			}
		}
		for (int i = 0; i < chars.size(); i++) {
			output.add(String.valueOf(chars.get(i)));
		}

		return output;
	}
	/**
	 * This method will convert a string into case holder
	 * which hold which character is upper case and which
	 * isn't.
	 * @param message
	 * @return
	 */
	private static String convertCase(String message) {
		char[] caseHolder = new char[message.length()];
		for(int i = 0; i < caseHolder.length; i++) {
			if (Character.isUpperCase(message.charAt(i))) {
				caseHolder[i] = 'A';
			} else if (Character.isLowerCase(message.charAt(i))) {
				caseHolder[i] = 'a';
			}
		}
		return String.valueOf(caseHolder);
	}

	/**
	 * This method will take a case code holder
	 * and apply the case to the given message
	 * @param caseBank
	 * @param message
	 * @return
	 */
	private static String caseConverter(String caseBank,String message){
		StringBuilder input = new StringBuilder(caseBank);
		StringBuilder output = new StringBuilder(message);
		for (int index = 0; index < input.length(); index++) {
		    char current = input.charAt(index);
		    char formatedString = output.charAt(index);
		    if (Character.isLowerCase(current)) {
		        output.setCharAt(index, Character.toLowerCase(formatedString));
		    } else {
		        output.setCharAt(index, Character.toUpperCase(formatedString));
		    }
		}
		return output.toString();
	}

	private static String printBytes(byte[] binaryEncription){
		return new String(binaryEncription, Charset.forName("UTF-8"));
	}

	public static byte[] encrypt(String message){
		String cypher = scrambler(message).toString();
		byte[] encryption = cypher.getBytes(Charset.forName("UTF-8"));
		System.out.println(printBytes(encryption));
		System.out.println(Arrays.toString(encryption));
		return encryption;
	}
	public static String decrypt(byte[] encryption){
		LinkedList<String> list  = fromStringToList(new String(encryption, Charset.forName("UTF-8")));
		LinkedList<String> unSwapped = Encrypter.unswapper(list);
		String unScrambled = Encrypter.unScrambler(unSwapped);
		String decyphered =  Encrypter.DecypherEncryption(unScrambled);
		return decyphered;
	}
	private static LinkedList<String> fromStringToList(String list){
		String[] newList = list.split(", ");
		LinkedList<String> code = new LinkedList<String>();
		for(int i = 0; i<newList.length; i++){
			code.add(newList[i]);
		}
		return code;
	}
	public static void main(String[] args) {
		System.out.println(Encrypter.caseConverter("AaaaAA", "eudycr"));
		System.out.println(Encrypter.convertCase("Whats up man, how are you doing today?"));
		System.out.println(Encrypter.obfuscate("Whats up man, how are you doing today?"));
		System.out.println(Encrypter.scrambler("Whats up man, how are you doing today?"));
		System.out.println(Encrypter.unswapper(Encrypter.scrambler("Whats up man, how are you doing today?")));
		System.out.println(Encrypter.unScrambler(Encrypter.unswapper(Encrypter.scrambler("Whats up man, how are you doing today?"))));
		System.out.println("");
		System.out.println(Encrypter.DecypherEncryption(Encrypter.unScrambler(Encrypter.unswapper(Encrypter.scrambler("Whats up man, how are you doing today?")))));
		System.out.println(Encrypter.DecypherEncryption("HIGYFS"));
		System.out.println("");
		System.out.println("");
		System.out.println("");
		byte[] encryption = Encrypter.encrypt("Whats up man, how are you doing today?");
		System.out.println(Encrypter.decrypt(encryption));
	}
}