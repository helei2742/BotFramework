package cn.com.helei.db_layer.service;

import cn.com.helei.common.dto.Result;
import cn.com.helei.common.entity.TwitterAccount;
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
public interface ITwitterAccountService extends IService<TwitterAccount>, IBaseService<TwitterAccount>, ImportService {

    Result saveTwitters(List<Map<String, Object>> rawLines);

}
