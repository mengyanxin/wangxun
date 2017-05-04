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
 * MyBatis自定义插件——根据模型进行删除操作
 * 方法名: delete
 */

public class DeletePlugin extends PluginAdapter {

    public DeletePlugin() {
        super();
    }

    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        Method delete = generateDelete(introspectedTable);
        interfaze.addMethod(delete);
        return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
    }


    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        String tableName = introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime();//数据库表名
        XmlElement parentElement = document.getRootElement();

        String methodName = "delete";
        String resultMap = introspectedTable.getBaseResultMapId();
        String paramterType = introspectedTable.getBaseRecordType();
        //方法节点生成
        XmlElement methodRootElement = new XmlElement("delete");
        methodRootElement.addAttribute(new Attribute("id", methodName));
        methodRootElement.addAttribute(new Attribute("parameterType",paramterType));
        //TRIM节点生成
        XmlElement trimElement = new XmlElement("trim");
        trimElement.addAttribute(new Attribute("prefix","WHERE"));
        trimElement.addAttribute(new Attribute("prefixOverrides","AND | OR"));
        //Trim节点循环
        Iterator<IntrospectedColumn> iterator = introspectedTable.getAllColumns()
                .iterator();
        StringBuilder nullTestStr = new StringBuilder();
        while (iterator.hasNext()) {
            IntrospectedColumn introspectedColumn = iterator.next();

            String parameterName = MyBatis3FormattingUtilities.getParameterClause(introspectedColumn);
            String columnName = MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn);
            String javaParameterName = introspectedColumn.getJavaProperty();
            nullTestStr.append(javaParameterName + " == null");
            if (iterator.hasNext()) {
                nullTestStr.append(" and ");
            }
            String testStr = javaParameterName + " != null";
            //IF节点拼接
            TextElement ifTextElement = new TextElement("AND " + columnName + "=" + parameterName);
            XmlElement ifElement = new XmlElement("if");
            ifElement.addAttribute(new Attribute("test",testStr));
            ifElement.addElement(ifTextElement);
            //TRIM节点拼接
            trimElement.addElement(ifElement);
        }
        //最后一个IF
        TextElement ifTextElement = new TextElement("AND 1=0");
        XmlElement ifElement = new XmlElement("if");
        ifElement.addAttribute(new Attribute("test",nullTestStr.toString()));
        ifElement.addElement(ifTextElement);
        trimElement.addElement(ifElement);

        //方法节点拼接
        TextElement methodTextElement = new TextElement("delete from " + tableName);
        methodRootElement.addElement(methodTextElement);
        methodRootElement.addElement(trimElement);

        parentElement.addElement(methodRootElement);

        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }

    private Method generateDelete(IntrospectedTable introspectedTable) {
        Method targetMethod = new Method("delete");

        FullyQualifiedJavaType parameterType = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        targetMethod.setVisibility(JavaVisibility.PUBLIC);
        targetMethod.setReturnType(FullyQualifiedJavaType.getIntInstance());
        targetMethod.addParameter(new Parameter(parameterType,"record"));

        context.getCommentGenerator().addGeneralMethodComment(targetMethod,introspectedTable);
        return targetMethod;
    }
}
