# ShoppingCart
Demo Spring-Boot application of shopping-cart

## About

This is a demo project for practicing SpringBoot. The idea was to build some basic shopping cart web app.

Database is in memory H2.

## How to run

What you need to install/have on your computer:
- Java 8
- Maven

You can run the application from the command line with Maven. 
Go to the root folder of the application and type:
```
./mvnw spring-boot:run
```

Or just use your IDE(f.e. IntelliJIDEA)

The application should be up and running within a few seconds.

Api url: `http://localhost:8080/api/shoppingCart`

In `/src/main/resources/application.properties` it is possible to change database source and local port.

## Helper Tools

### H2 Database web interface

Go to the web browser and visit `http://localhost:8080/h2-console`

In field **JDBC URL** put 
```
jdbc:h2:mem:shopping_cart_db
```

and press 'Connect'.

Database is filled with some example products on application start.

