package com.sjtu.chenzhongpu.cantonese;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by chenzhongpu on 9/18/16.
 */
public class Utils {

    public static final String WEBURL = "http://humanum.arts.cuhk.edu.hk/Lexis/lexi-can/";

    public static final String BIG5_MESSAGE = "big5_message";

    public static final String WORD_MESSAGE = "word_message";

    public static String getBig5FromString(String query) throws UnsupportedEncodingException{
        byte[] bytes = query.getBytes("Big5");
        if (bytes.length != 2) {
            throw new UnsupportedEncodingException();
        } else {
            char[] hexArray = "0123456789ABCDEF".toCharArray();
            char[] hexChars = new char[bytes.length * 2];
            for ( int j = 0; j < bytes.length; j++ ) {
                int v = bytes[j] & 0xFF;
                hexChars[j * 2] = hexArray[v >>> 4];
                hexChars[j * 2 + 1] = hexArray[v & 0x0F];
            }
            return new String(hexChars);
        }
    }

    public static WordBean getWordBeanFromWeb(String big5, String word_str){
        String url = WEBURL + "search.php?q=%" + big5.substring(0, 2) + "%" + big5.substring(2, 4);
        WordBean wordBean = new WordBean();
        wordBean.setBig5(big5);
        wordBean.setWord(word_str);
        try {
            Document doc  = Jsoup.connect(url).get();
            wordBean.setCanjie(doc.select("tr").get(1).select("td").get(3).text());
            wordBean.setEnglish(doc.select("table").get(3).select("tr").get(1).select("td").get(3).text());

            List<WordMean> wordMeanList = new ArrayList<>();

            Elements elements = doc.select("table").get(1).select("tr");
            for (int i = 1; i < elements.size(); i++) {
                WordMean wordMean = new WordMean();
                wordMean.setPronunce(elements.get(i).select("td").get(0).text());
                String means = elements.get(i).select("td").get(5).text();
                int _index = means.indexOf("[");
                if (_index != -1)
                    wordMean.setMean(means.substring(0, _index));
                else
                    wordMean.setMean(means);
                wordMeanList.add(wordMean);
            }
            wordBean.setWordMeenList(wordMeanList);
        } catch (IOException e) {
            Log.d("error", "error of fetch web");
            return null;
        }
        return wordBean;
    }


}

