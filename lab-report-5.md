# Lab Report 5

## Putting it all together

Ahhh... We have reached the final lab report of CSE 15L. This week, I wrapped up everything I've learned so far this quarter.

---

## Debugging Scenario

### Original Post

> Hi all,
> 
> I am currently trying to work through the Week 8's lab, and when I try to run my testing script, `SemanticAnalysisHandlerTests` stalls and never finishes executing. I have entered the following commands into the command-line.
>
```bash
[p1long@ieng6-202]:chat-server-pro:284$ bash test.sh
JUnit version 4.13.2
.
```
> This is my `test.sh` file:
```bash
#!/bin/bash

# compile and run my semantic analysis tests
javac -encoding utf-8 -g -cp .:lib/hamcrest-core-1.3.jar:lib/junit-4.13.2.jar *.java 
java -cp .:lib/hamcrest-core-1.3.jar:lib/junit-4.13.2.jar org.junit.runner.JUnitCore SemanticAnalysisHandlerTests
```
> 
> According to the lab instructions, this is the expected behavior. I suspect that somewhere underneath the hood, Java gets stuck in an infinite `while` loop. But I can't figure out where Java is getting stuck. I don't see anything unusual with the tester input, so I assume that the problem is not one with tester, but even deeper within the `handleRequest()` method.
>
> I am very confused on how to proceed. Any help would be appreciated. Thank you in advance!

### TA Response

Hi Phillip,

I see no problem with your *bash* script. So I agree that you made the correct diagnosis in saying that the bug is within `handleRequest()`, as this behavior is certainly indicative of an infinite `while` loop, of which there shouldn't be any in your test cases...right?

Let's first clarify this: `handleRequest()` is likely getting stuck in an infinite `while` loop, so the bug can probably be found in the `semantic-analysis` section (since this is the only section with `while` loops). The next question you should ask yourself is how can an infinite `while` loop occur? There is no `while (True) {} ` clauses in your code, so it has to be something more nuanced. Look at the conditions for both the outer- and inner-`while` loop in the `semantic-analysis` section. Will Java ever reach a point such that `characterIndex >= codePoints.length`, breaking the inner loop? Will Java ever reach a point such that `index >= chatHistoryArr.length`, breaking the outer loop?

Hopefully this response can guide you to your answer. Good luck.

### Student Response

> Thank you very much for your response TA.
>
> After thinking about what you said, specifically "will Java ever reach a point such that `characterIndex >= codePoints.length`", I realized that the `characterIndex` variable never changes. `characterIndex` is initialized to 0, whereas `codePoints.length` will always be greater than 0. Because `characterIndex` fails to increment, the inner-`while` loop condition always evaluates to `True`, causing the stalling bug that I have encountered.  Therefore, I must increment `characterIndex` properly.
> 
> It's funny how such a drastic bug can be caused by something as simple as a missing increment! Thank you so much for your help.

### Relevant Setup Information

#### File and Directory Structure

```
[p1long@ieng6-202]:chat-server-pro:285$ ls
ChatHandler.class        ChatServer.class    HandlerTests.java                   Server.class             URLHandler.class  test.sh
ChatHistoryReader.class  ChatServer.java     SemanticAnalysisHandlerTests.class  Server.java              chathistory
ChatHistoryReader.java   HandlerTests.class  SemanticAnalysisHandlerTests.java   ServerHttpHandler.class  lib
```

Note that I am running `SemanticAnalysisHandlerTests.java`, but `handleRequest()`, and thus the bug, is in `ChatServer.java`.

#### File Contents Prior to Bug Fix

##### test.sh

The bug-inducing script.

```java
#!/bin/bash

# compile and run my semantic analysis tests
javac -encoding utf-8 -g -cp .:lib/hamcrest-core-1.3.jar:lib/junit-4.13.2.jar *.java 
java -cp .:lib/hamcrest-core-1.3.jar:lib/junit-4.13.2.jar org.junit.runner.JUnitCore SemanticAnalysisHandlerTests
```

##### SemanticAnalysisHandlerTests.java

The tester program.

```
import static org.junit.Assert.*;
import org.junit.*;
import java.net.URI;

public class SemanticAnalysisHandlerTests {
    @Test
    public void handleRequest1() throws Exception {
    ChatHandler h = new ChatHandler();
    String url = "http://localhost:4000/chat?user=joe&message=hi";
    URI input = new URI(url);
    String expected = "joe: hi\n\n";
    assertEquals(expected, h.handleRequest(input));
    }

    @Test
    public void handleRequestMulti() throws Exception {
    ChatHandler h = new ChatHandler();
    String url1 = "http://localhost:4000/chat?user=onat&message=good%20luck";
    String url2 = "http://localhost:4000/chat?user=edwin&message=with%20your%20demo!";
    URI input1 = new URI(url1);
    URI input2 = new URI(url2);
    String expected = "onat: good luck\n\nedwin: with your demo!\n\n";
    h.handleRequest(input1);
    assertEquals(expected, h.handleRequest(input2));
    }

    @Test
    public void handleRequestSemanticAnalysis() throws Exception {
    ChatHandler h = new ChatHandler();
    String url1 = "http://localhost:4000/chat?user=onat&message=😂";
    String url2 = "http://localhost:4000/chat?user=onat&message=doggy🥹!!!";
    String url3 = "http://localhost:4000/chat?user=onat&message=TGIThanksgiving";
    String url4 = "http://localhost:4000/semantic-analysis?user=onat";

    URI input1 = new URI(url1);
    URI input2 = new URI(url2);
    URI input3 = new URI(url3);
    URI input4 = new URI(url4);
    String expected = "onat: 😂 This message has a LOL vibe.\n\nonat: doggy🥹!!! This message has a awwww vibe. This message ends forcefully.\n\nonat: TGIThanksgiving\n\n";

    h.handleRequest(input1);
    h.handleRequest(input2);
    h.handleRequest(input3);
    assertEquals(expected, h.handleRequest(input4));
    }
}
```

##### ChatServer.java

The program that contains the bug.

```java
import java.io.IOException;
import java.net.URI;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

class ChatHandler implements URLHandler {
    String chatHistory = "";

    public String handleRequest(URI url) {

        // expect /chat?user=<name>&message=<string>
        if (url.getPath().equals("/chat")) {
            String[] params = url.getQuery().split("&");
            String[] shouldBeUser = params[0].split("=");
            String[] shouldBeMessage = params[1].split("=");
            if (shouldBeUser[0].equals("user") && shouldBeMessage[0].equals("message")) {
            String user = shouldBeUser[1];
            String message = shouldBeMessage[1];
            this.chatHistory += user + ": " + message + "\n\n";
            return this.chatHistory;
            } else {
            return "Invalid parameters: " + String.join("&", params);
            }
        }
        else if (url.getPath().equals("/")){
            return this.chatHistory;
        }
        // expect /retrieve-history?file=<name>
        else if (url.getPath().equals("/retrieve-history")) {
            String[] params = url.getQuery().split("&");
            String[] shouldBeFile = params[0].split("=");
            if (shouldBeFile[0].equals("file")) {
            String fileName = shouldBeFile[1];
            ChatHistoryReader reader = new ChatHistoryReader();
            try {
                String[] contents = reader.readFileAsArray("chathistory/" + fileName);
                for (String line : contents) {
                this.chatHistory += line + "\n\n";
                }
            } catch (IOException e) {
                System.err.println("Error reading file: " + e.getMessage());
            }
            }
            return this.chatHistory;
        }
        // expect /save?name=<name>
        else if (url.getPath().equals("/save")) {
            String[] params = url.getQuery().split("&");
            String[] shouldBeFileName = params[0].split("=");
            if (shouldBeFileName[0].equals("name")) {
            File directory = new File("chathistory");
            File file = new File(directory, shouldBeFileName[1]);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(this.chatHistory);
                return "Data written to " + shouldBeFileName[1] + "in 'chat-history' folder.";
            } catch (IOException e) {
                e.printStackTrace();
                return "Error: Something wrong happen during file save, check StackTrace";
            }
            }
        }
        // expect /semantic-analysis?user=<name>
        else if (url.getPath().equals("/semantic-analysis")) {
            String[] params = url.getQuery().split("&");
            String[] shouldBeUser = params[0].split("=");
            String matchingMessages = "";
            if (shouldBeUser[0].equals("user")) {
            String[] chatHistoryArr = this.chatHistory.split("\n\n");
            int index = 0;
            while (index < chatHistoryArr.length) {
                String line = chatHistoryArr[index];
                int numberOfExclamationMarks = 0;
                String analysis = "";
                index += 1;
                if (line.startsWith(shouldBeUser[1])) {
                int[] codePoints = new int[0];
                codePoints = line.codePoints().toArray();
                int characterIndex = 0;
                while (characterIndex < codePoints.length) {
                    int character = codePoints[characterIndex];
                    if (character == (int) '!') {
                    numberOfExclamationMarks += 1;
                    }
                    if (new String(Character.toChars(character)).equals("😂")) {
                    analysis += " This message has a LOL vibe.";
                    }
                    else if (new String(Character.toChars(character)).equals("🥹")) {
                    analysis += " This message has a awwww vibe.";
                    }
                    else if (new String(Character.toChars(character)).equals("☕️")) {
                    analysis += " This message has a coffee vibe.";
                    }
                    else if (new String(Character.toChars(character)).equals("😄")) {
                    analysis += " This message has a happy vibe.";
                    }
                }
                if (numberOfExclamationMarks > 2) {
                    analysis += " This message ends forcefully.";
                }
                matchingMessages += line + analysis + "\n\n";
                }
            }
            } 
            return matchingMessages;
        }
        return this.chatHistory;
    }
}

class ChatServer {
    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(args[0]);
        Server.start(port, new ChatHandler());
    }
}
```

### Bug-Inducing Command

```bash
[p1long@ieng6-202]:chat-server-pro:284$ bash test.sh
JUnit version 4.13.2
.
```

### Bug Fix

Within the inner-`while` loop of the `semantic-analysis` section of the `handleRequest()` method (found in `ChatServer.java`), the `characterIndex` variable, which is part of the `while` loop condition, never incremented. As a result, the program got stuck in an infinite while loop. I simply had to a single line at the end of the `while` loop clause (after the `if` statement ladder) that incremented `characterIndex` after each iteration:

```java
characterIndex++;
```

---

## Reflection

My favorite CSE 15L learning experience in the second half of this quarter was definitely *Vim*. On a personal note, I was first exposed to the command-line interface by my father five years ago. One of the first skills that he taught me was not how to use *Vim*, but actually how to use *Vi*, *Vim*'s predecessor. However, with access to tools like VSCode and other fancy GUI text editors, I never put the time into learning *Vi*, and so I never really learned it. Nonetheless, I still thought that *Vi* and *Vim* were really cool tools that I would someday hopefully improve at. Back to the present, when Professor Politz announced that we would be learning *Vim*, I was very excited: I would be forced to learn *Vim*, something I always wanted to do, for a grade! Learning all the different key bindings was interesting, to say the least, but now that I know them, I can confidently say that...well...I've scratched the surface of *Vim*! Just as a side note, I also was very impressed by *Vim*'s syntax highlighting, yet another reason to be excited about the program. After the *Vim* lab report, I sent my dad an email telling him about what I had learned, and he was just as excited for me as I was. I look forward to continuing learning *Vim* in the future.

However, my most useful takeaway from CSE 15L was something tiny: SSH keys! I've been SSHing onto servers for four years now, and I never knew that it was possible to SSH without a password. I am very thankful for SSH keys, and I have set one up for each of the various servers that I work on. I remember when Professor Politz first introduced *bash*'s tab-complete, he remarked that despite it only saving a little time in the short run, the simple tab-complete can save programmers hours over the course of a year. SSH keys have had the same effect on me: no longer having to enter a password, I feel that I have saved myself tremendous amounts of time, especially since SSHing onto servers is a daily task for me. Thank you CSE 15L staff for teaching me these cool tricks!

---

Just like that, I was done with CSE 15L. Thank you tutors and TAs not only for reading my lab report, but also for all your hard work. This was my favorite class this past quarter, so thank you very much. See you all in later courses! Bye!
