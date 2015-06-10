package com.education;

import java.text.SimpleDateFormat;

/**
 * Created by su on 2014/6/16.
 */
public class Constants {
    //判断手机号码
    public static final String MOBILE_REGEXP = "^1[34578][0-9]{9}$";

    public static final SimpleDateFormat SDF_YYYY_MM = new SimpleDateFormat("yyyy年MM月");
    public static final SimpleDateFormat SDF_YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat SDF_YYYY_MM_DD_SLASH = new SimpleDateFormat("yyyy/MM/dd");
    public static final SimpleDateFormat SDF_YYYY_MM_DD_HH_MM = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static final SimpleDateFormat SDF_YYYY_MM_DD_HH_MM_SS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat SDF_YYYY_MM_DD_HH_MM_SS_FILE_NAME = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
    public static final SimpleDateFormat SDF_YYYY_MM_DD_T_HH_MM_SS = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    public static final SimpleDateFormat SDF_YYYY_MM_DD_HH_MM_SS_SSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");

    public static final String LOCK_PATTERN_FILE = "gesture.key";
    public static final String LOCK_PASSWORD_FILE = "password.key";

    public static final String PASSWORD_STR = "[~`!@#$%^&*()_=+\\[{\\]}\\|;:'\",<.>/?0-9a-zA-Z-]{6,20}";
    /*
    * same as microlog.appender.FileAppender.File in assets/microlog.properties
    * */
    public static final String APP_DIR_ROOT = "souyidai/";
    public static final String APP_DIR_TEMP = "souyidai/temp/";
    public static final String APP_DIR_PIC = "souyidai/pic/";
    public static final String APP_DIR_IMG_TEMP = "souyidai/img/temp";
    public static final String PRODUCT_ID = "T";

    public static final String SP_COLUMN_INIT = "init";
    public static final String SP_COLUMN_UUID = "uuid";
    public static final String SP_COLUMN_USER_ID = "user_id";

    public static final String SP_COLUMN_USER_ACCOUNTID = "user_accountId";
    public static final String SP_COLUMN_USER_XM = "user_xm";
    public static final String SP_COLUMN_USER_SFZH = "user_sfzh";
    public static final String SP_COLUMN_USER_KSCJ = "user_kscj";
    public static final String SP_COLUMN_USER_KSPW = "user_kspw";
    public static final String SP_COLUMN_USER_KSKL = "user_kskl";
    public static final String SP_COLUMN_USER_KSKLNAME = "user_ksklName";
    public static final String SP_COLUMN_USER_KQDH = "user_kqdh";
    public static final String SP_COLUMN_USER_KSKQNAME = "user_kskqName";


    public static final String SP_COLUMN_USER_SESSION = "user_session";
    public static final String SP_COLUMN_USER_LOGIN_TIME = "user_login_time";
    public static final String SP_COLUMN_USER_PHONE_NUMBER = "user_phone_number";
    public static final String SP_COLUMN_USER_PHONE_NICKNAME = "user_phone_nickname";
    public static final String SP_COLUMN_USER_PHONE_EMAIL = "user_phone_email";
    public static final String SP_COLUMN_USER_PHONE_ID5 = "user_phone_id5";
    public static final String SP_COLUMN_USER_EXPIRE_TIME = "user_expire_time";
    public static final String SP_COLUMN_USER_PROVINCE = "user_province";
    public static final String SP_COLUMN_USER_PROVINCE_ID = "user_province_id";
    public static final String SP_COLUMN_NEED_TO_UPDATE = "need_to_update";
    public static final String SP_COLUMN_FORCE_TO_UPDATE = "force_to_update";
    public static final String SP_COLUMN_NEW_VERSION_APK_NAME = "new_version_apk_name";
    public static final String SP_COLUMN_TEMP_USER_ID = "temp_user_id";
    public static final String SP_COLUMN_SHOW_UPLOAD_HINT = "show_upload_hint";
    public static final String SP_COLUMN_NEW_VERSION = "new_version";
    public static final String SP_COLUMN_NEW_VERSION_URL = "new_version_url";
    public static final String SP_COLUMN_NEW_VERSION_DESCRIPTION = "new_version_description";
    public static final String SP_COLUMN_ENVIRONMENT = "environment";
    public static final String SP_COLUMN_NICKNAME_STATUS = "nickname_status";

    public static final boolean DEBUG = false;
}
