$(function(){

    $("#searchFilter").click(function(){

        var content = $("#content").val();
        var start = $("#start").val();
        var end = $("#end").val()
        var type = $("#logtype").val();
        var params={content:content,start:start,end:end,type:type};
        var url="/log/logList";
        sendSearch(url,params);
    });

});

function pageSearch(cpg) {

    var content = $("#content").val();
    var start = $("#start").val();
    var end = $("#end").val()
    var type = $("#logtype").val();
   var params={content:content,start:start,end:end,page:cpg,type:type};

    var url="/log/logList";
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
                    $row.append(" <td>"+content[i]['content']+"</td>");
                    var t ="";
                    if(content[i].type ==1){
                        t="正常日志";
                    }else{
                        t ="异常日志";
                    }
                    $row.append(" <td>"+t+"</td>");


                    $row.append("<td>"+content[i].createtime+"</td>");



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
    var url="/log/logList";
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


