package com.EudyContreras.Snake.Utilities;

import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.EudyContreras.Snake.MultiplayerServer.MultiplayerClient;



/**
 * This is a class which can be used to encrypt any sort of string,
 * the class provides a simple encryption program which uses a scrambler/adder/replacer/swapper.
 * The encrypted message is sent as an object which can then be casted
 * into a byte array which can then be formated and decrypted by this software using the decrypt method.
 * If the message is infiltrated while traveling through a socket the thief
 * wont be able to see the content of the message without the key used by this class
 *
 * @author Eudy Contreras
 *
 */

public class Encrypter {

	private final char[] plain =
			{'A','B','C','D','E','F','G','H',
			 'I','J','K','L','M','N','O','P',
			 'Q','R','S','T','U','V','W','X',
			 'Y','Z','Ö','Ä','Å','0','1','2',
			 '3','4','5','6','7','8','9',' ',
			 ',','?','.'};

	private final char[] key   =
			{'D',',','6','9','X','Ä','U','J',
			 'A','L','3','Å','N','?','P','Q',
			 'W','S',' ','Ö','I','V','R','H',
			 'Y','M','K','B','O','4','F','1',
			 '8','Z','2','G','0','.','7','T',
			 'E','C','5'};

	private final char[] added  =
			{'L','7','Z','G','H','Ä','U','J',
			 'A','D','.','Å','0','C','M','Y',
			 'W','2','B','Ö','I','V','?','1',
			 'Q','P','K','T','O','4','6','X',
			 '8','3','S','9','N',',','E',' ',
			 '5','R','F'};

	private final String[] byte_Plain =
			{"1","2","3","4","5","6","7","8","9","0"};

	private final String[] byte_Key =
			{"8","0","5","4","9","6","1","3","7","2"};

	private final Map<Character, Character> plain_Map;
	{
		plain_Map = new HashMap<Character, Character>();
		for(int i = 0; i<plain.length; i++){
			plain_Map.put(plain[i],key[i]);
		}
	}

	private final Map<Character, Character> key_Map;
	{
		key_Map = new HashMap<Character, Character>();
		for(int i = 0; i<key.length; i++){
			key_Map.put(key[i],plain[i]);
		}
	}

	private Map<String, String> plain_Byte_Map;
	{
		plain_Byte_Map = new HashMap<String, String>();
		for(int i = 0; i<byte_Plain.length; i++){
			plain_Byte_Map.put(byte_Plain[i],byte_Key[i]);
		}
	}

	private final Map<String, String> key_Byte_Map;
	{
		key_Byte_Map = new HashMap<String, String>();
		for(int i = 0; i<byte_Key.length; i++){
			key_Byte_Map.put(byte_Key[i],byte_Plain[i]);
		}
	}

	private final int operationCount = 16;

	/**
	 * This method will will used a simple key
	 * and encrypt a message
	 * @param message
	 * @return
	 */
	public Encrypter(){

	}
	public Encrypter(char[] new_Key){
		if(new_Key!=null){
			key_Map.clear();
			for(int i = 0; i<key.length; i++){
				key_Map.put(new_Key[i],plain[i]);
			}
		}
	}
	public Encrypter(char[] new_CharSet, char[]new_Key){
		if(new_Key!=null && new_CharSet!=null){
			key_Map.clear();
			plain_Map.clear();
			for(int i = 0; i<new_CharSet.length; i++){
				key_Map.put(new_Key[i],new_CharSet[i]);
				plain_Map.put(new_CharSet[i], new_Key[i]);
			}
		}
	}
	private final String applySubstitution(String message) {

		char[] case_Binary = new char[message.length()];

		for(int i = 0; i < message.length(); i++) {
			if (Character.isUpperCase(message.charAt(i))) {
				case_Binary[i] = '1';
			} else if (Character.isLowerCase(message.charAt(i))) {
				case_Binary[i] = '0';
			}
		}

		char[] upper_Case_Message = new char[message.length()];

		for(int i = 0; i<message.length(); i++) {
			upper_Case_Message[i] = Character.toUpperCase(message.charAt(i));
		}

		char[] code = upper_Case_Message;

		for (int i = 0; i < code.length; i++) {
			if(plain_Map.containsKey(code[i])){
				code[i] = plain_Map.get(code[i]);
			}
		}
		return String.valueOf(code)+"¤¤¤¤"+String.valueOf(case_Binary);
	}

	/**
	 * this method will decipher and encrypted string message
	 * @param message
	 * @return
	 */
	private final String revertSubstitution(String message) {
		String[] code_and_case = message.split("¤¤¤¤");
		String code_Message;
		String case_Message;

		if(code_and_case.length == 2) {
			code_Message = code_and_case[0];
			case_Message = code_and_case[1];
		}
		else {
			code_Message = message;
			case_Message = null;
		}

		char[] code = code_Message.toCharArray();

		for (int i = 0; i < code.length; i++) {
			if(key_Map.containsKey(code[i])){
				code[i] = key_Map.get(code[i]);
			}
		}
		if (code_and_case.length == 2) {
			for (int i = 0; i < case_Message.length()-1; i++) {
				if (case_Message.charAt(i) == '1') {
					code[i] = Character.toUpperCase(code[i]);
				}
				else if (case_Message.charAt(i) == '0') {
					code[i] = Character.toLowerCase(code[i]);
				}
			}
		}
		return String.valueOf(code);
	}

	private final int getRandom(int value){
		SecureRandom rand = new SecureRandom();
		return rand.nextInt(value);
	}
	/**
	 * Further increases the encryption level of the message by adding random numbers and characters
	 * to the already encrypted message the process can then be reversed with an additional key.
	 * @param code: the encrypted message
	 * @return
	 */
	private final LinkedList<String> scramble(String code) {
		String encrypted_Code = applySubstitution(code);
		String[] case_and_code = encrypted_Code.split("¤¤¤¤");
		String cypher = case_and_code[0];
		String code_Case = case_and_code[1];
		LinkedList<String> cypherList = new LinkedList<>();

		char[] cypher_Char_Arr = cypher.toCharArray();

		for (int index = 0; index < cypher_Char_Arr.length; index++) {
			cypherList.add(added[getRandom(added.length)] + "" + added[getRandom(added.length)]+ String.valueOf(cypher_Char_Arr[index]) + added[getRandom(added.length)]+ (getRandom(99 - 10 + 1) + 10));
		}

		cypherList.addFirst("" + (getRandom(999 - 100 + 1) + 100));
		cypherList.add(code_Case + getRandom(10));

		return swap(cypherList);

	}
	/**
	 * This method will unscramble and rebuild a scrambled string
	 * @param cypher_List
	 * @return
	 */
	private final String unScramble(List<String> cypher_List) {
		StringBuilder string_Builder = new StringBuilder();
		List<String> cypher_List_Output = new LinkedList<>();
		String case_Code = cypher_List.get(cypher_List.size() - 1);

		for (int index = 0; index < cypher_List.size() - 1; index++) {
			cypher_List_Output.add(String.valueOf(cypher_List.get(index).toCharArray()[2]));

			if (index >= 1)
				string_Builder.append(cypher_List_Output.get(index));
			continue;
		}

		char[] raw_Code = (string_Builder.toString() + "¤¤¤¤" + case_Code).toCharArray();
		char[] unscrambled = new char[raw_Code.length - 1];

		for (int i = 0; i < unscrambled.length; i++) {

			unscrambled[i] = raw_Code[i];
		}
		return String.valueOf(unscrambled);
	}
	/**
	 * Swaps the content of the array the as many times as
	 * the operation count.
	 * @param code
	 * @return
	 */
	private final LinkedList<String> swap(LinkedList<String> code) {
		LinkedList<String> output = new LinkedList<>();
		LinkedList<char[]> chars = new LinkedList<>();
		String[] swapped_Array = new String[code.size()];

		for (int index = 0; index < code.size(); index++) {

			swapped_Array[index] = code.get(index);
			chars.add(swapped_Array[index].toCharArray());
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
	 * unswaps the content of the array in order to make it
	 * ready to be deciphered
	 * @param code
	 * @return
	 */
	private final List<String> unSwap(List<String> code) {
		List<String> output = new LinkedList<>();
		List<char[]> chars = new LinkedList<>();
		String[] unswapped_Array = new String[code.size()];

		for (int index = 0; index < code.size(); index++) {

			unswapped_Array[index] = code.get(index);
			chars.add(unswapped_Array[index].toCharArray());
		}

		for (int iteration = 0; iteration < operationCount; iteration++) {
			for (int i = chars.size() - 2; i >= 1; i--) {
				for (int r = chars.get(i).length - 1; r >= 0; r--) {

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

	private final String printBytes(byte[] binaryEncription){

		return new String(binaryEncription, Charset.forName("UTF-8"));

	}

	private final List<String> fromStringToList(String list) {

		return Arrays.asList(list.split(", "));

	}

	private final byte[] toPrimitives(Byte[] byte_Object){

		byte[] bytes = new byte[byte_Object.length];

		int i = 0;
		for(Byte byte_Objects : byte_Object)bytes[i++] = byte_Objects;

		return bytes;
	}

	private final Byte[] toByteObject(byte[] byte_prime) {
		Byte[] bytes = new Byte[byte_prime.length];

		int i = 0;
		for (byte byte_Primes : byte_prime) bytes[i++] = byte_Primes;

		return bytes;
	}
	/**
	 * Replace the first digits of the byte to a predefined digit!
	 * @param bytes
	 * @return
	 */
	private final Byte[] replaceBytes(Byte[] bytes) {
		Byte[] temp_Bytes = Arrays.copyOf(bytes, bytes.length);

		for (int i = 0; i < temp_Bytes.length; i++) {
			if (Integer.toString(temp_Bytes[i]).charAt(0) != '-') {
				String temp_String = String.valueOf(temp_Bytes[i]);
				String new_Value = plain_Byte_Map.get(Integer.toString(temp_Bytes[i]).substring(0, 1))+ temp_String.substring(1);
				temp_Bytes[i] = Byte.valueOf(new_Value);
			}
		}
		return temp_Bytes;
	}
	/**
	 * Places back the original digit which was previously replaced
	 * @param bytes
	 * @return
	 */
	private final Byte[] revertByteReplacement(Byte[] bytes) {
		Byte[] temp_Bytes = Arrays.copyOf(bytes, bytes.length);

		for (int i = 0; i < temp_Bytes.length; i++) {
			if (Integer.toString(temp_Bytes[i]).charAt(0) != '-') {
				String temp_String = String.valueOf(temp_Bytes[i]);
				String new_Value = key_Byte_Map.get(Integer.toString(temp_Bytes[i]).substring(0, 1))+ temp_String.substring(1);
				temp_Bytes[i] = Byte.valueOf(new_Value);
			}
		}
		return temp_Bytes;
	}
	/**
	 * Reverses the byte array for further obfuscation
	 * @param encryption
	 * @return
	 */
	private final byte[] revertBytes(Byte[] encryption){
		Byte[] byte_Array = replaceBytes(Arrays.copyOf(encryption, encryption.length));

		List<Byte> byte_List = Arrays.asList(byte_Array);

		Collections.reverse(byte_List);

		return toPrimitives(byte_Array);
	}
	/**
	 * Reverses the byte array to its original state
	 * @param encryption
	 * @return
	 */
	private final byte[] revertBytesBack(Byte[] encryption){
		Byte[] byte_Array = encryption;

		List<Byte> byte_List = Arrays.asList(byte_Array);

		Collections.reverse(byte_List);

		return toPrimitives(revertByteReplacement(byte_Array));
	}

	public byte[] encrypt(String message){
		String cypher = scramble(message).toString();

		byte[] encryption = cypher.getBytes(Charset.forName("UTF-8"));

		byte[] swapped_Bytes = revertBytes(toByteObject(encryption));

		return swapped_Bytes;
	}

	public String decrypt(byte[] encryption){
		byte[] unswapped_Bytes = revertBytesBack(toByteObject(encryption));

		List<String> list  = fromStringToList(new String(unswapped_Bytes, Charset.forName("UTF-8")));

		List<String> unSwapped = unSwap(list);

		String unScrambled = unScramble(unSwapped);

		String decyphered = revertSubstitution(unScrambled);

		return decyphered;
	}

	public static void main(String[] args) {
		char[] key   =
			{'O',',','O','O','O','O','U','O',
			 'O','O','O','O','O','?','O','Q',
			 'O','O',' ','O','O','O','O','O',
			 'O','O','O','O','O','4','O','1',
			 '8','O','2','G','0','.','O','O',
			 'O','O','O'};
			char[] key2   =
				{'O',',','O','O','O','O','U','O',
				 'O','O','O','O','O','?','O','Q',
				 'O','O',' ','O','O','O','O','O',
				 'O','O','O','O','O','4','O','1',
				 '8','O','2','G','0','.','O','O',
				 'O','O','O'};
		Encrypter encrypter = new Encrypter();
		System.out.println(encrypter.applySubstitution("Whats up man, how are you doing today?"));
		System.out.println(encrypter.scramble("Whats up man, how are you doing today?"));
		System.out.println(encrypter.unSwap(encrypter.scramble("Whats up man, how are you doing today?")));
		System.out.println("");
		System.out.println(encrypter.revertSubstitution(encrypter.unScramble(encrypter.unSwap(encrypter.scramble("Whats up man, how are you doing today?")))));
		System.out.println(encrypter.revertSubstitution("HIGYFS"));
		System.out.println("");


		long startTime = System.currentTimeMillis();
		byte[] encryption = encrypter.encrypt("Whats up man, how are you doing today?");
		long endTime = System.currentTimeMillis();
		System.out.println("Encryption speed: "+(endTime - startTime)+ " Milliseconds");

		System.out.println("Actual encrypted message:");
		System.out.println("////////////////////////////////////////////////////////////////////////////////");
		System.out.println("");
		System.out.println(encrypter.printBytes(encryption));

		System.out.println("");
		System.out.println("////////////////////////////////////////////////////////////////////////////////");
		System.out.println(Arrays.toString(encryption));
		long startTime2 = System.currentTimeMillis();
		System.out.println(encrypter.decrypt(encryption));
		long endTime2 = System.currentTimeMillis();
		System.out.println("Decryption speed: "+(endTime2 - startTime2)+ " Milliseconds");
	}
}