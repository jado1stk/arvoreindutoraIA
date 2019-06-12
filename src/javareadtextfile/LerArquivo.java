package javareadtextfile;
 
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
 
public class LerArquivo
{
    public String[] readLines(String nomeArquivo) throws IOException 
    {
        List<String> lista = new ArrayList<String>();
        FileReader fileReader = new FileReader(nomeArquivo);
         
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        List<String> linhas = new ArrayList<String>();
        String linha = null;
         
        while ((linha = bufferedReader.readLine()) != null) 
        {
            lista.add(linha);
        }
 
        bufferedReader.close();
   
        return lista.toArray(new String[linhas.size()]);
    }   
}