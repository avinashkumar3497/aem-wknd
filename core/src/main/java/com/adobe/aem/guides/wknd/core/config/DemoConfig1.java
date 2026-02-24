package com.adobe.aem.guides.wknd.core.config;

import org.osgi.annotation.bundle.Attribute;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.osgi.service.metatype.annotations.Option;

@ObjectClassDefinition(
    name = "DemoConfig1",
    description = "Demo Config 1"
)
public @interface DemoConfig1 {

    @AttributeDefinition(name = "show", description="shows the value to demo this config", type = AttributeType.STRING)
    public String show() default "Default String";

    @AttributeDefinition(name = "number", description="number will be used from this config", type = AttributeType.INTEGER)
    public int number() default 6;

    //value of type BOOLEAN
    @AttributeDefinition(name = "trueORfalse", description="Is it true or false?", type = AttributeType.BOOLEAN)
    public boolean boool() default true;

    //array
    @AttributeDefinition(name = "countries", description="Countries", type = AttributeType.STRING)
    public String[] coutries() default {"in","us"};

    @AttributeDefinition(name = "falvour", description="select the flavour", type = AttributeType.STRING,
        options = {
            @Option(label = "Chocolate", value = "chocolate"),
            @Option(label = "Vanilla", value = "vanilla"),
            @Option(label = "Butterscotch", value = "butterScotch")
        }
    )
    public String flavour() default "chocolate";
}
