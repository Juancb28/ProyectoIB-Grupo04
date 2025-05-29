module ec.edu.epn {
    requires javafx.controls;
    requires javafx.graphics;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;

    exports ec.edu.epn;
    exports ec.edu.epn.controller;
    

    opens ec.edu.epn.controller to javafx.fxml;
    opens ec.edu.epn.view to javafx.fxml;
}
