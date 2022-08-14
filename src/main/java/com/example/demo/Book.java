package com.example.demo;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//To maintain default values when using builder
@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@FieldNameConstants
public class Book {

  @Id
  private String id;
  private String title;
  private String isbn;
  private double price;
  private Date deletedAt;
}
