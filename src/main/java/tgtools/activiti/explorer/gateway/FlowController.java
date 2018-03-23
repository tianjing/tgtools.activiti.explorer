package tgtools.activiti.explorer.gateway;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tgtools.activiti.explorer.entity.ProcessVO;
import tgtools.activiti.explorer.service.FlowService;
import tgtools.util.StringUtil;
import tgtools.web.entity.GridData;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 田径
 * @Title
 * @Description
 * @date 12:33
 */
@RequestMapping("/activiti/explorer/manage/flow")
public class FlowController {

    @Autowired
    FlowService mFlowService;

    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    @ResponseBody
    public GridData list(@RequestParam("pageIndex") int pIndex, @RequestParam("pageSize") int pPageSize) throws IOException {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        long count = processEngine.getRepositoryService().createProcessDefinitionQuery().count();
        List<ProcessDefinition> prodefs = processEngine.getRepositoryService().createProcessDefinitionQuery().orderByProcessDefinitionName().asc().orderByProcessDefinitionVersion().asc().listPage((pIndex * pPageSize), pPageSize);
        ArrayList<ProcessVO> datas = new ArrayList<ProcessVO>();
        for (int i = 0; i < prodefs.size(); i++) {
            datas.add(new ProcessVO(prodefs.get(i)));
        }
        GridData entity = new GridData();
        entity.setTotalRows((int) count);
        entity.setCurPage(1);
        entity.setData(datas);

        return entity;
    }

    @RequestMapping(value = "/suspension/{id}", method = {RequestMethod.POST})
    @ResponseBody
    public ResponseCode suspension(@PathVariable("id") String processDefinitionId) {
        mFlowService.suspensionFlow(processDefinitionId);
        return ResponseCode.ok();
    }

    @RequestMapping(value = "/activate/{id}", method = {RequestMethod.POST})
    @ResponseBody
    public ResponseCode activate(@PathVariable("id") String processDefinitionId) {
        mFlowService.activateFlow(processDefinitionId);
        return ResponseCode.ok();
    }

    /**
     * 获取已发布流程xml
     *
     * @param pDeployid
     * @param pResponse
     */
    @RequestMapping(value = "/bpmn/xml", method = {RequestMethod.GET})
    public void getBpmnXml(@RequestParam("deployid") String pDeployid, HttpServletResponse pResponse) {
        try {
            pResponse.setContentType("text/xml;charset=UTF-8");
            byte[] data = mFlowService.getFlowbpmn(pDeployid, "xml");
            pResponse.getOutputStream().write(data);
            pResponse.flushBuffer();
        } catch (Exception e) {
            try {
                byte[] error = ("获取出错；原因：" + e.getMessage()).getBytes("UTF-8");
                pResponse.getOutputStream().write(error);
                pResponse.setContentType("image/png");
                pResponse.setHeader("Accept-Ranges", "bytes");
                pResponse.flushBuffer();
            } catch (IOException e1) {

            }
        }

    }

    /**
     * 获取已发布流程 png
     *
     * @param pDeployid
     * @param pResponse
     */
    @RequestMapping(value = "/bpmn/png", method = {RequestMethod.GET})
    public void getBpmnPng(@RequestParam("deployid") String pDeployid, HttpServletResponse pResponse) {
        try {
            byte[] data = mFlowService.getFlowbpmn(pDeployid, "png");
            pResponse.getOutputStream().write(data);
            pResponse.setContentType("image/png");
            pResponse.setHeader("Accept-Ranges", "bytes");
            pResponse.flushBuffer();
        } catch (Exception e) {
            try {

                byte[] error = ("获取流程图片出错；原因：" + e.getMessage()).getBytes("UTF-8");
                pResponse.getOutputStream().write(error);
                pResponse.setContentType("image/png");
                pResponse.setHeader("Accept-Ranges", "bytes");
                pResponse.flushBuffer();
            } catch (Exception ex) {
            }
        }
    }
    /**
     * 获取已发布流程 png
     *
     * @param pBusinessKey
     * @param pTaskDefID
     * @param pResponse
     */
    @RequestMapping(value = "/highlightimg", method = {RequestMethod.GET})
    public void getHighlightImg(@RequestParam("businesskey") String pBusinessKey,@RequestParam("taskdefid") String pTaskDefID, HttpServletResponse pResponse) {
        byte[] data = null;
        try {
             if (StringUtil.isNullOrEmpty(pTaskDefID)) {
                data = mFlowService.getHistoryHighlightImg(pBusinessKey);
            } else {
                data = mFlowService.getHighlightImg(pBusinessKey, pTaskDefID);
            }
            pResponse.getOutputStream().write(data);
            pResponse.setContentType("image/png");
            pResponse.setHeader("Accept-Ranges", "bytes");
            pResponse.flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class ResponseCode extends HashMap<String, Object> {

        private static final long serialVersionUID = 1L;

        public ResponseCode() {
            put("code", 0);
            put("msg", "操作成功");
        }

        public static ResponseCode error() {
            return error(1, "操作失败");
        }

        public static ResponseCode error(String msg) {
            return error(500, msg);
        }

        public static ResponseCode error(int code, String msg) {
            ResponseCode r = new ResponseCode();
            r.put("code", code);
            r.put("msg", msg);
            return r;
        }

        public static ResponseCode ok(String msg) {
            ResponseCode r = new ResponseCode();
            r.put("msg", msg);
            return r;
        }

        public static ResponseCode ok(Map<String, Object> map) {
            ResponseCode r = new ResponseCode();
            r.putAll(map);
            return r;
        }

        public static ResponseCode ok() {
            return new ResponseCode();
        }

        @Override
        public ResponseCode put(String key, Object value) {
            super.put(key, value);
            return this;
        }
    }
}
