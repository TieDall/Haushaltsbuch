
package model;

public class Income {
    
    CategoryIN category;
    double account;
    Person person;
    String note;
    
    

    public CategoryIN getCategory() {
        return category;
    }

    public void setCategory(CategoryIN category) {
        this.category = category;
    }

    public double getAccount() {
        return account;
    }

    public void setAccount(double account) {
        this.account = account;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
    
}
