package com.aizhixin.cloud.dataanalysis.common.init;

import com.aizhixin.cloud.dataanalysis.common.service.DistributeLock;
import com.aizhixin.cloud.dataanalysis.setup.service.WarningTypeService;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;

/**
 * @author: Created by jianwei.wu
 * @E-mail: wujianwei@aizhixin.com
 * @Date: 2018-01-04
 */
@Configuration
@Order(1000)
public class InitScheduling {
    private final static Logger LOG = LoggerFactory.getLogger(InitScheduling.class);
    private final static  int ZOOKEEPER_RETRY_TIMES = 3;            //zookeeper连接重试最大次数
    private final static  int ZOOKEEPER_RETRY_SLEEP_TIMES = 3000;   //zookeeper连接重试的间隔时间
    @Value("${zookeeper.connecton}")
    private String zkConnectString;
    @Value("${zookeeper.path}")
    private String zkLockPath;
    @Value("${zookeeper.task}")
    private String zkTaskPath;
    @Autowired
    private DistributeLock distributeLock;

    @PostConstruct
    public void cleanZookeeperTaskData() {
        CuratorFramework client = CuratorFrameworkFactory.newClient(zkConnectString, new RetryNTimes(ZOOKEEPER_RETRY_TIMES, ZOOKEEPER_RETRY_SLEEP_TIMES));
        try {
            client.start();
            StringBuilder lockPath = new StringBuilder(zkLockPath);
            StringBuilder taskPath = new StringBuilder(zkTaskPath);
            if(distributeLock.getLock(lockPath.toString(),taskPath.toString())) {
                client.delete().deletingChildrenIfNeeded().forPath(zkLockPath);
                client.delete().deletingChildrenIfNeeded().forPath(zkTaskPath);
            }
        } catch (Exception e) {
            LOG.error("删除锁路径({})和任务路径({})失败:{}", zkLockPath, zkTaskPath, e);
            e.printStackTrace();
        }finally{
            client.close();
        }
    }
}
