package org.spacevseti;

import org.spacevseti.merger.CssMerger;

import java.io.File;
import java.io.IOException;

/**
 * Created by space on 30.09.14.
 * Version 1
 */
public class MainClass {

    public static void main(String[] args) throws IOException {
        System.out.println(new CssMerger(new File("css-processor/html1/css", "all.css")).mergeTemp());
    }
}
