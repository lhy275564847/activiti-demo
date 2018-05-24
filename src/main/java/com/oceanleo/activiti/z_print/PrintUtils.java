package com.oceanleo.activiti.z_print;

import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author haiyang.li
 */
public class PrintUtils {

    public static void printTask(List<Task> taskList) {
        for (Task task : taskList) {
            System.out.println("任务ID:" + task.getId());
            System.out.println("任务名称:" + task.getName());
            System.out.println("任务的创建时间:" + task.getCreateTime());
            System.out.println("任务的办理人:" + task.getAssignee());
            System.out.println("流程实例ID：" + task.getProcessInstanceId());
            System.out.println("执行对象ID:" + task.getExecutionId());
            System.out.println("流程定义ID:" + task.getProcessDefinitionId());
            System.out.println("########################################################");
        }
    }

    public static void printProcessDefinition(List<ProcessDefinition> processDefinitionList) {
        for (ProcessDefinition processDefinition : processDefinitionList) {
            System.out.println("流程定义ID:" + processDefinition.getId());                 //流程定义的key+版本+随机生成数
            System.out.println("流程定义的名称:" + processDefinition.getName());           //对应helloworld.bpmn文件中的name属性值
            System.out.println("流程定义的key:" + processDefinition.getKey());             //对应helloworld.bpmn文件中的id属性值
            System.out.println("流程定义的版本:" + processDefinition.getVersion());        //当流程定义的key值相同的相同下，版本升级，默认1
            System.out.println("资源名称bpmn文件:" + processDefinition.getResourceName());
            System.out.println("资源名称png文件:" + processDefinition.getDiagramResourceName());
            System.out.println("部署对象ID：" + processDefinition.getDeploymentId());
            System.out.println("#########################################################");
        }
    }

    public static void printHistoricTaskInstance(List<HistoricTaskInstance> historicTaskInstanceList) {
        for (HistoricTaskInstance historicTaskInstance : historicTaskInstanceList) {
            System.out.println("Id:" + historicTaskInstance.getId());
            System.out.println("Name:" + historicTaskInstance.getName());
            System.out.println("ProcessInstanceId:" + historicTaskInstance.getProcessInstanceId());
            System.out.println("StartTime:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(historicTaskInstance.getStartTime()));
            System.out.println("EndTime:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(historicTaskInstance.getEndTime()));
            System.out.println("DurationInMillis:" + historicTaskInstance.getDurationInMillis());
            System.out.println("DeleteReason:" + historicTaskInstance.getDeleteReason());
            System.out.println("Assignee:" + historicTaskInstance.getAssignee());
            System.out.println("Category:" + historicTaskInstance.getCategory());
            System.out.println("Description:" + historicTaskInstance.getDescription());
            System.out.println("ExecutionId:" + historicTaskInstance.getExecutionId());
            System.out.println("FormKey:" + historicTaskInstance.getFormKey());
            System.out.println("Owner:" + historicTaskInstance.getOwner());
            System.out.println("ParentTaskId:" + historicTaskInstance.getParentTaskId());
            System.out.println("ProcessDefinitionId:" + historicTaskInstance.getProcessDefinitionId());
            System.out.println("ProcessInstanceId:" + historicTaskInstance.getProcessInstanceId());
            System.out.println("TaskDefinitionKey:" + historicTaskInstance.getTaskDefinitionKey());
            System.out.println("TenantId:" + historicTaskInstance.getTenantId());
//                System.out.println(hti.getId() + "    " + hti.getName() + "    " + hti.getProcessInstanceId() + "   " + hti.getStartTime() + "   " + hti.getEndTime() + "   " + hti.getDurationInMillis());
            System.out.println("################################");
        }
    }
}
