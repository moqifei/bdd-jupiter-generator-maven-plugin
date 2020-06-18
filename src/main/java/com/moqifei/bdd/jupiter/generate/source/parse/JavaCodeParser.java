package com.moqifei.bdd.jupiter.generate.source.parse;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.moqifei.bdd.jupiter.generate.source.model.Method;
import com.moqifei.bdd.jupiter.generate.util.StringUtil;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


/**
 * 描述:
 *
 * @author xiaogangfan
 * @create 2019-09-03 5:14 PM
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class JavaCodeParser {

    /**
     * customize parse
     */
    private String pathName;
    private String absDir;
    private String pkg;
    private String name;
    private String className;
    private String extendsClass;
    private String implementInterface;
    private Set<String> importList = new HashSet<>();

    private List<Method> methodList;

    /**
     * from gighub javaparser
     */
    private PackageDeclaration packageDeclaration;
    private CompilationUnit compilationUnit;
    private ClassOrInterfaceDeclaration classOrInterfaceDeclaration;
    private List<VariableDeclarator> fieldList;
    private List<MethodDeclaration> methodDeclarationList;
    private Map<String, MethodDeclaration> methodDeclarationMap;

    public String getVarName() {
        return StringUtil.firstLower(getName());
    }

    /**
     * 生成方法签名
     *
     * @param method
     * @return
     */
    public String generateMethodSign(Method method) {
        //return method.getName() +
        //    (CollectionUtils.isEmpty(method.getParamList()) ? "()" : "(" + method.getParamList().stream().map(
        //        t -> {
        //            return (t.getType().asString() + " " + t.getName().asString());
        //        }).collect(Collectors.toList()).stream().collect(
        //        Collectors.joining(",")) + "）")
        //    + ":" + method.getReturnType();
        return method.getName();
    }

}