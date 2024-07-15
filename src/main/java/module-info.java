module com.groupeisi.rent {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.groupeisi.rent to javafx.fxml;
    exports com.groupeisi.rent;
}