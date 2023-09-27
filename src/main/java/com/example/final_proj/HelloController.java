package com.example.final_proj;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HelloController {
    @FXML
    private AnchorPane loginForm;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginBtn;

    @FXML
    private Button closeBtn;

    private Connection connection;
    private PreparedStatement prepare;
    private ResultSet result;

    private AlertMessage alert = new AlertMessage();

    public void exit(ActionEvent event){
        Stage stage = (Stage) closeBtn.getScene().getWindow();
        stage.close();
        System.exit(0);
    }

    public void loginAccount(){
        if(emailField.getText().isEmpty() || passwordField.getText().isEmpty()){
            alert.errorMessage("Please fill all blank fields");

        }else{
            String sql = "SELECT  * from users WHERE email = ? AND  password  = ?";
            connection = Database.connectionDB();

            try{
                prepare = connection.prepareStatement(sql);
                prepare.setString(1, emailField.getText());
                prepare.setString(2, passwordField.getText());
                result = prepare.executeQuery();

                if(result.next()){
                    //if correct credentials
                    alert.successMessage("Login Successfully");
                    //Link to dashboard
                    Parent root =  FXMLLoader.load(getClass().getResource("mainForm.fxml"));

                    Stage stage = new Stage();
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.setScene(new Scene(root));
                    stage.show();

                    //To hide login form
                    loginBtn.getScene().getWindow().hide();

                }else{
                    //if wrong username or password
                    alert.errorMessage("Incorrect Username/Password");
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}