package com.skipjaq.webcrawler.web;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skipjaq.webcrawler.WebPageRecord;

@Entity
public class WebcrawlTask {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String baseUrl;

    @Column(nullable = false)
    private Status status = Status.UNFINISHED;

    @OneToMany(cascade = CascadeType.PERSIST)
    private Set<WebPageRecord> result;

    @Column
    private int deep;

    public WebcrawlTask() {
    }

    public WebcrawlTask(String baseUrl, int deep) {
        this.baseUrl = baseUrl;
        this.deep = deep;
    }

    public Status getStatus() {
        return status;
    }

    public Long getId() {
        return id;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Collection<WebPageRecord> getResult() {
        return result;
    }

    public void setResult(Collection<WebPageRecord> result) {
        this.result = new HashSet<>(result);
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getDeep() {
        return deep;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        WebcrawlTask task = (WebcrawlTask) o;

        return id != null ? id.equals(task.id) : task.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
