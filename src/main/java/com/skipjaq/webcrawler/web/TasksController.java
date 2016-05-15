package com.skipjaq.webcrawler.web;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.skipjaq.webcrawler.web.repositories.TaskRepository;

@Controller
@RequestMapping("/tasks")
public class TasksController {
    @Autowired
    private WebcrawlerService webcrawlerService;
    @Autowired
    TaskRepository taskRepository;

    @PostConstruct
    public void init() {
        taskRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public WebcrawlTask addTask(@RequestParam String baseUrl, @RequestParam int deep) {
        WebcrawlTask task = taskRepository.save(new WebcrawlTask(baseUrl, deep));
        webcrawlerService.submit(task);
        return task;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<WebcrawlTask> getAll() {
        return taskRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    @ResponseBody()
    public WebcrawlTask getOne(@PathVariable Long id) {
        return taskRepository.findById(id);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/unfinished")
    @ResponseBody()
    public List<WebcrawlTask> getOne() {
        return taskRepository.readAllByResultNull();
    }

}