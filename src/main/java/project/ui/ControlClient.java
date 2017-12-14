package project.ui;

public class ControlClient {
  public static void main(String[] args) {
    try {
      for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
        .getInstalledLookAndFeels()) {
        if ("Nimbus".equals(info.getName())) {
          javax.swing.UIManager.setLookAndFeel(info.getClassName());
          break;
        }
      }
    } catch (Exception ex) {
      java.util.logging.Logger.getLogger(ControlClient.class.getName())
        .log(java.util.logging.Level.SEVERE, null, ex);
    }

    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        AirConditional airConditional = new AirConditional();
        Light light = new Light();
        airConditional.setVisible(true);
        light.setVisible(true);
        Controller controller = new Controller(airConditional);
        controller.setVisible(true);
        new Door(controller, light, airConditional).setVisible(true);
      }
    });
  }
}
