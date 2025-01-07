package com.example.demo.sql;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;

public class SqlParser {
    
    public String addDataPermission(String sql, String dataPermissionCondition) throws JSQLParserException {
        Statement statement = CCJSqlParserUtil.parse(sql);
        
        if (statement instanceof Select) {
            Select selectStatement = (Select) statement;
            SelectBody selectBody = selectStatement.getSelectBody();
            
            if (selectBody instanceof PlainSelect) {
                processPlainSelect((PlainSelect) selectBody, dataPermissionCondition);
            } else if (selectBody instanceof SetOperationList) {
                processSetOperationList((SetOperationList) selectBody, dataPermissionCondition);
            }
            
            return statement.toString();
        }
        
        return sql;
    }
    
    private void processPlainSelect(PlainSelect plainSelect, String dataPermissionCondition) throws JSQLParserException {
        Expression whereExpression = plainSelect.getWhere();
        Expression permissionExpression = CCJSqlParserUtil.parseCondExpression(dataPermissionCondition);
        
        if (whereExpression == null) {
            plainSelect.setWhere(permissionExpression);
        } else {
            plainSelect.setWhere(new AndExpression(whereExpression, permissionExpression));
        }
    }
    
    private void processSetOperationList(SetOperationList setOperationList, String dataPermissionCondition) throws JSQLParserException {
        for (SelectBody selectBody : setOperationList.getSelects()) {
            if (selectBody instanceof PlainSelect) {
                processPlainSelect((PlainSelect) selectBody, dataPermissionCondition);
            }
        }
    }
} 