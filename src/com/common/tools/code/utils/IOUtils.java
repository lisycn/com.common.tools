package com.common.tools.code.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

public class IOUtils {
	private static final Logger logger = Logger.getLogger(IOUtils.class);
	
	public static void main(String[] args) throws IOException {
		testRemoveFile();
	}

	private static void testRemoveFile() throws IOException {
		logger.info("remove file begin..");
		String[] pathnames = {
			"D:/app/cts/com.indicator.web/src/com/youbesttech/fundtrade/controller/NonsAssetPayIntstPlanController.java",
			"D:/app/cts/com.indicator.web/src/com/youbesttech/fundtrade/controller/NonsAssetPayPrinPlanController.java",
			"D:/app/cts/com.indicator.web/src/com/youbesttech/fundtrade/dao/impl/NonsAssetPayIntstPlanDaoImpl.java",
			"D:/app/cts/com.indicator.web/src/com/youbesttech/fundtrade/dao/impl/NonsAssetPayPrinPlanDaoImpl.java",
			"D:/app/cts/com.indicator.web/src/com/youbesttech/fundtrade/dao/NonsAssetPayIntstPlanDao.java",
			"D:/app/cts/com.indicator.web/src/com/youbesttech/fundtrade/dao/NonsAssetPayPrinPlanDao.java",
			"D:/app/cts/com.indicator.web/src/com/youbesttech/fundtrade/pojo/NonsAssetPayIntstPlan.java",
			"D:/app/cts/com.indicator.web/src/com/youbesttech/fundtrade/pojo/NonsAssetPayPrinPlan.java",
			"D:/app/cts/com.indicator.web/src/com/youbesttech/fundtrade/service/impl/NonsAssetPayIntstPlanServiceImpl.java",
			"D:/app/cts/com.indicator.web/src/com/youbesttech/fundtrade/service/impl/NonsAssetPayPrinPlanServiceImpl.java",
			"D:/app/cts/com.indicator.web/src/com/youbesttech/fundtrade/service/NonsAssetPayIntstPlanService.java",
			"D:/app/cts/com.indicator.web/src/com/youbesttech/fundtrade/service/NonsAssetPayPrinPlanService.java",
			"D:/app/cts/com.dplatform.app/WebContent/fundtrade/nonsAssetPayIntstPlan",
			"D:/app/cts/com.dplatform.app/WebContent/fundtrade/nonsAssetPayPrinPlan",
			"D:/app/cts/com.dplatform.app/WebContent/js/app/fundtrade/nonsAssetPayIntstPlan",
			"D:/app/cts/com.dplatform.app/WebContent/js/app/fundtrade/nonsAssetPayPrinPlan"
		};
		
		for(String pathname : pathnames){
			File f = new File(pathname);
			if (f.isDirectory()) {
				FileUtils.deleteDirectory(f);
			}else{
				f.delete();
			}
		}
		

		logger.info("remove file end..");
	}
	
	public static List<String> readLine(File file,String encoding) throws FileNotFoundException, IOException{
		return org.apache.commons.io.IOUtils.readLines(new FileInputStream(file), encoding);
	}
}
