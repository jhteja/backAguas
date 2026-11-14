package com.example.acueducto;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import com.example.acueducto.model.enums.EstadoFactura;
import com.example.acueducto.repository.FacturaRepository;
import com.example.acueducto.repository.PagoRepository;
import com.example.acueducto.repository.PredioRepository;
import com.example.acueducto.repository.UsuarioRepository;

@SpringBootTest
@AutoConfigureMockMvc
class AcueductoApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PredioRepository predioRepository;

	@Autowired
	private FacturaRepository facturaRepository;

	@Autowired
	private PagoRepository pagoRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void openApiDocumentsApiEndpoints() throws Exception {
		mockMvc.perform(get("/v3/api-docs"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.openapi").exists())
				.andExpect(jsonPath("$.paths['/api/usuarios']").exists())
				.andExpect(jsonPath("$.paths['/api/predios']").exists())
				.andExpect(jsonPath("$.paths['/api/facturas']").exists());
	}

	@Test
	void seedCreatesUsersPropertiesAndInvoices() {
		var suscriptor = usuarioRepository.findByDocumento("1020304050")
				.orElseThrow(() -> new AssertionError("No se encontró el usuario de prueba"));
		var predios = predioRepository.findByUsuarioId(suscriptor.getId());

		assertFalse(predios.isEmpty(), "No se sembraron predios para el usuario de prueba");

		var facturas = predios.stream()
				.flatMap(predio -> facturaRepository.findByPredioId(predio.getId()).stream())
				.toList();

		assertFalse(facturas.isEmpty(), "No se sembraron facturas para los predios de prueba");
		assertTrue(facturas.stream().anyMatch(factura -> factura.getEstado() == EstadoFactura.PAGADA),
				"No se encontró una factura de prueba pagada");
		var facturasPagadas = facturas.stream()
				.filter(factura -> factura.getEstado() == EstadoFactura.PAGADA)
				.toList();
		assertTrue(facturasPagadas.stream().anyMatch(factura -> pagoRepository.existsByFacturaId(factura.getId())),
				"No se encontró un pago asociado a una factura pagada");

		System.out.printf("Seed verificado en Neon: usuarios=%d, predios_usuario_prueba=%d, facturas_usuario_prueba=%d, pagos_facturas_prueba=%d%n",
				usuarioRepository.count(), predios.size(), facturas.size(), pagoRepository.count());
	}
}
