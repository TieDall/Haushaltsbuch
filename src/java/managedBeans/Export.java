package managedBeans;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.xml.ws.Endpoint;
import ws_client.IOException_Exception;
import ws_client.PDFGenerator;
import ws_client.PDFGeneratorService;
import ws_server.Data;

@Named(value = "export")
@SessionScoped
public class Export implements Serializable {
    
    String linkPie;
    String linkBar;
    String failureMessage;
    
    boolean wsStarted;
    
    public Export() {
        linkPie = "http://localhost/Piechart.pdf";
        linkBar = "http://localhost/Barchart.pdf";
        failureMessage = "";        
        wsStarted = false;
    }
    
    private void startWS() {
        Endpoint endpointPDF = Endpoint.publish( "http://localhost:2345/services", new Data());
        wsStarted = true;
    }
    
    /**
     * Calls SOAP-RPC on Plugin-Server for generating PDF export (pie).
     */
    public void generatePie() {
        if (!wsStarted) {
            startWS();
        }
        PDFGenerator pdf = new PDFGeneratorService().getPDFGeneratorPort();
        try {
            pdf.createPieChart();
        } catch (IOException_Exception ex) {
            failureMessage = ex.toString();
        }
    }
    
    /**
     * Calls SOAP-RPC on Plugin-Server for generating PDF export (bar).
     */
    public void generateBar() {
        if (!wsStarted) {
            startWS();
        }
        PDFGenerator pdf = new PDFGeneratorService().getPDFGeneratorPort();
        try {
            pdf.createBarChart();
        } catch (IOException_Exception ex) {
            failureMessage = ex.toString();
        }
    }
    
    /*
        GETTER / SETTER
    */

    public String getLinkPie() {
        return linkPie;
    }

    public void setLinkPie(String linkPie) {
        this.linkPie = linkPie;
    }

    public String getLinkBar() {
        return linkBar;
    }

    public void setLinkBar(String linkBar) {
        this.linkBar = linkBar;
    }
    
}
