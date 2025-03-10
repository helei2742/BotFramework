package cn.com.helei.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Boolean success;
    private String errorMsg;
    private Object data;

    public static Result ok(){
        return new Result(true, null, null);
    }
    public static Result ok(Object data){
        return new Result(true, null, data);
    }
    public static Result fail(String errorMsg){
        return new Result(false, errorMsg, null);
    }
}
