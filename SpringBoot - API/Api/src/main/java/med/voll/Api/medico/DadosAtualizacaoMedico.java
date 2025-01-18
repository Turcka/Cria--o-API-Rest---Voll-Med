package med.voll.Api.medico;

import jakarta.validation.constraints.NotNull;
import med.voll.Api.endereco.DadosEndereco;

public record DadosAtualizacaoMedico(
    @NotNull
    Long id, 
    String nome, 
    String telefone, 
    DadosEndereco endereco) {

}
