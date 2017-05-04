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
 * MyBatis自定义插件——根据Key值Select数据库,Key值为Data Object字段名
 * 方法名: selectByKey
 */
public class SelectByKeyPlugin extends PluginAdapter {

    private final static String METHOD_NAME = "selectByKey";

    public SelectByKeyPlugin() {
        super();
    }

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        interfaze.addMethod(generateSelectByKeyMethod(introspectedTable));
        return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
    }


    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        XmlElement parentElement = document.getRootElement();
        parentElement.addElement(generateSelectByKeyMapper(introspectedTable));

        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }

    private Method generateSelectByKeyMethod(IntrospectedTable introspectedTable) {
        //方法名
        Method targetMethod = new Method(METHOD_NAME);
        //生成返回值
        FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getNewListInstance();
        FullyQualifiedJavaType listType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        returnType.addTypeArgument(listType);
        //生成参数
        FullyQualifiedJavaType parameterTypeFirst = listType;
        FullyQualifiedJavaType parameterTypeSecond = new FullyQualifiedJavaType("LogicDO");
        //设置方法
        targetMethod.setVisibility(JavaVisibility.PUBLIC);
        targetMethod.setReturnType(returnType);
        targetMethod.addParameter(new Parameter(parameterTypeFirst,"record"));
        targetMethod.addParameter(new Parameter(parameterTypeSecond, "logicDO"));
        //添加到Interface
        context.getCommentGenerator().addGeneralMethodComment(targetMethod,introspectedTable);
        return targetMethod;
    }

    private XmlElement generateSelectByKeyMapper(IntrospectedTable introspectedTable) {
        //获得表名
        String tableName = introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime();//数据库表名
        //设定方法节点属性
        String methodName = METHOD_NAME;
        String resultMap = introspectedTable.getBaseResultMapId();
        //方法节点生成
        XmlElement methodElement = new XmlElement("select");
        methodElement.addAttribute(new Attribute("id", methodName));
        methodElement.addAttribute(new Attribute("resultMap",resultMap));
        //CHOOSE节点生成
        XmlElement chooseElement = new XmlElement("choose");
        //TRIM节点循环
        Iterator<IntrospectedColumn> iterator = introspectedTable.getAllColumns()
                .iterator();
        //Column名称字符串Builder
        StringBuilder methodTextStr = new StringBuilder();
        //最后全空检查字符串
        while (iterator.hasNext()) {
            IntrospectedColumn introspectedColumn = iterator.next();
            //获取#{}参数 数据库表Cloumn名 DO模型成员变量的名
            String parameterName = MyBatis3FormattingUtilities.getParameterClause(introspectedColumn,"param1.");
            String columnName = MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn);
            String DOFieldName = introspectedColumn.getJavaProperty();
            //拼接COLUMN名
            methodTextStr.append(columnName);
            //全空检查字符串拼接
            if (iterator.hasNext()) {
                methodTextStr.append(", ");
            }
            //TEST条件语句
            String testStr = "\'" + DOFieldName + "\'" + " == param2.key";
            //WHEN节点拼接
            XmlElement whenElement = new XmlElement("when");
            TextElement whenTextElement = new TextElement(columnName + "=" + parameterName);
            whenElement.addAttribute(new Attribute("test",testStr));
            whenElement.addElement(whenTextElement);
            //TRIM节点拼接
            chooseElement.addElement(whenElement);
        }
        //最后一个参数全空的IF
        TextElement otherwiseTextElement = new TextElement("1=0");
        XmlElement otherwiseElement = new XmlElement("otherwise");
        otherwiseElement.addElement(otherwiseTextElement);
        chooseElement.addElement(otherwiseElement);

        //方法节点拼接
        TextElement methodTextElementFirst = new TextElement("select " + methodTextStr.toString());
        TextElement methodTextElementSecond = new TextElement("from " + tableName);
        TextElement methodTextElementThrid = new TextElement("where");

        //分页
        TextElement limitTextElement = new TextElement("limit #{param2.index,jdbcType=INTEGER},#{param2.offset,jdbcType=INTEGER}");
        XmlElement limitIfElement = new XmlElement("if");
        limitIfElement.addAttribute(new Attribute("test", "null != param2.offset and 0 != param2.offset"));
        limitIfElement.addElement(limitTextElement);

        methodElement.addElement(methodTextElementFirst);
        methodElement.addElement(methodTextElementSecond);
        methodElement.addElement(methodTextElementThrid);
        methodElement.addElement(chooseElement);
        methodElement.addElement(limitIfElement);

        return methodElement;
    }

}