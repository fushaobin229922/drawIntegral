package com.chtwm.mall.sdk.controller.pc.common;

import java.io.Serializable;

import com.hengtianjf.basic.business.tools.common.constant.GlobalResultCode;


/**
 * 响应的数据
 * @author jannal
 *
 * @param <T>
 */
public class CommonAppResponseBody<T> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    public final static  String SUCCESS_MSG = "处理成功！";
    public final static  String ERROR_MSG = "处理失败！";
    public final static String SUCCESS_STATUS = "0";// 成功
    public final static String FAILED_STATUS = "1";// 失败
    
    
    // 密钥(返回的密钥可以不要)
    private String hmac="hmac";
    // 状态码
    private String status = SUCCESS_STATUS;
    //特殊意义的编码
    private String code = GlobalResultCode.CommonResultCode.OPERATION_SUCCESS;
    // 消息
    private String msg=SUCCESS_MSG;
    // 响应的数据
    private T data ;
    
    
    public CommonAppResponseBody() {
    }
    
    
    public CommonAppResponseBody(T data) {
        this.data = data;
    }


    public CommonAppResponseBody(String status, String msg) {
        super();
        this.status = status;
        this.msg = msg;
    }
    public CommonAppResponseBody(String status, String msg,String code) {
        super();
        this.status = status;
        this.msg = msg;
        this.code=code;
    }
    
    public CommonAppResponseBody(String status,String msg,String code, T data) {
        super();
        this.status = status;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    
    public static <T>  CommonAppResponseBody<T> defaultFail(){
        return new CommonAppResponseBody<T>(CommonAppResponseBody.FAILED_STATUS,ERROR_MSG,GlobalResultCode.CommonResultCode.OPERATION_FAILED);
    } 
    
    public  static  <T> CommonAppResponseBody<T> defaultSuccess(){
        return new CommonAppResponseBody<T>(CommonAppResponseBody.SUCCESS_STATUS,SUCCESS_MSG,GlobalResultCode.CommonResultCode.OPERATION_SUCCESS);
    }

    public String getHmac() {
        return hmac;
    }

    public void setHmac(String hmac) {
        this.hmac = hmac;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
    
    
}
