package org.brokenlance.persistence;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.client.model.UpdateOptions;

import org.bson.Document;

import lombok.extern.slf4j.Slf4j;

/**
 * MongoPersistenceManager.
 *
 * This interface defines the operations required to persist JSON object relational structures to a persistent medium
 * such as a file system or a NoSQL database.
 *
 * This isn't intended to be a high-performance many read/write framework, but rather a mechanism to persist an object
 * structure infrequently, e.g., upon startup and shutdown of an application/system.
 */
@Slf4j
public class MongoPersistenceManager< T > implements PersistenceManager< T >
{
   private static final String STORE = "store";
   private static final String NAME  = "name";
   private static final String DATA  = "data";
   private              String name  = null;

   /**
    * This will be used by both serialize and deserialize.
    */
   private MongoCollection< Document > getMongoCollection()
   {
      String creds = System.getProperty( "mongo.creds",      "mongoadmin:secret" );
      String host  = System.getProperty( "mongo.hostport",   "localhost:27017"   );
      String db    = System.getProperty( "mongo.database",   "repository"        );
      String coll  = System.getProperty( "mongo.collection", "systemundertest"   );
      String url   = "mongodb://" + creds + "@" + host;

      log.debug( "Connection URL: {}", url );

      MongoClient mongoClient = MongoClients.create( url );

      if( mongoClient == null )
      {
         log.error( "Unable to connect to the Mongo instance!" );
         return null;
      }

      MongoDatabase database = mongoClient.getDatabase( db );

      if( database == null )
      {
         log.error( "Unable to connect to the Mongo database!" );
         return null;
      }

      database.createCollection( coll );
      MongoCollection< Document > collection = database.getCollection( coll );

      return collection;
   }

   /**
    * @param T -- type that will be persisted as a JSON value.
    */
   @Override
   public void serialize( T entity )
   {
      serialize( entity, STORE );
   }

   /**
    * @return T -- type that will be returned from the persistence layer.
    */
   @Override
   public T deserialize()
   {
      return deserialize( STORE );
   }

   /**
    * @param T -- type that will be persisted as a JSON value.
    */
   @Override
   public void serialize( T entity, String key )
   {
      log.debug( "Will serialize entity to mongo: {}", entity );

      MongoCollection< Document > collection = getMongoCollection();

      if( collection == null )
      {
         log.error( "Unable to create connection to Mongo instance!" );
         return;
      }

      Document             query       = new Document();
      Document             document    = new Document();
      Document             update      = new Document();
      JsonTransformer< T > transformer = new JsonTransformer<>();

      query.put( NAME, key );
      document.put( NAME, key );
      document.put( DATA, transformer.serialize( entity ) );
      update.put( "$set", document );

      UpdateOptions opts   = new UpdateOptions().upsert( true );
      UpdateResult  result = collection.updateOne( query, update, opts );

      log.info( "Mongo update result: {}", result );
   }

   /**
    * @return T -- type that will be returned from the persistence layer.
    */
   @Override
   public T deserialize( String key )
   {
      log.debug( "Will deserialize the data from mongo." );

      MongoCollection< Document > collection = getMongoCollection();

      if( collection == null )
      {
         log.error( "Unable to create connection to Mongo instance!" );
         return null;
      }

      Document             query       = new Document();
      JsonTransformer< T > transformer = new JsonTransformer<>();

      query.put( NAME, key );

      Document result = collection.find( query ).first();

      log.info( "Found first document: {}", result );

      T entity = transformer.deserialize( result.get( DATA, String.class ) );

      return entity;
   }
}
