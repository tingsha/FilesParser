package com.naname.fileparser;

import lombok.Data;

@Data
public class Chapter {
    private String id;
    private String content;
    private boolean isTitle;
    private String tabs;

    public Chapter(String id, String content, boolean isTitle, String tabs) {
        this.id = id;
        this.content = content;
        this.isTitle = isTitle;
        this.tabs = tabs;
    }
}
