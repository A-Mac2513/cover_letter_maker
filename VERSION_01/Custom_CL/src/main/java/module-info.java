module com.andrew_macdonald.custom_cl {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires org.apache.poi.ooxml;

    opens com.andrew_macdonald.custom_cl to javafx.fxml;
    exports com.andrew_macdonald.custom_cl;
}