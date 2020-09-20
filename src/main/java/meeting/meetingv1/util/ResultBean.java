package meeting.meetingv1.util;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApiModel("api接口通用返回对象")
public class ResultBean {

    @ApiModelProperty(value = "状态码",dataType = "Integer")
    private int code;

    @ApiModelProperty(value="响应信息",dataType = "String")
    private String message;

    @ApiModelProperty(value = "数据", dataType = "Map")
    private Map data;

    private ResultBean() {

    }

    public static ResultBean error(int code, String message) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(code);
        resultBean.setMessage(message);
        return resultBean;
    }

    public static ResultBean success() {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(0);
        resultBean.setMessage("success");
        return resultBean;
    }
    public static ResultBean success(List list) {
        HashMap<String ,List> map = new HashMap<>();
        map.put("list",list);
        return success(map);
    }

    public static  ResultBean success(Map data) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(0);
        resultBean.setMessage("success");
        resultBean.setData(data);
        return resultBean;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map getData() {
        return data;
    }

    public void setData(Map data) {
        this.data = data;
    }
}
