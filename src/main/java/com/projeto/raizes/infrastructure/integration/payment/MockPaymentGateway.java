package com.projeto.raizes.infrastructure.integration.payment;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Service
public class MockPaymentGateway {

    public Map<String, Object> processarPagamento(
            Long pedidoId,
            BigDecimal valor,
            String formaPagamento) {

        // Simula aprovação para valores até R$ 1000
        // Recusa para valores acima de R$ 1000 (simula limite)
        boolean aprovado = valor.compareTo(new BigDecimal("1000")) <= 0;

        String transacaoId = UUID.randomUUID().toString();
        String status = aprovado ? "APROVADO" : "RECUSADO";
        String mensagem = aprovado
            ? "Pagamento aprovado com sucesso."
            : "Pagamento recusado: valor acima do limite permitido.";

        return Map.of(
            "transacaoId", transacaoId,
            "pedidoId", pedidoId,
            "valor", valor,
            "formaPagamento", formaPagamento,
            "status", status,
            "mensagem", mensagem
        );
    }
}