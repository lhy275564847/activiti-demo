package com.oceanleo.activiti.q_cont_test;

import com.alibaba.fastjson.JSON;
import com.oceanleo.activiti.z_print.PrintUtils;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.*;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContTestTest {

    private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

    /**
     * 部署流程定义（从inputStream）
     */
    @Test
    public void deploymentProcessDefinition_inputStream() {
        InputStream inputStreamBpmn = this.getClass().getResourceAsStream("Node.bpmn");
//        InputStream inputStreamPng = this.getClass().getResourceAsStream("inclusiveGateway.png");
        Deployment deployment = processEngine.getRepositoryService()//与流程定义和部署对象相关的Service
                .createDeployment()//创建一个部署对象
//                .name("节点")//添加部署的名称
                .addInputStream("Node.bpmn20.xml", inputStreamBpmn)         //
//                .addInputStream("inclusiveGateway.png", inputStreamPng)           //
                .deploy();                                                  //完成部署
        System.out.println("部署ID：" + deployment.getId());                //
        System.out.println("部署名称：" + deployment.getName());            //
//        processEngine.getRepositoryService().deleteDeployment("27501", true);
    }

    @Test
    public void node() {
//        InputStream resouceStream = this.getClass().getResourceAsStream("Node.bpmn");
        ProcessDefinition node = processEngine.getRepositoryService().createProcessDefinitionQuery().processDefinitionKey("Node").latestVersion().singleResult();
//        ProcessDefinition node = processEngine.getRepositoryService().createProcessDefinitionQuery().processDefinitionKey("Node").singleResult();
        InputStream resouceStream = processEngine.getRepositoryService().getResourceAsStream(node.getDeploymentId(), node.getResourceName());
        XMLInputFactory xif = XMLInputFactory.newInstance();
        InputStreamReader in = null;
        XMLStreamReader xtr = null;
        try {
            in = new InputStreamReader(resouceStream, "UTF-8");
            xtr = xif.createXMLStreamReader(in);
            BpmnModel model = new BpmnXMLConverter().convertToBpmnModel(xtr);
            Collection<FlowElement> flowElements = model.getMainProcess().getFlowElements();
            for (FlowElement e : flowElements) {
                if (e instanceof UserTask) {
                    List<SequenceFlow> incomingFlows = ((UserTask) e).getIncomingFlows();
                    System.out.println("flowelement id:" + e.getId() + "\tname:" + e.getName() + "\t" + e.getClass().toString() + "\t\t\t\tdoc:" + e.getDocumentation());
                } else if (e instanceof Gateway) {
                    List<SequenceFlow> incomingFlows = ((Gateway) e).getIncomingFlows();
                    System.out.println("flowelement id:" + e.getId() + "\tname:" + e.getName() + "\t" + e.getClass().toString());
                } else {
                    System.out.println("flowelement id:" + e.getId() + "\tname:" + e.getName() + "\t" + e.getClass().toString());
                }
            }
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            try {
                if (xtr != null) {
                    xtr.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (XMLStreamException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 启动流程实例
     */
    @Test
    public void startProcessInstance() {
        //流程定义的key
        String processDefinitionKey = "myProcess_";
        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("flag", 4);
        ProcessInstance pi = processEngine.getRuntimeService().startProcessInstanceByKey(processDefinitionKey, map);
        System.out.println("流程实例ID:" + pi.getId());
        System.out.println("流程定义ID:" + pi.getProcessDefinitionId());

        List<Task> list = processEngine.getTaskService().createTaskQuery().orderByTaskCreateTime().asc().list();
        PrintUtils.printTask(list);
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
//        String[] arr = {"122504"};
//        for (int i = 0; i < arr.length; i++) {

        //任务ID50002
//            String taskId = arr[i];
        String taskId = "192506";
        //完成任务的同时，设置流程变量，使用流程变量用来指定完成任务后，下一个连线，对应exclusiveGateWay.bpmn文件中${money>1000}
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("flag", 1);
        variables.put("type", 1);
        //与正在执行的任务管理相关的Service
//            processEngine.getTaskService().complete(taskId, variables);
        processEngine.getTaskService().complete(taskId, variables);

        System.out.println("完成任务：任务ID：" + taskId);
        List<Task> list = processEngine.getTaskService().createTaskQuery().orderByTaskCreateTime().asc().list();
        PrintUtils.printTask(list);
//        }
    }
}
