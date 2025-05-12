package com.sales.backend.controllers;

import com.sales.backend.dto.ProdutoDTO;
import com.sales.backend.models.Produto;
import com.sales.backend.services.ProdutoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('admin', 'funcionario')")
    public ResponseEntity<List<ProdutoDTO>> listarTodos() {
        List<Produto> produtos = produtoService.listarTodos();
        return ResponseEntity.ok(produtoService.converterParaDTO(produtos));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin', 'funcionario')")
    public ResponseEntity<ProdutoDTO> buscarPorId(@PathVariable Integer id) {
        Produto produto = produtoService.buscarPorId(id);
        return ResponseEntity.ok(produtoService.converterParaDTO(produto));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('funcionario')")
    public ResponseEntity<ProdutoDTO> cadastrarProduto(@Valid @RequestBody ProdutoDTO produtoDTO) {
        Produto novoProduto = produtoService.salvar(produtoDTO);
        return new ResponseEntity<>(produtoService.converterParaDTO(novoProduto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('funcionario')")
    public ResponseEntity<ProdutoDTO> atualizarProduto(@PathVariable Integer id, @Valid @RequestBody ProdutoDTO produtoDTO) {
        Produto produtoAtualizado = produtoService.atualizar(id, produtoDTO);
        return ResponseEntity.ok(produtoService.converterParaDTO(produtoAtualizado));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('funcionario')")
    public ResponseEntity<Void> excluirProduto(@PathVariable Integer id) {
        produtoService.excluir(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}