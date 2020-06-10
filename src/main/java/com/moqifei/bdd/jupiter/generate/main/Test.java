package com.moqifei.bdd.jupiter.generate.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.google.common.collect.Lists;
import com.moqifei.bdd.jupiter.generate.factory.AbstractTestCodeFactory;
import com.moqifei.bdd.jupiter.generate.source.model.Config;
import com.moqifei.bdd.jupiter.generate.source.parse.JavaSourceCodeParser;
import com.moqifei.bdd.jupiter.generate.source.parse.JavaTestCodeParser;
import com.moqifei.bdd.jupiter.generate.source.visitor.JavaSourceCodeParserVisitor;
import com.moqifei.bdd.jupiter.generate.source.visitor.JavaTestCodeParserVisitor;

public class Test {
	public static void main(String[] args) throws Exception {

		String path = "E:/aiCoding/Mockito/bdd-jupiter/src/main/java/com/moqifei/bdd/jupiter/generate/source/parse";
//
//		JavaSourceCodeParser javaSourceCodeParser = parseSourceCodeFile(path);
//		JavaTestCodeParser javaTestCodeParser = parseTargeTestFile(path);
//
//		Config config = new Config();
//
//		String testFileString = generateTestFileString(javaSourceCodeParser, javaTestCodeParser, config);
//
//		writeFile(javaSourceCodeParser, testFileString);

		List<String> fileNames = Lists.newArrayList();
		findFileList(new File(path), fileNames);
		for (String fileName : fileNames) {
			System.out.println(fileName);
			JavaSourceCodeParser javaSourceCodeParser = parseSourceCodeFile(fileName);
			JavaTestCodeParser javaTestCodeParser = parseTargeTestFile(fileName);
	
			Config config = new Config();
	
			String testFileString = generateTestFileString(javaSourceCodeParser, javaTestCodeParser, config);
	
			writeFile(javaSourceCodeParser, testFileString);
		}

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
			JavaTestCodeParser javaTestCodeParser, Config config) {
		AbstractTestCodeFactory factory = AbstractTestCodeFactory.create(javaSourceCodeParser, javaTestCodeParser,
				config);
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
		int i = testFile.lastIndexOf("/");
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
