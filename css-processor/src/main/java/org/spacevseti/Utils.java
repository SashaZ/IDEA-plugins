package org.spacevseti;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by space on 01.10.14.
 * Version 1
 */
public final class Utils {
    private Utils() {
    }

    public static final String getImportFileNameFromLine(String line) {
        return StringUtils.substringBetween(line, "(", ")");
    }
}
