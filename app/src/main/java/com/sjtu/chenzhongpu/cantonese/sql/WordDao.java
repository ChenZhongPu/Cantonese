package com.sjtu.chenzhongpu.cantonese.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.sjtu.chenzhongpu.cantonese.Utils;
import com.sjtu.chenzhongpu.cantonese.WordBean;
import com.sjtu.chenzhongpu.cantonese.WordMean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenzhongpu on 9/19/16.
 */
public class WordDao {

    public static void insertWord(SQLiteDatabase db, WordBean wordBean, Context context, boolean isCache) {

        ContentValues values = new ContentValues();
        values.put(WordItem.WordEntry.COLUMN_BIG5, wordBean.getBig5());
        values.put(WordItem.WordEntry.COLUMN_CHRACTER, wordBean.getWord());


        try {
            db.insert(WordItem.WordEntry.TABLE_NAME, "", values);

        } catch (Exception e) {
            Log.d("error", "error of insert");
        }

        if (isCache) {
            ContentValues values1 = new ContentValues();
            values1.put(WordInfoItem.WordInfoEntry.COLUMN_BIG5, wordBean.getBig5());
            values1.put(WordInfoItem.WordInfoEntry.COLUMN_CANJIE, wordBean.getCanjie());
            values1.put(WordInfoItem.WordInfoEntry.COLUMN_english, wordBean.getEnglish());
            try {
                db.insert(WordInfoItem.WordInfoEntry.TABLE_NAME, "", values1);
            } catch (Exception e) {
                Log.d("error", "error of insert");
            }

            List<WordMean> wordMeanList = wordBean.getWordMeenList();

            for (int i = 0; i < wordMeanList.size(); i++) {
                ContentValues values2 = new ContentValues();
                WordMean wordMean = wordMeanList.get(i);
                values2.put(WordMeanItem.WordMeanEntry.COLUM_BIG5, wordBean.getBig5());
                values2.put(WordMeanItem.WordMeanEntry.COLUMN_SOUND, wordMean.getPronunce());
                values2.put(WordMeanItem.WordMeanEntry.COLUNN_MEANING, wordMean.getMean());
                try {
                    db.insert(WordMeanItem.WordMeanEntry.TABLE_NAME, "", values2);
                } catch (Exception e) {
                    Log.d("error", "error of insert");
                }
                // store audio in local
                new FetchAudioTask(context).execute(wordMean.getPronunce());
            }

        }

    }

    public static void deleteWord(SQLiteDatabase db, WordBean wordBean, Context context) {
        // unstar a word
        String[] delteArg = {wordBean.getBig5()};
        try {
            db.delete(WordItem.WordEntry.TABLE_NAME, WordItem.WordEntry.COLUMN_BIG5 + " = ?", delteArg);
            List<WordMean> wordMeanList = wordBean.getWordMeenList();
            if (wordMeanList != null) {
                for (int i = 0; i < wordMeanList.size(); i++) {
                    WordMean wordMean = wordMeanList.get(i);
                    File file = new File (context.getFilesDir(), wordMean.getPronunce() + ".wav");
                    file.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("error", "error of delete");
        }
    }

    public static void deleteWordByGig5(SQLiteDatabase db, String big5, Context context) {
        try {
            // get all sound name
            try (Cursor cursor = db.query(WordMeanItem.WordMeanEntry.TABLE_NAME, new String[]{WordMeanItem.WordMeanEntry.COLUMN_SOUND},
                    "big5 = ?", new String[]{big5}, null, null, null)) {
                while (cursor.moveToNext()) {
                    String sound = cursor.getString(cursor.getColumnIndex(WordMeanItem.WordMeanEntry.COLUMN_SOUND));
                    File file = new File(context.getFilesDir(), sound + ".wav");
                    file.delete();
                }
            }
            db.delete(WordItem.WordEntry.TABLE_NAME, WordItem.WordEntry.COLUMN_BIG5 + " = ?", new String[]{big5});

        } catch (Exception e) {
            Log.d("error", "error of delete by big5");
        }
    }

    public static WordBean queryWord(SQLiteDatabase db, String big5) {
        String[] wordColumn = {WordItem.WordEntry.COLUMN_CHRACTER};
        WordBean wordBean = new WordBean();
        try (Cursor cursor = db.query(WordItem.WordEntry.TABLE_NAME, wordColumn, "big5 = ?", new String[]{big5}, null, null, null)) {
            if (!(cursor.moveToFirst()) || cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            wordBean.setBig5(big5);
            wordBean.setWord(cursor.getString(cursor.getColumnIndex(WordItem.WordEntry.COLUMN_CHRACTER)));
            cursor.close();
        }

        // wordinfo, wordmean can be null
        String[] wordInfoColumn = {WordInfoItem.WordInfoEntry.COLUMN_english, WordInfoItem.WordInfoEntry.COLUMN_CANJIE};


        try (Cursor cursor = db.query(WordInfoItem.WordInfoEntry.TABLE_NAME, wordInfoColumn, "big5 = ?", new String[]{big5}, null, null, null)) {
            while (cursor.moveToNext()) {
                wordBean.setEnglish(cursor.getString(cursor.getColumnIndex(WordInfoItem.WordInfoEntry.COLUMN_english)));
                wordBean.setCanjie(cursor.getString(cursor.getColumnIndex(WordInfoItem.WordInfoEntry.COLUMN_CANJIE)));
            }
        }

        String[] wordMeanColumn = {WordMeanItem.WordMeanEntry.COLUNN_MEANING, WordMeanItem.WordMeanEntry.COLUMN_SOUND};

        try (Cursor cursor = db.query(WordMeanItem.WordMeanEntry.TABLE_NAME, wordMeanColumn, "big5 = ?", new String[]{big5}, null, null, null)) {
            List<WordMean> wordMeanList = new ArrayList<>();
            while (cursor.moveToNext()) {
                WordMean wordMean = new WordMean();
                wordMean.setMean(cursor.getString(cursor.getColumnIndex(WordMeanItem.WordMeanEntry.COLUNN_MEANING)));
                wordMean.setPronunce(cursor.getString(cursor.getColumnIndex(WordMeanItem.WordMeanEntry.COLUMN_SOUND)));
                wordMeanList.add(wordMean);
            }
            wordBean.setWordMeenList(wordMeanList);
        }
        return wordBean;
    }

    public static List<WordBean> getAllStarBean(SQLiteDatabase db) {

        List<WordBean> wordBeanList = new ArrayList<>();

        String[] wordColumn = {WordItem.WordEntry.COLUMN_BIG5, WordItem.WordEntry.COLUMN_CHRACTER};
        try (Cursor cursor = db.query(WordItem.WordEntry.TABLE_NAME, wordColumn, null, null, null, null, null)){
            while (cursor.moveToNext()) {
                WordBean wordBean = new WordBean();
                wordBean.setBig5(cursor.getString(cursor.getColumnIndex(WordItem.WordEntry.COLUMN_BIG5)));
                wordBean.setWord(cursor.getString(cursor.getColumnIndex(WordItem.WordEntry.COLUMN_CHRACTER)));
                wordBeanList.add(wordBean);
            }
        }
        return wordBeanList;
    }

}

class FetchAudioTask extends AsyncTask<String, Void, Void> {

    private Context mContext;

    FetchAudioTask(Context context) {
        mContext = context;
    }

    protected Void doInBackground(String... urls){
        String mProunce = urls[0];
        try {
            URL audioURL = new URL(Utils.WEBURL + "sound/" + urls[0] + ".wav");
            InputStream inputStream = audioURL.openStream();
            FileOutputStream outputStream = mContext.openFileOutput(mProunce + ".wav", Context.MODE_PRIVATE);
            int c;
            while ((c = inputStream.read()) != -1) {
                outputStream.write(c);
            }
            outputStream.close();
            inputStream.close();

        } catch (Exception e) {
            Log.d("error", "error of download");
        }
        return null;
    }

}
