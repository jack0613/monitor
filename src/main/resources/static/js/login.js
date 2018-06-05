/**
 * Created by Jack on 2018/6/4.
 */
$(function () {
    $("#login").click(function () {
        var userName = $("#username").val();
        var passWord = $("#password").val();
        if(userName ==null || userName =='' || passWord ==null || passWord==''){
            alert("用户名或密码不能为空");
            return;
        }
        var url = "/user/login/"+userName
        $.ajax({
            url: url,
            data: JSON.stringify(
                {"username": userName, "password": passWord}
            ),//参数列表,
            type: "POST",
            contentType:"application/json",
            dataType:"json",
            success: function (result) {
               // alert("success");
                if(result.flag=='success'){

                    location.href=""
                }else{
                    alert('用户名或密码错误');
                }
            },
            error: function (result) {
                alert("登陆失败");
            }

        })


    })


})
