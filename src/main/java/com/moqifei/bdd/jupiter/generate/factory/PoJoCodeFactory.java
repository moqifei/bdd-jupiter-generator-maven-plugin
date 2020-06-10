package com.moqifei.bdd.jupiter.generate.factory;

import com.moqifei.bdd.jupiter.generate.source.model.Config;
import com.moqifei.bdd.jupiter.generate.source.model.Method;
import com.moqifei.bdd.jupiter.generate.source.parse.JavaSourceCodeParser;
import com.moqifei.bdd.jupiter.generate.source.parse.JavaTestCodeParser;

/**
 * 描述:
 *
 * @author xiaogangfan
 * @create 2019-09-09 6:01 PM
 */
public class PoJoCodeFactory extends AbstractTestCodeFactory {

    public PoJoCodeFactory(JavaSourceCodeParser JavaSourceCodeParser, JavaTestCodeParser javaTestCodeParser,
        Config config) {
        this.javaSourceCodeParser = JavaSourceCodeParser;
        this.javaTestCodeParser = javaTestCodeParser;
        this.config = config;
    }

    @Override
    protected String initInstance() {
        StringBuffer sb = new StringBuffer();

        importSet.add("import org.junit.Before;");
        fieldSet.add(
            space4 + javaSourceCodeParser.getName() + " " + javaSourceCodeParser.getVarName()
                + " = null;");
        sb.append(enter + space4 + "@Before");
        sb.append(enter + space4 + "public void " + initInstance + "() {");
        sb.append(enter + space8 + "// Initialize the object to be tested");
        try {
            //Class clz = Class.forName(jsf.getPkg() + "." + jsf.getName());
            //ObjectTestUtilNew.initProcess(clz, sb);
            sb.append(enter + space8 + javaSourceCodeParser.getVarName() + " = ObjectInit.random(" + javaSourceCodeParser
                .getName() + ".class)" + sep);
        } catch (Exception e) {
            e.printStackTrace();
        }

        sb.append(enter + space4 + "}");
        return sb.toString();
    }

    public PoJoCodeFactory() {
        super();
    }

    @Override
    protected String writeMethodInvoke(Method method) {
        return super.writeMethodInvoke(method);
    }

    @Override
    protected String writeMethodAssert(Method method) {
        return super.writeMethodAssert(method);
    }

}