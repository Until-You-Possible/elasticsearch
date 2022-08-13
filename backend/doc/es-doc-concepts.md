**关于ES**

**什么是elasticsearch(ES)**

```text

Elasticsearch 是一个分布式、高扩展、高实时的搜索与数据分析引擎。它能很方便的使大量数据具有搜索、分析和探索的能力。
充分利用Elasticsearch的水平伸缩性，能使数据在生产环境变得更有价值。
Elasticsearch 的实现原理主要分为以下几个步骤，首先用户将数据提交到Elasticsearch 
数据库中，再通过分词控制器去将对应的语句分词，将其权重和分词结果一并存入数据，当用户搜索数据时候，再根据权重将结果排名，打分，再将返回结果呈现给用户。
Elasticsearch是与名为Logstash的数据收集和日志解析引擎以及名为Kibana的分析和可视化平台一起开发的。
这三个产品被设计成一个集成解决方案，称为“Elastic Stack”（以前称为“ELK stack”）。
Elasticsearch可以用于搜索各种文档。它提供可扩展的搜索，具有接近实时的搜索，并支持多租户。
Elasticsearch是分布式的，这意味着索引可以被分成分片，每个分片可以有0个或多个副本。每个节点托管一个或多个分片，并充当协调器将操作委托给正确的分片。
再平衡和路由是自动完成的。相关数据通常存储在同一个索引中，该索引由一个或多个主分片和零个或多个复制分片组成。一旦创建了索引，就不能更改主分片的数量。
Elasticsearch使用Lucene，并试图通过JSON和Java API提供其所有特性。
它支持facetting和percolating，如果新文档与注册查询匹配，这对于通知非常有用。
另一个特性称为“网关”，处理索引的长期持久性；例如，在服务器崩溃的情况下，可以从网关恢复索引。
Elasticsearch支持实时GET请求，适合作为NoSQL数据存储，但缺少分布式事务

```
