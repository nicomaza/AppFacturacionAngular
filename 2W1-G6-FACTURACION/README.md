# TPI

Este proyecto es parte del Trabajo Práctico Integrador (TPI) realizado durante el año 2023, de las asignaturas Laboratorio de Computación IV, Programación IV y Metodología de Sistemas de la carrera "Tecnicatura Universitaria en Programación" de la "Universidad Tecnológica Nacional - Facultad Regional Córdoba".

# Observability usage
How to use new observability implementations of Prometheos, micrometer and grafana
There is a new file on the project called prometheus.yml, on the last line there is  configurations that points de project to an ip adress you should change only that last one to your own ip.
DOCKER: to use prometheus and grafana is neccesary to go docker and follow the next steps:
- 1 pull prometheus image and run with the next commands. 
  - docker pull prometheus 
  - Command to replace with your localpath to you pc file prometheus.yml inside project resources  example: docker run -p 9090:9090 -v  C:\Users\Thiago\Facultad\Cuatri2\LCI\REPO_TPI\2W1-G6-FACTURACION\src\main\resources\prometheus.yml prom/prometheus 
- Commands to whatc graphs on grafana: 
  - docker run -d --name grafana -p 3000:3000 grafana/grafana 
  - Finally when you have already prometheus, grafana and your project runing go to your ip adres:3000 this will open grafana, there login with default crendentials admin/admin, go to datasources: 
  - Add a new datasource based on graphana in the source url input set ipadres:9090 and test and save it 
  - Then in the dashboard section you can add a new dashboard based on that datasource and watch prometheus graphs

FOR MORE INFORMATION CHECK: Spring Boot - Monitoring Microservice with Prometheus and Grafana | Java Techie