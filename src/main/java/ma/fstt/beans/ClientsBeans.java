package ma.fstt.beans;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.Data;
import ma.fstt.persistance.Client;

import java.util.List;
@Named
@RequestScoped
@Data

public class ClientsBeans {
    private String nom;
    private String addresse;
    private String telephone;
    private Client client;
    private EntityManagerFactory emf ;
    private EntityManager em ;
    private Client selectedClient;

    public ClientsBeans(){
        emf = Persistence.createEntityManagerFactory("default");
        em = emf.createEntityManager();
    }

    public void save(){
        Client clt = new Client();
        clt.setNom(nom);
        clt.setTelephone(telephone);
        clt.setAddresse(addresse);

        em.getTransaction().begin();
        System.out.println("COMIITING");
        em.persist(clt);
        em.getTransaction().commit();

        FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, "listClients.xhtml");

        //#return "listEtudiants.xhtml?faces-redirect=true";

    }

    public List<Client> lister (){

        List<Client> malist  = em.createQuery(" select clt from Client as clt").getResultList();

        return malist ;
    }
    public Client findClientById(Long id) {
        em.getTransaction().begin();
        Client client = em.find(Client.class, id);
        em.getTransaction().commit();
        return client;
    }

    public void supprimer(Client clt) {
        em.getTransaction().begin();
        clt = em.merge(clt); // Assure que l'entité est gérée
        em.remove(clt);
        em.getTransaction().commit();
    }

    public void edit(Long clientId) {
        client = em.find(Client.class, clientId);
        if (client == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur", "Client introuvable !"));
            FacesContext.getCurrentInstance().getApplication().getNavigationHandler()
                    .handleNavigation(FacesContext.getCurrentInstance(), null, "listClients.xhtml");
        } else {
            FacesContext.getCurrentInstance().getApplication().getNavigationHandler()
                    .handleNavigation(FacesContext.getCurrentInstance(), null, "edit.xhtml");
        }
    }

    public void update(Client clt) {
        if (clt != null) {


            em.getTransaction().begin();
            em.merge(clt); // Mise à jour du client
            em.getTransaction().commit();

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Succès", "Client mis à jour avec succès !"));
            FacesContext.getCurrentInstance().getApplication().getNavigationHandler()
                    .handleNavigation(FacesContext.getCurrentInstance(), null, "listClients.xhtml");
        }

    }




}
