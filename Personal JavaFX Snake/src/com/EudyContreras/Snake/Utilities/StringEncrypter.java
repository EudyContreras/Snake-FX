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
public class StringEncrypter {

	private final static Charset format = Charset.forName("UTF-8");

	private final static char[] plain =
		{'A','B','C','D','E','F','G','H',
		 'I','J','K','L','M','N','O','P',
		 'Q','R','S','T','U','V','W','X',
		 'Y','Z','Ö','Ä','Å','0','1','2',
		 '3','4','5','6','7','8','9',' ',
		 ',','?','.','!'};

	private final static char[] key   =
		{'D',' ','F','G','H','Ä','U','J',
		 'A','L','Z','Å','N',',','P','Q',
		 'W','S','T','Ö','.','V','R','X',
		 'Y','M','!','B','O','4','6','1',
		 '8','3','2','9','0','5','7','E',
		 'C','?','I','K'};

	private final static char[] added  =
		{'L','3','5','G','0','Ä','1','0',
		 'A','D','9','Å','N','C','0','Y',
		 'W','S','8','Ö','4','V','4','1',
		 'Q','7','K','6','O','3','6','X',
		 '8','3','2','9','0','5','7'};

	private final static String[] byte_Plain =
		{"1","2","3","4","5","6","7","8","9","0"};

	private final static String[] byte_Key =
		{"8","0","5","4","9","6","1","3","7","2"};

	private final static Map<Character, Character> plain_Map;
	static
	{
		plain_Map = new HashMap<Character, Character>();
		for(int i = 0; i<plain.length; i++){
			plain_Map.put(plain[i],key[i]);
		}
	}

	private final static Map<Character, Character> key_Map;
	static
	{
		key_Map = new HashMap<Character, Character>();
		for(int i = 0; i<plain.length; i++){
			key_Map.put(key[i],plain[i]);
		}
	}

	private static Map<String, String> plain_Byte_Map;
	static
	{
		plain_Byte_Map = new HashMap<String, String>();
		for(int i = 0; i<byte_Plain.length; i++){
			plain_Byte_Map.put(byte_Plain[i],byte_Key[i]);
		}
	}

	private final static Map<String, String> key_Byte_Map;
	static
	{
		key_Byte_Map = new HashMap<String, String>();
		for(int i = 0; i<byte_Plain.length; i++){
			key_Byte_Map.put(byte_Key[i],byte_Plain[i]);
		}
	}
	/**
	 * This values determined the swap indexes at which the swapping methods
	 * will operate.
	 */
	private final static int  swap_index_1 = 2;
	private final static int  swap_index_2 = 6;
	private final static int  swap_index_3 = 4;
	private final static int  swap_index_4 = 3;
	
	/*
	 * The higher the number the more random characters will be added to the encryption.
	 * The number set here will affect the performace of the application. The higher the
	 * number the lower the performance will be but the more populated with random characters
	 * he encryption will be.
	 */
	private final static int  max_additions = 6;
	
	/*
	 * The index in which the actually code character will be added too
	 * the index must be a number between 0 and max_additions-1.
	 */
	private final static int  insertion_index = 2; 

	/**
	 * Method used for encrypting a string. The String passed through
	 * this method will be encrypted and returned as byte array.
	 * The string will go through a series of encryption and obsfuscation
	 * method in order to assure that the contents of the string are kept
	 * private static and secure.
	 *
	 * @param message String message which you wish to encrypt.
	 * @return Returns the string given string as an encrypted byte array.
	 */
	public static byte[] encrypt(String message){
		return encrypt(message,null,null,null);
	}
	/**
	 * Method used for encrypting a string. The String passed through
	 * this method will be encrypted and returned as byte array.
	 * The string will go through a series of encryption and obsfuscation
	 * method in order to assure that the contents of the string are kept
	 * private and secure.
	  * This method allows the input of a custom key set to be used
	 * by this program in replacement of the originals. The key must contain all letters of the
	 * swedish dictionary as well as a space character represented as
	 * a character with a space in between and the standard comma sign.
	 *
	 * <h1>Knowing the key used by this program is not enough in order
	 * to decode the content of the strings encrypted by this program!</h1>
	 * <p>
	 * @param message String message which you wish to encrypt.
	 * @param new_Key :The key to be passed as a new replacement key. The key set must
	 * be 44 characters long and cannot contain duplicates. If the key is
	 * not 44 characters long it will simply be ignore and not used. The key
	 * must contain the standard comma, period, question mark, exclamation mark
	 * as well as the space chararcter ' '.
	 * @return Returns the string given string as an encrypted byte array.
	 */
	public static byte[] encrypt(String message,char[]new_Key){
		return encrypt(message,null,new_Key,null);
	}
	/**
	 * Method used for encrypting a string. The String passed through
	 * this method will be encrypted and returned as byte array.
	 * The string will go through a series of encryption and obsfuscation
	 * method in order to assure that the contents of the string are kept
	 * private and secure.
	 * This method allows the input of a custom character set and a key set to be used
	 * by this program in replacement of the originals. The character sets passed through
	 * this constructor cannot contain any duplicates.
	 * <p>
	 * <h1>The character set and the key set must be
	 * of equal lenghts and neither the character set nor the key may have repeating elements.
	 * Every element found in the character set must be present in the key set and most preferably
	 * at a different index<h1>
	 * <p>
	 * <h1>Knowing either the chararcter set or the key used by this program doest not garantee
	 * the ability to decode the content of the strings encrypted by this program!</h1>
	 * <p>
	 * @param message String message which you wish to encrypt.
	 * @param new_CharSet : The new character set to be passed as a new chararcter set. The character
	 * set cannot contain duplicates.
	 * @param new_Key :The key to be passed as a new replacement key. The key set must contain
	 * all the characters present in the character set and cannot contain duplicates
	 * @return Returns the string given string as an encrypted byte array.
	 */
	public static byte[] encrypt(String message, char[] new_CharSet, char[]new_Key){
		return encrypt(message,new_CharSet,new_Key,null);
	}
	/**
	 * Method used for encrypting a string. The String passed through
	 * this method will be encrypted and returned as byte array.
	 * The string will go through a series of encryption and obsfuscation
	 * method in order to assure that the contents of the string are kept
	 * private and secure.
	 * This method allows the input of a custom character set, key set and Byte key to be used
	 * by this program in replacement of the originals. The character sets passed through
	 * this constructor cannot contain any duplicates.
	 * <p>
	 * <h1>The character set and the key set must be
	 * of equal lenghts and neither the character set nor the key may have repeating elements.
	 * Every element found in the character set must be present in the key set and most preferably
	 * at a different index<h1>
	 * <p>
	 * <h1>Knowing either the chararcter set or the key used by this program doest not garantee
	 * the ability to decode the content of the strings encrypted by this program!</h1>
	 * <p>
	 * The byte key must contain 10 unique digits with numbers
	 * from 0 to 9 in any desired order.
	 *
	 * <h1>Knowing the byte key used by this program is not enough in order
	 * to decode the content of the strings encrypted by this program!</h1>
	 * <p>
	 * @param message String message which you wish to encrypt.
	 * @param new_CharSet : The new character set to be passed as a new chararcter set. The character
	 * set cannot contain duplicates.
	 * @param new_Key :The key to be passed as a new replacement key. The key set must contain
	 * all the characters present in the character set and cannot contain duplicates
	 * @param new_byte_Key :The key to be passed as a new replacement key.
	 * The key set mus be 10 characters long and cannot contain duplicates.
	 * @return Returns the string given string as an encrypted byte array.
	 */
	public static byte[] encrypt(String message, char[] new_CharSet, char[]new_Key, String[] new_byte_Key){
		setCharset_setKey_setByteKey(new_CharSet,new_Key,new_byte_Key);

		String cypher = addRandom(message).toString();

		byte[] encryption = cypher.getBytes(format);

		byte[] swapped_Bytes = revertBytes(EncryptionUtils.toByteObject(encryption));

		return swapBytes(swapped_Bytes);
	}
	/**
	 * Method used for decrypting a string in the form
	 * of a encrypted byte array. The array will go through a series
	 * of decryption methods in order to uncover the orignal content
	 *
	 * @param encryption :byte array containing the encrypted string.
	 * @return :Returns a decypted string.
	 */
	public static String decrypt(byte[] encryption){
		return decrypt(encryption,null,null,null);
	}

	/**
	 * Method used for decrypting a string in the form
	 * of a encrypted byte array. The array will go through a series
	 * of decryption methods in order to uncover the orignal content.

	 * This method allows the input of a custom key set to be used
	 * by this program in replacement of the originals. The key must contain all letters of the
	 * swedish dictionary as well as a space character represented as
	 * a character with a space in between and the standard comma sign.
	 *
	 * <h1>Knowing the key used by this program is not enough in order
	 * to decode the content of the strings encrypted by this program!</h1>
	 * <p>
	 * @param encryption :byte array containing the encrypted string.
	 * @param new_Key :The key to be passed as a new replacement key. The key set must
	 * be 44 characters long and cannot contain duplicates. If the key is
	 * not 44 characters long it will simply be ignore and not used. The key
	 * must contain the standard comma, period, question mark, exclamation mark
	 * as well as the space chararcter ' '.
	 * @return Returns a decypted string.
	 */
	public static String decrypt(byte[] encryption, char[] new_Key){
		return decrypt(encryption,null,new_Key,null);
	}

	/**
	 * Method used for decrypting a string in the form
	 * of a encrypted byte array. The array will go through a series
	 * of decryption methods in order to uncover the orignal content.
	 *
	 * This method allows the input of a custom character set and a key set to be used
	 * by this program in replacement of the originals. The character sets passed through
	 * this constructor cannot contain any duplicates.
	 * <p>
	 * <h1>The character set and the key set must be
	 * of equal lenghts and neither the character set nor the key may have repeating elements.
	 * Every element found in the character set must be present in the key set and most preferably
	 * at a different index<h1>
	 * <p>
	 * <h1>Knowing either the chararcter set or the key used by this program doest not garantee
	 * the ability to decode the content of the strings encrypted by this program!</h1>
	 * <p>
	 * @param encryption :byte array containing the encrypted string.
	 * @param new_CharSet : The new character set to be passed as a new chararcter set. The character
	 * set cannot contain duplicates.
	 * @param new_Key :The key to be passed as a new replacement key. The key set must contain
	 * all the characters present in the character set and cannot contain duplicates
	 * @return Returns a decypted string.
	 */
	public static String decrypt(byte[] encryption, char[] new_CharSet, char[]new_Key){
		return decrypt(encryption,new_CharSet,new_Key,null);
	}
	/**
	 * Method used for decrypting a string in the form
	 * of a encrypted byte array. The array will go through a series
	 * of decryption methods in order to uncover the orignal content.
	 *
	 * This method allows the input of a custom character set, key set and Byte key to be used
	 * by this program in replacement of the originals. The character sets passed through
	 * this constructor cannot contain any duplicates.
	 * <p>
	 * <h1>The character set and the key set must be
	 * of equal lenghts and neither the character set nor the key may have repeating elements.
	 * Every element found in the character set must be present in the key set and most preferably
	 * at a different index<h1>
	 * <p>
	 * <h1>Knowing either the chararcter set or the key used by this program doest not garantee
	 * the ability to decode the content of the strings encrypted by this program!</h1>
	 * <p>
	 * The byte key must contain 10 unique digits with numbers
	 * from 0 to 9 in any desired order.
	 *
	 * <h1>Knowing the byte key used by this program is not enough in order
	 * to decode the content of the strings encrypted by this program!</h1>
	 * <p>
	 * @param encryption :byte array containing the encrypted string.
	 * @param new_CharSet : The new character set to be passed as a new chararcter set. The character
	 * set cannot contain duplicates.
	 * @param new_Key :The key to be passed as a new replacement key. The key set must contain
	 * all the characters present in the character set and cannot contain duplicates
	 * @param new_byte_Key :The key to be passed as a new replacement key.
	 * The key set mus be 10 characters long and cannot contain duplicates.
	 * @return Returns a decypted string.
	 */
	public static String decrypt(byte[] encryption, char[] new_CharSet, char[]new_Key, String[] new_byte_Key){
		setCharset_setKey_setByteKey(new_CharSet,new_Key,new_byte_Key);

		byte[] unswapped_Bytes = revertBytesBack(EncryptionUtils.toByteObject(unSwapBytes(encryption)));

		List<String> list  = EncryptionUtils.fromStringToList(new String(unswapped_Bytes, format),", ");

		EncryptedMessage unScrambled = removeRandom(list);

		String decyphered = revertSubstitution(unScrambled);

		return decyphered;
	}

	private static void setCharset_setKey_setByteKey(char[] new_CharSet, char[]new_Key, String[] new_byte_Key){
		if(new_Key!=null && new_CharSet!=null && new_Key.length == new_CharSet.length){
			key_Map.clear();
			plain_Map.clear();
			for(int i = 0; i<new_CharSet.length; i++){
				key_Map.put(new_Key[i],new_CharSet[i]);
				plain_Map.put(new_CharSet[i], new_Key[i]);
			}
		}
		else if(new_Key!=null && new_CharSet==null && new_Key.length==plain.length){
			key_Map.clear();
			for(int i = 0; i<key.length; i++){
				key_Map.put(new_Key[i],plain[i]);
			}
		}
		if(new_byte_Key!=null && new_byte_Key.length==byte_Plain.length){
			key_Byte_Map.clear();
			for(int i = 0; i<byte_Plain.length; i++){
				key_Byte_Map.put(new_byte_Key[i],byte_Plain[i]);
			}
		}
	}
	/*
	 * Method used to swapp the elements of the input array.
	 * The elements will be swapped using 4 nested loops with
	 * different swap indexes. The elements are swapped around
	 * millions of times
	 */
	private static char[] swapCharacters(char[] chars){
		for(int i1 = 0; i1<chars.length-swap_index_1; i1++){
			char temp = chars[i1];
			chars[i1] = chars[i1+swap_index_1];
			chars[i1+swap_index_1] = temp;

			for(int i2 = 0; i2<chars.length-swap_index_2; i2++){
				char temp2 = chars[i2];
				chars[i2] = chars[i2+swap_index_2];
				chars[i2+swap_index_2] = temp2;

				for(int i3 = 0; i3<chars.length-swap_index_3; i3++){
					char temp3 = chars[i3];
					chars[i3] = chars[i3+swap_index_3];
					chars[i3+swap_index_3] = temp3;

					for(int i4 = swap_index_4; i4<chars.length; i4++){
						char temp4 = chars[i4];
						chars[i4] = chars[i4-swap_index_4];
						chars[i4-swap_index_4] = temp4;
					}
				}
			}
		}
		return chars;
	}
	/*
	 * Method used to swapp the elements which were previously swapped
	 * by the swapCharacters method back to their original index. The swap
	 */
	private static char[] revertCharacterSwap(char[] chars) {
		for (int i1 = chars.length - (swap_index_1+1); i1 >= 0; i1--) {

			for (int i2 = chars.length - (swap_index_2+1); i2 >= 0; i2--) {

				for (int i3 = chars.length - (swap_index_3+1); i3 >= 0; i3--) {

					for (int i4 = chars.length - 1; i4 >= (swap_index_4); i4--) {
						char temp4 = chars[i4];
						chars[i4] = chars[i4 - swap_index_4];
						chars[i4 - swap_index_4] = temp4;
					}

					char temp3 = chars[i3];
					chars[i3] = chars[i3 + swap_index_3];
					chars[i3 + swap_index_3] = temp3;
				}

				char temp2 = chars[i2];
				chars[i2] = chars[i2 + swap_index_2];
				chars[i2 + swap_index_2] = temp2;
			}

			char temp = chars[i1];
			chars[i1] = chars[i1 + swap_index_1];
			chars[i1 + swap_index_1] = temp;
		}
		return chars;
	}
	/*
	 * Method which swaps the elements match to the character set
	 * whith the correspondnt key element. The method will also create
	 * an array holing information about the case of each character. The method
	 * will also perform a character swap with a call to the swapCharacters method. After
	 * the process is completed this method will return a set of characters holding the
	 * coded message along with a set of characters holding case information. The
	 * two are divided by a special sequence of symbols in order to distinguish the
	 * the two. The return message will then be passed further for further encryption.
	 */
	private final static EncryptedMessage applySubstitution(char[] message) {

		char[] case_Binary = new char[message.length];
		char[] code_Message = new char[message.length];

		for(int i = 0; i < message.length; i++) {
			if (Character.isUpperCase(message[i])) {
				case_Binary[i] = '1';
			}
			if (Character.isLowerCase(message[i])) {
				case_Binary[i] = '0';
			}
			code_Message[i] = Character.toUpperCase(message[i]);
			if(plain_Map.containsKey(code_Message[i])){
				code_Message[i] = plain_Map.get(code_Message[i]);
			}
		}
		return new EncryptedMessage(swapCharacters(code_Message),case_Binary);
	}

	/*
	 * Method which reverts the character substitution performed by the
	 * applySubstitution. It does this by reverting the pattern in which the substitution
	 * was made. This will separate and analyze the message along with the case data
	 * and once this is done it will also peform a call to the revertCharacterSwap method
	 * in order to also unswap the order of the characters to their original state.
	 * Once the substitution and the character swap has been reversed it wil further
	 * analyze case data determine the case of each character. Upon completion it
	 * will return the decrypted message.
	 */
	private final static String revertSubstitution(EncryptedMessage message) {

		char[] code_Message = revertCharacterSwap(message.getCharMessage());
		char[] case_Message = message.getCharCase();

		for (int i = 0; i < code_Message.length; i++) {
			if(key_Map.containsKey(code_Message[i])){
				code_Message[i] = key_Map.get(code_Message[i]);
			}
			if (case_Message[i] == '1') {
				code_Message[i] = Character.toUpperCase(code_Message[i]);
			}
			if (case_Message[i] == '0') {
				code_Message[i] = Character.toLowerCase(code_Message[i]);
			}
		}

		return String.valueOf(code_Message);
	}

	/*
	 * Method used to further increases the obsfuscation level of the message by adding
	 * random numbers and characters to the body of the already encrypted message. This
	 * is done at key places of the message in order to allow the substraction of the oginal
	 * version of the encryption witout having to deal with the extrac characters and numbers
	 * added by this function. The function a
	 * the process can then be reversed with an additional key.

	 */
	private final static LinkedList<String> addRandom(String code) {
		LinkedList<String> cypherList = new LinkedList<>();
		EncryptedMessage case_and_code = applySubstitution(code.toCharArray());

		String code_message = case_and_code.getMessage();
		String code_case = case_and_code.getCase();

		for (int index = 0; index < code_message.length(); index++) {
			cypherList.add(EncryptionUtils.getRandomString(max_additions,code_message.charAt(index),insertion_index));

		}
		
		cypherList.addFirst("" + (EncryptionUtils.getRandomInterval(100,999)));
		cypherList.add(String.valueOf(code_case) + EncryptionUtils.getRandom(10));

		return cypherList;


	}
	/*
	 * Method used to remove all previously added obsfuscation elements. The method
	 * will loop through the values of a given list and it will filter the orignal's
	 * message values into their respective char arrays, one holding the code and the
	 * othe holding the case related data.
	 */
	private final static EncryptedMessage removeRandom(List<String> cypher_List) {

		StringBuilder string_Builder = new StringBuilder();

		char[] case_message = new char[cypher_List.size() - 2];
		char[] code_message = new char[cypher_List.size() - 1];

		for (int index = 0; index < cypher_List.size() - 1; index++) {
			if (index >= 1)
				string_Builder.append(String.valueOf(cypher_List.get(index).toCharArray()[insertion_index]));
			if (index < case_message.length)
				case_message[index] = cypher_List.get(cypher_List.size() - 1).toCharArray()[index];
		}
		
		code_message = string_Builder.toString().toCharArray();

		return new EncryptedMessage(code_message, case_message);
	}

	/*
	 * Method used to further encrypt the message by replacing the
	 * first first digit of every value on every index of the byte array
	 * except for indexes that hold a negative value. This method converts
	 * the byte value to string which is than formated and casted back to
	 * a byte. A modification can be perform that may allow each index to
	 * be replace using a numerical value check!
	 */
	private final static Byte[] replaceBytes(Byte[] bytes) {
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
	/*
	 * Method used to revert the previously replaced bytes back to
	 * to normal. The byte array will go throug an identical procedure
	 * as the one performed in the replace bytes method in order to
	 * give each byte index the original value of the first index.
	 */
	private final static Byte[] revertByteReplacement(Byte[] bytes) {
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
	/*
	 * Method used to swapp the elements of the input array.
	 * The elements will be swapped using 3 nested loops with
	 * different swap indexes. The elements are swapped around
	 * millions of times.
	 */
	private final static byte[] swapBytes(byte[] bytes){
		for(int i1 = 0; i1<bytes.length-swap_index_1; i1++){
			byte temp = bytes[i1];
			bytes[i1] = bytes[i1+swap_index_1];
			bytes[i1+swap_index_1] = temp;

			for(int i2 = 0; i2<bytes.length-swap_index_2; i2++){
				byte temp2 = bytes[i2];
				bytes[i2] = bytes[i2+swap_index_2];
				bytes[i2+swap_index_2] = temp2;

				for(int i3 = swap_index_4; i3<bytes.length; i3++){
					byte temp3 = bytes[i3];
					bytes[i3] = bytes[i3-swap_index_4];
					bytes[i3-swap_index_4] = temp3;
				}
			}
		}
		return bytes;
	}
	/*
	 * Method used to swapp the elements which were previously swapped
	 * by the swapBytes method back to their original index. The swap
	 */
	private final static byte[] unSwapBytes(byte[] bytes){
		for (int i1 = bytes.length - (swap_index_1+1); i1 >= 0; i1--) {
			for (int i2 = bytes.length - (swap_index_2+1); i2 >= 0; i2--) {
				for (int i3 = bytes.length - 1; i3 >= (swap_index_4); i3--) {
					byte temp3 = bytes[i3];
					bytes[i3] = bytes[i3 - swap_index_4];
					bytes[i3 - swap_index_4] = temp3;
				}
				byte temp2 = bytes[i2];
				bytes[i2] = bytes[i2 + swap_index_2];
				bytes[i2 + swap_index_2] = temp2;
			}
			byte temp = bytes[i1];
			bytes[i1] = bytes[i1 + swap_index_1];
			bytes[i1 + swap_index_1] = temp;
		}
		return bytes;
	}
	/**
	 * Reverses the byte array for further increase obfuscation.
	 */
	private final static byte[] revertBytes(Byte[] encryption){
		Byte[] byte_Array = encryption;

		List<Byte> byte_List = Arrays.asList(byte_Array);

		Collections.reverse(byte_List);

		return EncryptionUtils.toPrimitives(replaceBytes(byte_Array));
	}
	/**
	 * Reverses the byte array to its original state
	 */
	private final static byte[] revertBytesBack(Byte[] encryption){
		Byte[] byte_Array = encryption;

		List<Byte> byte_List = Arrays.asList(byte_Array);

		Collections.reverse(byte_List);

		return EncryptionUtils.toPrimitives(revertByteReplacement(byte_Array));
	}
	/**
	 * <h1>Encrypted Message</h1>
	 *
	 * This class is used as a wrapper containing both
	 * the code of the actual string and case data of
	 * said string.
	 *
	 * @author Eudy Contreras
	 * @version 1.1
	 * @since 2016-08-16
	 */
	private static class EncryptedMessage{
		private final String code_message;
		private final String case_message;

		public EncryptedMessage(char[] codeX, char[] caseX){
			this.code_message = String.valueOf(codeX);
			this.case_message = String.valueOf(caseX);
		}
		public final String getMessage(){
			return code_message;
		}
		public final String getCase(){
			return case_message;
		}
		public final char[] getCharMessage(){
			return code_message.toCharArray();
		}
		public final char[] getCharCase(){
			return case_message.toCharArray();
		}
	}
	/**
	 * <h1>Encryption Utility</h1>
	 *
	 * This class contains various functions used by the program.
	 *
	 * @author Eudy Contreras
	 * @version 1.1
	 * @since 2016-08-16
	 */
	private static class EncryptionUtils{
		/*
		 * Method which converts an Object byte array to a primitive type.
		 */
		private static byte[] toPrimitives(Byte[] byte_Object){

			byte[] bytes = new byte[byte_Object.length];

			int i = 0;
			for(Byte byte_Objects : byte_Object)bytes[i++] = byte_Objects;

			return bytes;
		}
		/*
		 * Method which converts a primitive byte array to an object byte array.
		 */
		private static Byte[] toByteObject(byte[] byte_prime) {
			Byte[] bytes = new Byte[byte_prime.length];

			int i = 0;
			for (byte byte_Primes : byte_prime) bytes[i++] = byte_Primes;

			return bytes;
		}
		/*
		 * Method which prints the content of a byte array.
		 */
		private static String printBytes(byte[] binaryEncription){
			return new String(binaryEncription, format);
		}
		/*
		 * Method which splits a string at a given character sequence
		 * and returns List made out of all the sections.
		 */
		private static List<String> fromStringToList(String list, String sequence) {
			return Arrays.asList(list.split(sequence));
		}
		/*
		 * Method which generates a secure random within a
		 * given range.
		 */
		private static int getRandom(int value){
			SecureRandom rand = new SecureRandom();
			return rand.nextInt(value);
		}
		/*
		 * Method which generates a secure random within a
		 * given interval.
		 */
		private static int getRandomInterval(int minValue, int maxValue) {
			SecureRandom rand = new SecureRandom();
			return rand.nextInt(maxValue + 1 - minValue) + minValue;
		}
		/*
		 * Method which returns a random string of the specified
		 * lenght containing a non random element located at the given index. 
		 */
		private static String getRandomString(int length, char code, int index){
			char[] randomString = new char[length];
			for(int i = 0;i<length; i++){
				randomString[i] =added[getRandom(added.length)];
				if(i==index){
					randomString[i] = code;
				}
			}
			return String.valueOf(randomString);
		}
		/*
		 * Method which returns a radom string of the specified
		 * lenght.
		 */
		@SuppressWarnings("unused")
		private static String getRandomString(int length){
			char[] randomString = new char[length];
			for(int i = 0;i<length; i++){
				randomString[i] =added[getRandom(added.length)];
			}
			return String.valueOf(randomString);
		}

	}
	public static void main(String[] args) {
//		EncryptionUtils.getRandomString(10,'Ö',1);
		long encrypt_start_time = System.currentTimeMillis();
		byte[] encryption = StringEncrypter.encrypt("What is up!!, Not much why??");
		long encrypt_end_time = System.currentTimeMillis();
		System.out.println("Encryption speed: "+(encrypt_end_time - encrypt_start_time)+ " Milliseconds");

		System.out.println("Actual encrypted message:");
		System.out.println("////////////////////////////////////////////////////////////////////////////////");
		System.out.println("");
		System.out.println(EncryptionUtils.printBytes(encryption));
		System.out.println("");
		System.out.println("////////////////////////////////////////////////////////////////////////////////");

//		System.out.println(Arrays.toString(encryption));
		long decrypt_start_time = System.currentTimeMillis();
		System.out.println(StringEncrypter.decrypt(encryption));
		long decrypt_end_time = System.currentTimeMillis();
		System.out.println("Decryption speed: "+(decrypt_end_time - decrypt_start_time)+ " Milliseconds");
	}
}