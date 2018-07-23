
package managedBeans;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named(value = "master")
@ApplicationScoped
public class Master {
    
    String heightIncome = "250";
    String heightOutcome = "12";

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
