package com.adobe.aem.guides.wknd.core.models.impl;



import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.guides.wknd.core.models.ModelDemo1;
import com.day.cq.wcm.api.PageManager;

@Model(
    adaptables = SlingHttpServletRequest.class,
    adapters = ModelDemo1.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ModelDemoImpl1 implements ModelDemo1{

    private static final Logger LOG = LoggerFactory.getLogger(ModelDemoImpl1.class);

    @SlingObject
    private SlingHttpServletRequest req;

    @SlingObject
    private ResourceResolver resolver;
    
    @ValueMapValue
    private String name;

    @ValueMapValue
    private int score;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public String getTitle() {
        return resolver.adaptTo(PageManager.class).getContainingPage(resolver.getResource("/content/wknd/us/en/about-us")).getTitle();
    }

    @RequestAttribute(name = "rAttr")
    private String rAttr;

    @Override
    public String getReqAttribute() {
        return rAttr;
    }

    @PostConstruct
    protected void constructed(){
        LOG.info("ModelDemo1 model is constructed");
    }

    @Override
    public List<String> getBooks() {
        List<String> ls = new ArrayList<>();
        Collections.addAll(ls,"hi", "hello", "hey");
        return ls;
    }

    @Override
    public Map<String, Integer> getMarks() {
        Map<String,Integer> myMap = new HashMap<>();
        myMap.put("math", 90);
        myMap.put("java", 91);
        myMap.put("hindi", 67);
        return myMap;
    }

    @Override
    public List<Map<String,String>> getBookNames(){
        List<Map<String,String>> bookNames = new ArrayList<>();
        Map<String,String> bookName = new HashMap<>();
        bookName.put("bookname","b1");
        bookName.put("subjectname","s1");
        bookNames.add(bookName);
        bookName = new HashMap<>();
        bookName.put("bookname","b2");
        bookName.put("subjectname","s2");
        bookNames.add(bookName);
        return bookNames;
    }

}
