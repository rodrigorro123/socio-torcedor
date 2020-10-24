package br.com.sociotorcedor.application.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor @NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class SocioTorcedorResponse {

	
    private Long idSocio;
    
    @NonNull
    private String nomeCompleto;
    
    @NonNull
    private String email;

    private Time time;
    
    @NonNull
    private LocalDate dtNascimento;
    
    private Boolean ativo;

    private List<Campanha> campanhas;
    private List<Campanha> campanhasDisponiveisNaoCadastradas;
}
