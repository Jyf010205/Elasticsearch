package com.sgzs.elasticsearch.service;


import com.sgzs.elasticsearch.entity.Item;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @author: jianyufeng
 * @description:
 * @date: 2020/6/30 14:18
 */
public interface ItemService extends ElasticsearchRepository<Item,Long> {
    /**
     * 根据价格区间查询
     * @param price1
     * @param price2
     * @return
     */
    List<Item> findByPriceBetween(double price1,double price2);
}
