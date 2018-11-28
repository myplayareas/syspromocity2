# Promocity
Aplicação cliente que manipula o banco do promocity (http://www.github.com/armandossrecife/promocity). 

Features
---

* CRUD de usuários
* CRUD de lojistas
* Controle de autenticação de usuários e lojistas
* CRUD de promoções
* CRUD de cupons
* Camada de segurança
* Box contendo mapa das lojas cadastradas

Sobre as operações para execução da aplicação
---

1. Faça o clone do repositório.

2. É preciso ter o mysql 5 instalado na sua instância.
- Obs: algumas vezes você pode enfrentar problemas com o driver mysql se você estiver usando mysql> 5.6, por exemplo se você tiver usando mysql 8 ou posterior, então você tem que substituir o driver de acordo com sua versão. Assim, é necessário atualizar as dependências do pom.xml de acordo com sua configuração e versão local.

3. Rode o script de banco de dados e cadastre o usuário admin (armando) com senha armando.

4. Limpe o projeto via comando clean do maven.
```
$mvn clean
```
5. Compile o projeto via modo teste do maven. 
```
$mvn test
```
6. Execute a classe principal (SystemApplication) do projeto via maven. 
```
$mvn spring-boot:run
```

Por padrão a aplicação roda em http://localhost:8080/login


Characteristics
---

* Spring Boot;
* Spring Security for basic login with permissions;
* Thymeleaf para view;
* Mysql Database or others;
* Basic entity crud;

TODO
---

* Search in the listing;
* Model of Dialog;
* Template for sending e-mail with template;

About Spring-boot packaging
---

1. Adding Classpath Dependencies
```
$mvn dependency:tree
```

2. Running the Example
Since you used the spring-boot-starter-parent POM, you have a useful run goal that you can use to start the application
```
$mvn spring-boot:run
```
3. How to test, execute and package the application?
You have to put Classpath Dependencies
```
$mvn dependency:tree
```
3.2. If you want to run the example directly from main path source code
```
$mvn spring-boot:run
```
3.3. If you want to create .jar package application. 
The packaget application .jar are archives containing your compiled classes along with all of the jar dependencies that your code needs to run.
```
$mvn clean package
```
3.4 To run that application, use the java -jar command, as follows:
```
$java -jar target/artefactId-version.jar
```
For further details click the link below to read full article about spring-boot packaging: 
https://docs.spring.io/spring-boot/docs/current/reference/html/getting-started-first-application.html#getting-started-first-application-pom

Special Configurations
---
For database security, datasource, jpa, thymeleaf and session configuration you have to change values in src/main/resources/sql/security.sql and src/main/resources/application.properties

References
---

[1] Spring MVC 4. Java Framework for MVC Web Applications. Available at https://docs.spring.io/spring/docs/current/spring-framework-reference/html/mvc.html

[2] Spring Boot 1. It is a Java Framework (based on the Spring Platform) for web applications that use inversion control container for the Java platform. Available at https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-security

[3] Thymeleaf. It is a Java / XHTML / HTML5 Java model engine that can work in both web (servlet-based) and non-web environments. It is best suited to serve XHTML / HTML5 in the MVC-based web application preview layer. Available at https://www.thymeleaf.org

[4] Bootstrap. Vision layer framework for responsive web applications. Available at https://v4-alpha.getbootstrap.com/getting-started/introduction

[5] JQuery. JavaScript Function Library. Available at https://jquery.com

[6] ORM JPA. Abstartion of data access. Available at https://docs.spring.io/spring-data/jpa/docs/current/reference/html

[7] Spring Security. It is a Java framework that provides an access control framework for Java / Java EE applications that provides authentication, authorization, and other security features for enterprise applications. Available at https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle

[8] Maven. Management of Builds and Dependencies. Available at https://maven.apache.org

[9] Mysql 5. Database Management System. Available at https://dev.mysql.com/downloads/mysql

[10] AdminLTE. Control panel template for web applications. Available at https://adminlte.io/themes/AdminLTE/index.html

Questions, suggestions or any kind of criticism contact us by email armando@ufpi.edu.br
