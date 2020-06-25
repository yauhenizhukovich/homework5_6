package com.gmail.supersonicleader.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.gmail.supersonicleader.repository.model.User;

public interface UserRepository extends GeneralRepository<User> {

    List<User> findAll(Connection connection) throws SQLException;

}
