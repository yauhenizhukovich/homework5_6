package com.gmail.supersonicleader.controller.impl;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import com.gmail.supersonicleader.controller.HomeWorkController;
import com.gmail.supersonicleader.repository.constant.DatabaseConstant;
import com.gmail.supersonicleader.service.UserService;
import com.gmail.supersonicleader.service.exception.AddUserException;
import com.gmail.supersonicleader.service.exception.AddUserGroupException;
import com.gmail.supersonicleader.service.exception.CreateTableException;
import com.gmail.supersonicleader.service.exception.DeleteUserException;
import com.gmail.supersonicleader.service.exception.DropTableException;
import com.gmail.supersonicleader.service.exception.FindDataException;
import com.gmail.supersonicleader.service.exception.UpdateUserException;
import com.gmail.supersonicleader.service.impl.UserServiceImpl;
import com.gmail.supersonicleader.service.model.UserDTO;
import com.gmail.supersonicleader.service.model.UserGroupDTO;
import com.gmail.supersonicleader.util.RandomUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HomeWorkControllerImpl implements HomeWorkController {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private static HomeWorkController instance;
    private static final int COUNT_OF_USER_GROUPS = 3;
    private static final int COUNT_OF_USERS = 30;
    private static final int USER_MIN_AGE = 25;
    private static final int USER_MAX_AGE = 35;
    private static final int AGE_TO_DELETE = 27;
    private static final int MIN_AGE_TO_UPDATE = 30;
    private static final int MAX_AGE_TO_UPDATE = 33;
    private static final boolean UPDATED_ACTIVITY = false;

    private HomeWorkControllerImpl() {
    }

    public static HomeWorkController getInstance() {
        if (instance == null) {
            instance = new HomeWorkControllerImpl();
        }
        return instance;
    }

    @Override
    public void runFirstTask() {
        UserService userService = UserServiceImpl.getInstance();

        try {
            userService.dropTableByName(DatabaseConstant.USER_INFORMATION_TABLE_NAME);
            userService.dropTableByName(DatabaseConstant.USER_TABLE_NAME);
            userService.dropTableByName(DatabaseConstant.USER_GROUP_TABLE_NAME);
        } catch (DropTableException e) {
            logger.error(e.getMessage(), e);
        }

        try {
            userService.createTableByName(DatabaseConstant.USER_GROUP_TABLE_NAME);
            userService.createTableByName(DatabaseConstant.USER_TABLE_NAME);
            userService.createTableByName(DatabaseConstant.USER_INFORMATION_TABLE_NAME);
        } catch (CreateTableException e) {
            logger.error(e.getMessage(), e);
        }

        List<UserGroupDTO> userGroups = new ArrayList<>();
        for (int i = 1; i <= COUNT_OF_USER_GROUPS; i++) {
            try {
                userGroups.add(userService.addUserGroup(
                        UserGroupDTO.newBuilder().name("Group" + i).build()
                ));
            } catch (AddUserGroupException e) {
                logger.error(e.getMessage(), e);
            }
        }

        List<UserDTO> users = new ArrayList<>();
        for (int i = 1; i <= COUNT_OF_USERS; i++) {
            try {
                users.add(userService.addUser(
                        UserDTO.newBuilder()
                                .username("Name" + i)
                                .password("1234" + i)
                                .isActive(RandomUtil.getRandomBooleanValue())
                                .userGroupId(getRandomUserGroup(userGroups).getId())
                                .age(RandomUtil.getElement(USER_MIN_AGE, USER_MAX_AGE))
                                .address("Address" + i)
                                .telephone("20000" + i)
                                .build()));
            } catch (AddUserException e) {
                logger.error(e.getMessage(), e);
            }
        }
        try {
            userService.findAllUsers();
            userService.findCountOfUsersByGroup();
            userService.deleteUsersWithAgeLessThan(AGE_TO_DELETE);
            userService.updateUserActivityByAge(MIN_AGE_TO_UPDATE, MAX_AGE_TO_UPDATE, UPDATED_ACTIVITY);
        } catch (FindDataException | UpdateUserException | DeleteUserException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private UserGroupDTO getRandomUserGroup(List<UserGroupDTO> userGroups) {
        return userGroups.get(RandomUtil.getElement(0, userGroups.size() - 1));
    }

}
