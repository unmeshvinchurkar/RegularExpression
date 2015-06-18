package com.tokenizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.regular.expression.RegularExpParser;

public class TypeDefLoader {

	private static final String CHAR_KEY = "CHAR";
	private Map<String, String> regExMap = new HashMap<String, String>();
	private Map<String, String> regExClassMap = new LinkedHashMap<String, String>();
	private Map<String, RegularExpParser> classParserMap = new LinkedHashMap<String, RegularExpParser>();

	public Map<String, String> getClassNameRegExMap() {
		return regExClassMap;
	}

	public Map<String, RegularExpParser> getClassNameRegExParserMap() {
		return classParserMap;
	}

	public void load() {		
		
		BufferedReader br = null;
		try {
			File file= new File(".//"+"typeDefs.properties");
			FileInputStream fin= new FileInputStream(file);
			br = new BufferedReader(new InputStreamReader(fin));
			
			String line = null;

			// Go to %BASIC_REG_EX%
			while (!(line = br.readLine()).equalsIgnoreCase("%BASIC_REG_EX%"))
				;
			while (!(line = br.readLine()).equalsIgnoreCase("%END%")) {
				if (!line.startsWith("//")) {
					_fillRegExMap(line);
				}
			}
			RegularExpParser charparser = new RegularExpParser()
					.compile(regExMap.get(CHAR_KEY));

			// Go to %TOKEN_CLASSES%
			while (!(line = br.readLine()).equalsIgnoreCase("%TOKEN_CLASSES%"))
				;
			while ((line = br.readLine()) != null
					&& !line.equalsIgnoreCase("%END%")) {
				if (line.indexOf('=') < 0 || line.startsWith("//")) {
					continue;
				}
				String key = line.substring(0, line.indexOf('='));
				String tmpValue = line.substring(line.indexOf('=') + 1);
				_fillRegExClassMap(key, tmpValue, charparser);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public RegularExpParser getRegExParserByClassName(String className) {
		return classParserMap.get(className);
	}

	private void _fillRegExMap(String line) {
		if (line.indexOf('=') > 0) {
			String key = line.substring(0, line.indexOf('='));
			String value = line.substring(line.indexOf('=') + 1);
			regExMap.put(key, value);
		}
	}

	private void _fillRegExClassMap(String key, String tmpValue,
			RegularExpParser charparser) {
		StringBuffer tmpValueBuffer = new StringBuffer(20);

		for (int i = 0; i < tmpValue.length(); i++) {
			Character c = tmpValue.charAt(i);

			if (c != ' ') {
				if (!charparser.match(String.valueOf(c))) {
					tmpValueBuffer.append(c);
				} else {
					StringBuffer sb1 = new StringBuffer(20);

					while (i < tmpValue.length()
							&& charparser.match(String.valueOf(tmpValue
									.charAt(i)))) {
						sb1.append(tmpValue.charAt(i++));
					}
					tmpValueBuffer.append("(" + regExMap.get(sb1.toString())
							+ ")");

					if (i < tmpValue.length()) {
						tmpValueBuffer.append(tmpValue.charAt(i));
					}
				}
			}
		}
		regExClassMap.put(key, tmpValueBuffer.toString());
		classParserMap.put(key,
				new RegularExpParser().compile(tmpValueBuffer.toString()));
	}

	public static void main(String[] args) {
		TypeDefLoader parser = new TypeDefLoader();
		parser.load();
		System.out.println(parser.getClassNameRegExMap());
		System.out.println(parser.getClassNameRegExParserMap());
	}
}
