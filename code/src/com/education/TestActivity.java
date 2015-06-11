package com.education;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.education.common.AppHelper;
import com.education.common.FastJsonRequest;
import com.education.common.VolleyErrorListener;
import com.education.common.VolleyResponseListener;
import com.education.entity.ErrorData;
import com.education.entity.Questions;
import com.education.entity.ShaiXuanJieGuo;
import com.education.entity.User;
import com.education.entity.UserInfo;
import com.education.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by su on 15-6-10.
 */
public class TestActivity extends CommonBaseActivity {

    private static final String TAG = "TestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserInfo userInfo = makeUserInfo();
//        updateKsxx(userInfo);
//        getQuestions();
//        shaiXuan();
        shouCangZhuanYe();
    }

    private void shouCangZhuanYe() {
        final FastJsonRequest request = new FastJsonRequest(Request.Method.POST, Url.SHOU_CANG_ZHUAN_YE
                , null, new VolleyResponseListener(this) {
            @Override
            public void onSuccessfulResponse(JSONObject response, boolean success) {
                if (success) {
                    JSONObject result = response.getJSONObject("result");
                    int status = result.getInteger("status");
                    if (status == 1) {

                    }
                    Toast.makeText(TestActivity.this, result.getString("msgText"), Toast.LENGTH_SHORT).show();
                } else {
                    ErrorData errorData = AppHelper.getErrorData(response);
                    Toast.makeText(TestActivity.this, errorData.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new VolleyErrorListener() {
            @Override
            public void onVolleyErrorResponse(VolleyError volleyError) {
                LogUtil.logNetworkResponse(volleyError, TAG);
                Toast.makeText(TestActivity.this, getResources().getString(R.string.internet_exception), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                User user = User.getInstance();
                Map<String, String> map = new HashMap<String, String>();
                map.put("userId", user.getId());
                map.put("yxdh", "1001"); //院校代号
                map.put("zydh", "01"); //专业代号
                map.put("zymc", "计算机科学及应用"); //专业名称
                map.put("lqpc", "1"); //录取批次
                map.put("source", "1"); //收藏来源 1为手工筛选 2为智能推荐

                return AppHelper.makeSimpleData("search", map);
            }
        };
        EduApp.sRequestQueue.add(request);
    }


    private void shaiXuan() {
        final FastJsonRequest request = new FastJsonRequest(Request.Method.POST, Url.SHAI_XUAN
                , null, new VolleyResponseListener(this) {
            @Override
            public void onSuccessfulResponse(JSONObject response, boolean success) {
                if (success) {
                    String datas = response.getString("datas");
                    ShaiXuanJieGuo result = JSON.parseObject(datas, ShaiXuanJieGuo.class);
                    Log.d(TAG, "shaixuan jieguo size: " + result.getYxzydata().size());
                } else {
                    ErrorData errorData = AppHelper.getErrorData(response);
                    Toast.makeText(TestActivity.this, errorData.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new VolleyErrorListener() {
            @Override
            public void onVolleyErrorResponse(VolleyError volleyError) {
                LogUtil.logNetworkResponse(volleyError, TAG);
                Toast.makeText(TestActivity.this, getResources().getString(R.string.internet_exception), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                User user = User.getInstance();
                Map<String, String> map = new HashMap<String, String>();
//                map.put("pageno", String.valueOf(1));
//                map.put("skey", "哈尔滨"); //搜索关键词
//                map.put("yxss", String.valueOf(7)); //院校省市 江苏省
//                map.put("yxlx", String.valueOf(9)); //院校类型 林业
//                map.put("yxxz", String.valueOf(2)); //院校性质 211
//                map.put("lqpc", String.valueOf(1)); //录取批次 特殊批
//                map.put("lqqk", "2013|1|500|550"); //历年录取情况 年份|查询类型|最低值|最高值 （2013年，按分数，最低500,最高550）
//                map.put("kskl", String.valueOf(2/*文史*/)); //科类代号 user.getKskl()
//                map.put("kqdh", String.valueOf(2/*上海*/)); //考区代号 user.getKqdh()


                map.put("pageno", String.valueOf(1));
                map.put("skey", "清华");
                map.put("yxss", "1|2|3");
                map.put("yxlx", "");
                map.put("yxxz", "1|2");
                map.put("lqpc", "3");
                map.put("lqqk", "2013|1|688|720");
                map.put("kskl", String.valueOf(1/*文史*/));
                map.put("kqdh", String.valueOf(2/*上海*/));

                return AppHelper.makeSimpleData("search", map);
            }
        };
        EduApp.sRequestQueue.add(request);
    }

    public List<Answer> makeAnswers() {
        List<Answer> answerList = new ArrayList<Answer>();
        Answer answer1 = new Answer();
        answer1.setDm(mQuestions.getTmDatas().get(0).getDm());
        answer1.setAns("A");
        answerList.add(answer1);

        Answer answer2 = new Answer();
        answer2.setDm(mQuestions.getTmDatas().get(1).getDm());
        answer2.setAns("A");
        answerList.add(answer2);

        Answer answer3 = new Answer();
        answer3.setDm(mQuestions.getTmDatas().get(2).getDm());
        answer3.setAns("A");
        answerList.add(answer3);

        Answer answer4 = new Answer();
        answer4.setDm(mQuestions.getTmDatas().get(3).getDm());
        answer4.setAns("B");
        answerList.add(answer4);

        Answer answer5 = new Answer();
        answer5.setDm(mQuestions.getTmDatas().get(4).getDm());
        answer5.setAns("B");
        answerList.add(answer5);

        Answer answer6 = new Answer();
        answer6.setDm(mQuestions.getTmDatas().get(5).getDm());
        answer6.setAns("B");
        answerList.add(answer6);

        Answer answer7 = new Answer();
        answer7.setDm(mQuestions.getTmDatas().get(6).getDm());
        answer7.setAns("C");
        answerList.add(answer7);

        Answer answer8 = new Answer();
        answer8.setDm(mQuestions.getTmDatas().get(7).getDm());
        answer8.setAns("D");
        answerList.add(answer8);

        Answer answer9 = new Answer();
        answer9.setDm(mQuestions.getTmDatas().get(8).getDm());
        answer9.setAns("D");
        answerList.add(answer9);

        Answer answer10 = new Answer();
        answer10.setDm(mQuestions.getTmDatas().get(9).getDm());
        answer10.setAns("D");
        answerList.add(answer10);
        return answerList;
    }

    public static class Answer {
        private String dm;
        private String ans;

        public String getDm() {
            return dm;
        }

        public void setDm(String dm) {
            this.dm = dm;
        }

        public String getAns() {
            return ans;
        }

        public void setAns(String ans) {
            this.ans = ans;
        }
    }

    private void answerQuestions(final List<Answer> answerList) {
        final FastJsonRequest request = new FastJsonRequest(Request.Method.POST, Url.XING_GE_CE_SHI_HUI_DA
                , null, new VolleyResponseListener(this) {
            @Override
            public void onSuccessfulResponse(JSONObject response, boolean success) {
                if (success) {

                } else {
                    ErrorData errorData = AppHelper.getErrorData(response);
                    Toast.makeText(TestActivity.this, errorData.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new VolleyErrorListener() {
            @Override
            public void onVolleyErrorResponse(VolleyError volleyError) {
                LogUtil.logNetworkResponse(volleyError, TAG);
                Toast.makeText(TestActivity.this, getResources().getString(R.string.internet_exception), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                User user = User.getInstance();
                Map<String, String> map = new HashMap<String, String>();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("request", "tmhd");
                int size = answerList.size();
                List<JSONObject> array = new ArrayList<JSONObject>();
                for (int i = 0; i < size; i++) {
                    Answer a = answerList.get(i);
                    JSONObject item = new JSONObject();
                    item.put("dm", a.getDm());
                    item.put("ans", a.getAns());
                    array.add(item);
                }

                JSONObject params = new JSONObject();
                params.put("userId", user.getId());
                params.put("count", String.valueOf(10));
                params.put("hdDatas", array);
                jsonObject.put("params", params);
                map.put("userData", jsonObject.toJSONString());
                if (EduApp.DEBUG) {
                    Log.d(TAG, "map: " + map);
                }
                return map;
            }
        };
        EduApp.sRequestQueue.add(request);
    }


    private Questions mQuestions = new Questions();
    private void getQuestions() {
        final FastJsonRequest request = new FastJsonRequest(Request.Method.POST, Url.XING_GE_CE_SHI_TI_MU
                , null, new VolleyResponseListener(this) {
            @Override
            public void onSuccessfulResponse(JSONObject response, boolean success) {
                if (success) {
                    String tmList = response.getString("tmList");
                    Questions questions = JSON.parseObject(tmList, Questions.class);
                    mQuestions = questions;
                    Log.v(TAG, "count: " + questions.getTmDatas().size());


                    List<Answer> answerList = makeAnswers();
                    answerQuestions(answerList);
                } else {
                    ErrorData errorData = AppHelper.getErrorData(response);
                    Toast.makeText(TestActivity.this, errorData.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new VolleyErrorListener() {
            @Override
            public void onVolleyErrorResponse(VolleyError volleyError) {
                LogUtil.logNetworkResponse(volleyError, TAG);
                Toast.makeText(TestActivity.this, getResources().getString(R.string.internet_exception), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return AppHelper.makeSimpleData("getTms", null);
            }
        };
        EduApp.sRequestQueue.add(request);
    }


    private UserInfo makeUserInfo() {
        User user = User.getInstance();
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getId());
        userInfo.setXm("苏"); //姓名
        userInfo.setSfzh("123456789012345678"); //身份证
        userInfo.setKscj(601); //分数
        userInfo.setKspw(10001); //排名
        userInfo.setKskl(2); //理工
        userInfo.setKqdh(3); //天津
        return userInfo;
    }

    private void updateKsxx(final UserInfo userInfo) {
        final FastJsonRequest request = new FastJsonRequest(Request.Method.POST, Url.KAO_SHENG_XIN_XI_XIU_GAI
                , null, new VolleyResponseListener(this) {
            @Override
            public void onSuccessfulResponse(JSONObject response, boolean success) {
                if (success) {
                    String ksxx = response.getString("ksxx");
                    UserInfo ui = JSON.parseObject(ksxx, UserInfo.class);
                    User user = User.getInstance();
                    user.setAccountId(ui.getAccountId());
                    user.setXm(ui.getXm());
                    user.setSfzh(ui.getSfzh());
                    user.setKscj(ui.getKscj());
                    user.setKspw(ui.getKspw());
                    user.setKskl(ui.getKskl());
                    user.setKsklName(ui.getKsklName());//科类名称
                    user.setKqdh(ui.getKqdh());//考区代号
                    user.setKskqName(ui.getKskqName());
                    User.saveUser(user);
                } else {
                    ErrorData errorData = AppHelper.getErrorData(response);
                    Toast.makeText(TestActivity.this, errorData.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new VolleyErrorListener() {
            @Override
            public void onVolleyErrorResponse(VolleyError volleyError) {
                LogUtil.logNetworkResponse(volleyError, TAG);
                Toast.makeText(TestActivity.this, getResources().getString(R.string.internet_exception), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userId", userInfo.getUserId());
                params.put("xm", userInfo.getXm());
                params.put("sfzh", userInfo.getSfzh());
                params.put("kscj", String.valueOf(userInfo.getKscj()));
                params.put("kspw", String.valueOf(userInfo.getKspw()));
                params.put("kskl", String.valueOf(userInfo.getKskl()));
                params.put("kskq", String.valueOf(userInfo.getKqdh()));
                return AppHelper.makeSimpleData("ksxxUpdate", params);
            }
        };
        EduApp.sRequestQueue.add(request);
    }

    @Override
    protected void unLoginForward(User user) {

    }

    @Override
    protected void forceUpdateForward() {

    }

    @Override
    protected void setupTitleBar() {

    }

    @Override
    protected String getTag() {
        return TAG;
    }
}
