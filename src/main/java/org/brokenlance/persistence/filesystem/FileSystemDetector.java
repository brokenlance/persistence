package org.brokenlance.persistence;

import org.brokenlance.factory.Detector;

public class FileSystemDetector implements Detector< String, PersistenceManager >
{
   /**
    * Default constructor.
    */
   public FileSystemDetector()
   {
   }

   /**
    * @return DefaultPersistenceManager
    */
   @Override
   public PersistenceManager get()
   {
      return new DefaultPersistenceManager();
   }

   /**
    * @param String
    * @return boolean
    */
   @Override
   public boolean test( String data )
   {
      return "file-system".equals( data );
   }
}
