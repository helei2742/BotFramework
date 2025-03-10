package cn.com.helei.common.dto.job;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AutoBotJobWSParam implements Serializable {

    @Serial
    private static final long serialVersionUID = 984984151765154986L;


    private Boolean isRefreshWSConnection;

    private Boolean wsUnlimitedRetry;

    private Integer wsConnectCount;

    private Integer reconnectLimit;

    private Integer heartBeatIntervalSecond;

    private Integer reconnectCountDownSecond;

}
