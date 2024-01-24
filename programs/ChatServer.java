import java.io.IOException;
import java.net.URI;

class ChatHandler implements URLHandler {

    // internal String that stores the chat
    String chat;

    // constructor
    ChatHandler() {
        this.chat = "";
    }

    // handle requests
    public String handleRequest(URI url) {
        // default behavior is to display chat
        if (url.getPath().equals("/")) {
            return this.chat;
        }
        // add a new message to the chat
        else if (url.getPath().equals("/add-message")) {
            String query = url.getQuery(); // get the query
            if (query == null) { // make sure query is valid
                return "Invalid query...See usage:\n/add-message?s=<string>&user=<string>\n";
            }
            String[] arguments = query.split("&"); // get arguments
            if (!(arguments[0].startsWith("s=") && arguments[1].startsWith("user="))) { // make sure query is valid
                return "Invalid query...See usage:\n/add-message?s=<string>&user=<string>\n";
            }
            this.chat += String.format("%s: %s\n", arguments[1].split("=")[1], arguments[0].split("=")[1]); // concatenate to chat
            return this.chat; // output chat
        }
        // help message
        else if (url.getPath().equals("/help")) {
            return "Welcome to ChatServer! Try add-message?s=<string>&user=<string> to add a chat.\n";
        }
        // unknown path
        else {
            return "404 Not Found!\n";
        }
    }
}

class ChatServer {
    public static void main(String[] args) throws IOException {

        // make sure port is supplied
        if(args.length == 0) {
            System.out.println("Missing port number! Try any number between 1024 to 49151");
            return;
        }

        // get the port
        int port = Integer.parseInt(args[0]);

        // create a web server
        Server.start(port, new ChatHandler());

    }
}