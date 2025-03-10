package cn.com.helei.common.util.http;

import cn.com.helei.common.entity.ProxyInfo;
import cn.com.helei.common.util.NamedThreadFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class RestApiClientFactory {

    private static final ConcurrentHashMap<ProxyInfo, RestApiClient> CLIENTS = new ConcurrentHashMap<>();

    private static final ExecutorService executor = Executors.newThreadPerTaskExecutor(new NamedThreadFactory("rest-api-client"));

    public static final RestApiClient DEFAULT = new RestApiClient(null, executor);

    public static RestApiClient getClient(ProxyInfo proxy) {
        if (proxy == null) {
            return DEFAULT;
        }

        return CLIENTS.compute(proxy, (k, v)->{
            if (v == null) {
                v = new RestApiClient(proxy, executor);
            }
            return v;
        });
    }
}
