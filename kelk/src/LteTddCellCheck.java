package com.boot.kaizen.business.fddsf.model;

import java.io.Serializable;
import java.util.Date;

/**
 * tdd室分小区测试设计
 * 
 * @author weichengz
 * @date 2019年1月25日 上午11:18:13
 */
public class LteTddCellCheck implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id;

	private String eodeBID;// 基站号
	private String baseStationName;// 基站名
	private String cellId;// 小区ID
	private String cellName;// 小区名字

	
	//小区参数
	private String upDownRatio;// 上下行子帧配比
	private String specialRatio;// 特殊子帧配比
	private String eci;// eci
	private String earfcn;// EARFCN
	private String pci;// PCI
	private String tac;// TAC
	private String rsPower;// RsPower
	private String pa;
	private String pb;
	private String antennaHangUp;// 合路方式
	private String azimuth;// 单双路
	
	// RRC Setup Success Rate
	private String rrcSuccRateAttempt;// 尝试次数
	private String rrcSuccRateSucc;// 成功次数
	private String rrcSuccRateFailure;// 失败次数

	// ERAB Setup Success Rate
	private String erabSuccRateAttempt;// 尝试次数
	private String erabSuccRateSucc;// 成功次数
	private String erabSuccRateFailure;// 失败次数

	// Access Success Rate
	private String accessSuccRateAttempt;// 尝试次数
	private String accessSuccRateSucc;// 成功次数
	private String accessSuccRateFailure;// 失败次数
	
	private String volteDownWire; // Volte呼叫掉话率 写死 0%;
	private String volteDelay; // Volte呼叫接入时延 写死 2393.5ms;

	
	// 系统内切换（室内外切换）
	private String systemInChangeAttempt;
	private String systemInChangeSuccSucc;
	private String systemInChangefailure;

	// 系统间切换
	private String systemOutChangeAttempt;
	private String systemOutChangeSuccSucc;
	private String systemOutChangefailure;
	
	private String floorHeight;//楼高
	
	
	private String testDate;
	// common
	private Integer projId;
	private Date createTime;
	private Date updateTime;
	private Integer createAt;

}