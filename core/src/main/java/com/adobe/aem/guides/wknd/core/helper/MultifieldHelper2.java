package com.adobe.aem.guides.wknd.core.helper;

public class MultifieldHelper2 {
    private String bookname;
    private String bookpublisher;
    private Integer copies;
    public MultifieldHelper2(String bookname, String bookpublisher, Integer copies) {
        this.bookname = bookname;
        this.bookpublisher = bookpublisher;
        this.copies = copies;
    }
    public String getBookname() {
        return bookname;
    }
    public String getBookpublisher() {
        return bookpublisher;
    }
    public Integer getCopies() {
        return copies;
    }
  
    
}
