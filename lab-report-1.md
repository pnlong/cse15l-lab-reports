# Lab Report 1

## Remote Access and Filesystem

I learned some new basic filesystem commands in lecture today. I will detail them (with examples) here.

### cd

`cd` is short for "change directory". As its name suggests, it changes the user's current working directory. Here are some examples:

- Here, I call `cd` alone from */home/lecture1*. It changed my working directory from */home/lecture1* to */home*, which my home directory. I suspect that calling `cd` without any arguments implicitly calls `cd ~` under the hood, where the `~` symbol represents the home directory. In other words, calling `cd` alone brings the user back to the home directory. This is not an error, and it's actually a pretty neat trick to quickly return to the home directory!

```
[user@sahara ~/lecture1]$ pwd
/home/lecture1
[user@sahara ~/lecture1]$ cd
[user@sahara ~]$ pwd
/home
```

- Here, I call `cd lecture1` from */home*, where *lecture1* is a directory within the */home* directory. It changed my working directory from */home* to */home/lecture1*. When calling `cd` with a directory as an argument, it works as expected, changing the user's working directory to the provided directory. This is the correct usage of the `cd` command. This is not an error.

```
[user@sahara ~]$ pwd
/home
[user@sahara ~]$ cd lecture1
[user@sahara ~/lecture1]$ pwd
/home/lecture1
```

- Here, I call `cd Hello.java` from */home/lecture1*, where *Hello.java* is a file in the directory */home/lecture1*. Passing a file path to the `cd` command is incorrect, as the `cd` command expects a directory (you can't change your directory to a file). Doing so caused the following error -- `bash: cd: Hello.java: Not a directory` -- which makes sense considering the usage of the `cd` command. Note that the current working directory did not change, since `cd` did not execute due to the error.

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

- Here, I call `ls` alone from */home/lecture1*. It printed the contents (all of the files and subdirectories) of my current working directory. I suspect that calling `ls` without any arguments implicitly calls `ls .` under the hood, where the `.` symbol represents the current working directory. In simple terms, calling `ls` alone prints out the contents of the working directory. This is not an error, and it's a very useful feature I could imagine using on a daily basis.

```
[user@sahara ~/lecture1]$ pwd
/home/lecture1
[user@sahara ~/lecture1]$ ls
Hello.class  Hello.java  messages  README
```

- Here, I call `ls lecture1` from */home*, where *lecture1* is a directory within the */home* directory. It printed the contents of */home* (note that *lecture1* is the only directory or file in */home*). When calling `ls` with a directory as an argument, it works as expected, printing out the contents of the provided directory. This is the correct usage of the `ls` command. This is not an error.

```
[user@sahara ~]$ pwd
/home
[user@sahara ~]$ ls
lecture1
```

- Here, I call `ls Hello.java` from */home/lecture1*, where *Hello.java* is a file in the directory */home/lecture1*. Passing a file path to the `ls` command doesn't make much sense, as the `ls` command is meant for listing the contents of a directory. However, doing so simply returned the exact filepath that I passed to the `ls` command. This is not an error, though calling `ls` on a file seems pretty redundant.

```
[user@sahara ~/lecture1]$ pwd
/home/lecture1
[user@sahara ~/lecture1]$ ls Hello.java
Hello.java
```

### cat

`cat` is short for "concatenate". It writes the contents of a file to the standard output (or a specific filepath when `>` is used). Here are some examples:

- Here, I call `cd` alone from */home/lecture1*. It changed my working directory from */home/lecture1* to */home*, which my home directory. I suspect that calling `cd` without any arguments implicitly calls `cd ~` under the hood, where the `~` symbol represents the home directory. In otherwords, calling `cd` alone brings the user back to the home directory. This is not an error, and it's actually a pretty neat trick to quickly return to the home directory!

```
[user@sahara ~/lecture1]$ pwd
/home/lecture1
[user@sahara ~/lecture1]$ cd
[user@sahara ~]$ pwd
/home
```

- Here, I call `cd lecture1` from */home*, where *lecture1* is a directory within the */home* directory. It changed my working directory from */home* to */home/lecture1*. When calling `cd` with a directory as an argument, it works as expected, changing the user's working directory to the provided directory. This is the correct usage of the `cd` command. This is not an error.

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
