package utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;

public class HttpUtil {
	public static <T> T getBodyContentFromReq(HttpServletRequest req, Class<T> clazz) {
		StringBuilder jsonBody = new StringBuilder();
		Gson gson = new Gson();

		try (BufferedReader reader = req.getReader()) {
			String line;
			while ((line = reader.readLine()) != null) {
				jsonBody.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		Logger logger = LoggerFactory.getLogger(HttpUtil.class);
		// Deserialize JSON -> Object
		try {
			return gson.fromJson(jsonBody.toString(), clazz);
		} catch (JsonSyntaxException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
}
