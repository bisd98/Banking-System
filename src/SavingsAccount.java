import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SavingsAccount extends Account{
    float interestRate;
    SavingsAccount(String accountNumber, String accountType, Bank accountBank,
                   float accountResources, float interestRate) {
        super(accountNumber, accountType, accountResources, accountBank);
        this.interestRate = interestRate;
    }

    void increaseAccountResources(){
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            this.accountBank.bankResources -= this.accountResources * this.interestRate;
            this.accountResources += this.accountResources * this.interestRate;
        }, 0, 1, TimeUnit.MINUTES);
    }

}
