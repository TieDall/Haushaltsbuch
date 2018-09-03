package managedBeans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import ws.IOException_Exception;
import ws.PDFGenerator;
import ws.PDFGeneratorService;

@Named(value = "export")
@RequestScoped
public class Export {
    
    String link;
    String failureMessage;
    
    public Export() {
        link = "http://localhost/Piechart.pdf";
        failureMessage = "";
    }
    
    /**
     * Calls SOAP-RPC on Plugin-Server for generating PDF export.
     */
    public void generate() {
        PDFGenerator pdf = new PDFGeneratorService().getPDFGeneratorPort();
        try {
            pdf.createPDF();
        } catch (IOException_Exception ex) {
            failureMessage = ex.toString();
        }
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
