# Lab Report 4

## *Vim*

This week, I learned all about the powerful command-line tool called **Vim**. In this lab, I clone a [GitHub repository](https://github.com/ucsd-cse15l-s23/lab7), find some bugs, make some edits, and commit my changes: *all from the command line*!

---

### Logging into `ieng6`

```
philliplong@Phillip-Longs-MacBook-Air ~ % ssh p1long@ieng6.ucsd.edu
Last login: Wed Feb 21 11:01:10 2024 from 137.110.32.86
Hello p1long, you are currently logged into ieng6-202.ucsd.edu

You are using 0% CPU on this system

Cluster Status 
Hostname     Time    #Users  Load  Averages  
ieng6-201   11:10:01   22  1.01,  0.75,  0.65
ieng6-202   11:10:01   7   0.58,  0.38,  0.36
ieng6-203   11:10:01   17  0.06,  0.25,  0.25

 

To begin work for one of your courses [ cs15lwi24 ], type its name 
at the command prompt.  (For example, "cs15lwi24", without the quotes).

To see all available software packages, type "prep -l" at the command prompt,
or "prep -h" for more options.
[p1long@ieng6-202]:~:128$ cs15lwi24
Wed Feb 21, 2024 11:17am - Prepping cs15lwi24
```

**Keys Pressed**: `ssh p1long@ieng6.ucsd.edu<enter>cs15lwi24<enter>`

My first step was to `ssh` onto `ieng6`. Because I had previously set up an `ssh` key, I was not prompted for a password, greatly expediting this process. I then entered `cs15lwi24`, which is a shortcut that changes my current working directory such that my new directory is course-specific to CSE15L (`/home/linux/ieng6/cs15lwi24/p1long`).

### Clone the Forked Repository

```
[p1long@ieng6-202]:p1long:129$ git clone git@github.com:pnlong/lab7.git
Cloning into 'lab7'...
Warning: Permanently added the RSA host key for IP address '140.82.113.4' to the list of known hosts.
remote: Enumerating objects: 58, done.
remote: Counting objects: 100% (24/24), done.
remote: Compressing objects: 100% (12/12), done.
remote: Total 58 (delta 15), reused 12 (delta 12), pack-reused 34
Receiving objects: 100% (58/58), 376.37 KiB | 1.81 MiB/s, done.
Resolving deltas: 100% (21/21), done.
[p1long@ieng6-202]:p1long:130$ cd lab7
```

**Keys Pressed**: `git clone <command><v><enter>cd l<tab><enter>`

For some background, before the lab, I set up an `ssh` key on `ieng6` to connect with GitHub, which allows me to clone repositories with an `ssh`, as opposed to `https`, address. I also forked the [lab7 GitHub repository](https://github.com/ucsd-cse15l-s23/lab7), and my fork can be found [here](https://github.com/pnlong/lab7). Back to the present, I copied the `ssh` URL from my lab7 fork. I proceeded to enter `git clone `, and then pasted the `ssh` URL with the ⌘V shortcut. I then changed my working directory to the cloned repository `lab7` with `cd`, employing tab autocomplete to avoid typing out the full directory name.

### Run the Failing Tests

```
[p1long@ieng6-202]:lab7:131$ sh test.sh
JUnit version 4.13.2
..E
Time: 0.518
There was 1 failure:
1) testMerge2(ListExamplesTests)
org.junit.runners.model.TestTimedOutException: test timed out after 500 milliseconds
	at ListExamples.merge(ListExamples.java:44)
	at ListExamplesTests.testMerge2(ListExamplesTests.java:19)

FAILURES!!!
Tests run: 2,  Failures: 1
```

**Keys Pressed**: `sh t<tab><enter>`

My next step was to run the tester shell script. Already knowing the file structure of `lab7`, I simply entered `sh t` and then employed tab autocomplete to complete the filename, as I knew that `test.sh` was the only file that started with a `t`. Running this script showed me that out of my two tests, one of them was failing. I needed to get to work!

### Edit the Code, Make Bug Fixes

```
[p1long@ieng6-202]:lab7:132$ vim ListExamples.java
```
```java
import java.util.ArrayList;
import java.util.List;

interface StringChecker { boolean checkString(String s); }

class ListExamples {

  // Returns a new list that has all the elements of the input list for which
  // the StringChecker returns true, and not the elements that return false, in
  // the same order they appeared in the input list;
  static List<String> filter(List<String> list, StringChecker sc) {
    List<String> result = new ArrayList<>();
    for(String s: list) {
      if(sc.checkString(s)) {
        result.add(0, s);
      }
    }
    return result;
  }


  // Takes two sorted list of strings (so "a" appears before "b" and so on),
  // and return a new list that has all the strings in both list in sorted order.
  static List<String> merge(List<String> list1, List<String> list2) {
    List<String> result = new ArrayList<>();
    int index1 = 0, index2 = 0;
    while(index1 < list1.size() && index2 < list2.size()) {
      if(list1.get(index1).compareTo(list2.get(index2)) < 0) {
        result.add(list1.get(index1));
        index1 += 1;
      }
      else {
        result.add(list2.get(index2));
        index2 += 1;
      }
    }
    while(index1 < list1.size()) {
      result.add(list1.get(index1));
      index1 += 1;
    }
    while(index2 < list2.size()) {
      result.add(list2.get(index2));
      // change index1 below to index2 to fix test
      index2 += 1;
    }
    return result;
  }


}
~
~
~
~
wq
```

**Keys Pressed**: `vim L<tab>.j<tab><enter>43jer2:wq<enter>`

To make edits to the repository's code base, I would need a command-line text editor: *Vim*! I entered `vim L` then used tab autocomplete to avoid typing out `ListExamples`. However, because there are multiple files in `lab7` that start with the pattern `ListExamples`, I needed to type `.j` to avoid matching both the `ListExamplesTests` files and `ListExamples.class`, which was created previously by running the `test.sh` script. I then used tab autocomplete to avoid typing out the `.java` file type. I had now entered the *Vim* interface. Breaking down the *Vim* command, I jumped down to the 44th line (the line with the error) with `43j` and then jumped to the end of the first word of that line (`index1`) with `e`. This moved my cursor to the `1` character, which happened to be the exact character I needed to fix, so I then entered `r2` to replace the `1` with a `2`, fixing the bug. To save my work and exit *Vim*, I entered `:wq`, where `w` writes the file and `q` quits *Vim*.

### Rerun the Tests

```
[p1long@ieng6-202]:lab7:133$ sh test.sh 
JUnit version 4.13.2
..
Time: 0.015

OK (2 tests)

```

**Keys Pressed**: `<up><up><enter>`

Back in the normal terminal, I now wanted to rerun `test.sh`. Because I knew that I had already done this before, I simply went back two commands in my Bash history by pressing the up-arrow twice. Back at the command `sh test.sh`, I reran my tests.

### Commit and Push Changes to GitHub

```
[p1long@ieng6-202]:lab7:134$ git commit -a -m "bug fixes"
[main c05c3c9] bug fixes
 Committer: Phillip Long <p1long@ieng6-202.ucsd.edu>
Your name and email address were configured automatically based
on your username and hostname. Please check that they are accurate.
You can suppress this message by setting them explicitly. Run the
following command and follow the instructions in your editor to edit
your configuration file:

    git config --global --edit

After doing this, you may fix the identity used for this commit with:

    git commit --amend --reset-author

 1 file changed, 1 insertion(+), 1 deletion(-)
[p1long@ieng6-202]:lab7:135$ git push origin main
Enumerating objects: 5, done.
Counting objects: 100% (5/5), done.
Delta compression using up to 8 threads
Compressing objects: 100% (3/3), done.
Writing objects: 100% (3/3), 295 bytes | 147.00 KiB/s, done.
Total 3 (delta 2), reused 0 (delta 0), pack-reused 0
remote: Resolving deltas: 100% (2/2), completed with 2 local objects.
To github.com:pnlong/lab7.git
   327ab1a..c05c3c9  main -> main
```

**Keys Pressed**: `git commit -a -m "bug fixes"<enter>git push origin main<enter>`

To commit my changes, I fully-typed out `git commit -a -m "bug fixes"`, as I do not know any shortcuts to expedite this command. The `-a` option stages every altered file in the repository to be committed, and the `-m` option allows me to specify my commit message. With my changes committed, I then pushed them with `git push origin main`; again, I do not know any keyboard shortcuts to expedite this command, so I typed it out in its entirety.

---

Just like that, I had fixed my bugs. Thank you for reading my lab report. One more left!
