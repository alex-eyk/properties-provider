package com.ximand.properties;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public final class JarUtils {

    private JarUtils() {
    }

    public static String getFileFromJarDirectoryPath(String filename, Class<?> clazz) {
        if (filename == null) {
            throw new IllegalArgumentException("Filename cannot be null");
        }
        final URL jarLocation = getJarLocation(clazz);
        final File file = urlToFile(jarLocation);
        if (file.getName().endsWith("jar")) {
            return file.getParent() + "/" + filename;
        } else {
            return file.getPath() + "/" + filename;
        }
    }

    /**
     * @param clazz Класс, расположение которого необходимо получить.
     * @return Если класс находится в файловой системе (файл с расширением .class), то будет возвращен путь к базовой
     * директории, иначе (если класс находиться в Jar-файле) будет возвращен путь до Jar-файла.
     */
    public static URL getJarLocation(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class cannot be null");
        }
        final URL codeSourceLocation = clazz.getProtectionDomain()
                .getCodeSource()
                .getLocation();
        if (codeSourceLocation != null) {
            return codeSourceLocation;
        }
        final URL classResource = clazz.getResource(clazz.getSimpleName() + ".class");
        if (classResource == null) {
            throw new IllegalStateException("Unable to get jar path: class resource has null reference");
        }
        final String pathWithoutSuffix = removeJarSuffix(clazz.getCanonicalName(), classResource.toString());
        final String path = removeJarPrefix(pathWithoutSuffix);
        try {
            return new URL(path);
        } catch (final MalformedURLException e) {
            throw new IllegalStateException("Unable to get jar path", e);
        }
    }

    private static String removeJarSuffix(String canonicalName, String path) {
        final String suffix = canonicalName
                .replace('.', '/') + ".class";
        if (path.endsWith(suffix) == false) {
            throw new IllegalStateException("Unable to get jar path: illegal path suffix, " +
                    "path: " + path + ", suffix: " + suffix);
        }
        return path.substring(0, path.length() - suffix.length());
    }

    private static String removeJarPrefix(String path) {
        if (path.startsWith("jar:")) {
            return path.substring(4, path.length() - 2);
        } else {
            return path;
        }
    }

    public static File urlToFile(URL url) {
        if (url == null) {
            throw new IllegalArgumentException();
        }
        return urlToFile(url.toString());
    }

    public static File urlToFile(final String url) {
        String path = url;
        if (path.startsWith("jar:")) {
            final int suffixIndex = path.indexOf("!/");
            if (suffixIndex == -1) {
                throw new IllegalStateException("Suffix not found");
            }
            path = path.substring(4, suffixIndex);
        }
        try {
            if (isWindows() && path.matches("file:[A-Za-z]:.*")) {
                path = "file:/" + path.substring(5);
            }
            return new File(new URL(path).toURI());
        } catch (MalformedURLException | URISyntaxException e) {
            if (path.startsWith("file:")) {
                path = path.substring(5);
                return new File(path);
            }
        }
        throw new IllegalArgumentException("Invalid URL: " + url);
    }

    private static boolean isWindows() {
        return System.getProperty("os.name")
                .startsWith("Windows");
    }
}
