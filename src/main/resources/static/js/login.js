/**
 * Created by Jack on 2018/6/4.
 */
$(function () {
    $("#login").click(function () {
        var userName = $("#username").val();
        var passWord = $("#password").val();
        $.ajax({
            url: "/user/login",
            data: JSON.stringify(
                {"username": userName, "password": passWord}
            ),//参数列表,
            type: "POST",
            contentType:"application/json",
            dataType:"json",
            success: function (result) {
                alert("success");
            },
            error: function (result) {
                alert("登陆失败");
            }

        })


    })


})
