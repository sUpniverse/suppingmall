package com.supshop.suppingmall.order;

import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderStatusTypeHandler extends EnumTypeHandler<Orders.OrderStatus> {

    public OrderStatusTypeHandler(Class<Orders.OrderStatus> type) {
        super(type);
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Orders.OrderStatus parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i,parameter.getCode());
    }

    @Override
    public Orders.OrderStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String result = rs.getString(columnName);
        return Orders.OrderStatus.getStatusByCode(result);
    }

    @Override
    public Orders.OrderStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String result = rs.getString(columnIndex);
        return Orders.OrderStatus.getStatusByCode(result);
    }

    @Override
    public Orders.OrderStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String result = cs.getString(columnIndex);
        return Orders.OrderStatus.getStatusByCode(result);
    }
}
