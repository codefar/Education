package com.education;

/**
 * Created by su on 15-6-5.
 */
public class Url {

    public static final String SCHEME = "http://10.10.18.7:8018";
    public static final String LOGIN = SCHEME + "/login"; //登录
    public static final String REGISTER_GET_SMS_CODE = SCHEME + "/register/getSMCode"; //获取短信
    public static final String REGISTER_CHECK_SMS_CODE = SCHEME + "/register/chkSMCode"; //检查短信
    public static final String REGISTER = SCHEME + "/register/regUser"; //注册
    public static final String CHANGE_PASSWORD = SCHEME + "/my/setpassword"; //修改密码
    public static final String SET_ACCOUNT = SCHEME + "/my/setaccount"; //设置账户
    public static final String RESET_PASSWORD = SCHEME + "/my/resetPassword";
    public static final String GET_SHARE_URL = SCHEME + "/share/getShareUrl"; //获取分享链接
    public static final String GET_QUESTION = "http://www.benshijy.com:8018/zntj/getTms";

    public static final String KAO_SHENG_XIN_XI = SCHEME + "/my/ksxxQuery"; //考生信息查询
    public static final String KAO_SHENG_XIN_XI_XIU_GAI = SCHEME + "/my/ksxxUpdate"; //考生信息修改

    public static final String XING_GE_CE_SHI_HUI_DA_ZHUANG_TAI = SCHEME + "/zntj/start"; //获取题目回答状态
    public static final String XING_GE_CE_SHI_TI_MU = SCHEME + "/zntj/getTms"; //获取题目
    public static final String XING_GE_CE_SHI_HUI_DA = SCHEME + "/zntj/tmhd"; //题目回答
    public static final String TUI_JIAN_XIN_XI = SCHEME + "/zntj/recommenreport"; //智能推荐-获取推荐信息
    public static final String ZHUAN_YE_FEN_XI_BAO_GAO = SCHEME + "/zntj/zyfx"; //智能推荐-单专业分析报告


    public static final String SHAI_XUAN = SCHEME + "/sgsx/search"; //筛选接口(搜索默认页的接口和正常的筛选接口)
    public static final String SHAI_XUAN_BY_COLLEGE = SCHEME + "/sgsx/searchmajor"; //根据院校筛选专业
    public static final String SHAI_XUAN_BY_MAJOR = SCHEME + "/sgsx/admission"; //专业历年录取情况 (手工筛选-专业录取情况)


    public static final String SHOU_CANG_ZHUAN_YE = SCHEME + "/zysc/collectmajor"; //收藏专业
    public static final String SHOU_CANG_YUAN_XIAO_LIE_BIAO = SCHEME + "/zysc/getcollectschool"; //收藏院校列表
    public static final String SHOU_CANG_ZHUAN_YE_LIE_BIAO = SCHEME + "/zysc/getcollectmajor"; //收藏专业列表
}

