package ma.fstt.beans;


import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.Data;
import ma.fstt.persistance.Client;
import ma.fstt.persistance.Produit;

import java.util.List;

@Named
@RequestScoped
@Data
public class ProduitBeans {
    private Produit produit;
    private String nom;
    private String description;
    private Double prix;
    private int stock;
    private EntityManagerFactory emf;
    private EntityManager em;

    public ProduitBeans() {
        emf= Persistence.createEntityManagerFactory("default");
        em = emf.createEntityManager();
    }

    public void save(){
        em.getTransaction().begin();
    Produit produit=new Produit();
    produit.setNom(nom);
    produit.setDescription(description);
    produit.setPrix(prix);
    produit.setStock(stock);

    em.persist(produit);
    em.getTransaction().commit();

    FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "listProduits.xhtml");

    }
    public Produit findProduitById(Long id) {
        em.getTransaction().begin();
        Produit produit = em.find(Produit.class, id);
        em.getTransaction().commit();
        return produit;
    }

    public List<Produit> ListProduits(){
        List<Produit> produits=em.createQuery("select prd from Produit as prd").getResultList();
        return produits;
    }

    public void supprimer(Produit prd){
        em.getTransaction().begin();
        prd=em.merge(prd);
        em.remove(prd);
        em.getTransaction().commit();

    }
}
