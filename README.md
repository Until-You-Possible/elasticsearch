# elasticsearch

前言
- 所有环境均在MAC(M1)上搭建
- 此demo使用elasticsearch最新版本8.3.x，springboot搭建java项目
- 所有demo使用真实的数据进行测试过，但是基于私有数据的安全考虑，只在本地做实验。
- 鉴于是本地做测试，建立ES客户端时，并未使用账号密码证书等相关涉及到权限的内容。
- 虽然是实验代码，代码也不断的优化中
- **ES核心的搜索内容(data, api)，后续单独整理，readme文档也会不断更新**

列举了elastic的基本的curd方法(相关方法名)

- 查看索引是否在ES中存在(existIndex)
- 查看文档是否在索引中存在(existDocument)
- 删除索引(deleteIndex)
- 创建索引(不指定mapping)(createIndex)
- 创建索引(指定mapping)(createIndexWithMapping)
- 查看索引相关的信息(indexDetail)
- 获取文档的信息(getDocumentInfo)
- 删除文档(deleteDocument)
- 向ES写入数据(fillIndexData)