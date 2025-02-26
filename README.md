# JDBC-Oracle


## Steps to set it up in MAC M1/M2:-

<ul>
<li> Download the Java JDK</li>
<li>Downlaod Docker</li>
<li>Install the Linux ARM 64 in zip only </li>
<li> git clone https://github.com/oracle/docker-images</li>
<li> go to docker-images--OracleDatabase--SingleInstance--dockerfiles-19.0.0.0 then copy paste the zip file of that linux over here  
</li>
<li>Then build the docker image with the command ./buildContainerImage.sh -v 19.3.0 -e</li>
<li> Run the Docker image docker run -d --name oracle19 -e ORACLE_PWD=mypassword1 -p 1521:1521 oracle/database:19.3.0-ee</li>
<li> after that downlaod the jar ile to build 7 execute JDBC according to your java jdk version this project use 17 from here :- https://www.oracle.com/database/technologies/appdev/jdbc-downloads.html </li>
<li>Command to build the java class javac -d bin -cp lib/ojdbc17.jar src/Connect.java </li>
<li>Command to run the class  files java -cp bin:lib/ojdbc17.jar Connect<li>
<li>then to connect to sql write this command docker exec -it 15edb64e33c7 sqlplus system/mypassword1@//localhost:1521/orclpdb1</li>
<li>then connect it with this connect SYS/mypassword1@//localhost:1521/orclpdb1 as sysdba;</li>



# Create Table:-

<img src="./img/create-table.png">

# Insert Data:-

<img src="./img/Enterd-data.png">