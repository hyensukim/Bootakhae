package com.bootakhae.webapp.product.services;

import com.bootakhae.webapp.product.dto.ProductDto;
import com.bootakhae.webapp.product.entities.ProductEntity;
import com.bootakhae.webapp.product.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
        product = productRepository.save(product);
        return product.entityToDto();
    }

    @Override
    public ProductDto getOneProduct(String productId) {
        log.debug("상품 상세 조회 실행");
        ProductEntity product = productRepository.findByProductId(productId).orElseThrow(
                () -> new RuntimeException("상품 조회 : 없는 상품입니다.")
        );
        return product.entityToDto();
    }

    @Override
    public List<ProductDto> getAllProducts(int nowPage, int pageSize) {
        log.debug("상품 목록 조회 실행");
        PageRequest pageRequest = PageRequest.of(nowPage, pageSize, Sort.by("createdAt").descending());
        Page<ProductEntity> pageList = productRepository.findAll(pageRequest);
        return pageList.stream().map(ProductEntity::entityToDto).toList();
    }
}
