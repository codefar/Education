package com.education.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.education.EduApp;
import com.education.Constants;
import com.education.utils.LogUtil;
import com.education.utils.NetDataUtil;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * Created by su on 2014/6/10.<br/>
 * 获取双卡接口方式：<br/>
 * Qualcomm:context.getSystemService(Context.MSIM_TELEPHONY_SERVICE); // MSimTelephonyManager phone_msim<br/>
 * MTK:TelephonyManager.getDeviceIdGemini(int) <br/>
 * Spreadtrum:TelephonyManager.getDefault(int)
 */

public class PhoneService {
    private static final boolean DEBUG = EduApp.DEBUG;
    private static final String TAG = "PhoneService";

    private static PhoneService sPhoneService;
    private String mDateString;
    /**
     * hardware info
     * */
    private static final String MODEL = Build.MODEL;
    private static final String MANUFACTURER = Build.MANUFACTURER;
    private static final String DEVICE_SERIAL = Build.SERIAL;
    private static final String SDK_INT = String.valueOf(Build.VERSION.SDK_INT);

    /**
     * telephony info
     * */
    private String mImeiSIM1;
    /**
     * 注意：一些双卡手机有两个imei，一些只有一个
     * */
    private String mImeiSIM2;
    private boolean mIsSIM1Ready;
    private boolean mIsSIM2Ready;
    private String mSerialSim1;
    private String mSerialSim2;
    private String mImsiSim1;
    private String mImsiSim2;
    /**
     * 如果是GSM，返回MSISDN，例如：CC+NDC+SN 86+133+... <br/>
     * 如果不可用，则返回null。<br/>
     * 注意：这是一个辅助方法，一般情况下，通过这种办法是无法获得手机号码的
     * */
    private String mLine1Number1;
    private String mLine1Number2;

    /**
     * 注意：只有注网之后才会得到
     * */
    @SuppressWarnings("unused")
    private String operator1;
    @SuppressWarnings("unused")
    private String operator2;

    /**
     * 传输telephony info时无需传输mcc和mnc，后台可以从imsi中提取。<br/>
     * 后台定位时需要传输mcc和mnc，单独传送即可。
     * */
    private String mMcc1;
    private String mMcc2;
    private String mMnc1;
    private String mMnc2;

    private static void loadMccMnc() {
        if (sPhoneService.mImsiSim1 != null && sPhoneService.mImsiSim1.length() == 5) {
            sPhoneService.mMcc1 = sPhoneService.mImsiSim1.substring(0, 3);
            sPhoneService.mMnc1 = sPhoneService.mImsiSim1.substring(3, 5);
        }
        if (sPhoneService.mImsiSim2 != null && sPhoneService.mImsiSim2.length() == 5) {
            sPhoneService.mMcc2 = sPhoneService.mImsiSim2.substring(0, 3);
            sPhoneService.mMnc2 = sPhoneService.mImsiSim2.substring(3, 5);
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("date: " + mDateString + " | ");
        sb.append("Model: " + MODEL + " | ");
        sb.append("Manufacturer: " + MANUFACTURER + " | ");
        sb.append("DeviceSerial: " + DEVICE_SERIAL + " | ");
        sb.append("SdkInt: " + SDK_INT + " | ");
        sb.append("ImeiSIM1: " + mImeiSIM1 + " | ");
        sb.append("SerialSim1: " + mSerialSim1 + " | ");
        sb.append("ImsiSim1: " + mImsiSim1 + " | ");
        sb.append("Mcc1: " + mMcc1 + " | ");
        sb.append("Mnc1: " + mMnc1 + " | ");
        sb.append("Line1Number1: " + mLine1Number1 + " | ");
        sb.append("IsSIM1Ready: " + mIsSIM1Ready + " | ");

        sb.append("ImeiSIM2: " + mImeiSIM2 + " | ");
        sb.append("SerialSim2: " + mSerialSim2 + " | ");
        sb.append("ImsiSim2: " + mImsiSim2 + " | ");
        sb.append("Mcc2: " + mMcc2 + " | ");
        sb.append("Mnc2: " + mMnc2 + " | ");
        sb.append("Line1Number2: " + mLine1Number2 + " | ");
        sb.append("IsSIM2Ready: " + mIsSIM2Ready);
        return sb.toString();
    }

    public static void saveToSP(PhoneService phoneService, SharedPreferences sp) {
        sp.edit().putString("date_string",phoneService.mDateString)
                .putString("model", phoneService.MODEL)
                .putString("manufacturer", phoneService.MANUFACTURER)
                .putString("device_serial", phoneService.DEVICE_SERIAL)
                .putString("sdk_int", phoneService.SDK_INT)
                .putString("imei_sim1", phoneService.mImeiSIM1)
                .putString("serial_sim1", phoneService.mSerialSim1)
                .putString("imsi_sim1", phoneService.mImsiSim1)
                .putString("line1_number1", phoneService.mLine1Number1)
                .putString("mcc1", phoneService.mMcc1)
                .putString("mnc1", phoneService.mMnc1)
                .putString("is_sim1_ready", String.valueOf(phoneService.mIsSIM1Ready))
                .putString("imei_sim2", phoneService.mImeiSIM2)
                .putString("serial_sim2", phoneService.mSerialSim2)
                .putString("imsi_sim2", phoneService.mImsiSim2)
                .putString("line1_number2", phoneService.mLine1Number2)
                .putString("mcc2", phoneService.mMcc2)
                .putString("mnc2", phoneService.mMnc2)
                .putString("is_sim2_ready", String.valueOf(phoneService.mIsSIM2Ready)).apply();
    }

    public static void copy(Context context) {
        getInstance(context);

    }

    public String getDateString() {
        return mDateString;
    }

    public String getMcc1() {
        return mMcc1;
    }

    public String getMcc() {
        return mMcc1 == null ? mMcc2 : mMcc1;
    }

    public String getMnc() {
        return mMnc1 == null ? mMnc2 : mMnc1;
    }

    public String getMcc2() {
        return mMcc2;
    }

    public String getMnc1() {
        return mMnc1;
    }

    public String getMnc2() {
        return mMnc2;
    }

    public String getImsiSim1() {
        return mImsiSim1;
    }

    public String getImsiSim2() {
        return mImsiSim2;
    }

    public String getImeiSIM1() {
        return mImeiSIM1;
    }

    public String getImeiSIM2() {
        return mImeiSIM2;
    }

    public boolean isSIM1Ready() {
        return mIsSIM1Ready;
    }

    public boolean isSIM2Ready() {
        return mIsSIM2Ready;
    }

    public String getSerialSim1() {
        return mSerialSim1;
    }

    public String getSerialSim2() {
        return mSerialSim2;
    }

    public String getLine1Number1() {
        return mLine1Number1;
    }

    public String getLine1Number2() {
        return mLine1Number2;
    }

    public String getModel() {
        return MODEL;
    }

    public String getManufacturer() {
        return MANUFACTURER;
    }

    public String getDeviceSerial() {
        return DEVICE_SERIAL;
    }

    public String getSdkInt() {
        return SDK_INT;
    }

    private PhoneService() { }

    public static PhoneService getInstance(Context context) {
        return reload(context, false);
    }

    public static TelephonyManager getSpreadtrumTelephonyManager(int slotID) {
        try{
            Class<?> tmClass = TelephonyManager.class;
            Method method = tmClass.getDeclaredMethod("getDefault", new Class[]{ int.class });
            TelephonyManager telephonyManager = (TelephonyManager) method.invoke(tmClass, new Object[]{slotID});
            return telephonyManager;
        } catch (Exception e) {
            if (DEBUG) {
                Log.w(TAG, e);
            }
        }

        return null;
    }

    private static boolean loadQualcomm(Context context) {
        try {
            Class<?> tmClass = Class.forName("android.telephony.MSimTelephonyManager");
            Object msimTelephonyManager = context.getSystemService("phone_msim");
            if (msimTelephonyManager != null) {
                sPhoneService.mImeiSIM2 = getQualcommTelephonyBySlot(tmClass, msimTelephonyManager, "getDeviceId", 1);
                sPhoneService.mSerialSim2 = getQualcommTelephonyBySlot(tmClass, msimTelephonyManager, "getSimSerialNumber", 1);
                sPhoneService.mImsiSim2 = getQualcommTelephonyBySlot(tmClass, msimTelephonyManager, "getSubscriberId", 1);
                sPhoneService.mLine1Number2 = getQualcommTelephonyBySlot(tmClass, msimTelephonyManager, "getLine1Number", 1);
                sPhoneService.mIsSIM2Ready = getQualcommSIMStateBySlot(tmClass, msimTelephonyManager, "getSimState", 1);
                return true;
            }
        } catch (Exception e) {
            if (DEBUG) {
                Log.d(TAG, "loadQualcomm failed!", e);
            }
        }
        return false;
    }

    private static  boolean getQualcommSIMStateBySlot(Class<?> tmClass, Object msimTelephonyManager,
                                                      String predictedMethodName, int slotID) throws GeminiMethodNotFoundException {
        boolean isReady = false;
        try{
            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            Method getSimStateGemini = tmClass.getMethod(predictedMethodName, parameter);

            Object[] obParameter = new Object[1];
            obParameter[0] = slotID;
            Object ob_phone = getSimStateGemini.invoke(msimTelephonyManager, obParameter);

            if(ob_phone != null){
                int simState = Integer.parseInt(ob_phone.toString());
                if(simState == TelephonyManager.SIM_STATE_READY){
                    isReady = true;
                }
            }
        } catch (Exception e) {
            if (DEBUG) {
                Log.w(TAG, e);
            }
            throw new GeminiMethodNotFoundException(predictedMethodName);
        }

        return isReady;
    }

    private static String getQualcommTelephonyBySlot(Class<?> tmClass, Object msimTelephonyManager,
                                                     String predictedMethodName, int slotID) throws GeminiMethodNotFoundException {
        String imei = null;
        try{

            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            Method getSimID = tmClass.getMethod(predictedMethodName, parameter);

            Object[] obParameter = new Object[1];
            obParameter[0] = slotID;
            Object ob_phone = getSimID.invoke(msimTelephonyManager, obParameter);

            if(ob_phone != null){
                imei = ob_phone.toString();
            }
        } catch (Exception e) {
            if (DEBUG) {
                Log.w(TAG, e);
            }
            throw new GeminiMethodNotFoundException(predictedMethodName);
        }

        return imei;
    }

    private static boolean loadMtk(Context context) {
        try {
            sPhoneService.mImeiSIM2 = getDeviceIdBySlot(context, "getDeviceIdGemini", 1);
            sPhoneService.mSerialSim2 = getDeviceIdBySlot(context, "getSimSerialNumberGemini", 1);
            sPhoneService.mImsiSim2 = getDeviceIdBySlot(context, "getSubscriberIdGemini", 1);
            sPhoneService.mLine1Number2 = getDeviceIdBySlot(context, "getLine1NumberGemini", 1);
            sPhoneService.mIsSIM2Ready = getSIMStateBySlot(context, "getSimStateGemini", 1);
            return true;
        } catch (Exception e) {
            if (DEBUG) {
                Log.d(TAG, "loadMtk failed!", e);
            }
        }
        return false;
    }

    private static boolean loadSpreadtrum(Context context) {
        TelephonyManager telephonyManager = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));
        try {
            Class<?> telephonyClass = Class.forName(telephonyManager.getClass().getName());
            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            Method isMultiSim = telephonyClass.getMethod("isMultiSim");
            Boolean isMultiSimPhone = (Boolean) isMultiSim.invoke(telephonyManager, null);

            if (isMultiSimPhone) {
                TelephonyManager telephonyManager1 = getSpreadtrumTelephonyManager(1);
                if (telephonyManager1 != null) {
                    sPhoneService.mImeiSIM2 = telephonyManager1.getDeviceId();
                    sPhoneService.mSerialSim2 = telephonyManager1.getSimSerialNumber();
                    sPhoneService.mLine1Number2 = telephonyManager1.getLine1Number();
                    sPhoneService.mImsiSim2 = telephonyManager1.getSubscriberId();
                    sPhoneService.mIsSIM2Ready = telephonyManager1.getSimState() == TelephonyManager.SIM_STATE_READY;
                    return true;
                }
            } else {
                return true;
            }
        } catch (Exception e) {
            if (DEBUG) {
                Log.d(TAG, "loadSpreadtrum failed!", e);
            }
        }
        return false;
    }

    private static boolean loadOthers(Context context) {
        try {
            sPhoneService.mImeiSIM2 = getDeviceIdBySlot(context, "getDeviceId", 1);
            sPhoneService.mSerialSim2 = getDeviceIdBySlot(context, "getSimSerialNumber", 1);
            sPhoneService.mImsiSim2 = getDeviceIdBySlot(context, "getSubscriberId", 1);
            sPhoneService.mLine1Number2 = getDeviceIdBySlot(context, "getLine1Number", 1);
            sPhoneService.mIsSIM2Ready = getSIMStateBySlot(context, "getSimState", 1);
            return true;
        } catch (Exception e) {
            if (DEBUG) {
                Log.d(TAG, "loadMtk failed!", e);
            }
        }
        return false;
    }

    /**
     * 按如下顺序进行尝试：Qualcomm，mtk，Spreadtrum<br/>
     * TODO 华为海思(HiSilicon)，samsung（Exynos），NVIDIA，intel<br/>
     * 注：TI（已退出手机cpu市场）<br/>
     * return 之前load mcc mnc
     * */
    private static void load(Context context) {
        sPhoneService.mDateString = Constants.SDF_YYYY_MM_DD_HH_MM_SS.format(new Date());
        TelephonyManager telephonyManager = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));

        sPhoneService.mImeiSIM1 = telephonyManager.getDeviceId();
        sPhoneService.mImeiSIM2 = null;
        sPhoneService.mSerialSim1 = telephonyManager.getSimSerialNumber();
        sPhoneService.mSerialSim2 = null;
        sPhoneService.mImsiSim1 = telephonyManager.getSubscriberId();
        sPhoneService.mImsiSim2 = null;
        sPhoneService.mLine1Number1 = telephonyManager.getLine1Number();
        sPhoneService.mLine1Number2 = null;
        sPhoneService.mIsSIM1Ready = telephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY;
        sPhoneService.mIsSIM2Ready = false;

        if (!loadQualcomm(context) || !loadMtk(context) || !loadSpreadtrum(context) || !loadOthers(context)) {
            if (DEBUG) {
                Log.w(TAG, "Failed to get 2nd telephony info!!!!!");
                Log.w(TAG, "Maybe it isn't a dual sim phone!");
            }
        }

        loadMccMnc();
    }

    public void reload(Context context) {
        reload(context, true);
    }

    private static PhoneService reload(Context context, boolean reload) {
        if(sPhoneService == null) {
            sPhoneService = new PhoneService();
            load(context);
        } else {
            if (reload) {
                if (DEBUG) {
                    Log.d(TAG, "----------------------reload---------------------------");
                }
                load(context);
            }
        }

        if (DEBUG) {
            Log.d(TAG, sPhoneService.toString());
        }
        return sPhoneService;
    }

    private static String getDeviceIdBySlot(Context context, String predictedMethodName, int slotID) throws GeminiMethodNotFoundException {
        String imei = null;
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try{
            Class<?> telephonyClass = Class.forName(telephony.getClass().getName());

            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            Method getSimID = telephonyClass.getMethod(predictedMethodName, parameter);

            Object[] obParameter = new Object[1];
            obParameter[0] = slotID;
            Object ob_phone = getSimID.invoke(telephony, obParameter);

            if(ob_phone != null){
                imei = ob_phone.toString();
            }
        } catch (Exception e) {
            if (DEBUG) {
                Log.w(TAG, e);
            }
            throw new GeminiMethodNotFoundException(predictedMethodName);
        }

        return imei;
    }

    private static  boolean getSIMStateBySlot(Context context, String predictedMethodName, int slotID) throws GeminiMethodNotFoundException {
        boolean isReady = false;
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        try{
            Class<?> telephonyClass = Class.forName(telephony.getClass().getName());
            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            Method getSimStateGemini = telephonyClass.getMethod(predictedMethodName, parameter);

            Object[] obParameter = new Object[1];
            obParameter[0] = slotID;
            Object ob_phone = getSimStateGemini.invoke(telephony, obParameter);

            if(ob_phone != null){
                int simState = Integer.parseInt(ob_phone.toString());
                if(simState == TelephonyManager.SIM_STATE_READY){
                    isReady = true;
                }
            }
        } catch (Exception e) {
            if (DEBUG) {
                Log.w(TAG, e);
            }
            throw new GeminiMethodNotFoundException(predictedMethodName);
        }

        return isReady;
    }

    private static class GeminiMethodNotFoundException extends Exception {
        public GeminiMethodNotFoundException(String info) {
            super(info);
        }
    }

    public static String getUUID(Context context) {
        PhoneService phoneService = PhoneService.getInstance(context);
        String uuid = phoneService.getDeviceSerial() + phoneService.getImeiSIM1();
        uuid = LogUtil.md5Hex(uuid);
        if (uuid != null) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            if (!sp.getString(Constants.SP_COLUMN_UUID, "").equals(uuid)) {
                sp.edit().putString(Constants.SP_COLUMN_UUID, uuid).apply();
                if (DEBUG) {
                    Log.e(TAG, "uuid has changed!");
                }
            }
            return uuid;
        }
        if (DEBUG) {
            Log.e(TAG, "There's no device id, imei and mac address here!");
        }
        return null;
    }

    public static void printTelephonyManagerMethodNamesForThisDevice(Context context) {
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        Class<?> telephonyClass;
        Log.d(TAG, telephony.getClass().getName());
        try {
            telephonyClass = Class.forName(telephony.getClass().getName());
            Method[] methods = telephonyClass.getMethods();
            for (int idx = 0; idx < methods.length; idx++) {
                Log.v(TAG, methods[idx] + " declared by " + methods[idx].getDeclaringClass());
            }
        } catch (ClassNotFoundException e) {
            if (DEBUG) {
                Log.w(TAG, e);
            }
        }
        Log.d(TAG, "android.telephony.MSimTelephonyManager");
        try {
            telephonyClass = Class.forName("android.telephony.MSimTelephonyManager");
            Method[] methods = telephonyClass.getMethods();
            for (int idx = 0; idx < methods.length; idx++) {
                Log.v(TAG, methods[idx] + " declared by " + methods[idx].getDeclaringClass());
            }
        } catch (ClassNotFoundException e) {
            if (DEBUG) {
                Log.w(TAG, e);
            }
        }
        Log.d(TAG, "+++++++++++++++++++++++++++++++++++++++++++");
        try {
            Object o = context.getSystemService("phone_msim");
            Log.d(TAG, "getName: " + o.getClass().getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "+++++++++++++++++++++++++++++++++++++++++++");
    }
}
