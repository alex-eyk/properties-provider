package com.ximand.properties;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Этой аннотацией помечаются поля, хранящие данные из properties-файла. При пометке поля аннотацией обязательно
 * необходимо указать название свойства из properties-файла. При необходимости можно указать значение поля по умолчанию:
 * при отсутсвии properties-файла или при отсутвсии нужной строки в файле будет использоваться именно это значение.
 * <pre><code>
 *    `@Property(name = "user", defaultValue="root")
 *     private String user;
 * </code></pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Property {

    String name();

    String defaultValue() default "0";

}
