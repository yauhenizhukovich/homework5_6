package com.gmail.supersonicleader.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import com.gmail.supersonicleader.repository.model.UserGroup;

public interface UserGroupRepository extends GeneralRepository<UserGroup> {

    Map<Integer, UserGroup> findAll(Connection connection) throws SQLException;

}
