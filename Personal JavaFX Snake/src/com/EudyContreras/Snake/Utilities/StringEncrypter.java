package com.EudyContreras.Snake.Utilities;

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;


/**
 * <h1>String Encrypter</h1>
 *
 * This programs allows you encrypt a String and store as a byte array.
 * the program provides a simple encryption set of methods which make use of
 * scrambling/adding/replacing/swapping/ the core structure of the String.
 * The encrypted message is the store as byte array object which can be sent through sockets
 * or stored locally. In order to be able to view the content of the String the object
 * must be passed through the decrypt method of this program which will format and reconstruc the
 * String to it's original state.
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

    private static char[] added  = {'L','3','5','G','0','Ä','1','0',
    							    'A','D','9','Å','N','C','0','Y',
    							    'W','S','8','Ö','4','V','4','1',
    							    'Q','7','K','6','O','3','6','X',
    							    '8','3','2','9','0','5','7'};
    /*
     * Meps which hold the encryption base and key data structures.
     */
    private static final Map<Character, Character> PLAIN_MAP = new HashMap<Character, Character>();
    private static final Map<Character, Character> KEY_MAP = new HashMap<Character, Character>();

    private static final Map<String, String> PLAIN_BYTE_MAP = new HashMap<String, String>();
    private static final Map<String, String> PLAIN_KEY_MAP = new HashMap<String, String>();

    /**
     * This values determined the swap indexes at which the swapping methods
     * will operate. More swapp indexes may be added for increase security.
     */
    private final static int  SWAP_INDEX_1 = 2;
    private final static int  SWAP_INDEX_2 = 6;
    private final static int  SWAP_INDEX_3 = 4;
    private final static int  SWAP_INDEX_4 = 3;

    /*
     * The higher the number the more random characters will be added to the encryption.
     * The number set here will affect the performance of the application. The higher the
     * number the lower the performance will be but the more populated with random characters
     * he encryption will be.
     * HIgher = more secure but slower.
     * Lower = less secure but faster.
     * recommended: 6
     */
    private static int  max_decoys = 5;

    /*
     * The index in which the actually code character will be inserted.
     * the index must be a number between 0 and the max_decoys value-1.
     */
    private static int  insertion_index = 2;

    /**
     * Method used for encrypting a String. The String passed through
     * this method will be encrypted and returned as byte array.
     * The String will go through a series of encryption and obfuscation
     * methods in order to assure that the contents of the String are kept
     * private and secure.
     * This method allows the input of a custom password to be associated with the
     * generated key. The password will be needed for decryption of Strings
     * which were encrypted using the submitted password.
     * <p>
     * @param message String message which you wish to encrypt.
     * @param password :The password to be associated with the encryption. The password must be at least 6 characters long.
     * The password must be at least 6 characters long.
     * @return Returns the String given String as an encrypted byte array.
     */
    public static byte[] encrypt(String message,Password password){
        return encrypt(message,password,null, null);
    }

    /**
     * Method used for encrypting a String. The String passed through
     * this method will be encrypted and returned as byte array.
     * The String will go through a series of encryption and obfuscation
     * method in order to assure that the contents of the String are kept
     * private and secure.
     * This method allows the input of a custom character set and a password
     * which are to be associated with the generated key. both the password and
     * the submitted character set will be needed for decryption of Strings which were encrypted using the
     * submitted password and character set.
     * <p>
     * @param message String message which you wish to encrypt.
     * @param password :The password to be associated with the encryption. The password must be at least 6 characters long.
     * @param new_CharSet :The character set used by the encrypted String. The character
     * set cannot contain any duplicates and must be at least 40 characters long.
     * @return Returns the String given String as an encrypted byte array.
     */
    public static byte[] encrypt(String message, Password password, char[] new_CharSet){
    	return encrypt(message,password,new_CharSet, null);
    }
    /**
     * Method used for encrypting a String. The String passed through
     * this method will be encrypted and returned as byte array.
     * The String will go through a series of encryption and obfuscation
     * method in order to assure that the contents of the String are kept
     * private and secure.
     * This method allows the input of a custom character set, a password and the security level
     * which is to be associated with the generated key. The password, the security level and
     * the submitted character set will be needed for decryption of Strings which were encrypted using the
     * submitted password, character set and security level.
     * <p>
     * @param message String message which you wish to encrypt.
     * @param password :The password to be associated with the encryption. The password must be at least 6 characters long.
     * @param new_CharSet :The character set used by the encrypted String. The character
     * set cannot contain any duplicates and must be at least 40 characters long.
     * @param security :The level of security which is to be assign to the encrypted message. The higher the security level
     * the higher the level of encryption but also the slower the encryption process will be. The lower the security level
     * the lower the encryption but the faster the encryption will be.
     * @return Returns the String given String as an encrypted byte array.
     */
    public static byte[] encrypt(String message, Password password, char[] new_CharSet, SecurityLevel security){

        if(prepareEncryption(new_CharSet, password, security)){

        String cypher = addRandom(message).toString();

        byte[] encryption = cypher.getBytes(StandardCharsets.UTF_8);

        byte[] swapped_Bytes = revertBytes(EncryptionUtils.toByteObject(encryption));

        return swapBytes(swapped_Bytes);

        }else{
            return null;
        }
    }
    /**
     * Method used for decrypting a String in the form
     * of a encrypted byte array. The array will go through a series
     * of decryption methods in order to uncover the original content.
     * This method allows the input of the password associated with
     * the encrypted file which is needed in order to be able to view the file.
     * <p>
     * @param encryption :byte array containing the encrypted String.
     * @param password :The password associated to the file. Needed in order
     * to view the file! The password must match the password used to encrypt the file
     * @return :Returns a decypted String.
     */
    public static String decrypt(byte[] encryption, Password password){
        return decrypt(encryption,password,null,null);
    }
    /**
     * Method used for decrypting a String in the form
     * of a encrypted byte array. The array will go through a series
     * of decryption methods in order to uncover the original content.
     *
     * This method allows the input of the character set and the password
     * associated with the encrypted file. Both the character set and the password
     * are needed in order to view the original String content
     * <p>
     * @param encryption :byte array containing the encrypted String.
     * @param password :The password associated to the file. Needed in order
     * to view the file! The password must match the password used to encrypt the file
     * @param new_CharSet :The character set associated with the encrypted file.
     * needed in order to view the file! Must match the character set used to encrypt the String
     * @return Returns a decypted String.
     */
    public static String decrypt(byte[] encryption, Password password, char[] new_CharSet){
    	 return decrypt(encryption,password,new_CharSet,null);
    }
    /**
     * Method used for decrypting a String in the form
     * of a encrypted byte array. The array will go through a series
     * of decryption methods in order to uncover the original content.
     *
     * This method allows the input of the character set, the password and the security level
     * associated with the encrypted file. All of the inputs are needed in order to view the original
     * content of the file.
     * <p>
     * @param encryption :byte array containing the encrypted String.
     * @param password :The password associated to the file. Needed in order
     * to view the file! The password must match the password used to encrypt the String.
     * @param new_CharSet :The character set associated with the encrypted file.Must match the character
     * set used to encrypt the String.
     * @param security :The security level which is associated with the encrypted String.Muse match
     * the security level used to encrypt the String.
     * @return Returns a decypted String.
     */
    public static String decrypt(byte[] encryption, Password password, char[] new_CharSet, SecurityLevel security){
        if(prepareEncryption(new_CharSet, password, security)){

        byte[] unswapped_Bytes = revertBytesBack(EncryptionUtils.toByteObject(unSwapBytes(encryption)));

        List<String> list  = EncryptionUtils.fromStringToList(new String(unswapped_Bytes, StandardCharsets.UTF_8),", ");

        EncryptedMessage unScrambled = removeRandom(list);

        String decyphered = revertSubstitution(unScrambled);

        return decyphered;
        }
        else{
            return "";
        }
    }
    /*
     * Method in charged of processing the encryption and decryption request made
     * by the user. This method will perform checks on the validity of the password and the character
     * set if any. If both the password and the character set are valid the method will then generate a key
     * and allow the encryption to proceed further.
     */
    private static boolean prepareEncryption(char[] new_CharSet, Password password, SecurityLevel security){
        boolean duplicates = false;
        String[] byte_Plain =
            {"1","2","3","4","5","6","7","8","9","0"};

        char[] plain_char_set =
            {'A','B','C','D','E','F','G','H',
             'I','J','K','L','M','N','O','P',
             'Q','R','S','T','U','V','W','X',
             'Y','Z','Ö','Ä','Å','0','1','2',
             '3','4','5','6','7','8','9',' ',
             ',','?','.','!'};

        if (password.isPasswordOk() && new_CharSet != null) {
            duplicates = EncryptionUtils.containsDuplicates(new_CharSet) ;
            if(!duplicates && new_CharSet.length>=40) {
                KeyGenerator keyGen = new KeyGenerator(password.getPassword(),new_CharSet, byte_Plain);
                setSecurityLevel(password.getPassword(), security);
                KEY_MAP.clear();
                PLAIN_MAP.clear();
                PLAIN_KEY_MAP.clear();
                PLAIN_BYTE_MAP.clear();
                for (int i = 0; i < new_CharSet.length; i++) {
                    PLAIN_MAP.put(keyGen.getBase_key()[i], keyGen.getNew_Key()[i]);
                    KEY_MAP.put(keyGen.getNew_Key()[i], keyGen.getBase_key()[i]);
                }
                for (int i = 0; i < byte_Plain.length; i++) {
                    PLAIN_BYTE_MAP.put(keyGen.getBase_Byte()[i], keyGen.getNew_Byte()[i]);
                    PLAIN_KEY_MAP.put(keyGen.getNew_Byte()[i], keyGen.getBase_Byte()[i]);
                }
                return true;
            }
            else if(duplicates && new_CharSet.length>=40){
                new InvalidCharacterSetException("The submitted character set contains duplicates!");

                return false;
            }
            else if(!duplicates && new_CharSet.length<40){
                new InvalidCharacterSetException("The submitted character set does not meet the character amount specification!");

                return false;
            }
            else{
                new InvalidCharacterSetException("The submitted character set contains less than then minimum allow amount of characters!");

                return false;
            }

        }
        else if (password.isPasswordOk() && new_CharSet == null) {
        	KeyGenerator keyGen = new KeyGenerator(password.getPassword(),plain_char_set, byte_Plain);
        	setSecurityLevel(password.getPassword(), security);
            KEY_MAP.clear();
            PLAIN_MAP.clear();
            PLAIN_KEY_MAP.clear();
            PLAIN_BYTE_MAP.clear();
            for (int i = 0; i < plain_char_set.length; i++) {
            	 PLAIN_MAP.put(keyGen.getBase_key()[i], keyGen.getNew_Key()[i]);
                 KEY_MAP.put(keyGen.getNew_Key()[i], keyGen.getBase_key()[i]);
            }
            for (int i = 0; i < byte_Plain.length; i++) {
                PLAIN_BYTE_MAP.put(keyGen.getBase_Byte()[i], keyGen.getNew_Byte()[i]);
                PLAIN_KEY_MAP.put(keyGen.getNew_Byte()[i], keyGen.getBase_Byte()[i]);
            }
            return true;
        } else {
            	new InvalidPasswordException("The submitted password is not valid");
            return false;
        }

    }
    /*
     * Method used to swap the elements of the input array.
     * The elements will be swapped using 4 nested loops with
     * different swap indexes. The elements are swapped around
     * millions of times
     */
    public static char[] swapCharacters(char[] chars){
        for(int i1 = 0; i1<chars.length-SWAP_INDEX_1; i1++){
            char temp = chars[i1];
            chars[i1] = chars[i1+SWAP_INDEX_1];
            chars[i1+SWAP_INDEX_1] = temp;

            for(int i2 = 0; i2<chars.length-SWAP_INDEX_2; i2++){
                char temp2 = chars[i2];
                chars[i2] = chars[i2+SWAP_INDEX_2];
                chars[i2+SWAP_INDEX_2] = temp2;

                for(int i3 = 0; i3<chars.length-SWAP_INDEX_3; i3++){
                    char temp3 = chars[i3];
                    chars[i3] = chars[i3+SWAP_INDEX_3];
                    chars[i3+SWAP_INDEX_3] = temp3;

                    for(int i4 = SWAP_INDEX_4; i4<chars.length; i4++){
                        char temp4 = chars[i4];
                        chars[i4] = chars[i4-SWAP_INDEX_4];
                        chars[i4-SWAP_INDEX_4] = temp4;
                    }
                }
            }
        }
        return chars;
    }
    /*
     * Method used to swap the elements which were previously swapped
     * by the swapCharacters method back to their original index. The swap
     */
    private static char[] revertCharacterSwap(char[] chars) {
        for (int i1 = chars.length - (SWAP_INDEX_1+1); i1 >= 0; i1--) {

            for (int i2 = chars.length - (SWAP_INDEX_2+1); i2 >= 0; i2--) {

                for (int i3 = chars.length - (SWAP_INDEX_3+1); i3 >= 0; i3--) {

                    for (int i4 = chars.length - 1; i4 >= (SWAP_INDEX_4); i4--) {
                        char temp4 = chars[i4];
                        chars[i4] = chars[i4 - SWAP_INDEX_4];
                        chars[i4 - SWAP_INDEX_4] = temp4;
                    }

                    char temp3 = chars[i3];
                    chars[i3] = chars[i3 + SWAP_INDEX_3];
                    chars[i3 + SWAP_INDEX_3] = temp3;
                }

                char temp2 = chars[i2];
                chars[i2] = chars[i2 + SWAP_INDEX_2];
                chars[i2 + SWAP_INDEX_2] = temp2;
            }

            char temp = chars[i1];
            chars[i1] = chars[i1 + SWAP_INDEX_1];
            chars[i1 + SWAP_INDEX_1] = temp;
        }
        return chars;
    }
    /*
     * Method which swaps the elements match to the character set
     * with the corresponding key element. The method will also create
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
            if(PLAIN_MAP.containsKey(code_Message[i])){
                code_Message[i] = PLAIN_MAP.get(code_Message[i]);
            }
        }
        return new EncryptedMessage(swapCharacters(code_Message),case_Binary);
    }

    /*
     * Method which reverts the character substitution performed by the
     * applySubstitution. It does this by reverting the pattern in which the substitution
     * was made. This will separate and analyze the message along with the case data
     * and once this is done it will also perform a call to the revertCharacterSwap method
     * in order to also unswap the order of the characters to their original state.
     * Once the substitution and the character swap has been reversed it will further
     * analyze case data determine the case of each character. Upon completion it
     * will return the decrypted message.
     */
    private final static String revertSubstitution(EncryptedMessage message) {

        char[] code_Message = revertCharacterSwap(message.getCharMessage());
        char[] case_Message = message.getCharCase();

        for (int i = 0; i < code_Message.length; i++) {
            if(KEY_MAP.containsKey(code_Message[i])){
                code_Message[i] = KEY_MAP.get(code_Message[i]);
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
     * Method used to further increases the obfuscation level of the message by adding
     * random numbers and characters to the body of the already encrypted message. This
     * is done at key places of the message in order to allow the subtraction of the original
     * version of the encryption without having to deal with the extract characters and numbers
     * added by this function. The function a
     * the process can then be reversed with an additional key.

     */
    private final static LinkedList<String> addRandom(String code) {

        LinkedList<String> cypherList = new LinkedList<>();
        EncryptedMessage case_and_code = applySubstitution(code.toCharArray());

        String code_message = case_and_code.getMessage();
        String code_case = case_and_code.getCase();

        for (int index = 0; index < code_message.length(); index++) {
            cypherList.add(EncryptionUtils.getRandomString(max_decoys,code_message.charAt(index),insertion_index));
        }

        cypherList.addFirst("" + (EncryptionUtils.getRandomInterval(100,999)));
        cypherList.addLast(code_case + EncryptionUtils.getRandomInterval(100,999)+EncryptionUtils.getRandomString(2));

        return cypherList;


    }
    /*
     * Method used to remove all previously added obfuscation elements. The method
     * will loop through the values of a given list and it will filter the orignal's
     * message values into their respective char arrays, one holding the code and the
     * other holding the case related data.
     */
    private final static EncryptedMessage removeRandom(List<String> cypher_List) {

        StringBuilder string_Builder = new StringBuilder();

        char[] case_message = new char[cypher_List.get(cypher_List.size()-1).length()-5];
        char[] code_message = new char[cypher_List.size()-1];

        for (int index = 0; index < cypher_List.size() - 1; index++) {
            if (index >= 1){
            	try{
                string_Builder.append(String.valueOf(cypher_List.get(index).toCharArray()[insertion_index]));
            	}catch(ArrayIndexOutOfBoundsException e){ /*TODO NOTHING*/}
            }
            if (index < case_message.length){
                case_message[index] = cypher_List.get(cypher_List.size() - 1).toCharArray()[index];
            }
        }

        code_message = string_Builder.toString().toCharArray();

        return new EncryptedMessage(code_message, case_message);
    }

    /*
     * Method used to further encrypt the message by replacing the
     * first first digit of every value on every index of the byte array
     * except for indexes that hold a negative value. This method converts
     * the byte value to String which is than formated and casted back to
     * a byte. A modification can be perform that may allow each index to
     * be replace using a numerical value check!
     */
    private final static Byte[] replaceBytes(Byte[] bytes) {
        Byte[] temp_Bytes = Arrays.copyOf(bytes, bytes.length);

        for (int i = 0; i < temp_Bytes.length; i++) {
            if (temp_Bytes[i]>=0) {
                String temp_String = String.valueOf(temp_Bytes[i]);
                String new_Value = PLAIN_BYTE_MAP.get(Integer.toString(temp_Bytes[i]).substring(0, 1))+ temp_String.substring(1);
                temp_Bytes[i] = Byte.valueOf(new_Value);
            }
        }
        return temp_Bytes;
    }
    /*
     * Method used to revert the previously replaced bytes back to
     * to normal. The byte array will go through an identical procedure
     * as the one performed in the replace bytes method in order to
     * give each byte index the original value of the first index.
     */
    private final static Byte[] revertByteReplacement(Byte[] bytes) {
        Byte[] temp_Bytes = Arrays.copyOf(bytes, bytes.length);

        for (int i = 0; i < temp_Bytes.length; i++) {
            if (temp_Bytes[i]>=0) {
                String temp_String = String.valueOf(temp_Bytes[i]);
                String new_Value = PLAIN_KEY_MAP.get(Integer.toString(temp_Bytes[i]).substring(0, 1))+ temp_String.substring(1);
                temp_Bytes[i] = Byte.valueOf(new_Value);
            }
        }
        return temp_Bytes;
    }
    /*
     * Method used to swap the elements of the input array.
     * The elements will be swapped using 3 nested loops with
     * different swap indexes. The elements are swapped around
     * millions of times.
     */
    private final static byte[] swapBytes(byte[] bytes){
        for(int i1 = 0; i1<bytes.length-SWAP_INDEX_1; i1++){
            byte temp = bytes[i1];
            bytes[i1] = bytes[i1+SWAP_INDEX_1];
            bytes[i1+SWAP_INDEX_1] = temp;

            for(int i2 = 0; i2<bytes.length-SWAP_INDEX_2; i2++){
                byte temp2 = bytes[i2];
                bytes[i2] = bytes[i2+SWAP_INDEX_2];
                bytes[i2+SWAP_INDEX_2] = temp2;

                for(int i3 = SWAP_INDEX_4; i3<bytes.length; i3++){
                    byte temp3 = bytes[i3];
                    bytes[i3] = bytes[i3-SWAP_INDEX_4];
                    bytes[i3-SWAP_INDEX_4] = temp3;
                }
            }
        }
        return bytes;
    }
    /*
     * Method used to swap the elements which were previously swapped
     * by the swapBytes method back to their original index. The swap
     */
    private final static byte[] unSwapBytes(byte[] bytes){
        for (int i1 = bytes.length - (SWAP_INDEX_1+1); i1 >= 0; i1--) {
            for (int i2 = bytes.length - (SWAP_INDEX_2+1); i2 >= 0; i2--) {
                for (int i3 = bytes.length - 1; i3 >= (SWAP_INDEX_4); i3--) {
                    byte temp3 = bytes[i3];
                    bytes[i3] = bytes[i3 - SWAP_INDEX_4];
                    bytes[i3 - SWAP_INDEX_4] = temp3;
                }
                byte temp2 = bytes[i2];
                bytes[i2] = bytes[i2 + SWAP_INDEX_2];
                bytes[i2 + SWAP_INDEX_2] = temp2;
            }
            byte temp = bytes[i1];
            bytes[i1] = bytes[i1 + SWAP_INDEX_1];
            bytes[i1 + SWAP_INDEX_1] = temp;
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
    /*
     * Method which checks the security level required by the user
     * and base on the specifications it will adjust the amount of characters per character
     * which are to be added to the encrypted message. The higher the security level the
     * higher the number of added characters and vice versa.
     */
    private final static void setSecurityLevel(String password, SecurityLevel security){
    	if(security!=null){
    		switch(security){
			case LOW:
				max_decoys = 1;
				insertion_index = 0;
				break;
			case MEDIUM:
				max_decoys = 4;
				insertion_index = 3;
				break;
			case HIGH:
				max_decoys = 8;
				insertion_index = 2;
				break;
			case VERY_HIGH:
				max_decoys = 16;
				insertion_index = 4;
				break;
			default:
				break;
    		}
    	}
    }
    /**
     * <h1>Encrypted Message</h1>
     *
     * This class is used as a wrapper containing both
     * the code of the actual String and case data of
     * said String.
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
        private final String getMessage(){
            return code_message;
        }
        private final String getCase(){
            return case_message;
        }
        private final char[] getCharMessage(){
            return code_message.toCharArray();
        }
        private final char[] getCharCase(){
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
    public static class EncryptionUtils{
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
         * Method which converts an Object byte array to a primitive type.
         */
        @SuppressWarnings("unused")
        private static char[] toCharPrimitive(Character[] char_Object){

            char[] chars = new char[char_Object.length];

            int i = 0;
            for(Character char_Objects : char_Object)chars[i++] = char_Objects;

            return chars;
        }
        /*
         * Method which converts a primitive byte array to an object byte array.
         */
        @SuppressWarnings("unused")
        private static Character[] toCharObject(char[] char_prime) {
            Character[] chars = new Character[char_prime.length];

            int i = 0;
            for (char chars_Primes : char_prime) chars[i++] = chars_Primes;

            return chars;
        }
        /*
         * Method which prints the content of a byte array.
         */
        public static void printBytes(byte[] byteEncryption){
            if(byteEncryption!=null){
            	System.out.println(Base64.encode(byteEncryption));
            }
        }
        /*
         * Method which splits a String at a given character sequence
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
         * Method which returns a random String of the specified
         * Length containing a non random element located at the given index.
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
         * Method which returns a random String of the specified
         * Length.
         */
        private static String getRandomString(int length){
            char[] randomString = new char[length];
            for(int i = 0;i<length; i++){
                randomString[i] =added[getRandom(added.length)];
            }
            return String.valueOf(randomString);
        }

        /*
         * Method which checks if a char array contains any duplicate values
         */
        public static boolean containsDuplicates(final char[] chars) {
            final int max_size = 99999;

            BitSet bitset = new BitSet(max_size + 1);
            bitset.set(0, max_size, false);
            for (int item : chars) {
                if (!bitset.get(item)) {
                    bitset.set(item, true);
                } else
                    return true;
            }
            return false;
        }
        private static SecureRandom getSha1Prng() {
			try {
				return SecureRandom.getInstance("SHA1PRNG");
			} catch (NoSuchAlgorithmException e) {
				throw new IllegalStateException(e);
			}
		}
        public static String[] convertToAlphaNumeric(char[] charSet, String password){
        	SecureRandom rand = getSha1Prng();
        	rand.setSeed(password.getBytes(StandardCharsets.UTF_8));
        	String[] alpha = new String[charSet.length];
        	int index;
        	for(int i = 0; i<charSet.length; i++){
        		index = rand.nextInt(charSet.length);
        		alpha[i] = ""+index;
        	}
        	return alpha;
        }
    }

    /**
     * Class which is in charge of generating new unique keys based on the
     * the given password and character set if any. Each
     * @author Eudy Contreras
     *
     */
	private static class KeyGenerator {
		private char[] char_key_set;
		private char[] char_base_set;
		private String[] byte_key_set;
		private String[] byte_base_set;
		private SecureRandom charRandom;
		private SecureRandom byteRandom;

		public KeyGenerator(String password, char[] charSet, String[] byteSet) {
			this.charRandom = this.getSha1Prng();
			this.byteRandom = this.getSha1Prng();
			this.generateKey(password,charRandom,charSet);
			this.generateKey(password,byteRandom,byteSet);
			this.shuffle(char_key_set,charRandom,ShuffelMethod.RICHARD_DURSTENFELD_SHUFFLE);
			this.shuffle(byte_key_set,byteRandom);
		}

		/*
		 * Creates a new key based on a given password. The password given will
		 * always reproduce the same key if combined with the same set of
		 * characters.
		 */
		private void generateKey(String password,SecureRandom charRandom, char[] baseChars) {
			charRandom.setSeed(password.getBytes(StandardCharsets.UTF_8));
			char_base_set = new char[baseChars.length];
			char_base_set = Arrays.copyOf(baseChars, baseChars.length);
			char_key_set = new char[char_base_set.length];
			char_key_set = Arrays.copyOf(char_base_set, baseChars.length);

		}

		private void generateKey(String password, SecureRandom byteRandom, String[] baseBytes) {
			byteRandom.setSeed(password.getBytes(StandardCharsets.UTF_8));
			byte_base_set = new String[baseBytes.length];
			byte_base_set = Arrays.copyOf(baseBytes, baseBytes.length);
			byte_key_set = new String[byte_base_set.length];
			byte_key_set = Arrays.copyOf(byte_base_set, baseBytes.length);
		}

		/*
		 * Method which securely shuffles an array using different conventional
		 * methods
		 */
		private void shuffle(char[] char_key,SecureRandom charRandom, ShuffelMethod method) {
			switch (method) {
			case FISHER_YATES_SHUFFLE:
				int index;
				for (int i = char_key.length - 1; i > 0; i--) {
					index = charRandom.nextInt(i + 1);
					if (index != i) {
						char_key[index] ^= char_key[i];
						char_key[i] ^= char_key[index];
						char_key[index] ^= char_key[i];
					}
				}
				break;
			case RICHARD_DURSTENFELD_SHUFFLE:
				for (int i = char_key.length - 1; i > 0; i--) {
					index = charRandom.nextInt(i + 1);
					char temp = char_key[index];
					char_key[index] = char_key[i];
					char_key[i] = temp;
				}
				break;
			default:
				break;

			}
		}
		/*
		 * Method which securely shuffles an array using different conventional
		 * methods
		 */
		private void shuffle(String[] byte_key, SecureRandom byteRandom) {
			int index;
			for (int i = byte_key.length - 1; i > 0; i--) {
				index = byteRandom.nextInt(i + 1);
				String temp = byte_key[index];
				byte_key[index] = byte_key[i];
				byte_key[i] = temp;
			}
		}

		private SecureRandom getSha1Prng() {
			try {
				return SecureRandom.getInstance("SHA1PRNG");
			} catch (NoSuchAlgorithmException e) {
				throw new IllegalStateException(e);
			}
		}

		private enum ShuffelMethod {
			FISHER_YATES_SHUFFLE, RICHARD_DURSTENFELD_SHUFFLE,
		}
		private char[] getBase_key() {
			return char_base_set;
		}
		private String[] getBase_Byte() {
			return byte_base_set;
		}
		private char[] getNew_Key() {
			return char_key_set;
		}
		private String[] getNew_Byte() {
			return byte_key_set;
		}
	}
    /**
     * Password class used by the String Encryption Utility
     * the pass word must contain at least 6 character.
     * @author Eudy Contreras
     *
     */
    public static class Password{
        private String password;

        public Password(String password){
            this.password = password;
        }
        private String getPassword(){
            return password;
        }
        private boolean isPasswordOk(){
            return password.length()>=6 && password!=null;
        }
    }
    public enum SecurityLevel{
    	LOW,MEDIUM,HIGH,VERY_HIGH
    }
    private static class InvalidPasswordException extends Exception {
        private static final long serialVersionUID = 1L;

        public InvalidPasswordException(String message) {
            super(message);
            System.err.println(this.getMessage());
            System.err.println("Please look at the documentation for more information");

        }

    }
    private static class InvalidCharacterSetException extends Exception {
        private static final long serialVersionUID = 1L;

        public InvalidCharacterSetException(String message) {
            super(message);
            System.err.println(this.getMessage());
            System.err.println("Please look at the documentation for more information");
        }

    }
    public static void main(String[] args) {
    	char[] new_char_set =
            {'D',' ','F','G','H','Ä','U','J',
             'A','L','Z','Å','N',',','P','Q',
             'W','S','T','Ö','.','V','R','X',
             'Y','M','!','B','O','4','6','1',
             '8','3','2','9','0','5','7','E',
             'C','?','I','K','$','/','@','*'};


    	char[] plain_char_set =
            {'A','B','C','D','E','F','G','H',
             'I','J','K','L','M','N','O','P',
             'Q','R','S','T','U','V','W','X',
             'Y','Z','Ö','Ä','Å','0','1','2',
             '3','4','5','6','7','8','9',' ',
             ',','?','.','!'};

        long encrypt_start_time = System.currentTimeMillis();
        byte[] encryption = StringEncrypter.encrypt("Your message is now secure!", new Password("password"));
        long encrypt_end_time = System.currentTimeMillis();
        System.out.println("Encryption speed: "+(encrypt_end_time - encrypt_start_time)+ " Milliseconds");


        System.out.println("Actual encrypted message:");
        System.out.println("////////////////////////////////////////////////////////////////////////////////");
        System.out.println("");
        EncryptionUtils.printBytes(encryption);
        System.out.println("");
        System.out.println("////////////////////////////////////////////////////////////////////////////////");


        long decrypt_start_time = System.currentTimeMillis();
        System.out.println(StringEncrypter.decrypt(encryption,new Password("password")));
        System.out.println(StringEncrypter.decrypt(Base64.decode("HJYTHTgcPiMcHBgNGhw+HBgcOBgYJBzDGhMOHTYzPh80HIUcwx8+GBgcDYUdOBwYGB9fPh8cNjMzNyQYPhwLGD4QNcM+GDU+wxgdPhwfYgs0Djgkwz5fDSY+OBIRMxA2Iic5HBhfND43GITDEhgYOT4LPpZhPhgdEyM1DZYcPhwaCz4+GBg5NjcyORwzwxwzNzI3MzQeJsMzGAs+HGEzEF83Mzc+GBw3hDMcHA4cNCVRPjY4GBEyEYVTOT4YNxwHPiccHA4YGDQYHBzDNhwaOT8+BwccGBgcHBw0ND4+NR0cHJY1HAc+NTI2Bx1h"),new Password("password")));
        long decrypt_end_time = System.currentTimeMillis();
        System.out.println("Decryption speed: "+(decrypt_end_time - decrypt_start_time)+ " Milliseconds");
    }
}
