package Cliente;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class ClienteFTP extends JFrame {

    FTPClient ftp = new FTPClient();

    JTextField campoIP = new JTextField("127.0.0.1");
    JTextField campoPorta = new JTextField("2121");
    JTextField campoUsuario = new JTextField("admin");
    JPasswordField campoSenha = new JPasswordField("1234");

    DefaultListModel<String> modeloLista = new DefaultListModel<>();
    JList<String> lista = new JList<>(modeloLista);

    JTextArea caixaLog = new JTextArea();

    JButton conectar = new JButton("Conectar");
    JButton baixar = new JButton("Baixar");
    JButton enviar = new JButton("Enviar");
    JButton deletar = new JButton("Deletar");
    JButton atualizar = new JButton("Atualizar");
    JButton sair = new JButton("Encerrar");

    public ClienteFTP() {

        setTitle("Cliente FTP Java");
        setSize(600,500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel conexao = new JPanel(new GridLayout(4,2));

        conexao.add(new JLabel("IP"));
        conexao.add(campoIP);

        conexao.add(new JLabel("Porta"));
        conexao.add(campoPorta);

        conexao.add(new JLabel("Usuário"));
        conexao.add(campoUsuario);

        conexao.add(new JLabel("Senha"));
        conexao.add(campoSenha);

        add(conexao, BorderLayout.NORTH);

        add(new JScrollPane(lista), BorderLayout.CENTER);

        JPanel botoes = new JPanel();

        botoes.add(conectar);
        botoes.add(baixar);
        botoes.add(enviar);
        botoes.add(deletar);
        botoes.add(atualizar);
        botoes.add(sair);

        add(botoes, BorderLayout.SOUTH);

        caixaLog.setEditable(false);
        add(new JScrollPane(caixaLog), BorderLayout.EAST);

        bloquearInterface();

        conectar.addActionListener(e -> conectar());
        baixar.addActionListener(e -> baixar());
        enviar.addActionListener(e -> enviar());
        deletar.addActionListener(e -> deletar());
        atualizar.addActionListener(e -> listar());
        sair.addActionListener(e -> encerrar());

        campoSenha.addActionListener(e -> conectar());
        campoUsuario.addActionListener(e -> conectar());
    }

    void conectar() {

        try {

            String ip = campoIP.getText();
            int porta = Integer.parseInt(campoPorta.getText());

            ftp.connect(ip, porta);

            boolean login = ftp.login(
                    campoUsuario.getText(),
                    new String(campoSenha.getPassword())
            );

            if(!login){

                JOptionPane.showMessageDialog(
                        this,
                        "Usuário ou senha incorreta",
                        "Erro de Login",
                        JOptionPane.ERROR_MESSAGE
                );

                campoSenha.setText("");
                ftp.disconnect();
                return;
            }

            ftp.enterLocalPassiveMode();
            ftp.setFileType(FTP.BINARY_FILE_TYPE);

            log("Conectado ao servidor!");

            ativarInterface();

            listar();

        } catch(Exception e){

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao conectar ao servidor",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );

            log(e.getMessage());
        }

    }

    boolean servidorOnline(){

        try{

            if(!ftp.isConnected())
                return false;

            ftp.sendNoOp();
            return true;

        }catch(Exception e){
            return false;
        }
    }

    void listar(){

        if(!servidorOnline()){
            bloquearInterface();
            log("Servidor desconectado");
            return;
        }

        try{

            modeloLista.clear();

            String[] arquivos = ftp.listNames();

            if(arquivos != null){
                for(String arq : arquivos){
                    modeloLista.addElement(arq);
                }
            }

        }catch(Exception e){
            bloquearInterface();
        }

    }

    void baixar(){

        if(!servidorOnline()){
            bloquearInterface();
            return;
        }

        try{

            String arquivo = lista.getSelectedValue();

            if(arquivo == null){
                log("Selecione um arquivo");
                return;
            }

            FileDialog fd = new FileDialog(this, "Salvar Arquivo", FileDialog.SAVE);
            fd.setFile(arquivo);
            fd.setVisible(true);

            if(fd.getFile()!=null){

                String caminho = fd.getDirectory()+fd.getFile();

                FileOutputStream fos = new FileOutputStream(caminho);

                ftp.retrieveFile(arquivo, fos);

                fos.close();

                log("Arquivo baixado: "+arquivo);
            }

        }catch(Exception e){
            bloquearInterface();
            log("Conexão perdida");
        }

    }

    void enviar(){

        if(!servidorOnline()){
            bloquearInterface();
            return;
        }

        try{

            FileDialog fd = new FileDialog(this, "Selecionar Arquivo", FileDialog.LOAD);
            fd.setVisible(true);

            if(fd.getFile()!=null){

                File file = new File(fd.getDirectory(),fd.getFile());

                FileInputStream fis = new FileInputStream(file);

                ftp.storeFile(file.getName(), fis);

                fis.close();

                log("Arquivo enviado: "+file.getName());

                listar();
            }

        }catch(Exception e){
            bloquearInterface();
            log("Conexão perdida");
        }

    }

    void deletar(){

        if(!servidorOnline()){
            bloquearInterface();
            return;
        }

        try{

            String arquivo = lista.getSelectedValue();

            if(arquivo == null){
                log("Selecione um arquivo");
                return;
            }

            ftp.deleteFile(arquivo);

            log("Arquivo deletado: "+arquivo);

            listar();

        }catch(Exception e){
            bloquearInterface();
        }

    }

    void encerrar(){

        try{

            if(ftp.isConnected()){
                ftp.logout();
                ftp.disconnect();
            }

            bloquearInterface();

            log("Cliente desconectado");

        }catch(Exception e){
            log(e.getMessage());
        }

    }

    void ativarInterface(){

        baixar.setEnabled(true);
        enviar.setEnabled(true);
        deletar.setEnabled(true);
        atualizar.setEnabled(true);
    }

    void bloquearInterface(){

        baixar.setEnabled(false);
        enviar.setEnabled(false);
        deletar.setEnabled(false);
        atualizar.setEnabled(false);
    }

    void log(String msg){
        caixaLog.append(msg+"\n");
    }

    public static void main(String[] args) {

        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(Exception e){
            e.printStackTrace();
        }

        new ClienteFTP().setVisible(true);
    }
}