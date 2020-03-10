package com.supshop.suppingmall.payment;

import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PaymentGroupTypeHandler extends EnumTypeHandler<Payment.PayGroupType> {

    public PaymentGroupTypeHandler(Class<Payment.PayGroupType> type) {
        super(type);
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Payment.PayGroupType parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i,parameter.getCode());
    }

    @Override
    public Payment.PayGroupType getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String result = rs.getString(columnName);
        return Payment.PayGroupType.getCodeString(result);
    }

    @Override
    public Payment.PayGroupType getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String result = rs.getString(columnIndex);
        return Payment.PayGroupType.getCodeString(result);
    }

    @Override
    public Payment.PayGroupType getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String result = cs.getString(columnIndex);
        return Payment.PayGroupType.getCodeString(result);
    }
}
