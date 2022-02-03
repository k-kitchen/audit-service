package com.klasha.audits.model;


import com.klasha.audits.enums.Action;
import com.klasha.audits.util.EntityUtilities;
import lombok.ToString;



import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.TemporalType.TIMESTAMP;

@ToString
@Entity
public class ObjectHistory extends BaseEntity implements Serializable {




    private String createdBy;


    private String modifiedBy;


    private Date createdDate;


    private Date modifiedDate;

    private String ipAddress;

    @Column(length = 1024)
    private String after;
    private String requestId;
    private String tableName;
    private String userAction;
    @Column(length = 1024)
    private String before;


    @Enumerated(STRING)
    private Action action;

    private String object;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }



    public Date getCreatedDate() {
        return createdDate;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAction() {
        return userAction;
    }

    public void setUserAction(String userAction) {
        this.userAction = userAction;
    }

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }



    public String getAfter() {
        return after;
    }

    public void setAfter(String content) {
        this.after = content;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }


    public ObjectHistory(Object object, Action action, String ipAddress, String requestId, String userAction) {
        this.object = object.getClass().getName();
        this.ipAddress = ipAddress;
        this.action = action;
        this.requestId = requestId;
        this.tableName = EntityUtilities.getTableName(object);
        this.userAction = userAction;
    }


    public ObjectHistory() {
    }
}
