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
```
mvn package -q
```

## How to Use the Jar file directyly
```
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
### 源码和Index
- 使用了/home/zhiganggao/test目录下的源码，一个简单的main.c文件
- 索引目录在 /home/zhiganggao/Index里
- 已加入自动搜索索引的目录
- 有效的测试命令如下
```
java -jar target/index-0.0.3-SNAPSHOT.jar -q main
```
- 问题: -f 指定Index目录的参数目前是无效的