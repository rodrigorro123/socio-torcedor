package br.com.sociotorcedor.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import br.com.sociotorcedor.domain.entity.SocioTorcedorEntity;
/**
 * interface para realizar integracação com base
 * @author rodrigo
 *
 */
public interface SocioTorcedorRepository extends CrudRepository<SocioTorcedorEntity, Long> {
	
	Optional<SocioTorcedorEntity> findByIdSocioAndAtivo(Long idSocio, Boolean ativo) ;
	
	Optional<SocioTorcedorEntity> findByEmailAndAtivo(String email,Boolean ativo);
	
	@Query( value = " SELECT * \n" + 
					" FROM SOCIO_TORCEDOR_CAMPANHAS sc, \n" + 
					"            SOCIO_TORCEDOR st, \n" + 
					"            CAMPANHA c\n" + 
					" where c.iD_CAMPANHA  = sc.camPANHAS_ID_CAMPANHA \n" + 
					" and sc.SOCIO_TORCEDOR_ID_SOCIO  = st.iD_SOCIO  \n" + 
					" and st.id_time = c.id_time\n" + 
					" and st.ativo = true\n" + 
					" and c.dt_vigencia_final > CURRENT_DATE\n" + 
					" and st.email = :email ",   nativeQuery = true)
	Optional<SocioTorcedorEntity> findByEmailAndAtivoByCampanhaTime(@Param("email") String email );

}
