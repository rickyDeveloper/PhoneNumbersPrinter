# PhoneNumbersPrinter

1-800-CODING-CHALLENGE
Many companies like to list their phone numbers using the letters printed on most telephones. This makes the number easier to remember for customers. An example may be 1-800-FLOWERS
This coding challenge is to write a program that will show a user possible matches for a list of provided phone numbers.
Your program should be a command line application that reads from files specified as command-line arguments or STDIN when no files are given. Each line of these files will contain a single phone number.
For each phone number read, your program should output all possible word replacements from a dictionary. Your program should try to replace every digit of the provided phone number with a letter from a dictionary word; however, if no match can be made, a single digit can be left as is at that point. No two consecutive digits can remain unchanged and the program should skip over a number (producing no output) if a match cannot be made.
Your program should allow the user to set a dictionary with the -d command-line option, but it's fine to use a reasonable default for your system. The dictionary is expected to have one word per line.
All punctuation and whitespace should be ignored in both phone numbers and the dictionary file. The program should not be case sensitive, letting "a" == "A". Output should be capital letters and digits separated at word boundaries with a single dash (-), one possible word encoding per line. For example, if your program is fed the number:
  2255.63
One possible line of output is
  CALL-ME
According to my dictionary.
The number encoding on the phone the program will use is:
DIGIT
CHARACTERS
2
ABC
3
DEF
4
GHI
5
JKL
 
DIGIT
CHARACTERS
6
MNO
7
PQRS
8
TUV
9
WXYZ
