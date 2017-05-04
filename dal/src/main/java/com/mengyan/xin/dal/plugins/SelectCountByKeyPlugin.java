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
import org.mybatis.generator.internal.util.JavaBeansUtil;

import java.util.Iterator;
import java.util.List;

/**
 * Deprecated 插件弃用
 */
public class SelectCountByKeyPlugin extends PluginAdapter {

    private final static String METHOD_NAME = "selectCountByKey";
    private final static String RANK_RESULT_MAP = "RankResultMap";
    private final static String RANK_DO_CLASS = "com.mengyan.xin.dal.model.RankDO";

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        interfaze.addMethod(generateTargetMehod(introspectedTable));
        return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        XmlElement parentElement = document.getRootElement();
        parentElement.addElement(generateRankResultMap());
        parentElement.addElement(generateTragetSQLMap(introspectedTable));
        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
//        addTargetField(topLevelClass,"columnCount",Integer.class);
//        addTargetField(topLevelClass,"key",String.class);
        return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
    }

    @Override
    public boolean sqlMapResultMapWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        //Column Number
//        XmlElement resultElement = new XmlElement("result");
//        resultElement.addAttribute(new Attribute("column","column_count"));
//        resultElement.addAttribute(new Attribute("property","columnCount"));
//        resultElement.addAttribute(new Attribute("jdbcType","INTEGER"));
//        element.addElement(resultElement);
        return super.sqlMapResultMapWithoutBLOBsElementGenerated(element, introspectedTable);
    }

    /**
     * 添加模型参数
     * @param topLevelClass DO
     * @param name 参数名
     * @param clazz 参数类
     */
    private void addTargetField(TopLevelClass topLevelClass, String name, Class clazz) {
        FullyQualifiedJavaType fieldType = new FullyQualifiedJavaType(clazz.getName());
        Field field = new Field(name,fieldType);
        field.setVisibility(JavaVisibility.PRIVATE);
        topLevelClass.addField(field);
        topLevelClass.addImportedType(field.getType());
        topLevelClass.addMethod(getJavaBeansGetter(name,fieldType));
        topLevelClass.addMethod(getJavaBeansSetter(name,fieldType));

    }

    /**
     * 添加Getter方法
     * @param name 参数名
     * @param type 参数类型
     */
    public Method getJavaBeansGetter(String name, FullyQualifiedJavaType type) {

        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(type);
        method.setName(JavaBeansUtil.getGetterMethodName(name, type));

        StringBuilder sb = new StringBuilder();
        sb.append("return " + name + ";");
        method.addBodyLine(sb.toString());

        return method;
    }

    /**
     * 添加Setter方法
     * @param name 参数名
     * @param type 参数类型
     */
    public Method getJavaBeansSetter(String name, FullyQualifiedJavaType type) {
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName(JavaBeansUtil.getSetterMethodName(name));
        method.addParameter(new Parameter(type, name));

        StringBuilder sb = new StringBuilder();
        sb.append("this." + name + " = " + name + ";");
        method.addBodyLine(sb.toString());
        return method;
    }

    /**
     * 添加Map接口中的方法
     */
    private Method generateTargetMehod(IntrospectedTable introspectedTable) {
        Method targetMethod = new Method(METHOD_NAME);
        //return type
        FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getNewListInstance();
        FullyQualifiedJavaType listType = new FullyQualifiedJavaType(RANK_DO_CLASS);
        returnType.addTypeArgument(listType);
        //设置方法
        targetMethod.setVisibility(JavaVisibility.PUBLIC);
        targetMethod.setReturnType(returnType);
        targetMethod.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(),"key"));
        targetMethod.addParameter(new Parameter(FullyQualifiedJavaType.getStringInstance(),"sort"));
        //添加到Interface
        return targetMethod;
    }

    /**
     * 生成SQL XML文件
     */
    private XmlElement generateTragetSQLMap(IntrospectedTable introspectedTable) {

        //获得表名
        String tableName = introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime();//数据库表名
        //设定方法节点属性
        String methodName = METHOD_NAME;
        String resultMap = RANK_RESULT_MAP;
        //
        XmlElement methodElement = new XmlElement("select");
        methodElement.addAttribute(new Attribute("id", methodName));
        methodElement.addAttribute(new Attribute("resultMap",resultMap));
        //CHOOSE节点生成
        XmlElement chooseElement = new XmlElement("choose");
        //TRIM节点循环
        Iterator<IntrospectedColumn> iterator = introspectedTable.getAllColumns()
                .iterator();
        while (iterator.hasNext()) {
            IntrospectedColumn introspectedColumn = iterator.next();
            //获取#{}参数 数据库表Cloumn名 DO模型成员变量的名
            String columnName = MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn);
            String DOFieldName = introspectedColumn.getJavaProperty();
            //TEST条件语句
            String testStr = "\'" + DOFieldName + "\'"  + " == param1";
            //WHEN节点拼接
            XmlElement whenElement = new XmlElement("when");
            TextElement whenTextElement = new TextElement(columnName);
            whenElement.addAttribute(new Attribute("test",testStr));
            whenElement.addElement(whenTextElement);
            //TRIM节点拼接
            chooseElement.addElement(whenElement);
        }

        //Sort 元素
        XmlElement sortElement = new XmlElement("choose");
        String[] sortStr = {"asc","desc"};
        for (int i=0; i<sortStr.length; i++) {
            //TEST条件语句
            String testStr = "\'" + sortStr[i] +"\'" + " == param2";
            //WHEN节点拼接
            XmlElement whenElement = new XmlElement("when");
            TextElement whenTextElement = new TextElement(sortStr[i]);
            whenElement.addAttribute(new Attribute("test",testStr));
            whenElement.addElement(whenTextElement);
            //Sort节点拼接
            sortElement.addElement(whenElement);
        }


        TextElement methodTextElementFirst = new TextElement("select count(*) as column_count,");
        TextElement methodTextElementSecond = new TextElement("as rank_value from " + tableName + " group by ");
        TextElement methodTextElementThrid = new TextElement("order by column_count");

        methodElement.addElement(methodTextElementFirst);
        methodElement.addElement(chooseElement);
        methodElement.addElement(methodTextElementSecond);
        methodElement.addElement(chooseElement);
        methodElement.addElement(methodTextElementThrid);
        methodElement.addElement(sortElement);

        return methodElement;

    }

    private XmlElement generateRankResultMap() {
        XmlElement resultElement = new XmlElement("resultMap");
        resultElement.addAttribute(new Attribute("id",RANK_RESULT_MAP));
        resultElement.addAttribute(new Attribute("type",RANK_DO_CLASS));

        //Column Number
        XmlElement columnElement = new XmlElement("result");
        columnElement.addAttribute(new Attribute("column","column_count"));
        columnElement.addAttribute(new Attribute("property","columnCount"));
        columnElement.addAttribute(new Attribute("jdbcType","INTEGER"));
        //Rank Value
        XmlElement rankElement = new XmlElement("result");
        rankElement.addAttribute(new Attribute("column","rank_value"));
        rankElement.addAttribute(new Attribute("property","rankValue"));
        rankElement.addAttribute(new Attribute("jdbcType","VARCHAR"));

        resultElement.addElement(rankElement);
        resultElement.addElement(columnElement);

        return resultElement;
    }
}
