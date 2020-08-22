package com.supshop.suppingmall.product;

import com.supshop.suppingmall.category.Category;
import com.supshop.suppingmall.delivery.Delivery;
import com.supshop.suppingmall.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductFactory {

    @Autowired ProductService productService;

    public Product createProduct(String productName,User seller,Category category) {

        ProductDetail productDetail = ProductDetail.builder()
                .detailId(1)
                .manufacturer("apple")
                .origin("베트남")
                .asNumber("02-333-4444")
                .spec1("256gb ssd")
                .spec2("16gb 메모리")
                .spec3("16인치 디스플레이")
                .spec4("트랙패드")
                .spec5("usb c type port 4개")
                .build();

        List<ProductOption> productOptions = new ArrayList<>();
        ProductOption productOption1 = new ProductOption();
        productOption1.setOptionId(1);
        productOption1.setOptionName("256");
        productOption1.setPrice(0);
        productOption1.setQuantity(50);
        ProductOption productOption2 = new ProductOption();
        productOption2.setOptionId(2);
        productOption2.setOptionName("512");
        productOption2.setPrice(300000);
        productOption2.setQuantity(50);
        productOptions.add(productOption1);
        productOptions.add(productOption2);

        Product product = Product.builder()
                .category(category)
                .name(productName)
                .price(2000000)
                .detail(productDetail)
                .options(productOptions)
                .seller(seller)
                .deliveryVendor(Delivery.DeliveryVendor.CJ)
                .deliveryPrice(3000)
                .status(Product.ProductStatus.WAIT)
                .thumbnail("/images/product/20200819/18/맥북16.jpg")
                .contents("<img src=\"/images/product/20200819/18/현대차.png\">")
                .build();
        return product;
    }
}
