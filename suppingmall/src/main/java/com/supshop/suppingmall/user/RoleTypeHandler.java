package com.supshop.suppingmall.user;

import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleTypeHandler extends EnumTypeHandler<Role> {

    public RoleTypeHandler(Class<Role> type) {
        super(type);
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Role parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getCode());
    }

    @Override
    public Role getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String result = rs.getString(columnName);
        return Role.getCodeString(result);
    }

    @Override
    public Role getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String result = rs.getString(columnIndex);
        return Role.getCodeString(result);
    }

    @Override
    public Role getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String result = cs.getString(columnIndex);
        return Role.getCodeString(result);
    }
}
