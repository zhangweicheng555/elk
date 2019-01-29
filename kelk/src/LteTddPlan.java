package com.boot.kaizen.business.fddsf.model;

import java.io.Serializable;
import java.util.Date;

/**
 * tdd室分规划表设计
 * 
 * @author weichengz
 * @date 2019年1月25日 上午11:17:52
 */
public class LteTddPlan implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;

	// 基站描述
	private String enodeBID;// 基站号
	private String baseStationName;// 基站名
	private String testDate;// 测试时间 yyyy-mm-dd
	private String districtCountry;// 区县
	private String address;// 详细地址
	private String baseStationType;// 基站类型 站点类型
	private String deviceType;// 设备类型
	private String videoFrequency;// 频段

	// 相关参数验收
	private String longitude;// 经度
	private String latitude;// 纬度
	private String broadBand;// 传输宽带
	private String ipConfig;// 传输ip配置

	private String testPerson;// 测试工程师
	private String testPersonPhone;// 测试工程师电话
	private String backPerson;// 后台工程师
	private String backPersonPhone;// 后台工程师电话

	private String dealPersonId;// 接收改计划任务的人的id

	private String administrativeArea;// 行政区
	private String commonStationName;// 共址站名
	private String stationAddressType;// 站址类型
	private String buildingFunction;// 建筑物功能
	private String floorNumber;// 楼层数
	private String oriSfSystem;// 原有室分系统
	private String combinationRoad;// 是否合路
	private String singleAndDoubleRold;// 单双路
	private String combinationRoadInstallOk;// 合路器是否安装正确
	private String remark;// 备注

	private Integer status;// 最终是不是报告可以审核 之后最后一步的时候 才设置为1 0 未审核 1审核通过 2审核不通过

	// common
	private Integer projId;
	private Date createTime;
	private Date updateTime;
	private Integer createAt;

	/**
	 * 以下目前是写死 先不用管
	 */

	// 重要性功能验收 写死
	private String stationChangeTestPass;// 同站切换验收
	private String threeFourOperatePass;// 3\4G互操作（重选、PS重定向）

	// 结构验收 写死
	private String combinationWayPass;// 合路方式是否合理
	private String deviceSupportPass;// 器件是否支持
	private String wirePointsReasonablePass;// 天线点位分布是否合理
	private String wirePortOutputPass;// 每层天线口输出功率是否满足要求
	// 可管可控验收 写死
	private String networkPlatformPass; // 接入网优平台信息是否准确
	private String informationPlatformPass; // 录入资管平台信息是否准确
	private String mrrPass; // MRR是否可以开启
	private String cellConfigPass; // 小区参数配置是否准确

	public enum StatueType {
		NO_CHECK(0, "未审核"), CHECK_PASS(1, "审核通过"), CHECK_NO_PASS(2, "审核不通过");

		private Integer code;
		private String name;

		StatueType(Integer code, String name) {
			this.code = code;
			this.name = name;
		}

		public Integer getCode() {
			return code;
		}

		public String getName() {
			return name;
		}

	}

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

	public String getDistrictCountry() {
		return districtCountry;
	}

	public void setDistrictCountry(String districtCountry) {
		this.districtCountry = districtCountry;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBaseStationType() {
		return baseStationType;
	}

	public void setBaseStationType(String baseStationType) {
		this.baseStationType = baseStationType;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getVideoFrequency() {
		return videoFrequency;
	}

	public void setVideoFrequency(String videoFrequency) {
		this.videoFrequency = videoFrequency;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getBroadBand() {
		return broadBand;
	}

	public void setBroadBand(String broadBand) {
		this.broadBand = broadBand;
	}

	public String getIpConfig() {
		return ipConfig;
	}

	public void setIpConfig(String ipConfig) {
		this.ipConfig = ipConfig;
	}

	public String getTestPerson() {
		return testPerson;
	}

	public void setTestPerson(String testPerson) {
		this.testPerson = testPerson;
	}

	public String getTestPersonPhone() {
		return testPersonPhone;
	}

	public void setTestPersonPhone(String testPersonPhone) {
		this.testPersonPhone = testPersonPhone;
	}

	public String getBackPerson() {
		return backPerson;
	}

	public void setBackPerson(String backPerson) {
		this.backPerson = backPerson;
	}

	public String getBackPersonPhone() {
		return backPersonPhone;
	}

	public void setBackPersonPhone(String backPersonPhone) {
		this.backPersonPhone = backPersonPhone;
	}

	public String getDealPersonId() {
		return dealPersonId;
	}

	public void setDealPersonId(String dealPersonId) {
		this.dealPersonId = dealPersonId;
	}

	public String getAdministrativeArea() {
		return administrativeArea;
	}

	public void setAdministrativeArea(String administrativeArea) {
		this.administrativeArea = administrativeArea;
	}

	public String getCommonStationName() {
		return commonStationName;
	}

	public void setCommonStationName(String commonStationName) {
		this.commonStationName = commonStationName;
	}

	public String getStationAddressType() {
		return stationAddressType;
	}

	public void setStationAddressType(String stationAddressType) {
		this.stationAddressType = stationAddressType;
	}

	public String getBuildingFunction() {
		return buildingFunction;
	}

	public void setBuildingFunction(String buildingFunction) {
		this.buildingFunction = buildingFunction;
	}

	public String getFloorNumber() {
		return floorNumber;
	}

	public void setFloorNumber(String floorNumber) {
		this.floorNumber = floorNumber;
	}

	public String getOriSfSystem() {
		return oriSfSystem;
	}

	public void setOriSfSystem(String oriSfSystem) {
		this.oriSfSystem = oriSfSystem;
	}

	public String getCombinationRoad() {
		return combinationRoad;
	}

	public void setCombinationRoad(String combinationRoad) {
		this.combinationRoad = combinationRoad;
	}

	public String getSingleAndDoubleRold() {
		return singleAndDoubleRold;
	}

	public void setSingleAndDoubleRold(String singleAndDoubleRold) {
		this.singleAndDoubleRold = singleAndDoubleRold;
	}

	public String getCombinationRoadInstallOk() {
		return combinationRoadInstallOk;
	}

	public void setCombinationRoadInstallOk(String combinationRoadInstallOk) {
		this.combinationRoadInstallOk = combinationRoadInstallOk;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public String getStationChangeTestPass() {
		return stationChangeTestPass;
	}

	public void setStationChangeTestPass(String stationChangeTestPass) {
		this.stationChangeTestPass = stationChangeTestPass;
	}

	public String getThreeFourOperatePass() {
		return threeFourOperatePass;
	}

	public void setThreeFourOperatePass(String threeFourOperatePass) {
		this.threeFourOperatePass = threeFourOperatePass;
	}

	public String getCombinationWayPass() {
		return combinationWayPass;
	}

	public void setCombinationWayPass(String combinationWayPass) {
		this.combinationWayPass = combinationWayPass;
	}

	public String getDeviceSupportPass() {
		return deviceSupportPass;
	}

	public void setDeviceSupportPass(String deviceSupportPass) {
		this.deviceSupportPass = deviceSupportPass;
	}

	public String getWirePointsReasonablePass() {
		return wirePointsReasonablePass;
	}

	public void setWirePointsReasonablePass(String wirePointsReasonablePass) {
		this.wirePointsReasonablePass = wirePointsReasonablePass;
	}

	public String getWirePortOutputPass() {
		return wirePortOutputPass;
	}

	public void setWirePortOutputPass(String wirePortOutputPass) {
		this.wirePortOutputPass = wirePortOutputPass;
	}

	public String getNetworkPlatformPass() {
		return networkPlatformPass;
	}

	public void setNetworkPlatformPass(String networkPlatformPass) {
		this.networkPlatformPass = networkPlatformPass;
	}

	public String getInformationPlatformPass() {
		return informationPlatformPass;
	}

	public void setInformationPlatformPass(String informationPlatformPass) {
		this.informationPlatformPass = informationPlatformPass;
	}

	public String getMrrPass() {
		return mrrPass;
	}

	public void setMrrPass(String mrrPass) {
		this.mrrPass = mrrPass;
	}

	public String getCellConfigPass() {
		return cellConfigPass;
	}

	public void setCellConfigPass(String cellConfigPass) {
		this.cellConfigPass = cellConfigPass;
	}

	public LteTddPlan(String id, String enodeBID, String baseStationName, String testDate, String districtCountry,
			String address, String baseStationType, String deviceType, String videoFrequency, String longitude,
			String latitude, String broadBand, String ipConfig, String testPerson, String testPersonPhone,
			String backPerson, String backPersonPhone, String dealPersonId, String administrativeArea,
			String commonStationName, String stationAddressType, String buildingFunction, String floorNumber,
			String oriSfSystem, String combinationRoad, String singleAndDoubleRold, String combinationRoadInstallOk,
			String remark, Integer status, Integer projId, Date createTime, Date updateTime, Integer createAt,
			String stationChangeTestPass, String threeFourOperatePass, String combinationWayPass,
			String deviceSupportPass, String wirePointsReasonablePass, String wirePortOutputPass,
			String networkPlatformPass, String informationPlatformPass, String mrrPass, String cellConfigPass) {
		super();
		this.id = id;
		this.enodeBID = enodeBID;
		this.baseStationName = baseStationName;
		this.testDate = testDate;
		this.districtCountry = districtCountry;
		this.address = address;
		this.baseStationType = baseStationType;
		this.deviceType = deviceType;
		this.videoFrequency = videoFrequency;
		this.longitude = longitude;
		this.latitude = latitude;
		this.broadBand = broadBand;
		this.ipConfig = ipConfig;
		this.testPerson = testPerson;
		this.testPersonPhone = testPersonPhone;
		this.backPerson = backPerson;
		this.backPersonPhone = backPersonPhone;
		this.dealPersonId = dealPersonId;
		this.administrativeArea = administrativeArea;
		this.commonStationName = commonStationName;
		this.stationAddressType = stationAddressType;
		this.buildingFunction = buildingFunction;
		this.floorNumber = floorNumber;
		this.oriSfSystem = oriSfSystem;
		this.combinationRoad = combinationRoad;
		this.singleAndDoubleRold = singleAndDoubleRold;
		this.combinationRoadInstallOk = combinationRoadInstallOk;
		this.remark = remark;
		this.status = status;
		this.projId = projId;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.createAt = createAt;
		this.stationChangeTestPass = stationChangeTestPass;
		this.threeFourOperatePass = threeFourOperatePass;
		this.combinationWayPass = combinationWayPass;
		this.deviceSupportPass = deviceSupportPass;
		this.wirePointsReasonablePass = wirePointsReasonablePass;
		this.wirePortOutputPass = wirePortOutputPass;
		this.networkPlatformPass = networkPlatformPass;
		this.informationPlatformPass = informationPlatformPass;
		this.mrrPass = mrrPass;
		this.cellConfigPass = cellConfigPass;
	}

	public LteTddPlan() {
		super();
	}

}