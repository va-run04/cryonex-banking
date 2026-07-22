package com.cryonex.customer.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "id_sequence")
@Getter
@Setter
public class IdSequence {

    @Id
    @Column(name = "entity_name", length = 30)
    private String entityName;

    @Column(name = "current_value", nullable = false)
    private Long currentValue;

}