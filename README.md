# properties-provider
Библиотека для простой загрузки properties-файлов и работы с файлами в директории с .jar файлом

## Загрузка .properties файлов
### 1. Создание объекта
Для начала необходимо создать класс с теми же полями, которые будут находиться в .properties файле. Пример:
```java
    @PropertiesPath("jarpath:/config.properties")
    public class Config {
        
        @Property(name = "server.threads", defaultValue = "4")
        private int threads;
        
        public int getThreads() {
            return threads;
        }
        
    }
```
В данном случае .properties файл находиться в той же папке, что и .jar файл и называется config.properties. В случае, если файл должен находиться в папке с ресурсами приложения, путь к нему должен быть записан следующим образом: res:/config.properties. Файл содержит одно поле с ключом server.threads, имеющим числовое значение.
В случае, если такого файла не существует, он будет создан автоматически при помощи стандартных значений.

### 2. Создание объекта
Для того, чтобы создать объекто необходимого класса, необходимо воспользоваться методом createInstance объекта PropertiesProvider, передав ему в качестве аргумента класс, сущность которого необходимо создать.
```java
    final PropertiesProvider propertiesProvider = new PropertiesProvider();
    final Config config = propertiesProvider.createInstance(Config.class);
```

## Получение файлов из директории с .jar файлом
Для получения файлов из директории с .jar файлом используется статическая функции getFileFromJarDirectoryPath(String filename, Class<?> clazz) класса JarUtils.
```java
    public class SomeClass {
    
        private void someFunction() {
            final File configFile = JarUtils.getFileFromJarDirectoryPath("config.properties", getClass());
            
        }
    
        private static void someStaticFunction() {
            final File configFile = JarUtils.getFileFromJarDirectoryPath("config.properties", SomeClass.class);
            
        }
    }
```

## Путь к .jar файлу
Чтобы получить путь непосредственно к .jar файлу, можно воспользоваться функцией getJarLocation(Class<?> clazz) класса JarUtils. Если класс находится в файловой системе (файл с расширением .class), то будет возвращен путь к базовой директории, иначе (если класс находиться в Jar-файле) будет возвращен путь до Jar-файла.
```java
    public class SomeClass {
    
        private void someFunction() {
            final URL jarLocation = JarUtils.getJarLocation(getClass());
            
        }
    
        private static void someStaticFunction() {
            final URL jarLocation = JarUtils.getJarLocation(SomeClass.class);
            
        }
    }
```

## Другие возможности
Также класс JarUtils содержит функции File urlToFile(URL url) и File urlToFile(String url). Данные функции равносильны следующему коду:
```java
    final File file = new File(url)
```
Однако функции класса JarUtils дополнительно обрабатывают ситуацию, когда url начинается с "jar:file:", возвращая путь к .jar файлу
