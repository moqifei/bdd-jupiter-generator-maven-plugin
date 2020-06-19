# bdd-jupiter-generator-maven-plugin
## bdd-jupiter-generator-maven-plugin的目的
   通过maven插件的形式，解析Java文件，自动生成bdd-jupiter单测框架模式下的单元测试用例
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
							<generatorMode>Spring</generatorMode>
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
        *通过maven命令执行
        `bdd-jupiter-generator:generate`
