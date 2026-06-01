package com.projeto.raizes.api.controller;

import com.projeto.raizes.domain.enums.StatusPedido;
import com.projeto.raizes.domain.model.Pedido;
import com.projeto.raizes.infrastructure.integration.payment.MockPaymentGateway;
import com.projeto.raizes.infrastructure.persistence.repository.PedidoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    private final PedidoRepository pedidoRepository;
    private final MockPaymentGateway mockPaymentGateway;

    public PagamentoController(PedidoRepository pedidoRepository,
                                MockPaymentGateway mockPaymentGateway) {
        this.pedidoRepository = pedidoRepository;
        this.mockPaymentGateway = mockPaymentGateway;
    }

    @PostMapping("/processar/{pedidoId}")
    public ResponseEntity<?> processarPagamento(@PathVariable Long pedidoId) {

        Optional<Pedido> pedidoOpt = pedidoRepository.findById(pedidoId);

        if (pedidoOpt.isEmpty()) {
            return ResponseEntity.status(404)
                .body(Map.of("error", "PEDIDO_NAO_ENCONTRADO",
                             "message", "Pedido não encontrado."));
        }

        Pedido pedido = pedidoOpt.get();

        if (!pedido.getStatus().equals(StatusPedido.AGUARDANDO_PAGAMENTO)) {
            return ResponseEntity.status(409)
                .body(Map.of("error", "STATUS_INVALIDO",
                             "message", "Pedido não está aguardando pagamento."));
        }

        Map<String, Object> resultado = mockPaymentGateway.processarPagamento(
            pedido.getId(),
            pedido.getTotal(),
            pedido.getFormaPagamento()
        );

        String statusPagamento = resultado.get("status").toString();

        if (statusPagamento.equals("APROVADO")) {
            pedido.setStatus(StatusPedido.PAGAMENTO_APROVADO);
            pedido.setStatusPagamento("APROVADO");
        } else {
            pedido.setStatus(StatusPedido.CANCELADO);
            pedido.setStatusPagamento("RECUSADO");
        }

        pedidoRepository.save(pedido);

        return ResponseEntity.ok(Map.of(
            "pedidoId", pedido.getId(),
            "statusPagamento", resultado.get("status"),
            "mensagem", resultado.get("mensagem"),
            "transacaoId", resultado.get("transacaoId"),
            "novoStatusPedido", pedido.getStatus()
        ));
    }
}