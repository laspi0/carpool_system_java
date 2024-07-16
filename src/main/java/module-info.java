module com.groupeisi.rent {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.sql;
    requires java.naming;
    requires com.jfoenix;
    requires spring.security.crypto;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;
    requires java.rmi;

    opens com.groupeisi.rent to javafx.fxml;
    opens com.groupeisi.rent.controllers to javafx.fxml;  // Ajoutez cette ligne
    opens com.groupeisi.rent.controllers.user to javafx.fxml;
    opens com.groupeisi.rent.controllers.vehicle to javafx.fxml;
    opens com.groupeisi.rent.entities to org.hibernate.orm.core, javafx.base;
    opens com.groupeisi.rent.controllers.trip to javafx.fxml;

    exports com.groupeisi.rent;
    exports com.groupeisi.rent.controllers;  // Ajoutez cette ligne
    exports com.groupeisi.rent.controllers.user;
    exports com.groupeisi.rent.controllers.trip;
    exports com.groupeisi.rent.controllers.vehicle;
}