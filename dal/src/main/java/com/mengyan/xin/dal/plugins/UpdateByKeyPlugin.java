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
 * MyBatis自定义插件——根据Key值Update数据库,Key值为Data Object字段名,Update中Key值字段为参考更新其它字段
 * 方法名: updateByKey
 */
public class UpdateByKeyPlugin extends PluginAdapter {

    private final static String METHOD_NAME = "updateByKey";

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        interfaze.addMethod(generateUpdateByKeyMethod(introspectedTable));
        return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        XmlElement parentElement = document.getRootElement();
        parentElement.addElement(generateUpdateByKeyMapper(introspectedTable));
        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }

    private Method generateUpdateByKeyMethod(IntrospectedTable introspectedTable) {
        //方法名
        Method targetMethod = new Method(METHOD_NAME);
        //生成返回值 参数
        FullyQualifiedJavaType returnType = FullyQualifiedJavaType.getIntInstance();
        FullyQualifiedJavaType parameterTypeFirst = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
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

    private XmlElement generateUpdateByKeyMapper(IntrospectedTable introspectedTable) {
        //获得表名
        String tableName = introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime();//数据库表名
        //设定方法节点属性
        String methodName = METHOD_NAME;
        //方法节点生成
        XmlElement methodElement = new XmlElement("update");
        methodElement.addAttribute(new Attribute("id", methodName));
        //TRIM节点生成
        XmlElement trimElement = new XmlElement("trim");
        trimElement.addAttribute(new Attribute("prefix","set"));
        trimElement.addAttribute(new Attribute("suffixOverrides",","));
        //CHOOSE节点生成
        XmlElement chooseElement = new XmlElement("choose");
        //TRIM节点循环
        Iterator<IntrospectedColumn> iterator = introspectedTable.getAllColumns().iterator();
        //最后全空检查字符串
        while (iterator.hasNext()) {
            IntrospectedColumn introspectedColumn = iterator.next();
            //获取#{}参数 数据库表Cloumn名 DO模型成员变量的名
            String parameterName = MyBatis3FormattingUtilities.getParameterClause(introspectedColumn,"param1.");
            String columnName = MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn);
            //MyBatis3FormattingUtilities是myBatis格式化后的#{param1.id, jdbcType=INTEGER}这种
            String DOFieldName = introspectedColumn.getJavaProperty();

            XmlElement ifElement = new XmlElement("if");
            //TEST条件语句
            String ifTestStr = "param1." + DOFieldName + " != null and param2.key != \'" + DOFieldName + "\'";
            //IF节点拼接
            TextElement ifTextElement = new TextElement(columnName + "=" + parameterName + ",");
            ifElement.addAttribute(new Attribute("test",ifTestStr));
            ifElement.addElement(ifTextElement);
            //TRIM节点拼接
            trimElement.addElement(ifElement);
            //WHENTEST条件语句
            String whenTestStr = "\'" + DOFieldName + "\'" + " == param2.key";
            //WHEN节点拼接
            XmlElement whenElement = new XmlElement("when");
            TextElement whenTextElement = new TextElement(columnName + "=" + parameterName);
            whenElement.addAttribute(new Attribute("test",whenTestStr));
            whenElement.addElement(whenTextElement);
            //CHOOSE节点拼接
            chooseElement.addElement(whenElement);
        }
        //CHOOSE最后一个参数全空的IF
        TextElement otherwiseTextElement = new TextElement("1=0");
        XmlElement otherwiseElement = new XmlElement("otherwise");
        otherwiseElement.addElement(otherwiseTextElement);
        chooseElement.addElement(otherwiseElement);

        //方法节点拼接
        TextElement methodTextElementFirst = new TextElement("update " + tableName);
        TextElement methodTextElementSecond = new TextElement("where");

        methodElement.addElement(methodTextElementFirst);
        methodElement.addElement(trimElement);
        methodElement.addElement(methodTextElementSecond);
        methodElement.addElement(chooseElement);

        return methodElement;

    }
}
