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
                    $row.append(" <td>\n" +
                        "              <div class=\"layui-unselect layui-form-checkbox\" lay-skin=\"primary\" data-id='2'><i class=\"layui-icon\">&#xe605;</i></div>\n" +
                        "            </td>");
                    $row.append(" <td>"+content[i].id+"</td>");
                    $row.append(" <td>"+content[i].ipName+"</td>");
                    $row.append(" <td>"+content[i].ipAddr+"</td>");
                    $row.append("<td>"+content[i].enabled+"</td>");
                    $row.append("<td>正常</td>");
                    $row.append("<td>2018-09232</td>");

                    $row.append("<td class=\"td-manage\">\n" +
                        "              <a onclick=\"member_stop(this,'10001')\" href=\"javascript:;\"  title=\"启用\">\n" +
                        "                <i class=\"layui-icon\">&#xe601;</i>\n" +
                        "              </a>\n" +
                        "              <a title=\"编辑\"  onclick=\"x_admin_show('编辑','member-edit.html',600,400)\" href=\"javascript:;\">\n" +
                        "                <i class=\"layui-icon\">&#xe642;</i>\n" +
                        "              </a>\n" +
                        "              <a onclick=\"x_admin_show('修改密码','member-password.html',600,400)\" title=\"修改密码\" href=\"javascript:;\">\n" +
                        "                <i class=\"layui-icon\">&#xe631;</i>\n" +
                        "              </a>\n" +
                        "              <a title=\"删除\" onclick=\"member_del(this,'要删除的id')\" href=\"javascript:;\">\n" +
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
                for(var i=0;i<=3;i++){
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