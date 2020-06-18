package com.moqifei.bdd.jupiter.generate.factory;

import com.moqifei.bdd.jupiter.generate.source.model.GeneratorConfig;
import com.moqifei.bdd.jupiter.generate.source.model.Method;
import com.moqifei.bdd.jupiter.generate.source.parse.JavaSourceCodeParser;
import com.moqifei.bdd.jupiter.generate.source.parse.JavaTestCodeParser;

/**
 * PoJo
 * @author moqifei
 * 简单Java对象测试类生成工具方法
 */
public class PoJoCodeFactory extends AbstractTestCodeFactory {

    public PoJoCodeFactory(JavaSourceCodeParser JavaSourceCodeParser, JavaTestCodeParser javaTestCodeParser,
        GeneratorConfig applyGeneratorConfig) {
        this.javaSourceCodeParser = JavaSourceCodeParser;
        this.javaTestCodeParser = javaTestCodeParser;
        this.config = applyGeneratorConfig;
    }

    @Override
    protected String initInstance() {    	
    	return super.initInstance();
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