package com.wictor.controller;

import com.wictor.Security.CpfService;
import com.wictor.Security.PasswordService;
import com.wictor.dao.UserDao;
import com.wictor.model.User;
import com.wictor.util.CpfValidator;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class login_controller {
    public void initialize() {
        Txt_cpf.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                validarcpf();
            }
        });
    }

    public void validarcpf() {
        String cpf = Txt_cpf.getText();
        if (cpf.isBlank()) {
            Lbl_cpf.setVisible(true);
            Lbl_cpf.setText("Campo obrigatório");
            return;
        }
        boolean valido = CpfValidator.validar(cpf);
        Lbl_cpf.setVisible(!valido);
        Lbl_cpf.setText("Cpf inválido!");

    }


    @FXML
    private TextField Txt_cpf;
    @FXML
    private Label Lbl_cpf;
    @FXML
    private PasswordField Txt_senha;
    @FXML
    private Label Lbl_senha;
    @FXML
    private Button Btn_login;

    @FXML
    public void onBtn_login_Click() {
        String Cpf = Txt_cpf.getText();
        String Cpf_cript = CpfService.Criptografia(Cpf);
        String hash = Txt_senha.getText();

        try {

            UserDao daoLogin = new UserDao();
            User usuario = daoLogin.autenticar(Cpf_cript, hash);
            if (usuario != null){mostrarAlertCerto();}
            else{mostrarAlertErro();}

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlertErro();
        }

    }

    public void mostrarAlertCerto(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText("Login efetuado!");
        alert.showAndWait();
    }

    public void mostrarAlertErro(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText("Falha no Login");
        alert.setContentText("Cpf e/ou senha incorretos.");
        alert.showAndWait();
    }
}
