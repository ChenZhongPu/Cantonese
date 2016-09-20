package com.sjtu.chenzhongpu.cantonese.sql;

import android.provider.BaseColumns;

/**
 * Created by chenzhongpu on 9/19/16.
 */
public final class WordMeanItem {

    public WordMeanItem() {}

    public static abstract class WordMeanEntry implements BaseColumns {
        public static final String TABLE_NAME = "wordmeans";
        public static final String COLUM_BIG5 = "big5";
        public static final String COLUMN_SOUND = "wordsound";
        public static final String COLUNN_MEANING = "wordmeaning";
    }
}
