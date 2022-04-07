package com.tua.apps.core.phone.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Data
@SuperBuilder
@Table(name = "account")
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CoreAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_generator")
    @SequenceGenerator(name = "account_generator", sequenceName = "account_id_seq", allocationSize = 1)
    Long id;

    @NotBlank
    @Size(max = 40)
    String authId;

    @NotBlank
    @Size(max = 30)
    String username;
}
