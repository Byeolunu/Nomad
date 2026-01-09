package nutar.back.web;

import nutar.back.dao.entites.Portfolio;
import nutar.back.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/portfolios")
@CrossOrigin(origins = "http://localhost:4200")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @GetMapping("/freelancer/{freelancerId}")
    public ResponseEntity<List<Portfolio>> getByFreelancer(@PathVariable Long freelancerId) {
        System.out.println("DEBUG: Getting portfolios for freelancer: " + freelancerId);
        List<Portfolio> items = portfolioService.getPortfoliosByFreelancer(freelancerId);
        System.out.println("DEBUG: Found " + items.size() + " portfolio items");
        return ResponseEntity.ok(items);
    }

    @PostMapping("/freelancer/{freelancerId}")
    @Transactional
    public ResponseEntity<?> addPortfolio(@PathVariable Long freelancerId, @RequestBody Portfolio portfolio) {
        System.out.println("DEBUG: Adding portfolio for freelancer: " + freelancerId);
        System.out.println("DEBUG: Portfolio data: " + portfolio.getTitle());
        try {
            Portfolio saved = portfolioService.createPortfolio(freelancerId, portfolio);
            System.out.println("DEBUG: Portfolio saved with ID: " + saved.getId());
            return ResponseEntity.ok(saved);
        } catch (RuntimeException e) {
            System.err.println("DEBUG ERROR: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> updatePortfolio(@PathVariable Long id, @RequestBody Portfolio portfolio) {
        try {
            Portfolio updated = portfolioService.updatePortfolio(id, portfolio);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deletePortfolio(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            portfolioService.deletePortfolio(id);
            response.put("message", "Portfolio deleted successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
