package org.brokenlance.persistence;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

import com.fasterxml.jackson.annotation.PropertyAccessor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonTransformer< T >
{
   /**
    * @param String - JSON representation of object.
    * @return T
    */
   public T deserialize( String json )
   {
      ObjectMapper mapper = new ObjectMapper();
      T            obj    = null;

      mapper.setVisibility        ( PropertyAccessor.FIELD,             Visibility.ANY                        );
      mapper.activateDefaultTyping( new LaissezFaireSubTypeValidator(), ObjectMapper.DefaultTyping.EVERYTHING );

      try
      {
         obj = mapper.readValue( json, new TypeReference< T >() {} );
      }
      catch( JsonProcessingException e )
      {
         log.error( "Unable to deserialize JSON: {}", e.toString() );
      }

      return obj;
   }

   /**
    * @param T
    * @return String - JSON representation of object.
    */
   public String serialize( T obj )
   {
      ObjectMapper mapper = new ObjectMapper();
      String       data   = null;

      mapper.setVisibility        ( PropertyAccessor.FIELD,             Visibility.ANY                        );
      mapper.activateDefaultTyping( new LaissezFaireSubTypeValidator(), ObjectMapper.DefaultTyping.EVERYTHING );

      try
      {
         data = mapper.writeValueAsString( obj );
      }
      catch( JsonProcessingException e )
      {
         log.error( "Unable to serialize object to JSON: {}", e.toString() );
      }

      return data;
   }
}
