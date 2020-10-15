package com.supshop.suppingmall.delivery;

import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeliveryStatusHandler extends EnumTypeHandler<Delivery.DeliveryStatus> {
    public DeliveryStatusHandler(Class<Delivery.DeliveryStatus> type) {
        super(type);
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Delivery.DeliveryStatus parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getCode());
    }

    @Override
    public Delivery.DeliveryStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String result = rs.getString(columnName);
        return Delivery.DeliveryStatus.getStatusByCode(result);
    }

    @Override
    public Delivery.DeliveryStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String result = rs.getString(columnIndex);
        return Delivery.DeliveryStatus.getStatusByCode(result);
    }

    @Override
    public Delivery.DeliveryStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String result = cs.getString(columnIndex);
        return Delivery.DeliveryStatus.getStatusByCode(result);
    }
}
