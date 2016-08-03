package com.EudyContreras.Snake.MultiplayerClient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

/**
 * Class used to convert objects to byte arrays and byte arrays to objects.
 * @author Eudy Contreras
 *
 */
public class ByteConversionUtility {
	/**
	 * Converts a specified object into a byte array.
	 * @param obj
	 * @return
	 */
	public static byte[] convertToByte(Object obj){
		ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
		ObjectOutput objectOutput = null;
		byte[] newByteArray = null;
		try {
			objectOutput = new ObjectOutputStream(byteOutput);
			objectOutput.writeObject(obj);
		  newByteArray = byteOutput.toByteArray();
		} catch (IOException e) {
			System.out.println("unable to write object to byte array" +e.getMessage());
			e.printStackTrace();
		} finally {
		  try {
		    if (objectOutput != null) {
		    	objectOutput.close();
		    }
		  } catch (IOException ex) {}
		  try {
			  byteOutput.close();
		  } catch (IOException ex) {}
		}
		return newByteArray;
	}
	/**
	 * Converts a byte array into an object.
	 * @param bytes
	 * @return
	 */
	public static Object convertToObject(byte[] bytes){
		ByteArrayInputStream byteInput = new ByteArrayInputStream(bytes);
		ObjectInput objectInput = null;
		Object newObject = null;
		try {
			objectInput = new ObjectInputStream(byteInput);
			newObject = objectInput.readObject();
		} catch (ClassNotFoundException | IOException e) {
			System.out.println("Unable to convert byte array to an object" + e.getMessage());
			e.printStackTrace();
		} finally {
		  try {
			  byteInput.close();
		  } catch (IOException ex) {}
		  try {
		    if (objectInput != null) {
		    	objectInput.close();
		    }
		  } catch (IOException ex) {}
		}
		return  newObject;
	}
}
