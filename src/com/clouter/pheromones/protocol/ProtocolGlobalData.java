package com.clouter.pheromones.protocol;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom2.Element;

import com.clouter.clouterutil.XmlUtil;

/**
 * 协议全局数据
 * @author flynn
 *
 */
public class ProtocolGlobalData {
	private static final Log log = LogFactory.getLog(ProtocolGlobalData.class);
	private static ProtocolGlobalData instance;
	public static ProtocolGlobalData getInstance(){
		if(instance == null){
			instance = new ProtocolGlobalData();
		}
		return instance;
	}
	private ProtocolGlobalData(){
		properties = new HashMap<String, String>();
		protocolList = new ArrayList<PheroProtocol>();
		protocolMap = new HashMap<Short, PheroProtocol>();
		protocolIdSet = new HashSet<Short>();
		nodeAliasSet = new HashSet<String>();
	}
	/**配置*/
	private Map<String, String> properties;
	/**文件位置*/
	private String baseDirPath;
	/**协议列表*/
	private List<PheroProtocol> protocolList;
	private Map<Short, PheroProtocol> protocolMap;
	/**协议id列表*/
	private Set<Short> protocolIdSet;
	/**协议对应Alias列表*/
	private Set<String> nodeAliasSet;
	
	public static void clean(){
		instance = new ProtocolGlobalData();
	}
	
	public void loadConfig(String filePath){
		Element configRoot = XmlUtil.getRoot(filePath);
		
		baseDirPath = new File(filePath).getParent();
		log.info("baseDirPath: " + baseDirPath);
		
		for(Element element : configRoot.getChildren()){
			if(element.getName().equals("properties")){
				properties.putAll(XmlUtil.propertiesParse(element));
			}else if(element.getName().equals("protocols")){
				load(element);
			}
		}
	}
	
	/**
	 * 根据名为protocols的Element加载协议描述信息
	 * @param element
	 */
	private void load(Element element){
		if(!element.getName().equals("protocols")) return;
		for(Element eleProtocol : element.getChildren("protocol")){
			String resource = XmlUtil.getAttributeValue(eleProtocol, "resource");
			if(resource != null){
				if(new File(resource).exists()){
					load(XmlUtil.getRoot(resource));
				}else{
					load(XmlUtil.getRoot(baseDirPath + "/" + resource));
				}
			}else{
				addProtocol(new PheroProtocol(eleProtocol));
			}
		}
	}
	
	private void addProtocol(PheroProtocol protocol){
		protocolList.add(protocol);
		protocolMap.put(protocol.getId(), protocol);
		protocolIdSet.add(protocol.getId());
		
		Set<String> set = new HashSet<>();
		protocol.getAliasPheroNode().getAllUsedAlias(false, set);
		nodeAliasSet.addAll(set);
	}
	
	public List<PheroProtocol> getProtocolList(){
		return protocolList;
	}
	
	public String getProperty(String key){
		return properties.get(key);
	}
	
	public boolean isUsedInProtocolNode(String alias){
		return nodeAliasSet.contains(alias);
	}
	
	public PheroProtocol getProtocol(short id){
		return protocolMap.get(id);
	}
}
