package com.oceanleo.spring;

import com.oceanleo.activiti.z_print.PrintUtils;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.InputStream;
import java.util.List;

/**
 * @author haiyang.li
 */
@ContextConfiguration(locations = {"classpath*:spring/spring-*.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class SpringTest {

    private String key = "Spring";

    @Autowired
    private TaskService taskService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private ManagementService managementService;
    @Autowired
    private RepositoryService repositoryService;

    //部署
    @Test
    public void deployment() {
        InputStream inputStream = this.getClass().getResourceAsStream("/Spring.bpmn");
        Deployment deployment = repositoryService.createDeployment().addInputStream("Spring.bpmn20.xml", inputStream).deploy();
        System.out.println("部署ID：" + deployment.getId());
        System.out.println("部署名称：" + deployment.getName());
        start();
        task();
    }

    //启动
    @Test
    public void start() {
        runtimeService.startProcessInstanceByKey(key);
    }

    //任务查询
    @Test
    public void task() {
        List<Task> taskList = taskService.createTaskQuery().list();
        PrintUtils.printTask(taskList);
    }

    //提交
    @Test
    public void complete() {
        taskService.complete("55003");
        task();
    }

    //流程挂起
    @Test
    public void suspend() {
        repositoryService.suspendProcessDefinitionByKey(key);
    }

    //挂起流程激活
    @Test
    public void activate() {
        repositoryService.activateProcessDefinitionByKey(key);
    }

    //查询
    @Test
    public void query() {
//        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().orderByHistoricActivityInstanceId().asc().list();
//        PrintUtils.printHistoricTaskInstance(list);

//        List<Task> list = taskService.createTaskQuery().processDefinitionKey(key).list();
//        PrintUtils.printTask(list);

        List<Task> list = taskService.createNativeTaskQuery().sql("SELECT * FROM " + managementService.getTableName(Task.class) + " T WHERE T.ID_ = #{ID}").parameter("ID", "60004").list();
        PrintUtils.printTask(list);

    }
}
