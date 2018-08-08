
package managedBeans;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import model.Category;
import model.Payments;
import model.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@Named(value = "master")
@ApplicationScoped
public class Master {
    
    SessionFactory sessionFactory;
    Session session;
    
    ArrayList<Payments> payments;
    
    // data dashboard
    String heightIncome = "0";
    String heightOutcome = "0";
    float sumOutcome = 0;
    float sumIncome = 0;
    float balance = 0;
    String balanceCss = "";
    ArrayList<Payments> listLastTen = new ArrayList<>();
    
    // data form
    List<Category> categoriesIncome;
    List<Category> categoriesOutcome;
    List<Person> person;
    
    // data overview list
    String inMonthData = "";
    String inMonthColor = ""; //'rgb(54, 162, 235)','rgb(255, 159, 64)','rgb(255, 205, 86)'
    String inMonthLabel = "";
    ArrayList<Payments> inMonthList = new ArrayList<>();
    String outMonthData = "";
    String outMonthColor = ""; //'rgb(54, 162, 235)','rgb(255, 159, 64)','rgb(255, 205, 86)'
    String outMonthLabel = "";
    ArrayList<Payments> outMonthList = new ArrayList<>();
    
    // data overview trend
    String trendHalfyearData = ""; //{x:'März', y:-15},{x:'April', y:-111},{x:'Mai', y:20},{x:'Juni', y:70},{x:'Juli', y:2},{x:'August', y:13},{x:'September', y:13}";
    String trendHalfyearColor = "'rgb(54, 162, 235)','rgb(255, 159, 64)','rgb(255, 205, 86)','rgb(54, 162, 235)','rgb(255, 159, 64)','rgb(255, 205, 86)','rgb(54, 162, 235)','rgb(54, 162, 235)',";
    String trendHalfyearLabels = ""; //'März','April','Mai','Juni','Juli','August','September',";
    String trendHalfyearIncome = "";
    String trendHalfyearOutcome = "";
    String trendHalfyearBalance = "";
    
    public Master() {        
        
        this.payments = new ArrayList<>();
        this.updateData();
        
    }
    
    /**
     * Loads data which is needed for trend on overview page.
     */
    private void loadOverviewTrendData() {
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
    
    /**
     * Loads data which is needed for list on overview page.
     */
    private void loadOverviewListData() {        
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
    
    private void loadPersons() {
        session.beginTransaction();
        List<Person> result = session.createQuery( "from Person" ).list();
        ArrayList<Person> bufferPerson = new ArrayList<>();
        for (Person per : result) {
            if (!per.isIsDeleted()) {
                bufferPerson.add(per);
            }
        }
        this.person = bufferPerson.subList(0, bufferPerson.size());
        session.getTransaction().commit();
    }
    
    private void loadCategories() {
        session.beginTransaction();
        List<Category> result = session.createQuery( "from Category" ).list();
        ArrayList<Category> bufferIncome = new ArrayList<>();
        ArrayList<Category> bufferOutcome = new ArrayList<>();
        for (Category cat : result) {
            if (!cat.isIsDeleted()) {
                if (cat.isIsIncome()) {
                    bufferIncome.add(cat);
                } else {
                    bufferOutcome.add(cat);
                }
            }
        }
        this.categoriesIncome = bufferIncome.subList(0, bufferIncome.size());
        this.categoriesOutcome = bufferOutcome.subList(0, bufferOutcome.size());
        session.getTransaction().commit();
    }
    
    public void addCategory(String name, boolean IsIncome){
        
        this.connectToDB();
        
        
        Category c = new Category();
        c.setIsDeleted(false);
        c.setIsIncome(IsIncome);
        c.setName(name);
        
        session.beginTransaction();
        session.persist(c);
        session.getTransaction().commit();
        
        this.disconnectFromDB();
     
        this.updateData();
    }

    public void addPerson(String name){
        
        this.connectToDB();
        
        
        Person p = new Person();
        p.setName(name);
        
        session.beginTransaction();
        session.persist(p);
        session.getTransaction().commit();
        
        this.disconnectFromDB();
     
        this.updateData();
    }
    
    public void addPaymentOutcome(String person, String category, String amount, String note){
        
        this.connectToDB();

        Category c = new Category();
        Person per = new Person();
        int cat_id = Integer.parseInt(category);
        int per_id = Integer.parseInt(person);
        
        for (int i = 0; i < this.categoriesOutcome.size(); i++) {
            if (this.categoriesOutcome.get(i).getId().equals(cat_id)) {
                c = this.categoriesOutcome.get(i);
            }
        }
        
        for (int i = 0; i < this.person.size(); i++) {
            if (this.person.get(i).getId().equals(per_id)) {
                per = this.person.get(i);
            }
        }
              
        Payments p = new Payments();
        p.setCategory(c);
        p.setPerson(per);
        float a = Float.parseFloat(amount);
        p.setAmount(a);
        p.setNote(note);
        
        Timestamp currentTimestamp = Timestamp.valueOf(LocalDateTime.now());
        p.setCreated(new Date(currentTimestamp.getTime()));
           
        session.beginTransaction();
        session.save(p);
        session.getTransaction().commit();
        
        this.disconnectFromDB();
        this.updateData();
    }

    public void addPaymentIncome(String person, String category, String amount, String note){
        
        this.connectToDB();

        Category c = new Category();
        Person per = new Person();
        int cat_id = Integer.parseInt(category);
        int per_id = Integer.parseInt(person);
        
        for (int i = 0; i < this.categoriesIncome.size(); i++) {
            if (this.categoriesIncome.get(i).getId().equals(cat_id)) {
                c = this.categoriesIncome.get(i);
            }
        }
        
        for (int i = 0; i < this.person.size(); i++) {
            if (this.person.get(i).getId().equals(per_id)) {
                per = this.person.get(i);
            }
        }
              
        Payments p = new Payments();
        p.setCategory(c);
        p.setPerson(per);
        float a = Float.parseFloat(amount);
        p.setAmount(a);
        p.setNote(note);
        
        Timestamp currentTimestamp = Timestamp.valueOf(LocalDateTime.now());
        p.setCreated(new Date(currentTimestamp.getTime()));
           
        session.beginTransaction();
        session.save(p);
        session.getTransaction().commit();
        
        this.disconnectFromDB();     
        this.updateData();
    }

    /**
     * Updates data for visualisation.
     */
    private void updateData() {
        System.out.println("Start");
        this.connectToDB();       
        this.updateDashboardOverview();
        this.updateDashboardList();
        this.loadPersons();
        this.loadCategories();
        this.loadOverviewListData();
        this.loadOverviewTrendData();
        this.disconnectFromDB();
    }
    
    /**
     * Disconnect from database.
     */
    private void disconnectFromDB() {
        session.close();
    }
    
    /**
     * Connect to database.
     */
    private void connectToDB() {
        sessionFactory = new Configuration()
            .configure("/model/hibernate.cfg.xml")
            .buildSessionFactory();
        session = sessionFactory.openSession();
        session.beginTransaction();
        List result = session.createQuery( "from Payments order by created" ).list();
        this.payments = new ArrayList(result);
        session.getTransaction().commit();
    }
    
    /**
     * Updates data for list on dashboard.
     * NOTE: connection to database needed.
     */
    private void updateDashboardList() {
        // extract last 10 created data items
        ArrayList<Payments> paymentCal = new ArrayList<>();
        int i = 0;
        for (Payments currenPay : this.payments) {
            if (i == 10) {
                break;
            }
            paymentCal.add(currenPay);
            i++;
        }
        
        // prepare data for visualation
        this.listLastTen = paymentCal;
        
    }
    
    /**
     * Updates data for overview on dashboard.
     * NOTE: connection to database needed.
     */
    private void updateDashboardOverview() {        
        // extract data which was created in current month
        ArrayList<Payments> paymentsCal = new ArrayList<>();
        for (Payments payment : this.payments) {
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
        this.sumIncome = income_sum;
        this.sumOutcome = outcome_sum;
        this.balance = (income_sum-outcome_sum);
        if ((income_sum-outcome_sum) >= 0) {
            this.balanceCss = "bilanz_positiv";
        } else {
            this.balanceCss = "bilanz_negativ";
        }
        
        // calculate heights
        if (income_sum == outcome_sum) {
            heightIncome = "250";
            heightOutcome = "250";
        } else if (income_sum > outcome_sum) {
            heightIncome = "250";
            float hOut = (outcome_sum / income_sum) * 250;
            heightOutcome = "" + hOut;
            System.out.println("hout = " + hOut);
        } else if (outcome_sum > income_sum) {
            heightOutcome = "250";
            float hIn = (income_sum / outcome_sum) * 250;
            heightIncome = "" + hIn;
        }
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

    public float getSumOutcome() {
        return sumOutcome;
    }

    public void setSumOutcome(float sumOutcome) {
        this.sumOutcome = sumOutcome;
    }

    public float getSumIncome() {
        return sumIncome;
    }

    public void setSumIncome(float sumIncome) {
        this.sumIncome = sumIncome;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String getBalanceCss() {
        return balanceCss;
    }

    public void setBalanceCss(String balanceCss) {
        this.balanceCss = balanceCss;
    }

    public ArrayList<Payments> getListLastTen() {
        return listLastTen;
    }

    public void setListLastTen(ArrayList<Payments> listLastTen) {
        this.listLastTen = listLastTen;
    }

    public List<Category> getCategoriesIncome() {
        return categoriesIncome;
    }

    public void setCategoriesIncome(List<Category> categoriesIncome) {
        this.categoriesIncome = categoriesIncome;
    }

    public List<Category> getCategoriesOutcome() {
        return categoriesOutcome;
    }

    public void setCategoriesOutcome(List<Category> categoriesOutcome) {
        this.categoriesOutcome = categoriesOutcome;
    }

    public List<Person> getPerson() {
        return person;
    }

    public void setPerson(List<Person> person) {
        this.person = person;
    }

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
