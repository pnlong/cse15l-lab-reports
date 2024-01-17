import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

class Handler implements URLHandler {

    // stores the list of strings
    ArrayList<String> strings;

    // constructor
    Handler() {
        this.strings = new ArrayList<String>();
    }

    // handle a request
    public String handleRequest(URI url) {
        // default is to print out the current string list
        if (url.getPath().equals("/")) {
            return "[" + String.join(", ", this.strings) + "]";
        }
        // to add an element to the string list
        else if (url.getPath().equals("/add")) {
            String[] parameters = url.getQuery().split("=");
            if (parameters[0].equals("s")) {
                this.strings.add(parameters[1]);
                return parameters[1] + " added.";
            } else {
                return "Invalid query. Try add?s=<string>.";
            }
        }
        // to search the list
        else if (url.getPath().equals("/search")) {
            String[] parameters = url.getQuery().split("=");
            if (parameters[0].equals("s")) {
                ArrayList<String> queryResults = new ArrayList<String>();
                for (String element: this.strings) {
                    if (element.contains(parameters[1])) {
                        queryResults.add(element);
                    }
                }
                return "[" + String.join(", ", queryResults) + "]";
            } else {
                return "Invalid query. Try search?s=<string>.";
            }
        }
        // help message
        else if (url.getPath().equals("help")) {
            return "Try add?s=<string> to add an entry. Try search?s=<string> to search for a certain substring within entries.";
        }
        // unknown argument
        else {
            return "404 Not Found!";
        }
    }
}

class SearchEngine {
    public static void main(String[] args) throws IOException {
        if(args.length == 0){
            System.out.println("Missing port number! Try any number between 1024 to 49151");
            return;
        }

        int port = Integer.parseInt(args[0]);

        Server.start(port, new Handler());
    }
}
