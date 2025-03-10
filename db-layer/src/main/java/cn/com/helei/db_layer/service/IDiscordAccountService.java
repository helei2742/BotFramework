package cn.com.helei.db_layer.service;

import cn.com.helei.common.dto.Result;
import cn.com.helei.common.entity.DiscordAccount;
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
public interface IDiscordAccountService extends IService<DiscordAccount>, IBaseService<DiscordAccount>, ImportService {

    Result saveDiscordAccounts(List<Map<String, Object>> rawLines);

}
