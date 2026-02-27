package com.wictor.controller;

import com.wictor.util.AgeValidator;
import com.wictor.util.CpfValidator;
import com.wictor.util.EmailValidator;
import com.wictor.util.NumberValidator;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;

public class user_controller {

    public void initialize() {
        Txt_cpf.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                validarcpf();
            }
        });
        Txt_tel1.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                validartel1();
            }
        });
        Txt_tel2.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                validartel2();
            }
        });
        Txt_email1.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                validaremail1();
            }
        });
        Txt_email2.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                validaremail2();
            }
        });
        Dt_datanasc.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                validardatanasc();
            }
        });
    }

    public void validarcpf() {
        String cpf = Txt_cpf.getText();
        if (cpf.isBlank()){Lbl_cpf.setVisible(false); return;}
        boolean valido = CpfValidator.validar(cpf);
        Lbl_cpf.setVisible(!valido);
    }

    public void validartel1() {
        String tel1 = Txt_tel1.getText();
        if (tel1.isBlank()){Lbl_tel1.setVisible(false); return;}
        boolean valido = NumberValidator.validar(tel1);
        Lbl_tel1.setVisible(!valido);
    }

    public void validartel2() {
        String tel2 = Txt_tel2.getText();
        if (tel2.isBlank()){Lbl_tel2.setVisible(false); return;}
        boolean valido = NumberValidator.validar(tel2);
        Lbl_tel2.setVisible(!valido);
    }

    public void validaremail1() {
        String email1 = Txt_email1.getText();
        if (email1.isBlank()){Lbl_email1.setVisible(false); return;}
        boolean valido = EmailValidator.validar(email1);
        Lbl_email1.setVisible(!valido);
    }

    public void validaremail2() {
        String email2 = Txt_email2.getText();
        if (email2.isBlank()){Lbl_email2.setVisible(false); return;}
        boolean valido = EmailValidator.validar(email2);
        Lbl_email2.setVisible(!valido);
    }

    public void validardatanasc() {
        LocalDate datanasc = Dt_datanasc.getValue();
        if (datanasc == null){Lbl_datanasc.setVisible(false); return;}
        boolean valido = AgeValidator.validar(datanasc);
        Lbl_datanasc.setVisible(!valido);
    }


    @FXML
    private TextField Txt_cpf;
    @FXML
    private TextField Txt_senha;
    @FXML
    private TextField Txt_nome;
    @FXML
    private TextField Txt_email1;
    @FXML
    private TextField Txt_tel1;
    @FXML
    private TextField Txt_email2;
    @FXML
    private TextField Txt_tel2;
    @FXML
    private TextField Txt_cep;
    @FXML
    private TextField Txt_bairro;
    @FXML
    private TextField Txt_rua;
    @FXML
    private TextField Txt_num;
    @FXML
    private TextField Txt_comp;
    @FXML
    private DatePicker Dt_datanasc;
    @FXML
    private RadioButton RadB_feminino;
    @FXML
    private RadioButton RadB_masculino;
    @FXML
    private Button Btn_img;
    @FXML
    private Button Btn_cad;
    @FXML
    private Label Lbl_cpf;
    @FXML
    private Label Lbl_tel1;
    @FXML
    private Label Lbl_tel2;
    @FXML
    private Label Lbl_email1;
    @FXML
    private Label Lbl_email2;
    @FXML
    private Label Lbl_datanasc;
    @FXML
    private ToggleGroup Group_sexo;

    @FXML
    public void onBtn_cad_Click() {
        boolean preenchido = preencimento();
        if (!preenchido) {

            Lbl_cpf.setVisible(true);

        }else{
            Lbl_cpf.setVisible(false);
        }

    }

    public boolean preencimento(){
        if (Txt_cpf.getText().isBlank()) return false;
        if (Txt_senha.getText().isBlank()) return false;
        if (Txt_nome.getText().isBlank()) return false;
        if (Txt_email1.getText().isBlank()) return false;
        if (Txt_tel1.getText().isBlank()) return false;
        if (Txt_cep.getText().isBlank()) return false;
        if (Txt_bairro.getText().isBlank()) return false;
        if (Txt_rua.getText().isBlank()) return false;
        if (Txt_num.getText().isBlank()) return false;
        if (Dt_datanasc.getValue() == null) return false;
        if (Group_sexo.getSelectedToggle() == null) return false;

        return true;

    }


}
