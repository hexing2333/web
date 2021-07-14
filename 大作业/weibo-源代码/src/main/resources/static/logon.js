function logon(){
    var email=$("#email").val()
    var email_re=/^([0-9A-Za-z\-_\.]+)@([0-9a-z]+\.[a-z]{2,3}(\.[a-z]{2})?)$/g;
    if(!email_re.test(email)){
        alert("电子邮件格式不正确");
        return false;
    }
    var username=$("#username").val()
    if(username.length>10){
        alert("用户名长度不允许超过10位");
        return false;
    }
    alert("注册成功")
    return true;
}