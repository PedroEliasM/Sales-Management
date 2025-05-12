package com.sales.backend.controllers;

import com.sales.backend.dto.EstoqueDTO;
import com.sales.backend.models.Estoque;
import com.sales.backend.services.EstoqueService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/estoque")
public class EstoqueController {

    @Autowired
    private EstoqueService estoqueService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('admin', 'funcionario')")
    public ResponseEntity<List<EstoqueDTO>> listarTodos() {
        List<Estoque> estoques = estoqueService.listarTodos();
        return ResponseEntity.ok(estoques.stream().map(estoqueService::converterParaDTO).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin', 'funcionario')")
    public ResponseEntity<EstoqueDTO> buscarPorId(@PathVariable Integer id) {
        Estoque estoque = estoqueService.buscarPorId(id);
        return ResponseEntity.ok(estoqueService.converterParaDTO(estoque));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('funcionario')")
    public ResponseEntity<EstoqueDTO> adicionarEstoque(@Valid @RequestBody EstoqueDTO estoqueDTO) {
        Estoque novoEstoque = estoqueService.salvar(estoqueDTO);
        return new ResponseEntity<>(estoqueService.converterParaDTO(novoEstoque), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('funcionario')")
    public ResponseEntity<EstoqueDTO> atualizarEstoque(@PathVariable Integer id, @Valid @RequestBody EstoqueDTO estoqueDTO) {
        Estoque estoqueAtualizado = estoqueService.atualizar(id, estoqueDTO);
        return ResponseEntity.ok(estoqueService.converterParaDTO(estoqueAtualizado));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('funcionario')")
    public ResponseEntity<Void> excluirEstoque(@PathVariable Integer id) {
        estoqueService.excluir(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}