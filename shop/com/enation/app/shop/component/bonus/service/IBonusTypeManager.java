package com.enation.app.shop.component.bonus.service;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.component.bonus.model.BonusType;
import com.enation.app.shop.component.bonus.model.RegisterBonus;
import com.enation.framework.database.Page;

/**
 * 红包类型管理
 * @author kingapex
 *2013-8-13下午2:38:47
 */
public interface IBonusTypeManager {
	
	/**
	 * 添加一个红包
	 * @param bronusType
	 */
	public void add(BonusType bronusType);
	
	
	/**
	 * 修改一个红包
	 * @param bronusType
	 */
	public void update(BonusType bonusType);
	
	
	/**
	 * 删除一个红包
	 * @param bronusTypeId
	 */
	@Transactional(propagation = Propagation.REQUIRED)  
	public void delete(Integer [] bonusTypeId);
	
	
	/**
	 * 分页读取红包类型
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public Page list(int page, int pageSize);
	public Page listRigister(int page, int pageSize);
	public Page listRigisterJson(Integer activeid,int page, int pageSize);
	public Page listRigisterForAllJson(int page, int pageSize,Integer activitid);
	
	
	/**
	 * 获取一个红包类型
	 * @param typeid
	 * @return
	 */
	public BonusType get(int typeid);
	
	/**
	 * 根据红包卢布金额获取一个红包类型
	 * @param type_money_ru
	 * @return
	 */
	public BonusType getBonusType(Double type_money_ru);

	/**
	 * 分页读取店铺红包类型
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public Page listStoreBouns(int page, int pageSize);


	public List<BonusType> queryForBonustype(int i);
	public void addRigisterBonus(RegisterBonus registerBonus);
	public void updateRegister(Integer id,RegisterBonus registerBonus);
	public void deleteRegisterBonus(Integer[] id);
	public void deleteRegister(Integer id);
	public int findRegisterBonus(Integer[] id);
	public int findBonus(Integer[] type_id);
	public RegisterBonus lookRegisterBonus(Integer id);
	public void addRegisterRel(Integer id,Integer activid);
	
}
