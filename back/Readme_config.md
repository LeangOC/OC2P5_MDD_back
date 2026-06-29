# Envirronnement git bash :
export DB_USER=user_root  
export DB_PASSWORD=password_root  

# Démarrage l'application:  
- mvn spring-boot:run

# lancer les tests de couverture Jacoco  
- mvn clean verify    
Rapport de test:  target/site/jacoco/index.html  

# Documentation Swagger/OpenApi :
http://localhost:8080/swagger-ui/index.html

# Javadoc :
- mvn javadoc:javadoc  
rapport Javadoc : target/site/apidocs/index.html

