package com.vendas.backend.controller;

import com.vendas.backend.dto.ProdutoDTO;
import com.vendas.backend.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public ResponseEntity<List<ProdutoDTO>> listarTodos() {
        List<ProdutoDTO> produtos = produtoService.listarTodos();
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDTO> buscarPorId(@PathVariable Integer id) {
        ProdutoDTO produto = produtoService.buscarPorId(id);
        return produto != null 
                ? ResponseEntity.ok(produto) 
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/funcionario/{idFuncionario}")
    public ResponseEntity<List<ProdutoDTO>> listarPorFuncionario(@PathVariable Integer idFuncionario) {
        List<ProdutoDTO> produtos = produtoService.listarPorFuncionario(idFuncionario);
        return ResponseEntity.ok(produtos);
    }

    @PostMapping
    public ResponseEntity<ProdutoDTO> salvar(@RequestBody ProdutoDTO produtoDTO) {
        ProdutoDTO novoProduto = produtoService.salvar(produtoDTO);
        return novoProduto != null 
                ? ResponseEntity.status(HttpStatus.CREATED).body(novoProduto) 
                : ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoDTO> atualizar(@PathVariable Integer id, @RequestBody ProdutoDTO produtoDTO) {
        ProdutoDTO produtoAtualizado = produtoService.atualizar(id, produtoDTO);
        return produtoAtualizado != null 
                ? ResponseEntity.ok(produtoAtualizado) 
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        boolean excluido = produtoService.excluir(id);
        return excluido 
                ? ResponseEntity.noContent().build() 
                : ResponseEntity.notFound().build();
    }
}