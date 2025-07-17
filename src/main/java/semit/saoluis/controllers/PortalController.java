package semit.saoluis.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import semit.saoluis.model.Portal;
import semit.saoluis.repository.PortalRepository;
import semit.saoluis.responses.ApiResponse;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/portais")
@CrossOrigin(origins = "*")
public class PortalController {

    private final PortalRepository portalRepository;

    public PortalController(PortalRepository portalRepository) {
        this.portalRepository = portalRepository;
    }

    // 🔸 Criar
    @PostMapping("/criar")
    public ResponseEntity<?> criar(@RequestBody Portal portal) {
        Portal novo = portalRepository.save(portal);
        return ResponseEntity.status(201).body(new ApiResponse(201, "Portal criado com sucesso.", novo));
    }

    // 🔸 Listar todos
    @GetMapping("/listar")
    public ResponseEntity<?> listar() {
        List<Portal> lista = portalRepository.findAll();
        return ResponseEntity.ok(new ApiResponse(200, "Portais encontrados.", lista));
    }
    
 // 🔸 Listar apenas os ativos
    @GetMapping("/ativos")
    public ResponseEntity<?> listarAtivos() {
        List<Portal> lista = portalRepository.findByAtivoTrue();
        return ResponseEntity.ok(new ApiResponse(200, "Portais ativos encontrados.", lista));
    }

    // 🔸 Listar apenas os visíveis
    @GetMapping("/visiveis")
    public ResponseEntity<?> listarVisiveis() {
        List<Portal> lista = portalRepository.findByIsVisibleTrue();
        return ResponseEntity.ok(new ApiResponse(200, "Portais visíveis encontrados.", lista));
    }

    // 🔸 Buscar por ID
    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        Optional<Portal> portal = portalRepository.findById(id);
        return portal.map(p -> ResponseEntity.ok(new ApiResponse(200, "Portal encontrado.", p)))
                .orElse(ResponseEntity.status(404).body(new ApiResponse(404, "Portal não encontrado.", null)));
    }

    // 🔸 Atualizar dados
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Portal dados) {
        return portalRepository.findById(id).map(portal -> {
            portal.setNome(dados.getNome());
            portal.setLink(dados.getLink());
            portal.setIsVisible(dados.getIsVisible());
            portalRepository.save(portal);
            return ResponseEntity.ok(new ApiResponse(200, "Portal atualizado com sucesso.", portal));
        }).orElse(ResponseEntity.status(404).body(new ApiResponse(404, "Portal não encontrado.", null)));
    }

    // 🔸 Alterar visibilidade
    @PutMapping("/visibilidade/{id}")
    public ResponseEntity<?> alterarVisibilidade(@PathVariable Long id, @RequestParam("isVisible") boolean isVisible) {
        return portalRepository.findById(id).map(portal -> {
            portal.setIsVisible(isVisible);
            portalRepository.save(portal);
            String msg = isVisible ? "Portal ativado com sucesso." : "Portal desativado com sucesso.";
            return ResponseEntity.ok(new ApiResponse(200, msg, portal));
        }).orElse(ResponseEntity.status(404).body(new ApiResponse(404, "Portal não encontrado.", null)));
    }

    // 🔸 Desativar
    @PutMapping("/ativo/{id}")
    public ResponseEntity<?> alterarStatusAtivo(@PathVariable Long id, @RequestParam("ativo") boolean ativo) {
        return portalRepository.findById(id).map(portal -> {
            portal.setAtivo(ativo);
            portalRepository.save(portal);
            String msg = ativo ? "Portal ativado com sucesso." : "Portal desativado com sucesso.";
            return ResponseEntity.ok(new ApiResponse(200, msg, portal));
        }).orElse(ResponseEntity.status(404).body(new ApiResponse(404, "Portal não encontrado.", null)));
    }
}
