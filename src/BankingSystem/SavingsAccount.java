package BankingSystem;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SavingsAccount extends Account{
    float interestRate;
    SavingsAccount(String accountNumber, String accountType, Bank accountBank,
                   float accountResources, float interestRate) {
        super(accountNumber, accountType, accountResources, accountBank, null);
        this.interestRate = interestRate;
    }

    void increaseAccountResources(){
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            this.accountBank.bankResources -= this.accountResources * this.interestRate;
            this.accountResources += this.accountResources * this.interestRate;
            Main.bankDataBase.updateResourcesOfSavingAcc(this.accountNumber, this.accountResources);
        }, 0, 5, TimeUnit.MINUTES);
    }

}
