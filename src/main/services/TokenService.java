package services;

import java.nio.file.Files;
import java.nio.file.Path;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import data.TokenPairData;
import exceptions.TokenException;

public class TokenService {
	public static void saveToFile(Path savingFilePath, TokenPairData data) throws TokenException {
		ObjectMapper mapper = new ObjectMapper();

		if (!Files.exists(savingFilePath)) {
			try {
				Files.createFile(savingFilePath);
			} catch (Exception e) {
				throw new TokenException(
						"Error occurs when create file to saving tokens");
			}
		}

		try {
			JsonNode rootNode = mapper.readTree(savingFilePath.toFile());
			if (rootNode.isMissingNode()) {
				rootNode = mapper.createObjectNode();
			}

			JsonNode tokenNode = rootNode.path("token");

			if (tokenNode.isMissingNode()) {
				tokenNode = mapper.createObjectNode();
				((ObjectNode) rootNode).set("token", tokenNode);
			}

			((ObjectNode) tokenNode).put("accessToken", data.getAccessToken());
			((ObjectNode) tokenNode).put("refreshToken", data.getRefreshToken());

			mapper.writerWithDefaultPrettyPrinter().writeValue(savingFilePath.toFile(), rootNode);
			System.out.println("Your token is updated");

		} catch (Exception e) {
			throw new TokenException("Error occurs when saving tokens");
		}
	}

	public static TokenPairData loadFromFile(Path savingFilePath) throws TokenException {
		ObjectMapper mapper = new ObjectMapper();

		try {
			JsonNode rootNode = mapper.readTree(savingFilePath.toFile());
			JsonNode tokenNode = rootNode.path("token");

			if (tokenNode.isMissingNode()) {
				throw new TokenException("Please login to get token pair");
			}

			return new TokenPairData(
					tokenNode.path("accessToken").asText(),
					tokenNode.path("refreshToken").asText());

		} catch (Exception e) {
			throw new TokenException("Error occurs when loading token");
		}
	}

}
