package hufs.lion.team404.service;

import hufs.lion.team404.domain.entity.Portfolio;
import hufs.lion.team404.domain.entity.PortfolioImage;
import hufs.lion.team404.repository.PortfolioImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PortfolioImageService {
    private final PortfolioImageRepository portfolioImageRepository;

    public void save(PortfolioImage portfolioImage) {
        portfolioImageRepository.save(portfolioImage);
    }

    public void delete(PortfolioImage portfolioImage) {
        portfolioImageRepository.delete(portfolioImage);
    }

    @Transactional
    public void update(Portfolio portfolio, List<MultipartFile> images) {
        if (images == null) return;

        var existing = portfolioImageRepository
                .findByPortfolioIdOrderByImageOrderAsc(portfolio.getId());
        portfolioImageRepository.deleteAll(existing);

        if (images.isEmpty()) return;

        int order = 1;
        for (MultipartFile file : images) {
            if (file == null || file.isEmpty()) continue;

            String saved = file.getOriginalFilename();
            PortfolioImage img = PortfolioImage.builder()
                    .portfolio(portfolio)
                    .imagePath("/uploads/portfolio/" + saved)
                    .originalFileName(file.getOriginalFilename())
                    .savedFileName(saved)
                    .fileSize(file.getSize())
                    .contentType(file.getContentType())
                    .imageOrder(order++)
                    .uploadedAt(java.time.LocalDateTime.now())
                    .build();

            portfolioImageRepository.save(img);
        }
    }

}
