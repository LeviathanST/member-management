package controllers;

import java.nio.file.Path;
import data.TokenPairData;
import services.TokenService;

public class TokenController {
	public static void save(Path savingFilePath, TokenPairData data) {
		try {
			TokenService.saveToFile(savingFilePath, data);
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
		}
	}

	public static TokenPairData load(Path savingFilePath) {
		try {
			TokenPairData data = TokenService.loadFromFile(savingFilePath);
			return data;
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
			return new TokenPairData(null, null);
		}
	}
}
