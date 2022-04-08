package com.tua.apps.core.phone.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Data
@SuperBuilder
@Table(name = "account")
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CorePhoneNumber {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    Integer accountId;

    @Size(max = 40)
    String number;
}
