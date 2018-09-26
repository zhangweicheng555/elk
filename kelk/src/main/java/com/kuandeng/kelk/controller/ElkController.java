package com.kuandeng.kelk.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.util.Lists;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.geo.ShapeRelation;
import org.elasticsearch.common.geo.builders.CoordinatesBuilder;
import org.elasticsearch.common.geo.builders.ShapeBuilder;
import org.elasticsearch.common.geo.builders.ShapeBuilders;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.GeoShapeQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.vividsolutions.jts.geom.Coordinate;

@Controller
@RequestMapping(value = "/elk")
public class ElkController {

	@Autowired
	private TransportClient client;

	@ResponseBody
	@RequestMapping(value = "/test")
	public GetResponse test() {
		GetResponse prepareGet = client.prepareGet("people", "man", "1").get();
		return prepareGet;
	}

	@ResponseBody
	@RequestMapping(value = "/add")
	public String add() throws FileNotFoundException {
		File csv = ResourceUtils.getFile("classpath:static/result.json");
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(csv));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String line = null;
		// 1000003_20180701073630032,2,1,1,1,"LINESTRING(116.45768485632
		// 39.96509901302,116.45259957425 39.9605453452,116.45175574526
		// 39.95982855956,116.45077438907 39.95895217194,116.45058579558
		// 39.95881661667,116.45038272396 39.9587131992,116.44957614438
		// 39.95846802419,116.44928076588 39.95832726994,116.44902624239
		// 39.95812890981,116.44795760085 39.95716989054,116.44779818589
		// 39.95706073559,116.44770221513 39.95703722857,116.44759374881
		// 39.95705250955,116.44723275438 39.95722471325,116.4466842971
		// 39.95759048583,116.44612628491 39.957936435,116.4443417694 39.9591306621)"
		try {
			while ((line = br.readLine()) != null) // 读取到的内容给line变量
			{
				insertElk(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "success";
	}

	/**
	 * 将json格式的数据导入索引中
	 */
	private void insertElk(String line) {
		System.out.println(line);
		//{"type":"Feature","geometry":{"type":"LineString","coordinate":[[116.45768485632,39.96509901302],[116.45259957425,39.9605453452],[116.45175574526,39.95982855956],[116.45077438907,39.95895217194],[116.45058579558,39.95881661667],[116.45038272396,39.9587131992],[116.44957614438,39.95846802419],[116.44928076588,39.95832726994],[116.44902624239,39.95812890981],[116.44795760085,39.95716989054],[116.44779818589,39.95706073559],[116.44770221513,39.95703722857],[116.44759374881,39.95705250955],[116.44723275438,39.95722471325],[116.4466842971,39.95759048583],[116.44612628491,39.957936435],[116.4443417694,39.9591306621]]},"properties":{"track_id":"1000003_20180701073630032","survey":"2","recog":"1","auto":"1","fusion":"1"}}
		System.out.println("");
		if (line != null && line != "" && line.trim().length()>0) {
		//	IndexResponse response = client.prepareIndex("geom", "geom").setSource(line.replace("LineString", "polygon")).get();
			IndexResponse response = client.prepareIndex("geom1", "geom1").setSource(line).get();
		}
	}

	
	
	/**
	INTERSECTS("intersects"),
    DISJOINT("disjoint"),
    WITHIN("within"),
    CONTAINS("contains");
	 */
	
	
	@ResponseBody
	@RequestMapping(value = "/testn")
	public String testn() {
		
		return "success";
	}
	
    
	
}
