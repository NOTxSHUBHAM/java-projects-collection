import java.util.Scanner;

// ───── Account class (Encapsulation) ─────
class BankAccount {
    private String owner;
    private String accountNo;
    private double balance;

    public BankAccount(String owner, String accountNo, double initialBalance) {
        this.owner     = owner;
        this.accountNo = accountNo;
        this.balance   = initialBalance;
    }

    public void deposit(double amount) {
        if (amount <= 0) { System.out.println("Deposit amount must be positive."); return; }
        balance += amount;
        System.out.printf("Deposited Rs. %.2f | New Balance: Rs. %.2f%n", amount, balance);
    }

    public void withdraw(double amount) {
        if (amount <= 0)        { System.out.println("Withdrawal amount must be positive."); return; }
        if (amount > balance)   { System.out.println("Insufficient funds!"); return; }
        balance -= amount;
        System.out.printf("Withdrawn Rs. %.2f | New Balance: Rs. %.2f%n", amount, balance);
    }

    public void checkBalance() {
        System.out.println("\n--- Account Details ---");
        System.out.println("Owner     : " + owner);
        System.out.println("Account No: " + accountNo);
        System.out.printf("Balance   : Rs. %.2f%n", balance);
    }

    public double getBalance() { return balance; }
}

// ───── Main App ─────
public class BankingApplication {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("===== Simple Banking Application =====");
        System.out.print("Enter account holder name: ");
        String name = sc.nextLine();
        System.out.print("Enter account number: ");
        String accNo = sc.nextLine();
        System.out.print("Enter initial deposit: Rs. ");
        double initial = sc.nextDouble();

        BankAccount account = new BankAccount(name, accNo, initial);
        System.out.println("Account created successfully!\n");

        boolean running = true;
        while (running) {
            System.out.println("\n1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Check Balance");
            System.out.println("4. Exit");
            System.out.print("Choose option: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter deposit amount: Rs. ");
                    account.deposit(sc.nextDouble()); break;
                case 2:
                    System.out.print("Enter withdrawal amount: Rs. ");
                    account.withdraw(sc.nextDouble()); break;
                case 3:
                    account.checkBalance(); break;
                case 4:
                    running = false;
                    System.out.println("Thank you for banking with us!"); break;
                default:
                    System.out.println("Invalid option.");
            }
        }
        sc.close();
    }
}
