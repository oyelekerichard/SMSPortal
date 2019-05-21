/*
 * Crown Interactive. Proprietary.
 */
package com.crowninteractive.smsportal.dto;

import com.crowninteractive.smsportal.util.ResponseCodes;

public class BaseResponse {

    private int retn;
    private String desc;
    private Object o;

    public BaseResponse(int retn, String desc) {
        this.retn = retn;
        this.desc = desc;
    }

    public BaseResponse() {
        this.desc = ResponseCodes.getDefaultMessageFor(ResponseCodes.REQUEST_SUCCESSFUL);
        this.retn = ResponseCodes.REQUEST_SUCCESSFUL;
    }

    public BaseResponse(Object o) {
        this.o = o;
        this.desc = ResponseCodes.getDefaultMessageFor(ResponseCodes.REQUEST_SUCCESSFUL);
        this.retn = ResponseCodes.REQUEST_SUCCESSFUL;
    }

    public Object getObj() {
        return o;
    }

    public void setObj(Object o) {
        this.o = o;
    }

    public int getRetn() {
        return retn;
    }

    public String getDesc() {
        return desc;
    }

    public void setRetn(int retn) {
        this.retn = retn;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
