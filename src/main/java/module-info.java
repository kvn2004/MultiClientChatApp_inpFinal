module lk.ijse.chattry2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;


    opens lk.ijse.chattry2 to javafx.fxml, javafx.graphics;
    exports lk.ijse.chattry2;


    opens lk.ijse.chattry2.Controller to javafx.fxml;
    exports lk.ijse.chattry2.Controller;
}
