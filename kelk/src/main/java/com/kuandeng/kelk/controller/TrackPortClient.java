package com.kuandeng.kelk.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/elk")
public class TrackPortClient {

	@Autowired
	private TransportClient transportClient;

	private java.util.List<String> listBySearchResponse(SearchHits hits) {
		java.util.List<String> list = new ArrayList<>();
		for (SearchHit searchHit : hits) {
			String sourceAsString = searchHit.getSourceAsString();
			list.add(sourceAsString);
		}
		return list;
	}

	@ResponseBody
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public Object test() throws InterruptedException, ExecutionException {
		SearchRequest request=new SearchRequest("forum");
		request.types("article");
		SearchSourceBuilder sourceBuilder=new SearchSourceBuilder();
		RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("postDate");
		rangeQuery.lte("2017-02-08").gte("2017-01-02");
		sourceBuilder.query(rangeQuery);
		sourceBuilder.sort("postDate", SortOrder.DESC);
		request.source(sourceBuilder);
		ActionFuture<SearchResponse> search = transportClient.search(request);
		SearchResponse actionGet = search.actionGet();
		return listBySearchResponse(actionGet.getHits());
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
