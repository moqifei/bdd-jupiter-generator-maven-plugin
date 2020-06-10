package com.moqifei.bdd.jupiter.generate.factory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.google.common.collect.Lists;
import com.moqifei.bdd.jupiter.generate.source.model.Config;
import com.moqifei.bdd.jupiter.generate.source.model.Method;
import com.moqifei.bdd.jupiter.generate.source.model.ModelEnum;
import com.moqifei.bdd.jupiter.generate.source.parse.JavaSourceCodeParser;
import com.moqifei.bdd.jupiter.generate.source.parse.JavaTestCodeParser;
import com.moqifei.bdd.jupiter.generate.util.StringUtil;
import com.moqifei.bdd.jupiter.generate.util.TestCodeUtil;


public abstract class AbstractTestCodeFactory {
	public static final String initInstance = "initInstance";
	public static final String sep = ";";
	public static final String enter = "\n";
	public static final String enter2 = "\n \n";
	public static final String sepAndenter = ";\n";
	public static final String impo = "import";
	public static final String tab = "  ";
	public static final String space4 = "    ";
	public static final String space8 = "        ";
	public static final String space12 = "            ";

	protected JavaSourceCodeParser javaSourceCodeParser = null;
	protected JavaTestCodeParser javaTestCodeParser = null;
	protected Config config = null;

	protected String pkg;
	protected Set<String> importSet = new HashSet<>();
	protected String commentStr;
	protected Set<String> annotationsSet = new HashSet<>();
	protected String classHeader;
	protected List<String> fieldSet = Lists.newArrayList();
	protected List<String> methodSet = Lists.newArrayList();

	public AbstractTestCodeFactory() {
	}

	public static AbstractTestCodeFactory create(JavaSourceCodeParser jsf, JavaTestCodeParser javaTestCodeParser,
			Config config) {
		if (needMockCase(jsf)) {
			return new JmockitCodeFactory(jsf, javaTestCodeParser, config);
		}
		return new PoJoCodeFactory(jsf, javaTestCodeParser, config);
	}

	/**
	 * @param jsf
	 * @return
	 */
	private static boolean needMockCase(JavaSourceCodeParser jsf) {
		if (jsf.getName().endsWith("Service") || jsf.getName().endsWith("ServiceImpl")
				|| jsf.getName().endsWith("Application")) {
			return true;
		}
		return false;
	}

	public AbstractTestCodeFactory(JavaSourceCodeParser javaSourceCodeParser) {
		this.javaSourceCodeParser = javaSourceCodeParser;
	}

	public String createFileString() {
		setPkg();
		setImport();
		setcommentStr();
		setClassAnnotations();
		setClassHeader();
		setFields();
		setMethods();

		return generateTestFileString();
	}

	private void setClassAnnotations() {
		this.annotationsSet.add("@Story(name = \"name\", description = \"描述\")");
		
	}

	protected void setcommentStr() {
		commentStr = "/** "
				+  enter  +"* This bdd-jupiter style test cases file was auto-generated, "
				+  enter  +"* you should completed it by your own intelligence, come on & have fan!"
				+  enter  + "*/";
	}

	protected void setFields() {
	}

	protected void setImport() {
		this.importSet.add(impo + " com.moqifei.bdd.jupiter.extension.Scene;");
		this.importSet.add(impo + " com.moqifei.bdd.jupiter.modle.annotations.ScenarioJsonSource;");
		this.importSet.add(impo + " com.moqifei.bdd.jupiter.modle.annotations.ScenarioSource;");
		this.importSet.add(impo + " com.moqifei.bdd.jupiter.modle.annotations.ScenarioTest;");
		this.importSet.add(impo + " com.moqifei.bdd.jupiter.modle.annotations.Story;");
		this.importSet.add(enter);
		this.importSet.addAll(javaSourceCodeParser.getImportList());

	}

	protected void setPkg() {
		pkg = Optional.ofNullable(javaSourceCodeParser.getPkg())
				.map(t -> "package " + t.replace(".main.", ".test.") + sep).orElse("");
	}

	protected void setClassHeader() {
		classHeader = "public class " + javaSourceCodeParser.getName() + "Test ";
	}

	protected void setMethods() {
		// Handle testfile
		handleTestFile();
		// Handle change method
		if(javaSourceCodeParser.getMethodList()!=null) {
			for (Method method : javaSourceCodeParser.getMethodList()) {
//				if (needFilter(method)) {
//					continue;
//				}
				handleNewMethod(method);
			}
		}
	}

	protected boolean needFilter(Method method) {
		String methodSign = javaSourceCodeParser.generateMethodSign(method);
		if (!config.getMethodNameList().contains(methodSign)) {
			return true;
		}
		if (javaSourceCodeParser.getMethodDeclarationMap() != null) {
			MethodDeclaration methodDeclaration = javaSourceCodeParser.getMethodDeclarationMap().get(methodSign);
			if (methodDeclaration != null) {
				return true;
			}
		}
		return false;
	}

	protected void handleNewMethod(Method method) {
		StringBuilder sb = new StringBuilder();
		sb.append(writeMethodHeader(method));
		sb.append(writeMethodMock(method));
		sb.append(writeMethodInvoke(method));
		//sb.append(writeMethodAssert(method));
		sb.append(writeMethodFooter());
		methodSet.add(sb.toString());
	}

	protected void handleTestFile() {
		if (javaTestCodeParser != null && javaTestCodeParser.getMethodDeclarationMap() != null) {
			MethodDeclaration methodDeclaration = javaTestCodeParser.getMethodDeclarationMap().get(initInstance);
			if (methodDeclaration == null) {
				methodSet.add(initInstance());
			}
			if (CollectionUtils.isNotEmpty(javaTestCodeParser.getMethodList())) {
				for (Method method : javaTestCodeParser.getMethodList()) {
					String methodSign = javaTestCodeParser.generateMethodSign(method);
					if (config.getMethodNameList().contains(methodSign)) {
						continue;
					}
					methodSet.add(enter + space4 + generateTestSourceCodeMethodString(method.getMethodDeclaration()));
				}
			}
		} else {
			methodSet.add(initInstance());
		}
	}

	protected String generateTestSourceCodeMethodString(MethodDeclaration methodDeclaration) {
		// todo
		return methodDeclaration.getTokenRange().get().toString();

	}

	public String generateTestFileString() {
		StringBuilder sb = new StringBuilder();
		sb.append(pkg);
		sb.append(enter);
		if (CollectionUtils.isNotEmpty(importSet)) {
			for (String s : importSet) {
				sb.append(enter + s.trim());
			}
		}
		sb.append(enter2);
		sb.append(commentStr);
		if (CollectionUtils.isNotEmpty(annotationsSet)) {
			for (String s : annotationsSet) {
				sb.append(enter + s.trim());
			}
		}
		sb.append(enter);
		sb.append(classHeader + "{");

		if (CollectionUtils.isNotEmpty(fieldSet)) {
			for (String s : fieldSet) {
				sb.append(enter + s);
			}
		}

		if (CollectionUtils.isNotEmpty(methodSet)) {
			for (String s : methodSet) {
				sb.append(enter + s);
			}
		}
		sb.append(enter + "}");
		return sb.toString();
	}

	public List<String> generateImport() {
		return new ArrayList<>();
	}

	protected String writeMethodMock(Method method) {
		return "";
	}

	protected String writeMethodFooter() {
		return enter + space4 + "}";
	}

	protected String writeMethodHeader(Method method) {
		StringBuffer methodHeader = new StringBuffer();
		methodHeader.append(enter2 + space4 + "@ScenarioTest(value = \"name\")");
		methodHeader.append(enter + space4 +  "@ScenarioJsonSource(resources = \"/xxx/xxx.json\", instance = Scene.class, key = \"xxx\")");
		methodHeader.append(enter + space4 +  "public void test" + StringUtil.firstUpper(method.getName()) + "(Scene scene) { ");
		return methodHeader.toString();
	}

	protected String initInstance() {
		return "";
	}

	protected String verify(ModelEnum ddd_model, Method method) {
		return "";
	}

	protected String writeMethodInvoke(Method method) {

		StringBuilder methodBody = new StringBuilder();
		methodBody.append(enter+space8+"scene.given(\"given phase desc\",()->{//put your given code here."+enter+space8+"})");
		methodBody.append(enter+space8+".and(\"given and phase desc\",()->{//put your given and code here."+enter+space8+"})");
		methodBody.append(enter+space8+".when(\"when phase desc\",()->{//put your when code here."+enter+space8+"})");
		methodBody.append(enter+space8+".then(\"then phase desc\",()->{//put your then code here."+enter+space8+"})");
		methodBody.append(enter+space8+".and(\"then and phase desc\",()->{//put your then and code here."+enter+space8+"})");
		methodBody.append(enter+space8+".run();");

		
		return methodBody.toString();
	}

	protected String writeMethodAssert(Method method) {

		StringBuilder assertStr = new StringBuilder();
		assertStr.append(enter + space8 + "// Write the Assert code");
		if (method.getType().isVoidType()) {
			importSet.add(TestCodeUtil.IMPO_ASSERT + sep);
			if (method.getParamList().size() == 0) {
				assertStr.append(enter + space8 + TestCodeUtil.ASSERTTRUE + sep);
			}
			for (Parameter parameter : method.getParamList()) {
				if (parameter.getType().isReferenceType()) {
					assertStr.append(enter + space8 + TestCodeUtil.EQUALS + sep);
				}
			}

		} else {
			assertStr.append(enter + space8 + TestCodeUtil.EQUALS + sep);
			assertStr.append(enter + space8 + TestCodeUtil.RESULTASSERT + sep);
		}
		return assertStr.toString();
	}
}