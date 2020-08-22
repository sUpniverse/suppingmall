package com.supshop.suppingmall.product;

import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductStatusTypeHandler extends EnumTypeHandler<Product.ProductStatus> {

    public ProductStatusTypeHandler(Class<Product.ProductStatus> type) { super(type); }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Product.ProductStatus parameter, JdbcType jdbcType) throws SQLException {
        super.setNonNullParameter(ps, i, parameter, jdbcType);
        ps.setString(i, parameter.getCode());
    }

    @Override
    public Product.ProductStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String result = rs.getString(columnName);
        return Product.ProductStatus.getCode(result);
    }

    @Override
    public Product.ProductStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String result = rs.getString(columnIndex);
        return Product.ProductStatus.getCode(result);
    }

    @Override
    public Product.ProductStatus getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String result = cs.getString(columnIndex);
        return Product.ProductStatus.getCode(result);
    }
}
