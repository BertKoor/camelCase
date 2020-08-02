package nl.ordina.bertkoor.camelcase.repo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigInteger;

@Data
@Entity
@Table(name = "CAMEL_REGISTRY", schema = "CAMEL_OWNER")
public class RegistryData {

    @Id @Column(name = "id")
    private BigInteger id;

    @Column(name = "birth_year")
    private int birthYear;

    @Column(name = "birth_month")
    private short birthMonth;

    @Column(name = "humps")
    private short humps;

    @Column(name = "weight")
    private int weight;

}
