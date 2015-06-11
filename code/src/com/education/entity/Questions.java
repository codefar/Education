package com.education.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by su on 15-6-10.
 */
public class Questions {
    private int count;
    private List<Question> tmDatas = new ArrayList<Question>();

    public List<Question> getTmDatas() {
        return tmDatas;
    }

    public void setTmDatas(List<Question> tmDatas) {
        this.tmDatas = tmDatas;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public static class Question {
        private String dm;
        private String content;

        public String getDm() {
            return dm;
        }

        public void setDm(String dm) {
            this.dm = dm;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
