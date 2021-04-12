import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.*;
import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class parser {

//function for parsing and forward indexing
    public void forwardIndexing() {
        //variables for tags
        String title_text=new String();
        boolean title = false;
        boolean text = false;


        //using StAX parser to parse the XML file
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_COALESCING,Boolean.TRUE);
        XMLEventReader eventReader;
        //Hashmap to keep count of words
        HashMap<String,Integer> w_values = new HashMap<String,Integer>();

        {
            try {
                eventReader = factory.createXMLEventReader(new FileReader("E:\\NUST\\CS\\Projects\\Search Engine\\simplewiki.xml"));

                //loop will run until program has next element or tag
                while (eventReader.hasNext()) {
                    //variable to get the next event
                    XMLEvent event = eventReader.nextEvent();

                    //switch case to check which tag have we encountered
                    switch (event.getEventType()) {
                        case XMLStreamConstants.START_ELEMENT:
                            //case for start of an element tag
                            StartElement startElement = event.asStartElement();
                            String tag_name = startElement.getName().getLocalPart();

                            if (tag_name.equalsIgnoreCase("page")) {
                                System.out.println("Page");


                            } else if (tag_name.equalsIgnoreCase("title")) {
                                title = true;
                            } else if (tag_name.equalsIgnoreCase("text")) {
                                text = true;
                            }
                            break;

                        case XMLStreamConstants.CHARACTERS:
                            //case to load data inside element tag
                            Characters characters = event.asCharacters();
                            if (title) {
                                title_text = characters.getData();
                                String[] words = title_text.split("\\W+");
                                System.out.println("Title " + title_text);
                                for (String s : words) {
                                    s = s.toLowerCase();
                                    if (w_values.containsKey(s)) {
                                        w_values.put(s, w_values.get(s) + 1000);
                                    }
                                    else{
                                        w_values.put(s,1000);
                                    }
                                }


                                title = false;

                            } else if (text) {
                                String content = characters.getData();
                                content=content.toLowerCase();

                                String purge[] = {"\\ba\\b\\s*", "\\ban\\b\\s*" , "\\band\\b\\s*", "\\bare\\b\\s*", "\\bas\\b\\s*", "\\bat\\b\\s*", "\\bbe\\b\\s*", "\\bbut\\b\\s*", "\\bby\\b\\s*",
                                        "\\bfor\\b\\s*", "\\bif\\b\\s*", "\\bin\\b\\s*", "\\binto\\b\\s*", "\\bis\\b\\s*", "\\bit\\b\\s*",
                                        "\\bno\\b\\s*", "\\bnot\\b\\s*", "\\bof\\b\\s*", "\\bon\\b\\s*", "\\bor\\b\\s*", "\\bs\\b\\s*", "\\bsuch\\b\\s*",
                                        "\\bthus\\b\\s*", "\\bthat\\b\\s*", "\\bthe\\b\\s*", "\\btheir\\b\\s*", "\\bthen\\b\\s*", "\\bthere\\b\\s*", "\\bthese\\b\\s*",
                                        "\\bthey\\b\\s*", "\\bthis\\b\\s*", "\\bto\\b\\s*", "\\bwas\\b\\s*", "\\bwill\\b\\s*", "\\bwith\\b\\s*","\\bwhile\\b\\s*","\\bits\\b\\s*","\\balso\\b\\s*","\\bcan\\b\\s*"};

                                for (int j = 0; j < purge.length; ++j)
                                {
                                    content = content.replaceAll(purge[j], "");
                                }
                                //System.out.println("text:  " + content);
                                String[] words = content.split("\\W+");


                                for (String s : words) {
                                    s = s.toLowerCase();
                                    if (w_values.containsKey(s)) {
                                        w_values.put(s, w_values.get(s) + 1);
                                    }
                                    else{
                                        w_values.put(s,1);
                                    }
                                }


                                text = false;
                            }
                            break;

                        case XMLStreamConstants.END_ELEMENT:
                            //if we are at the end of a page then start reverse indexing process
                            //clear the hashtable after it so the next page can be parsed.

                            EndElement endElement = event.asEndElement();
                            if (endElement.getName().getLocalPart().equalsIgnoreCase("page")) {
                                reverse_indexing(w_values,title_text);
                                System.out.println(w_values);
                                System.out.println("\n\n\n\nEnd of page");

                                w_values.clear();
                            }
                            break;

                    }
                }
            } catch (XMLStreamException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    private void reverse_indexing(HashMap<String,Integer> map, String title)
    {
        for(HashMap.Entry<String,Integer> word: map.entrySet()) {

            File file = new File("Indexed files\\"+word.getKey()+".txt");
            int value=word.getValue();
            try (FileWriter fw = new FileWriter(file,true)) {
                fw.append(title+" = "+value+"\n");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
