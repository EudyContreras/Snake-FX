package com.EudyContreras.Snake.Utilities;

import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;




/**
 * <h1>String Encrypter</h1>
 * 
 * This programs allows you encrypt a string and store as a byte array.
 * the program provides a simple encryption set of methods which make use of
 * scrambling/adding/replacing/swapping/ the core structure of the string. 
 * The encrypted message is the store as byte array object which can be sent through sockets
 * or stored locally. In order to be able to view the content of the string the object
 * must be passed through the decrypt method of this program which will format and reconstruc the
 * string to it's original state.
 * 
 * If the message is infiltrated while stored as an encrypted byte array whether if it is traveling 
 * through a socket or store locally on a system. the thief wont be able to see
 * the content of the message without the key used by this class 
 *
 * @author Eudy Contreras
 * @version 1.1
 * @since 2016-08-16
 */
public class Encrypter {

	private static final char[] plain =
		{'A','B','C','D','E','F','G','H',
		 'I','J','K','L','M','N','O','P',
		 'Q','R','S','T','U','V','W','X',
		 'Y','Z','Ö','Ä','Å','0','1','2',
		 '3','4','5','6','7','8','9',' ',','};

private static final char[] key   =
		{'D',' ','F','G','H','Ä','U','J',
		 'A','L','Z','Å','N',',','P','Q',
		 'W','S','T','Ö','I','V','R','X',
		 'Y','M','K','B','O','4','6','1',
		 '8','3','2','9','0','5','7','E','C'};

private static final char[] added  =
		{'L','E','Z','G','H','Ä','U','J',
		 'A','D','F','Å','N','C','M','Y',
		 'W','S','B','Ö','I','V','R','1',
		 'Q','P','K','T','O','4','6','X',
		 '8','3','2','9','0','5','7'};

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

	public Encrypter(){}
	
	/**
	 * This constructor allows the input of a new key to be used
	 * by this program. The key must contain all letters of the
	 * swedish dictionary as well as a space character represented as
	 * a character with a space in between and the standard comma sign.
	 * 
	 * <h1>Knowing the key used by this program is not enough in order
	 * to decode the content of the strings encrypted by this program!</h1>
	 * <p>
	 * @param new_Key :The key to be passed as a new replacement key. The key set must 
	 * be 41 characters long and cannot contain duplicates. the standard
	 * comma sign must be included as well as the space chararcter ' '.
	 */
	public Encrypter(char[] new_Key){
		if(new_Key!=null && new_Key.length==41){
			key_Map.clear();
			for(int i = 0; i<key.length; i++){
				key_Map.put(new_Key[i],plain[i]);
			}
		}
	}
	/**
	 * This constructor allows the input of a new character set and a new key to be used
	 * by this program in replacement of the originals. The character set passed through
	 * this constructor cannot contain any duplicates.
	 * <p>
	 * <h1>The character set and the key set must be 
	 * of equal lenghts and neither the character set nor the key may have repeating element.
	 * Every element found in the character set must be present in the key set and most preferably 
	 * at a different index<h1>
	 * <p>
	 * <h1>Knowing either the chararcter set or the key used by this program doest not garantee
	 * the ability to decode the content of the strings encrypted by this program!</h1>
	 * <p>
	 * @param new_CharSet : The new character set to be passed as a new chararcter set. The character
	 * set cannot contain duplicates.
	 * @param new_Key :The key to be passed as a new replacement key. The key set must contain
	 * all the characters present in the character set and cannot contain duplicates
	 */
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
	/*
	 * Method used to swapp the elements of the input array.
	 * The elements will be swapped using 4 nested loops with
	 * different swap indexes. The elements are swapped around 
	 * approximately 2,825,761 times while using the original 41
	 * character char set and key set.
	 */
	private char[] swapCharacters(char[] chars){
		for(int i = 0; i<chars.length-2; i++){
			char temp = chars[i];
			chars[i] = chars[i+2];
			chars[i+2] = temp;

			for(int r = 0; r<chars.length-6; r++){
				char temp2 = chars[r];
				chars[r] = chars[r+6];
				chars[r+6] = temp2;

				for(int s = 0; s<chars.length-4; s++){
					char temp3 = chars[s];
					chars[s] = chars[s+4];
					chars[s+4] = temp3;

					for(int t = 3; t<chars.length; t++){
						char temp4 = chars[t];
						chars[t] = chars[t-3];
						chars[t-3] = temp4;
					}
				}
			}
		}
		return chars;
	}
	/*
	 * Method used to to swapp the elements which were previously swapped 
	 * by the swapCharacters method back to their original index.
	 */
	private char[] revertCharacterSwap(char[] chars) {
		for (int i = chars.length - 3; i >= 0; i--) {

			for (int r = chars.length - 7; r >= 0; r--) {

				for (int s = chars.length - 5; s >= 0; s--) {

					for (int t = chars.length - 1; t >= 3; t--) {
						char temp4 = chars[t];
						chars[t] = chars[t - 3];
						chars[t - 3] = temp4;
					}

					char temp3 = chars[s];
					chars[s] = chars[s + 4];
					chars[s + 4] = temp3;
				}

				char temp2 = chars[r];
				chars[r] = chars[r + 6];
				chars[r + 6] = temp2;
			}

			char temp = chars[i];
			chars[i] = chars[i + 2];
			chars[i + 2] = temp;
		}
		return chars;
	}
	/*
	 * Method which 
	 */
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

		char[] code = swapCharacters(upper_Case_Message);

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

		char[] code = revertCharacterSwap(code_Message.toCharArray());

		for (int i = 0; i < code.length; i++) {
			if(key_Map.containsKey(code[i])){
				code[i] = key_Map.get(code[i]);
			}
		}
		if (code_and_case.length == 2) {
			char[] case_code = case_Message.toCharArray();
			for (int i = 0; i < case_code.length-1; i++) {
				if (case_code[i] == '1') {
					code[i] = Character.toUpperCase(code[i]);
				}
				else if (case_code[i] == '0') {
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

		return cypherList;

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

		String unScrambled = unScramble(list);

		String decyphered = revertSubstitution(unScrambled);

		return decyphered;
	}

	public static void main(String[] args) {
		Encrypter encrypter = new Encrypter();


		long startTime = System.currentTimeMillis();
		byte[] encryption = encrypter.encrypt("Whats up man, how are you doing today?, Would you come over to play some video games!!");
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