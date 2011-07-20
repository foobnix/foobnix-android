/**
 * 
 */
package com.foobnix.util;

import java.io.InputStream;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

/**
 * @author iivanenko
 * 
 */
public class XmlPersister {

    public static <T> T toModel(String xml, Class<T> clazz) {
        Serializer serializer = new Persister();

        T lfm = null;
        try {
            lfm = serializer.read(clazz, xml);
        } catch (Exception e) {
            new RuntimeException(e);
        }

        return lfm;
    }

    public static <T> T toModel(InputStream stream, Class<T> clazz) {
        Serializer serializer = new Persister();

        T lfm = null;
        try {
            lfm = serializer.read(clazz, stream);
        } catch (Exception e) {
            new RuntimeException(e);
        }

        return lfm;
    }

}
