
var CommentList={
		init:function(){
			var self  = this;		
		//	self.bindAllPageEvent();
		 	self.bindTabEvent();
		},
		
		/**
		 * 评论分页点击事件绑定
		 */
		bindDiscussPageEvent:function(){
			var self = this;
			$("#discuss_list_wrapper .page a.unselected").click(function(){
				var page = $(this).attr("page");
				self.loadDiscussList(page);
				return false;
			});
		},
		
		/**
		 * 评论分页点击事件绑定
		 */
		bindAskPageEvent:function(){
			var self = this;
			$("#ask_list_wrapper .page a.unselected").click(function(){
				var page = $(this).attr("page");
				self.loadAskList(page);
				return false;
			});
		},
		
		bindTabEvent:function(){
			//选 项卡切换
			$("#comment_tab li.discuss").click(function(){
				$("#discuss_wrapper").show();
				$("#ask_wrapper").hide();
				$("#comment_tab li").removeClass("selected");
				$(this).addClass("selected");
			});

			$("#comment_tab li.ask").click(function(){
				$("#discuss_wrapper").hide();
				$("#ask_wrapper").show();
				$("#comment_tab li").removeClass("selected");
				$(this).addClass("selected");
			});
		}
	};
	

/**
 * 评论表单提交
 */
var dialog;
var CommentForm={
	init:function(){		
		var self  = this;
		Grade.init();
	},
	
	formSubmit:function(btn,form){
		var str1 = form.serialize();
		/*var a = str1.indexOf("commenttype");
		var b = str1.substring(a+12,a+13);*/
		var reg = new RegExp("commenttype=(.*?)&");
		var r = str1.match(reg);
		var comtype = r[1];
		var options = { 
				url : ctx+"/api/b2b2c/storeCommentApi!add.do?ajax=yes",
				type : "POST",
				cache:false,
				dataType : "json",
				success : function(result) {
	 				if(result.result==1){
	 					
	 					if(language=="zh"){
	 						if(parseInt(comtype)==2){
	 							alert("提交成功!");
	 						}else{
	 							alert("提交成功，请等待管理员审核!");
	 						}
	 					}else{
	 						if(parseInt(comtype)==2){
	 							alert("Представили успешно，подождите рассмотрение администратора!");
	 						}else{
	 							alert("Представили успешно，подождите рассмотрение администратора， пожалуйста!");
	 						}
	 						 
	 					}
						
						 form.get(0).reset();
						 window.location.reload();
			 		}else{
			 			alert(result.message);
				 	}
				},
				error : function(e) {
					if(language=="zh"){
						alert("出现错误 ，请重试");
 					}else{
 						alert("Произошла ошибка,  повторите попробовать пожалуйста");
 					}
					
					btn.attr("disabled",false); 
				}
		};
		form.ajaxSubmit(options);
	},
};


/**
 * 评价
 */
var Grade={
		clicked:false,	
		init:function(){
			var self = this;
			$("#discuss_form_wrapper .grade a").hover(
				function(){
					self.hover($(this));
				}
			);

			$("#discuss_form_wrapper .grade a").click(function(){
				 self.clicked=true;
				 var index = parseInt($(this).text());
				 self.selectced(index,$(this).attr("status"));
			});
			
		},
		hover:function(curritem){
			var status=$(curritem).attr("status");
			$("#discuss_form_wrapper .grade a[status="+status+"]").slice(index-1,5).removeClass("selected");
			this.clicked= false;
			var index = parseInt(curritem.text());
			this.selectced(index,status);
			 
		},
		selectced:function(index,status){
			$("#discuss_form_wrapper .grade a[status="+status+"]").slice(0,index).addClass("selected");
			$("span[name='gradevalue'][status="+status+"]").html(index);
			$("#"+status).val(index);
		}
	};