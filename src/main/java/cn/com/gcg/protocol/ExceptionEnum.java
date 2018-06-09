package cn.com.gcg.protocol;



//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;

/**
 * Created by Jack on 2018/6/4.
 */
public enum ExceptionEnum {
    SystemOk("000000", "请求成功"),
    SystemError("999999", "系统错误，请稍后重试"),
    UserNameOrPassWordError("USER200001", "用户名或密码错误"),
    UserNonentity("USER200001", "用户不存在"),
    UserNameOrPassWordErrorCount("USER200002", "用户名或密码错误%d次，%d分钟后登录"),
    LoginTooMuch("USER200003", "操作过于频繁请稍后重试"),
    PhoneBeRegister("USER200004", "手机号%s已经被注册"),
    PhoneCodeError("USER200005", "验证码错误"),
    PhoneCodeSendFail("USER200006", "手机号%s，验证码发送失败"),
    PhoneCodeNull("USER200007", "验证码失效"),
    PhoneNumberError("USER200008", "输入手机号应为11位数"),
    TokenNotFind("USER200009", "无权限，请求被拒绝"),
    PhoneFormatError("USER200010", "输入手机号错误格式！！！"),
    FileIsNull("FILE300001", "上传文件为空"),
    StringZipErrot("Question400001", "题目内容压缩失败"),
    StringUnZipErrot("Question400002", "题目内容解压失败");

    private String message;
    private String code;

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    private ExceptionEnum(String code, String message) {
        this.message = message;
        this.code = code;
    }

   /* public static ExceptionEnum getByCode(String code) {
        List<ExceptionEnum> list = (List) Arrays.stream(values()).filter((e) -> {
            return StringUtils.equals(e.getCode(), code);
        }).collect(Collectors.toList());
        return Optional.ofNullable(list).isPresent() && list.size() > 0?(ExceptionEnum)list.get(0):SystemError;
    }*/
}
