package student.forum.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonErr {
    PARAM_WRONG(40000, "参数范围或格式错误"),
    NETWORK_WRONG(40001, "网络错误"),
    REQUEST_NOT_ALLOW(40002, "当前条件或时间不允许〒▽〒"),
    REQUEST_FREQUENTLY(40003, "请求繁忙，请稍后再试"),
    CONTENT_NOT_FOUND(40004, "你要找的东西好像走丢啦X﹏X"),
    METHOD_NOT_ALLOW(40005, "方法不允许"),
    THIS_IS_LAST_PAGE(40006, "这是最后一页，再怎么找也没有啦"),
    THIS_IS_FIRST_PAGE(40007, "没有上一页啦"),
    FILE_FORMAT_ERROR(40008,"文件格式错误"),
    CONNECT_TO_MYSQL_FAILED(40009,"数据获取异常了惹T_T"),
    NO_DATA(40010,"没有找到结果哦QwQ"),
    TOKEN_CHECK_FAILED(40011,"token检查有误"),
    OPERATE_REPEAT(40012,"重复的操作"),
    FILE_OUT_OF_LIMIT(40013,"文件大小超出限制!"),
    ARTICLE_IS_DELETE(40014,"好像已经被删除了呢"),
    SQL_NOT_ALLOWED_IN_STRING(40015,"包含不允许的字符!"),
    NO_AUTHORITY(40016,"你不能访问这里哦!");

    private final int code;
    private String message;

    CommonErr(int code) {
        this.code = code;
    }

    public CommonErr setMsg(String msg) {
        message = msg;
        return this;
    }
}
