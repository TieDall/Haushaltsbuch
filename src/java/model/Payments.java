package model;
// Generated 07.08.2018 10:24:17 by Hibernate Tools 4.3.1


import java.util.Date;

/**
 * Payments generated by hbm2java
 */
public class Payments  implements java.io.Serializable {


     private Integer id;
     private Category category;
     private Person person;
     private float amount;
     private String note;
     private Date created;

    public Payments() {
    }

	
    public Payments(Category category, Person person, float amount) {
        this.category = category;
        this.person = person;
        this.amount = amount;
    }
    public Payments(Category category, Person person, float amount, String note, Date created) {
       this.category = category;
       this.person = person;
       this.amount = amount;
       this.note = note;
       this.created = created;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Category getCategory() {
        return this.category;
    }
    
    public void setCategory(Category category) {
        this.category = category;
    }
    public Person getPerson() {
        return this.person;
    }
    
    public void setPerson(Person person) {
        this.person = person;
    }
    public float getAmount() {
        return this.amount;
    }
    
    public void setAmount(float amount) {
        this.amount = amount;
    }
    public String getNote() {
        return this.note;
    }
    
    public void setNote(String note) {
        this.note = note;
    }
    public Date getCreated() {
        return this.created;
    }
    
    public void setCreated(Date created) {
        this.created = created;
    }




}


