package com.moqifei.bdd.jupiter.generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.google.common.collect.Lists;
import com.moqifei.bdd.jupiter.generate.factory.AbstractTestCodeFactory;
import com.moqifei.bdd.jupiter.generate.source.enums.GeneratorModeEnum;
import com.moqifei.bdd.jupiter.generate.source.enums.SkipFlagEnum;
import com.moqifei.bdd.jupiter.generate.source.model.GeneratorConfig;
import com.moqifei.bdd.jupiter.generate.source.parse.JavaSourceCodeParser;
import com.moqifei.bdd.jupiter.generate.source.parse.JavaTestCodeParser;
import com.moqifei.bdd.jupiter.generate.source.visitor.JavaSourceCodeParserVisitor;
import com.moqifei.bdd.jupiter.generate.source.visitor.JavaTestCodeParserVisitor;

@Mojo(name = "generate")
public class GeneratorMojo extends AbstractMojo {

	@Parameter(property = "generate.configurationFile", required = true)
	private String configurationFile;

	@Parameter(property = "golablGeneratorMode", required = false)
	private String golablGeneratorMode;

	@Parameter(property = "generatorConfigs", required = false)
	private GeneratorConfig[] generatorConfigs;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("bdd-jupiter-generator start generate file path: " + configurationFile);
		List<String> fileNames = Lists.newArrayList();
		findFileList(new File(configurationFile), fileNames);
		for (String fileName : fileNames) {
			System.out.println(fileName);
			JavaSourceCodeParser javaSourceCodeParser = parseSourceCodeFile(fileName);
			JavaTestCodeParser javaTestCodeParser = parseTargeTestFile(fileName);

			GeneratorConfig applyGeneratorConfig = parseGeneratorConfig(generatorConfigs, fileName);

			getLog().info("bdd-jupiter-generator applyGeneratorConfig: " + applyGeneratorConfig);

			String testFileString = generateTestFileString(javaSourceCodeParser, javaTestCodeParser,
					applyGeneratorConfig);
			if (testFileString != null)
				writeFile(javaSourceCodeParser, testFileString);
		}
		getLog().info("bdd-jupiter-generator finished generate file path: " + configurationFile);
	}

	private GeneratorConfig parseGeneratorConfig(GeneratorConfig[] generatorConfigs, String fileName) {
		String shortName = fileName.substring(fileName.lastIndexOf(File.separator) + 1);
		GeneratorConfig defaultConfig = new GeneratorConfig();
		defaultConfig.setFullFileName(shortName);
		if (golablGeneratorMode != null) {
			defaultConfig.setGeneratorMode(golablGeneratorMode);
		}else {
			defaultConfig.setGeneratorMode(GeneratorModeEnum.SPRING.code());
		}
		defaultConfig.setSkipFlag(SkipFlagEnum.NONE.code());
		defaultConfig.setMethodLists(null);
		for (GeneratorConfig generatorConfig : generatorConfigs) {
			if (generatorConfig.getFullFileName().equals(shortName)) {
				if(generatorConfig.getGeneratorMode()!=null) {
					defaultConfig.setGeneratorMode(generatorConfig.getGeneratorMode());
				}
				defaultConfig.setSkipFlag(generatorConfig.getSkipFlag());
				defaultConfig.setMethodLists(generatorConfig.getMethodLists());
			}
		}
		return defaultConfig;
	}

	private static void findFileList(File file, List<String> fileNames) {
		if (!file.exists()) {
			return;
		}

		if (file.isFile()) {
			fileNames.add(file.getPath());
			return;
		}
		String[] files = file.list();
		for (int i = 0; i < files.length; i++) {
			File tempFile = new File(file, files[i]);
			if (tempFile.isFile()) {
				if (tempFile.getName().endsWith(".java")) {
					fileNames.add(tempFile.getPath());
				}
			} else {
				findFileList(tempFile, fileNames);
			}
		}
	}

	private static void writeFile(JavaSourceCodeParser javaSourceCodeParser, String testFileString) {
		String testFile = javaSourceCodeParser.getPathName().replace("main", "test");
		System.out.println(testFile);
		int i = testFile.lastIndexOf(File.separator);
		String dir = testFile.substring(0, i);

		File parentDir = new File(dir);
		if (!parentDir.exists()) {
			parentDir.mkdirs();
		}

		int i1 = testFile.lastIndexOf(".");
		String testFileName = testFile.substring(0, i1) + "Test.java";
		File file = new File(testFileName);

		PrintWriter pw = null;
		BufferedWriter bw = null;
		try {
			if (!file.exists()) { // 鍒ゆ柇鏄惁瀛樺湪java鏂囦欢
				file.createNewFile(); // 涓嶅瓨鍦ㄥ垯鍒涘缓
			}
			pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
			bw = new BufferedWriter(pw);
			bw.write(testFileString);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null) {
					bw.close();
				}
				if (pw != null) {
					pw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private static String generateTestFileString(JavaSourceCodeParser javaSourceCodeParser,
			JavaTestCodeParser javaTestCodeParser, GeneratorConfig applyGeneratorConfig) {
		AbstractTestCodeFactory factory = AbstractTestCodeFactory.create(javaSourceCodeParser, javaTestCodeParser,
				applyGeneratorConfig);
		return factory.createFileString();
	}

	private static JavaSourceCodeParser parseSourceCodeFile(String fileName) {
		JavaSourceCodeParser jsf = new JavaSourceCodeParser();
		try {
			FileInputStream in = new FileInputStream(fileName);
			CompilationUnit cu = JavaParser.parse(in);
			jsf.setPathName(fileName);

			cu.accept(new JavaSourceCodeParserVisitor(), jsf);
			// jsf.setClassName()
			return jsf;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("parse erroe", e);
		}
	}

	private static JavaTestCodeParser parseTargeTestFile(String fileName) {
		String testFile = fileName.replace("/main/", "/test/");
		//int i = testFile.lastIndexOf("/");
		int i1 = testFile.lastIndexOf(".");
		String testFileName = testFile.substring(0, i1) + "Test.java";
		File file = new File(testFileName);
		if (!new File(testFileName).exists()) {
			return null;
		}
		JavaTestCodeParser jsf = new JavaTestCodeParser();
		try {
			FileInputStream in = new FileInputStream(file);
			CompilationUnit cu = JavaParser.parse(in);
			jsf.setPathName(testFile);

			cu.accept(new JavaTestCodeParserVisitor(), jsf);
			// jsf.setClassName()
			return jsf;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("parse erroe", e);
		}
	}

}
