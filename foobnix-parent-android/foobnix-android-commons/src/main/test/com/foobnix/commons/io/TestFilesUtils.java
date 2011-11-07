package com.foobnix.commons.io;

import java.io.File;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;


public class TestFilesUtils extends TestCase {

    @Test
    public void test1() {
        List<File> recurcive = FileDirUtils.getRecurcive(new File("/tmp"), null);
        for (File file : recurcive) {
            if (file.isDirectory()) {
                System.out.println(String.format("[ %s ]", file.getName()));
            } else {
                System.out.println(String.format(file.getName()));
            }
        }
    }

    @Test
    public void testList(){
        List<File> list = FileDirUtils.getList(new File("/tmp"), new String[] { ".php" });
        assertNotNull(list);
        for (File file : list) {
            System.out.println(file.getName());
        }
        assertTrue(list.size() > 2);
    }

}
