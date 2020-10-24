package br.com.sociotorcedor.domain.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.sociotorcedor.domain.entity.TimeEntity;

public interface TimeRepository extends CrudRepository<TimeEntity, Long> {
	
}
