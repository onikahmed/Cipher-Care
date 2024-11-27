
import javax.swing.UIManager;

public class CipherCareMain {
    public static void main(String[] args) throws Exception {
         try {
           
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        new CipherCareLoginGUI();

    }
}
