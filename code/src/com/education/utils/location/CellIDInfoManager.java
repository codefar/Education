package com.souyidai.investment.android.utils.location;

import android.content.Context;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import com.souyidai.investment.android.Constants;
import com.souyidai.investment.android.SydApp;
import com.souyidai.investment.android.common.PhoneService;
import com.souyidai.investment.android.utils.NetDataUtil;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by su on 2014-06-03.
 * */
public class CellIDInfoManager {

    private static final boolean DEBUG = SydApp.DEBUG;
    private static final String TAG = "CellIDInfoManager";

    private CellIDInfoManager() {
    }

    /**
     * 4种定位方式：GPS定位，基站定位，WIFI定位和IP定位<br/>
     * 其中IP定位完全由后台处理；基站定位中，app只将基站信息与mcc、mnc数据传输给server，之后由server分析处理<br/>
     * 注意：返回的结果中，第一个CellIDInfo为当前所在Cell，其余为NeighboringCell
     */
    public static ArrayList<CellIDInfo> getCellIDInfo(Context context) {
        PhoneService phoneService = PhoneService.getInstance(context);
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        String mcc = phoneService.getMcc();
        String mnc = phoneService.getMnc();
        int type = manager.getNetworkType();
        String typeName = NetDataUtil.getNetworkTypeName(type);
        if (DEBUG) {
            Log.v(TAG, "type: " + typeName);
        }

        ArrayList<CellIDInfo> cellIdList = new ArrayList<CellIDInfo>();
        CellLocation cellLocation = manager.getCellLocation();
        if (cellLocation != null) {
            if (cellLocation instanceof GsmCellLocation) {
                GsmCellLocation gsmCellLocation = (GsmCellLocation) cellLocation;
                GSMCellInfo currentCell = new GSMCellInfo();
                int lac = gsmCellLocation.getLac();
                currentCell.mCid = gsmCellLocation.getCid();
                currentCell.mMCC = mcc;
                currentCell.mMNC = mnc;
                currentCell.mLac = lac;
                currentCell.mRadioType = typeName;
                currentCell.mDateString = Constants.SDF_YYYY_MM_DD_HH_MM_SS_SSS.format(new Date());
                cellIdList.add(currentCell);
                // neighbor cell info
                List<NeighboringCellInfo> list = manager.getNeighboringCellInfo();
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    NeighboringCellInfo neighboringCellInfo = list.get(i);
                    GSMCellInfo info = new GSMCellInfo();
                    info.mCid = neighboringCellInfo.getCid();
                    info.mMCC = mcc;
                    info.mMNC = mnc;
                    info.mRadioType = typeName;
//                currentCell.mRadioType = new CellIDInfo.RadioType("GSM", CellIDInfo.getNetworkType(type));
                    info.mLac =  neighboringCellInfo.getLac();
                    info.mDateString = Constants.SDF_YYYY_MM_DD_HH_MM_SS_SSS.format(new Date());
                    cellIdList.add(info);
                }
                return cellIdList;
            } else if (cellLocation instanceof CdmaCellLocation) {
                CdmaCellLocation cdmaCellLocation = (CdmaCellLocation) cellLocation;
                CDMACellInfo currentCell = new CDMACellInfo();
                currentCell.mStationId = cdmaCellLocation.getBaseStationId();
                currentCell.mNetworkId = cdmaCellLocation.getNetworkId();
                currentCell.mSystemId = cdmaCellLocation.getSystemId();
                currentCell.mMNC = mnc;
                currentCell.mMCC = mcc;
                currentCell.mDateString = Constants.SDF_YYYY_MM_DD_HH_MM_SS_SSS.format(new Date());
                currentCell.mRadioType = typeName;
//            currentCell.mRadioType = new CellIDInfo.RadioType("CDMA", CellIDInfo.getNetworkType(type));
                currentCell.mLatitude = cdmaCellLocation.getBaseStationLatitude();
                currentCell.mLongitude = cdmaCellLocation.getBaseStationLongitude();
                cellIdList.add(currentCell);

                // NeighboringCellInfo 中没有CDMA相关信息
                return cellIdList;
            } else {
                if (DEBUG) {
                    Log.w(TAG, "unknown cell location type: " + cellLocation);
                }
            }
        }
        return null;
    }
}
