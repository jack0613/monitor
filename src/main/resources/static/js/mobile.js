$(function(){

    $("#searchFilter").click(function(){

        var phoneName = $("#mobileName").val();
        var phone = $("#mobileNumber").val();
        var params={phoneName:phoneName,phone:phone};
        var url="/mobile/mobileList";
        sendSearch(url,params);
    });

});

function pageSearch(cpg) {

    var phoneName = $("#mobileName").val();
    var phone = $("#mobileNumber").val();
    var params={phoneName:phoneName,phone:phone,page:cpg};
    var url="/mobile/mobileList";
    sendSearch(url,params);

}

function sendSearch(url ,params){

    $.ajax({
        url: url,
        data: params,//参数列表,
        type: "post",
        dataType:"json",
        success: function (result) {
            //alert(result);
            var content = result.content;
            //alert(content );
            $("#totals").html("共有数据："+result.totalElements+" 条")
            //移除tbody 上的节点
            $("#tbd").empty();
            var tbd = $("tbd");
            if(content.length>0){
                for(var i=0;i<content.length;i++){

                    var $row= $("<tr></tr>");
                  /*  $row.append(" <td>\n" +
                        "              <div class=\"layui-unselect layui-form-checkbox\" lay-skin=\"primary\" data-id='2'><i class=\"layui-icon\">&#xe605;</i></div>\n" +
                        "            </td>");*/
                    $row.append(" <td>"+(i+1)+"</td>");
                    $row.append(" <td>"+content[i].phone+"</td>");
                    $row.append(" <td>"+content[i].phoneName+"</td>");
                    var enstr ="";
                    if(content[i].enabled==0){
                        enstr="禁用";
                    }else{
                        enstr="启用";
                    }


                    $row.append("<td>"+enstr+"</td>");



                    var editUrl = 'mobile-edit.html?id='+content[i].id;
                    $row.append("<td class=\"td-manage\">\n" +
                        "              <a title=\"编辑\"  onclick=\"x_admin_show('编辑','"+editUrl+"',600,400)\" href=\"javascript:;\">\n" +
                        "                <i class=\"layui-icon\">&#xe642;</i>\n" +
                        "              </a>\n" +
                        "              <a title=\"删除\" onclick=\"deleteMobileInfo(this,'"+content[i].id+"')\" href=\"javascript:;\">\n" +
                        "                <i class=\"layui-icon\">&#xe640;</i>\n" +
                        "              </a>\n" +
                        "&nbsp;<button class=\"layui-btn\" onclick=\"dailNumer('"+content[i].phone+"',"+content[i].enabled+")\"><i class=\"layui-icon\"></i>拨号"+
                        "            </td>");
                    $row.appendTo($("#tbd"));


                }

            }
            handlePage(result);

        },
        error: function (result) {
            alert("加载数据出错啦....");
        }

    })


}
function dataLoad(){
    var url="/mobile/mobileList";
    var params={};
    sendSearch(url,params);

}

function dailNumer(phone,enabled) {
    if(enabled ==0){
        alert("该号码被禁用！");
        return;
    }
    var layer = layui.layer;
    var params ={phone:phone};
    var url="/mobile/dial";
    $.ajax({
        url: url,
        data: params,//参数列表,
        type: "post",
        dataType:"json",
        success: function (result) {
            if(result){
                layer.msg('拨号成功!', {icon: 1, time: 1000});
            }else{
                layer.msg('拨号失败!', {icon: 1, time: 1000});
            }

        },
        error:function () {

            layer.msg('拨号失败!', {icon: 1, time: 1000});
        }
    });


}
function handlePage(result){
    var pageTotal= result.totalPages;//总页数
    var currentPage = result.number+1;//当前页
    var isfirstPage=result.first;//是否第一页
    var islastPage =result.last;//是否最后一页
    var $div=$("<div></div>");
    $("#pageDiv").empty();
    if(pageTotal>0){
        if(isfirstPage){//
            $div.append("  <a class=\"prev\" href=\"javascript:void(0)\">&lt;&lt;</a>");
        }else{
            $div.append("  <a class=\"prev\" href=\"javascript:void(0)\" onclick='pageSearch("+(currentPage-1)+")'>&lt;&lt;</a>")
        }
        if(pageTotal>3){

            if(currentPage<=3){
                for(var i=1;i<=3;i++){
                    if(i==currentPage){
                        $div.append(" <a class=\"num\" class=\"current\" href=\"javascript:void(0)\">"+i+"</a>");
                    }else{
                        $div.append(" <a class=\"num\" href=\"javascript:void(0)\" onclick='pageSearch("+i+")'>"+i+"</a>");
                    }
                }



            }else{
                if(islastPage){
                    $div.append(" <a class=\"num\" href=\"javascript:void(0)\" onclick=\"pageSearch("+(currentPage-2)+")\">"+(currentPage-2)+"</a>");
                    $div.append(" <a class=\"num\" href=\"javascript:void(0)\" onclick=\"pageSearch("+(currentPage-1)+")\">"+(currentPage-1)+"</a>");

                    $div.append(" <a class=\"num\" class=\"current\" href=\"javascript:void(0)\" >"+(currentPage)+"</a>") ;

                }else{
                    $div.append(" <a class=\"num\" href=\"javascript:void(0)\" onclick=\"pageSearch("+(currentPage-1)+")\">"+(currentPage-1)+"</a>");
                    $div.append(" <a class=\"num\" class=\"current\" href=\"javascript:void(0)\" >"+currentPage+"</a>") ;
                    $div.append(" <a class=\"num\" href=\"javascript:void(0)\" onclick=\"pageSearch("+(currentPage+1)+")\">"+(currentPage+1)+"</a>");

                }
            }



        }else{
            for(var i=1;i<=pageTotal;i++){
                if(i==currentPage){
                    $div.append(" <a class=\"num\" class=\"current\" href=\"javascript:void(0)\" >"+i+"</a>");
                }else{
                    $div.append(" <a class=\"num\" href=\"javascript:void(0)\" onclick='pageSearch("+i+")'>"+i+"</a>");
                }

            }

        }
        if(islastPage){//
            $div.append("  <a class=\"next\" href=\"javascript:void(0)\">&gt;&gt;</a>");
        }else{
            $div.append("  <a class=\"next\" href=\"javascript:void(0)\" onclick='pageSearch("+(currentPage+1)+")'>&gt;&gt;</a>")
        }
        $div.appendTo("#pageDiv");
    }


}


function addMobileInfo(){
    var layer = layui.layer;

    var phoneName = $("#mobileName").val();
    var phone = $("#mobileNumber").val();
    var enabled= $("#enabled").val();
    if(phoneName ==null || ''==phoneName || phone ==null || ''==phone){
        alert("请填写电话信息");
        return;
    }
    if(!validateMobile(phone)){
        alert("请输入正确的电话号码或手机号！");
        return;
    }

    var params = {phoneName:phoneName,phone:phone,enabled:enabled};

    var url="/mobile/save";
    $.ajax({
        url: url,
        data: params,//参数列表,
        type: "post",
        dataType:"json",
        success: function (result) {
            if(result !=null){
                layer.alert("增加成功", {icon: 6},function () {
                    var index = parent.layer.getFrameIndex(window.name);
                    parent.location.reload();
                    //关闭当前frame
                    parent.layer.close(index);
                });

            }
        }
    });


}

function mobileInfoUpdate(){
    var phoneName = $("#mobileName").val();
    var phone = $("#mobileNumber").val();
    var enabled= $("#enabled").val();
    var id = $("#id").val();
    if(!validateMobile(phone)){
        alert("请输入正确的电话号码或手机号！");
        return;
    }
    var params ={id:id,phoneName:phoneName,phone:phone,enabled:enabled};
    var url ="/mobile/update";
    $.ajax({
        url: url,
        data: params,//参数列表,
        type: "post",
        dataType:"json",
        success: function (result) {
            if(result !=null){
                layer.alert("修改成功", {icon: 6},function () {
                    var index = parent.layer.getFrameIndex(window.name);
                    parent.location.reload();
                    //关闭当前frame
                    parent.layer.close(index);
                });
            }

        }
    })


}


function deleteMobileInfo(obj,id){
    var layer= layui.layer;
    layer.confirm('确认要删除吗？',function(index){

        var url="/mobile/delete/"+id;
        $.ajax({
            url: url,
            type: "post",
            dataType:"json",
            success: function (result) {
              if(result){
                  location.reload();
                  layer.msg('已删除!', {icon: 1, time: 1000});
              }

            }
        })

    });
}
function validateMobile(mobile) {
    //var reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/
   var regMobile = /^1\d{10}$/;
   var regTel = /^0\d{2,3}-?\d{7,8}$/;


    return (regMobile.test(mobile)||regTel.test(mobile));

}

