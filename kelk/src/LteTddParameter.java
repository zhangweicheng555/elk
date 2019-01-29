package com.boot.kaizen.business.fddsf.model;

import java.io.Serializable;
import java.util.Date;

/**
 * lte fdd app对接参数信息 以小区为单位
 * 
 * @author weichengz
 * @date 2018年12月31日 下午10:31:14
 */
public class LteTddParameter implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String enodeBID;// 基站号
	private String baseStationName;// 基站名
	private String cellId;// 小区号
	private String cellName;// 小区名称
	private String testDate;// 测试时间

	private String ftpDownPass;// FTP下载是否通过
	private String ftpUpPass;// FTP上传是否通过

	private String convenienceCoveragePerPass;// 便利性测试覆盖率 这个目前用不到 留空

	/** 小区工程参数 **/
	private String eci;
	private String earfcn;
	private String pci;
	private String tac;

	// CSFB呼叫成功率
	private String csfCallAttempt;
	private String csfbCallSucc;
	private String csfbCallFailure;
	// Volte呼叫成功率
	private String volteCallAttempt;
	private String volteCallSucc;
	private String volteCallFailure;
	// Volte呼叫掉线率
	private String volteDownAttempt;
	private String volteDownSucc;
	private String volteDownFailure;

	// FTP下行吞吐率
	// 覆盖率
	private String ftpDownCover;// 遍历性测试性能指标
	private String ftpDownCoverRandom;// 任意点
	// 下行吞吐率
	private String ftpDownThroughput;// 遍历性测试性能指标
	private String ftpDownThroughputRandom;// 任意点

	// FTP上行吞吐率
	// 覆盖率
	private String ftpUpCover;// 遍历性测试性能指标
	private String ftpUpCoverRandom;// 任意点
	// 上行吞吐率
	private String ftpUpThroughput;// 遍历性测试性能指标
	private String ftpUpThroughputRandom;// 任意点

	// common
	private Integer projId;
	private Date createTime;
	private Date updateTime;
	private Integer createAt;

	//底层
	private String lowNum;
	private String lowRsrp;
	private String lowSinr;
	private String lowCallSuccPercent;
	//中层
	private String middleNum;
	private String middleRsrp;
	private String middleSinr;
	private String middleCallSuccPercent;
	//高层
	private String highNum;
	private String highRsrp;
	private String highSinr;
	private String highCallSuccPercent;
	//地下室
	private String baseNum;
	private String baseRsrp;
	private String baseSinr;
	private String baseCallSuccPercent;
	//电梯
	private String elevatorNum;
	private String elevatorRsrp;
	private String elevatorSinr;
	private String elevatorCallSuccPercent;

	
	
	
	
	private String volteSwitch;// VoLTE开关

	/**
	 * 一下图
	 */
	// 性能验收覆盖效果图
	private String rsrpImage; // RSRP覆盖图
	private String sinrImage; // SINR
	private String downRateImage;// 下载速率
	private String upRateImage;// 上传速率

	// 楼层覆盖效果图
	private String flowCoverRsrpImage; // RSRP覆盖图
	private String flowCoverSinrImage; // SINR覆盖图
	private String flowCoverpCIImage;// pci覆盖图

	// 测试曾图片 这个是个数组 对各逗号分隔；
	private String testFlowImage;

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

	public String getmBaseStationName() {
		return mBaseStationName;
	}

	public void setmBaseStationName(String mBaseStationName) {
		this.mBaseStationName = mBaseStationName;
	}

	public String getCellId() {
		return cellId;
	}

	public void setCellId(String cellId) {
		this.cellId = cellId;
	}

	public String getCellName() {
		return cellName;
	}

	public void setCellName(String cellName) {
		this.cellName = cellName;
	}

	public String getTestDate() {
		return testDate;
	}

	public void setTestDate(String testDate) {
		this.testDate = testDate;
	}

	public String getFtpDownPass() {
		return ftpDownPass;
	}

	public void setFtpDownPass(String ftpDownPass) {
		this.ftpDownPass = ftpDownPass;
	}

	public String getFtpUpPass() {
		return ftpUpPass;
	}

	public void setFtpUpPass(String ftpUpPass) {
		this.ftpUpPass = ftpUpPass;
	}

	public String getConvenienceCoveragePerPass() {
		return convenienceCoveragePerPass;
	}

	public void setConvenienceCoveragePerPass(String convenienceCoveragePerPass) {
		this.convenienceCoveragePerPass = convenienceCoveragePerPass;
	}

	public String getEci() {
		return eci;
	}

	public void setEci(String eci) {
		this.eci = eci;
	}

	public String getEarfcn() {
		return earfcn;
	}

	public void setEarfcn(String earfcn) {
		this.earfcn = earfcn;
	}

	public String getPci() {
		return pci;
	}

	public void setPci(String pci) {
		this.pci = pci;
	}

	public String getTac() {
		return tac;
	}

	public void setTac(String tac) {
		this.tac = tac;
	}

	public String getCsfCallAttempt() {
		return csfCallAttempt;
	}

	public void setCsfCallAttempt(String csfCallAttempt) {
		this.csfCallAttempt = csfCallAttempt;
	}

	public String getCsfbCallSucc() {
		return csfbCallSucc;
	}

	public void setCsfbCallSucc(String csfbCallSucc) {
		this.csfbCallSucc = csfbCallSucc;
	}

	public String getCsfbCallFailure() {
		return csfbCallFailure;
	}

	public void setCsfbCallFailure(String csfbCallFailure) {
		this.csfbCallFailure = csfbCallFailure;
	}

	public String getVolteCallAttempt() {
		return volteCallAttempt;
	}

	public void setVolteCallAttempt(String volteCallAttempt) {
		this.volteCallAttempt = volteCallAttempt;
	}

	public String getVolteCallSucc() {
		return volteCallSucc;
	}

	public void setVolteCallSucc(String volteCallSucc) {
		this.volteCallSucc = volteCallSucc;
	}

	public String getVolteCallFailure() {
		return volteCallFailure;
	}

	public void setVolteCallFailure(String volteCallFailure) {
		this.volteCallFailure = volteCallFailure;
	}

	public String getVolteDownAttempt() {
		return volteDownAttempt;
	}

	public void setVolteDownAttempt(String volteDownAttempt) {
		this.volteDownAttempt = volteDownAttempt;
	}

	public String getVolteDownSucc() {
		return volteDownSucc;
	}

	public void setVolteDownSucc(String volteDownSucc) {
		this.volteDownSucc = volteDownSucc;
	}

	public String getVolteDownFailure() {
		return volteDownFailure;
	}

	public void setVolteDownFailure(String volteDownFailure) {
		this.volteDownFailure = volteDownFailure;
	}

	public String getFtpDownCover() {
		return ftpDownCover;
	}

	public void setFtpDownCover(String ftpDownCover) {
		this.ftpDownCover = ftpDownCover;
	}

	public String getFtpDownCoverRandom() {
		return ftpDownCoverRandom;
	}

	public void setFtpDownCoverRandom(String ftpDownCoverRandom) {
		this.ftpDownCoverRandom = ftpDownCoverRandom;
	}

	public String getFtpDownThroughput() {
		return ftpDownThroughput;
	}

	public void setFtpDownThroughput(String ftpDownThroughput) {
		this.ftpDownThroughput = ftpDownThroughput;
	}

	public String getFtpDownThroughputRandom() {
		return ftpDownThroughputRandom;
	}

	public void setFtpDownThroughputRandom(String ftpDownThroughputRandom) {
		this.ftpDownThroughputRandom = ftpDownThroughputRandom;
	}

	public String getFtpUpCover() {
		return ftpUpCover;
	}

	public void setFtpUpCover(String ftpUpCover) {
		this.ftpUpCover = ftpUpCover;
	}

	public String getFtpUpCoverRandom() {
		return ftpUpCoverRandom;
	}

	public void setFtpUpCoverRandom(String ftpUpCoverRandom) {
		this.ftpUpCoverRandom = ftpUpCoverRandom;
	}

	public String getFtpUpThroughput() {
		return ftpUpThroughput;
	}

	public void setFtpUpThroughput(String ftpUpThroughput) {
		this.ftpUpThroughput = ftpUpThroughput;
	}

	public String getFtpUpThroughputRandom() {
		return ftpUpThroughputRandom;
	}

	public void setFtpUpThroughputRandom(String ftpUpThroughputRandom) {
		this.ftpUpThroughputRandom = ftpUpThroughputRandom;
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

	public String getlowNum() {
		return lowNum;
	}

	public void setlowNum(String lowNum) {
		this.lowNum = lowNum;
	}

	public String getlowRsrp() {
		return lowRsrp;
	}

	public void setlowRsrp(String lowRsrp) {
		this.lowRsrp = lowRsrp;
	}

	public String getlowSinr() {
		return lowSinr;
	}

	public void setlowSinr(String lowSinr) {
		this.lowSinr = lowSinr;
	}

	public String getlowCallSuccPercent() {
		return lowCallSuccPercent;
	}

	public void setlowCallSuccPercent(String lowCallSuccPercent) {
		this.lowCallSuccPercent = lowCallSuccPercent;
	}

	public String getVolteSwitch() {
		return volteSwitch;
	}

	public void setVolteSwitch(String volteSwitch) {
		this.volteSwitch = volteSwitch;
	}

	public String getRsrpImage() {
		return rsrpImage;
	}

	public void setRsrpImage(String rsrpImage) {
		this.rsrpImage = rsrpImage;
	}

	public String getSinrImage() {
		return sinrImage;
	}

	public void setSinrImage(String sinrImage) {
		this.sinrImage = sinrImage;
	}

	public String getDownRateImage() {
		return downRateImage;
	}

	public void setDownRateImage(String downRateImage) {
		this.downRateImage = downRateImage;
	}

	public String getUpRateImage() {
		return upRateImage;
	}

	public void setUpRateImage(String upRateImage) {
		this.upRateImage = upRateImage;
	}

	public String getFlowCoverRsrpImage() {
		return flowCoverRsrpImage;
	}

	public void setFlowCoverRsrpImage(String flowCoverRsrpImage) {
		this.flowCoverRsrpImage = flowCoverRsrpImage;
	}

	public String getFlowCoverSinrImage() {
		return flowCoverSinrImage;
	}

	public void setFlowCoverSinrImage(String flowCoverSinrImage) {
		this.flowCoverSinrImage = flowCoverSinrImage;
	}

	public String getFlowCoverpCIImage() {
		return flowCoverpCIImage;
	}

	public void setFlowCoverpCIImage(String flowCoverpCIImage) {
		this.flowCoverpCIImage = flowCoverpCIImage;
	}

	public String getTestFlowImage() {
		return testFlowImage;
	}

	public void setTestFlowImage(String testFlowImage) {
		this.testFlowImage = testFlowImage;
	}

	public LteTddParameter(String id, String enodeBID, String mBaseStationName, String cellId, String cellName,
			String testDate, String ftpDownPass, String ftpUpPass, String convenienceCoveragePerPass, String eci,
			String earfcn, String pci, String tac, String csfCallAttempt, String csfbCallSucc, String csfbCallFailure,
			String volteCallAttempt, String volteCallSucc, String volteCallFailure, String volteDownAttempt,
			String volteDownSucc, String volteDownFailure, String ftpDownCover, String ftpDownCoverRandom,
			String ftpDownThroughput, String ftpDownThroughputRandom, String ftpUpCover, String ftpUpCoverRandom,
			String ftpUpThroughput, String ftpUpThroughputRandom, Integer projId, Date createTime, Date updateTime,
			Integer createAt, String lowNum, String lowRsrp, String lowSinr, String lowCallSuccPercent,
			String volteSwitch, String rsrpImage, String sinrImage, String downRateImage, String upRateImage,
			String flowCoverRsrpImage, String flowCoverSinrImage, String flowCoverpCIImage, String testFlowImage) {
		super();
		this.id = id;
		this.enodeBID = enodeBID;
		this.mBaseStationName = mBaseStationName;
		this.cellId = cellId;
		this.cellName = cellName;
		this.testDate = testDate;
		this.ftpDownPass = ftpDownPass;
		this.ftpUpPass = ftpUpPass;
		this.convenienceCoveragePerPass = convenienceCoveragePerPass;
		this.eci = eci;
		this.earfcn = earfcn;
		this.pci = pci;
		this.tac = tac;
		this.csfCallAttempt = csfCallAttempt;
		this.csfbCallSucc = csfbCallSucc;
		this.csfbCallFailure = csfbCallFailure;
		this.volteCallAttempt = volteCallAttempt;
		this.volteCallSucc = volteCallSucc;
		this.volteCallFailure = volteCallFailure;
		this.volteDownAttempt = volteDownAttempt;
		this.volteDownSucc = volteDownSucc;
		this.volteDownFailure = volteDownFailure;
		this.ftpDownCover = ftpDownCover;
		this.ftpDownCoverRandom = ftpDownCoverRandom;
		this.ftpDownThroughput = ftpDownThroughput;
		this.ftpDownThroughputRandom = ftpDownThroughputRandom;
		this.ftpUpCover = ftpUpCover;
		this.ftpUpCoverRandom = ftpUpCoverRandom;
		this.ftpUpThroughput = ftpUpThroughput;
		this.ftpUpThroughputRandom = ftpUpThroughputRandom;
		this.projId = projId;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.createAt = createAt;
		this.lowNum = lowNum;
		this.lowRsrp = lowRsrp;
		this.lowSinr = lowSinr;
		this.lowCallSuccPercent = lowCallSuccPercent;
		this.volteSwitch = volteSwitch;
		this.rsrpImage = rsrpImage;
		this.sinrImage = sinrImage;
		this.downRateImage = downRateImage;
		this.upRateImage = upRateImage;
		this.flowCoverRsrpImage = flowCoverRsrpImage;
		this.flowCoverSinrImage = flowCoverSinrImage;
		this.flowCoverpCIImage = flowCoverpCIImage;
		this.testFlowImage = testFlowImage;
	}

	public LteTddParameter() {
		super();
	}

}
