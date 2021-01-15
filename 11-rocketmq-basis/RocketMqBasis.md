# RocketMQ介绍
## 一、RocketMQ各个角色的介绍
### 1.1 NameServer
管理Broker。集群是无状态的，NameServer集群是不需要数据同步的。

### 1.2 Broker
消息全部保存在Broker中，Broker自己会主动上报自己的状态信息给NameServer

### 1.3 Producer
生产者会把消息把发送到Broker，但是发送到哪个Broker？这个时候生产者会向NameServer询问可以向哪个Broker发送
数据。暂存和传输消息。

### 1.4 Consumer
消费者在消费数据的时候，会向NameServer询问，从哪个一个Broker获取消息，获取具体哪个Broker的地址。
消费可以

### Topic
区分消息的种类。

### Message Queue

## 二、Broker集群
### 2.1 集群的集中模式
* 单master
* 多master模式
* 多master多slave模式（同步复制）性能笔异步复制略低10%
* 多master多slave模式（异步复制）发生宕机时会丢失少了数据