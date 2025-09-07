/*     */ package io.aiven.kafka.connect.http.converter;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.stream.Collectors;
/*     */ import org.apache.kafka.connect.data.Struct;
/*     */ import org.apache.kafka.connect.errors.DataException;
/*     */ import org.apache.kafka.connect.sink.SinkRecord;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RecordValueConverter
/*     */ {
/*     */   public RecordValueConverter() {
/*  33 */     this.jsonRecordValueConverter = new JsonRecordValueConverter();
/*     */     
/*  35 */     this.converters = Map.of(String.class, record -> (String)record.value(), Map.class, this.jsonRecordValueConverter, Struct.class, this.jsonRecordValueConverter);
/*     */   }
/*     */ 
/*     */   
/*     */   private static final ConcurrentHashMap<Class<?>, Converter> RUNTIME_CLASS_TO_CONVERTER_CACHE = new ConcurrentHashMap<>();
/*     */   
/*     */   private final JsonRecordValueConverter jsonRecordValueConverter;
/*     */   
/*     */   private final Map<Class<?>, Converter> converters;
/*     */   
/*     */   public String convert(SinkRecord record) {
/*  46 */     Converter converter = getConverter(record);
/*  47 */     return converter.convert(record);
/*     */   }
/*     */   
/*     */   private Converter getConverter(SinkRecord record) {
/*  51 */     return RUNTIME_CLASS_TO_CONVERTER_CACHE.computeIfAbsent(record.value().getClass(), clazz -> {
/*     */           boolean directlyConvertible = this.converters.containsKey(clazz);
/*     */           List<Class<?>> convertibleByImplementedTypes = getAllSerializableImplementedInterfaces(clazz);
/*     */           validateConvertibility(clazz, directlyConvertible, convertibleByImplementedTypes);
/*     */           Class<?> implementedClazz = clazz;
/*     */           if (!directlyConvertible) {
/*     */             implementedClazz = convertibleByImplementedTypes.get(0);
/*     */           }
/*     */           return this.converters.get(implementedClazz);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<Class<?>> getAllSerializableImplementedInterfaces(Class<?> recordClazz) {
/*  69 */     Objects.requireNonNull(this.converters); return (List<Class<?>>)getAllInterfaces(recordClazz).stream().filter(this.converters::containsKey)
/*  70 */       .collect(Collectors.toList());
/*     */   }
/*     */   
/*     */   public static Set<Class<?>> getAllInterfaces(Class<?> clazz) {
/*  74 */     Set<Class<?>> interfaces = new HashSet<>();
/*     */     
/*  76 */     for (Class<?> implementation : clazz.getInterfaces()) {
/*  77 */       interfaces.add(implementation);
/*  78 */       interfaces.addAll(getAllInterfaces(implementation));
/*     */     } 
/*     */     
/*  81 */     if (clazz.getSuperclass() != null) {
/*  82 */       interfaces.addAll(getAllInterfaces(clazz.getSuperclass()));
/*     */     }
/*     */     
/*  85 */     return interfaces;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void validateConvertibility(Class<?> recordClazz, boolean directlyConvertible, List<Class<?>> convertibleByImplementedTypes) {
/*  93 */     boolean isConvertibleType = (directlyConvertible || !convertibleByImplementedTypes.isEmpty());
/*     */     
/*  95 */     if (!isConvertibleType) {
/*  96 */       throw new DataException(
/*  97 */           String.format("Record value must be a String, a Schema Struct or implement `java.util.Map`, but %s is given", new Object[] { recordClazz }));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 102 */     if (!directlyConvertible && convertibleByImplementedTypes.size() > 1) {
/*     */       
/* 104 */       String implementedTypes = convertibleByImplementedTypes.stream().map(Class::getSimpleName).collect(Collectors.joining(", ", "[", "]"));
/* 105 */       throw new DataException(
/* 106 */           String.format("Record value must be only one of String, Schema Struct or implement `java.util.Map`, but %s matches multiple types: %s", new Object[] { recordClazz, implementedTypes }));
/*     */     } 
/*     */   }
/*     */   
/*     */   static interface Converter {
/*     */     String convert(SinkRecord param1SinkRecord);
/*     */   }
/*     */ }


/* Location:              C:\Users\Alper.Tuncay\Downloads\http-connector-for-apache-kafka-0.8.0-SNAPSHOT (2).jar!\io\aiven\kafka\connect\http\converter\RecordValueConverter.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */