package services;

import dto.guild.CUGuildEventDTO;
import exceptions.*;
import models.Guild;
import models.GuildEvent;
import models.UserProfile;
import repositories.GenerationRepository;
import repositories.GuildRepository;
import repositories.events.GuildEventRepository;
import repositories.roles.RoleRepository;
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
	// TODO: CRUD Guild
	public static void create(Guild data)
			throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException,
			DataEmptyException, InvalidDataException, TokenException, NotHavePermission {
		try {
			boolean checkPermissions = false;
			if (data.getName().isEmpty()) {
				throw new DataEmptyException("Guild Name is empty");
			}
			if (!isValidString(data.getName())) {
				throw new InvalidDataException("Invalid guild name");
			}
			if (checkPermissions) {
				data.setName(normalizeName(data.getName()));
				GuildRepository.insert(data.getName());
			} else {
				throw new NotHavePermission("You don't have permission");
			}

		} catch (SQLIntegrityConstraintViolationException | IOException | ClassNotFoundException e) {
			throw new SQLIntegrityConstraintViolationException(
					String.format("Your guild name is existed: %s", data.getName()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when create guild: %s", data.getName()));
		}
	}

	public static void update(Guild data, Guild newData)
			throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException,
			DataEmptyException, InvalidDataException, TokenException, NotHavePermission {
		try {
			if (newData.getName().isEmpty()) {
				throw new DataEmptyException("Guild Name is empty");
			}
			if (!isValidString(newData.getName())) {
				throw new InvalidDataException("Invalid guild name");
			}
			data.setName(normalizeName(data.getName()));
			newData.setName(normalizeName(newData.getName()));
			if (data.getName().equals(newData.getName())) {
				throw new SQLException("User Input Name Existed");
			}
			newData = new Guild(GuildRepository.getIdByName(data.getName()), newData.getName());
		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLIntegrityConstraintViolationException(
					String.format("Your name is exist: %s", data.getName()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when update guild: %s", data.getName()));
		} catch (IOException | ClassNotFoundException e) {
			throw new TokenException("Can't get access token");
		}
	}

	public static void delete(Guild data)
			throws SQLException, SQLIntegrityConstraintViolationException, NotFoundException,
			DataEmptyException, InvalidDataException, TokenException, NotHavePermission {
		try {
			data = new Guild(GuildRepository.getIdByName(data.getName()), data.getName());
		} catch (SQLIntegrityConstraintViolationException e) {

			throw new SQLException(String.format("Disallow null values id %s", data.getName()));
		} catch (SQLException e) {
			throw new SQLException(String.format("Error occurs when delete guild: %s", data.getName()));
		} catch (IOException | ClassNotFoundException e) {
			throw new TokenException("Can't get access token");
		}
	}

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
		Logger logger = LoggerFactory.getLogger(GuildEventRepository.class);
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
		String prefix = GuildRepository.GetCodeByName(name);
		return RoleRepository.getAllByPrefix(prefix)
				.stream()
				.map(role -> role.replaceFirst("^[^_]+_", ""))
				.collect(Collectors.toList());
	}
}
