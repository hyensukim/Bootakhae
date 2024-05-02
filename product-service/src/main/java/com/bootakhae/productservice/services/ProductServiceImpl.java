package com.bootakhae.productservice.services;

import com.bootakhae.productservice.dto.ProductDto;
import com.bootakhae.productservice.dto.ProductListDto;
import com.bootakhae.productservice.entities.ProductEntity;
import com.bootakhae.productservice.global.exception.CustomException;
import com.bootakhae.productservice.global.exception.ErrorCode;
import com.bootakhae.productservice.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final Environment env;

    @Override
    public ProductDto registerProduct(ProductDto productDetails) {
        log.debug("상품 등록 실행");
        ProductEntity product = productDetails.dtoToEntity();

        productRepository.findByNameAndProducer(product.getName(), product.getProducer()).ifPresent(
                p -> {throw new CustomException(ErrorCode.DUPLICATED_PRODUCT);}
        );

        product = productRepository.save(product);

        return product.entityToDto();
    }

    @Transactional
    @Override
    public ProductListDto registerEventProduct(List<ProductDto> productDetailsList) {
        List<String> productIds = productDetailsList
                .stream()
                .map(ProductDto::getProductId)
                .collect(Collectors.toList());

        List<ProductEntity> productList = productRepository.findAllByProductIdIn(productIds);

        Map<String, ProductEntity> productMap = new HashMap<>();
        for(ProductEntity productEntity : productList) {
            productMap.put(productEntity.getProductId(), productEntity);
        }
        
        // trouble-shooting : NullPointerException
        /*
         * 복수개의 요청사항이 들어올 때, 일부 DB 내 없는 데이터인 경우, null을 반환하기 때문에 해당 값이 null이 아닌 경우에만
         * eventTime 을 수정하도록 로직 변경. - Optional 활용
         */
        for(ProductDto productDetails : productDetailsList) {
            Optional.ofNullable(productMap.get(productDetails.getProductId()))
                    .ifPresent(p -> p.registerEventTime(productDetails.getEventTime()));
        }


        return ProductListDto.builder()
                .productList(productList.stream().map(ProductEntity::entityToDto).collect(Collectors.toList()))
                .build();
    }

    @Override
    public ProductDto getOneProduct(String productId) {
        log.debug("상품 상세 조회 실행");
        ProductEntity product = productRepository.findByProductId(productId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_REGISTERED_PRODUCT)
        );
        return product.entityToDto();
    }

    @Override
    public ProductListDto getAllProducts(int nowPage, int pageSize) {
        log.debug("상품 목록 조회 실행");
        PageRequest pageRequest = PageRequest.of(nowPage, pageSize, Sort.by("createdAt").descending());
        Page<ProductEntity> pageList = productRepository.findAll(pageRequest);
        return ProductListDto.builder()
                .totalPages(pageList.getTotalPages())
                .totalProducts(pageList.getTotalElements())
                .productList(pageList.stream().map(ProductEntity::entityToDto).toList())
                .build();
    }

    @Override
    public List<ProductDto> getAllByProductIds(List<String> productIds) {
        log.debug("위시 리스트에 등록된 상품 목록 조회 실행");
        List<ProductEntity> productList = productRepository.findAllByProductIdIn(productIds);

        if (productList.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_EXISTS_WISHLIST);
        }

        return productList.stream().map(ProductEntity::entityToDto).toList();
    }

    @Transactional
    @Override
    public ProductDto updateStock(String productId, Long stock) {
        log.debug("상품 재고 수량 변경 실행");
        ProductEntity product = productRepository.findByProductId(productId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_REGISTERED_PRODUCT)
        );
        product.updateStock(stock);

        return product.entityToDto();
    }

    @Scheduled(cron = "${schedule.cron}")
    @Transactional
    public void changeEventOpenFlag() {
        log.info("상품 이벤트 상태 업데이트 실행");

        String format = Objects.requireNonNull(env.getProperty("schedule.start-time"));
        DateTimeFormatter eventDateTime = DateTimeFormatter.ofPattern(format);
        LocalDateTime startTime = LocalDateTime.parse("yyyy-MM-dd HH:mm", eventDateTime);

        List<ProductEntity> productList = productRepository.findEventProductList(startTime, LocalDateTime.now());

        // event open
        productList.forEach(ProductEntity::openThisEvent);
    }
}
