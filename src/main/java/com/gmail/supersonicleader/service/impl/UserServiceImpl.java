package com.gmail.supersonicleader.service.impl;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gmail.supersonicleader.repository.ConnectionRepository;
import com.gmail.supersonicleader.repository.UserGroupRepository;
import com.gmail.supersonicleader.repository.UserInformationRepository;
import com.gmail.supersonicleader.repository.UserRepository;
import com.gmail.supersonicleader.repository.constant.DatabaseConstant;
import com.gmail.supersonicleader.repository.impl.ConnectionRepositoryImpl;
import com.gmail.supersonicleader.repository.impl.UserGroupRepositoryImpl;
import com.gmail.supersonicleader.repository.impl.UserInformationRepositoryImpl;
import com.gmail.supersonicleader.repository.impl.UserRepositoryImpl;
import com.gmail.supersonicleader.repository.model.User;
import com.gmail.supersonicleader.repository.model.UserGroup;
import com.gmail.supersonicleader.repository.model.UserInformation;
import com.gmail.supersonicleader.service.UserService;
import com.gmail.supersonicleader.service.exception.AddUserException;
import com.gmail.supersonicleader.service.exception.AddUserGroupException;
import com.gmail.supersonicleader.service.exception.CreateTableException;
import com.gmail.supersonicleader.service.exception.DeleteUserException;
import com.gmail.supersonicleader.service.exception.DropTableException;
import com.gmail.supersonicleader.service.exception.FindDataException;
import com.gmail.supersonicleader.service.exception.UpdateUserException;
import com.gmail.supersonicleader.service.model.CountOfUsersByGroupDTO;
import com.gmail.supersonicleader.service.model.PrintUserDTO;
import com.gmail.supersonicleader.service.model.UserDTO;
import com.gmail.supersonicleader.service.model.UserGroupDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserServiceImpl implements UserService {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private ConnectionRepository connectionRepository = ConnectionRepositoryImpl.getInstance();
    private UserRepository userRepository = UserRepositoryImpl.getInstance();
    private UserGroupRepository userGroupRepository = UserGroupRepositoryImpl.getInstance();
    private UserInformationRepository userInformationRepository = UserInformationRepositoryImpl.getInstance();

    private static UserService instance;

    private UserServiceImpl() {
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserServiceImpl();
        }
        return instance;
    }

    @Override
    public void dropTableByName(String tableName) throws DropTableException {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                dropTableByName(tableName, connection);
                connection.commit();
                logger.info("Table \"" + tableName + "\" successfully dropped or not exists.");
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DropTableException("Table \"" + tableName + "\" failed to drop.");
        }
    }

    private void dropTableByName(String tableName, Connection connection) throws SQLException {
        switch (tableName) {
            case DatabaseConstant.USER_TABLE_NAME:
                userRepository.dropTable(connection);
                break;
            case DatabaseConstant.USER_GROUP_TABLE_NAME:
                userGroupRepository.dropTable(connection);
                break;
            case DatabaseConstant.USER_INFORMATION_TABLE_NAME:
                userInformationRepository.dropTable(connection);
                break;
            default:
                throw new IllegalArgumentException("There are no tables with that name.");
        }
    }

    @Override
    public void createTableByName(String tableName) throws CreateTableException {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                createTableByName(tableName, connection);
                connection.commit();
                logger.info("Table \"" + tableName + "\" created successfully.");
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new CreateTableException("Table \"" + tableName + "\" failed to create.");
        }
    }

    private void createTableByName(String tableName, Connection connection) throws SQLException {
        switch (tableName) {
            case DatabaseConstant.USER_TABLE_NAME:
                userRepository.createTable(connection);
                break;
            case DatabaseConstant.USER_GROUP_TABLE_NAME:
                userGroupRepository.createTable(connection);
                break;
            case DatabaseConstant.USER_INFORMATION_TABLE_NAME:
                userInformationRepository.createTable(connection);
                break;
            default:
                throw new IllegalArgumentException("You cannot create table with this name.");
        }
    }

    @Override
    public UserGroupDTO addUserGroup(UserGroupDTO userGroupDTO) throws AddUserGroupException {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                UserGroup userGroup = convertDTOToUserGroup(userGroupDTO);
                userGroup = userGroupRepository.add(connection, userGroup);
                userGroupDTO = convertUserGroupToDTO(userGroup);
                connection.commit();
                return userGroupDTO;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new AddUserGroupException("User group failed to add.");
        }
        return null;
    }

    private UserGroup convertDTOToUserGroup(UserGroupDTO userGroupDTO) {
        return UserGroup.newBuilder().name(userGroupDTO.getName()).build();
    }

    private UserGroupDTO convertUserGroupToDTO(UserGroup userGroup) {
        return UserGroupDTO.newBuilder().id(userGroup.getId()).name(userGroup.getName()).build();
    }

    @Override
    public UserDTO addUser(UserDTO userDTO) throws AddUserException {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                User user = convertDTOToUser(userDTO);
                UserInformation userInformation = convertDTOToUserInformation(userDTO);
                user = userRepository.add(connection, user);
                userInformation.setUserId(user.getId());
                userInformation = userInformationRepository.add(connection, userInformation);
                userDTO = convertUserToDTO(user, userInformation);
                connection.commit();
                return userDTO;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new AddUserException("User failed to add.");
        }
        return null;
    }

    private UserDTO convertUserToDTO(User user, UserInformation userInformation) {
        return UserDTO.newBuilder()
                .username(user.getUsername())
                .password(user.getPassword())
                .isActive(user.isActive())
                .userGroupId(user.getUserGroupId())
                .age(user.getAge())
                .address(userInformation.getAddress())
                .telephone(userInformation.getTelephone())
                .build();
    }

    private UserInformation convertDTOToUserInformation(UserDTO userDTO) {
        return UserInformation.newBuilder().address(userDTO.getAddress()).telephone(userDTO.getTelephone()).build();
    }

    private User convertDTOToUser(UserDTO userDTO) {
        return User.newBuilder()
                .username(userDTO.getUsername())
                .password(userDTO.getPassword())
                .isActive(userDTO.isActive())
                .userGroupId(userDTO.getUserGroupId())
                .age(userDTO.getAge())
                .build();
    }

    @Override
    public List<PrintUserDTO> findAllUsers() throws FindDataException {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                List<PrintUserDTO> usersDTO = findAllUsers(connection);
                connection.commit();
                return usersDTO;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new FindDataException("Finding users failed.");
        }
        return null;
    }

    private List<PrintUserDTO> findAllUsers(Connection connection) throws SQLException {
        List<PrintUserDTO> usersDTO = new ArrayList<>();
        List<User> users = userRepository.findAll(connection);
        Map<Integer, UserGroup> userGroups = userGroupRepository.findAll(connection);
        Map<Integer, UserInformation> usersInformation = userInformationRepository.findAll(connection);
        for (User user : users) {
            usersDTO.add(PrintUserDTO.newBuilder()
                    .id(user.getId())
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .isActive(user.isActive())
                    .age(user.getAge())
                    .userGroupName(userGroups.get(user.getUserGroupId()).getName())
                    .address(usersInformation.get(user.getId()).getAddress())
                    .telephone(usersInformation.get(user.getId()).getTelephone())
                    .build());
        }
        usersDTO.forEach(userDTO -> logger.info(userDTO.toString()));
        return usersDTO;
    }

    @Override
    public List<CountOfUsersByGroupDTO> findCountOfUsersByGroup() throws FindDataException {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                List<CountOfUsersByGroupDTO> countOfUsersByGroupDTO = findCountOfUsersByGroup(connection);
                connection.commit();
                return countOfUsersByGroupDTO;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new FindDataException("Finding user group information failed.");
        }
        return null;
    }

    private List<CountOfUsersByGroupDTO> findCountOfUsersByGroup(Connection connection) throws SQLException {
        List<CountOfUsersByGroupDTO> countOfUsersByGroupDTO = new ArrayList<>();
        List<User> users = userRepository.findAll(connection);
        Map<Integer, UserGroup> userGroups = userGroupRepository.findAll(connection);
        for (Map.Entry<Integer, UserGroup> userGroupEntry : userGroups.entrySet()) {
            long count = users.stream().filter(user -> user.getUserGroupId() == userGroupEntry.getKey()).count();
            countOfUsersByGroupDTO.add(CountOfUsersByGroupDTO.newBuilder()
                    .name(userGroupEntry.getValue().getName())
                    .count((int) count).build());
        }
        countOfUsersByGroupDTO.forEach(count -> logger.info(count.toString()));
        return countOfUsersByGroupDTO;
    }

    @Override
    public int deleteUsersWithAgeLessThan(int age) throws DeleteUserException {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                int count = deleteUsersWithAgeLessThan(age, connection);
                connection.commit();
                return count;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DeleteUserException("Failed to delete users.");
        }
        return -1;
    }

    private int deleteUsersWithAgeLessThan(int age, Connection connection) throws SQLException {
        List<User> users = userRepository.findAll(connection);
        final int[] count = {0};
        users.stream()
                .filter(user -> user.getAge() < age)
                .forEach(user -> {
                    try {
                        userInformationRepository.delete(connection, user.getId());
                        userRepository.delete(connection, user.getId());
                        count[0]++;
                    } catch (SQLException e) {
                        logger.error(e.getMessage(), e);
                    }
                });
        logger.info("Count of deleted rows: " + count[0]);
        return count[0];
    }

    @Override
    public void updateUserActivityByAge(int minAge, int maxAge, boolean isActive) throws UpdateUserException {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                updateUserActivityByAge(minAge, maxAge, isActive, connection);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new UpdateUserException("Failed to update users.");
        }
    }

    private void updateUserActivityByAge(int minAge, int maxAge, boolean isActive, Connection connection) throws SQLException {
        List<User> users = userRepository.findAll(connection);
        users.stream().filter(user -> (user.getAge() >= minAge && user.getAge() <= maxAge) && user.isActive() != isActive)
                .forEach(user ->
                {
                    try {
                        userRepository.update(
                                connection,
                                User.newBuilder().id(user.getId())
                                        .username(user.getUsername())
                                        .password(user.getPassword())
                                        .isActive(isActive)
                                        .userGroupId(user.getUserGroupId())
                                        .age(user.getAge())
                                        .build());
                    } catch (SQLException e) {
                        logger.info(e.getMessage(), e);
                    }
                });
    }

}

