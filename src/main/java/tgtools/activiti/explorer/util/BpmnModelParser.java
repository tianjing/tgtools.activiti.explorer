package tgtools.activiti.explorer.util;

import org.activiti.bpmn.model.*;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.runtime.Execution;
import tgtools.exceptions.APPErrorException;
import tgtools.util.LogHelper;
import tgtools.util.StringUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by tian_ on 2016-09-12.
 */
public class BpmnModelParser {

    private BpmnModel m_Model;
    private StartEvent m_StartEvent;
    private List<UserTask> m_UserTasks;
    private EndEvent m_EndEvent;
    private List<FlowElement> m_FlowElements;
    private Process m_Process;

    private BpmnModelParser(BpmnModel p_Model) {
        m_Model = p_Model;
        m_UserTasks = new ArrayList<UserTask>();
        m_FlowElements=new ArrayList<FlowElement>();
    }
    public boolean isFirstUserTask(String p_TaskDefID)
    {
        return getUserTask(0).getId().equals(p_TaskDefID);
    }
    public Process getProcess() {
        return m_Process;
    }
    public String getProcessID()
    {
        if(null!=m_Process)
        {
            return m_Process.getId();
        }
        return StringUtil.EMPTY_STRING;
    }
    public BpmnModel getModel() {
        return m_Model;
    }

    public StartEvent getStartEvent() {
        return m_StartEvent;
    }

    public List<UserTask> getUserTasks() {
        return m_UserTasks;
    }

    public EndEvent getEndEvent() {
        return m_EndEvent;
    }

    public List<FlowElement> getFlowElements() {
        return m_FlowElements;
    }
    public UserTask getUserTask(String p_TaskDefID)
    {
        for(int i=0;i<m_UserTasks.size();i++)
        {
            if(m_UserTasks.get(i).getId().equals(p_TaskDefID))
            {
                return m_UserTasks.get(i);
            }
        }
        return null;
    }
    /**
     * 获取UserTask表单属性集合
     * @param p_TaskDefID usertask task定义ID
     * @return
     */
    public List<FormProperty> getUserTaskFormPropertys(String p_TaskDefID)
    {
        return getUserTask(p_TaskDefID).getFormProperties();
    }

    /**
     * 获取UserTask表单属性名称集合
     * @param p_TaskDefID task定义ID
     * @return
     */
    public List<String> getUserTaskFormPropertyNames(String p_TaskDefID)
    {
        List<String> result=new ArrayList<String>();
        List<FormProperty> list=getUserTaskFormPropertys(p_TaskDefID);
        for(int i=0,count=list.size();i<count;i++)
        {
            result.add(list.get(i).getName());
        }
        return result;
    }
    public String getPreActID(String p_TaskID)
    {
       org.activiti.engine.task.Task task= ProcessEngines.getDefaultProcessEngine().getTaskService().createTaskQuery().taskId(p_TaskID).singleResult();
        Execution ex= ProcessEngines.getDefaultProcessEngine().getRuntimeService().createExecutionQuery().executionId(task.getExecutionId()).singleResult();
       List<HistoricActivityInstance> acts= ProcessEngines.getDefaultProcessEngine().getHistoryService().createHistoricActivityInstanceQuery().executionId(ex.getParentId()).orderByHistoricActivityInstanceStartTime().desc().list();
        if(acts.size()>0)
        {
            return acts.get(0).getActivityId();
        }
        return StringUtil.EMPTY_STRING;
    }

    /**
     * 下一步是否有分支
     * @param p_TaskDefID task定义ID
     * @return
     */
    public boolean nextIsBranch(String p_TaskDefID)
    {
        FlowNode flowele= (FlowNode)getUserTask(p_TaskDefID);
            for(int i=0;i<flowele.getOutgoingFlows().size();i++)
            {
               String id= flowele.getOutgoingFlows().get(i).getTargetRef();
                if(getElementByActivityID(id) instanceof ExclusiveGateway)
                {
                    return true;
                }
        }
        return false;
    }

    /**
     * 是否包含网关
     * @param p_FlowElement
     * @return
     */
    public boolean isInclusiveGateway(FlowElement p_FlowElement)
    {
        return p_FlowElement instanceof InclusiveGateway;
    }

    /**
     * 是否是并行网关
     * @param p_FlowElement
     * @return
     */
    public boolean isParallelGateway(FlowElement p_FlowElement)
    {
        return p_FlowElement instanceof ParallelGateway;
    }

    /**
     * 获取任务元素
     * @param p_TaskDefID
     * @return
     */
    public FlowElement getTaskElement(String p_TaskDefID)
    {
        return getUserTask(p_TaskDefID);
    }

    public FlowElement getElementByActivityID(String p_ActivityID)
    {
        for(int i=0;i<m_FlowElements.size();i++)
        {
            if(m_FlowElements.get(i).getId().equals(p_ActivityID))
            {
                return m_FlowElements.get(i);
            }
        }
        return null;
    }
    /**
     * 获取上一个元素
     * @param p_TaskDefID
     * @return
     */
    public FlowElement getPreElement(String p_TaskDefID)
    {
        List<FlowElement> nodes= getPreElements(p_TaskDefID);
        if(nodes.size()>0)
        {
            return nodes.get(0);
        }
        return null;
    }

    /**
     * 获取上个所有元素
     * @param p_TaskDefID
     * @return
     */
    public List<FlowElement> getPreElements(String p_TaskDefID)
    {
        FlowElement flowele= getTaskElement(p_TaskDefID);
        List<FlowElement> result=new ArrayList<FlowElement>();
        if(flowele instanceof FlowNode)
        {
            FlowNode node =(FlowNode)flowele;
            for(int i=0;i<node.getIncomingFlows().size();i++){
                FlowElement ele= getElementByActivityID(node.getIncomingFlows().get(i).getSourceRef());
                if(null!=ele){
                    result.add(ele);
                }
            }

        }
        return result;

        //return  getElementByActivityID(getPreActID(p_TaskID));
    }

    /**
     * 获取下一个元素
     * @param p_TaskDefID
     * @return
     */
    public FlowElement getNextElement(String p_TaskDefID)
    {
        FlowElement flowele= getTaskElement(p_TaskDefID);
        return getNextElement(flowele);
    }
    public FlowElement getStartElement()
    {
        for(int i=0,count=m_FlowElements.size();i<count;i++)
        {
            if(m_FlowElements.get(i) instanceof org.activiti.bpmn.model.StartEvent)
            {
                return m_FlowElements.get(i);
            }
        }
        return null;
    }
    public org.activiti.bpmn.model.UserTask getFirstTask()
    {
        FlowElement ele= getStartElement();
        List<FlowElement> eles= getNextElements(ele);
        for(int i=0,count=eles.size();i<count;i++) {
            if (eles.get(i) instanceof org.activiti.bpmn.model.UserTask)
            {
                return (org.activiti.bpmn.model.UserTask)eles.get(i);
            }
        }
        return null;
    }
    /**
     * 获取下一个元素  如果存在多个则选择第一个
     * @param p_FlowElement
     * @return
     */
    public FlowElement getNextElement(FlowElement p_FlowElement)
    {
        List<FlowElement> eles= getNextElements(p_FlowElement);
        if(eles.size()>0)
        {
            return eles.get(0);
        }
        return null;
    }
    public List<FlowElement> getNextElements(FlowElement p_FlowElement)
    {
        List<FlowElement> result=new ArrayList<FlowElement>();
        if(p_FlowElement instanceof FlowNode)
        {
            FlowNode node =(FlowNode)p_FlowElement;
            for(int i=0;i<node.getOutgoingFlows().size();i++){
                FlowElement ele= getElementByActivityID(node.getOutgoingFlows().get(i).getTargetRef());
                if(null!=ele){
                    result.add(ele);
                }
            }

        }
        return result;
    }

    /**
     * 获取第一个任务的角色
     * @return
     */
    public List<String> getCandidateGroupsByFirstTask()
    {
        org.activiti.bpmn.model.UserTask task= getFirstTask();
        if(null!=task)
        {
            return task.getCandidateGroups();
        }
        return new ArrayList<String>();
    }

    /**
     * 获取第一个任务的用户
     * @return
     * @throws APPErrorException
     */
    public List<String> getCandidateUsersByFirstTask() throws APPErrorException {
        org.activiti.bpmn.model.UserTask task=getFirstTask();
        if(null!=task) {
            return task.getCandidateUsers();
        }
        return new ArrayList<String>();
    }
        /**
         * 获取UserTask的所有角色
         * @param p_TaskDefID task定义ID
         * @return
         */
    public List<String> getCandidateGroups(String p_TaskDefID)
    {
        return getUserTask(p_TaskDefID).getCandidateGroups();
    }
    /**
     * 获取UserTask的所有用户
     * @param p_TaskDefID task定义ID
     * @return
     */
    public List<String> getCandidateUsers(String p_TaskDefID) throws APPErrorException {
        UserTask task=getUserTask(p_TaskDefID);
        if(null==task)
        {
            throw new APPErrorException("找不到UserTask,请检查流程图中的TaskDefID:"+p_TaskDefID);
        }
        return getUserTask(p_TaskDefID).getCandidateUsers();
    }



    public UserTask getUserTask(int p_Index)
    {
        return m_UserTasks.get(p_Index);
    }

    /**
     * 获取UserTask表单属性集合
     * @param p_Index usertask 的索引 从0开始
     * @return
     */
    public List<FormProperty> getUserTaskFormPropertys(int p_Index)
    {
        return m_UserTasks.get(p_Index).getFormProperties();
    }

    /**
     * 获取UserTask表单属性名称集合
     * @param p_Index usertask 的索引 从0开始
     * @return
     */
    public List<String> getUserTaskFormPropertyNames(int p_Index)
    {
        List<String> result=new ArrayList<String>();
        List<FormProperty> list=getUserTaskFormPropertys(p_Index);
        for(int i=0,count=list.size();i<count;i++)
        {
            result.add(list.get(i).getName());
        }
        return result;
    }
    /**
     * 获取UserTask的所有角色
     * @param p_Index usertask 的索引 从0开始
     * @return
     */
    public List<String> getCandidateGroups(int p_Index)
    {
        return getUserTask(p_Index).getCandidateGroups();
    }
    /**
     * 获取UserTask的所有用户
     * @param p_Index usertask 的索引 从0开始
     * @return
     */
    public List<String> getCandidateUsers(int p_Index)
    {
        return getUserTask(p_Index).getCandidateUsers();
    }



    private void parse() throws APPErrorException {
        try {
            List<Process> list = m_Model.getProcesses();
            System.out.println("Process size:"+list.size());

            for (int i = 0, count = list.size(); i < count; i++) {
                Process process = list.get(i);
                if (process.getFlowElements().isEmpty() && process.getLanes().isEmpty()) {
                    continue;
                }
                m_Process=process;
                Iterator flows = process.getFlowElements().iterator();

                while (flows.hasNext()) {
                    FlowElement artifact = (FlowElement) flows.next();

                    m_FlowElements.add(artifact);
                    LogHelper.info("","add ID:"+artifact.getId()+";String:"+artifact.getClass().toString(),"parse");
                    if (artifact instanceof org.activiti.bpmn.model.StartEvent) {


                        m_StartEvent = (org.activiti.bpmn.model.StartEvent) artifact;

                    } else if (artifact instanceof org.activiti.bpmn.model.UserTask) {
                        org.activiti.bpmn.model.UserTask converter = (org.activiti.bpmn.model.UserTask) artifact;


                        m_UserTasks.add(converter);

                    } //else if (artifact instanceof org.activiti.bpmn.model.ExclusiveGateway) {
                    //    org.activiti.bpmn.model.ExclusiveGateway converter = (org.activiti.bpmn.model.ExclusiveGateway) artifact;
                    //}else if (artifact instanceof org.activiti.bpmn.model.InclusiveGateway) {
                   //     org.activiti.bpmn.model.InclusiveGateway converter = (org.activiti.bpmn.model.InclusiveGateway) artifact;
                   // }else if (artifact instanceof org.activiti.bpmn.model.ParallelGateway) {
                   //     org.activiti.bpmn.model.ParallelGateway converter = (org.activiti.bpmn.model.ParallelGateway) artifact;
                   // }EventGateway


                    else if (artifact instanceof org.activiti.bpmn.model.EndEvent) {
                        m_EndEvent = (org.activiti.bpmn.model.EndEvent) artifact;
                    }
                }


            }
        } catch (Exception var12) {
            throw new APPErrorException("错误的模型，无法解析", var12);
        }


    }


    public static BpmnModelParser getInstance(BpmnModel p_Model) throws APPErrorException {
        if (null == p_Model) {
            throw new APPErrorException("无效的Model");
        }

        BpmnModelParser model = new BpmnModelParser(p_Model);
        model.parse();
        return model;
    }
    public static BpmnModelParser getInstance(String p_FlowID) throws APPErrorException {
        if( StringUtil.isNullOrEmpty(p_FlowID))
        {
            throw new APPErrorException("无效的流程ID");
        }
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        return getInstance(processEngine.getRepositoryService().getBpmnModel(p_FlowID));
    }
}
