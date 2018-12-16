/*package com.kuandeng.kelk.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.lucene.search.TermQuery;
import org.elasticsearch.Build;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.main.MainResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.get.GetField;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.JsonObject;

@Controller
@RequestMapping(value = "/elk")
public class ElsTest {

	@Autowired
	private RestHighLevelClient restHighLevelClient;

	@ResponseBody
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public Object test() throws IOException {
		
		return getBool();
	}


	*//**
	 * 多条件查询
	* @Description: TODO
	* @author weichengz
	 * @throws IOException 
	* @date 2018年12月16日 上午1:23:28
	 *//*
	private Object getBool() throws IOException {
		SearchRequest searchRequest=new SearchRequest("forum");
		searchRequest.types("article");
		
		SearchSourceBuilder sourceBuilder=new SearchSourceBuilder();
		
		BoolQueryBuilder query=new BoolQueryBuilder();
		
		TermQueryBuilder termQuery=QueryBuilders.termQuery("id", 1);
		MatchQueryBuilder matchQueryBuilder=QueryBuilders.matchQuery("articleID", "XHDK");
		
		query.should(termQuery);
		query.should(matchQueryBuilder);
		sourceBuilder.query(query);
		searchRequest.source(sourceBuilder);
		return restHighLevelClient.search(searchRequest);
	}



	private Object getByField() throws IOException {
		SearchRequest searchRequest=new SearchRequest("forum");
		searchRequest.types("article");
		SearchSourceBuilder sourceBuilder=new SearchSourceBuilder();
		QueryBuilder query=QueryBuilders.termQuery("id", 1);
		sourceBuilder.query(query);
		searchRequest.source(sourceBuilder);
		return restHighLevelClient.search(searchRequest).toString();
	}



	*//**
	 * 根据id获取
	* @Description: TODO
	* @author weichengz
	 * @throws IOException 
	* @date 2018年12月16日 上午12:38:24
	 *//*
	private Object getByIds() throws IOException {
		GetRequest getRequest=new GetRequest("forum", "article", "AWewMw91kPjChyG0b2aS");
		GetResponse getResponse = restHighLevelClient.get(getRequest);
		return getResponse.toString();
	}




	*//**
	 * 得到映射  这个没有
	* @Description: TODO
	* @author weichengz
	 * @throws IOException 
	* @date 2018年12月16日 上午12:20:57
	 *//*
	private String queryMapping() throws IOException {
		IndexRequest indexRequest=new IndexRequest();
		indexRequest.source("http://localhost:9200/forum/article/_mapping");
		IndexResponse indexResponse = restHighLevelClient.index(indexRequest);
		return indexResponse.toString();
		
	}




	*//**
	 * 
	 * @Description: bul添加k操作
	 * @author weichengz
	 * @throws IOException 
	 * @date 2018年12月15日 上午11:42:28
	 *//*
	private Object bulkOperation() throws IOException {
		BulkRequest bulkRequest=new BulkRequest();
		
		IndexRequest indexRequest1=new IndexRequest("forum", "article");
		Map<String, Object> source1=new HashMap<>();
		source1.put("id", "1");
		source1.put("articleID", "XHDK-A-1293-#fJ3");
		source1.put("userID", 1);
		source1.put("hidden", false);
		source1.put("postDate", "2017-01-01");
		indexRequest1.source(source1,XContentType.JSON);
		
		IndexRequest indexRequest2=new IndexRequest("forum", "article");
		Map<String, Object> source2=new HashMap<>();
		source2.put("id", "2");
		source2.put("articleID", "KDKE-B-9947-#kL5");
		source2.put("userID", 1);
		source2.put("hidden", false);
		source2.put("postDate", "2017-01-02");
		indexRequest2.source(source2,XContentType.JSON);
		
		IndexRequest indexRequest3=new IndexRequest("forum", "article");
		Map<String, Object> source3=new HashMap<>();
		source3.put("id", "3");
		source3.put("articleID", "KDKE-B-9947-#kL5");
		source3.put("userID", 2);
		source3.put("hidden", false);
		source3.put("postDate", "2017-01-01");
		indexRequest3.source(source3,XContentType.JSON);

		IndexRequest indexRequest4=new IndexRequest("forum", "article");
		Map<String, Object> source4=new HashMap<>();
		source4.put("id", "4");
		source4.put("articleID", "QQPX-R-3956-#aD8");
		source4.put("userID", 2);
		source4.put("hidden", true);
		source4.put("postDate", "2017-01-02");
		indexRequest4.source(source4,XContentType.JSON);
		
	    bulkRequest.add(indexRequest1);
		bulkRequest.add(indexRequest2);
		bulkRequest.add(indexRequest3);
		bulkRequest.add(indexRequest4);
		
		BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest);
		return bulkResponse.toString();
	}

	*//**
	 * 根据任务id查询
	 * 
	 * @Description:
	 * @author: weichengz
	 *//*
	private String getById() throws IOException {
		GetRequest getRequest = new GetRequest("test", "test", "4");
		GetResponse getResponse = restHighLevelClient.get(getRequest);
		String string = getResponse.toString();
		return string;
	}

	*//**
	 * 创建索引的时候添加数据
	 * 
	 * @Description:
	 * @author: weichengz
	 *//*
	private void createIndexAndAdd() throws IOException {
		IndexRequest indexRequest = new IndexRequest("zzz", "zzz");
		List<Map<String, Object>> list = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			Map<String, Object> map = new HashMap<>();
			map.put("name", "zhangsan");
			map.put("address", "山东潍坊");
			map.put("id", i);
			list.add(map);
		}
		for (Map<String, Object> map : list) {
			indexRequest.source(map);
			restHighLevelClient.index(indexRequest);
		}
	}

	*//**
	 * 删除索引
	 * 
	 * @Description:
	 * @author: weichengz
	 *//*
	private void deleteIndex() {
		// 删除test
		String str = "delete http://192.168.5.34:9200/test";
	}

	*//**
	 * 创建一个空的索引
	 * 
	 * @Description: source里面可以加记录
	 * @author: weichengz
	 *//*
	private void createIndex() throws IOException {
		IndexRequest indexRequest = new IndexRequest("zwc", "zwc");
		indexRequest.source("{}");// 添加记录
		String string = restHighLevelClient.index(indexRequest).toString();
	}

	*//**
	 * 查看索引
	 * 
	 * @Description:
	 * @author: weichengz
	 *//*
	private void indexs() {
		String indexs = "http://192.168.5.34:9200/_cat/indices?v";
	}

	*//**
	 * 查看集群的健康状态
	 * 
	 * @Description:
	 * @author: weichengz
	 *//*
	private void health() {
		String health = "http://192.168.5.34:9200/_cat/health?v";
	}

	*//**
	 * 返回集群的信息
	 * 
	 * @Description: 集群的名字、版本信息
	 * @author: weichengz
	 *//*
	private Object clusterInfo() throws IOException {
		MainResponse mainResponse = restHighLevelClient.info();
		String info = "uuid:" + mainResponse.getClusterUuid() + ";nodeName" + mainResponse.getNodeName() + ";version"
				+ mainResponse.getVersion() + ";clusterName" + mainResponse.getClusterName();
		TransportAddress remoteAddress = mainResponse.remoteAddress();
		String addressInfo = null;
		if (remoteAddress != null) {
			String address = remoteAddress.getAddress();
			String host = remoteAddress.getHost();
			int port = remoteAddress.getPort();
			addressInfo = address + "_" + host + "_" + port;
		}
		return info + "====" + addressInfo;
	}

	*//**
	 * 复杂查询
	 * 
	 * @Description:
	 * @author: weichengz
	 *//*
	private SearchResponse searchMuilti() throws IOException {
		SearchRequest searchRequest = new SearchRequest();
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		BoolQueryBuilder query = QueryBuilders.boolQuery();

		MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("name", "zhang");
		MatchPhraseQueryBuilder matchPhraseQueryBuilder = QueryBuilders.matchPhraseQuery("address.keyword",
				"shandong anqiu");
		RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("age").lte(30).gte(15);

		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().should(matchQueryBuilder)
				.should(matchPhraseQueryBuilder);

		query.must(boolQueryBuilder).must(rangeQueryBuilder);

		sourceBuilder.query(query);
		sourceBuilder.from(0);
		sourceBuilder.size(10);
		sourceBuilder.fetchSource(new String[] { "name", "address" }, new String[] {});
		sourceBuilder.sort("birth", SortOrder.DESC);
		sourceBuilder.timeout(new TimeValue(10000));// 10秒超时

		searchRequest.source(sourceBuilder);
		searchRequest.indices("test").types("test");
		SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
		return searchResponse;
	}

	*//**
	 * 获取返回数据的json传
	 * 
	 * @Description:
	 * @author: weichengz
	 *//*
	private void jsonString() throws IOException {
		GetRequest getRequest = new GetRequest("test", "test", "2");
		GetResponse getResponse = restHighLevelClient.get(getRequest);
		String jsonString = getResponse.toString();
	}

	private void delete() throws IOException {
		DeleteRequest deleteRequest = new DeleteRequest("test", "test", "1");
		DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest);
		deleteResponse.getResult();
	}

	*//***
	 * 判断是不是存在
	 * 
	 * @Description:
	 * @author: weichengz
	 *//*
	private void exists() throws IOException {
		GetRequest getRequest = new GetRequest("test", "test", "1000");
		restHighLevelClient.exists(getRequest);
	}

	*//**
	 * 修改
	 * 
	 * @Description:
	 * @author: weichengz
	 *//*
	private UpdateResponse update() throws IOException {
		UpdateRequest updateRequest = new UpdateRequest("test", "test", "1");
		Map<String, Object> map = new HashMap<>();
		map.put("name", "哈哈");
		updateRequest.doc(map);
		UpdateResponse update = restHighLevelClient.update(updateRequest);
		return update;
	}

	*//**
	 * 新增
	 * 
	 * @Description:
	 * @author: weichengz
	 *//*
	private IndexResponse add() throws IOException {
		IndexRequest indexRequest = new IndexRequest("test", "test", "10");
		Map<String, Object> map = new HashMap<>();
		map.put("name", "张维程");
		map.put("age", 30);
		map.put("address", "潍坊");
		map.put("school", "南昌大学也");
		map.put("birth", "1998-01-28");
		indexRequest.source(map);
		IndexResponse index = restHighLevelClient.index(indexRequest);
		RestStatus status = index.status();
		System.out.println(status.getStatus());
		return index;
	}

	
	 * "name": "liu anran", "age": 2, "address": "shandong linghe", "school":
	 * "xiaoxue", "birth": "2016-03-28", "hobby": { "fruit": "sleep", "food": ",nai"
	 * }, "tags": [ "run", "sleep" ]
	 

	*//**
	 * 根据id查询
	 * 
	 * @Description:
	 * @author: weichengz
	 *//*
	private Map<String, Object> findById() throws IOException {
		GetRequest request = new GetRequest("test", "test", "1");
		GetResponse response = restHighLevelClient.get(request);
		GetField field = response.getField("name");
		// System.out.println(field.getName()+":"+field.getValue());
		System.out.println("--------------------------------------");
		Map<String, Object> sourceAsMap = response.getSourceAsMap();// 将你存储的记录转为json传
		Set<Entry<String, Object>> entrySet = sourceAsMap.entrySet();// 将你存储的记录转为map key就是key v可能是你填写的数组传
		for (Entry<String, Object> entry : entrySet) {
			System.out.println(entry.getKey() + "___" + entry.getValue());
		}
		System.out.println("--------------------------------------");
		System.out.println(response.getId() + "  " + response.getIndex() + "  " + response.getVersion() + "  " + ":"
				+ response.getSourceAsString());
		return response.getSourceAsMap();
		*//**
		 * name___zheng lanying birth___1653-03-28 age___61 hobby___{fruit=mater,
		 * food=,mifan} tags___[run, work] -------------------------------------- 1 test
		 * 1 :{"name":"zheng lanying","age":61,"address":"shandong
		 * weifang","school":"beida","birth":"1653-03-28","hobby":{"fruit":"mater","food":",mifan"},"tags":["run","work"]}
		 * 
		 *//*
	}
}
*/