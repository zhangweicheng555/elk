package com.kuandeng.kelk.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long tookInMillis;
	private Long took;
	private Long totalHits;


	
	List<Map<String, Object>> list = new ArrayList<>();

	public Long getTookInMillis() {
		return tookInMillis;
	}

	public void setTookInMillis(Long tookInMillis) {
		this.tookInMillis = tookInMillis;
	}

	public Long getTook() {
		return took;
	}

	public void setTook(Long took) {
		this.took = took;
	}

	public Long getTotalHits() {
		return totalHits;
	}

	public void setTotalHits(Long totalHits) {
		this.totalHits = totalHits;
	}

	public List<Map<String, Object>> getList() {
		return list;
	}

	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}

	public TestModel(Long tookInMillis, Long took, Long totalHits, List<Map<String, Object>> list) {
		super();
		this.tookInMillis = tookInMillis;
		this.took = took;
		this.totalHits = totalHits;
		this.list = list;
	}

	public TestModel() {
		super();
	}

}
