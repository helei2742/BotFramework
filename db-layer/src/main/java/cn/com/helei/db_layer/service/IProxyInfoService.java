package cn.com.helei.db_layer.service;

import cn.com.helei.common.dto.Result;
import cn.com.helei.common.entity.ProxyInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author com.helei
 * @since 2025-02-05
 */
public interface IProxyInfoService extends IService<ProxyInfo>, IBaseService<ProxyInfo>, ImportService {


    Result saveProxyInfos(List<Map<String, Object>> rawLines);

}
