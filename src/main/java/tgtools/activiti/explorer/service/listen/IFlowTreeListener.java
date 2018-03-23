package tgtools.activiti.explorer.service.listen;


import tgtools.activiti.explorer.service.listen.event.FlowTreeCompletedEvent;
import tgtools.activiti.explorer.service.listen.event.FlowTreeFilterNodeEvent;

/**
 * 名  称：
 * 编写者：田径
 * 功  能：
 * 时  间：12:06
 */
public interface IFlowTreeListener {

    /**
     * 节点过滤
     * 添加一个节点时的事件
     * @param p_Event
     */
    void nodeFilter(FlowTreeFilterNodeEvent p_Event);

    /**
     * 节点生成完成时事件
     * @param p_Event
     */
    void nodeCompleted(FlowTreeCompletedEvent p_Event);
}
