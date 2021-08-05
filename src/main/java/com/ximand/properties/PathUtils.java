package com.ximand.properties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

public final class PathUtils {

    private PathUtils() {
    }

    public static String getJarDirectoryFilePath(String shortenedPath, Class<?> clazz) {
        return JarUtils.getFileFromJarDirectoryPath(getRelativePath(shortenedPath), clazz);
    }

    /**
     * @param shortenedPath Сокращенный путь, содержащий в начале jarpath:/
     * @return Полный путь к файлу, находящемуся в той же директории, что и Jar-файл
     */
    public static InputStream getJarDirectoryFileStream(String shortenedPath, Class<?> clazz) throws FileNotFoundException {
        return new FileInputStream(getJarDirectoryFilePath(shortenedPath, clazz));
    }

    /**
     * @param shortenedPath Сокращенный путь (содержащий в начале jarpath:/, libres:/ и т.д.)
     * @param classLoader   Загрузчик класса, необходимый для получения ресурса
     * @return Полный путь к файлу, находящемуся в директории ресурсов
     * @throws IllegalArgumentException Если файл не найден или он не содержит сокращения (jarpath:/, libres:/ и т.д .),
     *                                  значит, что был передан неверный параметр, поэтому будет брошено исключение.
     */
    @Deprecated
    public static String getResourcePath(String shortenedPath, ClassLoader classLoader) {
        return getResources(getRelativePath(shortenedPath), classLoader);
    }

    public static InputStream getResourceStream(String shortenedPath, ClassLoader classLoader) {
        return classLoader.getResourceAsStream(getRelativePath(shortenedPath));
    }

    /**
     * @param shortenedPath Сокращенный путь, содержащий в начале jarpath:/
     * @return Полный путь к файлу, находящемуся в директории ресурсов библиотеки
     * @throws IllegalArgumentException Если файл не найден или он не содержит сокращения (jarpath:/, libres:/ и т.д .),
     *                                  значит, что был передан неверный параметр, поэтому будет брошено исключение.
     */
    @Deprecated
    public static String getLibraryResourcePath(String shortenedPath) {
        return getResources(getRelativePath(shortenedPath), PathUtils.class.getClassLoader());
    }

    public static InputStream getLibraryResourceStream(String shortenedPath) {
        return PathUtils.class.getClassLoader().getResourceAsStream(getRelativePath(shortenedPath));
    }

    private static String getResources(String res, ClassLoader classLoader) {
        final URL resource = classLoader.getResource(res);
        if (resource != null) {
            return resource.getPath();
        } else {
            throw new IllegalArgumentException("File {" + res + "} not found in resources folder");
        }
    }

    /**
     * @param shortenedPath Сокращенный путь (содержащий в начале jarpath:/, libres:/ и т.д.)
     * @return Относительный путь (Без jarpath:/, libres:/ и т.д. в начале)
     */
    public static String getRelativePath(String shortenedPath) {
        final int colonIndex = shortenedPath.indexOf(":");
        if (colonIndex != -1) {
            return shortenedPath.substring(shortenedPath.indexOf(":") + 2);
        } else {
            throw new IllegalArgumentException("No reduction found in shortened path: " + shortenedPath);
        }
    }

}
