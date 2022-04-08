package com.tua.apps.tuaphoneapi.dto.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InboundSMSRequest {

    @NotBlank(message = "from is missing")
    @Size(min = 6, max = 20, message = "from is invalid")
    String from;

    @NotBlank(message = "to is missing")
    @Size(min = 6, max = 20, message = "to is invalid")
    String to;

    @NotBlank(message = "text is missing")
    @Size(min = 1, max = 120, message = "text is invalid")
    String text;
}
