package com.clouter.pheromones;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.clouter.clouterutil.FileUtil;
import com.clouter.clouterutil.velocity.ConverterParam;
import com.clouter.clouterutil.velocity.VelocityConverter;
import com.clouter.pheromones.node.PheroGlobalData;
import com.clouter.pheromones.node.PheromonesInputData;
import com.clouter.pheromones.protocol.ProtocolGlobalData;

public class PheromonesExtensionLauncher {
	private static final Log log = LogFactory.getLog(PheromonesExtensionLauncher.class);
	
	public void launchProtocol(String configPath){
		ProtocolGlobalData.clean();
		ProtocolGlobalData.getInstance().loadConfig(configPath);
		
		VelocityConverter converter = new VelocityConverter();
		
		//协议文档生成
		ConverterParam param = new ConverterParam();
		PheromonesInputData inputData = new PheromonesInputData(ProtocolGlobalData.getInstance().getProtocolList());
		inputData.addOther("protocolGlobalData", ProtocolGlobalData.getInstance());
		param.setInputData(inputData);
		
		param.setEncodeStr(ProtocolGlobalData.getInstance().getProperty("encode"));
		param.setVmFile(ProtocolGlobalData.getInstance().getProperty("vm_file_protocol"));
		
		converter.convert(param);
		
		String projectPath = PheroGlobalData.getInstance().getProperty("project_path");
		param.getFileContent().setFilePath(projectPath + "/" + ProtocolGlobalData.getInstance().getProperty("output_path_protocol"));
		param.getFileContent().setFileName(ProtocolGlobalData.getInstance().getProperty("output_name_protocol"));
		log.info("[output]" + param.getFileContent());
		FileUtil.saveFileContent(param.getFileContent());
		
		//协议分发器生成
		param.setEncodeStr(ProtocolGlobalData.getInstance().getProperty("encode"));
		param.setVmFile(ProtocolGlobalData.getInstance().getProperty("vm_file_dispatcher"));
		
		converter.convert(param);
		
		param.getFileContent().setFilePath(projectPath + "/" + ProtocolGlobalData.getInstance().getProperty("output_path_dispatcher"));
		param.getFileContent().setFileName(ProtocolGlobalData.getInstance().getProperty("output_name_dispatcher"));
		log.info("[output]" + param.getFileContent());
		FileUtil.saveFileContent(param.getFileContent());
	}
	
	public static void main(String[] args) {
		new PheromonesLauncher().launch("/Users/flynn/Documents/clouter/workspace/Pheromones/test/config.xml");
		new PheromonesExtensionLauncher().launchProtocol("/Users/flynn/Documents/clouter/workspace/PheromonesExtension/test/protocol_cfg.xml");
	}
}
