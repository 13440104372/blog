package com.zbw.blog;

public class AppResponse <T>{

    private int code;
    private String message;
    private T data;
    private boolean isOK;
    private static final int SUCCESS_CODE = 200;

    public AppResponse(int code,String message,T data){
        this.code = code;
        this.message = message;
        this.data = data;
        isOK = code == SUCCESS_CODE;
    }

    public static <D> AppResponse<D> onSuccess(D data){
        return new AppResponse<>(SUCCESS_CODE,"OK!",data);
    }

    public static <T>AppResponse<T> onError(int code,String message){
        if(code == SUCCESS_CODE){
            throw new IllegalCallerException("please use onSuccess while the code == 200");
        }
        return new AppResponse<>(code,message,null);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isOK() {
        return isOK;
    }

    public void setOK(boolean OK) {
        isOK = OK;
    }
}
