package org.spacevseti.cssmerger;

import org.apache.commons.lang3.StringUtils;
import org.spacevseti.Utils;

import java.util.*;

/**
 * Created by space on 01.10.14.
 * Version 1
 */
public class AnalyzeResult {
    private final Set<String> importFileNames;
    private final Map<String, String> excludeImportFileNamesWithCause;

    public AnalyzeResult() {
        importFileNames = new LinkedHashSet<String>();
        excludeImportFileNamesWithCause = new HashMap<String, String>();
    }

    public Set<String> getImportFileNames() {
        return importFileNames;
    }

    public Map<String, String> getExcludeImportFileNamesWithCause() {
        return excludeImportFileNamesWithCause;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(Utils.formatList("Found files for importing: ", getImportFileNames()));
        result.append(Utils.formatMap("Excluded files: ", getExcludeImportFileNamesWithCause()));
        if (result.length() == 0) {
            return StringUtils.EMPTY;
        }
        String prepend = "\nAnalyze result: ";
        result.insert(0, prepend);
        return result.toString();
    }
}
