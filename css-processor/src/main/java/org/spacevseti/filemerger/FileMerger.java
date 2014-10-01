package org.spacevseti.filemerger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.spacevseti.merger.StringConstants;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by space on 01.10.14.
 * Version 1
 */
public class FileMerger {
    private static final Charset CHARSET = Charset.forName(StringConstants.CHARSET.getValue());

    protected final File mergingFile;
    private final File rootDir;
    private final String mergingFileName;

    private final Pattern lineWithFilePathPattern;
    private final int filePathGroupPosition;

    private final Collection<String> excludeImportFilePaths;
    private boolean removeImportedFiles;

    public FileMerger(File mergingFile, String lineWithFilePathPattern, int filePathGroupPosition) throws IOException {
        this.mergingFile = mergingFile;
        this.rootDir = mergingFile.getParentFile();
        this.mergingFileName = FilenameUtils.getName(mergingFile.getCanonicalPath());

        this.lineWithFilePathPattern = Pattern.compile(lineWithFilePathPattern);
        this.filePathGroupPosition = filePathGroupPosition;
        this.excludeImportFilePaths = new HashSet<String>();
    }

    public FileMerger setExcludeImportFilePaths(Collection<String> excludeImportFilePaths) {
        excludeImportFilePaths.clear();
        excludeImportFilePaths.addAll(excludeImportFilePaths);
        return this;
    }

    public FileMerger setRemoveImportedFiles(boolean removeImportedFiles) {
        this.removeImportedFiles = removeImportedFiles;
        return this;
    }

    public final MergingResult merge() throws IOException {
        if (!mergingFile.exists()) {
            String errorMsg = "File " + mergingFile + " doesn't exist!";
            throw new IOException(errorMsg);
        }

        File backupFile = new File(rootDir, mergingFileName + ".orig" + System.currentTimeMillis());
        FileUtils.copyFile(mergingFile, backupFile);

        File resultFile = new File(FileUtils.getTempDirectory(), StringConstants.TEMP_MERGING_FILENAME.getValue());
        FileUtils.deleteQuietly(resultFile);

        MergingResult mergingResult = merge(mergingFile, resultFile);

        FileUtils.deleteQuietly(mergingFile);
        FileUtils.moveFile(resultFile, mergingFile);
        return mergingResult;
    }

    public final MergingResult merge(File inputFile, File outputFile) throws IOException {
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = FileUtils.openInputStream(inputFile);
            outputStream = FileUtils.openOutputStream(outputFile, true);
            return merge(inputStream, outputStream);
        } catch (IOException e) {
            String errorMsg = "Can't merge file '" + inputFile + "'";
            throw new IOException(errorMsg, e);
        } finally {
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(inputStream);
        }
    }

    private MergingResult merge(InputStream inputStream, OutputStream outputStream) throws IOException {
        List<String> readLines;
        try {
            readLines = IOUtils.readLines(inputStream, CHARSET);
        } catch (IOException e) {
            String errorMsg = "Can't read lines!";
            throw new IOException(errorMsg, e);
        }
        try {
            MergingResult mergingResult = new MergingResult();
            for (String line : readLines) {
                if (!importContentByLine(line, outputStream, mergingResult)) {
                    IOUtils.writeLines(Collections.singletonList(line), IOUtils.LINE_SEPARATOR, outputStream, CHARSET);
                }
            }
            return mergingResult;
        } catch (IOException e) {
            String errorMsg = "Can't write new lines!";
            throw new IOException(errorMsg, e);
        }
    }

    private boolean importContentByLine(String line, OutputStream outputStream, MergingResult mergingResult) throws IOException {
        Matcher matcher = lineWithFilePathPattern.matcher(line);
        if (!matcher.matches()) {
            return false;
        }

        String importFilePath = matcher.group(filePathGroupPosition);
        if (excludeImportFilePaths.contains(importFilePath)) {
            mergingResult.getExcludedFiles().add(importFilePath);
            return false;
        }

        try {
            importContentFromFile(outputStream, importFilePath);
            mergingResult.getMergedFiles().add(importFilePath);
            return true;
        } catch (IOException e) {
            mergingResult.getFailedFiles().add(importFilePath);
            return false;
        }
    }

    private void importContentFromFile(OutputStream outputStream, String importFilePath) throws IOException {
        File importFile = new File(rootDir, importFilePath);

        InputStream importInputStream = null;
        try {
            importInputStream = FileUtils.openInputStream(importFile);
            IOUtils.copy(importInputStream, outputStream);
        } catch (IOException e) {
            String errorMsg = "Can't import content from file '" + importFile + "'!";
            throw new IOException(errorMsg, e);
        } finally {
            IOUtils.closeQuietly(importInputStream);
        }

        if (removeImportedFiles){
            FileUtils.deleteQuietly(importFile);
        }
    }
}
