package com.gw.common;

/**
 * @Author zhanghan
 * @Date 2021/5/27 15:47
 * @Description 枚举类
 */
public enum DictionaryEnum {

    TASK_STATUS_FINISH("任务状态","12",41,"已完成","1"),
    TASK_STATUS_WORKING("任务状态","12",51,"进行中","2"),
    TASK_STATUS_NO_GET("任务状态","12",52,"未领取","3"),
    TASK_RATE_V1("级别","13",42,"不锈钢-1","1"),
    TASK_RATE_V2("级别","13",43,"不锈钢-2","2"),
    TASK_RATE_V3("级别","13",44,"不锈钢-3","3"),
    TASK_RATE_V4("级别","13",45,"铝-1","4"),
    TASK_RATE_V5("级别","13",46,"铝-2","5"),
    TASK_RATE_V6("级别","13",47,"铝-3","6"),
    TASK_COMMENT_START_1("评价星级","15",53,"一星级","1"),
    TASK_COMMENT_START_2("评价星级","15",54,"二星级","2"),
    TASK_COMMENT_START_3("评价星级","15",55,"三星级","3"),
    TASK_COMMENT_START_4("评价星级","15",56,"四星级","4"),
    TASK_COMMENT_START_5("评价星级","15",57,"五星级","5");


    //类型名称
    private String typeName;

    //类型
    private String type;

    //字典表主键
    private int id;

    private String valueName;

    //码值
    private String value;

    DictionaryEnum(String typeName,String type,int id,String valueName,String value){
        this.typeName = typeName;
        this.type = type;
        this.id = id;
        this.valueName = valueName;
        this.value = value;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
