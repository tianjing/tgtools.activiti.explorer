package tgtools.activiti.explorer.service.listen.event;

import tgtools.interfaces.Event;
import tgtools.web.entity.TreeNode;

import java.util.List;

/**
 * 名  称：
 * 编写者：田径
 * 功  能：
 * 时  间：12:07
 */
public class FlowTreeFilterNodeEvent extends Event {
    private String m_FlowID;
    private TreeNode m_Node;
    private TreeNode m_ParentNode;
    private String m_TaskDefID;
    private String m_Assignee;
    private List<String> m_CandidateUsers;
    private List<String> m_CandidateGroups;

    private boolean m_Cancel = false;

    public FlowTreeFilterNodeEvent() {
    }

    public FlowTreeFilterNodeEvent(String p_FlowID, TreeNode p_Node, TreeNode p_ParentNode,
                                   String p_TaskDefID, String p_Assignee, List<String> p_CandidateUsers, List<String> p_CandidateGroups) {
        m_FlowID=p_FlowID;
        m_Node = p_Node;
        m_ParentNode=p_ParentNode;
        m_TaskDefID = p_TaskDefID;
        m_Assignee = p_Assignee;
        m_CandidateUsers = p_CandidateUsers;
        m_CandidateGroups = p_CandidateGroups;
    }

    public TreeNode getParentNode() {
        return m_ParentNode;
    }

    /**
     * 获取流程ID
     * @return
     */
    public String getFlowID() {
        return m_FlowID;
    }


    /**
     * 获取当前节点
     * @return
     */
    public TreeNode getNode() {
        return m_Node;
    }

    /**
     * 作废 请使用 getTaskDefID()
     * 获取TaskID  如果为null 则当前节点不是TASK 节点
     * @return
     */
    @Deprecated
    public String getTaskID() {
        return m_TaskDefID;
    }

    /**
     * 获取TaskDefID 如果为null 则当前节点不是TASK 节点
     * @return
     */
    public String getTaskDefID()
    {
        return m_TaskDefID;
    }
    /**
     * 获取 模型图中 task 权限的 Assignee
     * @return
     */
    public String getAssignee() {
        return m_Assignee;
    }

    /**
     * 获取 模型图中 task 权限的 CandidateUsers
     * @return
     */
    public List<String> getCandidateUsers() {
        return m_CandidateUsers;
    }

    /**
     * 获取 模型图中 task 权限的 CandidateGroups
     * @return
     */
    public List<String> getCandidateGroups() {
        return m_CandidateGroups;
    }

    /**
     * 获取 是否取消当前节点 （如果为true 则当前节点无效）
     * @return
     */
    public boolean getCancel() {
        return m_Cancel;
    }

    /**
     * 获取 是否取消当前节点 （如果为true 则当前节点无效）
     * @param p_Cancel
     */
    public void setCancel(boolean p_Cancel) {
        m_Cancel = p_Cancel;
    }
}
