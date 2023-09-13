package org.camunda.bpm.run.helpers;

import java.io.File;

import org.json.JSONObject;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class DecodeHelper {


	final static String password = "SHERLOCKED";
	final static String salt = "4fdca4656d6302ef";
	
	public static JSONObject GetYamlProperties(String filePath,String yamlKey) throws Exception {

		try {

			File file = new File(filePath);
			ObjectMapper om = new ObjectMapper(new YAMLFactory());
			ObjectMapper jsonOm = new ObjectMapper();
			Object yamlObj = om.readValue(file, Object.class);
			String jsonStr = jsonOm.writeValueAsString(yamlObj);
			JSONObject jsonObj = new JSONObject(jsonStr);
			JSONObject ldapJson = jsonObj.getJSONObject(yamlKey);

			return ldapJson;
			
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public static String DecryptText(String encryptedText) {
		TextEncryptor decryptor = Encryptors.text(password, salt);
		return decryptor.decrypt(encryptedText);
	}
	
}
