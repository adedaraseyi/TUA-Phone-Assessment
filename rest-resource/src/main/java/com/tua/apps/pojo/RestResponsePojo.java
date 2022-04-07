package com.tua.apps.pojo;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestResponsePojo<T> {
    String message;
    Boolean success  = true;
    T data;
    Integer status = 200;

}
