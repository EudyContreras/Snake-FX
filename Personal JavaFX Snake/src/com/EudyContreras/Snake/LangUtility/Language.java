package com.EudyContreras.Snake.LangUtility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public enum Language {

	INSTANCE;

	private final String KEY_LANG_ENGLISH = "English";
	private final String PATH_LANG_ENGLISH = "com.SnakeGame.Language.lang_en";
	private final String FILE_LANG_ENGLISH = "lang_en.properties";

	private final String KEY_LANG_SWEDISH = "Svenska";
	private final String PATH_LANG_SWEDISH = "com.SnakeGame.Language.lang_se";
	private final String FILE_LANG_SWEDISH = "lang_se.properties";

	private final String PATH_LANG_ROOT = "." + File.separator + "Snake" + File.separator + "Lang" + File.separator;

	private final String PATH_LANG_FILE = PATH_LANG_ROOT + "lang.ini";

	private final HashMap<String, String> languageMap = new HashMap<String, String>();

	private String currentLanguage;

	private Language() {
		languageMap.put(KEY_LANG_ENGLISH, PATH_LANG_ENGLISH);
		languageMap.put(KEY_LANG_SWEDISH, PATH_LANG_SWEDISH);

		String language = loadLanguageSettingFromFile();

		currentLanguage = language;
	}

	@SuppressWarnings("resource")
	private String loadLanguageSettingFromFile() {
		File root = new File(PATH_LANG_ROOT);
		File file = new File(PATH_LANG_FILE);
		int c;
		StringBuilder lang = new StringBuilder();

		BufferedReader buffer;

		if( root.exists() && file.exists() ) {

			try {
				buffer = new BufferedReader(new FileReader(PATH_LANG_FILE));
			} catch (FileNotFoundException e) {
				System.out.println("Could not load lang");
				return null;
			}


			while(true) {
				try {
					c = buffer.read();
				} catch (IOException e) {
					System.out.println("Could not load lang");
					return null;
				}

				if( c != -1 ) {
					lang.append((char) c);
				} else {
					break;
				}
			}

			try {
				buffer.close();
			} catch (IOException e) {
				System.out.println("Could not load lang");
				return null;
			}
		}

		if(lang.length() == 0){
			return KEY_LANG_ENGLISH;
		} else {
			return lang.toString();
		}
	}

	private boolean saveLanguageSettingToFile(String language) {
		File root = new File(PATH_LANG_ROOT);
		File file = new File(PATH_LANG_FILE);
		if( ( root.exists() && file.exists() ) == false) {
			root.mkdirs();
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.out.println("Could not save lang setting.");
				return false;
			}
		}

		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(PATH_LANG_FILE)));

			writer.write(language);
			writer.close();
		} catch(IOException e) {
			System.out.println("Unable to save lang setting to file");
			return false;
		}
		System.out.println("Lang setting saved to file");
		return true;
	}

	public String getCurrentLanguage() {

		return currentLanguage;
	}

	public String getLanguagePath(String language) {
		if( languageMap.containsKey(language) ) {
			return languageMap.get(language);
		} else {
			//return default lang
			System.out.println("Invalid language.");
			return languageMap.get(KEY_LANG_ENGLISH);
		}
	}

	public boolean setLanguage(String language) {
		if(languageMap.containsKey(language)) {
			currentLanguage = language;
			saveLanguageSettingToFile(language);
			return true;
		} else {
			System.out.println("Invalid language.");
			return false;
		}
	}

	public ArrayList<String> getLanguagesAsList() {
		ArrayList<String> list = new ArrayList<String>();

		for(Map.Entry<String, String> cursor : languageMap.entrySet() ) {
			list.add(cursor.getKey());
		}

		Collections.sort(list);
		return list;
	}

	public Properties getLangAsProperties() {
		Properties prop = new Properties();
		String path = null;

		if(currentLanguage.equals(KEY_LANG_ENGLISH)) {
			path = FILE_LANG_ENGLISH;
		} else if(currentLanguage.equals(KEY_LANG_SWEDISH)){
			path = FILE_LANG_SWEDISH;
		} else {
			path = FILE_LANG_ENGLISH;
			System.out.println("Lang to Properties error!");
		}

		try {
			prop.load(getClass().getResourceAsStream((path)));
		} catch (IOException e) {
			System.out.println("Could not load language as property!");
			prop = null;
		}
		return prop;
	}
}

