package br.com.sociotorcedor.application.service.queue;

import java.util.Map;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import br.com.sociotorcedor.application.exception.ApiException;
import br.com.sociotorcedor.application.model.RequestAsyc;
import br.com.sociotorcedor.infrastructure.client.stream.ChannelStreams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class SocioConsumer {

	private final  SocioQueueSender sender;
	private final AsynchronosService asyn;
	
	@StreamListener(target = ChannelStreams.SOCIO_INPUT)
	 public void receive(@Payload RequestAsyc body) throws Exception {
    	try {
    		log.info("inicio fila");
    		
    		if(body.getTipoProcessamento() == 1) {
    			asyn.salveSocioAsyc(body.getSocio());
    		}else if(body.getTipoProcessamento() == 2) {
    			asyn.atualizarSocioAsyc(body.getSocio());
    		}

		}catch(ApiException ae){
			
  			body.setQtdeTentativa(body.getQtdeTentativa() == 0 ? 1 : body.getQtdeTentativa() + 1 );
  			if(body.getQtdeTentativa() < 6) {
  				sender.sendFail(body);
  				log.info("envio fila de falha");
  			}
  			
		}catch (Exception ex) {
			log.info("erro : " + ex.getMessage() );
			ex.printStackTrace();
		}
    }
	
	@StreamListener(target = ChannelStreams.SOCIO_FAIL_INPUT)
    public void receivefail(@Payload RequestAsyc body, @Header(name = "x-delay", required = false) Map<?,?> death ) throws Exception {
    	try {
    		log.info("inicio fila falha");
   	
    		if(body.getTipoProcessamento() == 1) {
    			asyn.salveSocioAsyc(body.getSocio());
    		}else if(body.getTipoProcessamento() == 2) {
    			asyn.atualizarSocioAsyc(body.getSocio());
    		}

		}catch(ApiException ae){
  			body.setQtdeTentativa(body.getQtdeTentativa() == 0 ? 1 : body.getQtdeTentativa() + 1 );
  			if(body.getQtdeTentativa() < 6) {
  				Thread.sleep(1000);
  				sender.sendFail(body);
  				log.info("envio fila de falha");
  			}else {
  				//TODO envio email
  				log.info("Envio mail responsavel api");
  			}
		}catch (Exception ex) {
			log.info("erro : " + ex.getMessage() );
			ex.printStackTrace();
		}
    }	
}
 
