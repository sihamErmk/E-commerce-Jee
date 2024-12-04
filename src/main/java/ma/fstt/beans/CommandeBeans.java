package ma.fstt.beans;


import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.Data;
import ma.fstt.persistance.Client;
import ma.fstt.persistance.Commande;
import ma.fstt.persistance.Produit;

import java.util.Date;
import java.util.List;

@Named
@RequestScoped
@Data
public class CommandeBeans {
    private EntityManager em;
    private EntityManagerFactory emf;
    private Client client;
    private Date dateCommande;
    private double total;
    public CommandeBeans() {
        emf= Persistence.createEntityManagerFactory("default");
        em=emf.createEntityManager();
    }


    public void save(Client client, Date dateCommande, double total) {
        Commande commande=new Commande();
        em.getTransaction().begin();
        commande.setDateCommande(dateCommande);
        commande.setTotal(total);
        commande.setClient(client);
        em.persist(commande);
        em.getTransaction().commit();
    }

    public List<Commande> list() {
        List<Commande> commandes=em.createQuery("select cmd from Commande as cmd").getResultList();
        return commandes;
    }

    public void supprimer(Commande cmd){
        em.getTransaction().begin();
        cmd=em.merge(cmd);
        em.remove(cmd);
        em.getTransaction().commit();
    }
}
