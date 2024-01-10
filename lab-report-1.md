# Lab Report 1

## Remote Access and Filesystem

I learned some new basic filesystem commands in lecture today. I will detail them (with examples) here.

### cd

`cd` is short for "change directory". As its name suggests, it changes the user's current working directory. Here are some examples:

- Here, I call `cd` alone from */home/lecture1*. It changed my working directory from */home/lecture1* to */home*, my home directory.

```
[user@sahara ~/lecture1]$ pwd
/home/lecture1
[user@sahara ~/lecture1]$ cd
[user@sahara ~]$ pwd
/home
```

- Here, I call `cd lecture1` from */home*. It changed my working directory from */home* to */home/lecture1*.

```
[user@sahara ~]$ pwd
/home
[user@sahara ~]$ cd lecture1
[user@sahara ~/lecture1]$ pwd
/home/lecture1
```

- Here, I call `cd Hello.java` from */home/lecture1*.

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
