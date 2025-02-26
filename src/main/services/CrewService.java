package services;

import repositories.RoleRepository;
import repositories.events.CrewEventRepository;
import exceptions.NotFoundException;
import utils.Pressessor;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import dto.crew.CUCrewEventDTO;
import repositories.CrewRepository;

public class CrewService {
                                // TODO: OH MY FORMATTER TvT
                                public static void insertEvent(String guildName, CUCrewEventDTO dto)
                                                                                                throws SQLException,
                                                                                                NotFoundException,
                                                                                                IllegalArgumentException,
                                                                                                IOException,
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
                                                                CrewEventRepository.insert(
                                                                                                                                guildName,
                                                                                                                                dto.getTitle(),
                                                                                                                                dto.getDescription(),
                                                                                                                                startTime,
                                                                                                                                endTime);
                                }

                                /// Dynamic update for each field
                                public static void updateEvent(String guildName, CUCrewEventDTO dto)
                                                                                                throws SQLException,
                                                                                                SQLIntegrityConstraintViolationException,
                                                                                                NotFoundException,
                                                                                                ClassNotFoundException,
                                                                                                IOException {
                                                                if (dto.getTitle().trim().isEmpty()
                                                                                                                                || dto.getDescription().trim().isEmpty()
                                                                                                                                || dto.getStartedAt().trim().isEmpty()
                                                                                                                                || dto.getEndedAt().trim().isEmpty()) {
                                                                                                throw new IllegalArgumentException("Some information is empty, can you check again!");
                                                                }

                                                                LocalDateTime startTime = Pressessor.parseDateTime(dto.getStartedAt());
                                                                LocalDateTime endTime = Pressessor.parseDateTime(dto.getEndedAt());

                                                                Pressessor.validateEventTime(startTime, endTime);
                                                                CrewEventRepository.update(
                                                                                                                                guildName,
                                                                                                                                dto.getEventId(),
                                                                                                                                dto.getTitle(),
                                                                                                                                dto.getDescription(),
                                                                                                                                startTime,
                                                                                                                                endTime);
                                }

                                public static List<String> getAllRolesByCrewName(String name) throws SQLException,
                                                                                                NotFoundException {
                                                                String prefix = CrewRepository.getCodeByName(name);
                                                                return RoleRepository.getAllByPrefix(prefix)
                                                                                                                                .stream()
                                                                                                                                .map(role -> role.replaceFirst("^[^_]+_", ""))
                                                                                                                                .collect(Collectors.toList());
                                }
}
