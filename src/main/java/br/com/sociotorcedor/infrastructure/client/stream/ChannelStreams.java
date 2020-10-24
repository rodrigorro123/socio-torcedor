package br.com.sociotorcedor.infrastructure.client.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
/**
 * interface que identifica o mapeamento das filas
 * @author rodrigo
 *
 */
public interface ChannelStreams {

    final String SOCIO_OUTPUT = "socioOutput";
    final String SOCIO_INPUT = "socioInput";
    
    final String SOCIO_FAIL_OUTPUT = "socioReprocessarOutput";
    final String SOCIO_FAIL_INPUT = "socioReprocessarInput";

    
    @Output(ChannelStreams.SOCIO_FAIL_OUTPUT)
    MessageChannel socioReprocessarOutput();

    @Input(ChannelStreams.SOCIO_FAIL_INPUT)
    SubscribableChannel socioReprocessarInput();


    @Output(ChannelStreams.SOCIO_OUTPUT)
    MessageChannel socioOutput();

    @Input(ChannelStreams.SOCIO_INPUT)
    SubscribableChannel socioInput();
    
    

    
}
