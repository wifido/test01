package com.enation.app.shop.core.plugin.search;

import java.util.List;

import com.enation.framework.util.StringUtil;

/**
 * 选器实体
 * @author kingapex
 *
 */
public class SearchSelector {
	
	private String name;
	private String url;
	private boolean isSelected;
	private String value; //当前的值 
	private String name_ru;
	private String name_en;
	
	//本选择器的其它选项
	private List<SearchSelector> otherOptions;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		if(!StringUtil.isEmpty(url) && url.startsWith("/")){
			url= url.substring(1, url.length());
		}
		return url;
	}
	
	
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean getIsSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public List<SearchSelector> getOtherOptions() {
		return otherOptions;
	}
	public void setOtherOptions(List<SearchSelector> otherOptions) {
		this.otherOptions = otherOptions;
	}
	
	public String getName_ru() {
		return name_ru;
	}
	public void setName_ru(String name_ru) {
		this.name_ru = name_ru;
	}
	public String getName_en() {
		return name_en;
	}
	public void setName_en(String name_en) {
		this.name_en = name_en;
	}
	
	
}
