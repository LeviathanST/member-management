package services;

import dto.guild.CUGuildEventDTO;
import exceptions.*;
import models.Guild;
import models.GuildEvent;
import models.UserProfile;
import repositories.GenerationRepository;
import repositories.GuildRepository;
import repositories.events.GuildEventRepository;
import repositories.RoleRepository;
import repositories.users.UserAccountRepository;
import repositories.users.UserProfileRepository;
import repositories.users.UserRoleRepository;
import utils.Pressessor;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class GuildService {
	// TODO: Guild CRUD
	public static void insertGuildEvent(String guildName, CUGuildEventDTO dto)
			throws SQLException, NotFoundException,
			IllegalArgumentException, IOException,
			ClassNotFoundException {
		if (dto.getTitle().trim().isEmpty()
				|| dto.getDescription().trim().isEmpty()
				|| dto.getStartedAt().trim().isEmpty()
				|| dto.getEndedAt().trim().isEmpty()) {
			throw new IllegalArgumentException(
					"Some information is empty when insert event for guild, can you check again!");
		}

		LocalDateTime startTime = Pressessor.parseDateTime(dto.getStartedAt());
		LocalDateTime endTime = Pressessor.parseDateTime(dto.getEndedAt());

		Pressessor.validateEventTime(startTime, endTime);
		GuildEventRepository.insert(
				guildName,
				dto.getTitle(),
				dto.getDescription(),
				startTime,
				endTime);
	}

	/// Dynamic update for each field
	public static void updateGuildEvent(String guildName, CUGuildEventDTO dto)
			throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException,
			ClassNotFoundException, IOException {
		if (dto.getTitle().trim().isEmpty()
				|| dto.getDescription().trim().isEmpty()
				|| dto.getStartedAt().trim().isEmpty()
				|| dto.getEndedAt().trim().isEmpty()) {
			throw new IllegalArgumentException("Some information is empty, can you check again!");
		}

		LocalDateTime startTime = Pressessor.parseDateTime(dto.getStartedAt());
		LocalDateTime endTime = Pressessor.parseDateTime(dto.getEndedAt());

		Pressessor.validateEventTime(startTime, endTime);
		GuildEventRepository.update(
				guildName,
				dto.getEventId(),
				dto.getTitle(),
				dto.getDescription(),
				startTime,
				endTime);
	}

	public static boolean isValidString(String input) {
		return input.matches("([A-Za-z]+\\s*)+");
	}

	public static String normalizeName(String input) {
		String[] parts = input.toLowerCase().split(" ");
		StringBuilder normalized = new StringBuilder();
		if (parts.length > 1) {

			for (String part : parts) {
				if (!part.isEmpty()) {
					normalized.append(Character.toUpperCase(part.charAt(0)))
							.append(part.substring(1))
							.append(" ");
				}
			}
		} else {
			return normalized.append(Character.toUpperCase(input.charAt(0)))
					.append(input.substring(1)).toString();
		}
		return String.join(" ", normalized);
	}

	// ----------------------------------------------------
	public static List<String> getAllRolesByGuildName(String name) throws SQLException, NotFoundException {
		String prefix = GuildRepository.getCodeByName(name);
		return RoleRepository.getAllByPrefix(prefix)
				.stream()
				.map(role -> role.replaceFirst("^[^_]+_", ""))
				.collect(Collectors.toList());
	}
}
