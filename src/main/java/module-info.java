module hunter.wotherspoon {
    requires javafx.controls;
    requires javafx.fxml;

    opens hunter.wotherspoon to javafx.fxml;
    exports hunter.wotherspoon;
}
