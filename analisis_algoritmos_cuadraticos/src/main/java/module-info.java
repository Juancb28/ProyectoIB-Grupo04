module ec.edu.epn {
    requires javafx.controls;
    requires javafx.fxml;

    opens ec.edu.epn to javafx.fxml;
    exports ec.edu.epn;
}
