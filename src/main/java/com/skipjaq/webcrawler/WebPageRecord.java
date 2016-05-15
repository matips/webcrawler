package com.skipjaq.webcrawler;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class WebPageRecord implements Serializable {
    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @Column String url;
    @Column long contentSize;
    @Column byte[] md5;

    public WebPageRecord() {
    }

    @ElementCollection List<String> reachablePages;

    public WebPageRecord(String url, long contentSize, byte[] md5, List<String> reachablePages) {
        this.url = url;
        this.contentSize = contentSize;
        this.md5 = md5;
        this.reachablePages = reachablePages;
    }

    public String getUrl() {
        return url;
    }

    public long getContentSize() {
        return contentSize;
    }

    public byte[] getMd5() {
        return md5;
    }

    public List<String> getReachablePages() {
        return reachablePages;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
