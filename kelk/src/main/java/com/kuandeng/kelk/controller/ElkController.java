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
import java.util.concurrent.ExecutionException;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.geo.builders.ShapeBuilders;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoShapeQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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
		// {"type":"Feature","geometry":{"type":"LineString","coordinate":[[116.45768485632,39.96509901302],[116.45259957425,39.9605453452],[116.45175574526,39.95982855956],[116.45077438907,39.95895217194],[116.45058579558,39.95881661667],[116.45038272396,39.9587131992],[116.44957614438,39.95846802419],[116.44928076588,39.95832726994],[116.44902624239,39.95812890981],[116.44795760085,39.95716989054],[116.44779818589,39.95706073559],[116.44770221513,39.95703722857],[116.44759374881,39.95705250955],[116.44723275438,39.95722471325],[116.4466842971,39.95759048583],[116.44612628491,39.957936435],[116.4443417694,39.9591306621]]},"properties":{"track_id":"1000003_20180701073630032","survey":"2","recog":"1","auto":"1","fusion":"1"}}
		System.out.println("");
		if (line != null && line != "" && line.trim().length() > 0) {
			// IndexResponse response = client.prepareIndex("geom",
			// "geom").setSource(line.replace("LineString", "polygon")).get();
			IndexResponse response = client.prepareIndex("geom1", "geom1").setSource(line).get();
		}
	}

	/**
	 * INTERSECTS("intersects"), DISJOINT("disjoint"), WITHIN("within"),
	 * CONTAINS("contains");
	 */

	@ResponseBody
	@RequestMapping(value = "/testn")
	public String testn() {

		return "success";
	}

	/**
	 * 测试api
	 * 查询
	 */
	@ResponseBody
	@RequestMapping(value = "/testQuery")
	public Map<String, Object> testQuery() {
		//getResponse 其实这个貌似就是对应我们的返回结果    目前跟查询id的是一样的
		GetResponse getResponse = client.prepareGet("geom1", "geom1", "AWYVy_58ftXJlJVmw2p9").get();
		Map<String, Object> map = getResponse.getSource();// 这个是我们存储的所有的 k v 信息
		String id = getResponse.getId();
		String index = getResponse.getIndex();
		String type = getResponse.getType();
		boolean exists = getResponse.isExists();

		map.put("id", id);
		map.put("index", index);
		map.put("type", type);
		map.put("exists", exists);
		return map;
	}
	/**
	 * 测试api
	 * 符合查询
	 */
	@ResponseBody
	@RequestMapping(value = "/testFQuery")
	public Map<String, Object> testFQuery() {
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		boolQuery.must(QueryBuilders.matchQuery("name", "zhangsan"));
		
		RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("date");
		rangeQuery.from("").to("");
		
		//两者关联
		boolQuery.filter(rangeQuery);
		
		SearchRequestBuilder searchRequestBuilder = client.prepareSearch("geom1")
		      .setTypes("geom1")
		      .setSearchType(SearchType.QUERY_THEN_FETCH)
		      .setQuery(boolQuery)
		      .setFrom(1)
		      .setSize(100);
		
		SearchResponse searchResponse = searchRequestBuilder.get();
		SearchHits hits = searchResponse.getHits();
		//这是我们的数据
		SearchHit[] hits2 = hits.getHits();
		for (SearchHit searchHit : hits2) {
			String id = searchHit.getId();
			Map<String, Object> source = searchHit.getSource();
		}
		//这是数量
		long totalHits = hits.getTotalHits();
		return null;
	}
	/**
	 * 正式查询  左上  右下  矩形查询
	 * @throws IOException 
	 */
	@ResponseBody
	@RequestMapping(value = "/findByRandomRange")
	public Map<String, Object> findByRange() throws IOException {
		Map<String, Object> map=new HashMap<>();
		//query
		GeoShapeQueryBuilder queryBuilder = QueryBuilders
				                            .geoShapeQuery("geometry",ShapeBuilders.newEnvelope(new Coordinate(-45,90), new Coordinate(145,-45)));
		//search
		SearchRequestBuilder searchRequestBuilder = client.prepareSearch("geom1")
			      .setTypes("geom1")
			      .setSearchType(SearchType.QUERY_THEN_FETCH)
			      .setQuery(queryBuilder)
			      .setFrom(1)
			      .setSize(9999);
		SearchResponse searchResponse = searchRequestBuilder.get();
		//花费时间
		long tookInMillis = searchResponse.getTookInMillis();
		map.put("time", tookInMillis);
		SearchHits searchHits = searchResponse.getHits();
		//数量
		long totalHits = searchHits.getTotalHits();
		map.put("total", totalHits);
		if (totalHits>0) {
			List<Map<String, Object>> list=new ArrayList<>();
			SearchHit[] hits = searchHits.getHits();
			for (SearchHit searchHit : hits) {
				Map<String, Object> source = searchHit.getSource();
				list.add(source);
			}
			map.put("source", list);
		}
		return map;
	}
	/**
	 * 添加
	 */
	@ResponseBody
	@RequestMapping(value = "/testAdd")
	public Map<String, Object> testAdd() throws IOException {
		//getResponse 其实这个貌似就是对应我们的返回结果    目前跟查询id的是一样的
		XContentBuilder xContentBuilder=XContentFactory
				                        .jsonBuilder()
				                        .startObject()
				                        .field("type", "Feature1")
				                        .endObject();
		IndexResponse indexResponse = client.prepareIndex("geom1", "geom1").setSource(xContentBuilder).get();
		indexResponse.getShardInfo();
		return null;
	}
	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping(value = "/testDelete")
	public Map<String, Object> testDelete() throws IOException {
		DeleteResponse deleteResponse = client.prepareDelete("geom1", "geom1", "AWYY8HC8ftXJlJVmw5Rm").get();
		return null;
	}

	/**
	 * 更新
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	@ResponseBody
	@RequestMapping(value = "/testUpdate")
	public Map<String, Object> testUpdate() throws IOException, InterruptedException, ExecutionException {
		UpdateRequest updateRequest=new UpdateRequest("geom1", "geom1", "这是要更新的id");
		XContentBuilder xContentBuilder=XContentFactory.jsonBuilder()
				                         .startObject()
				                         .field("", "")
				                         .endObject();
		updateRequest.doc(xContentBuilder);
		UpdateResponse updateResponse = client.update(updateRequest).get();
		return null;
	}
}
