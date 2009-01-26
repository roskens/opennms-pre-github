package org.opennms.netmgt.invd;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface JUnitScanner {
    String schemaConfig() default "/org/opennms/netmgt/config/test-database-schema.xml";
    String scannerConfig();
    String scannerType();
}
