package tgtools.activiti.explorer.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * @author 田径
 * @Title
 * @Description
 * @date 19:03
 */
public class FlowLogVo {
    @JsonProperty("NAME_")
    private String mName;
    @JsonProperty("LAST_")
    private String mLast;
    @JsonProperty("DESCRIPTION_")
    private String mDescription;
    @JsonProperty("START_TIME_")
    private Date mStartTime;
    @JsonProperty("END_TIME_")
    private Date mEndTime;

    public String getName() {
        return mName;
    }

    public void setName(String pName) {
        mName = pName;
    }

    public String getLast() {
        return mLast;
    }

    public void setLast(String pLast) {
        mLast = pLast;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String pDescription) {
        mDescription = pDescription;
    }

    public Date getStartTime() {
        return mStartTime;
    }

    public void setStartTime(Date pStartTime) {
        mStartTime = pStartTime;
    }

    public Date getEndTime() {
        return mEndTime;
    }

    public void setEndTime(Date pEndTime) {
        mEndTime = pEndTime;
    }
}
