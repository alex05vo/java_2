import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in)) {
            String userInput;

            while (true) {

                System.out.println("Введiть номер завдання (1), або 'q' для виходу:");
                userInput = scanner.nextLine();

                if (userInput.equalsIgnoreCase("q")) {
                    System.out.println("Goodbye ^_^");
                    return;
                }

                switch (userInput) {
                    case "1":
                        System.out.println("-------------------------------");
                        Hw hw = new Hw();
                        hw.Hello();
                        break;
                    default:
                        System.out.println("Неправильний ввiд. Введiть номер завдання (1) або 'q' для виходу.");
                        break;
                }
            }
        }
    }
}