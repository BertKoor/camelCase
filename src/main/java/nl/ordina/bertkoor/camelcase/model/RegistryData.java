package nl.ordina.bertkoor.camelcase.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "CAMEL_REGISTRY", schema = "CAMEL_OWNER")
public class RegistryData {

    @Id @Column(name = "id")
    private Long id;

    @Column(name = "birth_year")
    private int birthYear;

    @Column(name = "birth_month")
    private int birthMonth;

    @Column(name = "humps")
    private int humps;

    @Column(name = "weight")
    private int weight;

}
