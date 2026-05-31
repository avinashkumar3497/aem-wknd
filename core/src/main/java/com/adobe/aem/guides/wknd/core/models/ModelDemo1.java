package com.adobe.aem.guides.wknd.core.models;
import java.util.List;
import java.util.Map;

public interface ModelDemo1 {
    String getName();
    int getScore();
    String getTitle();
    String getReqAttribute();
    List<String> getBooks();
    Map<String,Integer> getMarks();
    List<Map<String,String>> getBookNames();
}
