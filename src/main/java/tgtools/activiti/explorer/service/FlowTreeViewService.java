package tgtools.activiti.explorer.service;


import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.UserTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgtools.activiti.explorer.service.listen.IFlowTreeListener;
import tgtools.activiti.explorer.service.listen.event.FlowTreeCompletedEvent;
import tgtools.activiti.explorer.service.listen.event.FlowTreeFilterNodeEvent;
import tgtools.activiti.explorer.util.BpmnModelParser;
import tgtools.exceptions.APPErrorException;
import tgtools.interfaces.IDispose;
import tgtools.web.entity.TreeNode;
import tgtools.web.util.TreeNodeParser;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 名  称：
 * 编写者：田径
 * 功  能：
 * 时  间：16:17
 */
@Service
public class FlowTreeViewService implements IDispose,Closeable {
    private String m_FlowID=null;

    FlowService mFlowService =new FlowService();
    private IFlowTreeListener m_TreeListener;

    /**
     * 添加监听
     * @param p_TreeListener
     */
    public void setTreeListener(IFlowTreeListener p_TreeListener) {
        m_TreeListener = p_TreeListener;
    }
    protected void onNodeCompleted(FlowTreeCompletedEvent p_Event)
    {
        if(null!=m_TreeListener)
        {
            m_TreeListener.nodeCompleted(p_Event);
        }
    }
    protected void onFilterNode(FlowTreeFilterNodeEvent p_Event)
    {
        if(null!=m_TreeListener)
        {
            m_TreeListener.nodeFilter(p_Event);
        }
    }
    /**
     * 获取流程树
     * @param p_flowid
     * @param p_Order
     * @param p_Group
     * @return
     * @throws APPErrorException
     */
    public String getTree(String pRootName, String p_flowid ,
                          String p_Order, String p_Group) throws APPErrorException {
        m_FlowID = p_flowid;
        return getRoot(pRootName,p_flowid ,
                p_Order,p_Group);
    }

    /**
     * 获取流程功能分组节点
     * @param index
     * @param p_Node
     * @param group
     * @param name
     * @param p_id
     * @throws APPErrorException
     */
    public void getFlowGroup(String index,
                             TreeNode p_Node, String group , String name, String p_id) throws APPErrorException {

        TreeNode node =new TreeNode();
        node.setId("1");
        node.setName("草稿");
        node.setIsLeaf(true);
        node.setType(TreeNode.TYPE_GRID);
        node.setOrder(index);
        node.setPid(p_id);

        FlowTreeFilterNodeEvent event =new FlowTreeFilterNodeEvent(m_FlowID,node,p_Node,null,null,null,null);
        onFilterNode(event);
        if(!event.getCancel()) {
            p_Node.getChildren().add(node);
        }

        TreeNode node1=new TreeNode();
        node1.setId("2");
        node1.setName("已发送");
        node1.setIsLeaf(true);
        node1.setPid(p_id);
        node1.setType(TreeNode.TYPE_GRID);
        node1.setOrder(index);
        event =new FlowTreeFilterNodeEvent(m_FlowID,node1,p_Node,null,null,null,null);
        onFilterNode(event);
        if(!event.getCancel()) {
            p_Node.getChildren().add(node1);
        }
    }

    /**
     * 获取流程所有节点
     * @param pRootName
     * @param pFlowid
     * @param pOrder
     * @param pGroup
     * @return
     * @throws APPErrorException
     */
    public String getRoot(String pRootName, String pFlowid ,
                          String pOrder, String pGroup) throws APPErrorException {
        String name = pRootName;
        TreeNode node=new TreeNode();
        node.setId("base");
        node.setName(name);
        node.setIsLeaf(false);
        node.setType(TreeNode.TYPE_NONE);
        node.setPid("-1");
        node.setFilter("");
        node.setOrder("-1");
        node.setImg("");
        node.setIsgroup("false");
        node.setExpanded(true);

        FlowTreeFilterNodeEvent event =new FlowTreeFilterNodeEvent(m_FlowID,node,null,null,null,null,null);
        onFilterNode(event);

        node.setChildren(getFlowUserTaskNode(pFlowid,pOrder,"base",pGroup,name));

        FlowTreeCompletedEvent comevent=new FlowTreeCompletedEvent(m_FlowID,node);
        onNodeCompleted(comevent);


        String root =TreeNodeParser.createNode(node);

        return "[" + root + "]";
    }

    /**
     * 获取流程所有任务节点
     * @param flowid
     * @param order
     * @param pid
     * @param group
     * @param name
     * @return
     * @throws APPErrorException
     */
    public List<TreeNode> getFlowUserTaskNode(String flowid,
                                              String order, String pid, String group , String name) throws APPErrorException {
        BpmnModel model=mFlowService.getBpmn(flowid);
        BpmnModelParser parse= BpmnModelParser.getInstance(model);
        List<TreeNode> nodes=new ArrayList<TreeNode>();

        List<UserTask> usertasks=parse.getUserTasks();
        for(int i=0,count=usertasks.size();i<count;i++)
        {

            TreeNode node =new TreeNode();
            node.setId(usertasks.get(i).getId());
            node.setName(usertasks.get(i).getName());

            node.setIsLeaf(false);
            node.setPid(pid);
            node.setExpanded(true);
            node.setType(TreeNode.TYPE_NONE);
            FlowTreeFilterNodeEvent event =new FlowTreeFilterNodeEvent(m_FlowID,node,null,node.getId(),usertasks.get(i).getAssignee(),usertasks.get(i).getCandidateUsers(),usertasks.get(i).getCandidateGroups());
            onFilterNode(event);
            if(!event.getCancel()) {
                nodes.add(node);
                getFlowGroup(String.valueOf(i),node,group,name,node.getId());
            }
        }

        TreeNode node =new TreeNode();
        node.setId(String.valueOf(usertasks.size()));
        node.setName("归档");
        node.setIsLeaf(true);
        node.setExpanded(true);
        node.setType(TreeNode.TYPE_GRID);
        FlowTreeFilterNodeEvent event =new FlowTreeFilterNodeEvent(m_FlowID,node,null,null,null,null,null);
        onFilterNode(event);
        if(!event.getCancel()) {
            nodes.add(node);
        }

        TreeNode node1 =new TreeNode();
        node1.setId(String.valueOf(usertasks.size()+1));
        node1.setName("作废");
        node1.setIsLeaf(true);
        node1.setExpanded(true);
        node1.setType(TreeNode.TYPE_GRID);
        event =new FlowTreeFilterNodeEvent(m_FlowID,node1,null,null,null,null,null);
        onFilterNode(event);
        if(!event.getCancel()) {
            nodes.add(node1);
        }

        return nodes;

    }

    @Override
    public void Dispose() {

    }

    @Override
    public void close() throws IOException {

    }
}
