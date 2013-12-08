EmailLogParser
==============

Java program that parses email log into groups.

IMagine you have a mail server log. It has many lines. Each line tell a Person A sent an email to Person B. There are two rules:

1. If a person X sent an email to Person Y, they are ina same group. Similarly, if Y also  sent an email to Z, Z is in the same group.

2. If Person A didn't send any email to any person in a group or didn'r recieve any email form any perdon in a group, A should not  be in the group. 

So, there may be many groups from the server log. Please implement a Java program to process the log and eventually  for every group print out every member in the group. 

First example:
Y Z
X Y
K X
A B
C A
M L
The program should print:

There are 3 groups:
Group 1: Y,Z,X,K
Group 2: A,B,C


Second example:

A C
B C
 The program should print:
 There is one group
 Group 1: A, C, B