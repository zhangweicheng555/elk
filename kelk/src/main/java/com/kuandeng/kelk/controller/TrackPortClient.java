package com.kuandeng.kelk.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.geo.ShapeRelation;
import org.elasticsearch.common.geo.builders.ShapeBuilders;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoShapeQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.index.search.stats.SearchStats.Stats;
import org.elasticsearch.search.DocValueFormat.DateTime;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregator.Range;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.kuandeng.kelk.model.TaskModel;

import io.swagger.annotations.ApiParam;

@Controller
@RequestMapping(value = "/elk")
public class TrackPortClient {

	@Autowired
	private TransportClient transportClient;

	@ResponseBody
	@RequestMapping(value = "/zwc")
	public Object testMode() throws Exception {
		return analyzeTask2();
	}
	@ResponseBody
	@RequestMapping(value = "/zwcn")
	public Object testModen() throws Exception {
		return importCsv();
	}

	

	/**
	 * 日期聚合处理
	 * @return
	 * @throws ParseException
	 */
	private Object analyzeTask2() throws ParseException  {
		SearchResponse actionGet = transportClient.prepareSearch("task").setTypes("task")
				.setQuery(QueryBuilders.rangeQuery("createdate").gte(1548314057990d).lte(1548314057994d))
				.addAggregation(AggregationBuilders.terms("agg").field("createdate").size(2000)).execute().actionGet();
		
		LongTerms terms = actionGet.getAggregations().get("agg");
		List<Bucket> buckets = terms.getBuckets();
		for (Bucket bucket : buckets) {
			System.out.println(bucket.getKey()+"----"+bucket.getDocCount());
		}
		return actionGet;
	}
	
	
	
	
	/**
	 * 多组分组排序聚合
	 * @return
	 */
	private Object analyzeTask() {
		SearchResponse actionGet = transportClient.prepareSearch("task").setTypes("task").addAggregation(AggregationBuilders.terms("agg").field("projid").order(BucketOrder.key(true)).size(300000).subAggregation(AggregationBuilders.terms("num").field("state").size(500))).execute().actionGet();
		Aggregations aggregations = actionGet.getAggregations();
		LongTerms aggTerms=aggregations.get("agg");
		List<Bucket> buckets = aggTerms.getBuckets();
		for (Bucket bucket : buckets) {
			LongTerms numTerms = bucket.getAggregations().get("num");
			List<Bucket> buckets2 = numTerms.getBuckets();
			for (Bucket bucket2 : buckets2) {
				System.out.println(bucket.getKey()+"   "+bucket2.getKey()+"   "+bucket2.getDocCount());
			}
		}
		return actionGet;
	}
	
	/**
	 * 统计不同项目下的各个状态的任务的数量
	 * @return
	 */
	private Object analyzeTask1() {
		SearchResponse actionGet = transportClient.prepareSearch("task").setTypes("task").addAggregation(AggregationBuilders.terms("agg").field("projId")).execute().actionGet();
		Aggregations aggregations = actionGet.getAggregations();
		LongTerms aggTerms=aggregations.get("agg");
		List<Bucket> buckets = aggTerms.getBuckets();
		for (Bucket bucket : buckets) {
			System.out.println(bucket.getKey()+"----"+bucket.getKeyAsString()+"----"+bucket.getDocCount());
		}
		return "success";
	}





	/**
	 * 导入任务表的数据
	 * @return
	 * @throws Exception
	 */
	public Object importCsv() throws Exception {
		InputStream inputStream = null;
		BufferedReader bufferedReader = null;
		File file = ResourceUtils.getFile("classpath:taskModel.csv");
		bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

		BulkRequest request = new BulkRequest();
		String str = null;
		while ((str = bufferedReader.readLine()) != null) {
			String[] strArray = str.split(",");
			TaskModel taskModel = new TaskModel(Integer.valueOf(strArray[0]), Integer.valueOf(strArray[1]), strArray[2],
					Integer.valueOf(strArray[3]), strArray[4], Long.valueOf(strArray[5].toString()), strArray[6],
					Long.valueOf(strArray[7].toString()), Integer.valueOf(strArray[8]), Integer.valueOf(strArray[9]),
					Integer.valueOf(strArray[10]), Integer.valueOf(strArray[11]), Integer.valueOf(strArray[12]));
			IndexRequest indexRequest = new IndexRequest("task", "task");
			indexRequest.source(JSONObject.toJSONString(taskModel), XContentType.JSON);
			request.add(indexRequest);
		}
		BulkResponse bulkResponse = transportClient.bulk(request).get();

		if (inputStream != null) {
			inputStream.close();
		}
		if (bufferedReader != null) {
			bufferedReader.close();
		}
		return "success";
	}

	private Object shown10() {
		SearchResponse actionGet = transportClient.prepareSearch("geom").setTypes("geom")
				.addAggregation(AggregationBuilders.terms("agg").field("properties.ADCODE")
						.subAggregation(AggregationBuilders.terms("aggsta").field("properties.STATE")))
				.execute().actionGet();
		Terms terms = actionGet.getAggregations().get("agg");
		List<? extends org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket> bucketsM = terms.getBuckets();
		for (org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket bucket : bucketsM) {
			StringTerms longTerms = bucket.getAggregations().get("aggsta");
			List<org.elasticsearch.search.aggregations.bucket.terms.StringTerms.Bucket> buckets = longTerms
					.getBuckets();
			for (org.elasticsearch.search.aggregations.bucket.terms.StringTerms.Bucket bk : buckets) {
				System.out.println(bucket.getKey() + "   " + bk.getKey() + "  " + bk.getDocCount());
			}
		}
		return actionGet;
	}

	private Object shown9() {
		SearchResponse actionGet = transportClient.prepareSearch("geom").setTypes("geom")
				.addAggregation(AggregationBuilders.terms("agg").field("properties.ADCODE")).execute().actionGet();
		Map<String, Aggregation> asMap = actionGet.getAggregations().getAsMap();
		Aggregation aggregation = asMap.get("agg");
		LongTerms terms = (LongTerms) aggregation;
		List<Bucket> buckets = terms.getBuckets();
		List<Map<String, Object>> list = new ArrayList<>();
		for (Bucket bucket : buckets) {
			Map<String, Object> map = new HashMap<>();
			map.put(bucket.getKey().toString(), bucket.getDocCount());// 这个地方可以过滤掉 你不想显示的数据 例如小于80的
			list.add(map);
		}
		return list;
	}

	private Object shown8() {
		SearchResponse actionGet = transportClient.prepareSearch("test").setTypes("test")
				.addAggregation(AggregationBuilders.terms("coount").field("date")).execute().actionGet();
		Aggregation aggregation = actionGet.getAggregations().getAsMap().get("coount");
		LongTerms terms = (LongTerms) aggregation;
		List<Bucket> buckets = terms.getBuckets();
		for (Bucket bucket2 : buckets) {
			System.out.println(bucket2.getKey() + "    " + bucket2.getDocCount());
		}
		return buckets;
	}

	@ResponseBody
	@RequestMapping(value = "/zwc1121")
	public Object testMode22() throws InterruptedException, ExecutionException, ParseException {
		return shown6();
	}

	private Object shown7() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date begin = sdf.parse("2018-01-01 12:12:12");// ","2019-01-01 12:12:12
		Date end = sdf.parse("2019-01-01 12:12:12");// ","
		long beginLong = begin.getTime();
		long endLong = end.getTime();
		SearchResponse actionGet = transportClient.prepareSearch("test").setTypes("test")
				.addAggregation(AggregationBuilders.dateRange("agg").field("date").format("yyyy-MM-dd HH:mmm:ss")
						.addRange(Double.valueOf(beginLong), Double.valueOf(beginLong)))
				.execute().actionGet();
		return actionGet;
	}

	private Object shown6() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		IndexRequest request = new IndexRequest("test", "test");
		Map<String, Object> source = new HashMap<>();
		source.put("name", "北京");
		source.put("address", "山东");
		source.put("date", sdf.parse("2018-10-10 12:12:12").getTime());
		request.source(source);

		IndexRequest request1 = new IndexRequest("test", "test");
		Map<String, Object> source1 = new HashMap<>();
		source1.put("name", "北京1");
		source1.put("address", "山东1");
		source1.put("date", sdf.parse("2018-07-10 12:12:12").getTime());
		request1.source(source1);

		IndexRequest request2 = new IndexRequest("test", "test");
		Map<String, Object> source2 = new HashMap<>();
		source2.put("name", "北京2");
		source2.put("address", "山东2");
		source2.put("date", sdf.parse("2019-01-23 12:12:12").getTime());
		request2.source(source2);

		BulkRequest bulkRequest = new BulkRequest();
		bulkRequest.add(request);
		bulkRequest.add(request1);
		bulkRequest.add(request2);
		BulkResponse actionGet2 = transportClient.bulk(bulkRequest).actionGet();

		return actionGet2;
	}

	private Object shown5() {
		SearchResponse actionGet = transportClient.prepareSearch("geom").setTypes("geom")
				.addAggregation(AggregationBuilders.dateRange("agg").field("properties.CREATETIME")
						.format("yyyy-MM-dd HH:mm:ss").addRange("2019-01-01", "2019-01-30"))
				.execute().actionGet();
		return actionGet;
	}

	private Object shown4() {
		SearchResponse actionGet = transportClient.prepareSearch("geom").setTypes("geom")
				.addAggregation(AggregationBuilders.terms("count").field("properties.ADCODE")
						.subAggregation(AggregationBuilders.topHits("top").explain(true).from(0).size(40)))
				.execute().actionGet();
		return actionGet;
	}

	/**
	 * 百分比聚合
	 * 
	 * @return
	 */
	private Object shown3() {
		SearchResponse actionGet = transportClient
				.prepareSearch("geom").setTypes("geom").addAggregation(AggregationBuilders.percentiles("percent")
						.field("properties.ADCODE").percentiles(1.0, 5.0, 10.0, 20.0, 30.0, 75.0, 95.0, 99.0))
				.execute().actionGet();
		// Percentiles agg = sr.getAggregations().get("agg");
		return actionGet;
	}

	/**
	 * 对数字类型的统计分析 name: "statsAgg", metaData: null, count: 13201, min: 110000, max:
	 * 522700, sum: 4941571300, avg: 374333.10355276114, avgAsString:
	 * "374333.10355276114", sumAsString: "4.9415713E9", minAsString: "110000.0",
	 * maxAsString: "522700.0", writeableName: "stats", type: "stats", fragment:
	 * true, mapped: true
	 * 
	 * @return
	 */
	private Object shown2() {
		SearchResponse searchResponse = transportClient.prepareSearch("geom").setTypes("geom")
				.addAggregation(AggregationBuilders.stats("statsAgg").field("properties.ADCODE")).execute().actionGet();
		org.elasticsearch.search.aggregations.metrics.stats.Stats stats = searchResponse.getAggregations()
				.get("statsAgg");
		Map<String, Object> map = new HashMap<>();
		map.put("searchResponse", searchResponse);
		map.put("stats", stats);
		return map;
	}

	/**
	 * 去重统计某个字段的数量
	 * 
	 * @return
	 */
	private Object shown1() {
		SearchResponse actionGet = transportClient.prepareSearch("geom").setTypes("geom")
				.addAggregation(AggregationBuilders.cardinality("count").field("properties.ADCODE")).execute()
				.actionGet();
		return actionGet;
	}

	/**
	 * 统计某个字段的数量
	 * 
	 * @return
	 */
	private Object shown() {
		SearchResponse actionGet = transportClient.prepareSearch("geom").setTypes("geom")
				.addAggregation(AggregationBuilders.count("count").field("properties.ADCODE")).execute().actionGet();
		return actionGet;
	}

	@ResponseBody
	@RequestMapping(value = "/zwc1")
	public Object testMode1() throws InterruptedException, ExecutionException {
		return agg2();
	}

	private Object showModel() throws InterruptedException, ExecutionException {
		SearchRequest request = new SearchRequest("geom");
		request.types("geom");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		TermQueryBuilder termQuery = QueryBuilders.termQuery("properties.ADCODE", "130600");
		sourceBuilder.query(termQuery);
		request.source(sourceBuilder);
		SearchResponse searchResponse = transportClient.search(request).get();
		return searchResponse;
	}

	/**
	 * 平台导出的信息在此项目中导入
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@GetMapping(value = "/import")
	public Object TagImport() throws Exception {

		BulkRequest request = new BulkRequest();
		InputStream inputStream = new FileInputStream(
				ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "45.dat"));
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		String str = null;
		while ((str = bufferedReader.readLine()) != null) {
			IndexRequest indexRequest = new IndexRequest("geom", "geom");
			indexRequest.source(str, XContentType.JSON);
			request.add(indexRequest);
		}
		BulkResponse bulkResponse = transportClient.bulk(request).get();

		if (inputStream != null) {
			inputStream.close();
		}
		if (bufferedReader != null) {
			bufferedReader.close();
		}
		return bulkResponse;
	}

	private IndexResponse show1() throws InterruptedException, ExecutionException {
		IndexRequest request = new IndexRequest("stu", "stu", "1");
		Map<String, Object> map = new HashMap<>();
		map.put("name", "zhangsan");
		map.put("age", 1);
		request.source(map);
		IndexResponse indexResponse = transportClient.index(request).get();
		return indexResponse;
	}

	private static void show() throws UnknownHostException, InterruptedException, ExecutionException {
		Settings settings = Settings.builder().put("cluster.name", "elasticsearch").put("client.transport.sniff", true)
				.build();
		TransportClient transportClient = new PreBuiltTransportClient(settings);
		TransportAddress transportAddress = new TransportAddress(InetAddress.getByName("192.168.1.40"), 9300);
		transportClient.addTransportAddress(transportAddress);

		BulkByScrollResponse bulkByScrollResponse = DeleteByQueryAction.INSTANCE.newRequestBuilder(transportClient)
				.filter(null).source("").get();
		long deleted = bulkByScrollResponse.getDeleted();

		// 根据查询条件异步的删除
		DeleteByQueryAction.INSTANCE.newRequestBuilder(transportClient).filter(null).source("")
				.execute(new ActionListener<BulkByScrollResponse>() {
					@Override
					public void onResponse(BulkByScrollResponse response) {//
						long deleted2 = response.getDeleted();
					}

					@Override
					public void onFailure(Exception e) {// 失败的话 做处理结果
					}
				});

		MultiGetResponse multiGetResponse = transportClient.prepareMultiGet().add("index1", "type1", "id or ids数组")
				.add("index1", "type1", "id or ids数组").get();

		BulkRequest bulkRequest = new BulkRequest();
		bulkRequest.add(new IndexRequest("", "", "").source(new HashMap<String, Object>()));
		bulkRequest.add(new IndexRequest("", "", "").source(new HashMap<String, Object>()));
		bulkRequest.add(new IndexRequest("", "", "").source(new HashMap<String, Object>()));
		BulkResponse bulkResponse = transportClient.bulk(bulkRequest).get();
		if (bulkResponse.hasFailures()) {
			String buildFailureMessage = bulkResponse.buildFailureMessage();
		}
	}

	@ResponseBody
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public Object test() throws FileNotFoundException, InterruptedException, ExecutionException {
		return geoBoxquery();
	}

	/**
	 * geobox查询
	 * 
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	private Object geoBoxquery() throws InterruptedException, ExecutionException {
		return null;
	}

	/**
	 * 空间位置查询 GeoShapeQuery
	 * 
	 * 
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@SuppressWarnings("deprecation")
	private SearchResponse geoquery() throws InterruptedException, ExecutionException {
		List<Coordinate> points = new ArrayList<>();
		Coordinate c1 = new Coordinate(116.23790171916, 40.16728581574);
		Coordinate c2 = new Coordinate(116.23650609399, 40.17225717213);
		Coordinate c3 = new Coordinate(116.23633473033, 40.17278619292);
		Coordinate c4 = new Coordinate(116.23614847086, 40.17326687872);
		Coordinate c5 = new Coordinate(116.23582308442, 40.17394986874);
		Coordinate c6 = new Coordinate(116.23148129945, 40.1823051491);
		points.add(c1);
		points.add(c2);
		points.add(c3);
		points.add(c4);
		points.add(c5);
		points.add(c6);
		GeoShapeQueryBuilder qb = new GeoShapeQueryBuilder("geometry", ShapeBuilders.newMultiPoint(points));
		qb.relation(ShapeRelation.INTERSECTS);

		SearchRequest request = new SearchRequest("geom");
		request.types("geom");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.query(qb);
		request.source(sourceBuilder);
		SearchResponse response = transportClient.search(request).get();
		return response;

	}

	/**
	 * 批量导入数据 成功了
	 * 
	 * @return
	 * @throws FileNotFoundException
	 */
	public String add1() throws FileNotFoundException {
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
				try {
					insertElk1(line);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "success";
	}

	private void insertElk1(String str) throws InterruptedException, ExecutionException {
		if (str != null && str != "" && str.trim().length() > 0) {
			IndexRequest request = new IndexRequest("geom", "geom");
			request.source(str, XContentType.JSON);
			transportClient.index(request).get();
		}
	}

	/**
	 * 批量导入数据 成功了
	 * 
	 * @return
	 * @throws FileNotFoundException
	 */
	public String add() throws FileNotFoundException {
		File csv = ResourceUtils.getFile("classpath:static/resul2.json");
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
				try {
					insertElk(line);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "success";
	}

	private void insertElk(String str) throws InterruptedException, ExecutionException {
		if (str != null && str != "" && str.trim().length() > 0) {
			IndexRequest request = new IndexRequest("geomn", "geom");
			int begin = str.indexOf("[[[");
			int end = str.indexOf("]]]");
			String beginStr = str.substring(begin, end);
			String endStr = beginStr.substring(3, beginStr.indexOf("]"));
			String finalStr = "[" + endStr + "]";
			str = str.replace("]]]", "]," + finalStr + "]]");
			System.out.println(str);
			// String strModel=str.substring(0, str.indexOf("]]]}"))+"]]]}}";
			request.source(str, XContentType.JSON);
			transportClient.index(request).get();
		}
	}

	/**
	 * mget操作
	 * 
	 * @Description: TODO
	 * @author weichengz
	 * @date 2018年12月23日 下午7:54:22
	 */
	private Object mgetOperate() throws InterruptedException, ExecutionException {
		MultiGetRequest request = new MultiGetRequest();
		request.add("forum", "article", "1_A6s2cBJGn1b8WJqoRF");
		request.add("forum", "article", "1vAys2cBJGn1b8WJYIS-");
		MultiGetResponse multiGetResponse = transportClient.multiGet(request).get();
		return multiGetResponse;
	}

	/**
	 * 全量替换或者部分替换
	 * 
	 * @Description: TODO
	 * @author weichengz
	 * @throws ExecutionException
	 * @throws InterruptedException
	 * @date 2018年12月23日 下午7:39:06
	 */
	private Object replaceAllAndPart() throws InterruptedException, ExecutionException {
		SearchRequest request = new SearchRequest("forum");
		request.types("article");

		transportClient.search(request);

		Map<String, Object> map = new HashMap<>();
		map.put("address", "潍坊");
		transportClient.prepareUpdate("forum", "article", "1vAys2cBJGn1b8WJYIS-").setDoc(map).get();// 部分

		Map<String, Object> mapT = new HashMap<>();
		mapT.put("address", "潍坊1");
		mapT.put("price", 178);
		UpdateRequest request2 = new UpdateRequest("forum", "article", "KDKE-B-9947-");// 部分
		request2.doc(mapT);
		transportClient.update(request2).get();

		IndexRequest indexRequest = new IndexRequest("forum", "article", "0fAqs2cBJGn1b8WJdIRK");// 全量
		Map<String, Object> source = new HashMap<>();
		source.put("id", "1");
		indexRequest.source(source);
		transportClient.index(indexRequest);
		return "success";
	}

	/**
	 * 先过滤 在分组 在计算平均值
	 * 
	 * @Description: TODO
	 * @author weichengz
	 * @throws ExecutionException
	 * @throws InterruptedException
	 * @date 2018年12月23日 下午6:50:48
	 */
	private Object aggAvgFilter() throws InterruptedException, ExecutionException {
		SearchRequest request = new SearchRequest("forum");
		request.types("article");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		TermsAggregationBuilder aggregation = AggregationBuilders.terms("tag_num").field("tag");
		AvgAggregationBuilder avgAggregationBuilder = AggregationBuilders.avg("avg_price").field("view_num");
		// aggregation.subAggregation(avgAggregationBuilder).order(BucketOrder.count(true));//按照数量排序
		aggregation.subAggregation(avgAggregationBuilder);
		aggregation.order(BucketOrder.aggregation("avg_price", true));
		sourceBuilder.aggregation(aggregation);

		MatchQueryBuilder postFilter = QueryBuilders.matchQuery("title", "java haddop");
		sourceBuilder.postFilter(postFilter);
		request.source(sourceBuilder);
		SearchResponse searchResponse = transportClient.search(request).get();
		Aggregation aggregation2 = searchResponse.getAggregations().get("tag_num");

		return aggregation2.toString();
	}

	private Object agg2() throws InterruptedException, ExecutionException {
		SearchRequest request = new SearchRequest("geom");
		request.types("geom");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.aggregation(AggregationBuilders.terms("num").field("properties.ADCODE"));
		request.source(sourceBuilder);
		SearchResponse searchResponse = transportClient.search(request).get();
		Map<String, Aggregation> asMap = searchResponse.getAggregations().getAsMap();
		return asMap.get("num");
	}

	/**
	 * 分组查询数量
	 * 
	 * @Description: TODO
	 * @author weichengz
	 * @date 2018年12月23日 下午6:30:58
	 */
	private Object agg() throws InterruptedException, ExecutionException {
		SearchResponse searchResponse2 = transportClient.prepareSearch("forum").setTypes("article")
				.addAggregation(AggregationBuilders.terms("tag_num").field("tag")).get();
		Aggregation aggregation = searchResponse2.getAggregations().get("tag_num");
		return aggregation.toString();
	}

	/**
	 * 短语匹配
	 * 
	 * @Description: TODO
	 * @author weichengz
	 * @date 2018年12月23日 下午6:00:41
	 */
	private Object prathMatch() throws InterruptedException, ExecutionException {
		SearchRequest request = new SearchRequest("forum");
		request.types("article");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		MatchPhraseQueryBuilder matchPhraseQuery = QueryBuilders.matchPhraseQuery("title", "is java");
		sourceBuilder.query(matchPhraseQuery);
		HighlightBuilder highlightBuilder = new HighlightBuilder();
		highlightBuilder.field("title");
		sourceBuilder.highlighter(highlightBuilder);
		request.source(sourceBuilder);
		SearchResponse searchResponse = transportClient.search(request).get();
		SearchHits hits = searchResponse.getHits();
		return searchResponse;
	}

	/**
	 * 多字段模糊匹配
	 * 
	 * @Description: TODO
	 * @author weichengz
	 * @date 2018年12月23日 下午5:56:13
	 */
	private Object MultiMatch() throws InterruptedException, ExecutionException {
		SearchRequest request = new SearchRequest("forum");
		SearchSourceBuilder sourceBuilde = new SearchSourceBuilder();
		MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("title", "java  hadoop");
		sourceBuilde.query(matchQuery);
		request.source(sourceBuilde);
		SearchResponse searchResponse = transportClient.search(request).get();
		return searchResponse.getHits();
	}

	/**
	 * 范围跟模糊匹配
	 * 
	 * @Description: TODO
	 * @author weichengz
	 * @date 2018年12月23日 下午5:51:17
	 */
	private Object rangeMatch() throws InterruptedException, ExecutionException {
		SearchRequest request = new SearchRequest("forum");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("title", "hadoop");
		sourceBuilder.query(matchQuery);
		RangeQueryBuilder queryBuilder = QueryBuilders.rangeQuery("view_num").lte(100).gte(0);
		sourceBuilder.postFilter(queryBuilder);
		request.source(sourceBuilder);
		SearchResponse searchResponse = transportClient.search(request).get();
		return searchResponse.getHits();
	}

	/**
	 * 模糊跟分页
	 * 
	 * @Description: TODO
	 * @author weichengz
	 * @date 2018年12月23日 下午5:51:31
	 */
	private Object matchSize() throws InterruptedException, ExecutionException {
		SearchRequest request = new SearchRequest("forum");
		request.types("article");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.query(QueryBuilders.matchQuery("title", "java"));
		sourceBuilder.from(0);
		sourceBuilder.size(1);
		request.source(sourceBuilder);
		SearchResponse searchResponse = transportClient.search(request).get();
		return searchResponse.getHits();
	}

	/**
	 * 范围查询
	 * 
	 * @Description: TODO
	 * @author weichengz
	 * @date 2018年12月23日 下午5:51:39
	 */
	private Object queryRange() {
		SearchRequest request = new SearchRequest("forum");
		request.types("article");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("postDate");
		rangeQuery.lte("2017-02-08").gte("2017-01-02");
		sourceBuilder.query(rangeQuery);
		sourceBuilder.sort("postDate", SortOrder.DESC);
		request.source(sourceBuilder);
		ActionFuture<SearchResponse> search = transportClient.search(request);
		SearchResponse actionGet = search.actionGet();
		return listBySearchResponse(actionGet.getHits());
	}

	private java.util.List<String> listBySearchResponse(SearchHits hits) {
		java.util.List<String> list = new ArrayList<>();
		for (SearchHit searchHit : hits) {
			String sourceAsString = searchHit.getSourceAsString();
			list.add(sourceAsString);
		}
		return list;
	}

	/**
	 * @Description: 范围查询 并按照某个字段排序
	 * @author weichengz
	 * @date 2018年12月16日 下午6:34:43
	 */
	private Object queryBySort() throws InterruptedException, ExecutionException {
		SearchRequest request = new SearchRequest("forum");
		request.types("article");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("view_num");
		rangeQuery.gte(0).lt(10);
		sourceBuilder.query(rangeQuery);
		sourceBuilder.sort("view_num", SortOrder.DESC);
		request.source(sourceBuilder);
		ActionFuture<SearchResponse> search = transportClient.search(request);
		SearchResponse searchResponse = search.get();
		SearchHits hits = searchResponse.getHits();
		return listBySearchResponse(hits);
	}

	private Object addField() {
		Map<String, Object> source = new HashMap<>();
		source.put("view_num", 9);
		UpdateRequestBuilder updateRequestBuilder = transportClient
				.prepareUpdate("forum", "article", "0PAjs2cBJGn1b8WJ3oTi").setDoc(source);
		UpdateResponse updateResponse = updateRequestBuilder.get();
		return updateResponse;
	}

	/**
	 * 部分更新
	 * 
	 * @Description: TODO
	 * @author weichengz
	 * @date 2018年12月16日 下午1:03:36
	 */
	private Object partUpdate() {
		UpdateRequest updateRequest = new UpdateRequest("forum", "article", "0fAqs2cBJGn1b8WJdIRK");
		Map<String, Object> source = new HashMap<>();
		source.put("articleID", "XHDK-A-1293-#fJ311");
		source.put("postDate", "2017-01-02");
		source.put("id", "1");
		source.put("userID", 30);
		updateRequest.doc(source);
		ActionFuture<UpdateResponse> update = transportClient.update(updateRequest);
		return update.actionGet();
	}

	/**
	 * 部分更新
	 * 
	 * @Description: TODO
	 * @author weichengz
	 * @date 2018年12月16日 下午12:51:18
	 */
	private Object updatePart() {
		Map<String, Object> source = new HashMap<>();
		source.put("userID", 4);
		UpdateRequestBuilder updateRequestBuilder = transportClient
				.prepareUpdate("forum", "article", "0fAqs2cBJGn1b8WJdIRK").setDoc(source);
		UpdateResponse updateResponse = updateRequestBuilder.get();

		return updateResponse;
	}

	/**
	 * 多条件查询
	 * 
	 * @Description: TODO
	 * @author weichengz
	 * @date 2018年12月16日 下午12:51:04
	 */
	private Object muitiSearch() throws InterruptedException, ExecutionException {
		SearchRequest searchRequest = new SearchRequest("forum");
		searchRequest.types("article");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

		TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("postDate", "2017-01-01");
		TermQueryBuilder termQueryBuilder2 = QueryBuilders.termQuery("articleID", "XHDK-A-1293-#fJ3");

		TermQueryBuilder termQueryBuilder3 = QueryBuilders.termQuery("postDate", "2017-01-01");

		boolQueryBuilder.should(termQueryBuilder);
		boolQueryBuilder.should(termQueryBuilder2);
		boolQueryBuilder.must(termQueryBuilder3);

		sourceBuilder.query(boolQueryBuilder);
		searchRequest.source(sourceBuilder);
		ActionFuture<SearchResponse> search = transportClient.search(searchRequest);
		SearchResponse searchResponse = search.get();

		return listBySearchResponse(searchResponse.getHits());
	}

	/**
	 * 根据字段查询
	 * 
	 * @Description: TODO
	 * @author weichengz
	 * @date 2018年12月16日 下午12:50:49
	 */
	private Object searchMethod() {
		SearchRequest searchRequest = new SearchRequest("forum");
		searchRequest.types("article");
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		TermQueryBuilder termQuery = QueryBuilders.termQuery("userID", 2);
		sourceBuilder.query(termQuery);
		searchRequest.source(sourceBuilder);
		SearchResponse actionGet = transportClient.search(searchRequest).actionGet();
		SearchHits hits = actionGet.getHits();
		java.util.List<String> list = listBySearchResponse(hits);
		return list;
	}

	/**
	 * 批量添加
	 * 
	 * @Description: TODO
	 * @author weichengz
	 * @date 2018年12月16日 上午2:33:26
	 */
	private Object batchMethood() {
		BulkRequest bulkRequest = new BulkRequest();

		IndexRequest indexRequest1 = new IndexRequest("forum", "article");
		Map<String, Object> source1 = new HashMap<>();
		source1.put("id", "1");
		source1.put("articleID", "XHDK-A-1293-#fJ3");
		source1.put("userID", 1);
		source1.put("hidden", false);
		source1.put("postDate", "2017-01-01");
		indexRequest1.source(source1);

		IndexRequest indexRequest2 = new IndexRequest("forum", "article");
		Map<String, Object> source2 = new HashMap<>();
		source2.put("id", "2");
		source2.put("articleID", "KDKE-B-9947-#kL5");
		source2.put("userID", 1);
		source2.put("hidden", false);
		source2.put("postDate", "2017-01-02");
		indexRequest2.source(source2);

		IndexRequest indexRequest3 = new IndexRequest("forum", "article");
		Map<String, Object> source3 = new HashMap<>();
		source3.put("id", "3");
		source3.put("articleID", "KDKE-B-9947-#kL5");
		source3.put("userID", 2);
		source3.put("hidden", false);
		source3.put("postDate", "2017-01-01");
		indexRequest3.source(source3);

		IndexRequest indexRequest4 = new IndexRequest("forum", "article");
		Map<String, Object> source4 = new HashMap<>();
		source4.put("id", "4");
		source4.put("articleID", "QQPX-R-3956-#aD8");
		source4.put("userID", 2);
		source4.put("hidden", true);
		source4.put("postDate", "2017-01-02");
		indexRequest4.source(source4);

		bulkRequest.add(indexRequest1);
		bulkRequest.add(indexRequest2);
		bulkRequest.add(indexRequest3);
		bulkRequest.add(indexRequest4);

		ActionFuture<BulkResponse> bulk = transportClient.bulk(bulkRequest);

		return bulk.actionGet().toString();
	}
}
