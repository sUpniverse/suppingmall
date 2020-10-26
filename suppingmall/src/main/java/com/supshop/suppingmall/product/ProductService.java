package com.supshop.suppingmall.product;

import com.mysql.cj.util.StringUtils;
import com.supshop.suppingmall.image.ImageService;
import com.supshop.suppingmall.mapper.ProductMapper;
import com.supshop.suppingmall.page.Criteria;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
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

    private static final int latestProductCount = 5;
    private static final int recommandProductCount = 8;

    public int getProductsCount(Criteria criteria) {
        return getProductsCount(null, null, null, null,criteria);
    }
    public int getProductsCount(Long categoryId,Long sellerId, ProductSearch search, Product.ProductStatus status,Criteria criteria) {
        return productMapper.findSaleProductCount(categoryId,sellerId,search,status,criteria);
    }

    // 물품 + 물품상세정보 + 물품 옵션
    public List<Product> getProducts() {
        return getProducts(null,null);
    }
    public List<Product> getProducts(ProductSearch search,Criteria criteria) {
        return productMapper.findAll(null,null, search,null,criteria);
    }

    // 전체 리스트 에서 오직 물품 정보만을 보여줄 때
    public List<Product> getOnSaleProductsOnMenu(Long categoryId, ProductSearch search, Criteria criteria) {
        return productMapper.findAllPart(null,categoryId,search,Product.ProductStatus.SALE,criteria);
    }

    // 전체 리스트 에서 상위 카테고리로 조회하며 물품 가져올 때
    public List<Product> getOnSaleProductsByParentCategoryOnMenu(Long parentId, ProductSearch search,Criteria criteria) {
        return productMapper.findAllPartByParentCategory(parentId, search,criteria);
    }


    // 물품 + 물품상세정보 + 물품 옵션 by 판매자 (판매자의 판매물품 정보 전체)
    public List<Product> getProductsBySeller(Long sellerId, ProductSearch search,Criteria criteria) {
        return productMapper.findAll(sellerId,null,search,null,criteria);
    }


    public Product getProduct(Long id) {
      return productMapper.findOne(id);
    }




    // 판매량이 가장 높은 순으로 검색
    public List<Product> getProductsOrderByOrdersQuantity() {
        return productMapper.findAllOrderByOrdersQuantity();
    }

    // 추천 물품
    public List<Product> getRecommendProducts() {
        return productMapper.findByRecommend();
    }


    @Transactional
    public void createProduct(Product product, Set<String> urls) throws FileNotFoundException {
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

        // product의 create가 성공이면, 물품들의 이미지 url을 가져와 Stroage에 저장하도록 함
        if(result == 1 && !StringUtils.isNullOrEmpty(product.getThumbnail())) {
            if(urls == null) urls = new HashSet<>();
            urls.add(product.getThumbnail());   // 썸네일 이미지도 포함
            String originUrl = setProductImageUrl(product, urls);
            boolean isSave = imageService.saveInStorage(urls, originUrl, product.getProductId(), productName);
            if(!isSave) {
                // Todo : 파일 저장 실패 exception && 시간초과 (지금은 단지 3번 실패 후 exception)
                int count  = 0;
                while (!isSave && count < 3){
                    isSave = imageService.saveInStorage(urls, originUrl, product.getProductId(), productName);
                    count++;
                }

                if(!isSave) throw new FileNotFoundException();
            }
            productMapper.updateProduct(productId,product);
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
