package foobnix.samba;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import jcifs.smb.SmbFile;
import junit.framework.TestCase;

import org.junit.Test;

import com.google.common.io.Files;
import com.google.common.io.InputSupplier;



public class TestSambaConnection extends TestCase {


    @Test
    public void test2() throws Exception {
        SmbFile file = new SmbFile("smb://10.1.14.6/shara/Avantasia - Another Angel Down.mp3");
        System.out.println(file.getName());
        
        File bufferedFile = File.createTempFile("demo", Long.toString(System.nanoTime()));
        Files.copy(new DemoSupplier(), bufferedFile);
        assertEquals(file.length(), bufferedFile.length());
        System.out.println(file.length());
    }
    
    class DemoSupplier implements InputSupplier<InputStream> {
        @Override
        public InputStream getInput() throws IOException {
            SmbFile file = new SmbFile("smb://10.1.14.6/shara/Avantasia - Another Angel Down.mp3");
            return file.getInputStream();
        }

    };
    
    


}

