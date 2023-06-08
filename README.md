# digitalOcean

Overview:
This project is a Java Web Application that lets users keep track of their tennis score. Its also possible to save ones data (wins, losses, results of game),
if the user creates an acount.

Installation Process:
This Project runs on Java 11. The application properties must be set, to ones personal details. You need to run mvn install -Pproduction in order to create a war file. 
Other than that all Dependencies and so on should work once the project is imported and the Maven clean install command is run.

Usage Guide:
All Views can be seen in the folder com.example.demo.views. All view classes have a @Route annotation that says where to find it. Theyre named accordingly
to what theyre representing. The /register route lets one register anccount...
All views can either can accessed by manually entering the according URL, or by clicking through the start page. 

Configuration:
In my case the project is hosted as a docker with digital ocean. The database is hosted by digital ocean as well. To do that, one has to create a database in 
digitalOcean and set the environment variables of the project in digitalOcean to the according value. 

Contact:
In case of any issues, you can contact me through ferdinand.gutzy@pm.me
