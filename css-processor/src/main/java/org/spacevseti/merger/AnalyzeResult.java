package org.spacevseti.merger;

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
}
