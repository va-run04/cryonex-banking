package com.cryonex.account.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class IdSequence {

    @Id
    @Column(name = "entity_name", length = 30)
    private String entityName;

    @Column(name = "current_value", nullable = false)
    private Long currentValue;

}
