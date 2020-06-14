package com.lyl.yukon.common.redis;

import org.redisson.api.*;

import java.util.concurrent.TimeUnit;

public interface DistributedLocker {
    RLock lock(String lockKey);

    RLock lock(String lockKey, long timeout);

    RLock lock(String lockKey, TimeUnit unit, long timeout);

    boolean tryLock(String lockKey, TimeUnit unit, long waitTime, long leaseTime);

    void unlock(String lockKey);

    void unlock(RLock lock);

    RCountDownLatch getRCountDownLatch(String lockKey);

    <V> RQueue<V> getRQueue(String lockKey);

    <V> RSortedSet<V> getRSortedSet(String lockKey);

    <V> RSet<V> getRSet(String lockKey);

}
