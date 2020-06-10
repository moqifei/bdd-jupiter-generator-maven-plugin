package com.moqifei.bdd.jupiter.generate.source.model;

import java.util.List;

import com.google.common.collect.Lists;
import lombok.Data;

/**
 * 右键弹出的配置项，可以指定要生成的methods、fields
 */
@Data
public class Config {
    List<String> methodList = Lists.newArrayList();
    List<String> methodNameList = Lists.newArrayList();
}
