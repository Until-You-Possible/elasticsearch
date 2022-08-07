**es相关的文档记录**

```text

直接从官网下载es的软件包使用。在mac虽然可以方便的使用别brew，貌似 brew install elasticsearch 这样使用方法已经被废弃
另一方面对M1(芯片)电脑，部分软件还没有适配ARM架构，所以最好是能官网下载对应的软件包

```
***PS***

最新的es8的版本，需要注意的是es8开始，es是默认开启ssl认证的，启动es后，没有ssl的认证是不能访问9200端口的，
在本地做测试或者是学习的时候，可以修改es的配置

```yaml

# Enable security features
xpack.security.enabled: false

```

xpack.security.enabled, 默认是true，改为false，重启es即可。


***关于java REST的不同版本***

java REST客户端分为2中，以及相关特点：

**java low level REST**

- 最小依赖
- 负责均衡
- 故障转移
- 故障链接策略(是否重新链接故障节点，取决于链接失败多少次，失败的次数越多，再次尝试同一个节点之前，客户端等待的时间越长)
- 持久化链接
- 跟踪记录请求和响应
- 自动发现集节点

[参考资料](https://www.elastic.co/guide/en/elasticsearch/client/java-api-client/current/java-rest-low.html)



es的low level client是通过http协议于es服务进行通信的。请求编码和响应解码保留给用户实现，与所有es版本兼容

**java high level client**

high client其主要是公开特定的方法和API，基于low level client，并处理请求编码和响应解码。

此次demo使用的是7.7版本。主要参考es官方文档整理。






