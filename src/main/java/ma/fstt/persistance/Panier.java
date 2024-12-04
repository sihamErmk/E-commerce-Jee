package ma.fstt.persistance;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Panier")
public class Panier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "produit_id")
    private Produit produit;

    private int quantite;
    private Double prix;
    private StringPanier status;


}
