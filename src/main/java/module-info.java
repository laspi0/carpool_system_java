module com.groupeisi.rent {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.sql;
    requires java.naming;
    requires com.jfoenix;
    requires spring.security.crypto;

    opens com.groupeisi.rent to javafx.fxml;
    opens com.groupeisi.rent.controllers.user to javafx.fxml;
    opens com.groupeisi.rent.entities to org.hibernate.orm.core;

    exports com.groupeisi.rent;
    exports com.groupeisi.rent.controllers.user;
}
