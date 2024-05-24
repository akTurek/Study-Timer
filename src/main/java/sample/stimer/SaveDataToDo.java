package sample.stimer;

import javafx.scene.control.ListView;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;



public class SaveDataToDo {

    public static void newOpravilo(String predmet, String opravilo, ListView<String> toDdListView) {
        File f = new File("C:\\StudyBudy\\"+predmet+".txt");

        try {
            BufferedReader file = new BufferedReader(new FileReader(f));
            StringBuffer inputBuffer = new StringBuffer();
            String line;

            while ((line = file.readLine()) != null) {
                inputBuffer.append(line);
                inputBuffer.append('\n');
            }

            line = opravilo+"@0";
            inputBuffer.append(line);
            inputBuffer.append('\n');
            file.close();

            // write the new string with the replaced line OVER the same file
            FileOutputStream fileOut = new FileOutputStream(f);
            fileOut.write(inputBuffer.toString().getBytes());
            fileOut.close();
            toDdListView.getItems().add(opravilo);

        } catch (IOException e) {
            System.out.println("Problem reading or creating file. " + f.getAbsolutePath());
            e.printStackTrace();
        }
    }

    public static void deleteOpravilo(String predmet, String opravilo) {
        File f = new File("C:\\StudyBudy\\"+predmet+".txt");
        System.out.println("backend oprvilo "+ opravilo);

        try {
            BufferedReader file = new BufferedReader(new FileReader(f));
            StringBuffer inputBuffer = new StringBuffer();
            String line;

            while ((line = file.readLine()) != null) {
                if (!line.equals(opravilo+"@0") &&  !line.equals(opravilo+"@1")) {
                    inputBuffer.append(line);
                    inputBuffer.append('\n');
                }else {
                    System.out.println("backendizbris "+line);
                }
            }
            file.close();
            FileOutputStream fileOut = new FileOutputStream(f);
            fileOut.write(inputBuffer.toString().getBytes());
            fileOut.close();


        } catch (Exception e) {
            System.out.println("Problem reading file. " + f.getAbsolutePath());
        }
    }

    public static String[] arrayOpravil(String predmet) {
        ArrayList<String> arrayListOpravil = new ArrayList<>();
        File f = new File("C:\\StudyBudy\\"+predmet+".txt");
        try {
            BufferedReader file = new BufferedReader(new FileReader(f));
            String line;

            while ((line = file.readLine()) != null) {
                String[] newLineArray = line.split("@");
                if (newLineArray[1].equals("1")) {
                    arrayListOpravil.add("☑ " +newLineArray[0]);
                } else {
                    arrayListOpravil.add("☐ "+newLineArray[0]);
                }

            }
            file.close();

        } catch (Exception e) {
            System.out.println("Problem reading file. " + f.getAbsolutePath());
            return null;
        }
        return arrayListOpravil.toArray(new String[0]);
    }

    public static int[] opravljenaNeopravljenaPieChart(ArrayList<String> arrayListPredmetov) {

        int [] opravljeniNeopravljeni =new int[]{0,0};
        for (String element : arrayListPredmetov) {
            File f = new File("C:\\StudyBudy\\"+element+".txt");
            try {
                BufferedReader file = new BufferedReader(new FileReader(f));
                String line;

                while ((line = file.readLine()) != null) {
                    String[] newLineArray = line.split("@");
                    if (newLineArray[1].equals("1")) {
                        opravljeniNeopravljeni[0]++;
                    } else {
                        opravljeniNeopravljeni[1]++;
                    }

                }
                file.close();

            } catch (Exception e) {
                System.out.println("Problem reading file. " + f.getAbsolutePath());
                return null;
            }
        }
        return opravljeniNeopravljeni;
    }




    public static void checkUncheck (String predmet, String currentOpravilo) {
        String opravilo = currentOpravilo.replaceAll("[☐☑] ","");
        //System.out.println("backend "+opravilo);
        File f = new File("C:\\StudyBudy\\"+predmet+".txt");

        try {
            BufferedReader file = new BufferedReader(new FileReader(f));
            StringBuffer inputBuffer = new StringBuffer();
            String line;

            while ((line = file.readLine()) != null) {
                if (line.equals(opravilo+"@0") || line.equals(opravilo+"@1")) {
                    String[] newLineArray = line.split("@");
                    if (newLineArray[1].equals("1")) {
                        line = opravilo+"@0";
                        //System.out.println("dal na 0");
                    } else {
                        line = opravilo+"@1";
                        //System.out.println("dal na 1");
                    }
                }
                inputBuffer.append(line);
                inputBuffer.append('\n');

            }
            file.close();

            // write the new string with the replaced line OVER the same file
            FileOutputStream fileOut = new FileOutputStream(f);
            fileOut.write(inputBuffer.toString().getBytes());
            fileOut.close();

        } catch (IOException e) {
            System.out.println("Problem reading or creating file. " + f.getAbsolutePath());
            e.printStackTrace();
        }
    }

}
