package services;

import dto.UpdateProfileDTO;
import dto.app.CUEventDTO;
import exceptions.*;
import models.UserProfile;
import repositories.CrewRepository;
import repositories.GuildRepository;
import repositories.PermissionRepository;
import repositories.RoleRepository;
import repositories.events.EventRepository;
import repositories.users.UserAccountRepository;
import repositories.users.UserProfileRepository;
import repositories.users.UserRoleRepository;
import utils.Pressessor;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

import constants.RoleContext;

public class ApplicationService {
                                public static void insertEvent(CUEventDTO dto)
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
                                                                EventRepository.insert(
                                                                                                                                dto.getTitle(),
                                                                                                                                dto.getDescription(),
                                                                                                                                startTime,
                                                                                                                                endTime);
                                }

                                /// Dynamic update for each field
                                public static void updateEvent(CUEventDTO dto)
                                                                                                throws SQLException,
                                                                                                SQLIntegrityConstraintViolationException,
                                                                                                NotFoundException,
                                                                                                ClassNotFoundException,
                                                                                                IOException {
                                                                if (dto.getTitle().trim().isEmpty()
                                                                                                                                || dto.getDescription().trim().isEmpty()
                                                                                                                                || dto.getStartedAt().trim().isEmpty()
                                                                                                                                || dto.getEndedAt().trim().isEmpty()) {
                                                                                                throw new IllegalArgumentException(
                                                                                                                                                                "Some information is empty, can you check again!");
                                                                }

                                                                LocalDateTime startTime = Pressessor.parseDateTime(dto.getStartedAt());
                                                                LocalDateTime endTime = Pressessor.parseDateTime(dto.getEndedAt());

                                                                Pressessor.validateEventTime(startTime, endTime);
                                                                EventRepository.update(
                                                                                                                                dto.getEventId(),
                                                                                                                                dto.getTitle(),
                                                                                                                                dto.getDescription(),
                                                                                                                                startTime,
                                                                                                                                endTime);
                                }

                                public static void updateUserProfile(UpdateProfileDTO data, String accountId)
                                                                                                throws SQLException,
                                                                                                TokenException,
                                                                                                NotFoundException,
                                                                                                IOException,
                                                                                                ClassNotFoundException,
                                                                                                IllegalArgumentException {
                                                                data.checkNullOrEmpty();
                                                                Pressessor.validateStudentCode(data.getStudentCode());
                                                                UserProfileRepository.update(data, accountId);
                                }

                                public static UserProfile readSpecifiedProfile(String username, String accountId,
                                                                                                Boolean cond)
                                                                                                throws AuthException,
                                                                                                ClassNotFoundException,
                                                                                                IOException,
                                                                                                NotFoundException,
                                                                                                NotFoundException,
                                                                                                SQLException {
                                                                // The user who have permission to view other profile or
                                                                // yourself
                                                                String username1 = UserAccountRepository.getNameById(accountId);
                                                                if (username.equals(username1) || cond) {
                                                                                                return UserProfileRepository.findByUsername(username);
                                                                }
                                                                throw new AuthException("FORBIDDEN");
                                }

                                /// TODO: We can use procedure sql
                                /// NOTE:
                                /// Create guild with one leader
                                public static void createGuild(String guildName, String guildCode, String username)
                                                                                                throws SQLException,
                                                                                                ClassNotFoundException,
                                                                                                IOException {
                                                                GuildRepository.create(guildCode, guildName);
                                                                RoleRepository.create(guildCode + "_Leader");
                                                                PermissionRepository.generateForNew(guildCode, RoleContext.GUILD);
                                                                PermissionRepository.addPermissionToRole(guildCode + "_Leader", guildCode
                                                                                                                                + ".*");
                                                                RoleRepository.addSpecifiedForUserWithPrefix(guildCode, username,
                                                                                                                                "Leader");
                                }
                                /// TODO: Guild delete

                                public static List<String> getGuilds(Boolean cond, String accountId)
                                                                                                throws SQLException {
                                                                if (cond) {
                                                                                                return GuildRepository.getAll();
                                                                } else {
                                                                                                return GuildRepository.getAllOfUser(accountId);
                                                                }
                                }

                                /// NOTE:
                                /// Create crew with one leader
                                public static void createCrew(String crewName, String crewCode, String username)
                                                                                                throws SQLException,
                                                                                                ClassNotFoundException,
                                                                                                IOException {
                                                                CrewRepository.create(crewCode, crewName);
                                                                RoleRepository.create(crewCode + "_Leader");
                                                                PermissionRepository.generateForNew(crewCode, RoleContext.CREW);
                                                                PermissionRepository.addPermissionToRole(crewCode + "_Leader", crewCode
                                                                                                                                + ".*");
                                                                RoleRepository.addSpecifiedForUserWithPrefix(crewCode, username,
                                                                                                                                "Leader");
                                }

                                public static List<String> getCrews(Boolean cond, String accountId)
                                                                                                throws SQLException {
                                                                if (cond) {
                                                                                                return CrewRepository.getAll();
                                                                } else {
                                                                                                return CrewRepository.getAllOfUser(accountId);
                                                                }
                                }
}
