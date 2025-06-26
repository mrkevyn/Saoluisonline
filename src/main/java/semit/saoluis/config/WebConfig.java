package semit.saoluis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${upload.dir}")
    private String uploadDir;

    @Value("${upload.dirprojetos}")
    private String uploadDirProjetos;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 🔸 Pasta de imagens (banners, etc)
        registry.addResourceHandler("/imagens/**")
                .addResourceLocations("file:///" + uploadDir);

        // 🔸 Pasta de projetos
        registry.addResourceHandler("/projetos/**")
                .addResourceLocations("file:///" + uploadDirProjetos);
    }
}
