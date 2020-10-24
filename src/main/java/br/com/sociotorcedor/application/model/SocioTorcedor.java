package br.com.sociotorcedor.application.model;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor @NoArgsConstructor
@Builder
public class SocioTorcedor implements Serializable {
 
	private static final long serialVersionUID = 7922895965953401494L;

	private Long idSocio;
    
    @NonNull
    private String nomeCompleto;
    
    @NonNull
    private String email;

    @NonNull
    private Long idTime;
    
    @NonNull
    private LocalDate dtNascimento;
    
    private Boolean ativo;

    private Long idCampanha;
    
}
