package org.spacevseti.cssmerger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;
import org.spacevseti.Utils;
import org.spacevseti.filemerger.FileMerger;
import org.spacevseti.filemerger.MergingResult;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.regex.Pattern;

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
        LineIterator it = null;
        OutputStream outputStream = null;
        try {
            it = FileUtils.lineIterator(resultFile, StringConstants.CHARSET.getValue());

            File tempFile = new File(FileUtils.getTempDirectory(), System.currentTimeMillis() + "-postmerging.temp");
            outputStream = FileUtils.openOutputStream(tempFile, true);

            Pattern pattern = Pattern.compile(StringConstants.CSS_IMPORT_REPLACE_PATTERN2.getValue());
            Collection<String> mergedFiles = new HashSet<String>(mergingResult.getMergedFiles());
            while (it.hasNext()) {
                String line = it.nextLine();

                String importFileName = Utils.getImportFileName(line, pattern, 1);
                if (StringUtils.isNotBlank(importFileName) && mergedFiles.remove(importFileName)) {
                    continue;
                }
                IOUtils.writeLines(Collections.singletonList(line), IOUtils.LINE_SEPARATOR, outputStream,StringConstants.CHARSET.getValue());
            }

            FileUtils.deleteQuietly(resultFile);
            FileUtils.moveFile(tempFile, resultFile);
        } catch (IOException e) {
            String errorMsg = "Can't read file '" + resultFile + "'!";
            throw new IOException(errorMsg, e);
        } finally {
            LineIterator.closeQuietly(it);
            IOUtils.closeQuietly(outputStream);
        }
    }
}
