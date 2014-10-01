package org.spacevseti;

import org.apache.commons.lang3.StringUtils;
import org.spacevseti.cssmerger.StringConstants;

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
}
