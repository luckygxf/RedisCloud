1. 作客户端从配置中心拉取配置
------------------------------------------------
1. wredis从配置中心获取redis主从信息
    1.1 主要通过 sentinel get-master-addr-by-name获取master节点的信息
------------------------------------------------
1. web管理平台
    1. 部署主从需要重写
        1.1 主从使用两台机器131， 312，作为主从
        1.2 使用三台机器部署sentinel;端口相同，原来端口 + 20000
------------------------------------------------  
客户端上报
    需要上报的数据，协议
    1. monitor
    
    2. client
------------------------------------------------      
2. 配置中心：
    2.1
3. 管理平台部署redis实例做的事情
    3.1 展示redis实例信息
1. monitor监控
    1.3 动态发现agent
4. web管理平台生成appkey
    1. client从配置中心拉取app配置
    2. 起三个虚拟机，主从使用同一端口,sentinel使用三个端口
------------------------------------------------        
    1.1 获取redis实例信息
    1.2 redis配置添加max_memory配置项
------------------------------------------------    
1. monitor监控
    1.1 使用线程池，每隔1分钟向agent发送top命令
    1.2 页面显示
    1.3 动态发现agent
------------------------------------------------
1. monitor监控
    1.1 监控redis实例 redis客户端发送info命令
    1.2 监控机器信息 agent执行top命令
    1.3 monitor先做单节点monitor
    1.4 创建redis实例向etcd注册 /com/gxf/redis/cloud/applist/
    1.5 启动agent实例向etcd注册 /com/gxf/redis/cloud/machinelist/
    1.6 etcd单节点启动
    1.7 创建machine表，以及machine统计表
    1.8 etcd客户端从etcd拉取机器列表
    1.9 向agent发送信息执行top命令     
------------------------------------------------
4. 启动etcd，etcd客户端    
------------------------------------------------
1. 用spring boot做一个填ip、port的页面
2. 将数据用mybatis存到mysql中
------------------------------------------------
1. 添加sentinel 
    1.1 master 端口号：6340
    1.2 slave 端口号:6341
    1.3 sentinel 端口号: 26340、26341、26342
    这里添加sentinel 26343
2. 添加slave
    1.1 master 端口号：6340
    1.2 slave 端口号:6341
    1.3 sentinel 端口号: 26340、26341、26342
    这里添加slave 6342
------------------------------------------------
1. redis手动主从切换
------------------------------------------------
1. 下线实例和启动实例
    1.1 启动实例host,port master | slave | sentinel(都是执行redis-server + machinepath + config)
    1.2 下线实例host,port master | slave | sentinel(jedis客户端执行关闭操作)
    1.3 开发agent在指定端口启动redis实例,已经存在的reids实例，配置文件存在
------------------------------------------------
1. 通过web管理平台部署集群3主3从
    1.1 master 端口号：6440， 6441， 6442
    1.2 slave 端口号：6443， 6444， 6445
    1.3 开发web模块
        1.3.1 部署好的实例，配置为集群模式
    1.4 部署机器：3主3从
        1.4.1 路径：/work/clusterNode%port
        1.4.2 配置文件：/work/clusterNode%port/cnd%port.conf
------------------------------------------------
1. 通过web管理平台启动master、slave、3个sentinel
    1.1 master 端口号：6340
    1.2 slave 端口号:6341
    1.3 sentinel 端口号: 26340、26341、26342
    1.4 封装原生redis 提供configRewrite方法
------------------------------------------------
1. 通过web管理平台指定端口，在指定的端口起一个redis实例
    1.1 配置文件名：redis%d.conf
    1.2 配置文件路径:/work/redis%d/redis%d.conf
    1.3 项目部署路径:/work/redis%d/
    1.4 redis其余文件存放在/work/redis%d/
    1.5 进入测试阶段，agent能创建好redis配置文件夹和配置文件
------------------------------------------------
1. 集成mybatis访问mysql
2. 集成spring boot到web模块