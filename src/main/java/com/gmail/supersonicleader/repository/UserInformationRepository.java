package com.gmail.supersonicleader.repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import com.gmail.supersonicleader.repository.model.UserInformation;

public interface UserInformationRepository extends GeneralRepository<UserInformation> {

    Map<Integer, UserInformation> findAll(Connection connection) throws SQLException;

}
