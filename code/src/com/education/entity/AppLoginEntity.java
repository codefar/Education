package com.education.entity;

/**
 * Created by wangyonghua on 15-5-18.
 */
public class AppLoginEntity {
    public static final String STATUS_SUCCESS = "SUCCESS";
    public String accessToken;
    public long code;
    public String errorMessage;
    public String expireTime;
    public boolean setAccessToken;
    public boolean setCode;//       "setCode": true,
    public boolean setErrorMessage; //       "setErrorMessage": true,
    public boolean setExpireTime;  //      "setExpireTime": true,
    public boolean setMobile;//       "setMobile": false,
    public boolean setNickName;//       "setNickName": false,
    public boolean setStatus;//       "setStatus": true,
    public boolean setUserId;      // setUserId": true,
    public String status;          // "status": "SUCCESS",
    public long userId;//     "userId": 1045595

    public AppLoginEntity() {
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public boolean isSetAccessToken() {
        return setAccessToken;
    }

    public void setSetAccessToken(boolean setAccessToken) {
        this.setAccessToken = setAccessToken;
    }

    public boolean isSetCode() {
        return setCode;
    }

    public void setSetCode(boolean setCode) {
        this.setCode = setCode;
    }

    public boolean isSetErrorMessage() {
        return setErrorMessage;
    }

    public void setSetErrorMessage(boolean setErrorMessage) {
        this.setErrorMessage = setErrorMessage;
    }

    public boolean isSetExpireTime() {
        return setExpireTime;
    }

    public void setSetExpireTime(boolean setExpireTime) {
        this.setExpireTime = setExpireTime;
    }

    public boolean isSetMobile() {
        return setMobile;
    }

    public void setSetMobile(boolean setMobile) {
        this.setMobile = setMobile;
    }

    public boolean isSetNickName() {
        return setNickName;
    }

    public void setSetNickName(boolean setNickName) {
        this.setNickName = setNickName;
    }

    public boolean isSetStatus() {
        return setStatus;
    }

    public void setSetStatus(boolean setStatus) {
        this.setStatus = setStatus;
    }

    public boolean isSetUserId() {
        return setUserId;
    }

    public void setSetUserId(boolean setUserId) {
        this.setUserId = setUserId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "AppLoginEntity{" +
                "accessToken='" + accessToken + '\'' +
                ", code=" + code +
                ", errorMessage='" + errorMessage + '\'' +
                ", expireTime='" + expireTime + '\'' +
                ", setAccessToken=" + setAccessToken +
                ", setCode=" + setCode +
                ", setErrorMessage=" + setErrorMessage +
                ", setExpireTime=" + setExpireTime +
                ", setMobile=" + setMobile +
                ", setNickName=" + setNickName +
                ", setStatus=" + setStatus +
                ", setUserId=" + setUserId +
                ", status='" + status + '\'' +
                ", userId=" + userId +
                '}';
    }
}
