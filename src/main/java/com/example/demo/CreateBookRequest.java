package com.example.demo;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@ToString(callSuper = true)
public class CreateBookRequest implements Serializable {

  @NotNull
  private String title;
  @NotNull
  private String isbn;
  @NotNull
  private double price;
}
