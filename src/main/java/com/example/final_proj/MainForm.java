package com.example.final_proj;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.w3c.dom.events.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainForm implements Initializable {

    @FXML
    private Button dashboardBtn;

    @FXML
    private Button expensesBtn;

    @FXML
    private Button logoutBtn;

    @FXML
    private AnchorPane dashboardForm;

    @FXML
    private BarChart<?, ?> expensesBar;

    @FXML
    private AnchorPane expensesForm;

    @FXML
    private TableView<Expenses> expensesTable;

    @FXML
    private TableColumn<Expenses, Date> col_Date;

    @FXML
    private TableColumn<Expenses, String> col_Category;

    @FXML
    private TableColumn<Expenses, String> col_Desc;

    @FXML
    private TableColumn<Expenses, String> col_Amount;

    @FXML
    private TableColumn<Expenses, Expenses> col_Action;

    @FXML
    private TextField searchField;

    @FXML
    private Button addExpenses;

    @FXML
    private Button updateExpenses;

    @FXML
    private Button deleteExpenses;

    @FXML
    private ComboBox<String> categoryField;

    @FXML
    private TextField descField;

    @FXML
    private DatePicker dateField;

    @FXML
    private TextField amountField;

    @FXML
    private Label totalExpenses;

    String query = null;
    Connection connection;
    PreparedStatement prepare;
    Statement statement;

    ResultSet resultSet;

    public Alert alert;


    public void close(){
        System.exit(0);
    }
    public void logout(){
        try{
            alert = new Alert (Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to log out?");
            Optional<ButtonType> option = alert.showAndWait();


            if(option.get().equals(ButtonType.OK)){
                logoutBtn.getScene().getWindow().hide();
                //you will be redirected to the login fxml
                Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setScene(scene);
                stage.show();

            }else return;

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void switchForm(ActionEvent event){
        if(event.getSource() == dashboardBtn){
            dashboardForm.setVisible(true);
            expensesForm.setVisible(false);
            ttlExpenses();

        }else if(event.getSource() == expensesBtn){
            dashboardForm.setVisible(false);
            expensesForm.setVisible(true);
            addCategoryListType();
            addExpensesShowListData();
            clear();
        }
    }

  private String [] categoryList = {"Transportation", "Travel", "Internet", "Electricity", "Shopping","Grocery", "Others"};
public void addCategoryListType() {
    List<String> listC = new ArrayList<>();
    for (String data : categoryList) {
        listC.add(data);
    }
    ObservableList<String> listData = FXCollections.observableArrayList(listC);
    categoryField.setItems(listData);
}


    public void addExpensesAdd(){
        String sql = "INSERT INTO expenses " + "(category, date, description, amount )"
                + "values (?, ? ,? ,?)";
        connection = Database.connectionDB();

        try{
            if(categoryField.getSelectionModel().getSelectedItem() == null
                    || dateField.getValue() == null
                    || descField.getText().isEmpty()
                    || amountField.getText().isEmpty()){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText("");
                alert.setContentText("Please all the blank fields");
                alert.showAndWait();

            }else{
                LocalDate date  = dateField.getValue();
               prepare = connection.prepareStatement(sql);
               prepare.setString(1, categoryField.getSelectionModel().getSelectedItem());
               prepare.setString(2, String.valueOf(date));
               prepare.setString(3, descField.getText());
               prepare.setString(4, amountField.getText());
               prepare.executeUpdate();
               addExpensesShowListData();
               clear();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void clear(){
        categoryField.getSelectionModel().clearSelection();
        dateField.setValue(null);
        descField.setText(null);
        amountField.setText(null);

    }

    public ObservableList<Expenses> addExpensesListData(){
        ObservableList<Expenses> expensesList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM `expenses`";
        connection = Database.connectionDB();

        try{
            prepare = connection.prepareStatement(sql);
            resultSet = prepare.executeQuery();
            Expenses exp;

            while(resultSet.next()){
                exp = new Expenses(resultSet.getInt("id"),
                        resultSet.getString("category"),
                        resultSet.getDate("date"),
                        resultSet.getString("description"),
                        resultSet.getDouble("amount"));

                expensesList.add(exp);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return expensesList;
    }
    private ObservableList<Expenses>  addExpensesList;

    public void addExpensesShowListData(){
        addExpensesList = addExpensesListData();
        col_Date.setCellValueFactory(new PropertyValueFactory<>("date"));
        col_Category.setCellValueFactory(new PropertyValueFactory<>("category"));
        col_Desc.setCellValueFactory(new PropertyValueFactory<>("description"));
        col_Amount.setCellValueFactory(new PropertyValueFactory<>("amount"));


        expensesTable.setItems(addExpensesList);
    }

    public void selectExpenses(){
        Expenses exp = expensesTable.getSelectionModel().getSelectedItem();
        int num = expensesTable.getSelectionModel().getSelectedIndex();

        if (num < 0) {
            return;
        }
        categoryField.getSelectionModel().select(exp.getCategory());
        dateField.setValue(exp.getDate().toLocalDate());
        descField.setText(exp.getDescription());
        amountField.setText(Double.toString(exp.getAmount()));
    }
    public void updateExpenses(){
        Expenses selectedExpense = expensesTable.getSelectionModel().getSelectedItem();

        if(selectedExpense == null){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("No selected Expenses");
            alert.showAndWait();
            return;
        }

        query =  "UPDATE expenses SET category = '"
                +categoryField.getSelectionModel().getSelectedItem()
                +"', date = '"+dateField.getValue()
                +"', description = '"+ descField.getText()
                +"',amount = '"+ amountField.getText()
                +"' WHERE expenses.id ="+ selectedExpense.getId();
        connection = Database.connectionDB();

        try{
            if(categoryField.getSelectionModel().getSelectedItem()==null ||
                    dateField.getValue()==null ||
                    descField.getText().isEmpty() ||
                    amountField.getText().isEmpty()){
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Please fill out all fields");
                    alert.showAndWait();

            }else{
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to update this expenses?");

                Optional<ButtonType> option = alert.showAndWait();

                if(option.get().equals(ButtonType.OK)){
                    statement = connection.createStatement();
                    statement.executeUpdate(query);
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Expenses Updated Successfully!");
                    alert.showAndWait();
                    addExpensesShowListData();
                    clear();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public void deleteExpenses(){
        Expenses selectedExpense = expensesTable.getSelectionModel().getSelectedItem();
        query = "DELETE FROM expenses where id = '"+ selectedExpense.getId()+"'";
        connection = Database.connectionDB();

        try {

            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this expenses?");

            Optional<ButtonType> option = alert.showAndWait();

            if(option.get().equals(ButtonType.OK)){
                statement = connection.createStatement();
                statement.executeUpdate(query);
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Expenses Updated Successfully!");
                alert.showAndWait();
                addExpensesShowListData();
                clear();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void search(){
        FilteredList<Expenses> filter = new FilteredList<>(addExpensesList, e->true);
        searchField.textProperty().addListener((Observable, OldValue, newValue)->{
            filter.setPredicate(predicateExpenseData->{
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }
                String searchKey = newValue.toLowerCase();

                if(predicateExpenseData.getCategory().toLowerCase().contains(searchKey)){
                    return true;
                }else if(predicateExpenseData.getDescription().toLowerCase().contains(searchKey)){
                    return true;
                }else return false;
            });
        });

        SortedList<Expenses> sortedList = new SortedList<>(filter);
        sortedList.comparatorProperty().bind(expensesTable.comparatorProperty());
        expensesTable.setItems(sortedList);
    }


       public void ttlExpenses(){
        query = "SELECT SUM(amount) from expenses";

        connection = Database.connectionDB();
        double total = 0;
        try{
            prepare = connection.prepareStatement(query);
            resultSet = prepare.executeQuery();

            while(resultSet.next()){
                total = resultSet.getDouble("SUM(amount)");
            }
            totalExpenses.setText(String.valueOf("₱ "+total));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        addCategoryListType();
        addExpensesShowListData();
        clear();
       ttlExpenses();
    }

}
