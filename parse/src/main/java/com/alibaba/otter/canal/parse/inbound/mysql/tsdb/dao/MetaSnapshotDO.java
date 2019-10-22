package com.alibaba.otter.canal.parse.inbound.mysql.tsdb.dao;

import java.util.Date;

/**
 * @author agapple 2017年7月27日 下午11:09:41
 * @since 1.0.25
 */
public class MetaSnapshotDO {

    private Long   id;
    private Date   gmtCreate;
    private Date   gmtModified;
    private String destination;
    private String binlogFile;
    private Long   binlogOffest;
    private String binlogMasterId;
    private Long   binlogTimestamp;
    private String data;
    private String extra;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getBinlogFile() {
        return binlogFile;
    }

    public void setBinlogFile(String binlogFile) {
        this.binlogFile = binlogFile;
    }

    public Long getBinlogOffest() {
        return binlogOffest;
    }

    public void setBinlogOffest(Long binlogOffest) {
        this.binlogOffest = binlogOffest;
    }

    public String getBinlogMasterId() {
        return binlogMasterId;
    }

    public void setBinlogMasterId(String binlogMasterId) {
        this.binlogMasterId = binlogMasterId;
    }

    public Long getBinlogTimestamp() {
        return binlogTimestamp;
    }

    public void setBinlogTimestamp(Long binlogTimestamp) {
        this.binlogTimestamp = binlogTimestamp;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("MetaSnapshotDO [id=").append(id).append(", gmtCreate=").append(gmtCreate).append(", gmtModified=").append(gmtModified).append(", destination=")
				.append(destination).append(", binlogFile=").append(binlogFile).append(", binlogOffest=").append(binlogOffest).append(", binlogMasterId=").append(binlogMasterId)
				.append(", binlogTimestamp=").append(binlogTimestamp).append(", data=").append(data).append(", extra=").append(extra).append("]").toString();
    }

}
