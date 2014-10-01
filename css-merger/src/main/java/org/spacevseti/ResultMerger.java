package org.spacevseti;

import org.apache.commons.lang3.StringUtils;

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

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(formatResultMergerList("Merged files: ", getMergedFiles()));
        result.append(formatResultMergerList("Excluded files: ", getExcludedFiles()));
        result.append(formatResultMergerList("Not founded files: ", getNotFoundFiles()));
        result.append(formatResultMergerList("Warnings: ", getWarnings()));
        if (result.length() == 0) {
            return StringUtils.EMPTY;
        }
        String prepend = "\nMerging result: ";
        result.insert(0, prepend);
        return result.toString();
    }

    private String formatResultMergerList(String ChapterName, List<String> fileNameList) {
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
