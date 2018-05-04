package tgtools.activiti.explorer.dao;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
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
    @Results({ @Result(property = "mName", column = "NAME_") ,
            @Result(property = "mLast", column = "LAST_"),
            @Result(property = "mDescription", column = "DESCRIPTION_") ,
            @Result(property = "mStartTime", column = "START_TIME_"),
            @Result(property = "mEndTime", column = "END_TIME_")})
    List<FlowLogVo> getFlowLog(String pProcInstId);
}
