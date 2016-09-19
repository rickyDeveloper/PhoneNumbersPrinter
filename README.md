# PhoneNumbersPrinter

======= Problem Statement (based on my understanding) ===========
Given:-

We will be given a dictionary of words. Each line of dictionary containing one word.
A word can have punctuation and white spaces in it.

We will be provided with phone numbers in an input file or on STDIN

Aim:-

For each phone number read, this program should output all possible word replacements from a dictionary.
This program should try to replace every digit of the provided phone number with a letter from a dictionary word;
however, if no match can be made, a single digit can be left as is at that point.
No two consecutive digits can remain unchanged and the program should skip over a number (producing no output) if a match cannot be made.


How to run this program:-
-------------------------

Jdk 1.8 or above version is required.

For testing we need junit-4.10.jar, hamcrest-core-1.1.jar, mockito-core-1.10.19.jar & objenesis-2.1.jar

If you want to recompile the project then you will need Gradle 2.11


Go to root directory of project i.e. in PhoneNumbersMatcher folder

Option 1:-

    Run the program with default dictionary availabe in project.  Phone number will be entered on STDIN

    Command --> java -cp ./build/classes/main/ com.naiyarv.Main -d src/resources/dictionary.txt


Option 2:-

    Run the program with use provided dictionary availabe in project.  Phone number will be entered on STDIN

    Command --> java -cp ./build/classes/main/ com.naiyarv.Main -d ${PATH_OF_DICTIONARY}


Option 3:-

    Run the program with default dictionary availabe in project.  Use default phone number files provided in project

    Command --> java -cp ./build/classes/main/ com.naiyarv.Main -d src/resources/dictionary.txt src/resources/phoneNumbers.txt src/resources/phoneNumbers_1.txt


Option 4:-

    Run the program with use provided dictionary availabe in project.  Phone number files will be provided as argument.

    Command --> java -cp ./build/classes/main/ com.naiyarv.Main -d ${PATH_OF_DICTIONARY}  ${PATH_OF_PHONE_FILE_1} ${PATH_OF_PHONE_FILE_2}






