package com.gmail.maxdiland.drebedengireports.util.sql;

import com.gmail.maxdiland.drebedengireports.util.sql.SqlLogicalOperator;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * author Maksim Diland
 */
public final class SqlUtil {

    private static final String SINGLE_QUOTE_WRAP_PATTERN = "'%s'";
    private static final String FUNCTION_PATTERN = "%s(%s)";
    private static final String CONTAINING_PHRASE_LIKE_CLAUSE_PATTERN = "%%%s%%";

    private SqlUtil() {}

    public static String wrapWithSingleQuotes(String stringToWrap) {
        return String.format(SINGLE_QUOTE_WRAP_PATTERN, stringToWrap);
    }

    public static String buildAndClause(String ... clauses) {
        return buildLogicalClause(SqlLogicalOperator.AND, clauses);
    }

    public static String buildOrClause(String ... clauses) {
        return buildLogicalClause(SqlLogicalOperator.OR, clauses);
    }

    public static String buildLogicalClause(SqlLogicalOperator operator, String ... clauses) {
        String[] filteredClauses = filterEmptyClauses(clauses);
        return StringUtils.join(filteredClauses, operator.getSqlRepresentationWithSpaces());
    }

    private static String[] filterEmptyClauses(String[] clauses) {
        if ( ArrayUtils.isEmpty(clauses) ) {
            return new String[0];
        }

        List<String> filteredClauses = new ArrayList<String>();
        for (String clause : clauses) {
            if ( StringUtils.isNotEmpty(clause) ) {
                filteredClauses.add(clause);
            }
        }
        return filteredClauses.toArray(new String[filteredClauses.size()]);
    }

    public static String buildEqualsClause(String columnName, String value) {
        return buildClause(columnName, SqlComparisonOperator.EQUAL, value);
    }

    public static String buildEqualsClause(String columnName, Number value) {
        return buildClause(columnName, SqlComparisonOperator.EQUAL, value);
    }

    public static String buildClause(String leftStatement, SqlComparisonOperator operator,
                                     String rightStatement) {
        return leftStatement + operator.getSqlRepresentationWithSpaces() + rightStatement;
    }

    public static String buildClause(String leftStatement, SqlComparisonOperator operator,
                                     Number value) {
        return buildClause(leftStatement, operator, String.valueOf(value));
    }

    public static String buildLikeClause(String columnName, String value) {
        return buildClause(columnName, SqlComparisonOperator.LIKE, wrapWithSingleQuotes(value));
    }

    public static String buildLikeClauseContainingPhrase(String columnName, String value) {
        return buildClause(
                columnName,
                SqlComparisonOperator.LIKE,
                wrapWithSingleQuotes(
                        String.format(CONTAINING_PHRASE_LIKE_CLAUSE_PATTERN, value)
                )
        );
    }

    public static String buildFunctionStatement(String functionName, String ... functionArgs) {
        String joinedFunctionArguments = StringUtils.join(functionArgs, " ,");
        return String.format(FUNCTION_PATTERN, functionName, joinedFunctionArguments);
    }

    public static String buildGreaterEqualsCondition(String leftClause, String rightClause) {
        return buildClause(leftClause, SqlComparisonOperator.GREATER_EQUAL, rightClause);
    }

    public static String buildLessCondition(String leftStatement, String rightStatement) {
        return buildClause(leftStatement, SqlComparisonOperator.LESS_THAN, rightStatement);
    }

    public static String buildCriteria(String where, String groupBy,
                                       String having, String orderBy) {
        String criteria = "";

        if (StringUtils.isNotEmpty(where)) {
            criteria += SqlKeyWord.WHERE.getSqlRepresentationWithSpaces() + where;
        }

        if (StringUtils.isNotEmpty(groupBy)) {
            criteria += SqlKeyWord.GROUP_BY.getSqlRepresentationWithSpaces() + groupBy;
        }

        if (StringUtils.isNotEmpty(having)) {
            criteria += SqlKeyWord.HAVING.getSqlRepresentationWithSpaces() + having;
        }

        if (StringUtils.isNotEmpty(orderBy)) {
            criteria += SqlKeyWord.ORDER_BY.getSqlRepresentationWithSpaces() + orderBy;
        }

        return criteria;
    }
}
