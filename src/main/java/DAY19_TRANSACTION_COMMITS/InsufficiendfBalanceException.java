package DAY19_TRANSACTION_COMMITS;

public class InsufficiendfBalanceException extends Exception{
    double amount;
    InsufficiendfBalanceException(double amount){
        super("Garreb tere pas "+amount+" itne bhi paise nhi hai");
    }
}
