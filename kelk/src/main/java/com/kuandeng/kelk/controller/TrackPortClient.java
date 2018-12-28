package com.kuandeng.kelk.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/elk")
public class TrackPortClient {

	@Autowired
	private TransportClient transportClient;

	public static void main(String[] args) {
		String str = "{\"type\":\"Feature\",\"geometry\":{\"type\":\"LineString\",\"coordinates\":[[[116.45768485632,39.96509901302],[116.45259957425,39.9605453452],[116.45175574526,39.95982855956],[116.45077438907,39.95895217194],[116.45058579558,39.95881661667],[116.45038272396,39.9587131992],[116.44957614438,39.95846802419],[116.44928076588,39.95832726994],[116.44902624239,39.95812890981],[116.44795760085,39.95716989054],[116.44779818589,39.95706073559],[116.44770221513,39.95703722857],[116.44759374881,39.95705250955],[116.44723275438,39.95722471325],[116.4466842971,39.95759048583],[116.44612628491,39.957936435],[116.4443417694,39.9591306621]]]},\"properties\":{\"track_id\":\"1000003_20180701073630032\",\"survey\":\"2\",\"recog\":\"1\",\"auto\":\"1\",\"fusion\":\"1\"}}";
		System.out.println(str);
		System.out.println(str.substring(0, str.indexOf("]]]}"))+"]]]}}");
	}

	@ResponseBody
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String test() throws FileNotFoundException {

		add1();
		return "success12121212";
	}

	/**
	 * 批量导入数据   成功了
	 * 
	 * @return
	 * @throws FileNotFoundException
	 */
	public String add1() throws FileNotFoundException {
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
			IndexRequest request = new IndexRequest("geomn", "geom");
			request.source(str,XContentType.JSON);
			transportClient.index(request).get();
		}
	}
	/**
	 * 批量导入数据   成功了
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
			int begin=str.indexOf("[[[");
			int end=str.indexOf("]]]");
			String beginStr=str.substring(begin,end);
			String endStr=beginStr.substring(3,beginStr.indexOf("]"));
			String finalStr="["+endStr+"]";
			str=str.replace("]]]", "],"+finalStr+"]]");
			System.out.println(str);
		//	String strModel=str.substring(0, str.indexOf("]]]}"))+"]]]}}";
			request.source(str,XContentType.JSON);
			transportClient.index(request).get();
		}
	}
	/**
	 {
  "mappings": {
    "geom": {
       "properties": {
          "location": {
             "type": "geo_shape"
          },
          
                            "type": {
                                "type": "text",
                                "fields": {
                                    "keyword": {
                                        "type": "keyword",
                                        "ignore_above": 256
                                    }
                                }
                            },"properties": {
                        "properties": {
                            "auto": {
                                "type": "text",
                                "fields": {
                                    "keyword": {
                                        "type": "keyword",
                                        "ignore_above": 256
                                    }
                                }
                            },
                            "fusion": {
                                "type": "text",
                                "fields": {
                                    "keyword": {
                                        "type": "keyword",
                                        "ignore_above": 256
                                    }
                                }
                            },
                            "recog": {
                                "type": "text",
                                "fields": {
                                    "keyword": {
                                        "type": "keyword",
                                        "ignore_above": 256
                                    }
                                }
                            },
                            "survey": {
                                "type": "text",
                                "fields": {
                                    "keyword": {
                                        "type": "keyword",
                                        "ignore_above": 256
                                    }
                                }
                            },
                            "track_id": {
                                "type": "text",
                                "fields": {
                                    "keyword": {
                                        "type": "keyword",
                                        "ignore_above": 256
                                    }
                                }
                            }
                        }
                    }
       }
    }
  }
}

	 */
	
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
