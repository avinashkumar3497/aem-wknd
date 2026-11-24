package com.adobe.aem.guides.wknd.core.models;

/**
 * LinkItem
 */
public class LinkItem {
    private String label;
    private String path;
    private boolean newTab;
    public void setLabel(String label) {
        this.label = label;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public void setNewTab(boolean newTab) {
        this.newTab = newTab;
    }
    public String getLabel() {
        return label;
    }
    public String getPath() {
        return path;
    }
    public boolean isNewTab() {
        return newTab;
    }
}
