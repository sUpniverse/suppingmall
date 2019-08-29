package com.supshop.suppingmall.user;

import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleTypeHandler extends EnumTypeHandler<User.Role> {

    public RoleTypeHandler(Class<User.Role> type) {
        super(type);
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, User.Role parameter, JdbcType jdbcType) throws SQLException {
        if(parameter != null) {
            ps.setString(i, parameter.getCode());
        } else {
            ps.setString(i, null);
        }
    }

    @Override
    public User.Role getResult(ResultSet rs, String columnName) throws SQLException {
        String result = rs.getString(columnName);
        try {
            return User.Role.getCodeString(result);
        } catch (IllegalArgumentException e) {
            return null;
        }

    }

    @Override
    public User.Role getResult(ResultSet rs, int columnIndex) throws SQLException {
        String result = rs.getString(columnIndex);
        try {
            return User.Role.getCodeString(result);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public User.Role getResult(CallableStatement cs, int columnIndex) throws SQLException {
        String result = cs.getString(columnIndex);
        try {
            return User.Role.getCodeString(result);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
