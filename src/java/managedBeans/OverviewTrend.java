
package managedBeans;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import model.Payments;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@Named(value = "overviewTrend")
@RequestScoped
public class OverviewTrend {
    
    // db meta
    SessionFactory sessionFactory;
    Session session;
    
    // data overview trend
    ArrayList<Payments> payments;
    String trendHalfyearData = ""; //{x:'März', y:-15},{x:'April', y:-111},{x:'Mai', y:20},{x:'Juni', y:70},{x:'Juli', y:2},{x:'August', y:13},{x:'September', y:13}";
    String trendHalfyearColor = "'rgb(54, 162, 235)','rgb(255, 159, 64)','rgb(255, 205, 86)','rgb(54, 162, 235)','rgb(255, 159, 64)','rgb(255, 205, 86)','rgb(54, 162, 235)','rgb(54, 162, 235)',";
    String trendHalfyearLabels = ""; //'März','April','Mai','Juni','Juli','August','September',";
    String trendHalfyearIncome = "";
    String trendHalfyearOutcome = "";
    String trendHalfyearBalance = "";
    
    /**
     * Constructor.
     */
    public OverviewTrend() {
        this.connectToDB();
        this.loadPayments();
        this.loadData();
        this.disconnectFromDB();
    }
    
    /**
     * Connect to database.
     */
    private void connectToDB() {
        sessionFactory = new Configuration()
            .configure("/model/hibernate.cfg.xml")
            .buildSessionFactory();
        session = sessionFactory.openSession();
    }
    
    /**
     * Disconnect from database.
     */
    private void disconnectFromDB() {
        session.close();
    }
    
    /**
     * Loads payments and store in object var.
     */
    private void loadPayments() {  
        session.beginTransaction();
        List result = session.createQuery( "from Payments order by created desc" ).list();
        this.payments = new ArrayList(result);
        session.getTransaction().commit();
        for (Payments payment : this.payments) {
            System.out.println("payment: "+payment.getAmount());
        }
    }
    
    /**
     * Loads data which is needed for trend on overview page.
     */
    private void loadData() {
        // half year        
        float bufferTrendHalfyearIncome = 0;
        float bufferTrendHalfyearOutcome = 0;
        float bufferTrendHalfyearBalance = 0;
        // create HashMap for months
        HashMap<Integer,Float> blub = new HashMap<>();
        Timestamp currentTimestamp = Timestamp.valueOf(LocalDateTime.now());
        int month = currentTimestamp.getMonth();
        boolean twoYears = false;
        for (int i = 0; i < 6; i++) {
            if (month < 0) {
                month = 11;
                twoYears = true;
            }
            blub.put(month, new Float(0));
            month--;
        }    
        // check if in year range
        ArrayList<Payments> payHalfYear = new ArrayList<>();
        for (Payments payment : payments) {
            if (twoYears) {
                if (payment.getCreated().getYear() == currentTimestamp.getYear() ||
                        payment.getCreated().getYear() == (currentTimestamp.getYear()-1)) {
                    payHalfYear.add(payment);
                }
            } else {
                if (payment.getCreated().getYear() == currentTimestamp.getYear()) {
                    payHalfYear.add(payment);
                }
            }
        }        
        // order into HashMap
        for (Payments payment : payHalfYear) {
            for (Map.Entry<Integer, Float> entry : blub.entrySet()) {
                if (entry.getKey() == payment.getCreated().getMonth()) {
                    if (payment.getCategory().isIsIncome()) {
                        entry.setValue(entry.getValue() + payment.getAmount());
                        bufferTrendHalfyearIncome = bufferTrendHalfyearIncome + payment.getAmount();
                    } else {
                        entry.setValue(entry.getValue() - payment.getAmount());
                        bufferTrendHalfyearOutcome = bufferTrendHalfyearOutcome + payment.getAmount();
                    }
                }
            }
        }
        // create var
        String bufferTrendHalfyearData = "";
        String bufferTrendHalfyearLabels = "";
        for (Map.Entry<Integer, Float> entry : blub.entrySet()) {
            bufferTrendHalfyearData = bufferTrendHalfyearData + "{x:'";
            bufferTrendHalfyearLabels = bufferTrendHalfyearLabels + "'";
            switch (entry.getKey()) {
                case 0:
                    bufferTrendHalfyearData = bufferTrendHalfyearData + "Januar";
                    bufferTrendHalfyearLabels = bufferTrendHalfyearLabels + "Januar";
                    break;
                case 1:
                    bufferTrendHalfyearData = bufferTrendHalfyearData + "Februar";
                    bufferTrendHalfyearLabels = bufferTrendHalfyearLabels + "Februar";
                    break;
                case 2:
                    bufferTrendHalfyearData = bufferTrendHalfyearData + "März";
                    bufferTrendHalfyearLabels = bufferTrendHalfyearLabels + "März";
                    break;
                case 3:
                    bufferTrendHalfyearData = bufferTrendHalfyearData + "April";
                    bufferTrendHalfyearLabels = bufferTrendHalfyearLabels + "April";
                    break;
                case 4:
                    bufferTrendHalfyearData = bufferTrendHalfyearData + "Mai";
                    bufferTrendHalfyearLabels = bufferTrendHalfyearLabels + "Mai";
                    break;
                case 5:
                    bufferTrendHalfyearData = bufferTrendHalfyearData + "Juni";
                    bufferTrendHalfyearLabels = bufferTrendHalfyearLabels + "Juni";
                    break;
                case 6:
                    bufferTrendHalfyearData = bufferTrendHalfyearData + "Juli";
                    bufferTrendHalfyearLabels = bufferTrendHalfyearLabels + "Juli";
                    break;
                case 7:
                    bufferTrendHalfyearData = bufferTrendHalfyearData + "August";
                    bufferTrendHalfyearLabels = bufferTrendHalfyearLabels + "August";
                    break;
                case 8:
                    bufferTrendHalfyearData = bufferTrendHalfyearData + "September";
                    bufferTrendHalfyearLabels = bufferTrendHalfyearLabels + "September";
                    break;
                case 9:
                    bufferTrendHalfyearData = bufferTrendHalfyearData + "Oktober";
                    bufferTrendHalfyearLabels = bufferTrendHalfyearLabels + "Oktober";
                    break;
                case 10:
                    bufferTrendHalfyearData = bufferTrendHalfyearData + "November";
                    bufferTrendHalfyearLabels = bufferTrendHalfyearLabels + "November";
                    break;
                case 11:
                    bufferTrendHalfyearData = bufferTrendHalfyearData + "Dezember";
                    bufferTrendHalfyearLabels = bufferTrendHalfyearLabels + "Dezember";
                    break;
                default:
                    break;
            }
            bufferTrendHalfyearData = bufferTrendHalfyearData + "',y:" + entry.getValue() + "},";
            bufferTrendHalfyearLabels = bufferTrendHalfyearLabels + "',";
        }
        bufferTrendHalfyearBalance = bufferTrendHalfyearIncome - bufferTrendHalfyearOutcome;
        // store in local var
        this.trendHalfyearData = bufferTrendHalfyearData;
        this.trendHalfyearLabels = bufferTrendHalfyearLabels;
        this.trendHalfyearIncome = ""+bufferTrendHalfyearIncome;
        this.trendHalfyearOutcome = ""+bufferTrendHalfyearOutcome;
        this.trendHalfyearBalance = ""+bufferTrendHalfyearBalance;
    }   
    
    /*
        GETTER / SETTER
    */

    public String getTrendHalfyearData() {
        return trendHalfyearData;
    }

    public void setTrendHalfyearData(String trendHalfyearData) {
        this.trendHalfyearData = trendHalfyearData;
    }

    public String getTrendHalfyearColor() {
        return trendHalfyearColor;
    }

    public void setTrendHalfyearColor(String trendHalfyearColor) {
        this.trendHalfyearColor = trendHalfyearColor;
    }

    public String getTrendHalfyearLabels() {
        return trendHalfyearLabels;
    }

    public void setTrendHalfyearLabels(String trendHalfyearLabels) {
        this.trendHalfyearLabels = trendHalfyearLabels;
    }

    public String getTrendHalfyearIncome() {
        return trendHalfyearIncome;
    }

    public void setTrendHalfyearIncome(String trendHalfyearIncome) {
        this.trendHalfyearIncome = trendHalfyearIncome;
    }

    public String getTrendHalfyearOutcome() {
        return trendHalfyearOutcome;
    }

    public void setTrendHalfyearOutcome(String trendHalfyearOutcome) {
        this.trendHalfyearOutcome = trendHalfyearOutcome;
    }

    public String getTrendHalfyearBalance() {
        return trendHalfyearBalance;
    }

    public void setTrendHalfyearBalance(String trendHalfyearBalance) {
        this.trendHalfyearBalance = trendHalfyearBalance;
    }
    
}
