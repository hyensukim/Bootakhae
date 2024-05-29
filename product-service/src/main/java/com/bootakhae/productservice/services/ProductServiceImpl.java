package com.bootakhae.productservice.services;

import com.bootakhae.productservice.dto.ProductDto;
import com.bootakhae.productservice.dto.ProductInfoDto;
import com.bootakhae.productservice.dto.ProductListDto;
import com.bootakhae.productservice.entities.ProductEntity;
import com.bootakhae.productservice.global.exception.CustomException;
import com.bootakhae.productservice.global.exception.ErrorCode;
import com.bootakhae.productservice.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final Environment env;

    @Transactional
    @Override
    @CacheEvict(value="PRODUCTS_CACHE", allEntries = true) // PRODUCTS_CACHE 라는 항목의 모든 캐시를 지웁니다.
    public ProductDto registerProduct(ProductDto productDetails) {
        log.debug("상품 등록 실행");
        ProductEntity product = productDetails.dtoToEntity();

        productRepository.findByNameAndProducer(product.getName(), product.getProducer()).ifPresent(
                p -> {
                    throw new CustomException(ErrorCode.DUPLICATED_PRODUCT);
                }
        );

        product = productRepository.save(product);

        return product.entityToDto();
    }

    @Transactional
    @CacheEvict(value="PRODUCTS_CACHE", allEntries = true) // PRODUCTS_CACHE 라는 항목의 모든 캐시를 지웁니다.
    @Override
    public void deleteProduct(String productId){
        log.debug("상품 삭제 실행");
        ProductEntity product = productRepository.findByProductId(productId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_REGISTERED_PRODUCT)
        );
        productRepository.delete(product);
    }

    @Transactional
    @Override
    public ProductListDto registerEventProduct(List<ProductDto> productDetailsList) {
        log.debug("이벤트 상품으로 등록 실행");
        List<String> productIds = productDetailsList
                .stream()
                .map(ProductDto::getProductId)
                .collect(Collectors.toList());

        List<ProductEntity> productList = productRepository.findAllByProductIdIn(productIds);

        Map<String, ProductEntity> productMap = new HashMap<>();

        for (ProductEntity productEntity : productList) {
            productMap.put(productEntity.getProductId(), productEntity);
        }

        // trouble-shooting : NullPointerException
        /*
         * 복수개의 요청사항이 들어올 때, 일부 DB 내 없는 데이터인 경우, null을 반환하기 때문에 해당 값이 null이 아닌 경우에만
         * eventTime 을 수정하도록 로직 변경. - Optional 활용
         */
        for (ProductDto productDetails : productDetailsList) {
            Optional.ofNullable(productMap.get(productDetails.getProductId()))
                    .ifPresent(p -> p.registerEventTime(productDetails.getEventTime()));
        }

        return ProductListDto.builder()
                .productList(productList.stream().map(ProductEntity::entityToDto).collect(Collectors.toList()))
                .build();
    }

    @Transactional
    @Override
    @Caching(evict = {@CacheEvict(value="PRODUCTS_CACHE", allEntries = true),
            @CacheEvict(value = "EVENT_PRODUCTS_CACHE", allEntries = true)})
    public void openEventProduct() {
        log.debug("이벤트 상품 오픈 실행");
        DateTimeFormatter eventDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        String dateTimeString = Objects.requireNonNull(env.getProperty("schedule.start-time")); // 예시 값입니다. 실제 사용하는 값으로 대체해야 합니다.
        LocalDateTime startTime = LocalDateTime.parse(dateTimeString, eventDateTimeFormatter);

        List<ProductEntity> productList = productRepository.findEventProductList(startTime, LocalDateTime.now());

        productList.forEach(ProductEntity::openThisEvent);
    }

    @Transactional
    @Override
    public List<ProductDto> checkAndDecreaseStock(List<ProductInfoDto> productInfoList) {
        log.debug("상품 재고 감소 실행");
        Map<String,Long> productMap = new HashMap<>();
        List<String> productIds = new ArrayList<>();

        productInfoList.forEach(productInfo -> {
            productIds.add(productInfo.getProductId());
            productMap.put(productInfo.getProductId(), productInfo.getQty());}
        );

        List<ProductEntity> productList = productRepository.findAllByProductIdIn(productIds);

        for(ProductEntity product : productList) {
            Long qty = productMap.get(product.getProductId());
            product.decreaseStock(qty);
        }

        productList = productRepository.saveAllAndFlush(productList);

        return  productList.stream().map(p -> {
            long qty = productMap.get(p.getProductId());
            return p.entityToDto(qty);
        }).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void restoreStock(List<ProductInfoDto> productInfoList) {
        log.debug("상품 재고 복구 실행");
        List<String> productIds = productInfoList
                .stream()
                .map(ProductInfoDto::getProductId)
                .collect(Collectors.toList());

        List<ProductEntity> productList = productRepository.findAllByProductIdIn(productIds);

        Map<String, Long> productMap = productInfoList
                .stream()
                .collect(Collectors.toMap(ProductInfoDto::getProductId,ProductInfoDto::getQty));

        for(ProductEntity product : productList) {
            Long qty = productMap.get(product.getProductId());
            product.restoreStock(qty);
        }
    }

    @Override
    public ProductDto getOneProduct(String productId) {
        log.debug("상품 상세 조회 실행");
        ProductEntity product = productRepository.findByProductId(productId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_REGISTERED_PRODUCT)
        );
        return product.entityToDto();
    }

    @Cacheable(value="PRODUCTS_CACHE", key="'nowPage:' + #nowPage")
    @Override
    public ProductListDto getNormalProducts(int nowPage, int pageSize) {
        log.debug("상품 목록 조회 실행");
        PageRequest pageRequest = PageRequest.of(nowPage, pageSize, Sort.by("createdAt").descending());
        Page<ProductEntity> pageList = productRepository.findByIsEvent(false,pageRequest);
        return ProductListDto.builder()
                .totalPages(pageList.getTotalPages())
                .totalProducts(pageList.getTotalElements())
                .productList(pageList.stream().map(ProductEntity::entityToDto).toList())
                .build();
    }

    @Cacheable(value="EVENT_PRODUCTS_CACHE", key="'nowPage:' + #nowPage")
    @Override
    public ProductListDto getEventProducts(int nowPage, int pageSize){
        log.debug("이벤트 상품 목록 조회 실행");
        PageRequest pageRequest = PageRequest.of(nowPage, pageSize, Sort.by("createdAt").descending());
        Page<ProductEntity> pageList = productRepository.findByIsEvent(true,pageRequest);
        return ProductListDto.builder()
                .totalPages(pageList.getTotalPages())
                .totalProducts(pageList.getTotalElements())
                .productList(pageList.stream().map(ProductEntity::entityToDto).toList())
                .build();
    }


    // test
    @Transactional
    @Override
//    public synchronized ProductDto decreaseStock(String productId, Long qty) {
//        log.debug("synchronized - 상품 재고 감소 실행");
    public ProductDto decreaseStockTest(String productId, Long qty) {
        log.debug("테스트 - 상품 재고 감소 실행");
        ProductEntity product = productRepository.findByProductId(productId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_REGISTERED_PRODUCT)
        );

        product.decreaseStock(qty);

        return productRepository.saveAndFlush(product).entityToDto();
    }

    // test
    @Transactional
    @Override
    public ProductDto decreaseStockPessimistic(String productId, Long qty) {
        log.debug("비관적 락 - 상품 재고 감소 실행");
        ProductEntity product = productRepository.findByProductIdPessimistic(productId).orElseThrow();
        product.decreaseStock(qty);
        return productRepository.saveAndFlush(product).entityToDto();
    }

    // test
    @Transactional
    @Override
    public void decreaseStockOptimistic(String productId, Long qty) {
        log.debug("낙관적 락 - 상품 재고 감소 실행");
        ProductEntity product = productRepository.findByProductIdOptimistic(productId).orElseThrow();
        product.decreaseStock(qty);
        productRepository.saveAndFlush(product).entityToDto();
    }
}
