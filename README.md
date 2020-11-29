# P06_DAJAVA_SpringBoot_DAL_BDD

## First Run

Install a MySQL/Maria DBB like [Xampp](https://www.apachefriends.org/fr/index.html) and execute [Schema SQL Script](https://jedi10.github.io/P06_DAJAVA_SpringBoot_DAL_BDD/scriptsSQL/paymybuddy_schema.sql) to create all tables in database.
 You can also just create the database _paymybuddy_ and let the application build all tables on the first run. You can add some content in tables with [Data SQL Script](https://jedi10.github.io/P06_DAJAVA_SpringBoot_DAL_BDD/scriptsSQL/paymybuddy_data.sql)


Build project with Maven file _pom.xml_ to import all dependencies.
    
    mvn package
    mvn clean install
    
## Resources
    
The project need _Java JDK 11_ or newer.
Open JDK is recommended: https://adoptopenjdk.net

The project use _Spring Boot 2.34_ https://start.spring.io 

### Dependencies 

    Lombok, 
    Spring Web (Tomcat),
    Spring Security,
	OAuth2 Client,
	Spring Data JPA,
	H2 DBB


