package tgtools.activiti.explorer.entity;

import org.activiti.engine.impl.pvm.PvmTransition;
import tgtools.exceptions.APPErrorException;
import tgtools.json.JSONArray;
import tgtools.json.JSONException;
import tgtools.json.JSONObject;
import tgtools.util.RegexHelper;
import tgtools.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tian_ on 2016-09-23.
 */
public class PvmTransitionExtCollection extends ArrayList<PvmTransitionExt> {
    public PvmTransitionExtCollection() {
    }

    public PvmTransitionExtCollection(List<PvmTransition> p_PvmTransitions) {
        for (int i = 0; i < p_PvmTransitions.size(); i++) {
            this.add(new PvmTransitionExt(p_PvmTransitions.get(i)));
        }
    }

    /**
     * @return
     */
    public JSONArray toJSONArray() throws JSONException {
        JSONArray array = new JSONArray();
        for (int i = 0; i < this.size(); i++) {
            JSONObject json = new JSONObject();
            json.put("id", this.get(i).getID());
            json.put("name", this.get(i).getName());
            json.put("key", this.get(i).getKey());
            json.put("value", getValue(this.get(i).getValue()));
            json.put("isGroup", this.get(i).getIsGroup());
            array.put(json);
        }
        return array;
    }

    /**
     * 返回下个节点的变量反值的集合
     * 比如 $｛pass==false｝ 那么 map 里就是 pass true
     *
     * @return
     * @throws APPErrorException
     */
    public Map<String, Object> toNegativeVariables() throws APPErrorException {
        Map<String, Object> var = toVariables();
        for (String key : var.keySet()) {
            if(var.get(key) instanceof Boolean) {
                var.put(key, !((Boolean) var.get(key)));
            }
            else if(var.get(key) instanceof Integer)
            {
                var.put(key, Integer.MAX_VALUE);
            }
        }
        return var;
    }

    /**
     * 返回下个节点的变量的集合
     *
     * @return
     * @throws APPErrorException
     */
    public Map<String, Object> toVariables() throws APPErrorException {
        Map<String, Object> vars = new HashMap<String, Object>();
        for (int i = 0; i < this.size(); i++) {
//            if (vars.containsKey(this.get(i).getKey())) {
//                throw new APPErrorException("表达式有重复的变量");
//            }
            if(!StringUtil.isNullOrEmpty(this.get(i).getKey())) {
                vars.put(this.get(i).getKey(), getValue(this.get(i).getValue()));
            }
        }
        return vars;
    }

    private Object getValue(String p_Value) {
        if(StringUtil.isNullOrEmpty(p_Value))
        {
            return p_Value;
        }
        else if ("true".equals(p_Value) || "false".equals(p_Value)) {
            return Boolean.valueOf(p_Value);
        }
        else if(RegexHelper.isNubmer(p_Value))
        {
           return Integer.valueOf(p_Value);
        }
        return p_Value;
    }
}
