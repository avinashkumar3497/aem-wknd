package com.adobe.aem.guides.wknd.core.models;

import java.util.Calendar;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.dam.cfm.ContentElement;
import com.adobe.cq.dam.cfm.ContentFragment;
import com.adobe.cq.dam.cfm.ContentVariation;
import com.adobe.cq.dam.cfm.FragmentData;

@Model(adaptables = SlingHttpServletRequest.class,
       defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ArticleCFReaderModel {

    private static final Logger LOG = LoggerFactory.getLogger(ArticleCFReaderModel.class);
    private static final String ARTICLE_MODEL = "/conf/wknd/settings/dam/cfm/models/article";

    @ValueMapValue
    private String fragmentPath;

    @ValueMapValue
    private String variationName;

    @SlingObject
    private ResourceResolver resolver;

    private String headline;
    private String summary;
    private String author;
    private Calendar publishDate;
    private String[] tags;
    private boolean empty = true;

    @PostConstruct
    protected void init() {
        if (StringUtils.isBlank(fragmentPath)) {
            return;
        }

        Resource cfResource = resolver.getResource(fragmentPath);
        if (cfResource == null) {
            LOG.warn("No resource found at {}", fragmentPath);
            return;
        }

        ContentFragment cf = cfResource.adaptTo(ContentFragment.class);
        if (cf == null) {
            LOG.warn("{} is not a Content Fragment", fragmentPath);
            return;
        }

        Resource dataNode = cfResource.getChild("jcr:content/data");
        String model = dataNode != null
                ? dataNode.getValueMap().get("cq:model", String.class)
                : null;
        if (!ARTICLE_MODEL.equals(model)) {
            LOG.warn("Fragment {} uses model {}, expected {}", fragmentPath, model, ARTICLE_MODEL);
            return;
        }

        headline    = read(cf, "headline", String.class);
        summary     = read(cf, "summary", String.class);
        author      = read(cf, "author", String.class);
        publishDate = read(cf, "publishDate", Calendar.class);
        tags        = read(cf, "tags", String[].class);

        empty = StringUtils.isBlank(headline);
    }

    private <T> T read(ContentFragment cf, String name, Class<T> type) {
        ContentElement element = cf.getElement(name);
        if (element == null) {
            return null;
        }

        FragmentData data = element.getValue();

        if (StringUtils.isNotBlank(variationName)) {
            ContentVariation variation = element.getVariation(variationName);
            if (variation != null) {
                data = variation.getValue();
            } else {
                LOG.debug("Variation '{}' not found on element '{}', using master", variationName, name);
            }
        }

        return data != null ? data.getValue(type) : null;
    }

    public String getHeadline() {
        return headline;
    }

    public String getSummary() {
        return summary;
    }

    public String getAuthor() {
        return author;
    }

    public Calendar getPublishDate() {
        return publishDate != null ? (Calendar) publishDate.clone() : null;
    }

    public String[] getTags() {
        return tags != null ? tags.clone() : new String[0];
    }

    public boolean isEmpty() {
        return empty;
    }
}