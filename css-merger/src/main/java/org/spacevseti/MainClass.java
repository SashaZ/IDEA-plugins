package org.spacevseti;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by space on 30.09.14.
 * Version 1
 */
public class MainClass {

    public static void main(String[] args) throws IOException {
        List<String> warnings = new CssMerger(new File("all.css")).merge();
        System.out.println("warnings = " + warnings);
    }
}
