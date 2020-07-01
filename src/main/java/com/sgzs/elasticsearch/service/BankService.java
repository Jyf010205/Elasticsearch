package com.sgzs.elasticsearch.service;

import com.sgzs.elasticsearch.entity.Bank;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author: jianyufeng
 * @description:
 * @date: 2020/6/30 15:12
 */
public interface BankService extends ElasticsearchRepository<Bank,Long> {
}
