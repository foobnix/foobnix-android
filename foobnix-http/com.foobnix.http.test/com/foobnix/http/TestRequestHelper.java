package com.foobnix.http;

import junit.framework.TestCase;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;

public class TestRequestHelper extends TestCase {
    static {
        PropertyConfigurator.configure(TestRequestHelper.class.getResource("log4j.properties"));
    }

    @Test
    public void testConeect() {
        RequestHelper helper = new RequestHelper();
        String postUrl = helper.getPostUrl("http://android.foobnix.com/vk");
        assertTrue(postUrl.startsWith("c892588@bofthew.com"));
    }

}
