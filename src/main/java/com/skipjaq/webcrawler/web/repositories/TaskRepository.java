package com.skipjaq.webcrawler.web.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.skipjaq.webcrawler.web.Status;
import com.skipjaq.webcrawler.web.WebcrawlTask;

public interface TaskRepository extends CrudRepository<WebcrawlTask, Long> {
    List<WebcrawlTask> findAll();

    WebcrawlTask findById(Long id);

    List<WebcrawlTask> readAllByResultNull();

    List<WebcrawlTask> findAllByStatus(Status status);
}
