package tgtools.activiti.explorer.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tgtools.activiti.explorer.config.Constants;
import tgtools.activiti.explorer.dao.FlowDao;
import tgtools.activiti.explorer.entity.FlowLogVo;
import tgtools.exceptions.APPErrorException;
import tgtools.util.StringUtil;
import tgtools.web.entity.BSGridDataEntity;
import tgtools.web.util.PageSqlUtil;

import java.util.List;


/**
 * Created by tian_ on 2016-09-07.
 */
@Service
public class FlowViewService {
    @Autowired
    protected FlowDao mFlowDao;
    /**
     * 获取流程日志
     *
     * @param pProcInstId 流程实例ID
     * @return
     */
    public List<FlowLogVo> getFlowLog(String pProcInstId) throws APPErrorException {
        return mFlowDao.getFlowLog(pProcInstId);
    }








}
