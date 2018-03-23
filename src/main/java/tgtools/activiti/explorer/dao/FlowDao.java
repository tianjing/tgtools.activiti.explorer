package tgtools.activiti.explorer.dao;

import org.apache.ibatis.annotations.SelectProvider;
import tgtools.activiti.explorer.entity.FlowLogVo;

import java.util.List;

/**
 * @author 田径
 * @Title
 * @Description
 * @date 19:02
 */
public interface FlowDao {

    @SelectProvider(type=FlowProvider.class,method = "getFlowLog")
    List<FlowLogVo> getFlowLog(String pProcInstId);
}
