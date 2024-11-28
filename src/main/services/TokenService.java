package services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import dto.TokenPairDTO;
import exceptions.TokenException;

public class TokenService {
	public static void saveToFile(Path savingFilePath, TokenPairDTO data) throws TokenException {
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

		} catch (Exception e) {
			throw new TokenException("Error occurs when saving tokens");
		}
	}

	public static TokenPairDTO loadFromFile(Path savingFilePath) throws TokenException, IOException {
		ObjectMapper mapper = new ObjectMapper();

		try {
			JsonNode rootNode = mapper.readTree(savingFilePath.toFile());
			JsonNode tokenNode = rootNode.path("token");

			if (tokenNode.isMissingNode()) {
				throw new TokenException("Please login to get token pair");
			}

			return new TokenPairDTO(
					tokenNode.path("accessToken").asText(),
					tokenNode.path("refreshToken").asText());

		} catch (IOException e) {
			throw new TokenException("Error occurs when loading token");
		}
	}

}
