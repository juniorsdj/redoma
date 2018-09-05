package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import view.Tela_Resumo;

public class Arquivo {

    //retorna o diretorio criado
    private File arquivo, diretorio;

    public Arquivo() {
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
    }

    //criando arquivo no diretorio
    public void criarArquivoTxt(String nomeDoArquivo) {
        //se o diretorio ainda nao foi criado
        File diretorio = getDiretorio();
        File arquivo = new File(diretorio, nomeDoArquivo + ".txt");
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

//    inserir formatado no arquivo txt
    public void printWriter(File arquivo, List<Object> lista, int contador) {
        List<String> selectAtual = (List<String>) lista.get(contador);
        try (FileWriter fw = new FileWriter(arquivo, true);
                PrintWriter gravarArquivo = new PrintWriter(fw)) {
            for (String linha : selectAtual) { // dá "warning: [unchecked] unchecked cast"
                gravarArquivo.printf(linha+"%n");
            }
            //pular uma linha
            gravarArquivo.printf("%n");
            
            fw.flush();
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(Tela_Resumo.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void excluirArquivo(File arquivo) {
        arquivo.delete();
    }
}
