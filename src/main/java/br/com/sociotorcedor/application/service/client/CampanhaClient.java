package br.com.sociotorcedor.application.service.client;

import java.time.LocalDate;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.sociotorcedor.application.model.Campanha;
import br.com.sociotorcedor.domain.entity.CampanhaEntity;

/**
 * Realiza a chamada da api externa de campanha, porem registrada no eureka
 * 
 * @author rodrigo
 *
 */
@FeignClient(value = "${integration.campanha.service-name}")
public interface CampanhaClient {

	/**
	 * get para buscar as campanhas
	 * @return
	 */
	@GetMapping(value = "/campanha", produces = MediaType.APPLICATION_JSON_VALUE)
	Campanha[] getCampanha();

	/**
	 * get para buscar campanha ativa por time do socio
	 * @param dtVigenciaFinal
	 * @param idTime
	 * @param idCampanha
	 * @return
	 */
	@GetMapping(value = "/campanha/vigenciawithtimecampanha", produces = MediaType.APPLICATION_JSON_VALUE)
	List<CampanhaEntity> findVigenciaTimeCampanha(
			@RequestParam(name = "dtVigenciaFinal", required = true) LocalDate dtVigenciaFinal,
			@RequestParam(name = "idTime", required = true) Long idTime,
			@RequestParam(name = "idCampanha", required = true) Long idCampanha);

	/**
	 * verifica as campanhas ativas por time
	 * @param dtVigenciaFinal
	 * @param idTime
	 * @return
	 */
	@GetMapping(value = "/campanha/vigenciawithtime", produces = MediaType.APPLICATION_JSON_VALUE)
	List<CampanhaEntity> findVigenciaTime(
			@RequestParam(name = "dtVigenciaFinal", required = true) LocalDate dtVigenciaFinal,
			@RequestParam(name = "idTime", required = true) Long idTime);

}
