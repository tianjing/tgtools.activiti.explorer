package tgtools.activiti.explorer.entity;

import org.activiti.engine.repository.ProcessDefinition;

public class ProcessVO {
    private String mId;
    private String mName;
    private String mKey;
    private String mDeploymentId;
    private String mSuspended;
    private int mVersion;


    public ProcessVO(ProcessDefinition pProcessDefinition) {
        this.setId(pProcessDefinition.getId());
        this.mName = pProcessDefinition.getName();
        this.mDeploymentId = pProcessDefinition.getDeploymentId();
        this.mSuspended=String.valueOf(pProcessDefinition.isSuspended());
        this.mVersion=pProcessDefinition.getVersion();
        this.mKey=pProcessDefinition.getKey();
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String pKey) {
        mKey = pKey;
    }

    public int getVersion() {
        return mVersion;
    }

    public void setVersion(int pVersion) {
        mVersion = pVersion;
    }

    public String getSuspended() {
        return mSuspended;
    }

    public void setSuspended(String pSuspended) {
        mSuspended = pSuspended;
    }

    public String getId() {
        return mId;
    }

    public void setId(String pId) {
        mId = pId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String pName) {
        mName = pName;
    }

    public String getDeploymentId() {
        return mDeploymentId;
    }

    public void setDeploymentId(String pDeploymentId) {
        mDeploymentId = pDeploymentId;
    }
}
