package com.kubbo.proxy.http.netty;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * <title>NettyProxyConfig</title>
 * <p></p>
 *
 * @author zhuwei
 *         2014/10/13
 */
public class NettyProxyConfig {

    private int workerCount;

    public static NettyProxyConfig load(String resourceName) {

        Config innnerConfig = ConfigFactory.parseResources("proxy.properties");
        NettyProxyConfig nettyConfig = new NettyProxyConfig();
        nettyConfig.setWorkerCount(innnerConfig.getInt("proxy.worker.count"));
        return nettyConfig;
    }
    public int getWorkerCount() {
        return workerCount;
    }

    public void setWorkerCount(int workerCount) {
        this.workerCount = workerCount;
    }
}