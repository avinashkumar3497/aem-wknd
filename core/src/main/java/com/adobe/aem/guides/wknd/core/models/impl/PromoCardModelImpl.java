package com.adobe.aem.guides.wknd.core.models.impl;

import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.adobe.aem.guides.wknd.core.models.PromoCardModel;
import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;

@Model(
    adaptables = {Resource.class, SlingHttpServletRequest.class},
    adapters = {PromoCardModel.class, ComponentExporter.class},
    resourceType = PromoCardModelImpl.RESOURCE_TYPE,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
          extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class PromoCardModelImpl implements PromoCardModel {

    protected static final String RESOURCE_TYPE = "wknd/components/promocard";
    private static final String DEFAULT_CTA_LABEL = "Read More";

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String description;

    @ValueMapValue
    private String linkPath;

    @ValueMapValue
    private String ctaLabel;

    @ValueMapValue
    private boolean hideCard;

    @SlingObject
    private ResourceResolver resourceResolver;

    private String link;
    private String resolvedCtaLabel;
    private boolean empty;

    @PostConstruct
    protected void init() {
        this.link = buildLink();
        this.resolvedCtaLabel = StringUtils.isNotBlank(ctaLabel)
                ? ctaLabel
                : (link != null ? DEFAULT_CTA_LABEL : null);
        this.empty = hideCard
                || (StringUtils.isBlank(title) && StringUtils.isBlank(description));
    }

    private String buildLink() {
        if (StringUtils.isBlank(linkPath)) {
            return null;
        }
        if (StringUtils.startsWith(linkPath, "http")) {
            return linkPath;
        }
        if (StringUtils.startsWith(linkPath, "/content")) {
            return resourceResolver.map(linkPath) + ".html";
        }
        return null;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getLink() {
        return link;
    }

    @Override
    public String getCtaLabel() {
        return resolvedCtaLabel;
    }

    @Override
    public boolean isEmpty() {
        return empty;
    }

    @Override
    public String getExportedType() {
        return RESOURCE_TYPE;
    }
}