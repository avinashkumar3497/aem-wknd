package com.adobe.aem.guides.wknd.core.schedulers;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
        name = "WKND - Expired Page Scanner",
        description = "Scans for pages whose off-time has passed but which are still active on publish.")
public @interface ExpiredPageScannerConfig {

    @AttributeDefinition(name = "Cron Expression",
            description = "Quartz cron expression. Default: daily at 02:00.",
            type = AttributeType.STRING)
    String cronExpression() default "0 0 2 * * ?";

    @AttributeDefinition(name = "Content Root Path",
            type = AttributeType.STRING)
    String contentRootPath() default "/content/mysite";

    @AttributeDefinition(name = "Enabled",
            type = AttributeType.BOOLEAN)
    boolean enabled() default true;

    @AttributeDefinition(name = "Max Results To Log",
            type = AttributeType.INTEGER)
    int maxResults() default 100;
}