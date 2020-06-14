package com.lyl.yukon.common.redis;

import org.redisson.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis Repository
 *
 * @author ligj
 */
@Component("redissonDistributedLocker")
public class RedissonDistributedLocker implements DistributedLocker {
    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RedissonDistributedLocker.class);

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public RLock lock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock();
        return lock;
    }

    @Override
    //leaseTime为加锁时间，单位为秒
    public RLock lock(String lockKey, long leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(leaseTime, TimeUnit.SECONDS);
        return lock;
    }

    @Override
    //timeout为加锁时间，时间单位由unit确定
    public RLock lock(String lockKey, TimeUnit unit, long timeout) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(timeout, unit);
        return lock;
    }

    @Override
    //tryLock()，马上返回，拿到lock就返回true，不然返回false。
    //带时间限制的tryLock()，拿不到lock，就等一段时间，超时返回false.
    public boolean tryLock(String lockKey, TimeUnit unit, long waitTime, long leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            return false;
        }
    }

    @Override
    public void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.unlock();
    }

    @Override
    public void unlock(RLock lock) {
        lock.unlock();
    }

    /**
     * 获取集合
     *
     * @param lockKey
     * @return
     */
    @Override
    public <V> RSet<V> getRSet(String lockKey) {
        RSet<V> rSet = redissonClient.getSet(lockKey);
        return rSet;
    }

    /**
     * 获取有序集合
     *
     * @param lockKey
     * @return
     */
    @Override
    public <V> RSortedSet<V> getRSortedSet(String lockKey) {
        RSortedSet<V> sortedSet = redissonClient.getSortedSet(lockKey);
        return sortedSet;
    }

    /**
     * 获取队列
     *
     * @param lockKey
     * @return
     */
    @Override
    public <V> RQueue<V> getRQueue(String lockKey) {
        RQueue<V> rQueue = redissonClient.getQueue(lockKey);
        return rQueue;
    }

    /**
     * 获取记数锁
     *
     * @param lockKey
     * @return
     */
    @Override
    public RCountDownLatch getRCountDownLatch(String lockKey) {
        RCountDownLatch rCountDownLatch = redissonClient.getCountDownLatch(lockKey);
        return rCountDownLatch;
    }
}
