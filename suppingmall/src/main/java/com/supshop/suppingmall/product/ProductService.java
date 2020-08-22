package com.supshop.suppingmall.product;

import com.supshop.suppingmall.image.ImageService;
import com.supshop.suppingmall.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductMapper productMapper;
    private final ImageService imageService;

    private static final String productImageUrl = "/images/product/";
    private static final String productName = "product";

    public List<Product> findAllProduct() {
        return productMapper.findProducts();
    }

    public List<Product> findAllProductOnSale() {
        return productMapper.findProductsOnSale();
    }

    public Product findProduct(Long id) {
      return productMapper.findProduct(id);
    }



    @Transactional
    public void createProduct(Product product, Set<String> urls) {
        int result = productMapper.insertProduct(product);
        Long productId = product.getProductId();
        List<ProductOption> options = product.getOptions();
        product.setStatus(Product.ProductStatus.SALE);
        for(ProductOption option : options) {
            option.setProductId(productId);
        }

        //식별 관계로 인해 productId가 필요하므로 product를 생성해 productId를 얻은 후 나머지 option들과 detail 정보를 넣음
        productMapper.addProductOptions(options);
        ProductDetail detail = product.getDetail();
        detail.setProductId(productId);
        productMapper.addProductDetail(detail);


        if(urls.size() > 0 && result == 1) {
            urls.add(product.getThumbnail());
            String originUrl = setProductImageUrl(product, urls);
            productMapper.updateProduct(productId,product);
            imageService.saveInStorage(urls,originUrl,product.getProductId(), productName);
        }
    }

    @Transactional
    public void updateProduct(Long id, Product product) {
        productMapper.updateProduct(id, product);
    }

    @Transactional
    public void updateProductOption(List<ProductOption> productOptionList) {
        productMapper.updateProductOption(productOptionList);
    }

    @Transactional
    public void deleteProduct(Long id) {
        productMapper.deleteProduct(id);
    }

    public List<Product> findProductsBySellerId(Long userId) {
        return productMapper.findProductsById(userId);
    }


    // image/product/{yyyyMMdd}/{userId}/fileName => image/product/{categoryId}/fileName
    // cloud storage의 경로 저장을 위해 이미지 url 변경
    public String setProductImageUrl(Product product, Set<String> urls) {
        String originUrl;
        String contents = product.getContents();
        String thumbnail = product.getThumbnail();

        String imageUrl = urls.iterator().next();
        String[] splitUrl = imageUrl.split(File.separator);
        int fileIndex = imageUrl.indexOf(splitUrl[splitUrl.length-1]);
        originUrl = imageUrl.substring(0,fileIndex);

        contents = contents.replace(originUrl, productImageUrl +product.getProductId()+File.separator);
        thumbnail = thumbnail.replace(originUrl, productImageUrl +product.getProductId()+File.separator);

        product.setContents(contents);
        product.setThumbnail(thumbnail);
        return originUrl;
    }

}
