package com.wictor.controller;

import com.wictor.dao.UserDao;
import com.wictor.model.User;
import com.wictor.util.AgeValidator;
import com.wictor.util.CpfValidator;
import com.wictor.util.EmailValidator;
import com.wictor.util.NumberValidator;
import com.wictor.Security.PasswordService;
import com.wictor.Security.CpfService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
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
        Txt_senha.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                Lbl_senha.setVisible(Txt_senha.getText().isBlank());
            }
        });
        Txt_nome.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                Lbl_nome.setVisible(Txt_nome.getText().isBlank());
            }
        });
        Txt_cep.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                Lbl_cep.setVisible(Txt_cep.getText().isBlank());
            }
        });
        Txt_bairro.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                Lbl_bairro.setVisible(Txt_bairro.getText().isBlank());
            }
        });
        Txt_rua.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                Lbl_rua.setVisible(Txt_rua.getText().isBlank());
            }
        });
        Txt_num.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                Lbl_num.setVisible(Txt_num.getText().isBlank());
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

    public void validartel1() {
        String tel1 = Txt_tel1.getText();
        if (tel1.isBlank()) {
            Lbl_tel1.setVisible(true);
            Lbl_tel1.setText("Campo obrigatório");
            return;
        }
        boolean valido = NumberValidator.validar(tel1);
        Lbl_tel1.setVisible(!valido);
        Lbl_tel1.setText("Telefone inválido!");
    }

    public void validartel2() {
        String tel2 = Txt_tel2.getText();
        if (tel2.isBlank()) {
            return;
        }
        boolean valido = NumberValidator.validar(tel2);
        Lbl_tel2.setVisible(!valido);
        Lbl_tel2.setText("Telefone inválido!");
    }

    public void validaremail1() {
        String email1 = Txt_email1.getText();
        if (email1.isBlank()) {
            Lbl_email1.setVisible(true);
            Lbl_tel2.setText("Campo obrigatório");
            return;
        }
        boolean valido = EmailValidator.validar(email1);
        Lbl_email1.setVisible(!valido);
        Lbl_email1.setText("Email inválido!");
    }

    public void validaremail2() {
        String email2 = Txt_email2.getText();
        if (email2.isBlank()) {
            return;
        }
        boolean valido = EmailValidator.validar(email2);
        Lbl_email2.setVisible(!valido);
        Lbl_email2.setText("Email inválido!");
    }

    public void validardatanasc() {
        LocalDate datanasc = Dt_datanasc.getValue();
        if (datanasc == null) {
            Lbl_datanasc.setVisible(true);
            Lbl_datanasc.setText("Campo obrigatório");
            return;
        }
        boolean valido = AgeValidator.validar(datanasc);
        Lbl_datanasc.setVisible(!valido);
        Lbl_datanasc.setText("Data de nascimento inválida!");
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
    private Label Lbl_senha;
    @FXML
    private Label Lbl_nome;
    @FXML
    private Label Lbl_cep;
    @FXML
    private Label Lbl_bairro;
    @FXML
    private Label Lbl_rua;
    @FXML
    private Label Lbl_num;
    @FXML
    private Label Lbl_sexo;
    @FXML
    private ToggleGroup Group_sexo;
    @FXML
    private ImageView ImgV_foto;
    private File foto;

    @FXML
    public void onBtn_cad_Click() {

        if (!preencimento()) return;
        String sexo;
        User cadUser = new User();
        String Cpf = Txt_cpf.getText();
        String Cpf_crpit = CpfService.Criptografia(Cpf);
        cadUser.setCpf(Cpf_crpit);
        String Senha = Txt_senha.getText();
        String hash = PasswordService.Criptografia(Senha);
        cadUser.setSenha(hash);
        cadUser.setNome(Txt_nome.getText());
        cadUser.setDatanasc(Dt_datanasc.getValue());
        cadUser.setEmail1(Txt_email1.getText());
        cadUser.setEmail2(Txt_email2.getText());
        cadUser.setTel1(Txt_tel1.getText());
        cadUser.setTel2(Txt_tel2.getText());
        cadUser.setCep(Txt_cep.getText());
        cadUser.setBairro(Txt_bairro.getText());
        cadUser.setRua(Txt_rua.getText());
        cadUser.setNum(Txt_num.getText());
        cadUser.setComp(Txt_comp.getText());
        cadUser.setAtivo(true);
        if (RadB_masculino.isSelected()) {
            sexo = "M";
        } else {
            sexo = "F";
        }
        cadUser.setSexo(sexo);
        try {
            UserDao dao = new UserDao();
            int idGerado = dao.insert(cadUser);
            cadUser.setId(idGerado);
            mostrarAlertCerto();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlertErro();
        }

    }

    @FXML
    public void onBtn_img_Click() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imagens", "*.png", "*.jpg", "*.jpeg")
        );
        Stage stage = (Stage) Btn_img.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            foto = file;
            Image image = new Image(file.toURI().toString());
            ImgV_foto.setImage(image);
        }
    }

    public boolean preencimento() {
        if (Txt_cpf.getText().isBlank()) {
            Lbl_cpf.setText("Campo obrigatório");
            Lbl_cpf.setVisible(true);
            return false;
        }
        if (Txt_senha.getText().isBlank()) {
            Lbl_senha.setVisible(true);
            return false;
        }
        if (Txt_nome.getText().isBlank()) {
            Lbl_nome.setVisible(true);
            return false;
        }
        if (Txt_email1.getText().isBlank()) {
            Lbl_email1.setText("Campo obrigatório");
            Lbl_email1.setVisible(true);
            return false;
        }
        if (Txt_tel1.getText().isBlank()) {
            Lbl_tel1.setText("Campo obrigatório");
            Lbl_tel1.setVisible(true);
            return false;
        }
        if (Txt_cep.getText().isBlank()) {
            Lbl_cep.setVisible(true);
            return false;
        }
        if (Txt_bairro.getText().isBlank()) {
            Lbl_bairro.setVisible(true);
            return false;
        }
        if (Txt_rua.getText().isBlank()) {
            Lbl_rua.setVisible(true);
            return false;
        }
        if (Txt_num.getText().isBlank()) {
            Lbl_num.setVisible(true);
            return false;
        }
        if (Dt_datanasc.getValue() == null) {
            Lbl_datanasc.setText("Campo obrigatório");
            Lbl_datanasc.setVisible(true);
            return false;
        }
        if (Group_sexo.getSelectedToggle() == null) {
            Lbl_sexo.setVisible(true);
            return false;
        }
        return true;
    }

    public void mostrarAlertCerto(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText("Usuário cadastrado com sucesso!");
        alert.showAndWait();
    }

    public void mostrarAlertErro(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText("Falha no cadastro");
        alert.setContentText("Não foi possível cadastrar o usuário.");
        alert.showAndWait();
    }


}
