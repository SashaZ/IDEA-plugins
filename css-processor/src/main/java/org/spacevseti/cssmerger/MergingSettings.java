package org.spacevseti.cssmerger;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by space on 01.10.14.
 * Version 1
 */
public class MergingSettings {

    public static final MergingSettings DEFAULT_MERGING_SETTINGS = new MergingSettings();

    private final Collection<String> excludeImportFileNames;

    public MergingSettings() {
        this.excludeImportFileNames = new HashSet<String>();
    }

    public MergingSettings(AnalyzeResult analyzeResult) {
        this.excludeImportFileNames = new HashSet<String>();
        excludeImportFileNames.addAll(analyzeResult.getExcludeImportFileNamesWithCause().keySet());
    }

    public Collection<String> getExcludeImportFileNames() {
        return excludeImportFileNames;
    }
}
