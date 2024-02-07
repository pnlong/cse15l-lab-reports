# Lab Report 3

This week, I learned some more useful command line tools. I am also learning the art of debugging!

---

## Bugs

I have chosen to analyze the `reverseInPlace()` method, which is part of `ArrayExamples.java`.

`reverseInPlace()` was originally only tested on an array of length 1. It would fail on any non-symmetric array of length >1, such as `{1,2,3,4}`. For example, the following test would fail (a symptom):

```java
@Test
public void testReverseInPlace1() {
  int[] input = { 1,2,3,4 };
  ArrayExamples.reverseInPlace(input);
  assertArrayEquals(new int[]{ 4,3,2,1 }, input);
}
```

In this case, though we expect `input` to be reversed (`{4,3,2,1}`), it actually becomes `{4,3,3,4}`. This isn't good!

However, some test cases pass just fine. For instance, `reverseInPlace()` can correctly reverse an array of length 1.

```java
@Test 
public void testReverseInPlace2() {
    int[] input = { 3 };
    ArrayExamples.reverseInPlace(input);
    assertArrayEquals(new int[]{ 3 }, input);
}
```

`input` is correctly reversed (though it really wasn't that hard)!

Running my two `JUnit` tests yields the following output:

```
philliplong@Phillip-Longs-MacBook-Air lab4 % javac ArrayExamples.java                                                                              
philliplong@Phillip-Longs-MacBook-Air lab4 % javac -cp .:lib/hamcrest-core-1.3.jar:lib/junit-4.13.2.jar ArrayTests.java
philliplong@Phillip-Longs-MacBook-Air lab4 % java -cp .:lib/hamcrest-core-1.3.jar:lib/junit-4.13.2.jar org.junit.runner.JUnitCore ArrayTests
JUnit version 4.13.2
.E.
Time: 0.004
There was 1 failure:
1) testReverseInPlace1(ArrayTests)
arrays first differed at element [2]; expected:<2> but was:<3>
        at org.junit.internal.ComparisonCriteria.arrayEquals(ComparisonCriteria.java:78)
        at org.junit.internal.ComparisonCriteria.arrayEquals(ComparisonCriteria.java:28)
        at org.junit.Assert.internalArrayEquals(Assert.java:534)
        at org.junit.Assert.assertArrayEquals(Assert.java:418)
        at org.junit.Assert.assertArrayEquals(Assert.java:429)
        at ArrayTests.testReverseInPlace1(ArrayTests.java:18)
        ... 32 trimmed
Caused by: java.lang.AssertionError: expected:<2> but was:<3>
        at org.junit.Assert.fail(Assert.java:89)
        at org.junit.Assert.failNotEquals(Assert.java:835)
        at org.junit.Assert.assertEquals(Assert.java:120)
        at org.junit.Assert.assertEquals(Assert.java:146)
        at org.junit.internal.ExactComparisonCriteria.assertElementsEqual(ExactComparisonCriteria.java:8)
        at org.junit.internal.ComparisonCriteria.arrayEquals(ComparisonCriteria.java:76)
        ... 38 more

FAILURES!!!
Tests run: 2,  Failures: 1

```

The code that induced this bug can be seen here:

```java
static void reverseInPlace(int[] arr) {
    for(int i = 0; i < arr.length; i += 1) {
        arr[i] = arr[arr.length - i - 1];
    }
}
```

With the bug, Java is looping over the entire array, editing it in place. Upon crossing the halfway point, Java begins to access values that have already been overwritten by earlier iterations of the for-loop! Therefore, with any array that isn't symmetric over its halfway point, any elements in the first half of the array will be lost, as they will be overwritten by the for-loop.

I fixed the bug with the following changes:

```java
static void reverseInPlace(int[] arr) {
    for(int i = 0; i < arr.length / 2; i += 1) {
        int temp = arr[i];
        arr[i] = arr[arr.length - i - 1];
        arr[arr.length - i - 1] = temp;
    }
}
```

The logic behind my fixes was to not loop over the entire array: rather, I would only loop over the first half of the array, and at each iteration, I would effectively swap "mirror" elements. Done correctly (without any unintentional overwrites), the bug would be fixed. This is best explained with an example. 

> Say we have an array `{1,2,3,4,5}`. The first iteration of my for-loop, I swap elements `1` and `5`, making sure to temporarily save `1` so that I don't lose any information through overwriting. With the array now `{5,2,3,4,1}`, I swap elements `2` and `5`. The array is now `{5,4,3,2,1}`. The for-loop stops here, because as an array with odd-numbered length (5), there is no need to swap the middle element.

## Researching Commands: `grep`

I am very curious on the usage of `grep`. I have always seen it in use, but I often struggle to understand what it's actually doing. Today, I will do a bit of research on some of the command-line options that the `grep` function provides.

### -R, -r, --recursive

I found this command-line option with `man grep`. The official documentation for this option is brief: `recursively search subdirectories listed`. However, upon further investigation, I have gathered the following: `grep` is typically used for searching a single file for a string-pattern, so this recursive option makes it possible to search multiple files at once. This is very useful in conducting wide-scale searches for a string-pattern in a directory of files. Frankly, `grep` is probably used more often with the `-r` option than it is without.

```
philliplong@Phillip-Longs-MacBook-Air docsearch % grep -r "base pair" technical/biomed | head -n 10             
technical/biomed/1471-2156-2-3.txt:          three exons. The first exon contains 279 base pairs (bp)
technical/biomed/1471-2121-3-10.txt:        both encode a 10-base pair sequence that is identical to
technical/biomed/1471-2121-3-10.txt:        the P3 sequence with an insertion of 3 base pairs at
technical/biomed/gb-2001-2-4-research0010.txt:          necessarily true because of Watson-Crick base pairing.
technical/biomed/gb-2003-4-4-r24.txt:        important considering that within a few hundred base pairs,
technical/biomed/gb-2001-2-4-research0011.txt:          sequenced to completion, yielding a 1,036 base pair (bp)
technical/biomed/1471-2229-2-3.txt:        stringency with a 300 base pair homologous 
technical/biomed/1471-2229-2-3.txt:        conducted at high stringency with a 1700 base pair
technical/biomed/1471-2229-2-3.txt:        approximately 100 base pairs for ease of visualization on
technical/biomed/1471-2229-2-3.txt:        amplification product of 579 base pairs. Primer pair T-21
```

I have truncated the output of this command to just the first 10 lines (as opposed to the full 226 that `grep` found). Here I am using `grep`'s `-r` option on the `technical/biomed` directory. This `grep` is searching within all the files in the provided directory for the string-pattern `"base pair"`. As mentioned previously, the `-r` option is useful for searching an entire directory for a string-pattern.


```
philliplong@Phillip-Longs-MacBook-Air docsearch % grep -r "base" technical | sort -R | head -n 10
technical/plos/pmed.0020050.txt:        necessary to compare ARV allocation strategies based upon the principle of efficiency
technical/biomed/gb-2002-4-1-r1.txt:            density of the base-pairs around all positive probes in
technical/plos/pmed.0020182.txt:        and nucleic acids) on a single miniaturized chip. The rapid extension of the chip-based CD4
technical/biomed/1471-2199-2-5.txt:        be directed by an imperfect immunoreceptor tyrosine-based
technical/biomed/1471-2164-2-9.txt:          dbEST databases at the NCBI, using the BLAST 2 program.
technical/biomed/1471-2199-2-5.txt:        immunoreceptor tyrosine-based inhibitory motif (ITIM)
technical/biomed/1471-2229-1-2.txt:        ligands in LHCPs are replaced with stronger Lewis bases.
technical/biomed/gb-2001-2-4-research0014.txt:        (IGI); the filter and glass-slide-based 'Atlas' arrays
technical/plos/pmed.0010062.txt:          and adiponectin were square-root transformed and insulin log transformed based on the
technical/biomed/1471-2326-2-4.txt:          baseline HIC.
```

With help from [StackOverflow](https://stackoverflow.com/questions/17578873/randomly-shuffling-lines-in-linux-bash) on how to randomly shuffle the lines of an input, I have randomly picked 10 lines from the output of this command (as opposed to showing the full 7,310 that `grep` found, as that would be too much). I am using `grep`'s `-r` option on the `technical` directory. This command is recursively searching within all files, and subfiles, and subfiles, and so on, in the provided directory for the string-pattern `"base"`. It's a good example of the recursive nature of the `-r` command, as all the subdirectories of `technical` are being searched. The `-r` option is not only useful for searching an entire directory for a string-pattern, but also all of that directory's subdirectories - and more!

### -n, --line-number

I found this command-line option with `man grep`. When using this option, for every match, `grep` will also display the line number on which the match occurred. This option is very useful for not only knowing if a pattern exists, but also knowing where that pattern is located in a file.

```
philliplong@Phillip-Longs-MacBook-Air docsearch % grep -n "base" technical/biomed/1471-2261-2-11.txt
301:          day, 3 days and 1 week followed by returning to base line
344:        remodeling studies are based on peripheral arteries, such
427:        days and 1 week followed by returning to base line level
```

Picking a random file in `technical/biomed`, I used `grep -n` to search for the string-pattern `"base"` in the file `technical/biomed/1471-2261-2-11.txt`. As you can see, `grep` outputs each line number on which the desired string-pattern occurs. This option is very useful for locating where in a file one can find a specific string.

```
philliplong@Phillip-Longs-MacBook-Air docsearch % grep -rn "base pair" technical/biomed | sort -R | head -n 10
technical/biomed/1471-2105-3-2.txt:444:            sequences and base pairs in the current structure model
technical/biomed/1471-2164-3-7.txt:203:          the detection window of 45 to 900 base pairs and
technical/biomed/1471-2164-2-1.txt:564:        date, extending over more than 1 megabase pairs of genomic
technical/biomed/1471-2229-2-3.txt:411:        amplification product of 579 base pairs. Primer pair T-21
technical/biomed/1471-2105-3-2.txt:452:            rRNA); and 3) all previously proposed base pairs that
technical/biomed/1471-2105-3-2.txt:572:            and base triples. Clicking on a position, base pair, or
technical/biomed/1471-2105-3-2.txt:182:            new secondary and tertiary structure base pairs that do
technical/biomed/1471-2229-2-3.txt:244:        stringency with a 300 base pair homologous 
technical/biomed/1471-2105-3-2.txt:261:            base pair) have the greatest amount of covariation;
technical/biomed/1471-2156-2-7.txt:262:          products, 200 and 175 base pairs (A allele). The G to A
```

Here, I am beginning to leverage the power of `grep` by combining two command-line options, `-r` and `-n`! Using the same trick I learned from [StackOverflow](https://stackoverflow.com/questions/17578873/randomly-shuffling-lines-in-linux-bash) earlier, I am displaying 10 randomly-picked matches out of the full 226 that `grep` found. Using `-r`, `grep` is searching all the files in `technical/biomed` for the string-pattern `"base pair"`, and when it matches that string, according to `-n`, it outputs the filename, line number on which the match occurred, and the portion of the line around the match. This is useful for locating where in a bunch of files a specific string occurs.

### -l, --files-with-matches

I found this command-line option with `man grep`. This option only returns the filenames, as opposed to lines, that match the specified pattern. The official documentation points out that because `grep -l` will only search a file until a match has been found, this option could potentially make searches computationally less expensive. Using the `-l` option, one could perhaps generate a list of filenames that contain a string-pattern match, and then from that list, determine on which lines a pattern is present; when dealing with a large amount of files, this approach could possibly save a lot of processing power. It should be noted that this option is almost always used alongside `-r`.

```
philliplong@Phillip-Longs-MacBook-Air docsearch % grep -l "base" technical/biomed/1471-2261-2-11.txt   
technical/biomed/1471-2261-2-11.txt
```

Using the same file in `technical/biomed` that I randomly-picked earlier, I used `grep -l` to search for the string-pattern `"base"` in the file `technical/biomed/1471-2261-2-11.txt`. This usage of `grep -l` is pretty redundant. As you can see, because `grep` found the desired string-pattern in the provided file, it outputted the filename and called it a day. Frankly, this usage of the `-l` option (with a single file) is pretty useless.

```
philliplong@Phillip-Longs-MacBook-Air docsearch % grep -rl "base pair" technical/biomed | head -n 10
technical/biomed/1471-2156-2-3.txt
technical/biomed/1471-2121-3-10.txt
technical/biomed/gb-2001-2-4-research0010.txt
technical/biomed/gb-2003-4-4-r24.txt
technical/biomed/gb-2001-2-4-research0011.txt
technical/biomed/1471-2229-2-3.txt
technical/biomed/gb-2001-2-7-research0025.txt
technical/biomed/1471-2458-3-5.txt
technical/biomed/1471-2199-2-12.txt
technical/biomed/gb-2001-2-3-research0008.txt
```

However, combining the power of `-l` with `-r`, one can begin to see where this command-line option shines. I have truncated the output of this command to just the first 10 lines (as opposed to the full 74 that `grep` found). Using `-r`, `grep` is searching all the files in `technical/biomed` for the string-pattern `"base pair"`, and as soon as it matches that string, according to `-l`, it outputs the filename. `grep` is showing all the files that contain the string `"base pair"`. This is useful for locating the files in which a specific string can be found.


### -i, --ignore-case

I found this command-line option with `man grep`. Similar to `-r`, this option is so ubiquitous that its official documentation is very brief: `perform case insensitive matching`. This option requires very little explanation: it ignores case when searching for a string-pattern. This is very useful when conducting searches for a string with little knowledge on how that string might be formatted inside of the files. For instance, if searching the `.txt` file version of a publication for a string-pattern, the `-i` option could be useful when combined with `-n`, as one could find instances of that string regardless of its location in a sentence (i.e. the first word of sentences are generally capitalized).

```
philliplong@Phillip-Longs-MacBook-Air docsearch % grep "BASE" technical/biomed/1471-2261-2-11.txt
philliplong@Phillip-Longs-MacBook-Air docsearch % grep -i "BASE" technical/biomed/1471-2261-2-11.txt
          day, 3 days and 1 week followed by returning to base line
        remodeling studies are based on peripheral arteries, such
        days and 1 week followed by returning to base line level
```

In this example, I search the same random file, `technical/biomed/1471-2261-2-11.txt`, that I've been using for the string-pattern `"BASE"`, knowing full-well that the string is not in the file. As expected, `grep` alone searches the file for the string, and returns no matches. However, upon specifying the `-i` option, `grep` returns three matches for the string `"BASE"`, case-insensitive. This option is useful for conducting a less-restrictive search for a certain string.

```
philliplong@Phillip-Longs-MacBook-Air docsearch % grep -ri "BASE" technical/biomed | sort -R | head -n 10
technical/biomed/1471-2148-3-4.txt:        (1) In the public databases, the Runt domain is
technical/biomed/gb-2002-3-12-research0081.txt:        (SQL) database that, despite having a limited set of
technical/biomed/1471-2105-2-9.txt:          end for every N occurring within 50 bases, and at the 3'
technical/biomed/gb-2000-1-2-research0003.txt:          procedure based on randomization that helps us select a
technical/biomed/1471-2148-1-14.txt:        & Henikoff [ 21 ] , which is based on the residue
technical/biomed/1472-6785-2-6.txt:          based estimate by the forest department of 36 tigers in
technical/biomed/gb-2001-2-10-research0042.txt:          27] and the FANTOM [ 28, 29] databases.
technical/biomed/1471-2296-3-3.txt:        completion of the baseline survey and the intervention and
technical/biomed/1471-2202-3-10.txt:          (see Figure 7). The raw data provide a baseline
technical/biomed/1471-2180-2-29.txt:        nucleic acid-based assays. This approach is general enough
```

As we've been doing previously, I harness the power of `-i` with `-r`. I truncated the output to a random selection of 10 lines (as opposed to the full 5,757). Using `-r`, `grep` is searching all the files in `technical/biomed` for the string-pattern `"BASE"`, but according to `-i`, `grep` is searching without regards to letter-case. It returns all matches across all the files in `technical/biomed` for any variation of `"base"`. This is useful for locating the files in which a string can be found with a more-broad of a search than usual, as we are not considering letter-case.

---

Thank you for reading my lab report. We've made it halfway through the quarter!
