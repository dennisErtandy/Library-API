package com.example.demo;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Setter
@Getter
@AllArgsConstructor
@ToString(callSuper = true)
public class ResponseTemplate<T> implements Serializable {

  public static final String SUCCESS_STATUS = "SUCCESS";
  public static final String FAILED_STATUS = "FAILED";
  public static final String FAILED_CREATE_MESSAGE = "Failed to create";
  public static final String FAILED_DELETE_MESSAGE = "Failed to delete";
  public static final String FAILED_UPDATE_MESSAGE = "Failed to update";
  public static final String FAILED_RETRIEVE_MESSAGE = "Failed to retrieve";

  public static final String SUCCESS_CREATE_MESSAGE = "Success to create";
  public static final String SUCCESS_DELETE_MESSAGE = "Success to delete";
  public static final String SUCCESS_UPDATE_MESSAGE = "Success to update";
  public static final String SUCCESS_RETRIEVE_MESSAGE = "Success to retrieve";
  private String status;
  private String message;
  private T data;


}
