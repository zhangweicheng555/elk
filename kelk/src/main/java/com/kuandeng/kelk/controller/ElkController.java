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
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.geo.ShapeRelation;
import org.elasticsearch.common.geo.builders.ShapeBuilders;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoBoundingBoxQueryBuilder;
import org.elasticsearch.index.query.GeoShapeQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.vividsolutions.jts.geom.Coordinate;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/elk")
public class ElkController {

	@Autowired
	private TransportClient client;

	@ResponseBody
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public GetResponse test() {
		GetResponse prepareGet = client.prepareGet("people", "man", "1").get();
		return prepareGet;
	}

	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add() throws FileNotFoundException {
		File csv = ResourceUtils.getFile("classpath:static/result.json");
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(csv));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String line = null;
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
	@RequestMapping(value = "/testn", method = RequestMethod.GET)
	public String testn() {

		return "success";
	}

	/**
	 * 测试api 查询
	 */
	@ResponseBody
	@RequestMapping(value = "/testQuery", method = RequestMethod.GET)
	public Map<String, Object> testQuery() {
		// getResponse 其实这个貌似就是对应我们的返回结果 目前跟查询id的是一样的
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
	 * 测试api 符合查询
	 */
	@ResponseBody
	@RequestMapping(value = "/testFQuery", method = RequestMethod.GET)
	public Map<String, Object> testFQuery() {
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		boolQuery.must(QueryBuilders.matchQuery("name", "zhangsan"));

		RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("date");
		rangeQuery.from("").to("");

		// 两者关联
		boolQuery.filter(rangeQuery);

		SearchRequestBuilder searchRequestBuilder = client.prepareSearch("geom1").setTypes("geom1")
				.setSearchType(SearchType.QUERY_THEN_FETCH).setQuery(boolQuery).setFrom(1).setSize(100);

		SearchResponse searchResponse = searchRequestBuilder.get();
		SearchHits hits = searchResponse.getHits();
		// 这是我们的数据
		SearchHit[] hits2 = hits.getHits();
		for (SearchHit searchHit : hits2) {
			String id = searchHit.getId();
			Map<String, Object> source = searchHit.getSource();
		}
		// 这是数量
		long totalHits = hits.getTotalHits();
		return null;
	}

	/**
	 * 正式查询 左上 右下 矩形查询
	 */
	@ApiOperation(value = "对角矩形查询", notes = "矩形查询", httpMethod = "GET")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "leftTopLongitude", value = "左上经度-45", required = true, dataType = "double", paramType = "query"),
			@ApiImplicitParam(name = "leftTopLatitude", value = "左上纬度90", required = true, dataType = "double", paramType = "query"),
			@ApiImplicitParam(name = "RightButtomLongitude", value = "右下经度145", required = true, dataType = "double", paramType = "query"),
			@ApiImplicitParam(name = "RightButtomLatitude", value = "右下纬度-45", required = true, dataType = "double", paramType = "query") })
	@ResponseBody
	@RequestMapping(value = "/findByRandomRange", method = RequestMethod.GET)
	public Map<String, Object> findByRange(Double leftTopLongitude, Double leftTopLatitude, Double RightButtomLongitude,
			Double RightButtomLatitude) throws IOException {
		
		Map<String, Object> map = new HashMap<>();
		// query
		GeoShapeQueryBuilder queryBuilder = QueryBuilders.geoShapeQuery("geometry",
				ShapeBuilders.newEnvelope(new Coordinate(leftTopLongitude, leftTopLatitude),
						new Coordinate(RightButtomLongitude, RightButtomLatitude))).relation(ShapeRelation.WITHIN);
		// search
		SearchRequestBuilder searchRequestBuilder = client.prepareSearch("geom1").setTypes("geom1")
				.setSearchType(SearchType.QUERY_THEN_FETCH).setQuery(queryBuilder).setFrom(1).setSize(9999);
		SearchResponse searchResponse = searchRequestBuilder.get();
		// 花费时间
		long tookInMillis = searchResponse.getTookInMillis();
		map.put("time", tookInMillis);
		SearchHits searchHits = searchResponse.getHits();
		// 数量
		long totalHits = searchHits.getTotalHits();
		map.put("total", totalHits);
		if (totalHits > 0) {
			List<Map<String, Object>> list = new ArrayList<>();
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
	 * 添加  json传的添加
	 */
	@ResponseBody
	@RequestMapping(value = "/testAddJson", method = RequestMethod.GET)
	public Map<String, Object> testAddJson() throws IOException {
		String json="{}";
		IndexResponse response = client.prepareIndex("geom1", "geom1").setSource(json, XContentType.JSON).get();
		return null;
	}

	/**
	 * 删除
	 */
	@ResponseBody
	@RequestMapping(value = "/testDelete", method = RequestMethod.GET)
	public Map<String, Object> testDelete() throws IOException {
		DeleteResponse deleteResponse = client.prepareDelete("geom1", "geom1", "AWYY8HC8ftXJlJVmw5Rm").get();
		return null;
	}

	/**
	 * 更新
	 * 
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@ResponseBody
	@RequestMapping(value = "/testUpdate", method = RequestMethod.GET)
	public Map<String, Object> testUpdate() throws IOException, InterruptedException, ExecutionException {
		UpdateRequest updateRequest = new UpdateRequest("geom1", "geom1", "这是要更新的id");
		XContentBuilder xContentBuilder = XContentFactory.jsonBuilder().startObject().field("", "").endObject();
		updateRequest.doc(xContentBuilder);
		UpdateResponse updateResponse = client.update(updateRequest).get();
		
		return null;
	}
	
	/**
	 * 根据id获取
	 */
	@ResponseBody
	@RequestMapping(value = "/testDelet1e", method = RequestMethod.GET)
	public Map<String, Object> testDelet1e() throws IOException {
		GetResponse response = client.prepareGet("geom1", "geom1", "1").get();
		return null;
	}
	/**
	 * 根据id删除
	 */
	@ResponseBody
	@RequestMapping(value = "/testDelet12e", method = RequestMethod.GET)
	public Map<String, Object> testDelet12e() throws IOException {
		DeleteResponse response = client.prepareDelete("geom1", "geom1", "12").get();
		DeleteResponse deleteResponse = client.prepareDelete().setIndex("geom1").setType("geom1").get();
		return null;
	}
	/**
	 * 根据查询的结果批量删除操作
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteBulk", method = RequestMethod.GET)
	public Map<String, Object> deleteBulk() throws IOException {
		MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("gender", "male");
		BulkByScrollResponse response = DeleteByQueryAction.INSTANCE.newRequestBuilder(client).filter(matchQuery).source("geom1").get();
		long deleted = response.getDeleted();
		return null;
	}
	/**
	 * 修改操作
	 */
	@ResponseBody
	@RequestMapping(value = "/update1", method = RequestMethod.GET)
	public Map<String, Object> update1() throws IOException {
		//方式1
		XContentBuilder xContentBuilder=XContentFactory.jsonBuilder().startObject().field("", "").endObject();
		UpdateResponse updateResponse = client.prepareUpdate("geom1", "geom1", "1").setDoc(xContentBuilder).get();
		//方式2
		String json="{}";
		UpdateResponse updateResponse2 = client.prepareUpdate("geom1", "geom1", "1").setDoc(json, XContentType.JSON).get();
		
		return null;
	}
	
	
	/**
	 * 更新 如果不存在那么就添加
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	@ResponseBody
	@RequestMapping(value = "/updateInsert", method = RequestMethod.GET)
	public Map<String, Object> updateInsert() throws IOException, InterruptedException, ExecutionException {
		//先建立这个 但是这个不一定在数据库中存在
		IndexRequest request=new IndexRequest("index", "type", "id").source(XContentFactory.jsonBuilder().startObject().field("", "").endObject());
		//更新请求 注意 如果找不到我更新的内容 那么久添加了一条数据
		UpdateRequest updateRequest=new UpdateRequest("index", "type", "id").doc(XContentFactory.jsonBuilder().startObject().field("", "").endObject()).upsert(request);
		UpdateResponse response = client.update(updateRequest).get();
		return null;
	}

}
