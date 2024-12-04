package ma.fstt.persistance;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String addresse;
    private String telephone;
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Panier> paniers;

    public static Client findClientById(long clientId) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();
        return em.find(Client.class, clientId);
    }
}
