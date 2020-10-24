package br.com.sociotorcedor.application.api;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.sociotorcedor.application.exception.ApiException;
import br.com.sociotorcedor.application.model.DadosResponse;
import br.com.sociotorcedor.application.model.ErrorResponse;
import br.com.sociotorcedor.application.model.SocioTorcedor;
import br.com.sociotorcedor.application.service.ApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@Api(value = "API Socio Torcedor")
@RestController
@RequiredArgsConstructor
@ApiResponses(value = { 
		@ApiResponse(code = 200, message = "Api executada com sucesso"),
		@ApiResponse(code = 202, message = "Registro processado com sucesso"),
		@ApiResponse(code = 401, message = "Você não está autorizado a visualizar o recurso"),
	    @ApiResponse(code = 403, message = "É proibido acessar o recurso que você estava tentando acessar"),
	    @ApiResponse(code = 404, message = "O recurso que você estava tentando acessar não foi encontrado"),
	    @ApiResponse(code = 500, message = "Erro interno")})
@RequestMapping("/socio")
public class apiController {
	
	private final ApiService service;

	@ApiOperation(value = "Metodo para salvar Socio")
	@RequestMapping(value = "",
    produces = { "application/json" },
    method = RequestMethod.POST)
	  public ResponseEntity<?> saveSocio(@RequestBody SocioTorcedor socio) {
	    try {

	    	DadosResponse result = service.salveSocio(socio);
	    	return ResponseEntity.status(result.getCode()).body(result);
	      
	    }catch (ApiException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(ErrorResponse
                            .builder()
                            .code(e.getCode())
                            .description(e.getReason())
                            .message(e.getMessage())
                            .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ErrorResponse
                            .builder()
                            .code("Internal Error")
                            .description(e.getMessage())
                            .message(e.getMessage())
                            .build());
        } 
	 }
	
	@ApiOperation(value = "Metodo para atualizar socios")
	@RequestMapping(value = "",
    produces = { "application/json" },
    method = RequestMethod.PUT)
	  public ResponseEntity<?> atualizarSocio(@RequestBody SocioTorcedor socio) {
	    try {

	    	DadosResponse result = service.atualizarSocio(socio);
	    	return ResponseEntity.status(result.getCode()).body(result);
	      
	    }catch (ApiException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(ErrorResponse
                            .builder()
                            .code(e.getCode())
                            .description(e.getReason())
                            .message(e.getMessage())
                            .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ErrorResponse
                            .builder()
                            .code("Internal Error")
                            .description(e.getMessage())
                            .message(e.getMessage())
                            .build());
        } 	
	 }

	@ApiOperation(value = "Metodo para buscar dados do socio")
	@RequestMapping(value = "",
    produces = { "application/json" },
    method = RequestMethod.GET)
	  public ResponseEntity<?> getSocio(@RequestParam(name = "email", required = true) String email ) {
	    try {

	    	return ResponseEntity.status(HttpStatus.OK).body( service.getSocio(email) );
	      
	    }catch (ApiException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(ErrorResponse
                            .builder()
                            .code(e.getCode())
                            .description(e.getReason())
                            .message(e.getMessage())
                            .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ErrorResponse
                            .builder()
                            .code("Internal Error")
                            .description(e.getMessage())
                            .message(e.getMessage())
                            .build());
        } 
	 }

	@ApiOperation(value = "Metodo para apagar socio")
	@RequestMapping(value = "/{id}",
    produces = { "application/json" },
    method = RequestMethod.DELETE)
	  public ResponseEntity<?> apagarSocio(@PathVariable(name = "id",required = true) Long id ) {
	    try {

	    	DadosResponse result = service.apagarSocio(id);
	    	return ResponseEntity.status(result.getCode()).body(result);
	      
	    }catch (ApiException e) {
            return ResponseEntity
                    .status(e.getStatusCode())
                    .body(ErrorResponse
                            .builder()
                            .code(e.getCode())
                            .description(e.getReason())
                            .message(e.getMessage())
                            .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ErrorResponse
                            .builder()
                            .code("Internal Error")
                            .description(e.getMessage())
                            .message(e.getMessage())
                            .build());
        } 	
	 }
	
}
