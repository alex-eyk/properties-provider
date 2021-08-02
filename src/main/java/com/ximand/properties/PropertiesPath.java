package com.ximand.properties;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Этой аннотацией помечаются классы, содержащие свойства, хранящиеся в properties-файлах. При пометке класса аннотацией
 * обязательно необходимо указать путь к properties-файлу. Это может быть как полный путь, так и записанный относительно
 * исполняемого Jar-файла. В таком случае путь следует записать следующим образом:
 * <pre><code>
 *    `@PropertiesPath("jarpath:/config.properties")
 *     public class Config {
 *         ...
 *     }
 * </code></pre>
 * Часть `jarpath:/` во время работы программы будет заменена на путь к Jar-файлу.
 *
 * Важно: при запуске программы из терминала на некоторых операционных системах путем к properties-файлам относительно
 * текущего Jar-файла будет являться путь в терминале на момет запуска. Это решается переходом в каталог с jar-файлом
 * перед запуском программы
 * <pre>
 *     cd 'jar_file_location'
 *     sudo java -jar /Application.jar
 * </pre>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PropertiesPath {

    /**
     * Путь к properties-файлу: полный либо относительный ("jarpath:/...")
     */
    String value();
}
