package com.boot.kaizen.business.fddsf.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 基站为单位接收app上传的信息
 * 
 * @author weichengz
 * @date 2019年1月25日 上午11:38:43
 */
public class LteTddStation implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String enodeBID;// 基站号
	private String baseStationName;// 基站名

	private String testDate;// 测试时间

	private String csfbFunctionTestPass;// CSFB功能测试 是否通过
	private String volteFunctionTestPass;// VoLTE功能测试 是否通过

	// common
	private Integer projId;
	private Date createTime;
	private Date updateTime;
	private Integer createAt;// 登陆人的id

	/**
	 * 以下为上传图片的名字
	 */
	private String allViewPic;// 建筑物全景照（从地面仰视）
	private String stationEntrancePic;// 站点入口图

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEnodeBID() {
		return enodeBID;
	}

	public void setEnodeBID(String enodeBID) {
		this.enodeBID = enodeBID;
	}

	public String getBaseStationName() {
		return baseStationName;
	}

	public void setBaseStationName(String baseStationName) {
		this.baseStationName = baseStationName;
	}

	public String getTestDate() {
		return testDate;
	}

	public void setTestDate(String testDate) {
		this.testDate = testDate;
	}

	public String getCsfbFunctionTestPass() {
		return csfbFunctionTestPass;
	}

	public void setCsfbFunctionTestPass(String csfbFunctionTestPass) {
		this.csfbFunctionTestPass = csfbFunctionTestPass;
	}

	public String getVolteFunctionTestPass() {
		return volteFunctionTestPass;
	}

	public void setVolteFunctionTestPass(String volteFunctionTestPass) {
		this.volteFunctionTestPass = volteFunctionTestPass;
	}

	public Integer getProjId() {
		return projId;
	}

	public void setProjId(Integer projId) {
		this.projId = projId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Integer createAt) {
		this.createAt = createAt;
	}

	public String getAllViewPic() {
		return allViewPic;
	}

	public void setAllViewPic(String allViewPic) {
		this.allViewPic = allViewPic;
	}

	public String getStationEntrancePic() {
		return stationEntrancePic;
	}

	public void setStationEntrancePic(String stationEntrancePic) {
		this.stationEntrancePic = stationEntrancePic;
	}

	public LteTddStation(String id, String enodeBID, String baseStationName, String testDate,
			String csfbFunctionTestPass, String volteFunctionTestPass, Integer projId, Date createTime, Date updateTime,
			Integer createAt, String allViewPic, String stationEntrancePic) {
		super();
		this.id = id;
		this.enodeBID = enodeBID;
		this.baseStationName = baseStationName;
		this.testDate = testDate;
		this.csfbFunctionTestPass = csfbFunctionTestPass;
		this.volteFunctionTestPass = volteFunctionTestPass;
		this.projId = projId;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.createAt = createAt;
		this.allViewPic = allViewPic;
		this.stationEntrancePic = stationEntrancePic;
	}

	public LteTddStation() {
		super();
	}

}
