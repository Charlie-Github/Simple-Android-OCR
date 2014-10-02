package com.edible.entity;

/**
 * 字典类生成工厂，根据语言参数生成对应的字典类
 * @author mingjiang
 *
 */
public class DictionaryFactory {
	public static Dictionary getDictionaryInstance(String lang) {
		if(lang.equalsIgnoreCase("en")) {
			return new DictionaryEN();
		} else if(lang.equalsIgnoreCase("fr")) {
			return new DictionaryFR();
		} else if(lang.equalsIgnoreCase("jp")) {
			return new DictionaryJP();
		} 
		return null;
	}
}
