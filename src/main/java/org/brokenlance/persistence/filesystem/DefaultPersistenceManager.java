package org.brokenlance.persistence;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;
import static java.util.stream.Collectors.joining;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import lombok.extern.slf4j.Slf4j;

/**
 * DefaultPersistenceManager.
 *
 * This interface defines the operations required to persist JSON object relational structures to a persistent medium
 * such as a file system or a NoSQL database.
 *
 * This isn't intended to be a high-performance many read/write framework, but rather a mechanism to persist an object
 * structure infrequently, e.g., upon startup and shutdown of an application/system.
 */
@Slf4j
public class DefaultPersistenceManager< T > implements PersistenceManager< T >
{
   private static final String filename = "persistence.json";

   /**
    * @param T -- type that will be persisted as a JSON value.
    */
   @Override
   public void serialize( T entity )
   {
      log.info( "Will serialize entity: {}", entity );

      try
      {
         JsonTransformer< T > transformer = new JsonTransformer<>();
         Files.write( Path.of( filename ), transformer.serialize( entity ).getBytes(), CREATE, WRITE );
      } 
      catch( IOException e ) 
      {
         log.error( "Unable to persist JSON data to file: {}", e.toString() );
      }
   }

   /**
    * @return T -- type that will be returned from the persistence layer.
    */
   @Override
   public T deserialize()
   {
      T obj = null;

      log.info( "Will deserialize the data." );

      try
      {
         JsonTransformer< T > transformer = new JsonTransformer<>();
         obj = transformer.deserialize( Files.lines( Path.of( filename ) ).collect( joining() ) );
      } 
      catch( IOException e ) 
      {
         log.error( "Unable to read JSON data from file: {}", e.toString() );
      }

      return obj;
   }
}
