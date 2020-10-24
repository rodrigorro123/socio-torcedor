package br.com.sociotorcedor.application.service;

import br.com.sociotorcedor.application.exception.ApiException;
import br.com.sociotorcedor.application.model.DadosResponse;
import br.com.sociotorcedor.application.model.SocioTorcedor;
import br.com.sociotorcedor.application.model.SocioTorcedorResponse;

public interface ApiService {

	public DadosResponse salveSocio(SocioTorcedor socio) throws ApiException;
	public SocioTorcedorResponse getSocio(String email) throws ApiException;
	public DadosResponse atualizarSocio(SocioTorcedor socio) throws ApiException;
	public DadosResponse apagarSocio(Long id) throws ApiException;
 

}
