package io.aiven.kafka.connect.http.converter;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.errors.DataException;
import org.apache.kafka.connect.sink.SinkRecord;

















public class RecordValueConverter
{
  public RecordValueConverter() {
    this.jsonRecordValueConverter = new JsonRecordValueConverter();
    
    this.converters = Map.of(String.class, record -> (String)record.value(), Map.class, this.jsonRecordValueConverter, Struct.class, this.jsonRecordValueConverter);
  }

  
  private static final ConcurrentHashMap<Class<?>, Converter> RUNTIME_CLASS_TO_CONVERTER_CACHE = new ConcurrentHashMap<>();
  
  private final JsonRecordValueConverter jsonRecordValueConverter;
  
  private final Map<Class<?>, Converter> converters;
  
  public String convert(SinkRecord record) {
    Converter converter = getConverter(record);
    return converter.convert(record);
  }
  
  private Converter getConverter(SinkRecord record) {
    return RUNTIME_CLASS_TO_CONVERTER_CACHE.computeIfAbsent(record.value().getClass(), clazz -> {
          boolean directlyConvertible = this.converters.containsKey(clazz);
          List<Class<?>> convertibleByImplementedTypes = getAllSerializableImplementedInterfaces(clazz);
          validateConvertibility(clazz, directlyConvertible, convertibleByImplementedTypes);
          Class<?> implementedClazz = clazz;
          if (!directlyConvertible) {
            implementedClazz = convertibleByImplementedTypes.get(0);
          }
          return this.converters.get(implementedClazz);
        });
  }





  
  private List<Class<?>> getAllSerializableImplementedInterfaces(Class<?> recordClazz) {
    Objects.requireNonNull(this.converters); return (List<Class<?>>)getAllInterfaces(recordClazz).stream().filter(this.converters::containsKey)
      .collect(Collectors.toList());
  }
  
  public static Set<Class<?>> getAllInterfaces(Class<?> clazz) {
    Set<Class<?>> interfaces = new HashSet<>();
    
    for (Class<?> implementation : clazz.getInterfaces()) {
      interfaces.add(implementation);
      interfaces.addAll(getAllInterfaces(implementation));
    } 
    
    if (clazz.getSuperclass() != null) {
      interfaces.addAll(getAllInterfaces(clazz.getSuperclass()));
    }
    
    return interfaces;
  }




  
  private static void validateConvertibility(Class<?> recordClazz, boolean directlyConvertible, List<Class<?>> convertibleByImplementedTypes) {
    boolean isConvertibleType = (directlyConvertible || !convertibleByImplementedTypes.isEmpty());
    
    if (!isConvertibleType) {
      throw new DataException(
          String.format("Record value must be a String, a Schema Struct or implement `java.util.Map`, but %s is given", new Object[] { recordClazz }));
    }


    
    if (!directlyConvertible && convertibleByImplementedTypes.size() > 1) {
      
      String implementedTypes = convertibleByImplementedTypes.stream().map(Class::getSimpleName).collect(Collectors.joining(", ", "[", "]"));
      throw new DataException(
          String.format("Record value must be only one of String, Schema Struct or implement `java.util.Map`, but %s matches multiple types: %s", new Object[] { recordClazz, implementedTypes }));
    } 
  }
  
  static interface Converter {
    String convert(SinkRecord param1SinkRecord);
  }
}