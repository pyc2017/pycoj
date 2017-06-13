package com.pycoj.entity;

import org.springframework.stereotype.Component;

/**
 * Created by Heyman on 2017/5/4.
 */
@Component
public class Question {
    private int id;
    private String title;
    private String description;
    private int submit;
    private String tag;
    private String input;
    private String output;
    private String hint;
    private boolean accessable;

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getSubmit() {
        return submit;
    }

    public void setSubmit(int submit) {
        this.submit = submit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAccessable() {
        return accessable;
    }

    public void setAccessable(boolean accessable) {
        this.accessable = accessable;
    }
}
