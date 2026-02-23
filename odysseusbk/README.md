# Project Title

Choose and leave only one of the following badge

![REPO-TYPE](https://img.shields.io/badge/repo--type-frontend-green?style=for-the-badge&logo=github)

The project is used to manage the whole UI by a UI container
There are two main functionalities. One functionality display information from different sources, and another one used to host application frames

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.
On a local machine, just get the jar file and execute it 
For example java -jar homeapp-0.0.1-SNAPSHOT.jar

If you want to use a specific profile, enter it in the command line
For example to use the test profile, just use the command
java -Dspring.profiles.active=test -jar homeapp-0.0.1-SNAPSHOT.jar

To build the docker image, follow the steps:
1. Copy docker/Dockerfile in the root folder of the system. 
2. Be sure that under the root there is target/homeapp-0.0.1-SNAPSHOT.jar
3. Run the command docker build -t homeapp . (be carefull to use dot at the end)
4. Wait the image to be created.

To run the docker image
1. Run the docker command: docker run -d homeapp
2. 


### Prerequisites

The project is developed as a microservice.
It is developed on Java 11.
The only thing necessary to run application is the JVM 11 or newer
You can run the application by calling:
java <app.name.jar>
The application is by default using port 6099. On development profile it is 16099
There is also a docker file, which can be used to create a docker image.

```
Give examples
```
### Security
The application is using Keycloack.
test profile is using keycloack installed on the platform (10.129.150.116)
production profile is using keycloack installed on ENG data lake environment
development environment is using a local keycloack
realm: MES-CoBrad
client: home-app
user: <tbd>
password:<tbd>
for testing the client used is public

### Installing

A step by step series of examples that tell you how to get a development env running
To install on a standalone system (no docker) just copy the jar file in the choosend working directory.
For example copy homeapp-0.0.1-SNAPSHOT.jar in /tmp/ folder

To build the docker image, follow the steps:
1. Copy docker/Dockerfile in the root folder of the system.
2. Be sure that under the root there is target/homeapp-0.0.1-SNAPSHOT.jar
3. Run the command docker build -t homeapp . (be carefull to use dot at the end)
4. Wait the image to be created.

To save the image
1. Run docker save, for example docker image save -o homeapp.tar homeapp
## Running the tests

Explain how to run the automated tests for this system

### Break down into end to end tests

Explain what these tests test and why

```
Give an example
```

### And coding style tests

Explain what these tests test and why

```
Give an example
```

## Deployment

To deploy on a system without docker:
1. Check that the curent version of java running is 11
2. Copy the jar file in a choosen location. For example copy homeapp-0.0.1-SNAPSHOT.jar in /tmp/ folder
3. Go to the folder where the jar file is. For example cd /tmp
4. Run java -jar command. Use in command line the appropriate profile. For example java -jar homeapp-0.0.1-SNAPSHOT.jar
If you want to use a specific profile, enter it in the command line
For example to use the test profile, just use the command
java -Dspring.profiles.active=test -jar homeapp-0.0.1-SNAPSHOT.jar


To run the docker image
1. Verify that the image exists. Run docker image ls and search the homeapp iamge
2. Run the docker command: docker run -d -p 26099:6099 mainhome

## Built With

* [SpringBoot](http://springboot.io) - The Java framework used
* [Maven](https://maven.apache.org/) - Dependency Management

## Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](tags). 

## Authors

* **Name Surname** - *Role* - [githubnickname](github_profile_url)
Iacob Crucianu - simavi developer
* 
See also the list of [contributors](contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Hat tip to anyone whose code was used
* Inspiration
* etc
