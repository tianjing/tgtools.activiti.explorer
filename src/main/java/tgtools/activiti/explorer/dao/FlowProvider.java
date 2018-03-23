package tgtools.activiti.explorer.dao;

import tgtools.activiti.explorer.config.Constants;
import tgtools.util.StringUtil;
import tgtools.web.util.PageSqlUtil;

/**
 * @author 田径
 * @Title
 * @Description
 * @date 19:06
 */
public class FlowProvider {

    public String getFlowLog(String pProcInstId){
        String sql = Constants.SQLs.View_GetFlowLog_SQL;
        sql = StringUtil.replace(sql, "${procid}", pProcInstId);
        return PageSqlUtil.getPageDataSQL(sql, String.valueOf(1),  String.valueOf(Integer.MAX_VALUE));
    }
}
