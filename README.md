# Projektkalkulations værktøj for Aplha Solutions
## Skaber samlet overblik og struktur over projekter

Dette er et projekt lavet af 4 studerende fra KEA til vores 2. semester eksamen. Projektet er blevet produceret udfra
kriterier stillet af Alpha Solutions, samt vores PO som var en studerende fra 4. semester. 
Projektet er vores forestilling om hvordan et projektkalkulations værktøj specifict lavet til Alpha Solutions kunne se ud.

Produktet bruger thymeleaf som frontend framework, sprinboot for vores backend logik, MySQL til håndteringen af vores
database, Microsoft Azure som vores deployment værktøj. Vi bruger også github actions for at kunne integrere en CI-CD 
pipeline. 

## Funktionalitet
Som det første vil du blive mødt af vores login side som beder dig om at logge ind med dine Alpha Solutions credentials.
Da du er ny bruger vil du som det første blive bedt om at skifte dit password til et nyt og sikker password.
Efter dette, hvis du er Admin, kan du tilgå vores medarbejderadministration hvorfra du kan tilføje, fjerne eller redigere
listen af medarbejdere som er i systemet.

Som projekt manager kan du tilføje, fjerne og redigere i projekter, sub projekter og opgaver. Du kan også tilføje
medarbejdere i systemet til projekter, subprojekter og opgaver. Admins har de samme rettigheder som en projekt manager.

Som medarbejder i systemet kan du kun se de projekter du er blevet tilføjet til. Derudover kan du indtaste hvor mange
timer du har brugt på en opgave.

Det er muligt i programmet at nedbryde et større projekt i mindre bider som subprojekter og opgaver.
Link til deployed version: 

## Teknologier
* Springboot (java)
* Thymeleaf
* CSS
* HTML
* MySql
* Gihub Actions
* Azure Web Apps