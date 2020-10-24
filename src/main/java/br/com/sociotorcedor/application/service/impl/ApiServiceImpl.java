package br.com.sociotorcedor.application.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import br.com.sociotorcedor.application.exception.ApiException;
import br.com.sociotorcedor.application.model.Campanha;
import br.com.sociotorcedor.application.model.DadosResponse;
import br.com.sociotorcedor.application.model.RequestAsyc;
import br.com.sociotorcedor.application.model.SocioTorcedor;
import br.com.sociotorcedor.application.model.SocioTorcedorResponse;
import br.com.sociotorcedor.application.service.ApiService;
import br.com.sociotorcedor.application.service.client.CampanhaClient;
import br.com.sociotorcedor.application.service.queue.SocioQueueSender;
import br.com.sociotorcedor.domain.entity.CampanhaEntity;
import br.com.sociotorcedor.domain.entity.SocioTorcedorEntity;
import br.com.sociotorcedor.domain.repository.SocioTorcedorRepository;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * Realiza as operaçõs principais do cadastro de socio
 * @author rodrigo
 *
 */
@AllArgsConstructor
@Service
@Log4j2
public class ApiServiceImpl implements ApiService{

	private final ModelMapper modelMapper;
	private final SocioTorcedorRepository scRepository;
	private final CampanhaClient campanhaClient;
	private final SocioQueueSender queue;
	
	/**
	 * Realiza a gravação do socio enviando os dados para fila apos validações
	 */
	@Override
	public DadosResponse salveSocio(SocioTorcedor socio) throws ApiException {
	 try {
		 DadosResponse response  = new DadosResponse();
		 
		 if(socioAtivo(socio) ) {
	            throw ApiException
	            .builder()
	            .statusCode(HttpStatus.BAD_REQUEST.value())
	            .code(ApiException.VALIDATION_ERROR)
	            .message("Ja existe um socio ativo")
	            .reason("Erro ao cadastrar socio")
	            .build();
		 }
		 
		 //monta objeto para envio da fila
		 RequestAsyc asyc = new RequestAsyc();
		 asyc.setSocio(socio);
		 asyc.setTipoProcessamento(1);
		 queue.send(asyc);

		 response.setCode(202);
		 response.setMessage("Dado enviado para processamento com Sucesso ");
		 return response;
		
		}catch(ApiException ae) {
            throw ApiException
            .builder()
            .statusCode(ae.getStatusCode())
            .code(ae.getCode())
            .message(ae.getMessage())
            .reason(ae.getReason())
            .build();
		} catch (Exception e) {
			log.error(e.getMessage());
            throw ApiException
            .builder()
            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .code(ApiException.GENERAL_ERROR)
            .message("Erro ao buscar dados")
            .reason("Erro ao buscar dados")
            .build();
		}
	}
	
	/**
	 * busca o socio por email
	 */
	public SocioTorcedorResponse getSocio(String email) throws ApiException{
		try {
			
			SocioTorcedorResponse sc = new SocioTorcedorResponse();
			SocioTorcedorEntity  socio = scRepository.findByEmailAndAtivoByCampanhaTime(email).orElse(scRepository.findByEmailAndAtivo(email,true).orElse(null));
			
			if(socio == null) {
	            throw ApiException
	            .builder()
	            .statusCode(HttpStatus.NOT_FOUND.value())
	            .code(ApiException.NOTFOUND_ERROR)
	            .message("Dados nao encontrado para o email informado")
	            .reason("dados nao localizado")
	            .build();
			}
			sc = modelMapper.map(socio, SocioTorcedorResponse.class);
			
			if(sc.getCampanhas().isEmpty()) {
				sc.setCampanhasDisponiveisNaoCadastradas(montaCampanhaNaoCadastrada(socio));
			}
			
			return sc;
		}catch(ApiException ae) {
            throw ApiException
            .builder()
            .statusCode(ae.getStatusCode())
            .code(ae.getCode())
            .message(ae.getMessage())
            .reason(ae.getReason())
            .build();
		} catch (Exception e) {
			log.error(e.getMessage());
            throw ApiException
            .builder()
            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .code(ApiException.GENERAL_ERROR)
            .message("Erro ao buscar dados")
            .reason("Erro ao buscar dados")
            .build();
		}
	}
	
	/**
	 * Monta as campanhas para o socio, referente ao time caso nao tenha sido escolhida nenhuma
	 * @param socio
	 * @return
	 * @throws ApiException
	 */
	private List<Campanha> montaCampanhaNaoCadastrada(SocioTorcedorEntity  socio )throws ApiException {
		try {
			List<CampanhaEntity> campanhas = campanhaClient.findVigenciaTime( LocalDate.now() , socio.getTime().getIdTime());
			if(campanhas.isEmpty()) return null;
			
			List<Campanha> camp = new ArrayList<Campanha>();
			for (CampanhaEntity campanhaEntity : campanhas) {
				camp.add( modelMapper.map(campanhaEntity, Campanha.class));	
			}
			
			return camp;
		} catch (FeignException fe) {
			//caso não tenha campanha ativa para o time retorna vazio
			log.error(fe.contentUTF8());
			if(fe.status() == 404) {
				log.info("Não dados de campanha ");
			}
			
			return null;
		}catch (Exception e) {
			log.error(e.getMessage());
            throw ApiException
            .builder()
            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .code(ApiException.GENERAL_ERROR)
            .message("Erro ao buscar campanhas nao cadastradas")
            .reason("Erro ao buscar dados")
            .build();
		}
	}
	
	/**
	 * identifica socio ativo
	 * @param socio
	 * @return
	 * @throws ApiException
	 */
	private Boolean socioAtivo(SocioTorcedor socio) throws ApiException {
		
		try {
			 SocioTorcedorEntity socioAtivo = scRepository.findByEmailAndAtivo(socio.getEmail(), true).orElse(null);
			 if(socioAtivo != null){
				return true; 
			 }
			  return false;

		} catch (Exception e) {
			log.error(e.getMessage());
            throw ApiException
            .builder()
            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .code(ApiException.GENERAL_ERROR)
            .message("Erro ao validar socio ")
            .reason("Erro ao validadar socio ativo")
            .build();
		}
		
	}
	
	/**
	 * valida os dados basicos, e em seguida envia para fila para atualizar o socio, 
	 */
	@Override
	public DadosResponse atualizarSocio(SocioTorcedor socio) throws ApiException {
	 try {
		 DadosResponse response  = new DadosResponse();
				 
		 SocioTorcedorEntity updtsocio = new SocioTorcedorEntity();
		 
		 if(Objects.isNull(socio.getIdSocio())) {
			 updtsocio = scRepository.findByEmailAndAtivo(socio.getEmail(), true).orElse(null);
		 }else {
			 updtsocio = scRepository.findByIdSocioAndAtivo(socio.getIdSocio(), true).orElse(null);
		 }
		 
		 if(updtsocio == null) {
	            throw ApiException
	            .builder()
	            .statusCode(HttpStatus.BAD_REQUEST.value())
	            .code(ApiException.VALIDATION_ERROR)
	            .message("Nao existe socio ativo")
	            .reason("Erro ao cadastrar socio")
	            .build();
		 }
		 
		 RequestAsyc asyc = new RequestAsyc();
		 asyc.setSocio(socio);
		 asyc.setTipoProcessamento(2);
		 queue.send(asyc);
		 		 
		response.setCode(202);
		response.setMessage("Dado enviado para processamento com Sucesso ");
		return response;
		
		}catch(ApiException ae) {
            throw ApiException
            .builder()
            .statusCode(ae.getStatusCode())
            .code(ae.getCode())
            .message(ae.getMessage())
            .reason(ae.getReason())
            .build();
		} catch (Exception e) {
			log.error(e.getMessage());
            throw ApiException
            .builder()
            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .code(ApiException.GENERAL_ERROR)
            .message("Erro ao buscar dados")
            .reason("Erro ao buscar dados")
            .build();
		}
	}
	
	/**
	 * valida existencia do socio e apaga da base
	 */
	@Override
	public DadosResponse apagarSocio(Long id) throws ApiException {
	 try {
		 DadosResponse response  = new DadosResponse();
				 
		 SocioTorcedorEntity updtsocio = scRepository.findById(id).orElse(null);
		  
		 if(updtsocio == null) {
	            throw ApiException
	            .builder()
	            .statusCode(HttpStatus.BAD_REQUEST.value())
	            .code(ApiException.VALIDATION_ERROR)
	            .message("Nao existe socio ativo")
	            .reason("Erro ao cadastrar socio")
	            .build();
		 }
		 
		 scRepository.deleteById(id);
		 		 
		response.setCode(202);
		response.setMessage("Socio apagado com Sucesso ");
		return response;
		
		}catch(ApiException ae) {
            throw ApiException
            .builder()
            .statusCode(ae.getStatusCode())
            .code(ae.getCode())
            .message(ae.getMessage())
            .reason(ae.getReason())
            .build();
		} catch (Exception e) {
			log.error(e.getMessage());
            throw ApiException
            .builder()
            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .code(ApiException.GENERAL_ERROR)
            .message("Erro ao buscar dados")
            .reason("Erro ao buscar dados")
            .build();
		}
	}
}
