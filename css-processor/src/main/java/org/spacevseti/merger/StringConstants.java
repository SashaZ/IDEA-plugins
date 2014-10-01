package org.spacevseti.merger;

import java.nio.charset.Charset;

/**
 * Created by space on 01.10.14.
 * Version 1
 */
public enum StringConstants {

    CHARSET(Charset.defaultCharset().name()),
    CSS_EXTENSION("css"),
    TEMP_MERGING_FILENAME("temp_merging.txt"),
    CSS_IMPORT_START_LINE("@import url"),
    CSS_IMPORT_START_LINE_COMMENT("/*@import url"),
    CSS_IMPORT_REPLACE_PATTERN("^/\\*@import url\\((.+?)\\).*\\*/$"),
    CSS_EXCLUDE_IMPORT_WORDS("don't copy, do not copy, not copy, no copy"),
    EXCLUDE_CAUSE_DON_NOT_COPY("do not copy"),
    EXCLUDE_CAUSE_REMOTE_LINK("remote link"),
    EXCLUDE_CAUSE_NOT_FOUND("not found"),
    ;

    private final String value;

    StringConstants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
