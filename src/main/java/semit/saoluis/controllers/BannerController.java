package semit.saoluis.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import semit.saoluis.model.Banner;
import semit.saoluis.repository.BannerRepository;
import semit.saoluis.responses.ApiResponse;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/banners")
@CrossOrigin(origins = "*")
public class BannerController {

    private final BannerRepository imagemRepository;

    @Value("${upload.dir}")
    private String uploadDir;  // 👈 Diretório definido no application.properties

    public BannerController(BannerRepository imagemRepository) {
        this.imagemRepository = imagemRepository;
    }

    // 🔸 Upload de imagem
    @PostMapping("/upload")
    public ResponseEntity<?> uploadImagem(
            @RequestParam("file") MultipartFile file,
            @RequestParam("nome") String nome,
            @RequestParam("link") String link,
            @RequestParam(value = "isVisible", required = false, defaultValue = "true") boolean isVisible) {
        try {
            File diretorio = new File(uploadDir);
            if (!diretorio.exists()) {
                diretorio.mkdirs();
            }

            String nomeArquivo = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path caminho = Paths.get(uploadDir + File.separator + nomeArquivo);

            Files.copy(file.getInputStream(), caminho, StandardCopyOption.REPLACE_EXISTING);

            Banner imagem = new Banner();
            imagem.setNome(nome);
            imagem.setTo("/imagens/" + nomeArquivo);
            imagem.setLink(link);
            imagem.setIsVisible(isVisible); // 👈 Aqui você seta o valor que veio no request

            imagemRepository.save(imagem);

            return ResponseEntity.status(201).body(
                    new ApiResponse(201, "Imagem salva com sucesso!", imagem)
            );

        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    new ApiResponse(500, "Erro ao salvar a imagem: " + e.getMessage(), null)
            );
        }
    }

    // 🔸 Listar todas as imagens
    @GetMapping
    public ResponseEntity<?> listar() {
        List<Banner> imagens = imagemRepository.findAll();

        if (imagens.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse(200, "Nenhuma imagem cadastrada.", imagens));
        }
        return ResponseEntity.ok(new ApiResponse(200, "Imagens encontradas.", imagens));
    }

    // 🔸 Buscar imagem por ID
    @GetMapping("/buscar/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        Optional<Banner> imagem = imagemRepository.findById(id);

        if (imagem.isPresent()) {
            return ResponseEntity.ok(new ApiResponse(200, "Imagem encontrada.", imagem.get()));
        } else {
            return ResponseEntity.status(404)
                    .body(new ApiResponse(404, "Imagem não encontrada com ID: " + id, null));
        }
    }
    
    @GetMapping("/visiveis")
    public ResponseEntity<?> listarSomenteVisiveis() {
        List<Banner> imagens = imagemRepository.findAll()
                .stream()
                .filter(imagem -> Boolean.TRUE.equals(imagem.getIsVisible()))
                .toList();

        return ResponseEntity.ok(new ApiResponse(200, "Imagens visíveis encontradas.", imagens));
    }

    // 🔸 Atualizar uma imagem
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Banner imagemAtualizada) {
        return imagemRepository.findById(id).map(imagem -> {
            imagem.setNome(imagemAtualizada.getNome());
            imagem.setLink(imagemAtualizada.getLink());
            imagem.setTo(imagemAtualizada.getTo());
            imagemRepository.save(imagem);
            return ResponseEntity.ok(new ApiResponse(200, "Imagem atualizada com sucesso.", imagem));
        }).orElse(ResponseEntity.status(404)
                .body(new ApiResponse(404, "Imagem não encontrada com ID: " + id, null)));
    }
    
 // 🔸 Alterar visibilidade da imagem
    @PutMapping("/visibilidade/{id}")
    public ResponseEntity<?> alterarVisibilidade(
            @PathVariable Long id,
            @RequestParam("isVisible") boolean isVisible) {

        return imagemRepository.findById(id).map(imagem -> {
            imagem.setIsVisible(isVisible);
            imagemRepository.save(imagem);
            String mensagem = isVisible ? "Imagem ativada com sucesso." : "Imagem desativada com sucesso.";
            return ResponseEntity.ok(new ApiResponse(200, mensagem, imagem));
        }).orElse(ResponseEntity.status(404)
                .body(new ApiResponse(404, "Imagem não encontrada com ID: " + id, null)));
    }

    // 🔸 Deletar uma imagem
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        return imagemRepository.findById(id).map(imagem -> {
            imagemRepository.delete(imagem);
            return ResponseEntity.ok(new ApiResponse(200, "Imagem deletada com sucesso.", null));
        }).orElse(ResponseEntity.status(404)
                .body(new ApiResponse(404, "Imagem não encontrada com ID: " + id, null)));
    }
}
