
package managedBeans;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Named;
import model.Category;
import model.Fixpayments;
import model.Payments;
import model.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * The Adding class includes the data which is needed for the forms and the 
 * logic which is called when the user want to add a new category, a new person
 * or a new payment.
 */
@Named(value = "adding")
@Dependent
public class Adding {
        
    // db meta
    SessionFactory sessionFactory;
    Session session;
    
    // data form
    List<Category> categoriesIncome;
    List<Category> categoriesOutcome;
    List<Person> person;
    
    /**
     * Constructor.
     */
    public Adding() {
        this.loadFormData();
    }
    
    /**
     * Loads data which is needed for the forms (categories [in/out], persons).
     */
    private void loadFormData() {
        this.connectToDB();
        this.loadCategories();
        this.loadPersons();
        this.disconnectFromDB();
    }
    
    /**
     * Connect to database using hibernate config.
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
     * Loads person data.
     * Result stored in object var 'person'.
     */
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
    
    /**
     * Loads categories data.
     * Result stored in object vars 'categoriesIncome' and 'categoriesOutcome'.
     */
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
    
    /**
     * Inserts new category into database.
     * @param name name of new category
     * @param IsIncome true if category is for income
     * @return new site
     */
    public String addCategory(String name, boolean IsIncome){        
        this.connectToDB();
        
        // create new category object 
        Category c = new Category();
        c.setIsDeleted(false);
        c.setIsIncome(IsIncome);
        c.setName(name);
        
        // insert into database
        session.beginTransaction();
        session.persist(c);
        session.getTransaction().commit();
        
        this.disconnectFromDB();
        return "index.xhtml?faces-redirect=true";
    }

    /**
     * Inserts new person into database.
     * @param name name of new person
     * @return new site
     */
    public String addPerson(String name){        
        this.connectToDB();        
        
        // create new person object
        Person p = new Person();
        p.setName(name);
        
        // insert into database
        session.beginTransaction();
        session.persist(p);
        session.getTransaction().commit();
        
        this.disconnectFromDB();
        return "index.xhtml?faces-redirect=true";
    }
    
    /**
     * Inserts new outcome payment into database.
     * @param person person which has added payment
     * @param category category of payment
     * @param amount amount of payment
     * @param note note of payment
     * @return new site
     */
    public String addPaymentOutcome(String person, String category, String amount, String note){        
        this.connectToDB();
        Payments p = new Payments();

        // assign category oject and person object to payment.
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
        p.setCategory(c);
        p.setPerson(per);
        
        // assign amount, note and timestamp to payment
        float a = Float.parseFloat(amount);
        p.setAmount(a);
        p.setNote(note);        
        Timestamp currentTimestamp = Timestamp.valueOf(LocalDateTime.now());
        p.setCreated(new Date(currentTimestamp.getTime()));
           
        // insert into database
        session.beginTransaction();
        session.save(p);
        session.getTransaction().commit();
        
        this.disconnectFromDB();
        return "index.xhtml?faces-redirect=true";
    }

    /**
     * Inserts new income payment into database.
     * @param person person which has added payment
     * @param category category of payment
     * @param amount amount of payment
     * @param note note of payment
     * @return new site
     */
    public String addPaymentIncome(String person, String category, String amount, String note) throws InterruptedException{        
        this.connectToDB();
        Payments p = new Payments();

        // assign category oject and person object to payment.
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
        p.setCategory(c);
        p.setPerson(per);
        
        // assign amount, note and timestamp to payment
        float a = Float.parseFloat(amount);
        p.setAmount(a);
        p.setNote(note);        
        Timestamp currentTimestamp = Timestamp.valueOf(LocalDateTime.now());
        p.setCreated(new Date(currentTimestamp.getTime()));
           
        // insert into database
        session.beginTransaction();
        session.save(p);
        session.getTransaction().commit();
        
        this.disconnectFromDB();
        return "index.xhtml?faces-redirect=true";
    }
    
    /**
     * Inserts new fix outcome payment into database.
     * @param person person which has added payment
     * @param category category of payment
     * @param amount amount of payment
     * @param note note of payment
     * @param regel intervall of payment
     * @return new site
     */
    public String addFixPaymentOutcome(String person, String category, String amount, String note, String regel){
        this.connectToDB();
        Fixpayments fp = new Fixpayments();
        
        // assign category oject and person object to payment.
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
        fp.setCategory(c);
        fp.setPerson(per);
        
        // assign amount, note and intervall to payment
        float a = Float.parseFloat(amount);
        fp.setAmount(a);
        fp.setNote(note);
        fp.setRegularityId(Integer.parseInt(regel));        
        Timestamp currentTimestamp = Timestamp.valueOf(LocalDateTime.now());
        fp.setCreated(new Date(currentTimestamp.getTime()));
           
        // insert into database
        session.beginTransaction();
        session.save(fp);
        session.getTransaction().commit();
        
        this.disconnectFromDB();
        return "index.xhtml?faces-redirect=true";
    }

    /**
     * Inserts new fix income payment into database.
     * @param person person which has added payment
     * @param category category of payment
     * @param amount amount of payment
     * @param note note of payment
     * @param regel intervall of payment
     * @return new site
     */
    public String addFixPaymentIncome(String person, String category, String amount, String note, String regel){
        this.connectToDB();
        Fixpayments fp = new Fixpayments();
        
        // assign category oject and person object to payment.
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
        fp.setCategory(c);
        fp.setPerson(per);
        
        // assign amount, note and intervall to payment
        float a = Float.parseFloat(amount);
        fp.setAmount(a);
        fp.setNote(note);
        fp.setRegularityId(Integer.parseInt(regel));        
        Timestamp currentTimestamp = Timestamp.valueOf(LocalDateTime.now());
        fp.setCreated(new Date(currentTimestamp.getTime()));
           
        // insert into database
        session.beginTransaction();
        session.save(fp);
        session.getTransaction().commit();
        
        this.disconnectFromDB();
        return "index.xhtml?faces-redirect=true";
    }
    
    /*
        GETTER / SETTER
    */

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
