package com.moqifei.bdd.jupiter.generate.source.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MockStatement {
    private String resultType = "";
    private String resultVar;
    private String invokeStatment;
}
