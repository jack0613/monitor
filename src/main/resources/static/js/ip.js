$(function(){

    $("#searchFilter").click(function(){

        var ipAddr = $("#ipAddr").val();
        var ipName = $("#ipName").val();
        var params={ipAddr:ipAddr,ipName:ipName};
        var url="/ip/iplist";
        sendSearch(url,params);
    });

});

function pageSearch(cpg) {

    var ipAddr = $("#ipAddr").val();
    var ipName = $("#ipName").val();
    var params={ipAddr:ipAddr,ipName:ipName,page:cpg};
    var url="/ip/iplist";
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
                    $row.append(" <td>"+content[i].ipName+"</td>");
                    $row.append(" <td>"+content[i].ipAddr+"</td>");
                    var enstr ="";
                    if(content[i].enabled==1){
                        enstr="启用";
                    }else{
                        enstr="禁用";
                    }
                    var st ="";
                    if( content[i].status==0){
                        st="不正常";
                    }else{
                        st="正常";
                    }

                    $row.append("<td>"+enstr+"</td>");
                    $row.append("<td>"+st+"</td>");
                    $row.append("<td>"+(content[i].lastOnlinetime==null?'':content[i].lastOnlinetime)+"</td>");

                    var editUrl = 'ip-edit.html?id='+content[i].id;
                    $row.append("<td class=\"td-manage\">\n" +
                        "              <a title=\"编辑\"  onclick=\"x_admin_show('编辑','"+editUrl+"',600,400)\" href=\"javascript:;\">\n" +
                        "                <i class=\"layui-icon\">&#xe642;</i>\n" +
                        "              </a>\n" +
                        "              <a title=\"删除\" onclick=\"deleteIpInfo(this,'"+content[i].id+"')\" href=\"javascript:;\">\n" +
                        "                <i class=\"layui-icon\">&#xe640;</i>\n" +
                        "              </a>\n" +
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
    var url="/ip/iplist";
    var params={};
    sendSearch(url,params);

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
                $div.append(" <a class=\"num\" href=\"javascript:void(0)\" onclick=\"pageSearch("+(currentPage-1)+")\">currentPage-1</a>");
                $div.append(" <a class=\"num\" class=\"current\" href=\"javascript:void(0)\" >currentPage</a>") ;
                $div.append(" <a class=\"num\" href=\"javascript:void(0)\" onclick=\"pageSearch("+(currentPage-1)+")\">currentPage+1</a>");
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


function addIpInfo(){
    var layer = layui.layer;
    var ipAddr = $("#ipAddr").val();
    var ipName = $("#ipName").val();
    var enabled= $("#enabled").val();
    if(ipAddr ==null || ''==ipAddr || ipName ==null || ''==ipName){
        alert("请填写IP信息");
        return;
    }
    if(!validateIp(ipAddr)){
        alert("请填写正确的IP地址");
        return;

    }
   // var b= validateIp(ipAddr);
   //  if(!b){
   //      alert("请填写正确的IP信息");
   //      return ;
   //  }

    var params = {ipAddr:ipAddr,ipName:ipName,enabled:enabled};

    var url="/ip/save";
    $.ajax({
        url: url,
        data: params,//参数列表,
        type: "post",
        dataType:"json",
        success: function (result) {
            if(result !=null){
                layer.alert("增加成功", {icon: 6},function () {
                    //重新加载列表
                    window.parent.location.reload();
                    var index = parent.layer.getFrameIndex(window.name);
                    //关闭当前frame
                    parent.layer.close(index);
                });

            }
        }
    });


}

function ipInfoUpdate(){
    var ipAddr= $("#ipAddr").val();
    var ipName=$("#ipName").val();
    var enabled = $("#enabled").val();
    var id = $("#id").val();
    if(!validateIp(ipAddr)){
        alert("请填写正确的IP地址");
        return;

    }
    var params ={id:id,ipAddr:ipAddr,ipName:ipName,enabled:enabled};
    var url ="/ip/update";
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


function deleteIpInfo(obj,id){
    var layer= layui.layer;
    layer.confirm('确认要删除吗？',function(index){

        var url="/ip/delete/"+id;
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
function validateIp(ip) {
    var exp=/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
    var reg = ip.match(exp);
    if(reg==null){
       return false;
    }else {
        return true;
    }

}

