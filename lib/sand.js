
function getAllInfo()
{
    var $keyword = $("#id_config_items_keyword").val();
    var $cat = $("#id_config_items_search_cata").find("option:selected").val();
    var $inshow =  $("#id_config_items_inshow").find("option:selected").val();
    var $sort =  $("#id_config_items_sort").find("option:selected").val();
    var $pitem =  $("#id_config_items_page_item_no").find("option:selected").val();
    var $jmpto =  $("#id_config_items_jmp_to").val();
    var $curpage =  $("#id_config_items_curpage").val();
    var $status = $("#id_config_status").text();
    var $idlist ="id=";
    $("#id_items_list :checkbox").each(function(){
        if ($(this).attr("checked") == "checked")
        {
              $idlist = $idlist +  $(this).attr("id") +":";
        }
    });
    
    console.log ("cur_page:" + $(id_config_items_curpage).attr("value"));
    console.log("keyword:"+$keyword);
    console.log("cat:"+$cat);
    console.log("inshow:"+$inshow);
    console.log("pitem:"+$pitem);
    console.log("jmpto:"+$jmpto);
    console.log("idlist:"+$idlist);
    console.log("curpage:"+$curpage);
    console.log("status:"+$status);
    console.log("&" + $idlist + "&keyword=" + $keyword + "&cat=" + $cat + "&inshow=" + $inshow + "&sort="+ $sort + "&pitem="+ $pitem + "&jmpto="+ $jmpto);
    
    return encodeURI($idlist + "&keyword=" + $keyword + "&cat=" + $cat + "&inshow=" + $inshow + "&sort="+ 
                     $sort + "&pitem="+ $pitem + "&jmpto="+ $jmpto + "&curpage="+$curpage + "&status=" + $status);
}

function getAllItemSelect()
{
    var $list = new Array();
    $("#id_items_list :checkbox").each(function(){
        if ($(this).attr("checked") == "checked")
        {
                $list.push($(this).attr("id"));
        }
    });
    return $list;    
}

$(document).ready(function(){

    $("#id_config_items_content").hide();
    
    
    
    
    $("#id_config_items_page_nav_bat li").livequery("click", function(){
        var $list = getAllItemSelect();
        var $str = "";
        var $to = $(this).attr("href");
		var _self = this;
		
		if ($(this).attr("id") == "id_config_items_show_info")return;
		
        console.log ($list);
        
         
        for ($i = 0; $i < $list.length; $i=$i + 1)
            $str = $list[$i] + ":" + $str;
        
        $to = $to  + "&ct=2&"  + "&id=" + $str;
        console.log ($to);
        $("#runing").dialog('open');
        
       // $("#id_dummy").load($to,
        $.get($to,
            function(data, textStatus){
                console.log("-data-" + data);
                console.log("-textStatus-" + textStatus);
                if (textStatus == "success")
                {
                    console.log("-textStatus-" + "success");
                    if (data == "success_force" || data == "success_forbid" || data == "success_normal")
                    {
                        var $list = getAllItemSelect();
                        for ($i = 0; $i < $list.length; $i = $i + 1)
                        {
                            var $item = $("#" + $list[$i] + "_status");
                            var $itemCheck = $("#" + $list[$i]);
                            var $itemForce = $("#" + $list[$i] + "_force");
                            var $itemForbid = $("#" + $list[$i] + "_forbid");
                            var $itemNormal = $("#" + $list[$i] + "_normal");

                            if (data == "success_force")
                            {
                                $item.text("已被始终推荐");
                                $itemForce.attr("style","margin:20px 0 0 0;padding-left:2px;display:none;");
                                $itemForbid.attr("style","margin:20px 0 0 0;padding-left:2px;");
                                $itemNormal.attr("style","margin:20px 0 0 0;padding-left:2px;");
                            }
                            else if (data == "success_forbid")
                            {
                                $item.text("已被禁止推荐");
                                $itemForce.attr("style","margin:20px 0 0 0;padding-left:2px;");
                                $itemForbid.attr("style","margin:20px 0 0 0;padding-left:2px;display:none;");
                                $itemNormal.attr("style","margin:20px 0 0 0;padding-left:2px;");
                            }
                            else
                            {
                                $item.text("普通方式");
                                $itemForce.attr("style","margin:20px 0 0 0;padding-left:2px;");
                                $itemForbid.attr("style","margin:20px 0 0 0;padding-left:2px;");
                                $itemNormal.attr("style","margin:20px 0 0 0;padding-left:2px;display:none;");
                            }
                            $itemCheck.attr("checked", false);
                        }  
                    }
                    $("#items_select_all").attr("checked", false);                     
                }                
                $("#runing").dialog('close');    
            })
    })
    
    $("#id_config_items_page_nav > li").livequery("click",function(){
		var $to = $(this).attr("href");
		var _self = this;
		$to = $to + "&ct=0&" + getAllInfo();
		$("#runing").dialog('open');
		console.log ("to:" + $to);
				
		$("#id_items_list").load($to,function(){
		    var $cur = $("#id_config_items_curpage").val();
		    var $total = $("#id_config_items_totalpage").val();
		    $("#id_config_items_jmp_to").attr("value", $cur);
		    $("#id_config_items_page_nav_total").text("共"+$total+"页");
		    console.log ("page" + $cur + "/" + $total);
		    $("#runing").dialog('close');
		    $(".config_items_item:odd").addClass("config_items_item_odd");
		});
    })

    $("#id_config_search_button").livequery("click",function(){
		var $to = $(this).attr("href");
		var _self = this;
		$to = $to + "&ct=0&" + getAllInfo();
		$("#runing").dialog('open');
		//console.log ("to:" + $to);
				
		$("#id_items_list").load($to,function(){
		    var $cur = $("#id_config_items_curpage").val();
		    var $total = $("#id_config_items_totalpage").val();
		    $("#id_config_items_jmp_to").attr("value", $cur);
		    $("#id_config_items_page_nav_total").text("共"+$total+"页");
		    console.log ("page" + $cur + "/" + $total);
		    $("#runing").dialog('close');
		    $(".config_items_item:odd").addClass("config_items_item_odd");
		});
    })
    
    
	$("#select_show_forever").livequery("click",function(){
		var $to = $(this).attr("href");
		var _self = this;
		$to = $to + "&ct=0"
		$("#runing").dialog('open');
		
		$("#id_items_list").load($to,function(){
		    $("#id_config_items_content").dialog({
			    height: 600,
		        width: 900,
		        draggable:true,
		        resizable:true,
                modal: true,
                title: "选择始终推荐的商品(细化的搜索条件，有利于提高工作效率)"      
		        });
		    var $cur = $("#id_config_items_curpage").val();
		    var $total = $("#id_config_items_totalpage").val();
		    $("#id_config_items_jmp_to").attr("value", $cur);
		    $("#id_config_items_page_nav_total").text("共"+$total+"页");
		    $("#id_config_status").text("forever");
		    
		    $("#runing").dialog('close');
		    $(".config_items_item:odd").addClass("config_items_item_odd");
		});
	})

	$("#id_cats_adjuest").livequery("click",function(){
		var $to = $(this).attr("href");
		var _self = this;
		$to =  $to + "&ct=2"
		$("#runing").dialog('open');
		console.log ($to);
		$("#id_cats_window_content").load($to,function(){
		    
		    $("#id_cats_window").dialog({
			    height: 500,
		        width: 800,
		        draggable:true,
		        resizable:true,
                modal: true,
                title: "选择要调整的类目"      
		        });
		    
		    
		    $("#id_cats_window_content > ul").attr("id", "id_cats_window_tree");
		    		    
    		$("#id_cats_window_tree").treeview({
				collapsed: true,
				animated: "fast",
				control:"#id_cats_nav",
				
			});

		    $("#runing").dialog('close');
		});
	})

	$("#id_cats_window input").livequery("change",function(){
		var $id = $(this).attr("id");
		console.log("click id:" + $id + "--:" + "#" + $id + " input");
		if ($(this).attr("checked") == "checked")
			$("#config_cats_cat_" + $id + " input").attr("checked", true);
		else
		{
			$("#config_cats_cat_" + $id + " input").attr("checked", false);
			$pid = $(this).parent().parent().parents().attr("id");
			$("#" + $pid + " > input").attr("checked", false);
		}	
	});
	
	$("#id_cats_nav_select_all").livequery("click",function(){
		$("#id_cats_window :checkbox").attr("checked",true);
	})
	
	$("#id_cats_nav_select_none").livequery("click",function(){
		$("#id_cats_window :checkbox").attr("checked",false);
	})

	$("#id_cats_nav_select_reverse").livequery("click",function(){
		$("#id_cats_window :checkbox").each(function(){
	       if ($(this).attr("checked") == "checked")
	           $(this).attr("checked",false);
	       else
	           $(this).attr("checked",true); 				
		});
	})
	
	$("#id_cats_nav_sure").livequery("click",function(){
		var $val = "";
		var $to = $(this).attr("href");
		var _self = this;
		var $status =false;
		$to = $to + "&ct=2"
		console.log($to);
		
		$("#runing").dialog('open');

		$("#id_cats_window :checkbox").each(function(){
			if ($(this).attr("checked") == "checked")
				$val = $val + $(this).attr("id") + ":"
		})
		$to = $to + "&id="+$val;
		$("#runing").dialog('open'); 

	        $.get($to, function(data, textStatus){
	            console.log("data:" + data + "   textStatus:" + textStatus);
	                if (textStatus == "success")
	                {
	                    if (data == "catsShow")
	                    {
						    $("#runing").dialog('close');
                            $status = true;
	                    }                     
	                }
	                if ($status == false)
                        $("#id_cats_select_sure_message").text("网络通讯出错了!按关闭返回");
	                else
                        $("#id_cats_select_sure_message").text("设置成功了!按关闭返回");
                    
                    $( "#id_cats_select_sure_message" ).dialog({
                    	modal: true,
                    	title: "设置状态",
                        draggable:false,
                        resizable:false,
                    	buttons: {
                    		Ok: function() {
                    			$( this ).dialog( "close" );
                    		}
                    	}
                    });
	            })
	    
	});
	
	$("#select_show_never").livequery("click",function(){
		var $to = $(this).attr("title");
		var _self = this;
		$to = $to + "&ct=0"
		$("#id_items_list").load($to,function(){$("#runing").removeClass("cover_active");});
		$( "#id_config_items_content" ).dialog({
		    height: 500,
		    width: 900,
            modal: true,
            title: "Dialog Title" ,
            zIndex:1000,
	    });
	})

    $("#items_select_all").livequery("change",function(){
       if ($("#items_select_all").attr("checked") == "checked")
           $(".config_items_checkbox input").attr("checked",true);
       else
           $(".config_items_checkbox input").attr("checked",false);  
    });
    
	$("#showcase_config_adjust_mothod_select").livequery("click",function(){
		var $to = $(this).attr("href");
		var _self = this;
		$to = $to + "&ct=2" + "&mode=" + $("#config_set_left :checked").attr("value");
		var $status=false;
		console.log ($to);
        $.get($to, function(data, textStatus){
	            console.log("data:" + data + "   textStatus:" + textStatus);
	                if (textStatus == "success")
	                {
	                    if (data == "success")
	                    {
						    $("#runing").dialog('close');
                            $status = true;
	                    }                     
	                }
	                if ($status == false)
                        $("#id_cats_select_sure_message").text("网络通讯出错了!按关闭返回");
	                else
                        $("#id_cats_select_sure_message").text("设置成功了!按关闭返回");
                    
                    $( "#id_cats_select_sure_message" ).dialog({
                    	modal: true,
                    	title: "设置状态",
                        draggable:false,
                        resizable:false,
                    	buttons: {
                    		Ok: function() {
                    		    $( this ).dialog( "close" );
                    		},
                    	},
                        close: function(event, ui) { $("#id_menu_showcase").trigger("click"); }
                    });
	            })
	})
		
    $("#config_set_left li").livequery("mouseover",function(){
        var $id = $(this).attr ("id");
        if ($id != "showcase_config_all" &&
            $id != "showcase_config_cats" &&
            $id != "showcase_config_set")
        {
            return;    
        }
        
        var $intd = $id+ "_intd";
        
        $("#config_set_right p").attr("style", "display:none;");
        $("#" + $intd).attr("style", "display:block;");
        
    })
});
