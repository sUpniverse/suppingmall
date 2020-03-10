package com.supshop.suppingmall.payment;

import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PaymentStatusTypeHandler extends EnumTypeHandler<Payment.PaymentStatus> {

    public PaymentStatusTypeHandler(Class<Payment.PaymentStatus> type) {
        super(type);
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Payment.PaymentStatus parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i,parameter.getCode());
    }

    @Override
    public Payment.PaymentStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String result = rs.getString(columnName);
        return Payment.PaymentStatus.getCodeString(result);
    }

    @Override
    public Payment.PaymentStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String result = rs.getString(columnIndex);
        return Payment.PaymentStatus.getCodeString(result);
    }

    @Override
    public Payment.PaymentStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String result = cs.getString(columnIndex);
        return Payment.PaymentStatus.getCodeString(result);
    }
}
