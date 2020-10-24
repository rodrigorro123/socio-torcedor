package br.com.sociotorcedor.application.service.queue;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import br.com.sociotorcedor.application.exception.ApiException;
import br.com.sociotorcedor.application.model.RequestAsyc;

@Component
public class SocioQueueSender {
	
    @Autowired
    private RabbitTemplate rabbitTemplate;

	@Value("${integration.nomeFila}")
	private String nomeFila;
	@Value("${integration.nomeFilaFalha}")
	private String nomeFilaFalha;
	
	
    public void send(RequestAsyc body) throws Exception{
    	try {
    		
    		rabbitTemplate.convertAndSend(nomeFila, body);
    		 
		} catch (Exception e) {
            throw ApiException.builder()
            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .code(ApiException.GENERAL_ERROR)
            .message(e.getMessage())
            .reason(e.getCause().getLocalizedMessage())
            .build();
		}
    }
  
    public void sendFail(RequestAsyc body) throws Exception{
    	try {
    		
    		rabbitTemplate.convertAndSend(nomeFilaFalha, body ,new MessagePostProcessor() {
    	        @Override
    	        public Message postProcessMessage(Message message) throws AmqpException {
    	            message.getMessageProperties().setHeader("x-delay", 15000);
    	            return message;
    	        }
    	    });
    		 
		} catch (Exception e) {
            throw ApiException.builder()
            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .code(ApiException.GENERAL_ERROR)
            .message(e.getMessage())
            .reason(e.getCause().getLocalizedMessage())
            .build();
		}
    }

}