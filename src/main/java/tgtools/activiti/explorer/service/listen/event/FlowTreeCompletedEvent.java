package tgtools.activiti.explorer.service.listen.event;

import tgtools.interfaces.Event;
import tgtools.web.entity.TreeNode;

/**
 * 名  称：
 * 编写者：田径
 * 功  能：
 * 时  间：14:13
 */
public class FlowTreeCompletedEvent extends Event {
    private String m_FlowID;
    private TreeNode m_Node;

    public FlowTreeCompletedEvent(){}

    public FlowTreeCompletedEvent(String p_FlowID, TreeNode p_Node) {
        m_FlowID=p_FlowID;
        m_Node=p_Node;
    }

    /**
     * 获取流程ID
     * @return
     */
    public String getFlowID() {
        return m_FlowID;
    }

    /**
     * 获取根节点
     * @return
     */
    public TreeNode getNode() {
        return m_Node;
    }

}
