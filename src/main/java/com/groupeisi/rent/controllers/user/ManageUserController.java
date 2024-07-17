package com.groupeisi.rent.controllers.user;

import com.groupeisi.rent.DAO.UserDAO;
import com.groupeisi.rent.entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.util.List;
import java.util.Optional;

public class ManageUserController {

    @FXML
    private TableView<User> userTableView;

    @FXML
    private TableColumn<User, String> firstNameColumn;
    @FXML
    private TableColumn<User, String> lastNameColumn;
    @FXML
    private TableColumn<User, String> emailColumn;
    @FXML
    private TableColumn<User, String> roleColumn;

    @FXML
    private HBox buttonContainer;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    public void initialize() {
        setupTableColumns();
        setupTableView();
        createButtons();
        loadUsers();
    }

    private void setupTableColumns() {
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
    }

    private void setupTableView() {
        userTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        userTableView.setPlaceholder(new Label("Aucun utilisateur trouvé"));

        userTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                System.out.println("Utilisateur sélectionné : " + newSelection.getFirstName() + " " + newSelection.getLastName());
            }
        });
    }

    private void createButtons() {
        Label addLabel = new Label("Ajouter");
        addLabel.setOnMouseClicked(event -> handleAddUser());

        Label editLabel = new Label("Modifier");
        editLabel.setOnMouseClicked(event -> handleEditUser());

        Label deleteLabel = new Label("Supprimer");
        deleteLabel.setOnMouseClicked(event -> handleDeleteUser());

        buttonContainer.getChildren().addAll(addLabel, editLabel, deleteLabel);

        // Style pour les labels
        String labelStyle = "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8 15 8 15; -fx-cursor: hand;";
        addLabel.setStyle(labelStyle);
        editLabel.setStyle(labelStyle);
        deleteLabel.setStyle(labelStyle);
    }

    private void loadUsers() {
        List<User> users = userDAO.findAllExceptAdmin();
        ObservableList<User> userObservableList = FXCollections.observableArrayList(users);
        userTableView.setItems(userObservableList);
    }

    private void handleAddUser() {
        System.out.println("Ajouter un utilisateur");
    }

    private void handleEditUser() {
        User selectedUser = userTableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            System.out.println("Modifier l'utilisateur : " + selectedUser.getFirstName());
        } else {
            showAlert("Aucun utilisateur sélectionné", "Veuillez sélectionner un utilisateur à modifier.");
        }
    }
    private void handleDeleteUser() {
        User selectedUser = userTableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Confirmation de suppression");
            confirmDialog.setHeaderText("Supprimer l'utilisateur");
            confirmDialog.setContentText("Êtes-vous sûr de vouloir supprimer l'utilisateur " + selectedUser.getFirstName() + " " + selectedUser.getLastName() + " ?");

            Optional<ButtonType> result = confirmDialog.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                boolean deleted = userDAO.deleteUser(selectedUser);
                if (deleted) {
                    showAlert("Suppression réussie", "L'utilisateur a été supprimé avec succès.");
                    loadUsers();
                } else {
                    showAlert("Erreur de suppression", "Une erreur est survenue lors de la suppression de l'utilisateur.");
                }
            }
        } else {
            showAlert("Aucun utilisateur sélectionné", "Veuillez sélectionner un utilisateur à supprimer.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}