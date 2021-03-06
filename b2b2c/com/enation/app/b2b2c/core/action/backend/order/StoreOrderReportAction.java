package com.enation.app.b2b2c.core.action.backend.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.service.order.IB2B2cOrderReportManager;
import com.enation.app.b2b2c.core.service.order.IStoreOrderManager;
import com.enation.app.b2b2c.core.service.order.IStoreSellBackManager;
import com.enation.app.shop.core.model.DlyType;
import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.model.PayCfg;
import com.enation.app.shop.core.service.IDlyTypeManager;
import com.enation.app.shop.core.service.IOrderManager;
import com.enation.app.shop.core.service.IPaymentManager;
import com.enation.app.shop.core.service.OrderStatus;
import com.enation.framework.action.WWAction;
@Component
@ParentPackage("eop_default")
@Namespace("/b2b2c/admin")
@Results({
	 @Result(name="paymentList",type="freemarker", location="/b2b2c/admin/orderReport/payment_list.html"),
	 @Result(name="returnedList",type="freemarker", location="/b2b2c/admin/orderReport/returned_list.html"),
	 @Result(name="returnedCheckList",type="freemarker", location="/b2b2c/admin/orderReport/returned_check_list.html")
})
@Action("storeOrderReport")
public class StoreOrderReportAction extends WWAction {
	private IStoreOrderManager storeOrderManager;
	private IB2B2cOrderReportManager b2B2cOrderReportManager;
	private IStoreSellBackManager storeSellBackManager;
	private IDlyTypeManager dlyTypeManager;
	private IPaymentManager paymentManager;
	private IOrderManager orderManager;
	private List<DlyType> shipTypeList;
	private List<PayCfg> payTypeList;
	
	private Map statusMap;
	private Map payStatusMap;
	private Map shipMap;
	private Map orderMap;
	
	private String payStatus_Json;
	private String status_Json;
	private String ship_Json;
	
	private Integer stype;
	private String keyword;
	private String start_time;
	private String end_time;
	private String sn;
	private String order;
	private Integer paystatus=null;
	private Integer payment_id=null;
	private String store_name;
	private Integer store_id;
	private Integer status;
	private Integer id;
	/**
	 * 付款单列表
	 * @param statusMap 订单状态,Map
	 * @param payStatusMap 付款状态,Map
	 * @param shipMap 发货状态,Map
	 * @param shipTypeList 配送方式列表,List
	 * @param payTypeList 付款方式列表,List
	 * @return
	 */
	public String paymentList(){
		if(statusMap==null){
			statusMap = new HashMap();
			statusMap = storeOrderManager.getStatusJson();
			String p= JSONArray.fromObject(statusMap).toString();
			status_Json=p.replace("[", "").replace("]", "");
		}
		
		if(payStatusMap==null){
			payStatusMap = new HashMap();
			payStatusMap = storeOrderManager.getpPayStatusJson();
			String p= JSONArray.fromObject(payStatusMap).toString();
			payStatus_Json=p.replace("[", "").replace("]", "");
			
		}
		
		if(shipMap ==null){
			shipMap = new HashMap();
			shipMap = storeOrderManager.getShipJson();
			String p= JSONArray.fromObject(shipMap).toString();
			ship_Json = p.replace("[", "").replace("]", "");
			
		}
		shipTypeList = dlyTypeManager.list();
		payTypeList = paymentManager.list();
		return "paymentList";
	}
	/**
	 * 获取付款单列表Json
	 * @param stype 搜索类型,Integer
	 * @param keyword 搜索关键字,String
	 * @param start_time 下单时间,String
	 * @param end_time 结束时间,Stirng
	 * @param sn 编号,String
	 * @param paystatus 付款状态,Integer
	 * @param payment_id 付款方式Id,Integer
	 * @param order 排序
	 * @return 付款单列表Json
	 */
	public String paymentListJson(){
		orderMap = new HashMap();
		orderMap.put("stype", stype);
		orderMap.put("keyword", keyword);
		orderMap.put("start_time", start_time);
		orderMap.put("end_time", end_time);
		orderMap.put("sn", sn);
		orderMap.put("paystatus", paystatus);
		orderMap.put("payment_id", payment_id);
		orderMap.put("store_name", store_name);
		
		this.webpage = b2B2cOrderReportManager.listPayment(orderMap,this.getPage(), this.getPageSize(), order);

		this.showGridJson(webpage);
		return JSON_MESSAGE;
	}
	/**
	 * 退货单列表
	 * @return
	 */
	public String returnedList(){
		return "returnedList";
	}
	/**
	 * 退货单列表JSON
	 * @return
	 */
	public String returnedListJson(){
		orderMap = new HashMap();
		orderMap.put("keyword", keyword);
		this.webpage = b2B2cOrderReportManager.listRefund(this.getPage(), this.getPageSize(), order, orderMap);
		this.showGridJson(webpage);
		return JSON_MESSAGE;
	}
	public String storeSellBackList(){
		Map map=new HashMap();
		map.put("start_time", start_time);
		map.put("end_time", end_time);
		this.webpage=storeSellBackManager.list(page, pageSize, store_id,status, map);
		this.showGridJson(this.webpage);
		return this.JSON_MESSAGE;
	}
	//1、跳转到退单审核列表
	/**
	 * 跳转到退单审核列表
	 * @return
	 */
	public String returnedCheckList(){
		return "returnedCheckList";
	}
	//2、进行审核页面
	public String returnedCheckListJson(){
		this.webpage = b2B2cOrderReportManager.listRefund(this.getPage(), this.getPageSize(), order);
		this.showGridJson(webpage);
		return JSON_MESSAGE;
	}
	//3、通过或者不通过
	public String editStatus(){
		if(status!=null && id!=null){
			if (status == 2){//通过
				this.b2B2cOrderReportManager.updateStatus(status, id);
				//修改相应的订单状态    ORDER_CANCEL_SHIP = -2; // 退货
				Order order = orderManager.get(sn);
				order.setStatus(OrderStatus.ORDER_CANCEL_SHIP);
				orderManager.edit(order);
				this.showSuccessJson("修改成功");
			}else{
				this.b2B2cOrderReportManager.updateStatus(status, id);
				//修改相应的订单状态    ORDER_DISPUTE = 10; // 纠纷订单
				Order order = orderManager.get(sn);
				order.setStatus(OrderStatus.ORDER_DISPUTE);
				orderManager.edit(order);
				this.showSuccessJson("修改成功");
			}
		}
		return this.JSON_MESSAGE;
	}
	//get set 
	public IStoreOrderManager getStoreOrderManager() {
		return storeOrderManager;
	}

	public void setStoreOrderManager(IStoreOrderManager storeOrderManager) {
		this.storeOrderManager = storeOrderManager;
	}
	public IDlyTypeManager getDlyTypeManager() {
		return dlyTypeManager;
	}

	public void setDlyTypeManager(IDlyTypeManager dlyTypeManager) {
		this.dlyTypeManager = dlyTypeManager;
	}

	public IPaymentManager getPaymentManager() {
		return paymentManager;
	}

	public void setPaymentManager(IPaymentManager paymentManager) {
		this.paymentManager = paymentManager;
	}

	public IOrderManager getOrderManager() {
		return orderManager;
	}
	
	public void setOrderManager(IOrderManager orderManager) {
		this.orderManager = orderManager;
	}
	public List<DlyType> getShipTypeList() {
		return shipTypeList;
	}

	public void setShipTypeList(List<DlyType> shipTypeList) {
		this.shipTypeList = shipTypeList;
	}

	public List<PayCfg> getPayTypeList() {
		return payTypeList;
	}

	public void setPayTypeList(List<PayCfg> payTypeList) {
		this.payTypeList = payTypeList;
	}

	public Map getStatusMap() {
		return statusMap;
	}

	public void setStatusMap(Map statusMap) {
		this.statusMap = statusMap;
	}

	public Map getPayStatusMap() {
		return payStatusMap;
	}

	public void setPayStatusMap(Map payStatusMap) {
		this.payStatusMap = payStatusMap;
	}

	public Map getShipMap() {
		return shipMap;
	}

	public void setShipMap(Map shipMap) {
		this.shipMap = shipMap;
	}

	public String getPayStatus_Json() {
		return payStatus_Json;
	}

	public void setPayStatus_Json(String payStatus_Json) {
		this.payStatus_Json = payStatus_Json;
	}

	public String getStatus_Json() {
		return status_Json;
	}

	public void setStatus_Json(String status_Json) {
		this.status_Json = status_Json;
	}

	public String getShip_Json() {
		return ship_Json;
	}

	public void setShip_Json(String ship_Json) {
		this.ship_Json = ship_Json;
	}
	public IB2B2cOrderReportManager getB2B2cOrderReportManager() {
		return b2B2cOrderReportManager;
	}
	public void setB2B2cOrderReportManager(
			IB2B2cOrderReportManager b2b2cOrderReportManager) {
		b2B2cOrderReportManager = b2b2cOrderReportManager;
	}
	public Map getOrderMap() {
		return orderMap;
	}
	public void setOrderMap(Map orderMap) {
		this.orderMap = orderMap;
	}
	public Integer getStype() {
		return stype;
	}
	public void setStype(Integer stype) {
		this.stype = stype;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public Integer getPaystatus() {
		return paystatus;
	}
	public void setPaystatus(Integer paystatus) {
		this.paystatus = paystatus;
	}
	public Integer getPayment_id() {
		return payment_id;
	}
	public void setPayment_id(Integer payment_id) {
		this.payment_id = payment_id;
	}
	public String getStore_name() {
		return store_name;
	}
	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}
	public IStoreSellBackManager getStoreSellBackManager() {
		return storeSellBackManager;
	}
	public void setStoreSellBackManager(IStoreSellBackManager storeSellBackManager) {
		this.storeSellBackManager = storeSellBackManager;
	}
	public Integer getStore_id() {
		return store_id;
	}
	public void setStore_id(Integer store_id) {
		this.store_id = store_id;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
}
