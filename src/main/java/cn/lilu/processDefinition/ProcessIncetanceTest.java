package cn.lilu.processDefinition;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

import java.io.InputStream;
import java.util.zip.ZipInputStream;

/**
 * 流程实例,任务,执行对象控制
 */
public class ProcessIncetanceTest {

    //流程引擎
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
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

}
