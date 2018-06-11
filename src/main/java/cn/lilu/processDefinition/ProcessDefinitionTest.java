package cn.lilu.processDefinition;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

public class ProcessDefinitionTest {

	//修改分支
	//测试:修改第二次

	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/**
	 * 部署流程定义(从classpath)
	 */
	@Test
	public void deploymentProcessDefinition() {
		Deployment deployment = processEngine.getRepositoryService()// 与流程定义和部署相关的Service
				.createDeployment()// 创建一个部署对象
				.name("流程定义")// 添加部署的名称
				.addClasspathResource("diagrams/helloword.bpmn")// 从classpath的资源加载,一次只能加载一个文件
				.addClasspathResource("diagrams/helloword.png")// 从classpath的资源加载,一次只能加载一个文件
				.deploy();// 完成部署

		System.out.println("部署ID: " + deployment.getId());
		System.out.println("部署名称: " + deployment.getName());
	}

	/**
	 * 部署流程定义(从zip)
	 */
	@Test
	public void deploymentProcessDefinition_zip() {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("diagrams/helloworld.zip");
		ZipInputStream zipInputStream = new ZipInputStream(in);
		Deployment deployment = processEngine.getRepositoryService()// 与流程定义和部署相关的Service
				.createDeployment()// 创建一个部署对象
				.name("流程定义zip")// 添加部署的名称
				.addZipInputStream(zipInputStream).deploy();// 完成部署

		System.out.println("部署ID: " + deployment.getId());
		System.out.println("部署名称: " + deployment.getName());
	}

	/**
	 * 查询流程定义
	 */
	@Test
	public void findProcessDefinition() {
		java.util.List<ProcessDefinition> list = processEngine.getRepositoryService()// 与流程定义和部署相关的Service
				.createProcessDefinitionQuery()// 创建流程定义查询
				// .deploymentId(processDefinitionId)//使用部署对象ID查询
				// .processDefinitionId(processDefinitionId)//使用流程定义ID查询
				// ...//使用流程定义的key查询
				// 使用流程定义的名称模糊查询
				/** 排序 */
				.orderByProcessDefinitionVersion().asc()// 按照版本的升序
				// 按照流程定义的名称降序排序
				/** 返回的结果集 */
				.list();// 返回一个集合列表,封装流程定义
		// .singleResult();//返回唯一结果集
		// .listPage(arg0, arg1);//分页查询

		if (list != null && list.size() > 0) {
			for (ProcessDefinition pd : list) {
				System.out.println("流程定义Id: " + pd.getId());// 流程定义的key+版本+随机生成数
				System.out.println("流程定义的名称: " + pd.getKey());// 对应helloword.bpmn文件中的name属性值
				System.out.println("流程定义key: " + pd.getKey());// 对应helloword.bpmn文件中的id属性值
				System.out.println("流程定义的版本: " + pd.getVersion());// 当流程定义的key值相同的情况下,版本逐一升级,默认1
				System.out.println("流程名称bpmn: " + pd.getResourceName());
				System.out.println("流程名称png: " + pd.getDiagramResourceName());
				System.out.println("部署对象ID: " + pd.getDeploymentId());
				System.out.println("################################");
			}
		}
	}

	/**
	 * 启动流程实例
	 */
	@Test
	public void startProcessInstance() {

		// 流程定义的key
		String processDefinitionkey = "helloworld";
		ProcessInstance pi = processEngine.getRuntimeService()// 与正在执行的流程实例和执行对象相关的service
				// 使用流程定义的key启动流程实例,krey对应helloworld.bpmn文件中id的属性值helloworld,
				// 使用key值启动,默认是按照最新版本的流程定义启动
				.startProcessInstanceByKey(processDefinitionkey);

		System.out.println("流程实例ID: " + pi.getId());
		System.out.println("流程定义ID: " + pi.getProcessDefinitionId());// 流程定义ID
	}

	/**
	 * 删除流程定义
	 */
	@Test
	public void deleteProcessDefinition() {
		// 使用部署ID,完成删除
		String deploymentId = "12501";
		processEngine.getRepositoryService()
				// .deleteDeployment(deploymentId);//不带级联的删除,只能删除没有启动的流程,如果流程启动,抛出异常
				.deleteDeployment(deploymentId, true);
		System.out.println("删除成功");
	}

	/**
	 * 查看流程图
	 * 
	 * @throws Exception
	 */
	@Test
	public void viewPic() throws Exception {
		/**
		 * 生成图片放到文件夹下
		 */
		String deploymentId = "7501";
		// 获取图片资源名称
		List<String> list = processEngine.getRepositoryService().getDeploymentResourceNames(deploymentId);
		// 定义图片资源的名称
		String resourceName = "";
		if (list != null && list.size() > 0) {
			for (String name : list) {
				if (name.indexOf(".png") >= 0) {
					resourceName = name;
				}
			}
		}
		// 获取图片的输入流
		InputStream in = processEngine.getRepositoryService().getResourceAsStream(deploymentId, resourceName);
		// 将图片生成到D盘目录下
		File file = new File("D:/" + resourceName);
		// 将输入流的图片写到D盘下
		FileUtils.copyInputStreamToFile(in, file);
	}
}
