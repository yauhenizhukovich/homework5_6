package com.gmail.supersonicleader.service;

import java.util.List;

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

public interface UserService {

    void dropTableByName(String tableName) throws DropTableException;

    void createTableByName(String tableName) throws CreateTableException;

    UserGroupDTO addUserGroup(UserGroupDTO userGroupDTO) throws AddUserGroupException;

    UserDTO addUser(UserDTO userDTO) throws AddUserException;

    List<PrintUserDTO> findAllUsers() throws FindDataException;

    List<CountOfUsersByGroupDTO> findCountOfUsersByGroup() throws FindDataException;

    int deleteUsersWithAgeLessThan(int age) throws DeleteUserException;

    void updateUserActivityByAge(int minAge, int maxAge, boolean isActive) throws UpdateUserException;

}
