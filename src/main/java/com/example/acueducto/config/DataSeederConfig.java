package com.example.acueducto.config;

import com.example.acueducto.model.entity.Factura;
import com.example.acueducto.model.entity.Pago;
import com.example.acueducto.model.entity.Predio;
import com.example.acueducto.model.entity.Usuario;
import com.example.acueducto.model.enums.EstadoFactura;
import com.example.acueducto.repository.FacturaRepository;
import com.example.acueducto.repository.PagoRepository;
import com.example.acueducto.repository.PredioRepository;
import com.example.acueducto.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class DataSeederConfig {

    @Bean
    public CommandLineRunner sembrarDatosIniciales(
            UsuarioRepository usuarioRepository,
            PredioRepository predioRepository,
            FacturaRepository facturaRepository,
            PagoRepository pagoRepository) {

        return args -> {
            System.out.println("\n🌱 Verificando datos de prueba...");

            if (!usuarioRepository.existsByDocumento("111111111")) {
                usuarioRepository.save(new Usuario("111111111", "Carlos Pérez (Admin)", "admin123", "ADMIN"));
            }

            Usuario suscriptor = usuarioRepository.findByDocumento("1020304050")
                    .orElseGet(() -> usuarioRepository.save(
                            new Usuario("1020304050", "María Rodríguez", "user123", "USER")));

            List<Predio> predios = predioRepository.findByUsuarioId(suscriptor.getId());
            if (predios.isEmpty()) {
                predios = predioRepository.saveAll(List.of(
                        new Predio(suscriptor, "Vereda El Rosario", "Calle Principal", "Lote 12", "Finca La Esperanza", "RESIDENCIAL", 1500.0, 25000.0),
                        new Predio(suscriptor, "Vereda El Rosario", "Sector Bajo", "Lote 45", "Casa Esquina", "COMERCIAL", 2000.0, 45000.0),
                        new Predio(suscriptor, "Vereda El Rosario", "Sector Alto", "Lote 03", "Finca El Paraíso", "RESIDENCIAL", 1500.0, 28000.0),
                        new Predio(suscriptor, "Vereda El Centro", "Carrera 4", "Lote 18", "Local Comercial", "COMERCIAL", 2000.0, 50000.0),
                        new Predio(suscriptor, "Vereda La Selva", "Vía Principal", "Lote 07", "Finca San José", "AGRICOLA", 1200.0, 30000.0),
                        new Predio(suscriptor, "Vereda El Rosario", "Calle 2", "Lote 22", "Casa Villa Luz", "RESIDENCIAL", 1500.0, 25000.0)
                ));
            }

            if (!predios.isEmpty()) {
                crearFacturaSiNoExiste(facturaRepository, pagoRepository, predios.get(0), LocalDate.now().plusDays(15),
                        25000.0, EstadoFactura.PENDIENTE, null);
            }
            if (predios.size() > 1) {
                crearFacturaSiNoExiste(facturaRepository, pagoRepository, predios.get(1), LocalDate.now().minusDays(5),
                        45000.0, EstadoFactura.PAGADA, LocalDateTime.now().minusDays(2));
            }

            System.out.println("✅ Verificación y siembra de datos completada.\n");
        };
    }

    private void crearFacturaSiNoExiste(
            FacturaRepository facturaRepository,
            PagoRepository pagoRepository,
            Predio predio,
            LocalDate fechaVencimiento,
            Double valor,
            EstadoFactura estado,
            LocalDateTime fechaPago) {
        String periodo = "2026-03";
        Factura factura = facturaRepository.findByPredioIdAndPeriodo(predio.getId(), periodo)
                .orElseGet(() -> facturaRepository.save(
                        new Factura(predio, periodo, fechaVencimiento, valor, estado, fechaPago)));

        if (estado == EstadoFactura.PAGADA) {
            if (factura.getEstado() != EstadoFactura.PAGADA) {
                factura.setEstado(EstadoFactura.PAGADA);
                factura.setFechaPago(fechaPago);
                factura = facturaRepository.save(factura);
            }
            if (!pagoRepository.existsByFacturaId(factura.getId())) {
                pagoRepository.save(new Pago(factura, "EFECTIVO", factura.getValor()));
            }
        }
    }
}