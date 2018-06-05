package cn.com.gcg.protocol;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by Jack on 2018/6/4.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MonitorResponse<T> {
    private String code;
    private String message;
    private T result;
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public T getResult() {
        return result;
    }
    public void setResult(T result) {
        this.result = result;
    }

    private MonitorResponse(){};
    private MonitorResponse(String code, String message){
        this.code = code;
        this.message = message;
    };
    private MonitorResponse(String code, String message, T t){
        this.code = code;
        this.message = message;
        this.result = t;
    };

    public static<T> MonitorResponse<T> success(T t){
        return new MonitorResponse<T>(ExceptionEnum.SystemOk.getCode(),ExceptionEnum.SystemOk.getMessage(),t) ;
    }

    public static<T> MonitorResponse<T> success(){
        return new MonitorResponse<T>(ExceptionEnum.SystemOk.getCode(),ExceptionEnum.SystemOk.getMessage()) ;
    }

    public static<T> MonitorResponse<T> fail(String code, String message) {
        return new MonitorResponse<T>(code, message);

    }
}
