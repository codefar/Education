package com.education;

/**
 * Created by su on 15-6-5.
 */
public class Url {

    public static final String SCHEME = "http://www.benshijy.com:8018";
    public static final String LOGIN = SCHEME + "/login"; //登录
    public static final String REGISTER_GET_SMS_CODE = SCHEME + "/register/getSMCode"; //获取短信
    public static final String REGISTER_CHECK_SMS_CODE = SCHEME + "/register/chkSMCode"; //检查短信
    public static final String REGISTER = SCHEME + "/register/regUser"; //注册
    public static final String CHANGE_PASSWORD = SCHEME + "/my/setpassword"; //修改密码
    public static final String SET_ACCOUNT = SCHEME + "/my/setaccount"; //设置账户
    public static final String RESET_PASSWORD = SCHEME + "/my/resetPassword";
    public static final String GET_SHARE_URL = SCHEME + "/share/getShareUrl"; //获取分享链接
    public static final String SHOU_CANG_YUAN_XIAO_LIE_BIAO = "/zysc/getYx";
    public static final String GET_QUESTION = "http://www.benshijy.com:8018/zntj/getTms";

    public static final String KAO_SHENG_XIN_XI = SCHEME + "/my/ksxxQuery"; //考生信息查询
    public static final String KAO_SHENG_XIN_XI_XIU_GAI = SCHEME + "/my/ksxxUpdate"; //考生信息修改

    public static final String XING_GE_CE_SHI_TI_MU = SCHEME + "/zntj/getTms";
    public static final String XING_GE_CE_SHI_HUI_DA = SCHEME + "/zntj/tmhd";
}

