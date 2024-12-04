package ma.fstt.converters;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.inject.Named;
import ma.fstt.persistance.Client;
import ma.fstt.beans.ClientsBeans;

@FacesConverter(forClass = Client.class)
public class ClientConverter implements Converter {

    private final ClientsBeans clientsBeans;

    @jakarta.inject.Inject
    public ClientConverter(@Named ClientsBeans clientsBeans) {
        this.clientsBeans = clientsBeans;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        Long id = Long.valueOf(value);
        // Fetch the Client object from the database or a service using the ID
        return clientsBeans.findClientById(id);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object object) {
        if (object == null) {
            return "";
        }
        if (object instanceof Client) {
            return String.valueOf(((Client) object).getId());
        } else {
            throw new IllegalArgumentException("Object is not a Client: " + object);
        }
    }
}
