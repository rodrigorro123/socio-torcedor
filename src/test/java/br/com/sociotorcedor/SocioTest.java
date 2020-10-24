package br.com.sociotorcedor;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.sociotorcedor.application.exception.ApiException;
import br.com.sociotorcedor.domain.entity.SocioTorcedorEntity;
import br.com.sociotorcedor.domain.repository.SocioTorcedorRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SocioTest {
    
    @Autowired
    private SocioTorcedorRepository repository;
    
  
    @Test
    public void salvaSocio() throws ApiException {
    	SocioTorcedorEntity socioSTE = new SocioTorcedorEntity(null, "rodrigo", "email", null, LocalDate.now().minusYears(20),true,null);
    	
    	SocioTorcedorEntity ret = repository.save(socioSTE);
    	
    	   assertThat(ret.getEmail())
    	      .isEqualTo("email");
    }
    
    @Test
    public void buscaSocio() throws ApiException {
    	
    	SocioTorcedorEntity socioSTE = new SocioTorcedorEntity(null, "rodrigo", "email", null, LocalDate.now().minusYears(20),true,null);
    	
    	SocioTorcedorEntity ret = repository.save(socioSTE);
    	
    	ret = repository.findByIdSocioAndAtivo(2L, true).orElse(null);
    	
    	   assertThat(ret.getEmail())
    	      .isEqualTo("email");
    }
    
}
 