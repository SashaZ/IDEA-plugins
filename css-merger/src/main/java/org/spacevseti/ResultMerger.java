package org.spacevseti;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by space on 01.10.14.
 * Version 1
 */
public class ResultMerger {
    private final List<String> mergedFiles;
    private final List<String> excludedFiles;
    private final List<String> notFoundFiles;
    private final List<String> warnings;

    public ResultMerger() {
        this.mergedFiles = new ArrayList<String>();
        this.excludedFiles = new ArrayList<String>();
        this.notFoundFiles = new ArrayList<String>();
        this.warnings = new ArrayList<String>();
    }

    public List<String> getMergedFiles() {
        return mergedFiles;
    }

    public List<String> getExcludedFiles() {
        return excludedFiles;
    }

    public List<String> getNotFoundFiles() {
        return notFoundFiles;
    }

    public List<String> getWarnings() {
        return warnings;
    }
}
