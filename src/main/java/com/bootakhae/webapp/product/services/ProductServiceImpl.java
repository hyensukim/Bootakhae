package com.bootakhae.webapp.product.services;

import com.bootakhae.webapp.product.dto.ProductDto;
import com.bootakhae.webapp.product.entities.ProductEntity;
import com.bootakhae.webapp.product.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    @Override
    public ProductDto registerProduct(ProductDto productDetails) {
        ProductEntity product = productDetails.dtoToEntity();
        product = productRepository.save(product);
        return product.entityToDto();
    }

    @Override
    public ProductDto getOneProduct(String productId) {
        ProductEntity product = productRepository.findByProductId(productId).orElseThrow(
                () -> new RuntimeException("상품 조회 : 없는 상품입니다.")
        );
        return product.entityToDto();
    }

    @Override
    public List<ProductDto> getAllProducts(int nowPage, int pageSize) {
        PageRequest pageRequest = PageRequest.of(nowPage, pageSize, Sort.by("createdAt").descending());
        Page<ProductEntity> pageList = productRepository.findAll(pageRequest);
        return pageList.stream().map(ProductEntity::entityToDto).toList();
    }
}
