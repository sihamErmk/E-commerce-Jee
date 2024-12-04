package ma.fstt.persistance;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Produit")
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String description;
    private Double prix;
    private int stock;

    public static Produit findProduitById(long produitId) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();
        return em.find(Produit.class, produitId);
    }
}
