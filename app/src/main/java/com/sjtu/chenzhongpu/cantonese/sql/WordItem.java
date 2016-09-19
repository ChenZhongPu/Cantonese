package com.sjtu.chenzhongpu.cantonese.sql;

import android.provider.BaseColumns;

/**
 * Created by chenzhongpu on 9/19/16.
 */
public final class WordItem {

    public WordItem() {}

    public static abstract class WordEntry implements BaseColumns {
        public static final String TABLE_NAME = "words";
        public static final String COLUMN_BIG5 = "big5";
        public static final String COLUMN_CHRACTER = "character_content";
    }
}
