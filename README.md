# Apache Tomcat Settings

---

#### Window / Preferences / General / Workspace
- text file encoding : `UTF-8`

#### Window / Preferences / General / Editors / Text Editors / Spelling
- Encoding : `UTF-8`

#### Window / Preferences / Web / CSS Files, HTML Files, JSP Files 
- Encoding : `UTF-8`

#### Window / Preferences / Installed JREs / 
- ##### check `jdk 17`

#### Window / Preferences / Server / Runtime Environment /
- ##### click `add` then `apache`
- ##### select `Tomcat v9.0 Server`

### Windows > Perspective > Customize Perspective
- `Shortcuts` tab
- select `java` from category
- uncheck `Java Project from Existing Ant Build file`
	
- select `web` from category
- uncheck `Static Web Project`
	
- ###### Apply and Close

---

## Project properties

### Java Build Path / select `Libraries` tab

##### JDK setting
- select `Module /  JRE System Library[]` then `Edit...`
- under `Execution environment` select `jdk 17.0.12_7`

##### Server setting
- select `ClassPath` then `Add Library...`
- Server Runtime / Next / 
- check `Apache Tomcat v9.0`

### Java Compiler / 
- `compiler compliance level:` ==17== 

### Project Facets / 
- `Dynamic Web Module` to ==4.0== 
- `Java` to ==17==  

### Targeted Runtimes /
- select `Apache Tomcat v9.0` 

### Web Project Settings / 
- context root:  `/`

---

### Servers view / create a new server
- Apache Tomcat
  - select `Tomcat v9.0 Server`
  - browse `Tomcat installation directory` 

- double click `Tomcat v9.0 Server at localhost`
  - under the `Port Name` set port number of `HTTP/1.1` to `80`
  - under the `Sever Options` check `Serve modules without publishing`

- right click  `Tomcat v9.0 Server at localhost`
  - from `add and removes` double click the project you want to add or remove

---

##### Happy Hacking