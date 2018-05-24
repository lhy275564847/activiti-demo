package com.oceanleo.activiti.n_inclusiveGateway;

import com.oceanleo.activiti.z_print.PrintUtils;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InclusiveGatewayTest {

    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    /**
     * 部署流程定义（从inputStream）
     */
    @Test
    public void deploymentProcessDefinition_inputStream() {
        InputStream inputStreamBpmn = this.getClass().getResourceAsStream("inclusiveGateway.bpmn");
        Deployment deployment = processEngine.getRepositoryService()//与流程定义和部署对象相关的Service
                .createDeployment()//创建一个部署对象
                .name("包含网关")//添加部署的名称
                .addInputStream("inclusiveGateway.bpmn", inputStreamBpmn)         //
//                .addInputStream("inclusiveGateway.png", inputStreamPng)           //
                .deploy();                                                  //完成部署
        System.out.println("部署ID：" + deployment.getId());                //
        System.out.println("部署名称：" + deployment.getName());            //
    }

    /**
     * 启动流程实例
     */
    @Test
    public void startProcessInstance() {
        //流程定义的key
        String processDefinitionKey = "inclusiveGateway";
        ProcessInstance pi = processEngine.getRuntimeService()//与正在执行的流程实例和执行对象相关的Service
                .startProcessInstanceByKey(processDefinitionKey);//使用流程定义的key启动流程实例，key对应helloworld.bpmn文件中id的属性值，使用key值启动，默认是按照最新版本的流程定义启动
        System.out.println("流程实例ID:" + pi.getId());//流程实例ID    101
        System.out.println("流程定义ID:" + pi.getProcessDefinitionId());//流程定义ID   helloworld:1:4
    }

    /**
     * 查询当前人的个人任务
     */
    @Test
    public void findMyPersonalTask() {
        List<Task> list = processEngine.getTaskService()//与正在执行的任务管理相关的Service
                .createTaskQuery()//创建任务查询对象
                /**查询条件（where部分）*/
//                .taskAssignee(assignee)//指定个人任务查询，指定办理人
//						.taskCandidateUser(candidateUser)//组任务的办理人查询
//						.processDefinitionId(processDefinitionId)//使用流程定义ID查询
//						.processInstanceId(processInstanceId)//使用流程实例ID查询
//						.executionId(executionId)//使用执行对象ID查询
                /**排序*/
                .orderByTaskCreateTime().asc()//使用创建时间的升序排列
                /**返回结果集*/
//						.singleResult()//返回惟一结果集
//						.count()//返回结果集的数量
//						.listPage(firstResult, maxResults);//分页查询
                .list();//返回列表
        PrintUtils.printTask(list);
    }

    /**
     * 完成我的任务
     */
    @Test
    public void completeMyPersonalTask() {
        String[] arr = {"117509","117507","117511"};
        for (int i = 0; i < arr.length; i++) {

            //任务ID50002
            String taskId = arr[i];
            //完成任务的同时，设置流程变量，使用流程变量用来指定完成任务后，下一个连线，对应exclusiveGateWay.bpmn文件中${money>1000}
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("date", 2);
            //与正在执行的任务管理相关的Service
//            processEngine.getTaskService().complete(taskId, variables);
            processEngine.getTaskService().complete(taskId);

            System.out.println("完成任务：任务ID：" + taskId);
        }
    }
}
