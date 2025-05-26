# Projektkalkulations værktøj for Aplha Solutions

## Skaber samlet overblik og struktur over projekter

Dette er et projekt lavet af 4 studerende fra KEA til vores 2. semester eksamen. Projektet er blevet produceret udfra
kriterier stillet af Alpha Solutions, samt vores PO som var en studerende fra 4. semester.
Projektet er vores forestilling om hvordan et projektkalkulations værktøj specifikt lavet til Alpha Solutions kunne se
ud.

Produktet bruger thymeleaf som frontend framework, sprinboot for vores backend logik, MySQL til håndteringen af vores
database, Microsoft Azure som vores deployment værktøj. Vi bruger også github actions for at kunne integrere en CI-CD
pipeline.

## Funktionalitet

Som det første vil du blive mødt af vores login side som beder dig om at logge ind med dine Alpha Solutions credentials.
Da du er ny bruger vil du som det første blive bedt om at skifte dit password til et nyt og sikker password.
Efter dette, hvis du er Admin, kan du tilgå vores medarbejderadministration hvorfra du kan tilføje, fjerne eller
redigere
listen af medarbejdere som er i systemet.

Som projekt manager kan du tilføje, fjerne og redigere i projekter, sub projekter og opgaver. Du kan også tilføje
medarbejdere i systemet til projekter, subprojekter og opgaver. Admins har de samme rettigheder som en projekt manager.

Som medarbejder i systemet kan du kun se de projekter du er blevet tilføjet til. Derudover kan du indtaste hvor mange
timer du har brugt på en opgave.

Det er muligt i programmet at nedbryde et større projekt i mindre bider som subprojekter og opgaver.
Link til deployed version: http://alphasolutions-deaddfgaa7bgarfk.northeurope-01.azurewebsites.net/

## Teknologier

* Springboot (java)
* Thymeleaf
* CSS
* HTML
* MySql
* Gihub Actions
* Azure Web Apps

## Kom i gang

### forudsætninger

* JDK 21 eller nyere
* Maven
* MySQL server

### Installation

1. Klon repo fra https://github.com/Daniel-Platz/AlphaSolutions.git
2. Opret en MySQL database kaled 'alphasolutions_db'. Kør MySQL scripts fra AlphaSolutions\docs\
3. Konfigurer dine environment variables så de passer til din database login credenser. Eller indtast dem direkte i
   src/main/resources/application-dev.properties (USIKKER!!!)

<pre>   spring.datasource.url=jdbc:mysql://localhost:3306/alphasolution_db [indsæt evt. deployed database]
   spring.datasource.username=[username]
   spring.datasource.password=[password] 
</pre>
4. Byg of kør projektet.
5. Applikation burde nu kunne køres lokalt på http://localhost:8080/