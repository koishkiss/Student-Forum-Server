package student.forum.util;

import student.forum.core.exception.CheckException;

import java.util.ArrayList;
import java.util.List;

public class ConditionalSqlMaker {

    private final List<String> conditions;

    public ConditionalSqlMaker() {
        conditions = new ArrayList<>();
    }

    //获得条件查询Sql
    public String getConditionSql() {
        if (conditions.isEmpty()) return "1=1";
        else if (conditions.size() == 1) return conditions.get(0);
        StringBuilder conditionSql = new StringBuilder(conditions.get(0));
        for (int i = 1; i < conditions.size(); i++) {
            conditionSql.append(" AND ").append(conditions.get(i));
        }
        return conditionSql.toString();
    }

    //添加条件
    public ConditionalSqlMaker addCondition(String field, Object value) {

        //值为null，不添加此条件
        if (value == null) {
            return this;
        }

        //值为String类型，默认开启检查
        if (value instanceof String) {
            return addStringCondition(field,(String) value,true);
        }

        conditions.add(String.format("%s='%s'",field,value));
        return this;
    }

    public ConditionalSqlMaker addStringCondition(String field, String value, boolean check) {

        if (value == null) {
            return this;
        }

        if (check && CheckUtil.checkSQL(value)) {
            throw new CheckException("包含非法的字符!");
        }

        conditions.add(String.format("%s='%s'",field,value));
        return this;
    }

    public ConditionalSqlMaker addStringCondition(String field, String value) {
        return addStringCondition(field,value,true);
    }

    public ConditionalSqlMaker addSearchCondition(String field, String search) {

        if (search == null) {
            return this;
        }

        if (CheckUtil.checkSQL(search)) {
            throw new CheckException("包含非法的字符!");
        }

        conditions.add(String.format("LOCATE('%s',%s)>0",search,field));
        return this;
    }

}
