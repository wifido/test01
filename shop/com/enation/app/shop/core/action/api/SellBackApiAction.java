package com.enation.app.shop.core.action.api;


import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.service.auth.IAdminUserManager;
import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.model.SellBackGoodsList;
import com.enation.app.shop.core.model.SellBackList;
import com.enation.app.shop.core.service.ILogiManager;
import com.enation.app.shop.core.service.IOrderManager;
import com.enation.app.shop.core.service.ISellBackManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.action.WWAction;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.util.DateUtil;
/**
 *  退换货Api
 * @author fenlongli
 *
 */
@ParentPackage("eop_default")
@Namespace("/api/shop")
@Action("sellBack")
@Component
public class SellBackApiAction extends WWAction {
	private IOrderManager orderManager;
	private ILogiManager logiManager;
	private IAdminUserManager adminUserManager;
	private ISellBackManager sellBackManager;
	private IDaoSupport daoSupport;
	
	private String remark;
	private Integer[] goodsId;
	private Integer[] gid;
	private Integer[] goodsNum;
	private Integer[] payNum;
	private Integer[] productId;
	private Double[] price;
	private Integer orderId;
	
	private String return_account; //退款账户
	private String refund_way;	//退款方式
	private String refund_reason;//退货理由
	/**
	 * 保存退货申请
	 * @param orderId 订单ID
	 * @param goodsId 商品ID
	 * @param gid 选择退货的商品ID
	 * @param goodsNum 退货数量
	 * @param payNum 购买数量
	 * @param sid 退货单ID
	 * @param remark 退货原因
	 * @param productId 货品ID
	 * @param price 商品单价
	 * @return
	 */
	public String save(){
		try {
			//创建退货单
			SellBackList sellBackList=new SellBackList();
			//记录会员信息
			Member member =  UserConext.getCurrentMember();
			sellBackList.setMember_id(member.getMember_id());
			sellBackList.setSndto(member.getName());
			//订单信息
			Order order = orderManager.get(orderId);
			if(order.getShipping_area()!=null || order.getShipping_area()!="" || order.getShipping_area().trim()!="暂空"){
				sellBackList.setAdr(order.getShip_addr());
			}else{
				String adr[]=order.getShipping_area().split("-"); 
				sellBackList.setAdr(adr[0] + adr[1] + adr[2] + order.getShip_addr());
			}
			sellBackList.setTradeno(com.enation.framework.util.DateUtil.toString(DateUtil.getDateline(),"yyMMddhhmmss"));//退货单号
			sellBackList.setOrdersn(order.getSn());
			sellBackList.setRegoperator("会员");
			sellBackList.setTel(order.getShip_tel());
			sellBackList.setZip(order.getShip_zip());
			sellBackList.setTradestatus(0);
			sellBackList.setRegtime(DateUtil.getDateline());
			sellBackList.setDepotid(order.getDepotid());
			sellBackList.setRemark(remark);
			sellBackList.setRefund_way(refund_way);
			sellBackList.setReturn_account(return_account);
			Integer sid = this.sellBackManager.save(sellBackList);
			//创建退货单结束 创建退货单商品
			for(int i=0;i<goodsId.length;i++){
				for(int j=0;j<gid.length;j++){
					if(goodsId[i].intValue()==gid[j].intValue()){
						this.saveGoodsList(goodsId[i],goodsNum[j],payNum[j],sid,remark,productId[i],price[i]);
						this.daoSupport.execute("update es_order_items set state=1 where order_id=? and product_id=?", this.orderId,productId[i]); //将订单货品更新为申请退货
					}
				}
			}
			this.showSuccessJson("退货单申请成功，请等待审核");
		} catch (Exception e) {
			this.showErrorJson("退货单申请失败");
			this.logger.error("退货单申请失败：",e);
		}
		return this.JSON_MESSAGE;
	}
	
//	Logi logi = logiManager.getLogiById(sellBackList.getLogi_id()); //物流公司 如果提交审核提供客户寄回地址并且让客户选择物流公司填写快递单号
//	sellBackList.setLogi_name(logi.getName());
//	sellBackList.setDepotid(depotid);
	/**
	 * 保存退货商品
	 * @param goodsid 商品Id
	 * @param goodsnum 退货的数量
	 * @param paynum 购买数量
	 * @param id 退货单Id
	 * @param remark 备注
	 * @param productid 货品Id
	 * @param price 价格
	 * @return 退货商品ID
	 */
	public Integer saveGoodsList(Integer goodsid,Integer goodsnum,Integer paynum,Integer id,String remark,Integer productid,Double price){
		SellBackGoodsList sellBackGoods = new SellBackGoodsList();
	 
		sellBackGoods.setRecid(id);
		sellBackGoods.setPrice(price);
		sellBackGoods.setReturn_num(goodsnum);
		sellBackGoods.setShip_num(paynum);
		sellBackGoods.setGoods_id(goodsid);	
		sellBackGoods.setGoods_remark(remark);
		sellBackGoods.setProduct_id(productid);
		Integer sid = this.sellBackManager.saveGoodsList(sellBackGoods);
		return sid;
	}

	public IOrderManager getOrderManager() {
		return orderManager;
	}

	public void setOrderManager(IOrderManager orderManager) {
		this.orderManager = orderManager;
	}

	public ILogiManager getLogiManager() {
		return logiManager;
	}

	public void setLogiManager(ILogiManager logiManager) {
		this.logiManager = logiManager;
	}

	public IAdminUserManager getAdminUserManager() {
		return adminUserManager;
	}

	public void setAdminUserManager(IAdminUserManager adminUserManager) {
		this.adminUserManager = adminUserManager;
	}

	public ISellBackManager getSellBackManager() {
		return sellBackManager;
	}

	public void setSellBackManager(ISellBackManager sellBackManager) {
		this.sellBackManager = sellBackManager;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer[] getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer[] goodsId) {
		this.goodsId = goodsId;
	}

	public Integer[] getGid() {
		return gid;
	}

	public void setGid(Integer[] gid) {
		this.gid = gid;
	}

	public Integer[] getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(Integer[] goodsNum) {
		this.goodsNum = goodsNum;
	}

	public Integer[] getPayNum() {
		return payNum;
	}

	public void setPayNum(Integer[] payNum) {
		this.payNum = payNum;
	}

	public Integer[] getProductId() {
		return productId;
	}

	public void setProductId(Integer[] productId) {
		this.productId = productId;
	}

	public Double[] getPrice() {
		return price;
	}

	public void setPrice(Double[] price) {
		this.price = price;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getReturn_account() {
		return return_account;
	}

	public void setReturn_account(String return_account) {
		this.return_account = return_account;
	}

	public String getRefund_way() {
		return refund_way;
	}

	public void setRefund_way(String refund_way) {
		this.refund_way = refund_way;
	}

	public IDaoSupport getDaoSupport() {
		return daoSupport;
	}

	public void setDaoSupport(IDaoSupport daoSupport) {
		this.daoSupport = daoSupport;
	}

	public String getRefund_reason() {
		return refund_reason;
	}

	public void setRefund_reason(String refund_reason) {
		this.refund_reason = refund_reason;
	}
	
	
}
