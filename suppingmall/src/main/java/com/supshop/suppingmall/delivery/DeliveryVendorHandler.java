package com.supshop.suppingmall.delivery;

import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DeliveryVendorHandler extends EnumTypeHandler<Delivery.DeliveryVendor> {
    public DeliveryVendorHandler(Class<Delivery.DeliveryVendor> type) {
        super(type);
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Delivery.DeliveryVendor parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getCode());
    }

    @Override
    public Delivery.DeliveryVendor getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String result = rs.getString(columnName);
        return Delivery.DeliveryVendor.getCode(result);
    }

    @Override
    public Delivery.DeliveryVendor getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String result = rs.getString(columnIndex);
        return Delivery.DeliveryVendor.getCode(result);
    }

    @Override
    public Delivery.DeliveryVendor getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String result = cs.getString(columnIndex);
        return Delivery.DeliveryVendor.getCode(result);
    }
}
