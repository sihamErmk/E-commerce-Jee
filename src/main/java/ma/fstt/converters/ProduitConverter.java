package ma.fstt.converters;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import ma.fstt.persistance.Produit;
import ma.fstt.beans.ProduitBeans;

@FacesConverter(value = "produitConverter")
public class ProduitConverter implements Converter<Produit> {

    @Override
    public Produit getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        ProduitBeans produitBeans = context.getApplication().evaluateExpressionGet(context, "#{produitBeans}", ProduitBeans.class);
        return produitBeans.findProduitById(Long.valueOf(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Produit produit) {
        if (produit == null) {
            return "";
        }
        return String.valueOf(produit.getId());
    }
}
