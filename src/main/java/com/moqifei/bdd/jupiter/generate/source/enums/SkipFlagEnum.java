package com.moqifei.bdd.jupiter.generate.source.enums;

public enum SkipFlagEnum {
	ALL("all"),
	NONE("none"),
	PARTIAL("partial");

	private final String code;

	private SkipFlagEnum(String code) {
		this.code = code;
	}
	
	public String code() {
		return code;
	}
	
	public static SkipFlagEnum fromCode(String code) {
		for(SkipFlagEnum skipFlagEnum : SkipFlagEnum.values()) {
			if(skipFlagEnum.code.equals(code)) {
				return skipFlagEnum;
			}
		}
		return null;
	}

}
