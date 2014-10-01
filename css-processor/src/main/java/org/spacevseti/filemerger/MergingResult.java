package org.spacevseti.filemerger;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by space on 01.10.14.
 * Version 1
 */
public class MergingResult {
    private final Collection<String> mergedFiles;
    private final Collection<String> excludedFiles;
    private final Collection<String> failedFiles;
    private final Collection<String> warnings;

    public MergingResult() {
        this.mergedFiles = new ArrayList<String>();
        this.excludedFiles = new ArrayList<String>();
        this.failedFiles = new ArrayList<String>();
        this.warnings = new ArrayList<String>();
    }

    public Collection<String> getMergedFiles() {
        return mergedFiles;
    }

    public Collection<String> getExcludedFiles() {
        return excludedFiles;
    }

    public Collection<String> getFailedFiles() {
        return failedFiles;
    }

    public Collection<String> getWarnings() {
        return warnings;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(formatResultMergerList("Merged files: ", getMergedFiles()));
        result.append(formatResultMergerList("Excluded files: ", getExcludedFiles()));
        result.append(formatResultMergerList("Failed files: ", getFailedFiles()));
        result.append(formatResultMergerList("Warnings: ", getWarnings()));
        if (result.length() == 0) {
            return StringUtils.EMPTY;
        }
        String prepend = "\nMerging result: ";
        result.insert(0, prepend);
        return result.toString();
    }

    private String formatResultMergerList(String ChapterName, Collection<String> fileNameList) {
        if (fileNameList.isEmpty()) {
            return StringUtils.EMPTY;
        }
        StringBuilder result = new StringBuilder();
        result.append("\n  ").append(ChapterName).append("\n");

        for (String fileName : fileNameList) {
            result.append("\t- ").append(fileName).append("\n");
        }
        return result.toString();
    }
}
