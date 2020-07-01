# bdd-jupiter-generator-maven-plugin
## bdd-jupiter-generator-maven-plugin的目的
   通过maven插件的形式，解析Java文件，自动生成[bdd-jupiter](https://github.com/moqifei/bdd-jupiter)单测框架模式下的单元测试用例
## 使用方式
   * 添加pom,插件依赖
   ```
   <build>
		<plugins>
			<plugin>
				<artifactId>maven-compiler-plugin</artifactId>
				<version>3.8.1</version>
			</plugin>
			<plugin>
				<artifactId>maven-surefire-plugin</artifactId>
				<version>2.22.2</version>
			</plugin>
			<plugin>
				<groupId>com.moqifei.bdd.jupiter</groupId>
				<artifactId>bdd-jupiter-generator-maven-plugin</artifactId>
				<version>1.0.0-SNAPSHOT</version>
				<configuration>
					<!-- 指定配置文件的位置 -->
					<configurationFile>${basedir}/src/main/java/com/moqifei/bdd/jupiter/writer/test</configurationFile>
					<golablGeneratorMode>PoJo</golablGeneratorMode>
					<generatorConfigs>
					  <generatorConfig
							implementation="com.moqifei.bdd.jupiter.generate.source.model.GeneratorConfig">
							<generatorMode>SpringBoot</generatorMode>
							<fullFileName>SkipClass.java</fullFileName>
							<skipFlag>all</skipFlag>
						</generatorConfig>
						<generatorConfig
							implementation="com.moqifei.bdd.jupiter.generate.source.model.GeneratorConfig">
							<generatorMode>SpringBoot</generatorMode>
							<fullFileName>SpringBootClass.java</fullFileName>
							<skipFlag>none</skipFlag>
						</generatorConfig>
						<generatorConfig
							implementation="com.moqifei.bdd.jupiter.generate.source.model.GeneratorConfig">
							<generatorMode>Spring</generatorMode>
							<fullFileName>SpringClass.java</fullFileName>
							<skipFlag>none</skipFlag>
						</generatorConfig>
						<generatorConfig
							implementation="com.moqifei.bdd.jupiter.generate.source.model.GeneratorConfig">
							<generatorMode>PoJo</generatorMode>
							<fullFileName>PoJoClass.java</fullFileName>
							<skipFlag>partial</skipFlag>
							<methodLists>
								<methodList>testReduce</methodList>
							</methodLists>
						</generatorConfig>
					</generatorConfigs>
				</configuration>
				<dependencies>
					<dependency>
						<groupId>com.github.javaparser</groupId>
						<artifactId>javaparser-core</artifactId>
						<version>3.6.5</version>
					</dependency>
					<dependency>
						<groupId>com.athaydes.osgiaas</groupId>
						<artifactId>osgiaas-javac</artifactId>
						<version>0.7</version>
					</dependency>
					<dependency>
						<groupId>org.apache.commons</groupId>
						<artifactId>commons-lang3</artifactId>
						<version>3.10</version>
					</dependency>
					<dependency>
						<groupId>org.projectlombok</groupId>
						<artifactId>lombok</artifactId>
						<version>1.18.12</version>
					</dependency>
					<dependency>
						<groupId>org.apache.commons</groupId>
						<artifactId>commons-collections4</artifactId>
						<version>4.4</version>
					</dependency>
					<dependency>
						<groupId>com.google.guava</groupId>
						<artifactId>guava</artifactId>
						<version>23.0</version>
					</dependency>
				</dependencies>
			</plugin>
		</plugins>
	</build>
   ```
   * 通过maven命令执行
   
   `bdd-jupiter-generator:generate`
## 使用配置详解
   * configurationFile  
   configurationFile指定了生产单元测试用例代码的源文件所在包路径，一般格式为${basedir}/src/main/java/com/xxx  
   * golablGeneratorMode  
   golablGeneratorMode支持三种配置项：  
   > PoJo标识将configurationFile路径下的源文件，当作简单Java对象生成测试用例类，不考虑Spring集成依赖及Mock依赖等;      
   > Spring标识将configurationFile路径下的源文件，当作Spring bean类生成测试用例，自动添加Spring测试集成所需代码依赖，并“猜”出所需Mock的Fileds,自动生成到单测案例中，并集成相关Mock及断言代码， **Spring为默认配置**;      
   > SpringBoot除了自动添加SprintBoot测试集成所需代码依赖外，功能与Spring配置项一致;  
   * generatorConfig  
   generatorConfig是针对单个Java源文件生成测试用例的自定义配置，其中fullFileName元素标识需要自定义的Java源文件名称，generatorMode元素配置含义与golablGeneratorMode一致，如果单独配置了generatorMode元素，则golablGeneratorMode配置对该文件的测试用例不生效，skipFlag元素支持三种配置项：  
   > all 标识跳过fullFileName元素指定Java源文件，不生成单元测试文件  
   > none 标识fullFileName元素指定Java源文件中的所有方法，均生成单元测试案列，**none为默认配置**  
   > partial 标识跳过fullFileName元素指定Java源文件中的部分方法，以methodLists元素配置为准，其余方法生成单元测试案列  
## 举例说明  
   * PoJoClass
   ```
   package com.moqifei.bdd.jupiter.writer.test;  
   import org.springframework.stereotype.Service;
@Service
public class PoJoClass {
	public int testAdd(int a, int b) {
		return a+b;
	}
	
	public int testReduce(int a, int b) {
		return a- b;
	}
}
   ```  
   * 自动生成的PoJoClassTest  
   ```
   package com.moqifei.bdd.jupiter.writer.test;

import com.moqifei.bdd.jupiter.modle.annotations.ScenarioJsonSource;
import com.moqifei.bdd.jupiter.modle.annotations.ScenarioTest;
import com.moqifei.bdd.jupiter.modle.annotations.ScenarioSource;
import com.moqifei.bdd.jupiter.modle.annotations.Story;
import com.moqifei.bdd.jupiter.extension.Scene;  
import org.springframework.stereotype.Service;

 
/** 
* This bdd-jupiter style test cases file was auto-generated, 
* you should completed it by your own intelligence, come on & have fan!
*/
@Story(name = "name", description = "描述")
public class PoJoClassTest {


 
    @ScenarioTest(value = "name")
    @ScenarioJsonSource(resources = "/xxx/xxx.json", instance = Scene.class, key = "xxx")
    public void testTestAdd(Scene scene) { 
        scene.given("given phase desc",()->{//put your given code here.
        })
        .and("given and phase desc",()->{//put your given and code here.
        })
        .when("when phase desc",()->{//put your when code here.
        })
        .then("then phase desc",()->{//put your then code here.
        })
        .and("then and phase desc",()->{//put your then and code here.
        })
        .run();
    }
}
   ```  
   * SpringClass  
   ```
   package com.moqifei.bdd.jupiter.writer.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpringClass {
	@Autowired
	private PoJoClass poJoClass;
	
	public int doAdd() {
		int a = poJoClass.testAdd(poJoClass.testReduce(3, 1), 2);
		return a;
	}
}  
   ```  
   * 自动生成的SpringClassTest  
   ```
   package com.moqifei.bdd.jupiter.writer.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import com.moqifei.bdd.jupiter.extension.Scene;
import org.mockito.InjectMocks;
import org.mockito.ArgumentMatchers;

import org.junit.jupiter.api.BeforeEach;
import com.moqifei.bdd.jupiter.modle.annotations.ScenarioJsonSource;
import com.moqifei.bdd.jupiter.modle.annotations.ScenarioTest;
import com.moqifei.bdd.jupiter.modle.annotations.ScenarioSource;
import org.springframework.beans.factory.annotation.Autowired;
import com.moqifei.bdd.jupiter.modle.annotations.Story;
import org.springframework.stereotype.Service;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.mockito.Mock;
 
/** 
* This bdd-jupiter style test cases file was auto-generated, 
* you should completed it by your own intelligence, come on & have fan!
*/
@Story(name = "name", description = "描述")
@ContextConfiguration("classpath:spring/spring-config-test.xml")
@ExtendWith(SpringExtension.class)
public class SpringClassTest {
    @Mock
    PoJoClass poJoClass;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.initMocks(this);
    }

 
    @ScenarioTest(value = "name")
    @ScenarioJsonSource(resources = "/xxx/xxx.json", instance = Scene.class, key = "xxx")
    public void testDoAdd(Scene scene) { 
        scene.given("given phase desc",()->{//replace your given code here.
            scene.put("xxx", ArgumentMatchers.any());
        })
        .and("given and phase desc",()->{//put your given and code here.
        })
        .when("when phase desc",()->{//replace your when code here.
             Mockito.when(poJoClass.testAdd(poJoClass.testReduce(ArgumentMatchers.any(),ArgumentMatchers.any()),ArgumentMatchers.any())).thenReturn(ArgumentMatchers.any());
        })
        .then("then phase desc",()->{//replace your then code here.
            Object object = scene.<Object>get("xxx");
            int a = poJoClass.testAdd(poJoClass.testReduce(ArgumentMatchers.any(),ArgumentMatchers.any()),ArgumentMatchers.any());
            assertThat(a, is(equalTo(object)));
        })
        .and("then and phase desc",()->{//put your then and code here.
        })
        .run();
    }
}  
   ```  
  * SpringBootClass  
  ```
  package com.moqifei.bdd.jupiter.writer.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SpringBootClass {
	@Autowired
	private PoJoClass poJoClass;
	
	public int doReduce() {
		int a = poJoClass.testReduce(poJoClass.testAdd(3, 1), poJoClass.testReduce(2, 3));
		return b;
	}
}
   ```
   * 自动生成的SpringBootClassTest
   ```
   package com.moqifei.bdd.jupiter.writer.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import com.moqifei.bdd.jupiter.extension.Scene;
import org.mockito.InjectMocks;
import org.mockito.ArgumentMatchers;

import org.junit.jupiter.api.BeforeEach;
import com.moqifei.bdd.jupiter.modle.annotations.ScenarioJsonSource;
import com.moqifei.bdd.jupiter.modle.annotations.ScenarioTest;
import com.moqifei.bdd.jupiter.modle.annotations.ScenarioSource;
import org.springframework.beans.factory.annotation.Autowired;
import com.moqifei.bdd.jupiter.modle.annotations.Story;
import org.springframework.stereotype.Service;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.mockito.Mock;
 
/** 
* This bdd-jupiter style test cases file was auto-generated, 
* you should completed it by your own intelligence, come on & have fan!
*/
@Story(name = "name", description = "描述")
@SpringBootTest(classes = Object.class)
@ExtendWith(SpringExtension.class)
public class SpringBootClassTest {
    @Mock
    PoJoClass poJoClass;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.initMocks(this);
    }

 
    @ScenarioTest(value = "name")
    @ScenarioJsonSource(resources = "/xxx/xxx.json", instance = Scene.class, key = "xxx")
    public void testDoReduce(Scene scene) { 
        scene.given("given phase desc",()->{//replace your given code here.
            scene.put("xxx", ArgumentMatchers.any());
        })
        .and("given and phase desc",()->{//put your given and code here.
        })
        .when("when phase desc",()->{//replace your when code here.
             Mockito.when(poJoClass.testReduce(poJoClass.testAdd(ArgumentMatchers.any(),ArgumentMatchers.any()),poJoClass.testReduce(ArgumentMatchers.any(),ArgumentMatchers.any()))).thenReturn(ArgumentMatchers.any());
        })
        .then("then phase desc",()->{//replace your then code here.
            Object object = scene.<Object>get("xxx");
            int a = poJoClass.testReduce(poJoClass.testAdd(ArgumentMatchers.any(),ArgumentMatchers.any()),poJoClass.testReduce(ArgumentMatchers.any(),ArgumentMatchers.any()));
            assertThat(a, is(equalTo(object)));
        })
        .and("then and phase desc",()->{//put your then and code here.
        })
        .run();
    }
}  
    ```
