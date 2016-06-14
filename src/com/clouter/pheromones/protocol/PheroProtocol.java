package com.clouter.pheromones.protocol;

import java.util.HashMap;
import java.util.Map;

import org.jdom2.Attribute;
import org.jdom2.Element;

import com.clouter.clouterutil.XmlUtil;
import com.clouter.pheromones.exception.NoSuchProtocolException;
import com.clouter.pheromones.node.PheroGlobalData;
import com.clouter.pheromones.node.PheroNode;

/**
 * 协议描述类
 * @author flynn
 *
 */
public class PheroProtocol {
	private Map<String, String> properties;
	
	public PheroProtocol(Element element){
		properties = new HashMap<String, String>();
		properties.putAll(XmlUtil.propertiesParse(element));
		for(Attribute attribute : element.getAttributes()){
			properties.put(attribute.getName(), attribute.getValue());
		}
	}
	
	public PheroNode getAliasPheroNode(){
		return PheroGlobalData.getInstance().getPheroNode(getProperty("alias"));
	}

	public short getId() {
		return Short.parseShort(getProperty("id"));
	}

	public String getAlias() {
		return getProperty("alias");
	}
	
	public String getProperty(String key){
		String rt = properties.get(key);
		if(rt == null){
			String parentIdStr = properties.get("parent");
			if(parentIdStr != null){
				short parentId = Short.parseShort(parentIdStr);
				PheroProtocol protocol = ProtocolGlobalData.getInstance().getProtocol(parentId);
				if(protocol == null){
					throw new NoSuchProtocolException(parentId);
				}
				return protocol.getProperty(key);
			}
		}
		return rt;
	}
	
	public boolean isVisible(){
		return "true".equals(getProperty("visible"));
	}
}
