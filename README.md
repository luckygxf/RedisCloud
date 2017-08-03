1. 系统架构
    web + agent + pb
2. web
    spring + mybatis
3. 序列化框架比较
    主流框架，进行对比
4. 部署
    agent:
        1. 部署在机器的目录:/opt/redis_cloud
5. redis java客户端使用封装的WJedis
6. etcd存放的数据
    1. machineliest:/com/gxf/redis/cloud/machinelist/ip
    2. applist:/com/gxf/redis/cloud/applist/host:port
7. monitor:
    1. redis info没有获取到内容，redis可以判为下线，可以作为监控，发送通知给负责人进行处理