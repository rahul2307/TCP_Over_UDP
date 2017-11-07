# TCP_Over_UDP

# Project by:-
Roll no:14115017 Anushree Ghosh

Roll no:14115038 Harshil Gupta

Roll no:14115041 Inakollu Neha Priyanka

Roll no:14115053 Meenakshi Choudhary

Roll no:14115073 Rahul Baghel
# Build Instructions:
# Windows:
1.Open command prompt

2.From cmd go to the directory containing the project and compile all the files
 
                              javac *.java
3.Type the following in command prompt to run the server
                        
                        java Server <server portno>
                        ex. java Server 7777
4.Type the following in command prompt to run the server
                        
                        java Client <client portno> <127.0.0.1> <server portno>
                        ex. java Client 4444 127.0.0.1 7777
# Unix:
1.Open terminal

2.From terminal go to the directory containing the project and compile all the files
 
                              javac *.java
3.Type the following in terminal to run the server
                        
                        java Server <server portno>
                        ex. java Server 7777
4.Type the following in terminal to run the server
                        
                        java Client <client portno> <127.0.0.1> <server portno>
                        ex. java Client 4444 127.0.0.1 7777
# Notes
"data.in" contains the data we want to send.After the whole data is sent through server it initiates a four way hadnshaking to close to connection .
