package com.education.utils.location;

import android.telephony.SignalStrength;

public class CellIDInfo {
    protected String mMCC;
    protected String mMNC;
    protected String mDateString;
    @SuppressWarnings("unused")
    protected SignalStrength mSignalStrength; //TODO 可以通过PhoneStateListener 来获取，如果有必要的话
    protected String mRadioType;

    public CellIDInfo() {
//        mRadioType = radioType;
    }
}
