//package com.EudyContreras.Snake.Utilities;
//
//import java.nio.charset.Charset;
//import java.security.SecureRandom;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//
///**
// * <h1>String Encrypter</h1>
// *
// * This programs allows you encrypt a String and store as a byte array.
// * the program provides a simple encryption set of methods which make use of
// * scrambling/adding/replacing/swapping/ the core structure of the String.
// * The encrypted message is the store as byte array object which can be sent through sockets
// * or stored locally. In order to be able to view the content of the String the object
// * must be passed through the decrypt method of this program which will format and reconstruc the
// * String to it's original state.
// *
// * If the message is infiltrated while stored as an encrypted byte array whether if it is traveling
// * through a socket or store locally on a system. the thief wont be able to see
// * the content of the message without the key used by this class
// *
// * @author Eudy Contreras
// * @version 1.1
// * @since 2016-08-16
// */
//public class ByteEncryption {
//    private final static Charset format = Charset.forName("UTF-8");
//    private static byte[] added  = {5,8,127,95,127,-48,-128,-15,21,5,-57,-27,65,31,-55,95,48}; //Min = -128, Max = 127
//    private static byte[] base_bytes  = {-128,127}; //Min = -128, Max = 127
//    /*
//     * Meps which hold the encryption base and key data structures.
//     */
//    private static final Map<Byte, Byte> plain_Map = new HashMap<Byte, Byte>();
//    private static final Map<Byte, Byte> key_Map = new HashMap<Byte, Byte>();
//
//    private static final Map<String, String> plain_Byte_Map = new HashMap<String, String>();
//    private static final Map<String, String> key_Byte_Map = new HashMap<String, String>();
//
//    /**
//     * This values determined the swap indexes at which the swapping methods
//     * will operate. More swapp indexes may be added for increase security.
//     */
//    private final static int  swap_index_1 = 2;
//    private final static int  swap_index_2 = 6;
//    private final static int  swap_index_3 = 4;
//    private final static int  swap_index_4 = 3;
//
//    /*
//     * The higher the number the more random characters will be added to the encryption.
//     * The number set here will affect the performance of the application. The higher the
//     * number the lower the performance will be but the more populated with random characters
//     * he encryption will be.
//     * HIgher = more secure but slower.
//     * Lower = less secure but faster.
//     * recommended: 6
//     */
//    private static int  max_additions = 5;
//
//    /*
//     * The index in which the actually code character will be inserted.
//     * the index must be a number between 0 and the max_additions value-1.
//     */
//    private static int  insertion_index = 2;
//
//
//    public static byte[] encrypt(Object object,Password password){
//        return encrypt(object,password, null);
//    }
//
//    public static byte[] encrypt(Object object, Password password, SecurityLevel security){
//    	byte[] obj = ByteConversionUtility.convertToByte(object);
//        if(prepareEncryption(password, security)){
//
//        String cypher = addRandom(obj).toString();
//
//        byte[] encryption = cypher.getBytes(format);
//
//        byte[] swapped_Bytes = revertBytes(EncryptionUtils.toByteObject(encryption));
//
//        return swapBytes(swapped_Bytes);
//
//        }else{
//            return null;
//        }
//    }
//
//    public static Object decrypt(Object object, Password password){
//        return decrypt(object,password,null);
//    }
//
//    public static Object decrypt(Object object, Password password, SecurityLevel security){
//    	byte[] obj = ByteConversionUtility.convertToByte(object);
//        if(prepareEncryption(password, security)){
//
//        byte[] unswapped_Bytes = revertBytesBack(EncryptionUtils.toByteObject(unSwapBytes(obj)));
//
////        byte[] unScrambled = removeRandom(unswapped_Bytes);
//
//        byte[] decyphered = revertSubstitution(unswapped_Bytes);
//
//       	Object newObject = ByteConversionUtility.convertToObject(obj);
//        return newObject;
//        }
//        else{
//            return "";
//        }
//    }
//    /*
//     * Method in charged of processing the encryption and decryption request made
//     * by the user. This method will perform checks on the validity of the password and the character
//     * set if any. If both the password and the character set are valid the method will then generate a key
//     * and allow the encryption to proceed further.
//     */
//    private static boolean prepareEncryption(Password password, SecurityLevel security){
//    	 String[] byte_Plain =
//             {"1","2","3","4","5","6","7","8","9","0"};
//    	 String[] byte_key =
//             {"6","2","7","1","5","9","4","0","8","3"};
//
//       if (password.isPasswordOk()) {
//        	KeyGenerator keyGen = new KeyGenerator(password.getPassword(),base_bytes);
//        	setSecurityLevel(password.getPassword(), security);
//            key_Map.clear();
//            plain_Map.clear();
//            plain_Byte_Map.clear();
//            key_Byte_Map.clear();
//            for (int i = 0; i < base_bytes.length; i++) {
//            	 plain_Map.put(keyGen.getBase_Byte()[i], keyGen.getNew_Byte()[i]);
//                 key_Map.put(keyGen.getNew_Byte()[i], keyGen.getBase_Byte()[i]);
//            }
//            for (int i = 0; i < byte_Plain.length; i++) {
//            	plain_Byte_Map.put(byte_Plain[i],byte_key[i]);
//            	key_Byte_Map.put(byte_key[i], byte_Plain[i]);
//           }
//            return true;
//        } else {
//            	new InvalidPasswordException("The submitted password is not valid");
//            return false;
//        }
//
//    }
//    /*
//     * Method used to swap the elements of the input array.
//     * The elements will be swapped using 4 nested loops with
//     * different swap indexes. The elements are swapped around
//     * millions of times
//     */
//    private static byte[] swapByteElements(byte[] bytes){
//        for(int i1 = 0; i1<bytes.length-swap_index_1; i1++){
//        	byte temp = bytes[i1];
//            bytes[i1] = bytes[i1+swap_index_1];
//            bytes[i1+swap_index_1] = temp;
//
//            for(int i2 = 0; i2<bytes.length-swap_index_2; i2++){
//            	byte temp2 = bytes[i2];
//                bytes[i2] = bytes[i2+swap_index_2];
//                bytes[i2+swap_index_2] = temp2;
//
//                for(int i3 = 0; i3<bytes.length-swap_index_3; i3++){
//                	byte temp3 = bytes[i3];
//                    bytes[i3] = bytes[i3+swap_index_3];
//                    bytes[i3+swap_index_3] = temp3;
//
//                    for(int i4 = swap_index_4; i4<bytes.length; i4++){
//                    	byte temp4 = bytes[i4];
//                        bytes[i4] = bytes[i4-swap_index_4];
//                        bytes[i4-swap_index_4] = temp4;
//                    }
//                }
//            }
//        }
//        return bytes;
//    }
//    /*
//     * Method used to swap the elements which were previously swapped
//     * by the swapCharacters method back to their original index. The swap
//     */
//    private static byte[] revertByteElementSwap(byte[] bytes) {
//        for (int i1 = bytes.length - (swap_index_1+1); i1 >= 0; i1--) {
//
//            for (int i2 = bytes.length - (swap_index_2+1); i2 >= 0; i2--) {
//
//                for (int i3 = bytes.length - (swap_index_3+1); i3 >= 0; i3--) {
//
//                    for (int i4 = bytes.length - 1; i4 >= (swap_index_4); i4--) {
//                        byte temp4 = bytes[i4];
//                        bytes[i4] = bytes[i4 - swap_index_4];
//                        bytes[i4 - swap_index_4] = temp4;
//                    }
//
//                    byte temp3 = bytes[i3];
//                    bytes[i3] = bytes[i3 + swap_index_3];
//                    bytes[i3 + swap_index_3] = temp3;
//                }
//
//                byte temp2 = bytes[i2];
//                bytes[i2] = bytes[i2 + swap_index_2];
//                bytes[i2 + swap_index_2] = temp2;
//            }
//
//            byte temp = bytes[i1];
//            bytes[i1] = bytes[i1 + swap_index_1];
//            bytes[i1 + swap_index_1] = temp;
//        }
//        return bytes;
//    }
//    /*
//     * Method which swaps the elements match to the character set
//     * with the corresponding key element. The method will also create
//     * an array holing information about the case of each character. The method
//     * will also perform a character swap with a call to the swapCharacters method. After
//     * the process is completed this method will return a set of characters holding the
//     * coded message along with a set of characters holding case information. The
//     * two are divided by a special sequence of symbols in order to distinguish the
//     * the two. The return message will then be passed further for further encryption.
//     */
//    private final static byte[] applySubstitution(byte[] bytes) {
//
//        byte[] byte_data = new byte[bytes.length];
//
//        for(byte b: bytes) {
//            if(plain_Map.containsKey(b)){
//                b = plain_Map.get(b);
//            }
//        }
//        return swapByteElements(byte_data);
//    }
//
//    /*
//     * Method which reverts the character substitution performed by the
//     * applySubstitution. It does this by reverting the pattern in which the substitution
//     * was made. This will separate and analyze the message along with the case data
//     * and once this is done it will also perform a call to the revertCharacterSwap method
//     * in order to also unswap the order of the characters to their original state.
//     * Once the substitution and the character swap has been reversed it will further
//     * analyze case data determine the case of each character. Upon completion it
//     * will return the decrypted message.
//     */
//    private final static byte[] revertSubstitution(byte[] bytes) {
//
//        byte[] byte_data = revertByteElementSwap(bytes);
//
//        for (byte b: byte_data) {
//            if(key_Map.containsKey(b)){
//                b = key_Map.get(b);
//            }
//        }
//
//        return byte_data;
//    }
//
//    /*
//     * Method used to further increases the obfuscation level of the message by adding
//     * random numbers and characters to the body of the already encrypted message. This
//     * is done at key places of the message in order to allow the subtraction of the original
//     * version of the encryption without having to deal with the extract characters and numbers
//     * added by this function. The function a
//     * the process can then be reversed with an additional key.
//
//     */
//    private final static byte[] addRandom(byte[] bytes) {
////        LinkedList<Byte> cypherList = new LinkedList<>();
////        byte[] byte_data = applySubstitution(bytes);
////
////        for (byte b: byte_data) {
////            cypherList.add(EncryptionUtils.getRandomByteArray(max_additions,b,insertion_index));
////        }
////
////        cypherList.addFirst((EncryptionUtils.getRandomInterval(-128,127)));
////        cypherList.addLast(EncryptionUtils.getRandomInterval(-128,127));
////
//        return bytes;
//
//
//    }
//    /*
//     * Method used to remove all previously added obfuscation elements. The method
//     * will loop through the values of a given list and it will filter the orignal's
//     * message values into their respective char arrays, one holding the code and the
//     * other holding the case related data.
//     */
//    private final static byte[] removeRandom(List<String> cypher_List) {
////
////        StringBuilder string_Builder = new StringBuilder();
////
////        char[] case_message = new char[cypher_List.get(cypher_List.size()-1).length()-5];
////        char[] code_message = new char[cypher_List.size()-1];
////
////        for (int index = 0; index < cypher_List.size() - 1; index++) {
////            if (index >= 1){
////            	try{
////                string_Builder.append(String.valueOf(cypher_List.get(index).toCharArray()[insertion_index]));
////            	}catch(ArrayIndexOutOfBoundsException e){ /*TODO NOTHING*/}
////            }
////            if (index < case_message.length){
////                case_message[index] = cypher_List.get(cypher_List.size() - 1).toCharArray()[index];
////            }
////        }
////
////        code_message = string_Builder.toString().toCharArray();
////
//        return null;
//    }
//
//    /*
//     * Method used to further encrypt the message by replacing the
//     * first first digit of every value on every index of the byte array
//     * except for indexes that hold a negative value. This method converts
//     * the byte value to String which is than formated and casted back to
//     * a byte. A modification can be perform that may allow each index to
//     * be replace using a numerical value check!
//     */
//    private final static Byte[] replaceBytes(Byte[] bytes) {
//        Byte[] temp_Bytes = Arrays.copyOf(bytes, bytes.length);
//
////        for (int i = 0; i < temp_Bytes.length; i++) {
////            if (Integer.toString(temp_Bytes[i]).charAt(0) != '-') {
////                String temp_String = String.valueOf(temp_Bytes[i]);
////                String new_Value = plain_Byte_Map.get(Integer.toString(temp_Bytes[i]).substring(0, 1))+ temp_String.substring(1);
////                temp_Bytes[i] = Byte.valueOf(new_Value);
////            }
////        }
//        return temp_Bytes;
//    }
//    /*
//     * Method used to revert the previously replaced bytes back to
//     * to normal. The byte array will go through an identical procedure
//     * as the one performed in the replace bytes method in order to
//     * give each byte index the original value of the first index.
//     */
//    private final static Byte[] revertByteReplacement(Byte[] bytes) {
//        Byte[] temp_Bytes = Arrays.copyOf(bytes, bytes.length);
////
////        for (int i = 0; i < temp_Bytes.length; i++) {
////            if (Integer.toString(temp_Bytes[i]).charAt(0) != '-') {
////                String temp_String = String.valueOf(temp_Bytes[i]);
////                String new_Value = key_Byte_Map.get(Integer.toString(temp_Bytes[i]).substring(0, 1))+ temp_String.substring(1);
////                temp_Bytes[i] = Byte.valueOf(new_Value);
////            }
////        }
//        return temp_Bytes;
//    }
//    /*
//     * Method used to swap the elements of the input array.
//     * The elements will be swapped using 3 nested loops with
//     * different swap indexes. The elements are swapped around
//     * millions of times.
//     */
//    private final static byte[] swapBytes(byte[] bytes){
//        for(int i1 = 0; i1<bytes.length-swap_index_4; i1++){
//            byte temp = bytes[i1];
//            bytes[i1] = bytes[i1+swap_index_4];
//            bytes[i1+swap_index_4] = temp;
//
//            for(int i2 = 0; i2<bytes.length-swap_index_2; i2++){
//                byte temp2 = bytes[i2];
//                bytes[i2] = bytes[i2+swap_index_2];
//                bytes[i2+swap_index_2] = temp2;
//
//                for(int i3 = swap_index_1; i3<bytes.length; i3++){
//                    byte temp3 = bytes[i3];
//                    bytes[i3] = bytes[i3-swap_index_1];
//                    bytes[i3-swap_index_1] = temp3;
//                }
//            }
//        }
//        return bytes;
//    }
//    /*
//     * Method used to swap the elements which were previously swapped
//     * by the swapBytes method back to their original index. The swap
//     */
//    private final static byte[] unSwapBytes(byte[] bytes){
//        for (int i1 = bytes.length - (swap_index_4+1); i1 >= 0; i1--) {
//
//        	for (int i2 = bytes.length - (swap_index_2+1); i2 >= 0; i2--) {
//
//        		for (int i3 = bytes.length - 1; i3 >= (swap_index_1); i3--) {
//
//        			byte temp3 = bytes[i3];
//                    bytes[i3] = bytes[i3 - swap_index_1];
//                    bytes[i3 - swap_index_1] = temp3;
//                }
//                byte temp2 = bytes[i2];
//                bytes[i2] = bytes[i2 + swap_index_2];
//                bytes[i2 + swap_index_2] = temp2;
//            }
//            byte temp = bytes[i1];
//            bytes[i1] = bytes[i1 + swap_index_4];
//            bytes[i1 + swap_index_4] = temp;
//        }
//        return bytes;
//    }
//    /**
//     * Reverses the byte array for further increase obfuscation.
//     */
//    private final static byte[] revertBytes(Byte[] encryption){
//        Byte[] byte_Array = encryption;
//
//        List<Byte> byte_List = Arrays.asList(byte_Array);
//
//        Collections.reverse(byte_List);
//
//        return EncryptionUtils.toPrimitives(replaceBytes(byte_Array));
//    }
//    /**
//     * Reverses the byte array to its original state
//     */
//    private final static byte[] revertBytesBack(Byte[] encryption){
//        Byte[] byte_Array = encryption;
//
//        List<Byte> byte_List = Arrays.asList(byte_Array);
//
//        Collections.reverse(byte_List);
//
//        return EncryptionUtils.toPrimitives(revertByteReplacement(byte_Array));
//    }
//    /*
//     * Method which checks the security level required by the user
//     * and base on the specifications it will adjust the amount of characters per character
//     * which are to be added to the encrypted message. The higher the security level the
//     * higher the number of added characters and vice versa.
//     */
//    private final static void setSecurityLevel(String password, SecurityLevel security){
//    	if(security!=null){
//    		switch(security){
//			case LOW:
//				max_additions = 1;
//				insertion_index = 0;
//				break;
//			case MEDIUM:
//				max_additions = 4;
//				insertion_index = 3;
//				break;
//			case HIGH:
//				max_additions = 6;
//				insertion_index = 2;
//				break;
//			case VERY_HIGH:
//				max_additions = 10;
//				insertion_index = 7;
//				break;
//			default:
//				break;
//    		}
//    	}
//    }
//    /**
//     * <h1>Encryption Utility</h1>
//     *
//     * This class contains various functions used by the program.
//     *
//     * @author Eudy Contreras
//     * @version 1.1
//     * @since 2016-08-16
//     */
//    public static class EncryptionUtils{
//        /*
//         * Method which converts an Object byte array to a primitive type.
//         */
//        private static byte[] toPrimitives(Byte[] byte_Object){
//
//            byte[] bytes = new byte[byte_Object.length];
//
//            int i = 0;
//            for(Byte byte_Objects : byte_Object)bytes[i++] = byte_Objects;
//
//            return bytes;
//        }
//        /*
//         * Method which converts a primitive byte array to an object byte array.
//         */
//        private static Byte[] toByteObject(byte[] byte_prime) {
//            Byte[] bytes = new Byte[byte_prime.length];
//
//            int i = 0;
//            for (byte byte_Primes : byte_prime) bytes[i++] = byte_Primes;
//
//            return bytes;
//        }
//        /*
//         * Method which prints the content of a byte array.
//         */
//        public static String printBytes(byte[] binaryEncription){
//            if(binaryEncription!=null){
//            return new String(binaryEncription, format);
//            }
//            else
//                return "";
//        }
//
//        /*
//         * Method which generates a secure random within a
//         * given range.
//         */
//        private static int getRandom(int value){
//            SecureRandom rand = new SecureRandom();
//            return rand.nextInt(value);
//        }
//        /*
//         * Method which generates a secure random within a
//         * given interval.
//         */
//        private static int getRandomInterval(int minValue, int maxValue) {
//            SecureRandom rand = new SecureRandom();
//            return rand.nextInt(maxValue + 1 - minValue) + minValue;
//        }
//        /*
//         * Method which generates a secure random within a
//         * given interval.
//         */
//        private static byte[] getRandomByteArray(int lenght) {
//            SecureRandom rand = new SecureRandom();
//            byte[] randBytes = new byte[lenght];
//            rand.nextBytes(randBytes);
//            return randBytes;
//        }
//        /*
//         * Method which returns a random String of the specified
//         * Length containing a non random element located at the given index.
//         */
//        private static byte[] getRandomByteArray(int length, byte key_byte, int index){
//            byte[] decoy_bytes = new byte[length];
//            for(int i = 0;i<length; i++){
//                decoy_bytes[i] =added[getRandom(added.length)];
//                if(i==index){
//                    decoy_bytes[i] = key_byte;
//                }
//            }
//            return decoy_bytes;
//        }
//        /*
//         * Method which returns a random String of the specified
//         * Length.
//         */
//        private static byte getRandomByte(int length){
//            byte random_byte = 0;
//            for(int i = 0;i<length; i++){
//                random_byte =added[getRandom(added.length)];
//            }
//            return random_byte;
//        }
//        /*
//         * Method which takes and unlimited number of byte arrays
//         * and merge them in the order they were passed through
//         * the parametter of this method
//         */
//    	public static byte[] merge(byte[]... arrays) {
//    		int size = 0;
//    		for (byte[] array : arrays) {
//    			size += array.length;
//    		}
//    		int pos = 0;
//    		byte[] merged = new byte[size];
//    		for (byte[] array : arrays) {
//    			System.arraycopy(array, 0, merged, pos, array.length);
//    			pos += array.length;
//    		}
//    		return merged;
//    	}
//    }
//    /**
//     * Class which is in charge of generating new unique keys based on the
//     * the given password and character set if any. Each
//     * @author Eudy Contreras
//     *
//     */
//	private static class KeyGenerator {
//		private byte[] byte_key_set;
//		private byte[] byte_base_set;
//		private SecureRandom byteRandom = new SecureRandom();
//
//		public KeyGenerator(String password, byte[] byteSet) {
//			this.generateKey(password,byteRandom,byteSet);
//			this.shuffle(byte_key_set,byteRandom);
//		}
//
//		/*
//		 * Creates a new key based on a given password. The password given will
//		 * always reproduce the same key if combined with the same set of
//		 * characters.
//		 */
//		private void generateKey(String password, SecureRandom byteRandom, byte[] baseBytes) {
//			byteRandom.setSeed(password.getBytes(format));
//			byte_base_set = new byte[baseBytes.length];
//			byte_base_set = Arrays.copyOf(baseBytes, baseBytes.length);
//			byte_key_set = new byte[byte_base_set.length];
//			byte_key_set = Arrays.copyOf(byte_base_set, baseBytes.length);
//		}
//		/*
//		 * Method which securely shuffles an array using different conventional
//		 * methods
//		 */
//		private void shuffle(byte[] byte_key, SecureRandom byteRandom) {
//			int index;
//			for (int i = byte_key.length - 1; i > 0; i--) {
//				index = byteRandom.nextInt(i + 1);
//				byte temp = byte_key[index];
//				byte_key[index] = byte_key[i];
//				byte_key[i] = temp;
//			}
//		}
//		public byte[] getBase_Byte() {
//			return byte_base_set;
//		}
//		public byte[] getNew_Byte() {
//			return byte_key_set;
//		}
//	}
//    /**
//     * Password class used by the String Encryption Utility
//     * the pass word must contain at least 6 character.
//     * @author Eudy Contreras
//     *
//     */
//    public static class Password{
//        private String password;
//
//        public Password(String password){
//            this.password = password;
//        }
//        private String getPassword(){
//            return password;
//        }
//        private boolean isPasswordOk(){
//            return password.length()>=6 && password!=null;
//        }
//    }
//    public enum SecurityLevel{
//    	LOW,MEDIUM,HIGH,VERY_HIGH
//    }
//    private static class InvalidPasswordException extends Exception {
//        private static final long serialVersionUID = 1L;
//
//        public InvalidPasswordException(String message) {
//            super(message);
//            System.err.println(this.getMessage());
//            System.err.println("Please look at the documentation for more information");
//
//        }
//
//    }
//    private static class InvalidCharacterSetException extends Exception {
//        private static final long serialVersionUID = 1L;
//
//        public InvalidCharacterSetException(String message) {
//            super(message);
//            System.err.println(this.getMessage());
//            System.err.println("Please look at the documentation for more information");
//        }
//
//    }
//    public static void main(String[] args) {
//    	char[] new_char_set =
//            {'D',' ','F','G','H','�','U','J',
//             'A','L','Z','�','N',',','P','Q',
//             'W','S','T','�','.','V','R','X',
//             'Y','M','!','B','O','4','6','1',
//             '8','3','2','9','0','5','7','E',
//             'C','?','I','K','$','/','@','*'};
//
//        long encrypt_start_time = System.currentTimeMillis();
//        byte[] encryption = ByteEncryption.encrypt("Your message is now secure!!", new Password("password"), SecurityLevel.MEDIUM);
//        long encrypt_end_time = System.currentTimeMillis();
//        System.out.println("Encryption speed: "+(encrypt_end_time - encrypt_start_time)+ " Milliseconds");
//
//
//
//        System.out.println("Actual encrypted message:");
//        System.out.println("////////////////////////////////////////////////////////////////////////////////");
//        System.out.println("");
//        System.out.println(EncryptionUtils.printBytes(encryption));
//        System.out.println("");
//        System.out.println("////////////////////////////////////////////////////////////////////////////////");
//
//
////      System.out.println(Arrays.toString(encryption));
//        long decrypt_start_time = System.currentTimeMillis();
//        System.out.println(ByteEncryption.decrypt(encryption,new Password("password"),SecurityLevel.MEDIUM));
//        long decrypt_end_time = System.currentTimeMillis();
//        System.out.println("Decryption speed: "+(decrypt_end_time - decrypt_start_time)+ " Milliseconds");
//    }
//}
