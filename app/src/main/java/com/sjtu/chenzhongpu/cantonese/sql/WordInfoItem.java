package com.sjtu.chenzhongpu.cantonese.sql;

import android.provider.BaseColumns;

/**
 * Created by chenzhongpu on 9/19/16.
 */
public final class WordInfoItem {

    public WordInfoItem() {}

    public static abstract class WordInfoEntry implements BaseColumns {
        public static final String TABLE_NAME = "wordinfos";
        public static final String COLUMN_BIG5 = "big5";
        public static final String COLUMN_CANJIE = "canjie";
        public static final String COLUMN_english = "english";
    }
}
