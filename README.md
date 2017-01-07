# MinLang
A minimal languge interpreter/interpreter template.

The purpose of this project was to create a truly minimal programming language interpretator whose code
functionality can easily be replicated in other programming languages.

The syntax is very simple and is influenced by assembly code syntax to a degree, the commands are:
"def, step, cmp, inc, dec, out, end".

The "def" command creates and defines a variable, so:
def x : 1
would create a variable x and assign the value 1 to it. You can also assign strings, floats
and even other already defined variables, like so:
def y : 1.1
def z : "words words words"
def x : y

The "step" command tells the interpreter to skip forwards or backwards in code, so:
step 3
def x : 3
def x : 2
def x : 1
would mean that the "def x : 3" & "def x : 2" lines would be skipped (or stepped over if you will).
The step instruction also allows going backwards in code, this is done by using negative integers
it would look something like this:
step -3
The interpreter will not allow you to step beyond the margins of code, so if the code is 10 lines
and at line 8 you try to run the command "step 3", the interpreter will return an error. Also
be aware of the potential for infinite loops.

The "cmp" command compares two variables to each other, if they are equal the next line executes,
other wise the next line is skipped, so:
cmp 3 : x
def y : 2
def z : 1
if x is equal to 3 then the "def y : 2" line will be interpreted otherwise the interpreter will skip
to the next line. The comparison also applies to strings and etc.

The "inc" and "dec" increment or decrement the give variable by one respectively, so:
def x : 1
inc x
would increment the variable x by 1.
def x : 4
dec x
would decrement the variable x by 1.
The commands also work on foats.

The "out" command the specified output and/or variables, so:
out "Hi!"
would output the string "Hi!", you can also intermix wariables and strings together, like this:
def x : "word2"
out "word1" x "word2"
would output "word1word2word3". You can also use next line "\n" or tab "\t", like so:
out "lhs" \t "rhs"
would print out "lhs  rhs".
out "top" \n "bottom"
would print out:
top
bottom
Using the command alone without any arguments, will simply print out a empty line.

The "end" command will stop the interpreter, its purpose is to end
loops mostly, for instance:
def x : 0
cmp x : 10
end
out x \n
inc x
step -4
will output the numbers 0 to 9, if not for the "end" command this would loop forever.
