package semit.saoluis.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import semit.saoluis.model.Projetos;
import semit.saoluis.repository.ProjetosRepository;
import semit.saoluis.responses.ApiResponse;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/projetos")
@CrossOrigin(origins = "*")
public class ProjetosController {

    private final ProjetosRepository projetoRepository;
    
    @Value("${upload.dirprojetos}")
    private String uploadDirProjetos;  // 👈 Diretório definido no application.properties

    public ProjetosController(ProjetosRepository projetoRepository) {
        this.projetoRepository = projetoRepository;
    }
    
    @PostMapping("/upload")
    public ResponseEntity<?> uploadProjeto(
            @RequestParam("file") MultipartFile file,
            @RequestParam("nome") String nome,
            @RequestParam("link") String link,
            @RequestParam(value = "isVisible", required = false, defaultValue = "true") boolean isVisible) {
        try {
            File diretorio = new File(uploadDirProjetos);
            if (!diretorio.exists()) {
                diretorio.mkdirs();
            }

            String nomeArquivo = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path caminho = Paths.get(uploadDirProjetos + File.separator + nomeArquivo);

            Files.copy(file.getInputStream(), caminho, StandardCopyOption.REPLACE_EXISTING);

            Projetos projeto = new Projetos();
            projeto.setNome(nome);
            projeto.setTo("/projetos/" + nomeArquivo);
            projeto.setLink(link);  // pode ajustar o link pra refletir pasta pública correta
            projeto.setIsVisible(isVisible);

            projetoRepository.save(projeto);

            return ResponseEntity.status(201).body(
                    new ApiResponse(201, "Projeto salvo com sucesso!", projeto)
            );

        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new ApiResponse(500, "Erro ao salvar o projeto: " + e.getMessage(), null)
            );
        }
    }

    // Listar todos
    @GetMapping
    public ResponseEntity<?> listar() {
        List<Projetos> projetos = projetoRepository.findAll();
        if (projetos.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse(200, "Nenhum projeto cadastrado.", projetos));
        }
        return ResponseEntity.ok(new ApiResponse(200, "Projetos encontrados.", projetos));
    }

    // Buscar por id
    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        Optional<Projetos> projeto = projetoRepository.findById(id);
        if (projeto.isPresent()) {
            return ResponseEntity.ok(new ApiResponse(200, "Projeto encontrado.", projeto.get()));
        } else {
            return ResponseEntity.status(404)
                    .body(new ApiResponse(404, "Projeto não encontrado com ID: " + id, null));
        }
    }
    
    @GetMapping("/visiveis")
    public ResponseEntity<?> listarSomenteVisiveis() {
        List<Projetos> projetos = projetoRepository.findAll()
                .stream()
                .filter(projeto -> Boolean.TRUE.equals(projeto.getIsVisible()))
                .toList();

        return ResponseEntity.ok(new ApiResponse(200, "Projetos visíveis encontrados.", projetos));
    }

    // Criar novo
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Projetos projeto) {
        Projetos novoProjeto = projetoRepository.save(projeto);
        return ResponseEntity.status(201)
                .body(new ApiResponse(201, "Projeto criado com sucesso.", novoProjeto));
    }

    // Atualizar
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Projetos projetoAtualizado) {
        return projetoRepository.findById(id).map(projeto -> {
            projeto.setNome(projetoAtualizado.getNome());
            projeto.setLink(projetoAtualizado.getLink());
            projeto.setTo(projetoAtualizado.getTo());
            projetoRepository.save(projeto);
            return ResponseEntity.ok(new ApiResponse(200, "Projeto atualizado com sucesso.", projeto));
        }).orElse(ResponseEntity.status(404)
                .body(new ApiResponse(404, "Projeto não encontrado com ID: " + id, null)));
    }
    
    // 🔸 Alterar visibilidade da imagem
    @PutMapping("/visibilidade/{id}")
    public ResponseEntity<?> alterarVisibilidade(
            @PathVariable Long id,
            @RequestParam("isVisible") boolean isVisible) {

        return projetoRepository.findById(id).map(projeto -> {
            projeto.setIsVisible(isVisible);
            projetoRepository.save(projeto);
            String mensagem = isVisible ? "Imagem ativada com sucesso." : "Imagem desativada com sucesso.";
            return ResponseEntity.ok(new ApiResponse(200, mensagem, projeto));
        }).orElse(ResponseEntity.status(404)
                .body(new ApiResponse(404, "Imagem não encontrada com ID: " + id, null)));
    }

    // Deletar
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        return projetoRepository.findById(id).map(projeto -> {
            projetoRepository.delete(projeto);
            return ResponseEntity.ok(new ApiResponse(200, "Projeto deletado com sucesso.", null));
        }).orElse(ResponseEntity.status(404)
                .body(new ApiResponse(404, "Projeto não encontrado com ID: " + id, null)));
    }
}
