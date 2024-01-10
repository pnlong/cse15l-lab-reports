# Lab Report 1

## Remote Access and Filesystem

I learned some new basic filesystem commands in lecture today. I will detail them (with examples) here.

### cd

`cd` is short for "change directory". As its name suggests, it changes the user's current working directory. Here are some examples:

- Here, I call `cd` alone from */home/lecture1*. It changed my working directory from */home/lecture1* to */home*, my home directory. I suspect that calling `cd` without any arguments implicitly calls `cd ~` under the hood, where the `~` symbol represents the home directory. In otherwords, calling `cd` alone brings the user back to the home directory. This is not an error, and it's actually a pretty neat trick to quickly return to the home directory!

```
[user@sahara ~/lecture1]$ pwd
/home/lecture1
[user@sahara ~/lecture1]$ cd
[user@sahara ~]$ pwd
/home
```

- Here, I call `cd lecture1` from */home*. It changed my working directory from */home* to */home/lecture1*. Note that *lecture1* is a directory within the */home* directory. When calling `cd` with a directory as an argument, it works as expected, changing the user's working directory to the provided directory. This is the correct usage of the `cd` command. This is not an error.

```
[user@sahara ~]$ pwd
/home
[user@sahara ~]$ cd lecture1
[user@sahara ~/lecture1]$ pwd
/home/lecture1
```

- Here, I call `cd Hello.java` from */home/lecture1*, where *Hello.java* is a file in the directory */home/lecture1*. Passing a file path to the `cd` command is incorrect, as the `cd` command expects a directory (you can't change your directory to a file). Doing so caused the following error -- `bash: cd: Hello.java: Not a directory` -- which makes sense considering the usage of the `cd` command.

```
[user@sahara ~/lecture1]$ pwd
/home/lecture1
[user@sahara ~/lecture1]$ cd Hello.java
bash: cd: Hello.java: Not a directory
[user@sahara ~/lecture1]$ pwd
/home/lecture1
```

### ls

`ls` is short for "list". It lists the contents of a directory. Here are some examples:

- 

### cat

`cat` is short for "concatenate". It writes the contents of a file to the standard output (or a specific filepath when `>` is used). Here are some examples:

- 
