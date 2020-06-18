package com.moqifei.bdd.jupiter.generate.source.parse;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.moqifei.bdd.jupiter.generate.source.model.Method;
import com.moqifei.bdd.jupiter.generate.util.StringUtil;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
public class JavaTestCodeParser extends JavaCodeParser {

    public String getMethodString(MethodDeclaration methodDeclaration) {
        return "";
    }

    /**
     * 生成方法签名
     *
     * @param method
     * @return
     */
    @Override
    public String generateMethodSign(Method method) {
        //return StringUtil.firstLower(method.getName().substring(4)) +
        //    (CollectionUtils.isEmpty(method.getParamList()) ? "()" : "(" + method.getParamList().stream().map(
        //        t -> {
        //            return (t.getType().asString() + " " + t.getName().asString());
        //        }).collect(Collectors.toList()).stream().collect(
        //        Collectors.joining(",")) + "）")
        //    + ":" + method.getReturnType();
        if (method.getName().startsWith("test")) {
            return StringUtil.firstLower(method.getName().substring(4));
        } else {
            return method.getName();
        }

    }
}