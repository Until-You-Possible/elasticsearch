package com.example.es.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.mapping.*;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.DeleteResponse;
import co.elastic.clients.elasticsearch.core.ExistsRequest;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import com.example.es.client.ElasticSearchClient;
import com.example.es.core.EnumDataType;
import com.example.es.core.EnumIndexesType;
import com.example.es.util.readSetting.ReadJsonFile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

import static com.example.es.core.Constants.*;
import static com.example.es.core.EnumMessageType.BEEN_DELETED;
import static com.example.es.core.EnumMessageType.ES_STATUS_DOC_DEL;
import static com.example.es.core.EnumMessageType.NOT_FOUND_DOC;


@Service
public class ElasticSearchService {

    private ElasticsearchClient elasticsearchClient;

    private IndexCourseService indexCourseService;

    ReadJsonFile readJsonFile;

    private ElasticsearchClient getElasticSearchClient() {
        if (elasticsearchClient == null) {
            return elasticsearchClient = new ElasticSearchClient().getClient();
        }
        return elasticsearchClient;
    }

    private ReadJsonFile getReadJsonFile() {
        if (readJsonFile == null) {
            return  readJsonFile = new ReadJsonFile();
        }
        return readJsonFile;
    }

    // 查看index是否在es中存在
    public HashMap<String, Object> existIndex(String indexName) {
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            BooleanResponse booleanResponse = getElasticSearchClient().indices()
                    .exists(existRequest -> existRequest.index(indexName));
            hashMap.put(SUCCESS, booleanResponse.value());
        } catch (IOException e) {
            hashMap.put(SUCCESS, Boolean.FALSE);
            hashMap.put(MESSAGE, e.getMessage());
        }
        // log.info("== {} 索引是否存在: {}", result);
        return hashMap;
    }

    // 查看相关doc是否在index中存在
    public boolean existDocument(String indexName, String id) {
        ExistsRequest request = new ExistsRequest.Builder()
                .index(indexName)
                .id(id)
                .build();
        boolean result;
        try {
            BooleanResponse response = getElasticSearchClient().exists(request);
            result = response.value();
        } catch (IOException e) {
            result = false;
        }
        // log.info("== {} 文档是否存在: {}", result);
        return result;
    }

    // 删除相关 index
    public HashMap<String, Object> deleteIndex(String indexName) {
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            DeleteIndexResponse deleteIndexResponse = getElasticSearchClient().indices().delete(d -> d.index(indexName));
            hashMap.put(SUCCESS, deleteIndexResponse.acknowledged());
        } catch (IOException e) {
             // log.info("delete index message", e.getMessage(), e);
            hashMap.put(SUCCESS, Boolean.FALSE);
            hashMap.put(MESSAGE, e.getMessage());
        }
         // log.info("== {} index 是否被删除: {}", result);
        return hashMap;

    }

    // 创建index (不指定Mapping)
    public HashMap<String, Object> createIndex(String indexName){
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            CreateIndexResponse createIndexResponse = getElasticSearchClient()
                    .indices().create(createRequest -> createRequest.index(indexName));
            hashMap.put(SUCCESS, createIndexResponse.acknowledged());
        } catch (IOException e) {
            hashMap.put(SUCCESS, Boolean.FALSE);
            hashMap.put(MESSAGE, e.getMessage());
        }
        // log.info("== {} index 是否被创建成功(不指定mapping的): {}", result);
        return hashMap;
    }

    // 指定mapping创建index
    public HashMap<String, Object> createIndexWithMapping(String indexName) throws IOException {
        // 获取resource下对应的配置
         String fileName = EnumIndexesType.valueOf(indexName.toUpperCase()).getSettingName();
        InputStream inputStreamSettingJson = this.getClass().getClassLoader().getResourceAsStream(fileName);
        if(inputStreamSettingJson == null) {
            return createIndex(indexName);
        }
        CreateIndexRequest request = new CreateIndexRequest.Builder()
                .index(indexName)
                .withJson(inputStreamSettingJson)
                .build();
        CreateIndexResponse createIndexResponse = getElasticSearchClient().indices().create(request);
        HashMap<String, Object> hashMap  = new HashMap<>();
        hashMap.put(INDEX_NAME, indexName);
        hashMap.put(SUCCESS, createIndexResponse.acknowledged());
        return hashMap;
    }

    // 查看索引相关的信息
    public HashMap<String, Object> indexDetail(String indexName) throws IOException {
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            GetIndexResponse getIndexResponse = getElasticSearchClient().indices()
                    .get(getRequest -> getRequest.index(indexName));
            Map<String, Property> propertyMap = Objects.requireNonNull(Objects.requireNonNull(getIndexResponse.get(indexName)).mappings()).properties();
            hashMap.put(MESSAGE, propertyMap.keySet());
        } catch (IOException e) {
            hashMap.put(MESSAGE, e.getMessage());
        }
        hashMap.put(INDEX_NAME, indexName);
        return hashMap;
    }

    // 向ES写入数据
    public HashMap<String, Object> fillIndexData(List<Object> objectArrayList, String indexName) throws IOException {
        HashMap<String, Object> hashMap = new HashMap<>();
        if (Objects.requireNonNull(objectArrayList).isEmpty()) {
            hashMap.put(MESSAGE, "payload is empty!");
        }
        HashMap<String, HashMap<String, Object>> map = prepareFillIndexData(indexName);
        if ((boolean) map.get(RESULT).get(SUCCESS)) {
            hashMap.put(MESSAGE,  bulkInsertDocument(objectArrayList, indexName));
            hashMap.put(INDEX_NAME,  indexName);
        }
        return hashMap;
    }

    // 批量插入文档
    public HashMap<String, Object> bulkInsertDocument(List<Object> payload, String indexName) throws IOException {
        HashMap<String, Object> hashMap = new HashMap<>();
        List<BulkOperation> bulkOperationArrayList = new ArrayList<>();
        for(Object o : payload) {
            bulkOperationArrayList.add(BulkOperation.of(p -> p.index(i -> i.document(o))));
        }
        Instant beforeTime = Instant.now();
        BulkResponse bulkResponse = getElasticSearchClient().bulk(os -> os.index(indexName).operations(bulkOperationArrayList));
        Instant afterTime = Instant.now();
        hashMap.put(COUNT, bulkResponse.items().size());
        hashMap.put(SUCCESS, bulkResponse.errors());
        hashMap.put(COST_TIME, Duration.between(beforeTime,afterTime).getSeconds() + "(秒)");
        return hashMap;
    }

    // 写入数据之前的相关操作
    public HashMap<String, HashMap<String, Object>> prepareFillIndexData(String indexName) throws IOException {
        HashMap<String, HashMap<String, Object>> hashMap  = new HashMap<>();
        boolean boolExist = (boolean) existIndex(indexName).get(SUCCESS);
        if (boolExist) {
            hashMap.put(RESULT, deleteIndex(indexName));
        } else {
            hashMap.put(RESULT, createIndexWithMapping(indexName));
        }
        return hashMap;
    }

    //获取文档信息
    public HashMap<String, Object> getDocumentInfo(String indexName,  String id) throws IOException {
        HashMap<String, Object> hashMap = new HashMap<>();
        GetResponse<Object> response = getElasticSearchClient().get(g -> g
                .index(indexName)
                .id(id),
                Object.class
        );
        if (response.found()) {
            hashMap.put(SUCCESS, Boolean.TRUE);
            hashMap.put(RESULT, response.source());
        } else {
            hashMap.put(SUCCESS, Boolean.FALSE);
            hashMap.put(RESULT, NOT_FOUND);
        }
        return hashMap;
    }

    // 更新文档
    public void updateDocument() {

    }

    // 添加文档
    public void  addNewDocument() {

    }

    // 删除文档
    public HashMap<String, Object> deleteDocument(String indexName, String id) throws IOException {
        HashMap<String, Object> hashMap = new HashMap<>();
        DeleteResponse deleteResponse = getElasticSearchClient().delete(deleteRequest ->
                deleteRequest.index(indexName).id(id)
                );
        String result = deleteResponse.result().toString();
        if (Objects.equals(result, ES_STATUS_DOC_DEL.toString())) {
            hashMap.put(SUCCESS, Boolean.TRUE);
            hashMap.put(MESSAGE, BEEN_DELETED);
        } else {
            hashMap.put(SUCCESS, Boolean.FALSE);
            hashMap.put(MESSAGE, NOT_FOUND_DOC);
        }
        return hashMap;
    }

    // 处理mapping
    public Property handleTypesProperties(String name) {
        EnumDataType dataType = EnumDataType.valueOf(name.toUpperCase());
        switch (dataType) {
            case AGGREGATEMETRICDOUBLE:
                return new Property(new AggregateMetricDoubleProperty.Builder().build());
            case BINARY:
                return new Property(new BinaryProperty.Builder().build());
            case BOOLEAN:
                return new Property(new BooleanProperty.Builder().build());
            case COMPLETION:
                return new Property(new CompletionProperty.Builder().build());
            case KEYWORD:
                return new Property(new ConstantKeywordProperty.Builder().build());
            case DATENANOS:
                return new Property(new DateNanosProperty.Builder().build());
            case DATE:
                return new Property(new DateProperty.Builder().build());
            case DATERANGE:
                return new Property(new DateRangeProperty.Builder().build());
            case DENSEVECTOR:
                return new Property(new DenseVectorProperty.Builder().build());
            case DOUBLE:
                return new Property(new DoubleNumberProperty.Builder().build());
            case DOUBLERANGE:
                return new Property(new DoubleRangeProperty.Builder().build());
            case ALIAS:
                return new Property(new FieldAliasProperty.Builder().build());
            case FLATTENED:
                return new Property(new FlattenedProperty.Builder().build());
            case FLOAT:
                return new Property(new FloatNumberProperty.Builder().build());
            case FLOATRANGE:
                return new Property(new FloatRangeProperty.Builder().build());
            case GEOPONIT:
                return new Property(new GeoPointProperty.Builder().build());
            case GEOSHAPE:
                return new Property(new GeoShapeProperty.Builder().build());
            case HALFFLOAT:
                return new Property(new HalfFloatNumberProperty.Builder().build());
            case HISTOGRAM:
                return new Property(new HistogramProperty.Builder().build());
            case INTEGER:
                return new Property(new IntegerNumberProperty.Builder().build());
            case INTEGRERANGE:
                return new Property(new IntegerRangeProperty.Builder().build());
            case IP:
                return new Property(new IpProperty.Builder().build());
            case IPRANGE:
                return new Property(new IpRangeProperty.Builder().build());
            case JOIN:
                return new Property(new JoinProperty.Builder().build());
            case LONG:
                return new Property(new LongNumberProperty.Builder().build());
            case MATCHONLYTEXT:
                return new Property(new MatchOnlyTextProperty.Builder().build());
            case MURMUR3:
                return new Property(new Murmur3HashProperty.Builder().build());
            case NESTED:
                return new Property(new NestedProperty.Builder().build());
            case OBJECT:
                return new Property(new ObjectProperty.Builder().build());
            case PERCOLATOR:
                return new Property(new PercolatorProperty.Builder().build());
            case POINT:
                return new Property(new PointProperty.Builder().build());
            case RANKFEATURE:
                return new Property(new RankFeatureProperty.Builder().build());
            case SCALEDFLOAT:
                return new Property(new ScaledFloatNumberProperty.Builder().build());
            case SEARCHASYOUTYPE:
                return new Property(new SearchAsYouTypeProperty.Builder().build());
            case SHAPE:
                return new Property(new ShapeProperty.Builder().build());
            case SHORT:
                return new Property(new ShortNumberProperty.Builder().build());
            case TOKENCOUNT:
                return new Property(new TokenCountProperty.Builder().build());
            case UNSIGNEDLONG:
                return new Property(new UnsignedLongNumberProperty.Builder().build());
            case VERSION:
                return new Property(new VersionProperty.Builder().build());
            case WILDCARD:
                return new Property(new WildcardProperty.Builder().build());
            default:
                return  new Property(new TextProperty.Builder().build());
        }
    }

}

// 关于其他几种写如的方式，参考官方文档
// https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/current/indexing.html
