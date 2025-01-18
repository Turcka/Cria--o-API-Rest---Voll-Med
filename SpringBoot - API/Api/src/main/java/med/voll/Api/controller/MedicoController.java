package med.voll.Api.controller;

// import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.Api.medico.DadosAtualizacaoMedico;
import med.voll.Api.medico.DadosCadastroMedico;
import med.voll.Api.medico.DadosDetalhamentoMedico;
import med.voll.Api.medico.DadosListagemMedicos;
import med.voll.Api.medico.MedicoRepository;
import med.voll.Api.medico.Medico;

@RestController
@RequestMapping("medicos")
public class MedicoController {
    // @PostMapping("path")
    // public String postMethodName(@RequestBody String entity) {
    //     
        
    //     return entity;
    // }
    
    @Autowired
    private MedicoRepository repository;

    @SuppressWarnings("rawtypes")
    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroMedico dados, UriComponentsBuilder uribuilder) {
        var medico = new Medico(dados);
        repository.save(medico);
        var uri = uribuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico));
    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemMedicos>> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {
        var page =  repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedicos::new);
        return ResponseEntity.ok(page);
    }

    @SuppressWarnings("rawtypes")
    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoMedico dados){
        var medico = repository.getReferenceById(dados.id()); 
        medico.atualizarInformações(dados);
        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }

    // Create a method to delete 
    @SuppressWarnings("rawtypes")
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id) {
        var medico = repository.getReferenceById(id); 
        medico.excluir(); 
        return ResponseEntity.noContent().build();
    }

    @SuppressWarnings("rawtypes")
    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id) {
        var medico = repository.getReferenceById(id); 
        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }

}
