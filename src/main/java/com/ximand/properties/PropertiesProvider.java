package com.ximand.properties;

public class PropertiesProvider {

    /**
     * Метод отвечает за создание новой сущности класса, хранящего информацию из properties-файла. Класс обязательно
     * должен быть помечен аннотацией @PropertiesPath.
     *
     * Если файл конфигурации, указанный в аннотации, во время работы программы не будет найден, то будет создан новый
     * файл, где все свойства будут принимать стандартные значения, которые можно указать как параметр `default value` у
     * аннотации Property:
     * <pre><code>
     *    `@PropertiesPath("jarpath:/config.properties")
     *     public class Config {
     *
     *        `@Property(name = "user", defaultValue="root")
     *         private String user;
     *
     *     }
     * </code></pre>
     *
     * @param tClass Класс, сущность которого должна быть создана
     * @return Объект, в котором все поля, помеченные аннотацией Property, инициализированы значениями из
     * properties-файла
     * @throws IllegalStateException В случае ошибок при описании классов или при работе с файлами будет брошено
     *                               исключение IllegalStateException c описанием возникшей проблемы.
     */
    public <T> T createInstance(Class<T> tClass) {
        FieldsInjector<T> injector =
                new FieldsInjector<>(tClass, getPropertiesPath(tClass));
        return injector.inject();
    }

    private <T> String getPropertiesPath(Class<T> tClass) {
        PropertiesPath propertiesPath = tClass
                .getDeclaredAnnotation(PropertiesPath.class);
        if (propertiesPath != null) {
            return propertiesPath.value();
        } else {
            throw new IllegalStateException("Class whose instance should be create " +
                    "not marked with annotation @PropertiesPath");
        }
    }

}
