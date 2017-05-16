import jade.Boot;

/**
 * Created by piotr on 16.05.17.
 */
public class Main {
    public static void main(String[] args) {
        String[] arguments = new String[] {
                "-gui",
                "Product1:examples.Product"
        };

        Boot.main(arguments);
    }
}
