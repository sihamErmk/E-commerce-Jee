package ma.fstt.beans;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.Data;
import ma.fstt.persistance.*;

import java.util.Date;
import java.util.List;

@Named
@RequestScoped
@Data
public class PanierBeans {
private Date date;

    @Inject
    private CommandeBeans commande;
    private EntityManager em;
    private EntityManagerFactory emf;
    private long clientid;
    private Client client;
    private Produit produit;
    private long produitId;
    private int quantite;
    private Double prix;
    public PanierBeans() {
        emf= Persistence.createEntityManagerFactory("default");
        em = emf.createEntityManager();
    }


    public void save() {
        Panier panier = new Panier();
        em.getTransaction().begin();

        panier.setProduit(Produit.findProduitById(produitId));
        Produit produitqt=panier.getProduit();// Correction ici
        panier.setClient(Client.findClientById(clientid)); // Correction ici
        panier.setQuantite(quantite);
        panier.setPrix(produitqt.getPrix()*quantite);
        panier.setStatus(StringPanier.NON_VALIDE);

        em.persist(panier);
        em.getTransaction().commit();

        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "listPaniers.xhtml");
    }


    public List<Panier>  lister(){
        List<Panier> panier=em.createQuery("select pan from Panier as pan").getResultList();
        return panier;
    }


    public void supprimer (Panier pnr){
        em.getTransaction().begin();
        pnr=em.merge(pnr);
        em.remove(pnr);
        em.getTransaction().commit();
    }


    public void valider_panier(Panier pnr) {
        // Vérifiez que le produit et le panier sont gérés
        if (pnr == null || pnr.getProduit() == null) {
            throw new IllegalArgumentException("Le panier ou le produit associé est invalide.");
        }

        em.getTransaction().begin();
        try {
            // Vérifiez la quantité en stock
            Produit produit = pnr.getProduit(); // Assurez-vous que c'est l'entité gérée
            if (produit.getStock() < pnr.getQuantite()) {
                throw new IllegalStateException("Stock insuffisant pour valider le panier.");
            }

            // Met à jour le statut du panier
            pnr.setStatus(StringPanier.Valide);

            // Décrémente la quantité en stock du produit
            produit.setStock(produit.getStock() - pnr.getQuantite());

            // Synchronise les modifications avec la base
            em.merge(pnr); // Facultatif si pnr est déjà géré
            em.merge(produit); // Facultatif si produit est déjà géré

            // Calcul du total de la commande
            double totalCommande = pnr.getPrix() ;

            // Créer la commande après la validation du panier
            commande.save(pnr.getClient(), new Date(), totalCommande);

            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback(); // Annule la transaction en cas d'erreur
            throw e; // Relance l'exception pour gestion ultérieure
        }
    }


}
