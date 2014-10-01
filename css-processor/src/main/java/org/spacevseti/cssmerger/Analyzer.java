package org.spacevseti.cssmerger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;
import org.spacevseti.Utils;

import java.io.File;
import java.io.IOException;

/**
 * Created by space on 01.10.14.
 * Version 1
 */
public class Analyzer {

    public AnalyzeResult preMergeAnalyze(File mergingFile) throws IOException {
        LineIterator it = null;
        try {
            it = FileUtils.lineIterator(mergingFile, StringConstants.CHARSET.getValue());

            AnalyzeResult analyzeResult = new AnalyzeResult();
            while (it.hasNext()) {
                analyzeLine(it.nextLine(), mergingFile.getParentFile(), analyzeResult);
            }
            return analyzeResult;
        } catch (IOException e) {
            String errorMsg = "Can't read merging file '" + mergingFile + "' (" + mergingFile.getPath() + ")";
            throw new IOException(errorMsg, e);
        } finally {
            LineIterator.closeQuietly(it);
        }
    }

    private void analyzeLine(String line, File rootDir, AnalyzeResult analyzeResult) {
        String importFileName = Utils.getImportFileNameFromLine(line);
        if (StringUtils.isBlank(importFileName)) {
            return;
        }
        analyzeResult.getImportFileNames().add(importFileName);

        String excludeCause = isExcludeImportLine(line, importFileName, rootDir);
        if (StringUtils.isNotBlank(excludeCause)) {
            analyzeResult.getExcludeImportFileNamesWithCause().put(importFileName, excludeCause);
        }
    }

    private String isExcludeImportLine(String line, String importFileName, File rootDir) {
        if (StringUtils.startsWith(importFileName, "http")) {
            return StringConstants.EXCLUDE_CAUSE_REMOTE_LINK.getValue();
        }
        for (String excludeWord : StringUtils.split(StringConstants.CSS_EXCLUDE_IMPORT_WORDS.getValue(), ',')) {
            if (StringUtils.contains(line, excludeWord)) {
                return StringConstants.EXCLUDE_CAUSE_DON_NOT_COPY.getValue();
            }
        }
        if (!new File(rootDir, importFileName).exists()) {
            return StringConstants.EXCLUDE_CAUSE_NOT_FOUND.name();
        }
        return StringUtils.EMPTY;
    }
}
