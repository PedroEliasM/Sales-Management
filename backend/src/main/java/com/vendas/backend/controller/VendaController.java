package com.vendas.backend.controller;

import com.vendas.backend.dto.VendaDTO;
import com.vendas.backend.model.Venda;
import com.vendas.backend.service.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendas")
public class VendaController {

    @Autowired
    private VendaService vendaService;

    @GetMapping
    public ResponseEntity<List<Venda>> listarTodas() {
        List<Venda> vendas = vendaService.listarTodas();
        return ResponseEntity.ok(vendas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venda> buscarPorId(@PathVariable Integer id) {
        Venda venda = vendaService.buscarPorId(id);
        return venda != null
                ? ResponseEntity.ok(venda)
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<Venda>> listarPorCliente(@PathVariable Integer idCliente) {
        List<Venda> vendas = vendaService.listarPorCliente(idCliente);
        return ResponseEntity.ok(vendas);
    }

    @PostMapping
    public ResponseEntity<?> realizarVenda(@RequestBody VendaDTO vendaDTO) {
        try {
            Venda venda = vendaService.realizarVenda(vendaDTO);
            return venda != null
                    ? ResponseEntity.status(HttpStatus.CREATED).body(venda)
                    : ResponseEntity.badRequest().body("Não foi possível realizar a venda");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}