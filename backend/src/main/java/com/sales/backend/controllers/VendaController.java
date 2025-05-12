package com.sales.backend.controllers;

import com.sales.backend.dto.VendaDTO;
import com.sales.backend.models.Venda;
import com.sales.backend.services.VendaService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/vendas")
public class VendaController {

    @Autowired
    private VendaService vendaService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('admin', 'funcionario')")
    public ResponseEntity<List<VendaDTO>> listarTodos() {
        List<Venda> vendas = vendaService.listarTodos();
        return ResponseEntity.ok(vendaService.converterParaDTO(vendas));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin', 'funcionario', 'cliente')")
    public ResponseEntity<VendaDTO> buscarPorId(@PathVariable Integer id) {
        Venda venda = vendaService.buscarPorId(id);
        return ResponseEntity.ok(vendaService.converterParaDTO(venda));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('cliente')")
    public ResponseEntity<VendaDTO> realizarVenda(@Valid @RequestBody VendaDTO vendaDTO) {
        Venda novaVenda = vendaService.salvar(vendaDTO);
        return new ResponseEntity<>(vendaService.converterParaDTO(novaVenda), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<Void> excluirVenda(@PathVariable Integer id) {
        vendaService.excluir(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}