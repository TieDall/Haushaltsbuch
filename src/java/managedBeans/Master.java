
package managedBeans;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import model.Payments;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@Named(value = "master")
@ApplicationScoped
public class Master {
    
    SessionFactory sessionFactory;
    
    ArrayList<Payments> payments;
    
    String heightIncome = "250";
    String heightOutcome = "12";
    
    public Master() {
        this.payments = new ArrayList<>();
        this.loadPayments();
    }    
    
//    private void refreshData() {
//        GregorianCalendar now = new GregorianCalendar();
//        DateFormat df = DateFormat.getDateInstance(DateFormat.MONTH_FIELD);
//        df = DateFormat.getDateInstance(DateFormat.LONG);               // 14. April 2012
//        System.out.println(df.format(now.getTime())); 
//    }
    
    /**
     * Loads the data from the database.
     */
    private void loadPayments() {
        sessionFactory = new Configuration()
            .configure("/model/hibernate.cfg.xml")
            .buildSessionFactory();
        Session session;
        
        session = sessionFactory.openSession();
        session.beginTransaction();
        List result = session.createQuery( "from Payments" ).list();
        this.payments = new ArrayList(result);
        for (Payments payment : payments) {
            System.out.println(payment.getCreated());
        }
        session.getTransaction().commit();
        session.close();
    }

    /*
        GETTER / SETTER
    */
    
    public String getHeightIncome() {
        return heightIncome;
    }

    public void setHeightIncome(String heightIncome) {
        this.heightIncome = heightIncome;
    }

    public String getHeightOutcome() {
        return heightOutcome;
    }

    public void setHeightOutcome(String heightOutcome) {
        this.heightOutcome = heightOutcome;
    }
    
    
    
}
