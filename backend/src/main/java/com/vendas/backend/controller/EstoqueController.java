package com.vendas.backend.controller;

import com.vendas.backend.model.Estoque;
import com.vendas.backend.service.EstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estoque")
public class EstoqueController {

    @Autowired
    private EstoqueService estoqueService;

    @GetMapping
    public ResponseEntity<List<Estoque>> listarTodos() {
        List<Estoque> estoques = estoqueService.listarTodos();
        return ResponseEntity.ok(estoques);
    }

    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<Estoque> buscarPorProdutoId(@PathVariable Integer produtoId) {
        Estoque estoque = estoqueService.buscarPorProdutoId(produtoId);
        return estoque != null
                ? ResponseEntity.ok(estoque)
                : ResponseEntity.notFound().build();
    }

    @PutMapping("/produto/{produtoId}/quantidade/{quantidade}")
    public ResponseEntity<Void> atualizarQuantidade(
            @PathVariable Integer produtoId,
            @PathVariable Integer quantidade) {
        
        boolean atualizado = estoqueService.atualizarQuantidade(produtoId, quantidade);
        return atualizado
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }
}