package com.moqifei.bdd.jupiter.generate.builder;

import com.moqifei.bdd.jupiter.generate.source.model.Method;
import com.moqifei.bdd.jupiter.generate.source.parse.JavaSourceCodeParser;

/**
 * 描述:
 *
 * @author xiaogangfan
 * @create 2019-09-03 8:41 PM
 */
public abstract class AbstractBuilder {

    private JavaSourceCodeParser javaSourceCodeParser;

    String getContent() {
        StringBuffer stringBuffer = new StringBuffer();

        return stringBuffer.toString();
    }

    protected StringBuilder generatePackage() {
        return new StringBuilder(javaSourceCodeParser.getPkg());
    }

    protected StringBuilder generateImports() {
        return null;
    }

    protected StringBuilder generateMethod() {
        StringBuilder sb = new StringBuilder();
        for (Method method : javaSourceCodeParser.getMethodList()) {
            sb.append("public void test" + method.getName() + "() { ");
            sb.append("}");
        }
        return null;
    }

}