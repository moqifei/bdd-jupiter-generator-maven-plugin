package com.moqifei.bdd.jupiter.generate.source.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class GeneratorConfig {
	
	private String generatorMode; //模式 Spring SpringBoot PoJo
	private String fullFileName;  //类名
	private String skipFlag;      //all --全部跳过   none--全部生成  partial --跳过methodLists指定方法
	private String[] methodLists; //拟跳过方法列表
	
	
}
