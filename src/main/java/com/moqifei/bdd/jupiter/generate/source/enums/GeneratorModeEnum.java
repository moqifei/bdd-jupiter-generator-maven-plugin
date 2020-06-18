package com.moqifei.bdd.jupiter.generate.source.enums;

public enum GeneratorModeEnum {
	SPRING("Spring"),
	SPRINGBOOT("SpringBoot"),
	POJO("PoJo");	
	
	private final String code;
	
	private GeneratorModeEnum(String code) {
		this.code = code;
	}
	
	public String code() {
		return code;
	}
	
	public static GeneratorModeEnum fromCode(String code) {
		for(GeneratorModeEnum generatorModeEnum : GeneratorModeEnum.values()) {
			if(generatorModeEnum.code.equals(code)) {
				return generatorModeEnum;
			}
		}
		return null;
	}
}
