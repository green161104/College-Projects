package Util.singlyLinkedLists;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Files {

    /**
     * Método responsável por ler um ficheiro
     *
     * @param nameFile Nome do ficheiro
     * @return As linhas do ficheiro
     */
    public static List<String> readFile(String nameFile) {
        List<String> file = new ArrayList<String>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(nameFile));
            while (br.ready()) {
                file.add(br.readLine());
            }
            br.close();
        } catch (Exception e) {
        }

        return file;
    }

    /**
     * Método responsável por ler um ficheiro
     *
     * @param nameFile Nome do ficheiro
     * @param Linha Linha a adicionar no ficheiro
     * @return True caso seja adicionada a linha com sucesso, False caso
     * contrário
     */
    public static boolean writeFile(String nameFile, String Linha) {
        try {
            FileWriter fw = new FileWriter(nameFile, true);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(Linha);
            bw.newLine();
            bw.close();
            fw.close();
        } catch (IOException ex) {
            return false;
        }
        return true;
    }
}
