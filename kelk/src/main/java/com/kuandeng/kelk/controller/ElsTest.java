package com.kuandeng.kelk.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.get.GetField;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/elk")
public class ElsTest {

	@Autowired
	private RestHighLevelClient restHighLevelClient;

	@ResponseBody
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public Object test() throws IOException {
		SearchRequest searchRequest=new SearchRequest();
		SearchSourceBuilder sourceBuilder=new SearchSourceBuilder();
		BoolQueryBuilder query = QueryBuilders.boolQuery();
		
		MatchQueryBuilder matchQueryBuilder=QueryBuilders.matchQuery("name", "zhang");
		MatchPhraseQueryBuilder matchPhraseQueryBuilder=QueryBuilders.matchPhraseQuery("address.keyword", "shandong anqiu");
		RangeQueryBuilder rangeQueryBuilder=QueryBuilders.rangeQuery("age").lte(30).gte(15);
		
		BoolQueryBuilder boolQueryBuilder=QueryBuilders.boolQuery().should(matchQueryBuilder).should(matchPhraseQueryBuilder);
		
		query.must(boolQueryBuilder).must(rangeQueryBuilder);
		
		sourceBuilder.query(query);
		sourceBuilder.from(0);
		sourceBuilder.size(10);
		sourceBuilder.fetchSource(new String[] {"name","address"},new String[] {});
		sourceBuilder.sort("birth",SortOrder.DESC);
		sourceBuilder.timeout(new TimeValue(10000));//10秒超时
		
		searchRequest.source(sourceBuilder);
		searchRequest.indices("test").types("test");
		SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
		return searchResponse.toString();
	}

	/**
	 * 获取返回数据的json传
	* @Description:
	* @author: weichengz
	 */
	private void jsonString() throws IOException {
		GetRequest getRequest=new GetRequest("test", "test", "2");
		GetResponse getResponse = restHighLevelClient.get(getRequest);
		String jsonString = getResponse.toString();
	}

	private void delete() throws IOException {
		DeleteRequest deleteRequest=new DeleteRequest("test", "test", "1");
		DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest);
		deleteResponse.getResult();
	}

	/***
	 * 判断是不是存在
	* @Description:
	* @author: weichengz
	 */
	private void exists() throws IOException {
		GetRequest getRequest=new GetRequest("test", "test", "1000");
		 restHighLevelClient.exists(getRequest);
	}

	/**
	 * 修改
	* @Description:
	* @author: weichengz
	 */
	private UpdateResponse update() throws IOException {
		UpdateRequest updateRequest=new UpdateRequest("test", "test", "1");
		Map<String, Object> map=new HashMap<>();
		map.put("name", "哈哈");
		updateRequest.doc(map);
		UpdateResponse update = restHighLevelClient.update(updateRequest);
		return update;
	}

	/**
	 * 新增
	* @Description:
	* @author: weichengz
	 */
	private IndexResponse add() throws IOException {
		IndexRequest indexRequest=new IndexRequest("test", "test", "10");
		Map<String, Object> map=new HashMap<>();
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
	
	
	/*"name": "liu anran",
    "age": 2,
    "address": "shandong linghe",
    "school": "xiaoxue",
    "birth": "2016-03-28",
    "hobby": {
        "fruit": "sleep",
        "food": ",nai"
    },
    "tags": [
        "run",
        "sleep"
    ]*/
	
	/**
	 * 根据id查询
	* @Description:
	* @author: weichengz
	 */
	private Map<String, Object> findById() throws IOException {
		GetRequest request=new GetRequest("test", "test", "1");
		GetResponse response = restHighLevelClient.get(request);
		GetField field = response.getField("name");
		//System.out.println(field.getName()+":"+field.getValue());
		System.out.println("--------------------------------------");
		Map<String, Object> sourceAsMap = response.getSourceAsMap();//将你存储的记录转为json传
		Set<Entry<String,Object>> entrySet = sourceAsMap.entrySet();//将你存储的记录转为map  key就是key  v可能是你填写的数组传
		for (Entry<String, Object> entry : entrySet) {
			System.out.println(entry.getKey()+"___"+entry.getValue());
		}
		System.out.println("--------------------------------------");
		System.out.println(response.getId()+"  "+response.getIndex()+"  "+response.getVersion()+"  "+":"+response.getSourceAsString());
		return response.getSourceAsMap();
		/**
			name___zheng lanying
			birth___1653-03-28
			age___61
			hobby___{fruit=mater, food=,mifan}
			tags___[run, work]
			--------------------------------------
			1  test  1  :{"name":"zheng lanying","age":61,"address":"shandong weifang","school":"beida","birth":"1653-03-28","hobby":{"fruit":"mater","food":",mifan"},"tags":["run","work"]}

		 */
	}
}
