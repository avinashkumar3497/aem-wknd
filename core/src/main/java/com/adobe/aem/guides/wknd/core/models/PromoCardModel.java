package com.adobe.aem.guides.wknd.core.models;

public interface PromoCardModel {
    String getTitle(); 
    String getDescription();
    String getLink();
    String getCtaLabel();
    boolean isEmpty();
    String getExportedType();
}
