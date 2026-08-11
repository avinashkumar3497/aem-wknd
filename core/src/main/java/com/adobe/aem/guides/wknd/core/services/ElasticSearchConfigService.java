package com.adobe.aem.guides.wknd.core.services;

/**
 * Configuration Service for Elastic enterprise search
 */

public interface ElasticSearchConfigService {
    
	/** ES Crawl Request API Endpoint */
    String getApiEndPoint();

    /** Private key for Authorization */
    String getPrivateKey();

    /** Connect Timeout */
    int getConnectTimeout();

    /** Connection Request Timeout */
    int getConnectionRequestTimeout();

    /** Socket Timeout */
    int getSocketTimeout();

    /** Allowed Templates */
    String[] getAllowedTemplates();

}
