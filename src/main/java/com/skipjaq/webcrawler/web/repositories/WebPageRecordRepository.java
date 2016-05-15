package com.skipjaq.webcrawler.web.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.skipjaq.webcrawler.WebPageRecord;

public interface WebPageRecordRepository extends CrudRepository<WebPageRecord, Long> {
    List<WebPageRecord> findAll();
}
