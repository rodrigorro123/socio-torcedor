package br.com.sociotorcedor.application.service.queue;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.sociotorcedor.application.exception.ApiException;
import br.com.sociotorcedor.application.model.SocioTorcedor;
import br.com.sociotorcedor.application.service.client.CampanhaClient;
import br.com.sociotorcedor.domain.entity.CampanhaEntity;
import br.com.sociotorcedor.domain.entity.SocioTorcedorEntity;
import br.com.sociotorcedor.domain.repository.SocioTorcedorRepository;
import br.com.sociotorcedor.domain.repository.TimeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Service
public class AsynchronosService {
	
	private final SocioTorcedorRepository scRepository;
	private final TimeRepository timeRepository;
	private final CampanhaClient campanhaClient;

	public void salveSocioAsyc(SocioTorcedor socio) throws ApiException {
		 try {
			 
			 SocioTorcedorEntity socioEntity = new SocioTorcedorEntity();
			 PropertyUtils.copyProperties(socioEntity, socio );
			 
			 socioEntity.setTime(timeRepository.findById(socio.getIdTime()).orElse(null));
			 if(Objects.nonNull(socio.getIdTime()) && Objects.nonNull(socio.getIdCampanha())) {
				 List<CampanhaEntity> vtc = campanhaClient.findVigenciaTimeCampanha(LocalDate.now(), socio.getIdTime(), socio.getIdCampanha());
				 socioEntity.setCampanhas (vtc);
			 }
			 
			 scRepository.save(socioEntity); 
			
			} catch (Exception e) {
				log.error(e.getMessage());
	            throw ApiException
	            .builder()
	            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
	            .code(ApiException.GENERAL_ERROR)
	            .message("Erro ao processar dados fila")
	            .reason("Erro ao processar dados")
	            .build();
			}
		}
	
	public void atualizarSocioAsyc(SocioTorcedor socio) throws ApiException {
		 try {
					 
			 SocioTorcedorEntity updtsocio = scRepository.findByIdSocioAndAtivo(socio.getIdSocio(), true).orElse(null);
			 
			 if(Objects.isNull(socio.getIdSocio())) {
				 updtsocio = scRepository.findByEmailAndAtivo(socio.getEmail(), true).orElse(null);
			 }
			 		 
			 if(!updtsocio.getCampanhas().isEmpty() && updtsocio.getTime().getIdTime() != socio.getIdTime() ) {
				 updtsocio.getCampanhas().removeAll(updtsocio.getCampanhas());
			 }
			 
			 updtsocio.setTime(timeRepository.findById(socio.getIdTime()).orElse(null));
			 if(Objects.nonNull(updtsocio.getTime().getIdTime()) && Objects.nonNull(socio.getIdCampanha())) {
				 List<CampanhaEntity> vtc = campanhaClient.findVigenciaTimeCampanha(LocalDate.now(), updtsocio.getTime().getIdTime(), socio.getIdCampanha());
				 if(!updtsocio.getCampanhas().containsAll(vtc)) {
					 updtsocio.getCampanhas().addAll(vtc);
				 }
			 }            
			 scRepository.save(updtsocio); 
			
			}  catch (Exception e) {
				log.error(e.getMessage());
	            throw ApiException
	            .builder()
	            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
	            .code(ApiException.GENERAL_ERROR)
	            .message("Erro ao processar dados fila")
	            .reason("Erro ao processar dados")
	            .build();
			}
		}
	
}
