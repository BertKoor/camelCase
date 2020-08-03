package nl.ordina.bertkoor.camelcase.repo;

import nl.ordina.bertkoor.camelcase.model.RegistryData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CamelRegistry extends CrudRepository<RegistryData, Long> {
}
