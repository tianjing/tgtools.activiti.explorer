package tgtools.activiti.explorer.entity;

import org.activiti.engine.impl.pvm.PvmTransition;
import tgtools.util.StringUtil;

/**
 * Created by tian_ on 2016-09-23.
 */
public class PvmTransitionExt {
    public PvmTransitionExt(PvmTransition p_PvmTransition) {
        m_PvmTransition = p_PvmTransition;
    }

    private String m_ID;
    private String m_Name;
    private String m_Key;
    private String m_Value;
    private boolean m_IsGroup = false;
    private PvmTransition m_PvmTransition;
    private boolean m_IsParse = false;

    public boolean getIsGroup() {
        return m_IsGroup;
    }

    //conditionText
    private void parseConditionText() {
        if (m_IsParse) return;
        Object obj = m_PvmTransition.getProperty("conditionText");
        if (null != obj) {
            String conditionText = obj.toString();
            if (StringUtil.contains(conditionText, '$')) {
                conditionText = conditionText.trim();
                conditionText = conditionText.substring(2, conditionText.length() - 1);
                String[] strs = conditionText.split("==");
                m_Key = strs[0].trim();
                m_Value = strs[1].trim();
            }
        }
        m_IsParse = true;
    }

    public String getID() {
        return m_PvmTransition.getDestination().getId();
    }


    public String getName() {
        Object type = m_PvmTransition.getDestination().getProperty("type");
        if (null != type && "endEvent".equals(type.toString())) {
            return "结束";
        }
//        Object name= m_PvmTransition.getDestination().getProperty("name");
        // return null!=name?name.toString(): StringUtil.EMPTY_STRING;
        return getName(m_PvmTransition);
    }

    private String getName(PvmTransition p_PvmTransition) {
        Object type = p_PvmTransition.getDestination().getProperty("type");
        if (null != type && "usertask".equals(type.toString().toLowerCase())) {
            Object name = p_PvmTransition.getDestination().getProperty("name");
            if (null != name && !StringUtil.isNullOrEmpty(name.toString())) {
                if ("parallelgateway".equals(p_PvmTransition.getSource().getProperty("type").toString().toLowerCase()) || "userTask".equals(p_PvmTransition.getSource().getProperty("type").toString().toLowerCase()))
                    m_IsGroup = true;
                return name.toString();
            }
        }
        return getName(p_PvmTransition.getDestination().getOutgoingTransitions().get(0));
    }

    public String getKey() {
        parseConditionText();
        return m_Key;
    }

    public String getValue() {
        parseConditionText();
        return m_Value;
    }


    public PvmTransition getPvmTransition() {
        return m_PvmTransition;
    }


}
