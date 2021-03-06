package com.mengyan.xin.dal.plugins;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;

import java.util.Iterator;
import java.util.List;

/**
 * MyBatis自定义插件——根据Data Object非空字段Select数据库,如果字段为空则忽略该字段
 * 方法名: select
 */
public class SelectPlugin extends PluginAdapter {


    private final static String METHOD_NAME = "select";

    public SelectPlugin() {
        super();
    }

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        interfaze.addMethod(generateSelectMethod(introspectedTable));
        return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
    }


    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        XmlElement parentElement = document.getRootElement();
        parentElement.addElement(generateSelectMapper(introspectedTable));

        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }

    private Method generateSelectMethod(IntrospectedTable introspectedTable) {
        //方法名
        Method targetMethod = new Method(METHOD_NAME);
        //生成返回值 参数
        FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getNewListInstance();
        FullyQualifiedJavaType listAndParameterType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        FullyQualifiedJavaType secondParameterType = new FullyQualifiedJavaType("LogicDO");
        returnType.addTypeArgument(listAndParameterType);
        //设置方法
        targetMethod.setVisibility(JavaVisibility.PUBLIC);
        targetMethod.setReturnType(returnType);
        targetMethod.addParameter(new Parameter(listAndParameterType,"record"));
        targetMethod.addParameter(new Parameter(secondParameterType, "logicDO"));
        //添加到Interface
        context.getCommentGenerator().addGeneralMethodComment(targetMethod,introspectedTable);
        return targetMethod;
    }

    private XmlElement generateSelectMapper(IntrospectedTable introspectedTable) {
        //获得表名
        String tableName = introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime();//数据库表名
        //设定方法节点属性
        String methodName = METHOD_NAME;
        String resultMap = introspectedTable.getBaseResultMapId();
        String paramterType = introspectedTable.getBaseRecordType();
        //方法节点生成
        XmlElement methodElement = new XmlElement("select");
        methodElement.addAttribute(new Attribute("id", methodName));
        methodElement.addAttribute(new Attribute("resultMap",resultMap));
        methodElement.addAttribute(new Attribute("parameterType",paramterType));
        //TRIM节点生成
        XmlElement trimElement = new XmlElement("trim");
        trimElement.addAttribute(new Attribute("prefix","WHERE"));
        trimElement.addAttribute(new Attribute("prefixOverrides", "AND"));
        //TRIM节点循环
        Iterator<IntrospectedColumn> iterator = introspectedTable.getAllColumns()
                .iterator();
        //Column名称字符串Builder
        StringBuilder methodTextStr = new StringBuilder();
        //最后全空检查字符串
        StringBuilder nullTestStr = new StringBuilder();
        while (iterator.hasNext()) {
            IntrospectedColumn introspectedColumn = iterator.next();
            //获取#{}参数 数据库表Cloumn名 DO模型成员变量的名
            String parameterName = MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, "param1.");
            String columnName = MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn);
            String DOFieldName = introspectedColumn.getJavaProperty();
            //拼接COLUMN名
            methodTextStr.append(columnName);
            //全空检查字符串拼接
            nullTestStr.append("param1."+DOFieldName + " == null");
            if (iterator.hasNext()) {
                methodTextStr.append(", ");
                nullTestStr.append(" and ");
            }
            //TEST条件语句
            String testStr = "param1." + DOFieldName + " != null";
            //IF节点拼接
            XmlElement ifElement = new XmlElement("if");
            TextElement ifTextElement = new TextElement("AND " + columnName + "=" + parameterName);
            ifElement.addAttribute(new Attribute("test",testStr));
            ifElement.addElement(ifTextElement);
            //TRIM节点拼接
            trimElement.addElement(ifElement);
        }
        //最后一个参数全空的IF
        TextElement ifTextElement = new TextElement("AND 1=0");
        XmlElement ifElement = new XmlElement("if");
        ifElement.addAttribute(new Attribute("test",nullTestStr.toString()));
        ifElement.addElement(ifTextElement);
        trimElement.addElement(ifElement);

        //分页
        TextElement limitTextElement = new TextElement("limit #{param2.index,jdbcType=INTEGER},#{param2.offset,jdbcType=INTEGER}");
        XmlElement limitIfElement = new XmlElement("if");
        limitIfElement.addAttribute(new Attribute("test", "null != param2.offset and 0 != param2.offset"));
        limitIfElement.addElement(limitTextElement);

        //方法节点拼接
        TextElement methodTextElementFirst = new TextElement("select " + methodTextStr.toString());
        TextElement methodTextElementSecond = new TextElement("from " + tableName);

        methodElement.addElement(methodTextElementFirst);
        methodElement.addElement(methodTextElementSecond);
        methodElement.addElement(trimElement);
        methodElement.addElement(limitIfElement);

        return methodElement;
    }
}
