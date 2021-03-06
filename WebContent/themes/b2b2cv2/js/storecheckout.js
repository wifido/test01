var Address=["shipAddr","shipZip","shipName","shipTel","shipMobile"];

var CheckOut={
		
	init:function(){
		var self = this;
		var pointprice = $("#pointpriceId").val();	
		/**
		 * ====================================================
		 * 声明收货地址、付款方式、配送方式的Wrapper
		 * ====================================================
		 */
		//收货地址
		this.addrSelectedWrap =  $("#checkout_wrapper .address .selected"); //收货地址-选中状态的Wrapper
		this.addrModifyWrap   =  $("#checkout_wrapper .address .modify");   //收货地址-修改状态的Wrapper
		
		//付款方式
		this.paySelectedWrap  =  $("#checkout_wrapper .payment .selected"); //付款方式-选中状态的Wrapper
		this.payModifyWrap    =  $("#checkout_wrapper .payment .modify");   //付款方式-修改状态的Wrapper

		
		this.dlytypeSelectedWrap  =  $("#checkout_wrapper .dlytype .selected"); //配送方式-选中状态的Wrapper
		this.dlytypeModifyWrap    =  $("#checkout_wrapper .dlytype .modify");   //配送方式-修改状态的Wrapper
		
		
		self.bindAddrEvent();
		self.bindPaymentEvent();
		self.bindDlytypeEvent();
		
		//地区联动选择-选择最后一级（区）时自动设置邮编
		/*RegionsSelect.regionChange=function(regionid,name,zipcode){
			self.addrModifyWrap.find(".input [name='shipZip']").val(zipcode);
		};*/
		
		/**
		 * 优惠劵显示的切换
		 */
		$(".pucker input[type=checkbox]").click(function(){
			var parent = $(this).parent();
			var bonus_select=$("#bonus_select").val();
			if(this.checked){
				if(parent.parent().hasClass("coupon")){
					if(bonus_select==undefined){
						$("#coupondiv").load("checkout/bonus.html",function(){
							self.whenOpen();
						});
					}else{
						if(bonus_select==0){
							alert("您购买的商品中含有秒杀产品不允许使用优惠券!");
							return false;
						}else{
							$("#coupondiv").load("checkout/bonus.html",function(){
								self.whenOpen();
							});
						}
					}
				}
				parent.siblings(".content").show();
			}else{
				parent.siblings(".content").hide();
				self.changeBonus(0);
			}
			
		});
		
		/**
		 * 显示或隐藏发票抬头
		 */
		$("[name=receiptType]").click(function(){
			var value = $(this).val();
			if(value=="1"){
				$("#receiptTitle").hide();
			}else{
				$("#receiptTitle").show();
			}
		});
		
		
		
		/**
		 * 新增判断有没有收货地址
		 */
		
		$("#createBtn").click(function(){
			
   var modifyWrapper = self.addrModifyWrap.find(".def_addr:checked");
			
			if(modifyWrapper.html()==null){
				alert("请选择收货信息");
				return false;
			}
			self.createOrder($(this));
		});
		
		
	},
	changeBonus:function(bonusid){
		var regionid = $("#region_id").val();
		var typeid = $('input:radio[name="typeId"]:checked').val();
		if(regionid==null){
			regionid=0;
		}
		if(typeid==null){
			typeid=0;
		}
		if($("#bonusid").val()!=0){
			$("#ECS_BONUS option[value='0']").attr("selected", true);
			alert("只能只用一种优惠券");
			return;
		}
		$.ajax({
			url:"api/shop/bonus!useOne.do?bonusid="+bonusid+"&regionid="+regionid+"&typeid="+typeid,
			dataType:"json",
			success:function(res){
				if(res.result==1){
					CheckOut.refreshTotal();
				}else{
					alert(res.message);
				}
			},
			error:function(){
				alert("糟糕，使用优惠卷发生意外错误");
			}
		});
	},
	
	//绑定打开窗口事件
	 whenOpen:function(){
		 var self  = this;
		 this.bindUseEvent();
		 //关闭按钮事件
		 $(".cartPop .c").click(function(){
				$(".cartPoint").empty();
				
				Cart.refreshTotal(); 					
		});
		 //添加一个新输入框
		 $(".bonusADD").click(function(){
			 
			 if($(".bonusSPAN>div").size()>=5){
				 alert("最多使用5个红包");
				 return false;
			 }
			 
			 var newInput = $(".bonusSPAN>div:first").clone();
			 newInput.removeAttr("id").find("input").removeAttr("disabled").val("");
			 newInput.find(".userY").show();
			 $(".bonusSPAN").append(newInput);
			 self.bindUseEvent();
		 });
	 }
	,
	bindUseEvent:function(){
		var self = this;
		$(".bonusSPAN .userY").unbind("click").bind("click",function(){
			var ipt = $(this).prev("input");
			var sn  = ipt.val();
			if(sn==""){
				alert( "请输入优惠卷号码" );
				return ;
			}
			var count =0; 
			$(".bonusSPAN input").each(function(i,v){
				if(v.value== sn){
					count++;
				}
			});
			
			if(count>1 ){
				alert("输入的号码重复");
				ipt.select();
				return ;
			}
			
			self.useBonus(sn,ipt);
		});
		
		$(".bonusSPAN .userQ").unbind("click").bind("click",function(){
			var ipt =  $(this).siblings("input");
			var sn  = ipt.val();
			var box = $(this).parent();
			self.cancelBonus(sn,box);
		});
	},
	//实体券
	useBonus:function(sn,ipt){
		
		var regionid = $("#region_id").val();
		var typeid = $('input:radio[name="typeId"]:checked').val();
		if(regionid==null){
			regionid=0;
		}
		if(typeid==null){
			typeid=0;
		}
		
		$.ajax({
			url:"api/shop/bonus!useSn.do?sn="+sn+"&regionid="+regionid+"&typeid="+typeid,
			dataType:"json",
			success:function(res){
				if(res.result==1){
					 ipt.attr("disabled",true);
					 ipt.next().hide();
					 CheckOut.refreshTotal();
				}else{
					alert(res.message);
				}
			},
			error:function(){
				alert("糟糕，使用优惠卷发生意外错误");
			}
		});
	},
	cancelBonus:function(sn,box){
		if(sn==""){
			if($(".bonusSPAN>div").size()>1){
				box.remove();
			}else{
				box.find("input").removeAttr("disabled").val("");
				box.find(".userY").show();
			}
			return false;
		}
			
		$.ajax({
			url:"api/shop/bonus!cancelSn.do?sn="+sn,
			dataType:"json",
			success:function(res){
				if(res.result==1){
					if($(".bonusSPAN>div").size()>1){
						box.remove();
					}else{
						box.find("input").removeAttr("disabled").val("");
						box.find(".userY").show();
					}
					 CheckOut.refreshTotal();
				}else{
					alert(res.message);
				}
			},
			error:function(){
				alert("糟糕，使用优惠卷发生意外错误");
			}
		});
		
	},
		
	refreshTotal:function(){
		var regionid = $("#regionid").val();
		var pointprice = $("#pointpriceId").val();	
 		/*var dlytype = $("[name=typeId]:checked");
 		if( dlytype.size()== 0 ){
 			$.alert("请选择配送方式");
 			return ;
 		}
 		var typeId = dlytype.val();*/
 		var typeId=0;
 		//$(".total_wrapper").load("checkout/checkout_total.html?regionId="+regionid+"&typeId="+typeId);
 		window.location.href="checkout.html?integratedprice="+pointprice;
	},
	
	showModifyUI:function(){
		var self = this;
		self.setAddressValue(  self.addrModifyWrap.find(".input") );
		self.addrSelectedWrap.hide();
		self.addrModifyWrap.show();
	}
	
	,
	/**
	 * ====================================
	 * 绑定地址操作事件
	 * ====================================
	 */
	bindAddrEvent:function(){
		
		var self = this;
		//显示地址修改界面
		this.addrSelectedWrap.find(".modify_btn").click(function(){
			$(".address").addClass("relborder");
			$(".payment").removeClass("relborder");
			self.showModifyUI();
		});
			
		
		
		//保存收货地址
		$("#saveAddrBtn").click(function(){
			
			var result = $("#checkoutform").checkall();
			if(!result ) return ;
			
			var modifyWrapper = self.addrModifyWrap.find(".def_addr:checked");
			
			if(modifyWrapper.html()==null){
				alert("请选择收货信息");
				return false;
			}
			//设置 hidden的值 
			self.setHiddenValue(modifyWrapper );
			
			//为显示选中的界面显示html
			var html = self.createAddressHtml( modifyWrapper );
			self.addrSelectedWrap.find("span").remove();
			self.addrSelectedWrap.append(html).show();
			self.addrModifyWrap.hide();
			$(".address").removeClass("relborder");
			$(".payment").addClass("relborder");
			
			//让用户重新选择支付方式 
			self.loadPayment();
			
		});
		
		
		//收货地址选择事件
		this.addrModifyWrap.find(".list input[name=addressId]").click(function(){
			var pointprice = $("#pointpriceId").val();	
			var $this= $(this);
			for(index in Address){
				var name = Address[index];
				$("#"+ name).val( $this.attr(name)  );	
			}
			self.setAddressValue(  self.addrModifyWrap.find(".input")+"?integratedprice="+pointprice );
			//RegionsSelect.load($this.attr("province_id"),$this.attr("city_id"),$this.attr("region_id"));
		});		
	},
	
	
	/**
	 *====================================
	 * 绑定付款方式事件
	 *==================================== 
	 */
	bindPaymentEvent:function(){
		var self = this;
		//显示付款方式修改界面
		this.paySelectedWrap.find(".modify_btn").click(function(){
			self.paySelectedWrap.hide();
			self.payModifyWrap.show();
			$(".payment").addClass("relborder");
		});
		

		//保存付款方式 
		$("#savePaymentBtn").click(function(){
			 var payment = self.payModifyWrap.find(".list [name='paymentId']:checked");
			 if(payment.size()==0){
				 $.alert("请选择支付方式");
				 return false;
			 }
			 var name = payment.attr("payment_name");
			 self.paySelectedWrap.find("span").remove();
			 self.paySelectedWrap.append("<span class='pay_payment'>"+name+"</span>").show();
			 self.payModifyWrap.hide();
			 
			 self.loadOrderTotal();
			 $(".payment").removeClass("relborder");
			 
			 //让用户重新选择配送方式
			 //self.loadDlytype(); 
		});
		
	}
	,
	/**
	 *====================================
	 * 绑定配送方式事件
	 *==================================== 
	 */	
	bindDlytypeEvent:function(){
		var self = this;
		//显示配送方式修改界面
		this.dlytypeSelectedWrap.find(".modify_btn").click(function(){
			self.dlytypeSelectedWrap.hide();
			self.dlytypeModifyWrap.show();
		});
		

		//保存配送方式 
		$("#saveDlytypeBtn").click(function(){
			
			var dlytype= self.dlytypeModifyWrap.find(".list input:radio[name='typeId']:checked");
			if(dlytype.size()==0){
				$.alert("请选择配送方式");
				return false;
			}
			var name = dlytype.attr("type_name");
			
			self.dlytypeSelectedWrap.find("span").remove();
			self.dlytypeSelectedWrap.append("<span>"+name+"</span>").show();
			self.dlytypeModifyWrap.hide();
			
			self.loadOrderTotal();
		});		
	},
	
	/***
	 * 加载支付方式列表
	 */
	loadPayment:function(){
		var self = this;
		$("#savePaymentBtn").show();
		self.paySelectedWrap.hide();
		self.payModifyWrap.show();
		this.payModifyWrap.find(".list").load("checkout/payment_list.html",function(){
			self.payModifyWrap.find("input[name=paymentId]").click(function(){
				self.payModifyWrap.find(".biref").hide();
				$(this).parents("li").find(".biref").show();
			});
		});
	}
	,
	
	
	/**
	 * 加载配送方式
	 */
	loadDlytype:function(){
		var self = this;
		$("#saveDlytypeBtn").show();
		self.dlytypeSelectedWrap.hide();
		self.dlytypeModifyWrap.show();
		var regionid = $("#region_id").val();
		this.dlytypeModifyWrap.find(".list").load("/checkout/dlytype_list.html?regionid="+regionid);
	}
	
	
	,
	/**
	 * 给定一个包装器
	 * @param wrapper
	 * @returns {String}
	 */
	createAddressHtml:function(wrapper){
		var shipAddr = wrapper.attr("shipAddr");
		var shipZip = wrapper.attr("shipZip");
		var shipName = wrapper.attr("shipName");
		var shipTel = wrapper.attr("shipTel");
		var shipMobile = wrapper.attr("shipMobile");
		var html = "<span class='take_delivery'>"+shipAddr+",(收货人："+shipName+",电话："+shipTel+",手机："+shipMobile+",邮编："+shipZip+")</span>";
		
		return html;
		
	},
	
	/**
	 * 设置hidden的value
	 */
	setHiddenValue:function(wrapper){
		
		$("#shipAddr").val( wrapper.find("[name=shipAddr]").val());
		$("#shipZip").val ( wrapper.find("[name=shipZip]").val() );
		$("#shipName").val( wrapper.find("[name=shipName]").val());
		$("#shipTel").val( wrapper.find("[name=shipTel]").val() );
		$("#shipMobile").val(wrapper.find("[name=shipMobile]").val());
		
	}
	,
	
	/**
	 * 设置修改界面的值 
	 * @param wrapper
	 */
	setAddressValue:function(wrapper){
	 
		wrapper.find("[name=shipAddr]").val  ( $("#shipAddr").val()  );
		wrapper.find("[name=shipZip]").val   ( $("#shipZip").val()  );
		wrapper.find("[name=shipName]").val  ( $("#shipName").val()  );
		wrapper.find("[name=shipTel]").val   ( $("#shipTel").val()   );
		wrapper.find("[name=shipMobile]").val( $("#shipMobile").val()   );
		
		
	},
	 
	/**
	 * 加载订单价格 
	 */
 	loadOrderTotal:function(){
 		var regionid = $("#regionid").val();
 		/*var dlytype = $("[name=typeId]:checked");
 		if( dlytype.size()== 0 ){
 			$.alert("请选择配送方式");
 			return ;
 		}
 		var typeId = dlytype.val();*/
 		var typeId=0;
 		$(".total_wrapper").load("checkout/checkout_total.html?regionId="+regionid+"&typeId="+typeId);
	},
	
	createOrder:function(btn){
		var pointprice = $("#pointpriceId").val();	
		var storeids="";
		$("input[name='storeid']").each(function(){
			storeids+=$(this).val()+",";
		});
		if(btn.hasClass("disabled")){
			return false;
		}
		btn.addClass("disabled");
 		$.Loading.show("正在提交您的订单，请稍候..."); 
		var options = {
			url : ctx+"/api/shop/order!create.do?ajax=yes&storeids="+storeids+"&pointprice="+pointprice,
			type : "POST",
			dataType : 'json',
			success : function(result) {
 				if(result.result==1){
	 				location.href="order_create_success.html?orderid="+result.order.order_id;
	 			}else{
	 				$.Loading.hide();
	 				$.alert("下单失败，请重新从购物车下单！");//result.message
	 				location.href="cart.html";
		 		} 
 				btn.removeClass("disabled");
 				btn.addClass("enable");
			},
			error : function(e) {
				$.alert("出现错误 ，请重试");
				btn.addClass("enable");
			}
		};

		$('#checkoutform').ajaxSubmit(options);		
	}

};
$(function(){
	$("#mobile").setValidator(function(){
		var tel= $.trim( $("#tel").val() ) ;
		var mobile = $.trim( $("#mobile").val() ) ;
		
		if( tel=="" && mobile==""  ){
			return  "手机或电话必须填写一项";
		}
		
		if( mobile!="" && !$.isMobile(mobile) ){
			return  "手机格式不正确";
		}
		
		return true;
	});
	
	
	$("#region_id").setValidator(function(){
		var value = $("#region_id").val();
		if( value=="" || value=="0" ) return "地区信息不完整";
		else return true;
	});
	
	
	CheckOut.init();
});