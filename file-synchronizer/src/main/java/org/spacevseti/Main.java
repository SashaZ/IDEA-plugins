package org.spacevseti;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by space on 08.10.14.
 * Version 1
 */
public class Main {
    public static void main(String[] args) {
        hello();
        testSoup();
    }

    private static void testSoup() {
        String html = "<html><head><title>First parse</title></head>"
                + "<body><p>Parsed HTML into a doc.</p></body></html>";
        Document doc = Jsoup.parse(html);
        System.out.println("doc = " + doc);
    }

    private static void hello() {
        System.out.println("Hello gradle world");
    }
}
