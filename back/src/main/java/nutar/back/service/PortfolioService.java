package nutar.back.service;

import nutar.back.dao.entites.Portfolio;

import java.util.List;
import java.util.Optional;

public interface PortfolioService {
    List<Portfolio> getPortfoliosByFreelancer(Long freelancerId);
    Optional<Portfolio> getById(Long id);
    Portfolio createPortfolio(Long freelancerId, Portfolio portfolio);
    Portfolio updatePortfolio(Long id, Portfolio portfolio);
    void deletePortfolio(Long id);
}
