package com.example.demo;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Setter
@Getter
@NoArgsConstructor
@ToString(callSuper = true)
public class BookResponse implements Serializable {
  
  private String id;
  private String title;
  private String isbn;
  private double price;

}
