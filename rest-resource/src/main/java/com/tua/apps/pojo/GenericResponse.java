package com.tua.apps.pojo;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GenericResponse {
    @Builder.Default
    String message = "";

    @Builder.Default
    String error = "";
}
