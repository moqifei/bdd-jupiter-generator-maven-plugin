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
					<configurationFile>${basedir}/src/main/java/com/moqifei/bdd/jupiter/writer</configurationFile>
					<golablGeneratorMode>PoJo</golablGeneratorMode>
					<generatorConfigs>
						<generatorConfig
							implementation="com.moqifei.bdd.jupiter.generate.source.model.GeneratorConfig">
							<generatorMode>SpringBoot</generatorMode>
							<fullFileName>BasicStoryWriter.java</fullFileName>
							<skipFlag>all</skipFlag>
						</generatorConfig>
						<generatorConfig
							implementation="com.moqifei.bdd.jupiter.generate.source.model.GeneratorConfig">
							<generatorMode>Spring</generatorMode>
							<fullFileName>TextWriter.java</fullFileName>
							<skipFlag>none</skipFlag>
						</generatorConfig>
						<generatorConfig
							implementation="com.moqifei.bdd.jupiter.generate.source.model.GeneratorConfig">
							<generatorMode>PoJo</generatorMode>
							<fullFileName>Test.java</fullFileName>
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
