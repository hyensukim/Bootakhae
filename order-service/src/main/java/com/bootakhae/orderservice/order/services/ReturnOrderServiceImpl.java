package com.bootakhae.orderservice.order.services;

import com.bootakhae.orderservice.order.dto.ReturnOrderDto;
import com.bootakhae.orderservice.order.entities.ReturnOrderEntity;
import com.bootakhae.orderservice.order.repositories.ReturnOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReturnOrderServiceImpl implements ReturnOrderService {

    private final ReturnOrderRepository returnOrderRepository;

    @Override
    public List<ReturnOrderDto> getReturnOrders(int nowPage, int pageSize) {
        Page<ReturnOrderEntity> returnList = returnOrderRepository.findAll(PageRequest.of(nowPage,pageSize));
        return returnList.stream().map(ReturnOrderEntity::entityToDto).toList();
    }
}
