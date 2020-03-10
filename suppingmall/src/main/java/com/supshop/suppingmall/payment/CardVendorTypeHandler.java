package com.supshop.suppingmall.payment;

import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CardVendorTypeHandler extends EnumTypeHandler<Payment.CardVendor> {

    public CardVendorTypeHandler(Class<Payment.CardVendor> type) {
        super(type);
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Payment.CardVendor parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i,parameter.getCode());
    }

    @Override
    public Payment.CardVendor getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String result = rs.getString(columnName);
        return Payment.CardVendor.getCodeString(result);
    }

    @Override
    public Payment.CardVendor getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String result = rs.getString(columnIndex);
        return Payment.CardVendor.getCodeString(result);
    }

    @Override
    public Payment.CardVendor getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String result = cs.getString(columnIndex);
        return Payment.CardVendor.getCodeString(result);
    }
}
