package com.example.final_proj;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.w3c.dom.events.MouseEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class AddExpenses implements Initializable {

    @FXML
    private AnchorPane addExpenses;

    @FXML
    private TextField descField;

    @FXML
    private ComboBox<?> categoryField;

    @FXML
    private DatePicker dateField;

    @FXML
    private TextField amountField;

    @FXML
    private Button addBtn;

    @FXML
    private Button cancelBtn;

    private Connection connection;
    private PreparedStatement prepare;
    private ResultSet result;
    private Statement statement;
    private boolean update = false;

    private int expenseId;

    private Alert alert;

    public ObservableList<Expenses> addExpensesListData(){

        ObservableList<Expenses> listData = FXCollections.observableArrayList();
        String sql = "SELECT * from expenses";
        connection = Database.connectionDB();

        try{
            prepare = connection.prepareStatement(sql);
            result  = prepare.executeQuery();
            Expenses expenses;

            while(result.next()){
                expenses = new Expenses(
                        result.getInt("id"),result.getString("category"),
                        result.getDate("date"),
                        result.getString("description"),
                        result.getDouble("amount"));

                listData.add(expenses);
            }
        }catch (Exception e){
            e.printStackTrace();

        }
        return listData;
    }

    private ObservableList<Expenses> addExpensesList;
    public void addExpensesShowListData(){
        addExpensesList = addExpensesListData();
        
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
