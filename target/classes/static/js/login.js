$(function () {
    $("#submit").click(function () {
        g_showLoading();
        //MD5
        var inputPass = $("#password").val();
        var salt = g_passsword_salt;
        var str = ""+salt.charAt(0)+salt.charAt(2) + inputPass +salt.charAt(5) + salt.charAt(4);
        var password = md5(str);
        console.log(password);

        $.ajax('/login/do_login', {
            data: {
                mobile: $("#mobile").val(),
                password: password
            },
            dataType:'json',//服务器返回json格式数据
            type: 'post',//HTTP请求类型
            timeout: 10000,//超时时间设置为10秒；
            success: function (data) {
                console.log(data);
                layer.closeAll();
                if (data.code === 0) {
                    alert("登录成功");
                    layer.msg("成功");
                    window.location.href = "/goods/to_list";
                } else {
                    alert("登录出现异常");
                    layer.msg(data.msg);
                }
            },
            error: function (xhr, type, errorThrown) {
                alert("fail");
                alert(arguments[1]);
                layer.closeAll();
            }
        });
    });
})
