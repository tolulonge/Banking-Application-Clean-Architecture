# Banking-Application-Clean-Architecture
A multi-modular banking application that features Clean Architecture and allows you to view account related information, filter transactions based on value and type, cache and view transactions when offline.

This Clean architecture project also features concepts in Android such as;
  - Pragmative Reactive Programming using RxJava2
  - Android Architecture Components such as LiveData and ViewModel
  - Dependency Injection using Dagger 2
  - Repository Pattern
  - Testing with Junit and Espresso

It consists of 6 layers each represented as modules;
  - App Layer: hosts the dependency injection, and UI 
  - Presentation Layer: contains ViewModels, interface adapters, domain layer interaction
  - Domain Layer: contains entities, use cases, data contract
  - Data Layer: Uses repository pattern, communicates with datasources with data source contracts and exposes a single source of data for the app
  - DataSource Layer(Local): contains databases, dao's, entities and implement datasource contracts set forth by the data layer using Room
  - DataSource Layer(Remote): fulfills contract set forth by the dataLayer, contains api definitions, mappers
  
  
![banking_acct_info_screen](https://user-images.githubusercontent.com/40584796/152590553-4b1f73bd-74fa-44ef-89c2-1463cd803919.jpeg)
![banking_transaction_screen](https://user-images.githubusercontent.com/40584796/152590565-0c601ca2-4334-419b-bd8d-06e5b347bf39.jpeg)

Credits: Adapting CLEAN Architecture in Android Apps by Kaushal Dhruw- PluralSight
