package testActiviti;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.junit.Test;

public class TestActiviti {

	/**
	 * 使用代码创建工作流需要的23张表
	 */
	@Test
	public void creatTable() {
		ProcessEngineConfiguration engineConfiguration = ProcessEngineConfiguration
				.createStandaloneProcessEngineConfiguration();
		// 链接数据库的配置
		engineConfiguration.setJdbcDriver("com.mysql.jdbc.Driver");
		engineConfiguration.setJdbcUrl("jdbc:mysql:///activiti");
		engineConfiguration.setJdbcUsername("root");
		engineConfiguration.setJdbcPassword("admin");

		engineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
		ProcessEngine processEngine = engineConfiguration.buildProcessEngine();
		System.out.println("processEngine" + processEngine);

	}

	/**
	 * 使用配置文件创建表
	 */
	@Test
	public void creatTable2() {
		ProcessEngineConfiguration engineConfiguration = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
		// 工作流的核心对象
		ProcessEngine processEngine = engineConfiguration.buildProcessEngine();
		System.out.println("processEngine" + processEngine);
	}

}
