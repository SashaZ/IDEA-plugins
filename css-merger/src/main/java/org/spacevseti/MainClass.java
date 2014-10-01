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
        ResultMerger resultMerger = new CssMerger(new File("css-merger/css", "all.css")).merge();
        System.out.println(resultMerger);
    }
}
