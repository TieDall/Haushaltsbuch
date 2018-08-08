
package managedBeans;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    
    public Master() {        
        
        this.payments = new ArrayList<>();
        this.updateData();
        
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
   
}
