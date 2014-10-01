package org.spacevseti;

import org.apache.commons.lang3.StringUtils;
import org.spacevseti.cssmerger.StringConstants;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by space on 01.10.14.
 * Version 1
 */
public final class Utils {

    private static final Pattern PATTERN_FOR_GET_IMPORT_FILENAME = Pattern.compile(StringConstants.CSS_IMPORT_REPLACE_PATTERN.getValue());

    private Utils() {
    }

    public static String getImportFileNameFromLine(String line) {
        return getImportFileName(line, PATTERN_FOR_GET_IMPORT_FILENAME, 1);
    }

    public static String getImportFileName(String line, Pattern patternForGetImportFilename, int matcherGroup) {
        Matcher matcher = patternForGetImportFilename.matcher(line);
        return matcher.matches() ? matcher.group(matcherGroup) : StringUtils.EMPTY;
    }

    public static String formatList(String ChapterName, Collection<String> list) {
        if (list.isEmpty()) {
            return StringUtils.EMPTY;
        }
        StringBuilder result = new StringBuilder();
        result.append("\n  ").append(ChapterName).append(" (").append(list.size()).append(")").append("\n");

        for (String fileName : list) {
            result.append("\t- ").append(fileName).append("\n");
        }
        return result.toString();
    }

    public static String formatMap(String ChapterName, Map<String, String> map) {
        if (map.isEmpty()) {
            return StringUtils.EMPTY;
        }
        StringBuilder result = new StringBuilder();
        result.append("\n  ").append(ChapterName).append(" (").append(map.size()).append(")").append("\n");

        for (Map.Entry<String, String> entry : map.entrySet()) {
            result.append("\t- ").append(entry.getKey()).append(" (").append(entry.getValue()).append(")").append("\n");
        }
        return result.toString();
    }
}
