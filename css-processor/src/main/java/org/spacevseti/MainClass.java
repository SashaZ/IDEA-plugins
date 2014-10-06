package org.spacevseti;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.spacevseti.cssmerger.CssMerger;

import java.io.File;
import java.io.IOException;

/**
 * Created by space on 30.09.14.
 * Version 1
 */
public class MainClass {

    public static void main(String[] args) throws IOException {
        File allCssFile = new File("css-processor/html1/css", "all.css");

        System.out.println(new CssMerger(allCssFile, true).merge());
//        testLineIterator(allCssFile);
    }

    private static void testLineIterator(File allCssFile) throws IOException {
        LineIterator it = FileUtils.lineIterator(allCssFile, "UTF-8");
        try {
            while (it.hasNext()) {
                String line = it.nextLine();
                if (line.startsWith("@")) it.remove();
                /// do something with line
            }
        } finally {
            LineIterator.closeQuietly(it);
        }
    }
}
