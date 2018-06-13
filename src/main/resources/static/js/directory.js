$(function(){

    $("#searchFilter").click(function(){

        var pathName = $("#pathName").val();
        var fileName = $("#fileName").val();
        var params={filePath:pathName,fileName:fileName};
        var url="/dir/dirlist";
        sendSearch(url,params);
    });

});

function pageSearch(cpg) {

    var pathName = $("#pathName").val();
    var fileName = $("#fileName").val();
    var params={filePath:pathName,fileName:fileName,page:cpg};
    var url="/dir/dirlist";
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
                    $row.append(" <td>\n" +
                        "              <div class=\"layui-unselect layui-form-checkbox\" lay-skin=\"primary\" data-id='2'><i class=\"layui-icon\">&#xe605;</i></div>\n" +
                        "            </td>");
                    $row.append(" <td>"+ (i+1) +"</td>");
                    $row.append(" <td>"+content[i].filePath+"</td>");
                    $row.append(" <td>"+content[i].fileName+"</td>");
                    $row.append(" <td>"+content[i].monitorTime+"</td>");
                    var enstr ="";
                    if(content[i].enabled==1){
                        enstr="启用";
                    }else{
                        enstr="禁用";
                    }
                    var st ="";
                    if( content[i].status==0){
                        st="不正常";
                    }else if( content[i].status==1){
                        st="正常";
                    }

                    $row.append("<td>"+enstr+"</td>");
                    $row.append("<td>"+st+"</td>");
                    $row.append("<td>"+(content[i].lastOnlinetime==null?'':content[i].lastOnlinetime)+"</td>");

                    var editUrl = 'directory-edit.html?id='+content[i].id;
                    $row.append("<td class=\"td-manage\">\n" +
                        "              <a title=\"编辑\"  onclick=\"x_admin_show('编辑','"+editUrl+"',600,400)\" href=\"javascript:;\">\n" +
                        "                <i class=\"layui-icon\">&#xe642;</i>\n" +
                        "              </a>\n" +
                        "              <a title=\"删除\" onclick=\"deleteDirInfo('"+content[i].id+"')\" href=\"javascript:;\">\n" +
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
    var url="/dir/dirlist";
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


function addDirInfo(){
    var layer = layui.layer;
    var pathName = $("#pathName").val();
    var fileName = $("#fileName").val();
    var monitorTime= $("#monitorTime").val();
    var enabled= $("#enabled").val();
    if(pathName == null || '' == pathName || fileName == null || '' == fileName){
        alert("请填写目录信息");
        return;
    }
    if(monitorTime == null || monitorTime == ""){
        alert("请填写时间间隔信息");
        return;
    }
   // var b= validateIp(ipAddr);
   //  if(!b){
   //      alert("请填写正确的IP信息");
   //      return ;
   //  }

    var params = {filePath:pathName,fileName:fileName,monitorTime:monitorTime,enabled:enabled};

    var url="/dir/save";
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

function dirInfoUpdate(){
    var pathName= $("#pathName").val();
    var fileName=$("#fileName").val();
    var enabled = $("#enabled").val();
    var monitorTime = $("#monitorTime").val();
    var id = $("#id").val();
    var params ={filePath:pathName,fileName:fileName,monitorTime:monitorTime,enabled:enabled,id:id};
    var url ="/dir/update";
    $.ajax({
        url: url,
        data: params,//参数列表,
        type: "post",
        dataType:"json",
        success: function (result) {
            if(result !=null){
                //重新加载列表
                window.parent.location.reload();
                layer.alert("修改成功", {icon: 6},function () {
                    var index = parent.layer.getFrameIndex(window.name);
                    //关闭当前frame
                    parent.layer.close(index);
                });
            }

        }
    })


}


function deleteDirInfo(id){
    var layer= layui.layer;
    layer.confirm('确认要删除吗？',function(index){
        var url="/dir/delete/"+id;
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

function pathNameRule(){
    //layer.alert(", {icon: 0},function () {});

    layer.open({
        type: 1,
        skin: 'layui-layer-rim', //加上边框
        area: ['420px', '240px'], //宽高
        content: '<html><body><h2>ftp路径规则：ftp://127.0.01{^}21{^}test{^}test{^}/directory</br>共享文件规则：\\\\127.0.0.1\\directory</br>本地磁盘：d:\\\\directory\\</h2></body></html>'
    });

}

function fileNameRule(){
    //layer.alert(", {icon: 0},function () {});

    layer.open({
        type: 1,
        skin: 'layui-layer-rim', //加上边框
        area: ['420px', '240px'], //宽高
        content: '<html><body><h2>文件名规则：【#yyyy#】年【#MM#】年【#dd#】短信.txt 根据系统运行时间将被转换为 2018年05月01日短信.txt' +
        '</br>可用标签：【#yyyy#】 2018(年份) 【#MM#】 05 (双位月份) 【#M#】 5 (单位月份) 【#dd#】 01(双位日) 【#d#】 1(单位日) 【#HH#】 01(双位小时) 【#H#】 1(单位小时)' +
        ' 【#mm#】 01(双位分钟) 【#m#】 1(单位分钟) ' +
        '</h2></body></html>'
    });

}

function timeRule(){
    layer.open({
        type: 1,
        skin: 'layui-layer-rim', //加上边框
        area: ['420px', '240px'], //宽高
        content: '<html><body><h2>时间规则：秒 分钟 小时 日期 月份 星期 年（可选）' +
        '</br>例如：</br>"0 0 12 * * ?" 每天中午12点触发</br>"0 0/5 14 * * ?" 在每天下午2点到下午2:55期间的每5分钟触发</br>"0 0/5 14,18 * * ?" 在每天下午2点到2:55期间和下午6点到6:55期间的每5分钟触发</br>' +
        '"0 * 14 * * ?" 在每天下午2点到下午2:59期间的每1分钟触发 ' +
        '</h2></body></html>'
    });

}


