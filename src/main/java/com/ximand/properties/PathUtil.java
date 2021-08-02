package com.ximand.properties;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public final class PathUtil {

    private PathUtil() {
    }

    /**
     * @param shortenedPath Сокращенный путь, содержащий в начале jarpath:/
     * @return Полный путь к файлу, находящемуся в той же директории, что и Jar-файл
     */
    public static String getPathFromJarDirectory(String shortenedPath, Class<?> clazz) {
        try {
            final URI location = clazz.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI();
            return new File(location).getPath() + "/" + getRelativePath(shortenedPath);
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Unable to get Jar-file path", e);
        }
    }

    /**
     * @param shortenedPath Сокращенный путь (содержащий в начале jarpath:/, libres:/ и т.д.)
     * @param classLoader   Загрузчик класса, необходимый для получения ресурса
     * @return Полный путь к файлу, находящемуся в директории ресурсов
     * @throws IllegalArgumentException Если файл не найден или он не содержит сокращения (jarpath:/, libres:/ и т.д .),
     *                                  значит, что был передан неверный параметр, поэтому будет брошено исключение.
     */
    public static String getResourcePath(String shortenedPath, ClassLoader classLoader) {
        return getResources(getRelativePath(shortenedPath), classLoader);
    }

    /**
     * @param shortenedPath Сокращенный путь, содержащий в начале jarpath:/
     * @return Полный путь к файлу, находящемуся в директории ресурсов библиотеки
     * @throws IllegalArgumentException Если файл не найден или он не содержит сокращения (jarpath:/, libres:/ и т.д .),
     *                                  значит, что был передан неверный параметр, поэтому будет брошено исключение.
     */
    public static String getLibraryResourcePath(String shortenedPath) {
        return getResources(getRelativePath(shortenedPath), PathUtil.class.getClassLoader());
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
