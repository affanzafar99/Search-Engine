import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class Search {

    private FileReader file;
    private String file_name;

    Scanner input=new Scanner(System.in);


    //function that prompts the user to search
    public void query() {
        System.out.println("Enter the word you want to search for: ");
        String word = input.nextLine();
        System.out.println(word);
        word = word.toLowerCase();
        String[] WORD = word.split("\\s+");

        //In case of multiple word If statement would be true
        if (WORD.length > 1) {

            multiple_words(word);
        }
        else{
            single_word(WORD[0]);
        }

    }


    //function for single word search
    public void single_word(String word)
    {
        //this would be true if search results exists for the given word
        if(file_open(word))
        {
            display_sWord_results();
        }
        else{
            System.out.println("No results found");
        }
    }


    // return true when file is accessed successfully
    public boolean file_open(String title)
    {
        try{
            this.file_name="./Indexed files/"+title+".txt";
            file=new FileReader(file_name);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    //displays result for single words search
    public void display_sWord_results(){
        Scanner reader=new Scanner(file);
        TreeMap<Integer,String> treeMap=new TreeMap<Integer, String>(Collections.reverseOrder());

        while(reader.hasNext())
        {
            String line= reader.nextLine();
            //System.out.println(line);
            String[] content=line.split("=");


            for(int i=0;i<content.length;i++)
            {
                content[i]=content[i].trim();
                //System.out.println(content[i]+"   size="+content[i].length());
            }
            treeMap.put(Integer.parseInt(content[1]),content[0]);

           // return;
            //treeMap.put(content[0],Integer.parseInt(content[1]));
        }
        int i=0;
        System.out.println("Top 10 results According to relevance: ");
        for(Map.Entry<Integer,String> entry: treeMap.entrySet())
        {
            System.out.println(entry.getValue());
            i++;
            if(i==10)
                break;
        }

    }


    //function for multiple word search
    //this function can be further improved
    public void multiple_words(String word){

        word = word.replaceAll("[^a-zA-Z0-9\\s ]", " ");
        String purge[] = {"\\ba\\b\\s*", "\\ban\\b\\s*", "\\band\\b\\s*", "\\bare\\b\\s*", "\\bas\\b\\s*", "\\bat\\b\\s*", "\\bbe\\b\\s*", "\\bbut\\b\\s*", "\\bby\\b\\s*",
                "\\bfor\\b\\s*", "\\bif\\b\\s*", "\\bin\\b\\s*", "\\binto\\b\\s*", "\\bis\\b\\s*", "\\bit\\b\\s*",
                "\\bno\\b\\s*", "\\bnot\\b\\s*", "\\bof\\b\\s*", "\\bon\\b\\s*", "\\bor\\b\\s*", "\\bs\\b\\s*", "\\bsuch\\b\\s*",
                "\\bthus\\b\\s*", "\\bthat\\b\\s*", "\\bthe\\b\\s*", "\\btheir\\b\\s*", "\\bthen\\b\\s*", "\\bthere\\b\\s*", "\\bthese\\b\\s*",
                "\\bthey\\b\\s*", "\\bthis\\b\\s*", "\\bto\\b\\s*", "\\bwas\\b\\s*", "\\bwill\\b\\s*", "\\bwith\\b\\s*", "\\bwhile\\b\\s*", "\\bits\\b\\s*", "\\balso\\b\\s*", "\\bcan\\b\\s*"};
        for (int j = 0; j < purge.length; ++j) {
            word = word.replaceAll(purge[j], "");
        }
        String[] WORDs = word.split("\\s+");

        // We were required to find the intersection of the words according to the page ids for that purpose we have used a hash table; Key: Title, Value: intersection
        Hashtable<String,Integer> table = new Hashtable<String,Integer>();
        for(int i =0;i<WORDs.length;i++){
            String Path="./Indexed files/"+WORDs[i]+".txt";
            FileReader file =null;
            BufferedReader buffer =null;
            try{
                file = new FileReader(Path);
                buffer = new BufferedReader(file);
                String sCurrentLine;
                while ((sCurrentLine = buffer.readLine()) != null) {
                    String[] search = sCurrentLine.split("=");
                    //String ForHash = search[0]+","+search[1];
                    //String Words[] = ForHash.split(",");
                    String title=search[0].trim();
                    int count =1;
                    if ( table.containsKey(title)) { //replacing all the Words[1] with title
                        count = table.get(title);
                        count++;
                        table.put(title,count);
                    }
                    else{
                        table.put(title,count);
                    }
                }
            }catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    if (buffer != null)
                        buffer.close();
                    if (file != null)
                        file.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        int c1 =0;
        System.out.println("Top 10 results According to relevance: ");
        // This algorithm to retrieve the relevant result by using intersection basically we are printing the top 10 result of the top
        // based on the intersection
        for (int i =WORDs.length;i>1;i--) {
            Enumeration<String> titles = table.keys();
            while(table.contains(i)) {
                String checker = titles.nextElement();
                if(table.get(checker)==i) {
                    if(c1<10){
                        System.out.println(checker);
                    }else{
                        break;
                    }
                    c1++;
                    table.remove(checker);

                }
            }
        }
        Enumeration<String> titles2 = table.keys();

    }

}
