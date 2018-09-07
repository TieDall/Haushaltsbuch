
package ws_server;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.jws.WebMethod;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import model.Payments;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


@WebService(name = "Data")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
public class Data {
    
    
    @WebMethod(operationName = "getPieData")
    @WebResult(name = "dataPie")
    public HashMap<String, Float> dataPie() {        
        return getDataPie();
    }
    
    @WebMethod(operationName = "getBarData")
    @WebResult(name = "dataBar")
    public HashMap<String, Float> dataBar() {        
        return getDataBar();
    }
    
    private HashMap<String, Float> getDataPie() {
        HashMap<String, Float> data = new HashMap<>(); 
        ArrayList<Payments> payments = new ArrayList<>();
        
        SessionFactory sessionFactory;
        Session session;
        sessionFactory = new Configuration()
                .configure("/model/hibernate.cfg.xml")
                .buildSessionFactory();
        session = sessionFactory.openSession();
        
        session.beginTransaction();
        List result = session.createQuery( "from Payments order by created desc" ).list();
        payments = new ArrayList(result);
        session.getTransaction().commit();
        
        // seperate in income and outcome
        ArrayList<Payments> payIn = new ArrayList<>();
        for (Payments payment : payments) {
            if (payment.getCategory().isIsIncome()){
                payIn.add(payment);
            }
        }
        
        // month
        // seperate current month
        ArrayList<Payments> payInMonth = new ArrayList<>();
        Timestamp currentTimestamp = Timestamp.valueOf(LocalDateTime.now());
        for (Payments paymentsIn : payIn) {
            if (paymentsIn.getCreated().getMonth() == currentTimestamp.getMonth()){
                payInMonth.add(paymentsIn);
            }
        }
        // seperate in categories
        HashMap<String,Float> payInMonthCat = new HashMap<String, Float>();
        for (Payments paymentsIn : payInMonth) {
            boolean added = false;
            for (Map.Entry<String, Float> entry : payInMonthCat.entrySet()) {
                if (entry.getKey().equals(paymentsIn.getCategory().getName())) {
                    entry.setValue(entry.getValue() + paymentsIn.getAmount());
                    added = true;
                }
            }
            if (!added) {
                payInMonthCat.put(paymentsIn.getCategory().getName(), paymentsIn.getAmount());
            }
        }
        
        session.close();
        
        return payInMonthCat;
    }
    
    private HashMap<String, Float> getDataBar() {
        HashMap<String, Float> data = new HashMap<>();
        ArrayList<Payments> payments = new ArrayList<>();
        
        SessionFactory sessionFactory;
        Session session;
        sessionFactory = new Configuration()
                .configure("/model/hibernate.cfg.xml")
                .buildSessionFactory();
        session = sessionFactory.openSession();
        
        session.beginTransaction();
        List result = session.createQuery( "from Payments order by created desc" ).list();
        payments = new ArrayList(result);
        session.getTransaction().commit();
        
        // extract data which was created in current month
        ArrayList<Payments> paymentsCal = new ArrayList<>();
        for (Payments payment : payments) {
            Timestamp currentTimestamp = Timestamp.valueOf(LocalDateTime.now());
            Timestamp ts = new Timestamp(currentTimestamp.getYear(), currentTimestamp.getMonth(), 1, 0, 0, 0, 0);
            if (payment.getCreated().after(ts)) {
                paymentsCal.add(payment);
            }
        }
        
        // seperate in income and outcome
        ArrayList<Payments> incomes = new ArrayList<>();
        ArrayList<Payments> outcomes = new ArrayList<>();
        for (Payments payment : paymentsCal) {
            if (payment.getCategory().isIsIncome()) {
                incomes.add(payment);
            } else {
                outcomes.add(payment);
            }
        }
        
        // calculate sums
        float income_sum = 0;
        float outcome_sum = 0;
        for (Payments income : incomes) {
            income_sum = income_sum+ income.getAmount();
        }
        for (Payments outcome : outcomes) {
            outcome_sum = outcome_sum + outcome.getAmount();
        }
        float sumIncome = income_sum;
        float sumOutcome = outcome_sum;
        
        data.put("Einnahmen", sumIncome);
        data.put("Ausgaben", sumOutcome);
        
        session.close();
        
        return data;
    }
    
}
