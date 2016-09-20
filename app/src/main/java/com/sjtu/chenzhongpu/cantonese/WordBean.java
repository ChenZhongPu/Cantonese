package com.sjtu.chenzhongpu.cantonese;

import java.util.List;

/**
 * Created by chenzhongpu on 9/18/16.
 */
public class WordBean {

    private String big5;
    private String word;
    private String canjie;
    private String english;
    private List<WordMean> wordMeenList;

    private boolean isStar;

    public String getBig5() {
        return big5;
    }

    public void setBig5(String big5) {
        this.big5 = big5;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getCanjie() {
        return canjie;
    }

    public void setCanjie(String canjie) {
        this.canjie = canjie;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public List<WordMean> getWordMeenList() {
        return wordMeenList;
    }

    public void setWordMeenList(List<WordMean> wordMeenList) {
        this.wordMeenList = wordMeenList;
    }

    public boolean isStar() {
        return isStar;
    }

    public void setStar(boolean star) {
        isStar = star;
    }
}



