package org.wuneng.web.postcard.beans;

public class CheckResult {
    private boolean success;
    private Object payload;
    private String errCode;

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    @Override
    public String toString() {
        return "CheckResult{" +
                "success=" + success +
                ", payload=" + payload +
                ", errCode='" + errCode + '\'' +
                '}';
    }
}
