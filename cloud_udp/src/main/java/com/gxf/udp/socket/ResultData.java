package com.gxf.udp.socket;

/**
 * Created by 58 on 2017/7/11.
 */
public class ResultData {
    private int sessionId;
    private DataReturnedEvent dataReturnedEvent;
    private byte[] result;
    private int resultCode;

    public ResultData(DataReturnedEvent dataReturnedEvent) {
        this.dataReturnedEvent = dataReturnedEvent;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public DataReturnedEvent getDataReturnedEvent() {
        return dataReturnedEvent;
    }

    public void setDataReturnedEvent(DataReturnedEvent dataReturnedEvent) {
        this.dataReturnedEvent = dataReturnedEvent;
    }

    public byte[] getResult() {
        return result;
    }

    public void setResult(byte[] result) {
        this.result = result;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
}
