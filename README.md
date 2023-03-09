# TroobIndex
- 本工程只用于测试索引是否成功创建并可以使用
- 本工程不用于创建索引
- 本工程不会被Troob的初始化流程使用，但是可用于测试Troob的初始化流程是否正常工作
## Help
- this project is used to use Index to search some key word
## How to Move this project to anywhare
- basically you only need these files
```
pom.xml
src/*
```
- copy them ,then
```shell
mvn package -q
```

## How to Use the Jar file directyly
```shell
java -jar target/index-0.0.3-SNAPSHOT.jar -h
java -jar target/index-0.0.3-SNAPSHOT.jar -q Copyright

java -jar target/index-0.0.3-SNAPSHOT.jar -f /Users/gaozhigang/Downloads/index -q echo

java -jar target/index-0.0.3-SNAPSHOT.jar -f /Users/gaozhigang/Downloads/Index -q MessagingException 

java -jar target/index-0.0.3-SNAPSHOT.jar -f /Users/gaozhigang/Downloads/Index -q handleOpthins

```

## In Docker
```
/opt/jdk1.8.0_161/bin/java -jar target/index-0.0.3-SNAPSHOT.jar -f /opt/files/index_base/TroobInit -q mysqlshCount
```

# 在不同环境上的测试
## 在Ubuntu 21 上
### Maven 镜像配置
- 参考mirror.xml里的内容
- 修改路径 /etc/maven/settings.xml
### 源码和Index
- 使用了/home/zhiganggao/test目录下的源码，一个简单的main.c文件
- 索引目录在 /home/zhiganggao/Index里
- 已加入自动搜索索引的目录
### 有效的测试命令如下
```shell
java -jar target/index-0.0.3-SNAPSHOT.jar -q main
```
 
## 在Ubuntu 14 上
### Maven 镜像配置
- 修改路径 ~/.m2/settings.xml 
- 内容
```xml
  <mirrors>
    <mirror>
     <id>aliyunmaven</id>
     <mirrorOf>central</mirrorOf>
     <name>阿里云公共仓库</name>
     <url>https://maven.aliyun.com/repository/central</url>
    </mirror>
    <mirror>
      <id>repo1</id>
      <mirrorOf>central</mirrorOf>
      <name>central repo</name>
      <url>http://repo1.maven.org/maven2/</url>
    </mirror>
    <mirror>
     <id>aliyunmaven</id>
     <mirrorOf>apache snapshots</mirrorOf>
     <name>阿里云阿帕奇仓库</name>
     <url>https://maven.aliyun.com/repository/apache-snapshots</url>
    </mirror>
  </mirrors>

```
### JAVA 版本适配问题
- 直接去掉pom.xml 里的下面配置, 就会自动使用当前可用的java版本了
```xml
		<maven.compiler.source>11</maven.compiler.source>
		<maven.compiler.target>11</maven.compiler.target>
```
### 有效的测试命令如下
```shell
java -jar target/index-0.0.3-SNAPSHOT.jar -f /opt/index_base/kernel_2_6_32_27 -q open
java -jar target/index-0.0.3-SNAPSHOT.jar -f /opt/index_base/dpdk -q open
```