package com.ximand.properties;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PathUtilTest {

    @Test
    void getPathFromJarDirectoryTest() {
        String path = PathUtil.getPathFromJarDirectory("jarpath:/config.properties", PathUtil.class);
        checkSlashes(path);
    }

    @Test
    void getPathFromJarDirectoryIllegalArgumentTest() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> PathUtil.getPathFromJarDirectory("folder/config.properties", PathUtil.class)
        );
    }

    @Test
    void getResourcePathTest() {
        String path = PathUtil.getResourcePath("res:/config.properties",
                PathUtilTest.class.getClassLoader());
        checkSlashes(path);
    }

    @Test
    void getResourcePathIllegalArgumentTest() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> PathUtil.getResourcePath("config.properties", PathUtilTest.class.getClassLoader())
        );
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> PathUtil.getResourcePath("res:/config.props", PathUtilTest.class.getClassLoader())
        );
    }

    @Test
    void getLibraryResourcePathTest() {
        String path = PathUtil.getLibraryResourcePath("libres:/default_config.properties");
        checkSlashes(path);
    }

    @Test
    void getLibraryResourcePathIllegalArgumentTest() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> PathUtil.getLibraryResourcePath("config.properties")
        );
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> PathUtil.getLibraryResourcePath("libres:/config.props")
        );
    }

    @Test
    void getRelativePathTest() {
        String expectedPath = "folder/config.properties";
        String actualPath = PathUtil.getRelativePath("jarpath:/folder/config.properties");
        Assertions.assertEquals(expectedPath, actualPath);
    }

    @Test
    void getRelativePathIllegalArgumentTest() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> PathUtil.getRelativePath("jarpath/config.props")
        );
    }

    private void checkSlashes(String path) {
        if (path.matches(".*//+.*")) {
            Assertions.fail("Path should not contains '//': " + path);
        }
    }

}