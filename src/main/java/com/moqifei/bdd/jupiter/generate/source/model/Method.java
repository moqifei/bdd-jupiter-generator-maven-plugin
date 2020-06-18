package com.moqifei.bdd.jupiter.generate.source.model;

import java.util.Optional;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.type.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Method {
    private String name;
    /**
     * 返回值类型
     */
    private String returnType;
    private Type type;
    private String body;
    private Optional<BlockStmt> originBody;

    private NodeList<Parameter> paramList;
    private MethodDeclaration methodDeclaration;

}
