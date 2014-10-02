package org.spacevseti.filemerger;

import org.apache.commons.lang3.StringUtils;
import org.spacevseti.Utils;

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
        result.append(Utils.formatList("Merged files", getMergedFiles()));
        result.append(Utils.formatList("Excluded files", getExcludedFiles()));
        result.append(Utils.formatList("Failed files", getFailedFiles()));
        result.append(Utils.formatList("Warnings", getWarnings()));
        if (result.length() == 0) {
            return StringUtils.EMPTY;
        }
        String prepend = "\nMerging result: ";
        result.insert(0, prepend);
        return result.toString();
    }
}
