package com.skipjaq.webcrawler.web;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skipjaq.webcrawler.WebPageRecord;
import com.skipjaq.webcrawler.Webcrawler;
import com.skipjaq.webcrawler.web.repositories.TaskRepository;
import com.skipjaq.webcrawler.web.repositories.WebPageRecordRepository;

@Service
public class WebcrawlerService {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(WebcrawlerService.class);
    @Autowired
    Webcrawler webcrawler;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    WebPageRecordRepository webPageRecordRepository;

    ExecutorService threadPool = Executors.newFixedThreadPool(1);

    @PostConstruct
    public void resumeUnfinished() {
        List<WebcrawlTask> unfinished = taskRepository.findAllByStatus(Status.UNFINISHED);
        unfinished.forEach(this::submit);

    }

    public void submit(WebcrawlTask submitedTask) {
        LOGGER.info("Submitted task {} witch deep {}", submitedTask.getBaseUrl(), submitedTask.getDeep());
        threadPool.submit(() -> {
            try {
                LOGGER.info("Submitted task {} witch deep {} started", submitedTask.getBaseUrl(), submitedTask.getDeep());
                Collection<WebPageRecord> result = webcrawler.parseRecursively(submitedTask.getBaseUrl(), submitedTask.getDeep());
                submitedTask.setStatus(Status.SUCCESS);
                submitedTask.setResult(result);

                webPageRecordRepository.save(result);
//                        .forEach(webPageRecordRepository::save);
                taskRepository.save(submitedTask);
                LOGGER.info("Task {} witch deep {} finished", submitedTask.getBaseUrl(), submitedTask.getDeep());
            } catch (IOException e) {
                LOGGER.error("Task {} witch deep {} fails", submitedTask.getBaseUrl(), submitedTask.getDeep());
                submitedTask.setStatus(Status.FAIL);
                taskRepository.save(submitedTask);

            } catch (Exception e) {
                LOGGER.error("", e);
            }
        });
    }
}
