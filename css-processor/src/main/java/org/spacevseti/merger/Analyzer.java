package org.spacevseti.merger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.spacevseti.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by space on 01.10.14.
 * Version 1
 */
public class Analyzer {

    private final AnalyzeResult analyzeResult;

    public Analyzer() {
        analyzeResult = new AnalyzeResult();
    }

    public AnalyzeResult preMergeAnalyze(File mergingFile) throws IOException {
        FileInputStream inputStream = null;
        try {
            inputStream = FileUtils.openInputStream(mergingFile);
            for (String line : IOUtils.readLines(inputStream, StringConstants.CHARSET.getValue())) {
                if (StringUtils.startsWith(line, StringConstants.CSS_IMPORT_START_LINE.getValue())) {
                    String importFileName = Utils.getImportFileNameFromLine(line);
                    analyzeResult.getImportFileNames().add(importFileName);

                    String excludeCause = isExcludeImportLine(mergingFile, line, importFileName);
                    if (StringUtils.isNoneBlank(excludeCause)) {
                        analyzeResult.getExcludeImportFileNamesWithCause().put(importFileName, excludeCause);
                    }
                }
            }
        } catch (IOException e) {
            String errorMsg = "Can't merge file '" + mergingFile + "' (" + mergingFile.getCanonicalPath() + ")";
            throw new IOException(errorMsg, e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return analyzeResult;
    }

    private String isExcludeImportLine(File mergingFile, String line, String importFileName) {
        if (StringUtils.startsWith(importFileName, "http")) {
            return StringConstants.EXCLUDE_CAUSE_REMOTE_LINK.getValue();
        }
        for (String excludeWord : StringUtils.split(StringConstants.CSS_EXCLUDE_IMPORT_WORDS.getValue(), ',')) {
            if(StringUtils.contains(line, excludeWord)){
                return StringConstants.EXCLUDE_CAUSE_DON_NOT_COPY.getValue();
            }
        }
        if (new File(mergingFile.getParentFile(), importFileName).exists()) {
            return StringConstants.EXCLUDE_CAUSE_NOT_FOUND.name();
        }
        return StringUtils.EMPTY;
    }
}
