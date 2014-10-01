package org.spacevseti.cssmerger;

import org.spacevseti.filemerger.FileMerger;
import org.spacevseti.filemerger.MergingResult;

import java.io.File;
import java.io.IOException;

/**
 * Created by space on 01.10.14.
 * Version 1
 */
public class CssMerger extends FileMerger {

    public CssMerger(File mergingFile) throws IOException {
        super(mergingFile, StringConstants.CSS_IMPORT_REPLACE_PATTERN.getValue(), 1);

        AnalyzeResult analyzeResult = new Analyzer().preMergeAnalyze(mergingFile);
        setRemoveImportedFiles(false);
        setExcludeImportFilePaths(analyzeResult.getExcludeImportFileNamesWithCause().keySet());
    }

    protected final void postProcessing(File resultFile, MergingResult mergingResult) throws IOException {

    }
}
