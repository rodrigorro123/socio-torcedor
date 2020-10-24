package br.com.sociotorcedor.application.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * classe de controle para envio dos dados para fila
 * @author rodrigo
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestAsyc implements Serializable {

	private static final long serialVersionUID = 7849734960627943440L;

	private SocioTorcedor socio;
	private int tipoProcessamento;
	private int qtdeTentativa;
    
}
