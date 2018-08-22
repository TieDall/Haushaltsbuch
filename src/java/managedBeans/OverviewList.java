
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

@Named(value = "overviewList")
@RequestScoped
public class OverviewList {
    
    // db meta
    SessionFactory sessionFactory;
    Session session;
    
    // data overview list
    ArrayList<Payments> payments;
    String inMonthData = "";
    String inMonthColor = ""; 
    String inMonthLabel = "";
    ArrayList<Payments> inMonthList = new ArrayList<>();
    String outMonthData = "";
    String outMonthColor = ""; 
    String outMonthLabel = "";
    ArrayList<Payments> outMonthList = new ArrayList<>();
    
    /**
     * Constructor.
     */
    public OverviewList(){
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
     * Loads data which is needed for overview list.
     */
    private void loadData(){
              
        // seperate in income and outcome
        ArrayList<Payments> payIn = new ArrayList<>();
        ArrayList<Payments> payOut = new ArrayList<>();
        for (Payments payment : payments) {
            if (payment.getCategory().isIsIncome()){
                payIn.add(payment);
            } else {
                payOut.add(payment);
            }
        }
        
        // month
        // seperate current month
        ArrayList<Payments> payInMonth = new ArrayList<>();
        ArrayList<Payments> payOutMonth = new ArrayList<>();
        Timestamp currentTimestamp = Timestamp.valueOf(LocalDateTime.now());
        for (Payments payments : payIn) {
            if (payments.getCreated().getMonth() == currentTimestamp.getMonth()){
                payInMonth.add(payments);
            }
        }
        for (Payments payments : payOut) {
            if (payments.getCreated().getMonth() == currentTimestamp.getMonth()){
                payOutMonth.add(payments);
            }
        }
        // seperate in categories
        HashMap<String,Float> payInMonthCat = new HashMap<String, Float>();
        HashMap<String,Float> payOutMonthCat = new HashMap<String, Float>();
        for (Payments payments : payInMonth) {
            boolean added = false;
            for (Map.Entry<String, Float> entry : payInMonthCat.entrySet()) {
                if (entry.getKey().equals(payments.getCategory().getName())) {
                    entry.setValue(entry.getValue() + payments.getAmount());
                    added = true;
                }
            }
            if (!added) {
                payInMonthCat.put(payments.getCategory().getName(), payments.getAmount());
            }
        }
        for (Payments payments : payOutMonth) {
            boolean added = false;
            for (Map.Entry<String, Float> entry : payOutMonthCat.entrySet()) {
                if (entry.getKey().equals(payments.getCategory().getName())) {
                    entry.setValue(entry.getValue() + payments.getAmount());
                    added = true;
                }
            }
            if (!added) {
                payOutMonthCat.put(payments.getCategory().getName(), payments.getAmount());
            }
        }
        // create var
        String bufferInMonthData = "";
        String bufferInMonthColor = "";
        String bufferInMonthLabel = "";
        String bufferOutMonthData = "";
        String bufferOutMonthColor = "";
        String bufferOutMonthLabel = "";
        for (Map.Entry<String, Float> entry : payInMonthCat.entrySet()) {
            String key = entry.getKey();
            Float value = entry.getValue();
            bufferInMonthData = bufferInMonthData + "'" + value + "',";
            bufferInMonthLabel = bufferInMonthLabel + "'" + key + "',";
        }
        for (Map.Entry<String, Float> entry : payOutMonthCat.entrySet()) {
            String key = entry.getKey();
            Float value = entry.getValue();
            bufferOutMonthData = bufferOutMonthData + "'" + value + "',";
            bufferOutMonthLabel = bufferOutMonthLabel + "'" + key + "',";
        }
        int inNumberItems = payInMonthCat.size();
        int outNumberItems = payOutMonthCat.size();
        String color1 = "'rgb(54, 162, 235)',";
        String color2 = "'rgb(255, 159, 64)',";
        String color3 = "'rgb(255, 205, 86)',";
        int inNumberCol = 1;
        for (int i = 0; i < inNumberItems; i++) {
            if (inNumberCol == 4) {
                inNumberCol = 1;
            }            
            if (i == inNumberItems-1 && inNumberCol != 3) {
                bufferInMonthColor = bufferInMonthColor + color2;
            } else {
                if (inNumberCol == 1) {
                    bufferInMonthColor = bufferInMonthColor + color1;
                } else if (inNumberCol == 2) {
                    bufferInMonthColor = bufferInMonthColor + color2;
                } else if (inNumberCol == 3) {
                    bufferInMonthColor = bufferInMonthColor + color3;
                }
            }
            inNumberCol++;
        }
        int outNumberCol = 1;
        for (int i = 0; i < outNumberItems; i++) {
            if (outNumberCol == 4) {
                outNumberCol = 1;
            }            
            if (i == outNumberItems-1 && outNumberCol != 3) {
                bufferOutMonthColor = bufferOutMonthColor + color2;
            } else {
                if (outNumberCol == 1) {
                    bufferOutMonthColor = bufferOutMonthColor + color1;
                } else if (outNumberCol == 2) {
                    bufferOutMonthColor = bufferOutMonthColor + color2;
                } else if (outNumberCol == 3) {
                    bufferOutMonthColor = bufferOutMonthColor + color3;
                }
            }
            outNumberCol++;
        }
        // store in local var
        this.inMonthList = payInMonth;
        this.inMonthData = bufferInMonthData;
        this.inMonthLabel = bufferInMonthLabel;
        this.inMonthColor = bufferInMonthColor;
        this.outMonthList = payOutMonth;
        this.outMonthData = bufferOutMonthData;
        this.outMonthLabel = bufferOutMonthLabel;
        this.outMonthColor = bufferOutMonthColor;
    }
    
    /*
        GETTER / SETTER
    */

    public String getInMonthData() {
        return inMonthData;
    }

    public void setInMonthData(String inMonthData) {
        this.inMonthData = inMonthData;
    }

    public String getInMonthColor() {
        return inMonthColor;
    }

    public void setInMonthColor(String inMonthColor) {
        this.inMonthColor = inMonthColor;
    }

    public String getInMonthLabel() {
        return inMonthLabel;
    }

    public void setInMonthLabel(String inMonthLabel) {
        this.inMonthLabel = inMonthLabel;
    }

    public ArrayList<Payments> getInMonthList() {
        return inMonthList;
    }

    public void setInMonthList(ArrayList<Payments> inMonthList) {
        this.inMonthList = inMonthList;
    }

    public String getOutMonthData() {
        return outMonthData;
    }

    public void setOutMonthData(String outMonthData) {
        this.outMonthData = outMonthData;
    }

    public String getOutMonthColor() {
        return outMonthColor;
    }

    public void setOutMonthColor(String outMonthColor) {
        this.outMonthColor = outMonthColor;
    }

    public String getOutMonthLabel() {
        return outMonthLabel;
    }

    public void setOutMonthLabel(String outMonthLabel) {
        this.outMonthLabel = outMonthLabel;
    }

    public ArrayList<Payments> getOutMonthList() {
        return outMonthList;
    }

    public void setOutMonthList(ArrayList<Payments> outMonthList) {
        this.outMonthList = outMonthList;
    }    
    
}
