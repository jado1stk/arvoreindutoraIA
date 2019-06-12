package projetoia2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProjetoIA2 {

    public static int countWords(String s){

        int contaPalavras = 0;

        boolean palavra = false;
        int endOfLine = s.length() - 1;

        for (int i = 0; i < s.length(); i++) {
            // if the char is a letter, word = true.
            if (Character.isLetter(s.charAt(i)) || Character.isDigit(s.charAt(i)) && i != endOfLine) {
                palavra = true;
                // if char isn't a letter and there have been letters before,
                // counter goes up.
            } else if (!Character.isLetter(s.charAt(i)) || Character.isDigit(s.charAt(i)) && palavra) {
                contaPalavras++;
                palavra = false;
                // last word of String; if it doesn't end with a non letter, it
                // wouldn't count without this.
            } else if (Character.isLetter(s.charAt(i)) || Character.isDigit(s.charAt(i)) && i == endOfLine) {
                contaPalavras++;
            }
        }
        return contaPalavras;
    }
    
    public static double calculoImpureza(double qtd0, double qtd1, int opcao)
    {
        double qtdInstancias = qtd1 + qtd0;
        double impureza = 0;
        switch(opcao)
        {
        //Calculo Gini
            case 2:
            impureza = (1 - (Math.pow( (double)qtd1/qtdInstancias, 2.0) + Math.pow( (double)qtd0/qtdInstancias, 2.0)));    
            System.out.println("Gini: " + impureza);
            break;

        //Calculo Entropia
            case 3:
                if(qtd0 == 0 || qtd1 == 0)
                {
                    impureza = 0;
                }
                else
                {
                    double log1 = ((double) (qtd1/qtdInstancias) * (Math.log( (double) (qtd1/qtdInstancias))/ Math.log(2)));
                    double log2 = ((double) (qtd0/qtdInstancias) * (Math.log( (double) (qtd0/qtdInstancias))/ Math.log(2)));
                    impureza = -(log1 + log2);
                }
            System.out.println("Entropia: " + impureza);
            break;

        //Calculo Erro de Classificação
            case 1:
                if(qtd1 >= qtd0){
                    impureza = (double) 1 - qtd1/qtdInstancias;
                }else{
                    impureza = (double) 1 - qtd0/qtdInstancias;
                }
            System.out.println("Erro de classe: " + impureza);
            break;

            default:
                System.out.println("Erro: Selecione uma medidade de impureza (1 - Gini / 2- Entropia / 3- Erro de Classe");
                break;
        }
        
        return impureza;
    }
    
    public static double calcGanho(double qtdFilho0, double qtdFilho1, double impurezaPai, double impurezaFilho0, double impurezaFilho1)
    {
        double qtdTotal = qtdFilho0 + qtdFilho1;
        return (impurezaPai - (impurezaFilho0*(qtdFilho0/qtdTotal) + impurezaFilho1*(qtdFilho1/qtdTotal)) );
    }
    
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {

        //The text file location of your choice
        String nomeArquivo = args[0];
        String saida0 = args[1]; 
        String saida1 = args[2]; 
        String atributo = args[3]; 
        Integer tipoImpureza = Integer.parseInt(args[4]);
        List<String> lista = new ArrayList<>();
        FileReader fileReader = new FileReader(nomeArquivo);
        int contador = 0;

        try (BufferedReader bufferedReader = new BufferedReader(fileReader))
        {
            List<String> linhas = new ArrayList<>();
            String linha = null;

            while ((linha = bufferedReader.readLine()) != null) 
            {
                contador = countWords(linha);
                int tamanho = linha.length();
                String[] newAtributo = new String[tamanho];
                newAtributo = linha.split(" ");
                for(int i = 0; i < contador; i++)
                {
                    lista.add(newAtributo[i]);
                }
            }
        }catch(Exception e){
            System.out.println(e);
        }
        int totalLinhas = lista.size()/contador;
        int k = 0;

        // Hora de colocar numa matriz para trabalhar mais facilmente
        String [][] listaVariaveis = new String[totalLinhas][contador];
        for(int i = 0; i < totalLinhas ; i++)
        {
            for(int j = 0; j < contador; j++)
            {
                listaVariaveis[i][j] = lista.get(k);
                //System.out.println("Variavel da posicao [" + i + "][" + j + "]: " + listaVariaveis[i][j]);
                k++;
            }
        }
        int colunaVariavel = 0;
        for(int j = 0; j < contador; j++)
        {
            if(atributo.equals(listaVariaveis[0][j]))
            {
                colunaVariavel = j;
                break;
            }
        }
        List<Integer> conta0 = new ArrayList<>();
        List<Integer> conta1 = new ArrayList<>();
        for(int i = 1; i < totalLinhas; i++)
        {
            if(listaVariaveis[i][colunaVariavel].equals("0"))
            {
                conta0.add(i);
            }
            else if(listaVariaveis[i][colunaVariavel].equals("1"))
            {
                conta1.add(i);
            }
        }
        String newline = System.getProperty("line.separator");
        String addTxt0 = "";
        String addTxt1 = "";
        for(int i = 0; i < contador; i++)
        {
            if (i != (contador-1))
            {
                addTxt0 = addTxt0 + listaVariaveis[0][i] + " ";
                addTxt1 = addTxt1 + listaVariaveis[0][i] + " ";
            }
            else
            {
                addTxt0 = addTxt0 + listaVariaveis[0][i] + newline;
                addTxt1 = addTxt1 + listaVariaveis[0][i] + newline;
            }
        }
        double contaFilho0_0 = 0, contaFilho0_1 = 0;
        double contaFilho1_0 = 0, contaFilho1_1 = 0;
        for(int i = 0; i < conta0.size(); i++)
        {
            // Adicionar no TXT
            for(int j = 0; j < contador; j++)
            {
                if (j != (contador-1))
                    addTxt0 = addTxt0 + listaVariaveis[conta0.get(i)][j] + " ";
                else
                    addTxt0 = addTxt0 + listaVariaveis[conta0.get(i)][j] + newline;
                
            }
            if(listaVariaveis[conta0.get(i)][contador-1].equals("0"))
            {
                contaFilho0_0++;
            }
            else if(listaVariaveis[conta0.get(i)][contador-1].equals("1"))
            {
                contaFilho0_1++;
            }
        }
        
        for(int i = 0; i < conta1.size(); i++)
        {
            // Adicionar no TXT
            for(int j = 0; j < contador; j++)
            {
                if (j != (contador-1))
                    addTxt1 = addTxt1 + listaVariaveis[conta1.get(i)][j] + " ";
                else
                    addTxt1 = addTxt1 + listaVariaveis[conta1.get(i)][j] + newline;
                
            }
            if(listaVariaveis[conta1.get(i)][contador-1].equals("0"))
            {
                contaFilho1_0++;
            }
            else if(listaVariaveis[conta1.get(i)][contador-1].equals("1"))
            {
                contaFilho1_1++;
            }
        }
        //System.out.println(contaFilho0_0);
        
        System.out.println("Isto será adicionado ao primeiro TXT: \n" + addTxt0);
        System.out.println("Isto será adicionado ao segundo TXT: \n" + addTxt1);

        System.out.println("Os arquivos serão salvos nos arquivos a seguir: ");
        
        BufferedWriter writer = null;
        // Primeira saída (A que contém os 0s)
        try {
            File logFile = new File(saida0);
            writer = new BufferedWriter(new FileWriter(logFile));
            writer.write(addTxt0);
            System.out.println(logFile.getAbsolutePath());
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        // Segunda saída (contém os 1s)
        try {
            File logFile = new File(saida1);
            writer = new BufferedWriter(new FileWriter(logFile));
            writer.write(addTxt1);
            System.out.println(logFile.getAbsolutePath());
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        int separador = 0;
        String resultado = atributo;

        for(int i = 0; i < lista.size() ; i++)
        {
            boolean numerico = true;
            try{
                Double num = Double.parseDouble(lista.get(i));
            }catch (NumberFormatException e){
                numerico = false;
            }
            if(!numerico){
                separador++;
            }
        }

        double contaPai0 = 0, contaPai1 = 0;
        for(int i = 1; i < totalLinhas; i++)
        {
            if(listaVariaveis[i][contador-1].equals("0"))
            {
                contaPai0++;
            }
            else if(listaVariaveis[i][contador-1].equals("1"))
            {
                contaPai1++;
            }
        }

        System.out.println(contaPai0);
        System.out.println(contaPai1);
        double impurezaPai = calculoImpureza(contaPai0, contaPai1, tipoImpureza);
        double impurezaFilho0 = calculoImpureza(contaFilho0_0, contaFilho0_1, tipoImpureza);
        double impurezaFilho1 = calculoImpureza(contaFilho1_0, contaFilho1_1, tipoImpureza);
        double ganho = calcGanho((contaFilho0_0+contaFilho0_1), (contaFilho1_0+contaFilho1_1), impurezaPai, impurezaFilho0, impurezaFilho1);
        System.out.println("O ganho é de: " + ganho);
    }
}