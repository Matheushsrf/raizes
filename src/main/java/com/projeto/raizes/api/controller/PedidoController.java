package com.projeto.raizes.api.controller;

import com.projeto.raizes.domain.enums.CanalPedido;
import com.projeto.raizes.domain.enums.StatusPedido;
import com.projeto.raizes.domain.model.*;
import com.projeto.raizes.infrastructure.persistence.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final EstoqueRepository estoqueRepository;

    public PedidoController(PedidoRepository pedidoRepository,
                             ClienteRepository clienteRepository,
                             ProdutoRepository produtoRepository,
                             EstoqueRepository estoqueRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
        this.estoqueRepository = estoqueRepository;
    }

    @PostMapping
    public ResponseEntity<?> criarPedido(@RequestBody Map<String, Object> body) {

        Long clienteId = Long.valueOf(body.get("clienteId").toString());
        Long unidadeId = Long.valueOf(body.get("unidadeId").toString());
        String canal = body.get("canalPedido").toString();
        String formaPagamento = body.get("formaPagamento").toString();

        Optional<Cliente> clienteOpt = clienteRepository.findById(clienteId);
        if (clienteOpt.isEmpty()) {
            return ResponseEntity.status(404)
                .body(Map.of("error", "CLIENTE_NAO_ENCONTRADO",
                             "message", "Cliente não encontrado."));
        }

        List<Map<String, Object>> itensBody = (List<Map<String, Object>>) body.get("itens");
        List<ItemPedido> itens = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (Map<String, Object> itemMap : itensBody) {
            Long produtoId = Long.valueOf(itemMap.get("produtoId").toString());
            Integer quantidade = Integer.valueOf(itemMap.get("quantidade").toString());

            Optional<Produto> produtoOpt = produtoRepository.findById(produtoId);
            if (produtoOpt.isEmpty()) {
                return ResponseEntity.status(404)
                    .body(Map.of("error", "PRODUTO_NAO_ENCONTRADO",
                                 "message", "Produto " + produtoId + " não encontrado."));
            }

            Optional<Estoque> estoqueOpt = estoqueRepository
                .findByProdutoIdAndUnidadeId(produtoId, unidadeId);

            if (estoqueOpt.isEmpty() || estoqueOpt.get().getQuantidade() < quantidade) {
                return ResponseEntity.status(409)
                    .body(Map.of("error", "ESTOQUE_INSUFICIENTE",
                                 "message", "Estoque insuficiente para o produto " + produtoId));
            }

            Produto produto = produtoOpt.get();
            BigDecimal subtotal = produto.getPrecoBase()
                .multiply(BigDecimal.valueOf(quantidade));

            ItemPedido item = new ItemPedido();
            item.setProduto(produto);
            item.setQuantidade(quantidade);
            item.setPrecoUnitario(produto.getPrecoBase());
            item.setSubtotal(subtotal);

            itens.add(item);
            total = total.add(subtotal);
        }

        Pedido pedido = new Pedido();
        pedido.setCliente(clienteOpt.get());
        pedido.setCanalPedido(CanalPedido.valueOf(canal));
        pedido.setFormaPagamento(formaPagamento);
        pedido.setTotal(total);
        pedido.setStatusPagamento("PENDENTE");

        for (ItemPedido item : itens) {
            item.setPedido(pedido);
        }
        pedido.setItens(itens);

        Pedido salvo = pedidoRepository.save(pedido);

        return ResponseEntity.status(201)
            .body(Map.of(
                "pedidoId", salvo.getId(),
                "status", salvo.getStatus(),
                "total", salvo.getTotal(),
                "canalPedido", salvo.getCanalPedido(),
                "statusPagamento", salvo.getStatusPagamento()
            ));
    }

    @GetMapping
    public ResponseEntity<?> listarPedidos(
            @RequestParam(required = false) String canalPedido) {

        List<Pedido> pedidos;

        if (canalPedido != null) {
            pedidos = pedidoRepository.findByCanalPedido(
                CanalPedido.valueOf(canalPedido));
        } else {
            pedidos = pedidoRepository.findAll();
        }

        return ResponseEntity.ok(Map.of("total", pedidos.size(),
                                        "pedidos", pedidos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPedido(@PathVariable Long id) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(id);

        if (pedidoOpt.isEmpty()) {
            return ResponseEntity.status(404)
                .body(Map.of("error", "PEDIDO_NAO_ENCONTRADO",
                             "message", "Pedido não encontrado."));
        }

        return ResponseEntity.ok(pedidoOpt.get());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> atualizarStatus(@PathVariable Long id,
                                              @RequestBody Map<String, String> body) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(id);

        if (pedidoOpt.isEmpty()) {
            return ResponseEntity.status(404)
                .body(Map.of("error", "PEDIDO_NAO_ENCONTRADO",
                             "message", "Pedido não encontrado."));
        }

        Pedido pedido = pedidoOpt.get();
        pedido.setStatus(StatusPedido.valueOf(body.get("status")));
        pedidoRepository.save(pedido);

        return ResponseEntity.ok(Map.of(
            "pedidoId", pedido.getId(),
            "novoStatus", pedido.getStatus()
        ));
    }
}