package com.aizhixin.cloud.dataanalysis.common.service;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.aizhixin.cloud.dataanalysis.common.util.DateUtil;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁工具
 * 部署多个节点的时候，对调度任务限制只能在其中一个节点启动
 */
@Component
public class DistributeLock {
    private final static  Logger LOG = LoggerFactory.getLogger(DistributeLock.class);
    private final static  int ZOOKEEPER_RETRY_TIMES = 3;            //zookeeper连接重试最大次数
    private final static  int ZOOKEEPER_RETRY_SLEEP_TIMES = 3000;   //zookeeper连接重试的间隔时间
    private final static  int TASK_LOCKED_TIME_SECOND = 30;         //任务锁定的最大时间(秒)
    @Value("${zookeeper.connecton}")
    private String zkConnectString;
    @Value("${zookeeper.path}")
    private String zkLockPath;
    @Value("${zookeeper.task}")
    private String zkTaskPath;

    /**
     * 获取分布式锁的核心逻辑
     * @param lockPath      锁路径
     * @param taskPath      任务路径
     * @return              获取锁是否成功
     */
    public boolean getLock(String lockPath, String taskPath) {
        boolean locked = false;
        CuratorFramework client = CuratorFrameworkFactory.newClient(zkConnectString, new RetryNTimes(ZOOKEEPER_RETRY_TIMES, ZOOKEEPER_RETRY_SLEEP_TIMES));
        InterProcessMutex lock = null;
        try {
            client.start();
            lock = new InterProcessMutex(client, lockPath);
            if (lock.acquire(TASK_LOCKED_TIME_SECOND, TimeUnit.SECONDS)) {
                Stat stat = client.checkExists().forPath(taskPath);
                if (null == stat) {
                    client.create().creatingParentContainersIfNeeded().forPath(taskPath);
                    locked = true;
                }
            }
        } catch (Exception e) {
            LOG.warn("获取锁失败", e);
            e.printStackTrace();
        } finally {
            if (null != lock) {
                try {
                    lock.release();
                } catch (Exception e) {
                    LOG.warn("释放锁失败", e);
                    e.printStackTrace();
                }
            }
            client.close();
        }
        return locked;
    }

    public void delete() {
        CuratorFramework client = CuratorFrameworkFactory.newClient(zkConnectString, new RetryNTimes(ZOOKEEPER_RETRY_TIMES, ZOOKEEPER_RETRY_SLEEP_TIMES));
        try {
            client.start();
            client.delete().deletingChildrenIfNeeded().forPath(zkLockPath);
            client.delete().deletingChildrenIfNeeded().forPath(zkTaskPath);
        } catch (Exception e) {
            LOG.warn("删除锁路径({})和任务路径({})失败:{}", zkLockPath, zkTaskPath, e);
            e.printStackTrace();
        }finally{
        	client.close();
        }
    }


    /**
     * 报到注册预警
     * @return  是否获取到锁
     */
    public boolean getStuRegisterLock() {
        StringBuilder lockPath = new StringBuilder(zkLockPath);
        StringBuilder taskPath = new StringBuilder(zkTaskPath);
        Date current = new Date();
        String curDayString = DateUtil.format(current);
        String HHmm = DateUtil.format(current, "HHmm");
        lockPath.append("/").append(curDayString).append("/register/").append(HHmm);
        taskPath.append("/").append(curDayString).append("/register/").append(HHmm);

        return getLock(lockPath.toString(), taskPath.toString());
    }
    
    /**
     * 旷课预警
     * @return  是否获取到锁
     */
    public boolean getRollCallLock() {
        StringBuilder lockPath = new StringBuilder(zkLockPath);
        StringBuilder taskPath = new StringBuilder(zkTaskPath);
        Date current = new Date();
        String curDayString = DateUtil.format(current);
        String HHmm = DateUtil.format(current, "HHmm");
        lockPath.append("/").append(curDayString).append("/outsch/").append(HHmm);
        taskPath.append("/").append(curDayString).append("/outsch/").append(HHmm);

        return getLock(lockPath.toString(), taskPath.toString());
    }
    
    public boolean getRollCallDayLock() {
        StringBuilder lockPath = new StringBuilder(zkLockPath);
        StringBuilder taskPath = new StringBuilder(zkTaskPath);
        Date current = new Date();
        String curDayString = DateUtil.format(current);
        String HHmm = DateUtil.format(current, "HHmm");
        lockPath.append("/").append(curDayString).append("/rcallday/").append(HHmm);
        taskPath.append("/").append(curDayString).append("/rcallday/").append(HHmm);

        return getLock(lockPath.toString(), taskPath.toString());
    }

    /**
     * 旷课信息统计
     * @return  是否获取到锁
     */
    public boolean getRollCallCountLock() {
        StringBuilder lockPath = new StringBuilder(zkLockPath);
        StringBuilder taskPath = new StringBuilder(zkTaskPath);
        Date current = new Date();
        String curDayString = DateUtil.format(current);
        String HHmm = DateUtil.format(current, "HHmm");
        lockPath.append("/").append(curDayString).append("/outct/").append(HHmm);
        taskPath.append("/").append(curDayString).append("/outct/").append(HHmm);

        return getLock(lockPath.toString(), taskPath.toString());
    }
    
    /**
     * 总评成绩预警
     * @return  是否获取到锁
     */
    public boolean getTotalScoreLock() {
        StringBuilder lockPath = new StringBuilder(zkLockPath);
        StringBuilder taskPath = new StringBuilder(zkTaskPath);
        Date current = new Date();
        String curDayString = DateUtil.format(current);
        String HHmm = DateUtil.format(current, "HHmm");
        lockPath.append("/").append(curDayString).append("/total/").append(HHmm);
        taskPath.append("/").append(curDayString).append("/total/").append(HHmm);

        return getLock(lockPath.toString(), taskPath.toString());
    }

    /**
     * 总评不及格成绩信息统计
     * @return  是否获取到锁
     */
    public boolean getTotalScoreCountLock() {
        StringBuilder lockPath = new StringBuilder(zkLockPath);
        StringBuilder taskPath = new StringBuilder(zkTaskPath);
        Date current = new Date();
        String curDayString = DateUtil.format(current);
        String HHmm = DateUtil.format(current, "HHmm");
        lockPath.append("/").append(curDayString).append("/totalct/").append(HHmm);
        taskPath.append("/").append(curDayString).append("/totalct/").append(HHmm);

        return getLock(lockPath.toString(), taskPath.toString());
    }
    
    
    /**
     * 补考成绩预警
     * @return  是否获取到锁
     */
    public boolean getMakeUpScoreLock() {
        StringBuilder lockPath = new StringBuilder(zkLockPath);
        StringBuilder taskPath = new StringBuilder(zkTaskPath);
        Date current = new Date();
        String curDayString = DateUtil.format(current);
        String HHmm = DateUtil.format(current, "HHmm");
        lockPath.append("/").append(curDayString).append("/makeup/").append(HHmm);
        taskPath.append("/").append(curDayString).append("/makeup/").append(HHmm);

        return getLock(lockPath.toString(), taskPath.toString());
    }

    /**
     * 补考不及格成绩信息统计
     * @return  是否获取到锁
     */
    public boolean getMakeUpScoreCountLock() {
        StringBuilder lockPath = new StringBuilder(zkLockPath);
        StringBuilder taskPath = new StringBuilder(zkTaskPath);
        Date current = new Date();
        String curDayString = DateUtil.format(current);
        String HHmm = DateUtil.format(current, "HHmm");
        lockPath.append("/").append(curDayString).append("/makeupct/").append(HHmm);
        taskPath.append("/").append(curDayString).append("/makeupct/").append(HHmm);

        return getLock(lockPath.toString(), taskPath.toString());
    }
    
    /**
     * 之前两学期成绩统计
     * @return  是否获取到锁
     */
    public boolean getScoreFluctuateCountLock() {
        StringBuilder lockPath = new StringBuilder(zkLockPath);
        StringBuilder taskPath = new StringBuilder(zkTaskPath);
        Date current = new Date();
        String curDayString = DateUtil.format(current);
        String HHmm = DateUtil.format(current, "HHmm");
        lockPath.append("/").append(curDayString).append("/flucount/").append(HHmm);
        taskPath.append("/").append(curDayString).append("/flucount/").append(HHmm);

        return getLock(lockPath.toString(), taskPath.toString());
    }

    /**
     * 成绩波动预警统计
     * @return  是否获取到锁
     */
    public boolean getScoreFluctuateLock() {
        StringBuilder lockPath = new StringBuilder(zkLockPath);
        StringBuilder taskPath = new StringBuilder(zkTaskPath);
        Date current = new Date();
        String curDayString = DateUtil.format(current);
        String HHmm = DateUtil.format(current, "HHmm");
        lockPath.append("/").append(curDayString).append("/fluct/").append(HHmm);
        taskPath.append("/").append(curDayString).append("/fluct/").append(HHmm);

        return getLock(lockPath.toString(), taskPath.toString());
    }
    
   
    /**
     * 修读异常预警统计
     * @return  是否获取到锁
     */
    public boolean getAttendAbnormalLock() {
        StringBuilder lockPath = new StringBuilder(zkLockPath);
        StringBuilder taskPath = new StringBuilder(zkTaskPath);
        Date current = new Date();
        String curDayString = DateUtil.format(current);
        String HHmm = DateUtil.format(current, "HHmm");
        lockPath.append("/").append(curDayString).append("/attend/").append(HHmm);
        taskPath.append("/").append(curDayString).append("/attend/").append(HHmm);

        return getLock(lockPath.toString(), taskPath.toString());
    }
    
    /**
     * 英语四级考试
     * @return  是否获取到锁
     */
    public boolean getCet4ScoreJobLock() {
        StringBuilder lockPath = new StringBuilder(zkLockPath);
        StringBuilder taskPath = new StringBuilder(zkTaskPath);
        Date current = new Date();
        String curDayString = DateUtil.format(current);
        String HHmm = DateUtil.format(current, "HHmm");
        lockPath.append("/").append(curDayString).append("/cet4/").append(HHmm);
        taskPath.append("/").append(curDayString).append("/cet4/").append(HHmm);

        return getLock(lockPath.toString(), taskPath.toString());
    }
    
    /**
     * 退学预警
     * @return
     */
    public boolean getDropOutJobLock() {
        StringBuilder lockPath = new StringBuilder(zkLockPath);
        StringBuilder taskPath = new StringBuilder(zkTaskPath);
        Date current = new Date();
        String curDayString = DateUtil.format(current);
        String HHmm = DateUtil.format(current, "HHmm");
        lockPath.append("/").append(curDayString).append("/dropout/").append(HHmm);
        taskPath.append("/").append(curDayString).append("/dropout/").append(HHmm);

        return getLock(lockPath.toString(), taskPath.toString());
    }
    
    
    /**
     * 修改预警处理状态
     * @return  是否获取到锁
     */
    public boolean updateWarnStateJobLock() {
        StringBuilder lockPath = new StringBuilder(zkLockPath);
        StringBuilder taskPath = new StringBuilder(zkTaskPath);
        Date current = new Date();
        String curDayString = DateUtil.format(current);
        String HHmm = DateUtil.format(current, "HHmm");
        lockPath.append("/").append(curDayString).append("/updstate/").append(HHmm);
        taskPath.append("/").append(curDayString).append("/updstate/").append(HHmm);

        return getLock(lockPath.toString(), taskPath.toString());
    }
    
    
    public boolean getTeachingScheduleLock() {
        StringBuilder lockPath = new StringBuilder(zkLockPath);
        StringBuilder taskPath = new StringBuilder(zkTaskPath);
        Date current = new Date();
        String curDayString = DateUtil.format(current);
        String HHmm = DateUtil.format(current, "HHmm");
        lockPath.append("/").append(curDayString).append("/teasch/").append(HHmm);
        taskPath.append("/").append(curDayString).append("/teasch/").append(HHmm);

        return getLock(lockPath.toString(), taskPath.toString());
    }


    public boolean getWarningSettingsOnAndOffScheduleLock() {
        StringBuilder lockPath = new StringBuilder(zkLockPath);
        StringBuilder taskPath = new StringBuilder(zkTaskPath);
        Date current = new Date();
        String curDayString = DateUtil.format(current);
//        String HHmm = DateUtil.format(current, "HHmm");
        lockPath.append("/").append(curDayString).append("/warningsettings");
        taskPath.append("/").append(curDayString).append("/warningsettings");
        return getLock(lockPath.toString(), taskPath.toString());
    }


    public void cleanZookeeperTaskData() {
        delete();
    }
    /****************************************学情资源锁***********************************************/

    /**
     * 获取/添加  资源锁
     * @return
     */
    public boolean getLock(StringBuilder path) {
        StringBuilder lockPath = new StringBuilder(zkLockPath);
        StringBuilder taskPath = new StringBuilder(zkTaskPath);
        lockPath.append(path);
        taskPath.append(path);
        return getLock(lockPath.toString(), taskPath.toString());
    }

    /**
     * 释放资源
     * @return
     */
    public void delete(StringBuilder path) {
        CuratorFramework client = CuratorFrameworkFactory.newClient(zkConnectString, new RetryNTimes(ZOOKEEPER_RETRY_TIMES, ZOOKEEPER_RETRY_SLEEP_TIMES));
        try {
            client.start();
            client.delete().deletingChildrenIfNeeded().forPath(zkLockPath+path);
            client.delete().deletingChildrenIfNeeded().forPath(zkTaskPath+path);
        } catch (Exception e) {
            LOG.warn("删除锁路径({})和任务路径({})失败:{}", zkLockPath+path, zkTaskPath+path, e);
            e.printStackTrace();
        }finally{
            client.close();
        }
    }





}
