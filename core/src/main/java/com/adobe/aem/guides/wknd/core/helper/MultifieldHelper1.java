package com.adobe.aem.guides.wknd.core.helper;

import java.beans.JavaBean;

import org.apache.sling.api.resource.Resource;

public class MultifieldHelper1 {
    private String bookName;
    private Integer copies;
    public MultifieldHelper1(String bookName, Integer copies) {
        this.bookName = bookName;
        this.copies = copies;
    }
    public MultifieldHelper1(Resource entryNode) {
        bookName = entryNode.getValueMap().get("bookName",String.class);
        copies = entryNode.getValueMap().get("copies",Integer.class);
    }
    public String getBookName() {
        return bookName;
    }
    public Integer getCopies() {
        return copies;
    }
    public void setBookName(String bookName) {
        this.bookName = bookName;
    }
    public void setCopies(Integer copies) {
        this.copies = copies;
    }
}
