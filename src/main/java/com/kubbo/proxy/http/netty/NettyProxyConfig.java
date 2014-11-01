package com.kubbo.proxy.http.netty;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <title>NettyProxyConfig</title>
 * <p></p>
 *
 * @author zhuwei
 *         2014/10/13
 */
public class NettyProxyConfig {

    private static final Logger logger = LoggerFactory.getLogger(NettyProxyConfig.class);

    private int workerCount;
    private int bossCount;
    public static NettyProxyConfig load(String resourceName) {

        Config innerConfig = ConfigFactory.parseResources("proxy.conf");
        logger.info("load config {}", innerConfig.toString());
        NettyProxyConfig nettyConfig = new NettyProxyConfig();
        nettyConfig.setWorkerCount(innerConfig.getInt("proxy.worker"));
        nettyConfig.setBossCount(innerConfig.getInt("proxy.boss"));
        return nettyConfig;
    }
    public int getWorkerCount() {
        return workerCount;
    }

    public void setWorkerCount(int workerCount) {
        this.workerCount = workerCount;
    }


    public int getBossCount() {
        return bossCount;
    }

    public void setBossCount(int bossCount) {
        this.bossCount = bossCount;
    }
}