package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import view.Tela_Resumo;

public class Arquivo {

    //retorna o diretorio criado
    private File arquivo, diretorio;

    public Arquivo() {
//        File diretorio = new File("C:/Redoma");
    }

    public File getArquivo() {
        return arquivo;
    }

    public void setArquivo(File arquivo) {
        this.arquivo = arquivo;
    }

    public File getDiretorio() {
        return diretorio;
    }

    public void setDiretorio(File diretorio) {
        this.diretorio = diretorio;
    }

    public void criarDiretorio() {
        //criando diretorio
        File diretorio = new File("C:/Redoma");
        try {
            diretorio.mkdir();//comando para criar diretorio     
            System.out.println("Diretorio criado com sucesso!");
            setDiretorio(diretorio);
            System.out.println("O diretorio foi criado em-->" + getDiretorio().getAbsolutePath());
        } catch (Exception e) {
            System.out.println("O diretorio nao foi criado" + e);
        }

//        setDiretorioCriado(false);
//        setDiretorio(novoDiretorio);
        
    }

    //criando arquivo no diretorio
    public void criarArquivoTxt(String nomeDoArquivo) {
        //se o diretorio ainda nao foi criado
        File diretorio = getDiretorio();
//        boolean arquivoFoiCriado = false;
        //o tipo e txt
        File arquivo = new File(diretorio, nomeDoArquivo+".txt");
        try {
            arquivo.createNewFile();
            System.out.println("Arquivo criado com sucesso!");
            setArquivo(arquivo);
            System.out.println("O arquivo foi criado em-->" + getArquivo().getAbsolutePath());
        } catch (IOException ex) {
            System.out.println("Erro o arquivo nao foi criado" + ex);
            Logger.getLogger(Tela_Resumo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    //salva o conteudo do select no arquivo
    public void salvarNoTxt(String linha, File arquivo) {
        //append true
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(arquivo, true))) {
            bw.write(linha);
            bw.newLine();
            bw.flush();//pegar toda a string do tunelamento
            bw.close();

        } catch (IOException ex) {
            Logger.getLogger(Tela_Resumo.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void excluirArquivo(File arquivo) {
        arquivo.delete();
    }
}
