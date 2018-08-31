package managedBeans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named(value = "export")
@RequestScoped
public class Export {
    
    String link;
    
    public Export() {
        link = "";
    }
    
    /**
     * Calls SOAP-RPC on Plugin-Server for generating PDF export.
     */
    public void generate() {
        
    }
    
    /*
        GETTER / SETTER
    */

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
    
}
