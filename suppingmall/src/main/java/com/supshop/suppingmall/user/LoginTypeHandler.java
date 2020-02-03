package com.supshop.suppingmall.user;

import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginTypeHandler extends EnumTypeHandler<User.LoginType> {

    public LoginTypeHandler(Class<User.LoginType> type) {
        super(type);
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, User.LoginType parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i,parameter.getCode());
    }

    @Override
    public User.LoginType getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String result = rs.getString(columnName);
        return User.LoginType.getCodeString(result);
    }

    @Override
    public User.LoginType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String result = rs.getString(columnIndex);
        return User.LoginType.getCodeString(result);
    }

    @Override
    public User.LoginType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String result = cs.getString(columnIndex);
        return User.LoginType.getCodeString(result);
    }
}
