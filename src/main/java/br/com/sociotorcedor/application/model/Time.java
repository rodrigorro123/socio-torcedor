package br.com.sociotorcedor.application.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class Time {
	@JsonIgnore
    private Long idTime;
    private String nomeTime;
    
}
