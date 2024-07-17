package com.groupeisi.rent.controllers.user;

import com.groupeisi.rent.DAO.UserDAO;
import com.groupeisi.rent.entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Optional;

public class ManageUserController {

    @FXML private TableView<User> userTableView;
    @FXML private TableColumn<User, String> firstNameColumn;
    @FXML private TableColumn<User, String> lastNameColumn;
    @FXML private TableColumn<User, String> emailColumn;
    @FXML private TableColumn<User, String> roleColumn;
    @FXML private HBox buttonContainer;
    @FXML private VBox inputVBox;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private ComboBox<String> roleComboBox;

    private final UserDAO userDAO = new UserDAO();
    private User selectedUser;

    @FXML
    public void initialize() {
        setupTableColumns();
        setupTableView();
        createButtons();
        loadUsers();
        setupRoleComboBox();
    }

    private void setupTableColumns() {
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
    }

    private void setupTableView() {
        userTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        userTableView.setPlaceholder(new Label("Aucun utilisateur trouvé"));

        userTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedUser = newSelection;
                populateFields(newSelection);
            }
        });
    }

    private void setupRoleComboBox() {
        roleComboBox.setItems(FXCollections.observableArrayList("ADMIN", "USER", "DRIVER"));
    }

    private void createButtons() {
        Label addLabel = createStyledLabel("Ajouter", this::handleAddUser);
        Label editLabel = createStyledLabel("Modifier", this::handleEditUser);
        Label deleteLabel = createStyledLabel("Supprimer", this::handleDeleteUser);

        buttonContainer.getChildren().addAll(addLabel, editLabel, deleteLabel);
    }

    private Label createStyledLabel(String text, Runnable action) {
        Label label = new Label(text);
        label.setOnMouseClicked(event -> action.run());
        label.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 8 15 8 15; -fx-cursor: hand;");
        return label;
    }

    private void loadUsers() {
        List<User> users = userDAO.findAllExceptAdmin();
        ObservableList<User> userObservableList = FXCollections.observableArrayList(users);
        userTableView.setItems(userObservableList);
    }

    private void handleAddUser() {
        if (validateInput()) {
            User newUser = createUserFromInput();
            userDAO.save(newUser);
            showAlert("Succès", "L'utilisateur a été ajouté avec succès.");
            clearFields();
            loadUsers();
        }
    }

    private void handleEditUser() {
        if (selectedUser == null) {
            showAlert("Erreur", "Veuillez sélectionner un utilisateur à modifier.");
            return;
        }

        if (validateInput()) {
            updateSelectedUser();
            userDAO.update(selectedUser);
            showAlert("Succès", "L'utilisateur a été modifié avec succès.");
            clearFields();
            loadUsers();
        }
    }

    private void handleDeleteUser() {
        if (selectedUser == null) {
            showAlert("Erreur", "Veuillez sélectionner un utilisateur à supprimer.");
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirmation de suppression");
        confirmDialog.setHeaderText("Supprimer l'utilisateur");
        confirmDialog.setContentText("Êtes-vous sûr de vouloir supprimer l'utilisateur " + selectedUser.getFirstName() + " " + selectedUser.getLastName() + " ?");

        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean deleted = userDAO.deleteUser(selectedUser);
            if (deleted) {
                showAlert("Succès", "L'utilisateur a été supprimé avec succès.");
                clearFields();
                loadUsers();
            } else {
                showAlert("Erreur", "Une erreur est survenue lors de la suppression de l'utilisateur.");
            }
        }
    }

    private boolean validateInput() {
        if (firstNameField.getText().trim().isEmpty() || lastNameField.getText().trim().isEmpty() ||
                emailField.getText().trim().isEmpty() || roleComboBox.getValue() == null) {
            showAlert("Erreur", "Tous les champs doivent être remplis.");
            return false;
        }
        return true;
    }

    private User createUserFromInput() {
        User user = new User();
        user.setFirstName(firstNameField.getText().trim());
        user.setLastName(lastNameField.getText().trim());
        user.setEmail(emailField.getText().trim());
        user.setRole(roleComboBox.getValue());
        return user;
    }

    private void updateSelectedUser() {
        selectedUser.setFirstName(firstNameField.getText().trim());
        selectedUser.setLastName(lastNameField.getText().trim());
        selectedUser.setEmail(emailField.getText().trim());
        selectedUser.setRole(roleComboBox.getValue());
    }

    private void populateFields(User user) {
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        emailField.setText(user.getEmail());
        roleComboBox.setValue(user.getRole());
    }

    private void clearFields() {
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        roleComboBox.getSelectionModel().clearSelection();
        selectedUser = null;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}