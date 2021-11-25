# properties-provider
Library for easily loading properties files and working with files in a directory with a .jar file

## Loading .properties files
### 1. Create class
First, you need to create a class with the same fields that will be in the .properties file. Example:
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
In this case, the .properties file is located in the same folder as the .jar file and it's name – config.properties. If the file located in the folder with application resources, the path should be written as follows: res:/config.properties. The file from example contains one field with the server.threads key, which has an integer value.
If such a file does not exist, it will be created automatically using default values.

### 2. Create instance of class
To create an instance of previously created class, you should use method createInstance of the PropertiesProvider object, passing it as an argument the class whose instance you want to create.
```java
    final PropertiesProvider propertiesProvider = new PropertiesProvider();
    final Config config = propertiesProvider.createInstance(Config.class);
```

## Getting files from a directory with a .jar file
To get files from a directory with a .jar file, use getFileFromJarDirectoryPath(String filename, Class<?> clazz) static function of the JarUtils class.
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

## Jar file path
You can use getJarLocation(Class<?> clazz) static function of the JarUtils class to get the path directly to the .jar file. If the class is in the file system (file with the .class extension), then the path to the base directory will be returned, otherwise (if the class is in the Jar file) the path to the Jar file will be returned.
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

## Other possibilities
JarUtils class also contains File urlToFile(URL url) and File urlToFile(String url) functions. These functions are equivalent to the following code:
```java
    final File file = new File(url)
```
However, the functions of the JarUtils class additionally handle the situation when the url begins with "jar: file:" by returning the path to the .jar file.

## Install
1. Download [latest release](https://github.com/Ximand931/properties-provider/releases/tag/v1.0.2)
2. Move downloaded file to libs folder of your project. If such folder does not exist, create it.
3. Add dependency to your build.gradle file
```groovy
    dependencies {
        implementation files('libs/PropertiesProvider-1.0.2.jar')
    
    }
```
or add all dependency from libs folder
```groovy
    dependencies {
        implementation fileTree(dir: 'libs', include: ['*.jar'])
        
    }
```

