package com.bootakhae.productservice.services;

import com.bootakhae.productservice.dto.ProductDto;
import com.bootakhae.productservice.dto.ProductListDto;
import com.bootakhae.productservice.entities.ProductEntity;
import com.bootakhae.productservice.global.exception.CustomException;
import com.bootakhae.productservice.global.exception.ErrorCode;
import com.bootakhae.productservice.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

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
                .nowPage(nowPage)
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
}
