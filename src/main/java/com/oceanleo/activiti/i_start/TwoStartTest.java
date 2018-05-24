package com.oceanleo.activiti.i_start;

import com.oceanleo.activiti.z_print.PrintUtils;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TwoStartTest {

    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    /**
     * 部署流程定义（从inputStream）
     */
    @Test
    public void deploymentProcessDefinition_inputStream() {
        System.out.println(processEngine.getClass());
        InputStream inputStreamBpmn = this.getClass().getResourceAsStream("twoStart.bpmn");
//        InputStream inputStreamPng = this.getClass().getResourceAsStream("start.png");
        Deployment deployment = processEngine.getRepositoryService()//与流程定义和部署对象相关的Service
                .createDeployment()//创建一个部署对象
                .name("多个开始")//添加部署的名称
                .addInputStream("twoStart.bpmn", inputStreamBpmn)//
//                .addInputStream("start.png", inputStreamPng)//
                .deploy();//完成部署
        System.out.println("部署ID：" + deployment.getId());//
        System.out.println("部署名称：" + deployment.getName());//
    }

    /**
     * 启动流程实例+判断流程是否结束+查询历史
     */
    @Test
    public void deployeeProcessInstance() {
        List<ProcessDefinition> processDefinitionList = processEngine.getRepositoryService().createProcessDefinitionQuery().list();
        PrintUtils.printProcessDefinition(processDefinitionList);
    }

    /**
     * 启动流程实例+判断流程是否结束+查询历史
     */
    @Test
    public void startProcessInstance() {
        //流程定义的key
        String processDefinitionKey = "twoStart";
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("role", "M");
        ProcessInstance pi = processEngine.getRuntimeService()//与正在执行的流程实例和执行对象相关的Service
                .startProcessInstanceByKey(processDefinitionKey, variables);//使用流程定义的key启动流程实例，key对应helloworld.bpmn文件中id的属性值，使用key值启动，默认是按照最新版本的流程定义启动
        System.out.println("流程实例ID:" + pi.getId());//流程实例ID    101
        System.out.println("流程定义ID:" + pi.getProcessDefinitionId());//流程定义ID   helloworld:1:4
    }

    @Test
    public void queryTask() {
        //流程定义的key
        String processDefinitionKey = "twoStart";
        List<Task> taskList = processEngine.getTaskService().createTaskQuery().processDefinitionKey(processDefinitionKey).list();
        PrintUtils.printTask(taskList);
    }

    /**
     * 完成我的任务
     */
    @Test
    public void completeMyPersonalTask() {
        //任务ID
        String taskId = "175003";
        for (String t : taskId.split(",")) {

            processEngine.getTaskService()//与正在执行的任务管理相关的Service
                    .complete(t);
            System.out.println("完成任务：任务ID：" + t);
        }
    }
}
