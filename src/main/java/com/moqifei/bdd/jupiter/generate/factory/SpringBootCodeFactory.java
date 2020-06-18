package com.moqifei.bdd.jupiter.generate.factory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.stmt.Statement;
import com.google.common.collect.Lists;
import com.moqifei.bdd.jupiter.generate.source.model.GeneratorConfig;
import com.moqifei.bdd.jupiter.generate.source.model.Method;
import com.moqifei.bdd.jupiter.generate.source.model.MockStatement;
import com.moqifei.bdd.jupiter.generate.source.parse.JavaSourceCodeParser;
import com.moqifei.bdd.jupiter.generate.source.parse.JavaTestCodeParser;
import com.moqifei.bdd.jupiter.generate.util.StringUtil;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class SpringBootCodeFactory extends AbstractTestCodeFactory{
	public SpringBootCodeFactory(JavaSourceCodeParser JavaSourceCodeParser, JavaTestCodeParser javaTestCodeParser,
		        GeneratorConfig applyGeneratorConfig) {
		        this.javaSourceCodeParser = JavaSourceCodeParser;
		        this.javaTestCodeParser = javaTestCodeParser;
		        this.config = applyGeneratorConfig;
	}
	
	@Override
	protected String initInstance() {
		StringBuffer sb = new StringBuffer();
		importSet.add(impo + " org.springframework.test.context.junit.jupiter.SpringExtension;");
		importSet.add(impo + " org.mockito.MockitoAnnotations;");
		importSet.add(impo + " org.junit.jupiter.api.extension.ExtendWith;");
		importSet.add(impo + " org.junit.jupiter.api.BeforeEach;");

		annotationsSet.add("@ExtendWith(SpringExtension.class)");
		annotationsSet.add("@SpringBootTest(classes = Object.class)");

		sb.append(enter + space4 + "@BeforeEach");
		sb.append(enter + space4 + "public void beforeEach() {");
		sb.append(enter + space8 + "MockitoAnnotations.initMocks(this);");
		sb.append(enter + space4 + "}");
		
		return sb.toString();
	}
	
	@Override
	protected String writeGivenPhase(Method method) {

		importSet.add(impo + " org.mockito.ArgumentMatchers;");

		StringBuilder methodBody = new StringBuilder();
		methodBody.append(enter + space8 + "scene.given(\"given phase desc\",()->{//replace your given code here."
				+ enter + space12 + "scene.put(\"xxx\", ArgumentMatchers.any());" + enter + space8 + "})");
		methodBody.append(enter + space8 + ".and(\"given and phase desc\",()->{//put your given and code here." + enter
				+ space8 + "})");
		return methodBody.toString();
	}

	@Override
	protected String writeWhenPhase(Method method) {
		StringBuilder methodBody = new StringBuilder();
		methodBody.append(enter + space8 + ".when(\"when phase desc\",()->{//replace your when code here.");

		List<String> matcherList = serviceInvokeMatcher();
		System.out.println("serviceInvokeMatcher->matcherList" + matcherList);
		Iterator<Statement> iterator = method.getMethodDeclaration().getBody().get().getStatements().iterator();
		while (iterator.hasNext()) {
			Node entry = iterator.next();
			
			for (Node childNode : entry.getChildNodes()) {
				// if (childNode.getTokenRange().toString().trim().endsWith(";")) {
				// 使用正则将关键的调用抓取出来
				MockStatement matchStatment = getMatchStatment(childNode.getTokenRange().toString(), matcherList);

				System.out.println("getMatchStatment->matchStatment" + matchStatment);
				if (matchStatment == null || StringUtils.isBlank(matchStatment.getInvokeStatment())) {
					continue;
				}
				methodBody.append(enter + space12 + " Mockito.when("
						+ StringUtil.replaceBlank(serviceStatmentMatchers(matchStatment.getInvokeStatment()))
						+ ").thenReturn(ArgumentMatchers.any())" + sep);

			}

		}

		methodBody.append(enter + space8 + "})");

		return methodBody.toString();
	}

	@Override
	protected String writeThenPhase(Method method) {
		
		importSet.add(impo + " static org.hamcrest.CoreMatchers.equalTo;");
		importSet.add(impo + " static org.hamcrest.CoreMatchers.is;");
		importSet.add(impo + " static org.hamcrest.MatcherAssert.assertThat;");
		
		StringBuilder methodBody = new StringBuilder();
		methodBody.append(enter + space8 + ".then(\"then phase desc\",()->{//replace your then code here." + enter
				+ space12 + "Object object = scene.<Object>get(\"xxx\");");
		
		List<String> matcherList = serviceInvokeMatcher();
		Iterator<Statement> iterator = method.getMethodDeclaration().getBody().get().getStatements().iterator();
		while (iterator.hasNext()) {
			Node entry = iterator.next();
			
			for (Node childNode : entry.getChildNodes()) {
				MockStatement matchStatment = getMatchStatment(childNode.getTokenRange().toString(), matcherList);
				if (matchStatment == null || StringUtils.isBlank(matchStatment.getInvokeStatment())) {
					continue;
				}
				methodBody.append(enter + space12 + matchStatment.getResultType() 
						+" "+ matchStatment.getResultVar() +" = "+ StringUtil.replaceBlank(serviceStatmentMatchers(matchStatment.getInvokeStatment()))
						+  sep);
				methodBody.append(enter + space12 + "assertThat(" + matchStatment.getResultVar() +", is(equalTo(object)))"+  sep);
			}
		}
		
		methodBody.append(enter + space8 + "})");
		methodBody.append(enter + space8 + ".and(\"then and phase desc\",()->{//put your then and code here." + enter
				+ space8 + "})");
		return methodBody.toString();
	}

	private List<String> serviceInvokeMatcher() {
		List<String> result = new ArrayList<>();
		if (CollectionUtils.isEmpty(javaSourceCodeParser.getFieldList())) {
			return Lists.newArrayList();
		}
		for (VariableDeclarator var : javaSourceCodeParser.getFieldList()) {
			if (var.getParentNode().get().getTokenRange().toString().contains("@")) {
				result.add("(.*?)(=)(.*?)(" + var.getName().toString() + "\\.)(.*?)(\\((\\n)?)(.*?)(])");
			}
		}
		return result;
	}

	public MockStatement getMatchStatment(String statement, List<String> matcherList) {
		for (String reg : matcherList) {
			if (StringUtils.isBlank(reg.trim())) {
				continue;
			}
			Pattern pattern = Pattern.compile(reg);
			Matcher matcher = pattern.matcher(statement);
			while (matcher.find()) {
				return wrapMockStatement(matcher.group());
			}
		}
		return null;

	}

	private MockStatement wrapMockStatement(String group) {
		MockStatement mockStatement = new MockStatement();
		System.out.println("wrapMockStatement->group" + group);
		String statment = group.substring(9, group.length() - 1);
		System.out.println("wrapMockStatement->statment" + statment);
		String[] statmentArray = statment.trim().split("=");

		mockStatement.setInvokeStatment(statmentArray[1]);
		String[] declari = statmentArray[0].trim().split(" ");
		List<String> collect = Arrays.stream(declari).filter(row -> StringUtils.isNotBlank(row.trim()))
				.collect(Collectors.toList());
		if (collect.size() > 1) {
			for (int i = 0; i < collect.size(); i++) {
				if (collect.size() - 1 == i) {
					mockStatement.setResultVar(collect.get(1));
				} else {
					mockStatement.setResultType(mockStatement.getResultType() + collect.get(i));
				}
			}

		} else {
			mockStatement.setResultVar(collect.get(0));
		}
		return mockStatement;
	}

	private String serviceStatmentMatchers(String invokeStatment) {

		Stack<ParenthesesIndex> stack = new Stack<ParenthesesIndex>();
		Map<Integer, Integer> matcherIndexMap = new HashMap<Integer, Integer>();

		List<String> parameterList = new ArrayList<String>();
		for (int index = 0; index < invokeStatment.length(); index++) {
			switch (invokeStatment.charAt(index)) {
			case '(':
				ParenthesesIndex pi = new ParenthesesIndex();
				pi.setS(invokeStatment.charAt(index));
				pi.setIndex(index + 1);
				stack.push(pi);
				break;
			case ')':
				if (!stack.empty() && stack.peek().getS() == '(') {
					ParenthesesIndex lastPi = stack.pop();
					matcherIndexMap.put(lastPi.getIndex(), index);
				}
				break;
			}
		}
		matcherIndexMap.forEach((key, value) -> {
			String s = invokeStatment.substring(key, value);
			String[] parameters = s.split(",");
			for (String parameter : parameters) {
				if (parameter.contains("(") || parameter.contains(")")) {
					continue;
				}
				int index = invokeStatment.indexOf(parameter, key);
				parameterList.add(invokeStatment.substring(index, parameter.length() + index));
			}
		});
		String result = invokeStatment;
		for (String parameter : parameterList) {
			result = result.replace(parameter, "ArgumentMatchers.any()");
		}
		System.out.println(result);
		return result;
	}

	@Data
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ParenthesesIndex {
		private Character s;
		private int index;
	}

	public static void main(String[] args) {
		String invokeStatment = "test.testAdd(1, test.testReduce(test.testAdd(1,2), 1), 4)";

		Stack<ParenthesesIndex> stack = new Stack<ParenthesesIndex>();
		Map<Integer, Integer> matcherIndexMap = new HashMap<Integer, Integer>();

		List<String> parameterList = new ArrayList<String>();
		for (int index = 0; index < invokeStatment.length(); index++) {
			switch (invokeStatment.charAt(index)) {
			case '(':
				ParenthesesIndex pi = new ParenthesesIndex();
				pi.setS(invokeStatment.charAt(index));
				pi.setIndex(index + 1);
				stack.push(pi);
				break;
			case ')':
				if (!stack.empty() && stack.peek().getS() == '(') {
					ParenthesesIndex lastPi = stack.pop();
					matcherIndexMap.put(lastPi.getIndex(), index);
				}
				break;
			}
		}
		matcherIndexMap.forEach((key, value) -> {
			String s = invokeStatment.substring(key, value);
			String[] parameters = s.split(",");
			for (String parameter : parameters) {
				if (parameter.contains("(") || parameter.contains(")")) {
					continue;
				}
				int index = invokeStatment.indexOf(parameter, key);
				parameterList.add(invokeStatment.substring(index, parameter.length() + index));
			}
		});
		String result = invokeStatment;
		for (String parameter : parameterList) {
			result = result.replace(parameter, "ArgumentMatchers.any()");
		}
		System.out.println(result);
	}
}
