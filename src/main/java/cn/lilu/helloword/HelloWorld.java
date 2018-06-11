package cn.lilu.helloword;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class HelloWorld {

	ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

	/**
	 * 部署流程定义
	 */
	@Test
	public void deploymentProcessDefinition() {
		Deployment deployment = processEngine.getRepositoryService()// 与流程定义和部署相关的Service
				.createDeployment()// 创建一个部署对象
				.name("helloword入门程序").addClasspathResource("diagrams/helloword.bpmn")// 从classpath的资源加载,一次只能加载一个文件
				.addClasspathResource("diagrams/helloword.png")// 从classpath的资源加载,一次只能加载一个文件
				.deploy();// 完成部署

		System.out.println(deployment.getId());
		System.out.println(deployment.getName());
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
	 * 查询当前人的个人任务
	 */
	@Test
	public void findMyPersonalTask() {
		String assignee = "范冰冰";
		List<Task> list = processEngine.getTaskService()// 与正在执行的任务管理器相关的service
				.createTaskQuery()// 创建任务查询对象
				.taskAssignee(assignee)// 指定个人任务查询,指定办理人
				.list();
		System.out.println(list);
		if (list != null && list.size() > 0) {
			for (Task task : list) {
				System.out.println("任务ID: " + task.getId());
				System.out.println("任务名称: " + task.getName());
				System.out.println("任务创建时间: " + task.getCreateTime());
				System.out.println("任务办理人: " + task.getAssignee());
				System.out.println("任务实例ID: " + task.getProcessInstanceId());
				System.out.println("执行对象ID: " + task.getExecutionId());
				System.out.println("流程定义ID: " + task.getProcessDefinitionId());
			}
		}
	}

	/**
	 * 完成我的任务,任务完成之后就查询不到当前个人任务了,就会成为历史了
	 */
	@Test
	public void completeMyPersonalTask() {
		// 任务ID
		String taskId = "104";

		processEngine.getTaskService()// 与正在执行的任务管理器相关的service
				.complete(taskId);
		System.out.println("完成任务,任务ID为: " + taskId);
	}

}
