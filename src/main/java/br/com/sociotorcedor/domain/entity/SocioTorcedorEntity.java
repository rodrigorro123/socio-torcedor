package br.com.sociotorcedor.domain.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
/**
 * classe para mapeamento do banco
 * @author rodrigo
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "socio_torcedor")
public class SocioTorcedorEntity {
	
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id_socio",unique = true)
    private Long idSocio;
    
    @NonNull
    @Column(name = "nome_completo", length = 250)
    private String nomeCompleto;
    
    @NonNull
    @Column(name = "email", length = 150)
    private String email;
   
    @OneToOne 
    @JoinColumn(name = "id_time", referencedColumnName = "id_time")
    private TimeEntity time;
    
    
    @NonNull
    @Column(name = "dt_nascimento")
    private LocalDate dtNascimento;
    
    @Column(name = "ativo")
    private Boolean ativo;
    
//    @ManyToMany (fetch = FetchType.EAGER)
    @OneToMany(fetch = FetchType.EAGER)
     List<CampanhaEntity> campanhas;
    


}
