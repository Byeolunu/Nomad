package nutar.back.service;

import nutar.back.dao.entites.Freelancer;
import nutar.back.dao.entites.Portfolio;
import nutar.back.dao.repositories.FreelancerRepository;
import nutar.back.dao.repositories.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PortfolioManager implements PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private FreelancerRepository freelancerRepository;

    @Override
    public List<Portfolio> getPortfoliosByFreelancer(Long freelancerId) {
        return portfolioRepository.findByFreelancerId(freelancerId);
    }

    @Override
    public Optional<Portfolio> getById(Long id) {
        return portfolioRepository.findById(id);
    }

    @Override
    @Transactional
    public Portfolio createPortfolio(Long freelancerId, Portfolio portfolio) {
        Freelancer freelancer = freelancerRepository.findById(freelancerId)
                .orElseThrow(() -> new RuntimeException("Freelancer not found"));
        portfolio.setFreelancer(freelancer);
        return portfolioRepository.save(portfolio);
    }

    @Override
    @Transactional
    public Portfolio updatePortfolio(Long id, Portfolio portfolio) {
        Portfolio existing = portfolioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        existing.setTitle(portfolio.getTitle());
        existing.setDescription(portfolio.getDescription());
        existing.setImageUrl(portfolio.getImageUrl());
        existing.setProjectUrl(portfolio.getProjectUrl());
        return portfolioRepository.save(existing);
    }

    @Override
    @Transactional
    public void deletePortfolio(Long id) {
        portfolioRepository.deleteById(id);
    }
}
