package org.brokenlance.persistence;

import org.brokenlance.factory.Detector;

public class MongoDetector implements Detector< String, PersistenceManager >
{
   /**
    * Default constructor.
    */
   public MongoDetector()
   {
   }

   /**
    * @param TYPE
    * @return TYPE
    */
   @Override
   public PersistenceManager get()
   {
      return new MongoPersistenceManager();
   }

   /**
    * @param TYPE
    * @return TYPE
    */
   @Override
   public boolean test( String data )
   {
      return "mongo".equals( data );
   }
}
