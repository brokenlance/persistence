package org.brokenlance.persistence;

/**
 * PersistenceManager.
 *
 * This interface defines the operations required to persist a single JSON object relational structures to a persistent medium
 * such as a file system or a NoSQL database.
 *
 * This isn't intended to be a high-performance many read/write framework, but rather a mechanism to persist an object
 * structure infrequently, e.g., upon startup and shutdown of an application/system.
 *
 * The following demonstrates how to use this framework with the Factory framework:
 *
 *    Factory< String, PersistenceManager< Document > > pFactory = new Factory<>( "org.brokenlance.persistence" );
 *    PersistenceManager< Document > pm = pFactory.build( "file-system" );
 *    pm.serialize( document );
 *    Document deser = pm.deserialize();
 */
public interface PersistenceManager< T >
{
   /**
    * @param T -- type that will be persisted as a JSON value.
    */
   public void serialize( T entity );

   /**
    * @return T -- type that will be returned from the persistence layer.
    */
   public T deserialize();

   /**
    * @param T -- type that will be persisted as a JSON value.
    * @param String -- the key under which the data will be associated.
    */
   public void serialize( T entity, String key );

   /**
    * @return T -- type that will be returned from the persistence layer.
    * @param String -- the key under which the data is associated.
    */
   public T deserialize( String key );
}
