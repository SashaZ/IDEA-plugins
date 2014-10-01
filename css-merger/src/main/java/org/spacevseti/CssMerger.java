package org.spacevseti;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by space on 01.10.14.
 * Version 1
 */
public class CssMerger {
    private static final Charset CHARSET = Charset.defaultCharset();

    private final File mergingFile;
    private final File rootCssDir;
    private final String mergingFileName;


    public CssMerger(File mergingFile) throws IOException {
        this.mergingFile = mergingFile;
        this.rootCssDir = mergingFile.getParentFile();
        this.mergingFileName = FilenameUtils.getName(mergingFile.getCanonicalPath());
    }

    /**
     *
     * @return warnings
     * @throws IOException
     */
    public List<String> merge() throws IOException {
        if (!mergingFile.exists()) {
            String errorMsg = "File " + mergingFile.getName() + " doesn't exist (" + mergingFile.getCanonicalPath() + ")";
            throw new IOException(errorMsg);
        }

        File backupFile = new File(rootCssDir, "_"+ mergingFileName);
        FileUtils.copyFile(mergingFile, backupFile);

        File resultFile = new File(FileUtils.getTempDirectory(), "result.css");

        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        List<String> warnings = new ArrayList<String>();
        try {
            inputStream = FileUtils.openInputStream(mergingFile);
            List<String> lines = IOUtils.readLines(inputStream, CHARSET);

            FileUtils.deleteQuietly(resultFile);
            outputStream = FileUtils.openOutputStream(resultFile, true);
            for (String line : lines) {
                List<String> analyzeResult = analyze(line, warnings);
                IOUtils.writeLines(analyzeResult, IOUtils.LINE_SEPARATOR, outputStream, CHARSET);
            }
        } catch (IOException e) {
            String errorMsg = "Can't merge file '" + mergingFile.getName() + "' (" + mergingFile.getCanonicalPath() + ")";
            throw new IOException(errorMsg, e);
        } finally {
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(inputStream);
        }

        FileUtils.deleteQuietly(mergingFile);
        FileUtils.moveFile(resultFile, mergingFile);

        return warnings;
    }

    private List<String> analyze(String line, List<String> warnings) {
        if (StringUtils.startsWith(line, "@import url") && !StringUtils.startsWith(line, "@import url(http")) {
            String importFileName = StringUtils.substringBetween(line, "(", ")");
            if (StringUtils.contains(line, "do not copy")) {
                warnings.add("Importing File '" + importFileName + "' was excluding)");
            } else {
                FileInputStream inputStream = null;
                File importFile = new File(rootCssDir, importFileName);
                try {
                    inputStream = FileUtils.openInputStream(importFile);
                    return IOUtils.readLines(inputStream, CHARSET);
                } catch (IOException e) {
                    String warningMsg = "Importing File " + importFile.getName() + " doesn't exist (" + importFileName + ").";
                    warnings.add(warningMsg);
                } finally {
                    IOUtils.closeQuietly(inputStream);
                }
            }
        }
        return Collections.singletonList(line);
    }
}
