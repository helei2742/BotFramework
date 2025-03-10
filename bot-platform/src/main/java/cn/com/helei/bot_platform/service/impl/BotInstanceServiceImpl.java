package cn.com.helei.bot_platform.service.impl;

import cn.com.helei.common.dto.PageResult;
import cn.com.helei.db_layer.mapper.BotInfoMapper;
import cn.com.helei.db_layer.mapper.IBaseMapper;
import cn.com.helei.db_layer.service.AbstractBaseService;
import cn.com.helei.common.entity.BotInfo;
import cn.com.helei.common.entity.BotInstance;
import cn.com.helei.db_layer.mapper.BotInstanceMapper;
import cn.com.helei.rpc.IBotInstanceRPC;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author com.helei
 * @since 2025-02-18
 */
@Slf4j
@DubboService
public class BotInstanceServiceImpl extends AbstractBaseService<BotInstanceMapper, BotInstance> implements IBotInstanceRPC {

    @Autowired
    private BotInfoMapper botInfoMapper;

    public BotInstanceServiceImpl() {
        super(botInstance -> {
            botInstance.setInsertDatetime(LocalDateTime.now());
            botInstance.setUpdateDatetime(LocalDateTime.now());
            botInstance.setIsValid(1);
        });
    }
    @Override
    public PageResult<BotInstance> conditionPageQuery(int page, int limit, Map<String, Object> filterMap) throws SQLException {

        PageResult<BotInstance> result = super.conditionPageQuery(page, limit, filterMap);

        // 填充botInfo
        List<Integer> botIds = result.getList().stream().map(BotInstance::getBotId).toList();
        Map<Integer, BotInfo> idMapBotInfo = botInfoMapper.selectBatchIds(botIds)
                .stream().collect(Collectors.toMap(BotInfo::getId, botInfo -> botInfo));

        for (BotInstance instance : result.getList()) {
            instance.setBotInfo(idMapBotInfo.get(instance.getBotId()));
        }

        return result;
    }


    @Override
    public Boolean existsBotInstance(BotInstance query) {
        return getBaseMapper().exists(new QueryWrapper<>(query));
    }
}
