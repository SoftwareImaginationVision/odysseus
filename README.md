# ODYSSEUS Backend Services

## Gaia-X Aligned Microservices Repository

![REPO-TYPE](https://img.shields.io/badge/repo--type-backend-blue?style=for-the-badge&logo=github)
![ARCH](https://img.shields.io/badge/architecture-microservices-orange?style=for-the-badge)
![COMPLIANCE](https://img.shields.io/badge/Gaia--X-aligned-success?style=for-the-badge)

------------------------------------------------------------------------

# 1. Executive Summary

This repository contains two backend/frontend microservices/microfrontends forming part of the
ODYSSEUS platform, designed in alignment with Gaia-X principles:

-   Sovereignty & Federated Architecture\
-   Secure Identity & Access Management\
-   Interoperability & Portability\
-   Transparency & Traceability\
-   Open Standards & Open APIs

The services are:

  Service    Purpose
  ---------- ---------------------------------------------------
  HomeApp    Backend management service for BCP application, and frontend for BCP
  Traveler   Functional backend service for traveler application, and Web frontend for tarveler application

Both services are implemented as independent Spring Boot microservices, using also JSF and Primefaces
deployable standalone or containerized.

------------------------------------------------------------------------

# 2. Architecture Overview

## 2.1 Architectural Style

-   Microservices Architecture\
-   Stateless REST APIs\
-   Container-ready\
-   Profile-based configuration (dev/test/prod)

## 2.2 Technology Stack

  Layer               Technology
  ------------------- -------------
  Runtime             Java 11
  Framework           Spring Boot
  Build               Maven
  Containerization    Docker
  Identity & Access   Keycloak
  Versioning          SemVer
  JSF		      Primefaces

------------------------------------------------------------------------

# 3. Service Catalogue

## 3.1 HomeApp Service

Description:\
Manages backend application logic for the BCP system.

Artifact:\
odysseus-homeapp-0.0.1-SNAPSHOT.jar

Default Ports: - 6099 (default) - 16099 (development)

Keycloak Configuration: - Realm: ODYSSEUS - Client: home-app

------------------------------------------------------------------------

## 3.2 Traveler Service

Description:\
Provides backend functionalities for the trader application.

Artifact:\
odysseus-traveler-0.0.1-SNAPSHOT.jar

Default Ports: - 6099 (default) - 16099 (development)

Keycloak Configuration: - Realm: ODYSSEUS - Client: odysseus-app

------------------------------------------------------------------------

# 4. Gaia-X Alignment

## 4.1 Identity & Access Management

Both services integrate with Keycloak, ensuring:

-   Federated identity support\
-   OAuth2 / OpenID Connect compliance\
-   Role-based access control\
-   Environment-specific identity providers

------------------------------------------------------------------------

## 4.2 Interoperability

-   RESTful APIs\
-   JSON-based communication\
-   Environment profile isolation\
-   Containerized deployment model

Deployable on: - On-premise infrastructure\
- Private cloud\
- Public cloud\
- Federated infrastructures

------------------------------------------------------------------------

## 4.3 Portability & Containerization

Example:

docker build -t homeapp . docker run -d -p 26099:6099 homeapp

------------------------------------------------------------------------

# 5. Deployment Model

## 5.1 Standalone JVM Deployment

Requirements: - Java 11+

Execution:

java -jar `<service>`{=html}.jar

Profile selection:

java -Dspring.profiles.active=test -jar `<service>`{=html}.jar

------------------------------------------------------------------------

## 5.2 Container Deployment

Build:

docker build -t `<image-name>`{=html} .

Run:

docker run -d -p `<external-port>`{=html}:6099 `<image-name>`{=html}

------------------------------------------------------------------------

# 6. Environment Profiles

  Profile       Purpose
  ------------- --------------------------------------
  development   Local testing with local Keycloak
  test          Platform integration testing
  production    Deployment in AMAZON / SIMAVI data lake

------------------------------------------------------------------------

# 7. Security Model

The security model includes:

-   OAuth2 authentication\
-   OpenID Connect tokens\
-   Centralized identity management\
-   Realm-based isolation\
-   Role-based authorization

Realm used: ODYSSEUS

------------------------------------------------------------------------

# 8. Build & Lifecycle Management

From each service directory:

mvn clean install

Tests:

mvn test

Artifacts generated in:

target/

------------------------------------------------------------------------

# 9. Governance & Contribution

-   Versioning follows Semantic Versioning\
-   Contributors listed in repository\
-   Pull request workflow defined in CONTRIBUTING.md\
-   MIT License

Primary Contributor: - Iacob Crucianu -- SIMAVI Developer

------------------------------------------------------------------------

# 10. Compliance Considerations

This repository aligns with Gaia-X principles through:

-   Federated identity integration\
-   Container portability\
-   Open standards adoption\
-   Microservices modularity\
-   Transparent deployment model\
-   Profile-based environment separation

Future extensions may include: - Service Self-Description (SD)\
- Policy rules declaration\
- Data sovereignty metadata\
- Federation catalog registration\
- Trust anchor integration

------------------------------------------------------------------------

# 11. License

Licensed under the MIT License. See LICENSE.md for details.
