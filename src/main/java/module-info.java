module excel {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires component.inspector.fx;
    requires java.naming;
    requires java.desktop;
    requires jdk.jfr;

    exports excel;
    opens excel to javafx.fxml;
}