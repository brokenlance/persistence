# persistence
This project provides an abstract persistence framework that utilizes the org.brokenlance.factory framework.
Based on whatever criteria is desired, a concrete persistence class is created that defines the operations for that particular medium.

Since this methodology uses the org.brokenlance.factory framework, additional concrete implementations can be added easily since the
SOLID principles are followed, i.e., concrete implementations can be added without having to modify other classes--open/closed principle.

### Usage
The factory pattern can be used to generate the desired concrete implementation, e.g., a regular file system persistence implementation, 
a mongo implementation, etc.

```java
      Factory< String, PersistenceManager< Document > > pFactory = new Factory<>( "org.brokenlance.persistence" );
      PersistenceManager< Document > pm = pFactory.build( "mongo" );
      pm.serialize( document );
```

In this case, the "file-system" detector should match and create its datatype:

```java
      Factory< String, PersistenceManager< Document > > pFactory = new Factory<>( "org.brokenlance.persistence" );
      PersistenceManager< Document > pm = pFactory.build( "file-system" );
      pm.serialize( document );
```

In this case, the "mongo" detector should match and create its datatype:

```java
      Factory< String, PersistenceManager< Document > > pFactory = new Factory<>( "org.brokenlance.persistence" );
      PersistenceManager< Document > pm = pFactory.build( "file-system" );
      Document = pm.deserialize();
```
