package nl.ordina.bertkoor.camelcase.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface CamelRegistry extends CrudRepository<RegistryData, BigInteger> {
}
