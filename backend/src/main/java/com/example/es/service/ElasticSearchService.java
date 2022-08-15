package com.example.es.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.mapping.*;
import co.elastic.clients.elasticsearch.core.ExistsRequest;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import com.alibaba.fastjson.JSONObject;
import com.example.es.client.ElasticSearchClient;
import com.example.es.core.Constants;
import com.example.es.core.EnumDataType;
import com.example.es.util.CommonUtil;
import com.example.es.util.readSetting.ReadJsonFile;

import java.io.IOException;
import java.util.*;

import static com.example.es.core.Constants.*;
import static com.example.es.core.EnumDataType.*;
import static com.example.es.core.EnumDataType.KEYWORD;

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

    private IndexCourseService getIndexCourseService() {
        return Objects.requireNonNullElseGet(indexCourseService, () -> indexCourseService = new IndexCourseService());
    }

    /**
     * 查看index是否在es中存在
     * @param  indexName, Name of the Index
     * @return true if found, false otherwise
     * @throws IOException, if something happened
     */

    public boolean existIndex(String indexName) throws IOException {
        boolean result;
        try {
            BooleanResponse booleanResponse = getElasticSearchClient().indices()
                    .exists(existRequest -> existRequest.index(indexName));
            result = booleanResponse.value();
        } catch (IOException e) {
            result = false;
        }
        // log.info("== {} 索引是否存在: {}", result);
        return result;
    }

    /**
     * 查看相关doc是否在index中存在
     * @param indexName, Name of the index;
     * @param id, ID of the document
     * @return true if found, false otherwise
     * @throws IOException,  if something happened
     */
    public boolean existDocument(String indexName, String id) throws IOException {
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


    /**
     * 删除相关 index
     * @param indexName, Name of th index
     * @return true if found, false otherwise
     * @throws IOException，if something happened
     */
    public boolean deleteIndex(String indexName) throws IOException {
        boolean result;
        try {
            DeleteIndexResponse deleteIndexResponse = getElasticSearchClient().indices().delete(d -> d.index(indexName));
            result = deleteIndexResponse.acknowledged();
        } catch (IOException e) {
             // log.info("delete index message", e.getMessage(), e);
            result = false;
        }
         // log.info("== {} index 是否被删除: {}", result);
        return result;

    }

    /**
     *
     * 创建index (不指定Mapping)
     * @param indexName Name of the index (needed)
     * @return true if created successfully, false otherwise
     * @throws IOException, if something happened
     */
    public boolean createIndex(String indexName) throws IOException {
        boolean result;
        try {
            CreateIndexResponse createIndexResponse = getElasticSearchClient()
                    .indices().create(createRequest -> createRequest.index(indexName));
            result = createIndexResponse.acknowledged();
        } catch (IOException e) {
            result = false;
        }
        // log.info("== {} index 是否被创建成功(不指定mapping的): {}", result);
        return result;
    }


    // 指定mapping创建index
    public Boolean createIndexWithMapping(String indexName) throws IOException {
        // 获取resource下对应的配置
        String getSetting = getReadJsonFile().readJsonSetting(
                CommonUtil.getResourceFilePath(COURSE_SETTING_JSON)
        );
        JSONObject settingObject = JSONObject.parseObject(getSetting);
        // 获取properties
        JSONObject properties = settingObject.getJSONObject(MAPPINGS).getJSONObject(PROPERTIES);
        //定义文档属性
        Map<String, Property> propertyMap = new HashMap<>();

        properties.keySet().forEach(key -> propertyMap.put(key, handleTypesProperties((String) properties.getJSONObject(key).get(TYPE))));

        // 设置索引的文档类型映射
        TypeMapping typeMapping = new TypeMapping.Builder()
                .properties(propertyMap)
                .build();
        CreateIndexRequest request = new CreateIndexRequest.Builder()
                .index(indexName)
                .mappings(typeMapping)
                .build();
        CreateIndexResponse createIndexResponse = getElasticSearchClient().indices().create(request);
        return createIndexResponse.acknowledged();
    }


    // 查看索引相关的信息
    public HashMap<String, Object> indexDetail(String indexName) throws IOException {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(Constants.INDEX_NAME, indexName);
        try {
            GetIndexResponse getIndexResponse = getElasticSearchClient().indices()
                    .get(getRequest -> getRequest.index(indexName));

            Map<String, Property> propertyMap = Objects.requireNonNull(Objects.requireNonNull(getIndexResponse.get(indexName)).mappings()).properties();
            hashMap.put(Constants.MESSAGE, propertyMap.keySet());
        } catch (IOException e) {
            hashMap.put(Constants.MESSAGE, e.getMessage());
        }
        return hashMap;
    }

    // 添加文档
    public void  addNewDocument() {

    }

    //获取文档信息
    public void getDocumentInfo() {

    }

    // 更新文档
    public void updateDocument() {

    }

    // 删除文档
    public void deleteDocument() {

    }

    // 批量插入文档
    public void bulkInsertDocument() {
        // 获取所有数据
        List<Object> list = getIndexCourseService().getCourseData();
        List<BulkOperation> bulkOperationArrayList = new ArrayList<>();
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
