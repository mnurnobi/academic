import re
import sys

input_file = open(sys.argv[1], "r")
input_text = input_file.read()


keyword = {'void': 'T_Void',
           'string': 'T_String',
           'while': 'T_While',
           'break': 'T_Break',
           'int': 'T_Int',
           'double': 'T_Double',
           'bool': 'T_Bool',
           'null': 'T_Null',
           'for': 'T_For',
           'if': 'T_If',
           'else': 'T_Else',
           'return': 'T_Return',
           'Print': 'T_Print',
           'ReadInteger': 'T_ReadInteger',
           'ReadLine': 'T_ReadLine'}

keyword_keys = keyword.keys()

bool_constants = {'true': 'T_BoolConstant',
                  'false': 'T_BoolConstant'}
bool_constants_keys = bool_constants.keys()

punctuation = {'!': '!',
               ';': ';',
               ',': ',',
               '.': '.',
               '(': '(',
               ')': ')',
               '{': '{',
               '}': '}'}

punctuation_keys = punctuation.keys()

operators = {'+': '+',
             '-': '-',
             '*': '*',
             '/': '/',
             '%': '%',
             '<': '<',
             '<=': 'T_LessEqual',
             '>': '>',
             '>=': 'T_GreaterEqual',
             '=': '=',
             '==': 'T_Equal',
             '!=': 'T_NotEqual',
             '&&': 'T_And',
             '||': 'T_Or'}

operators_keys = operators.keys()


tokenCategory = {'T_Identifier', 'T_IntConstant', 'T_DoubleConstant', 'T_StringConstant', 'T_BoolConstant'}

position = 0
numErrors = 0

LINE_COUNT = 1
COL_START = 1
COL_END = 1


# custom util methods

def custom_atoi(astr):
    num = 0
    for c in astr:
        if '0' <= c <= '9':
            num = num * 10 + ord(c) - ord('0')
    return num


# error printing related START
def printErrorMessage(line_no, msg):
    if line_no > 0:
        print("\n*** Error line ", line_no, ".")
    else:
        print("\n*** Error.")
    print("*** ", msg, "\n")


def unterminatedComment():
    printErrorMessage(0, "Input ends with unterminated comment")


def longIdentifier(line_no, identifier):
    msg = "Identifier too long: \"" + identifier + "\""
    printErrorMessage(line_no, msg)


def unterminatedString(line_no, txt):
    msg = "Unterminated string constant: " + txt
    printErrorMessage(line_no, msg)


def unrecognizedCharacter(line_no, ch):
    msg = "Unrecognized char: '" + ch + "'"
    printErrorMessage(line_no, msg)


# error printing related END


def isKeyword(identifier):
    global LINE_COUNT, COL_START
    # this is the initial state for the identifiers
    if identifier in keyword_keys:
        print(identifier, "line", LINE_COUNT, "cols", COL_START, "-", COL_END, "is", keyword.get(identifier))
        COL_START = COL_END + 1
        return True
    elif identifier in bool_constants_keys:
        print(identifier, "line", LINE_COUNT, "cols", COL_START, "-", COL_END, "is T_BoolConstant (value =", identifier, ")")
        COL_START = COL_END + 1
        return True
    else:
        return False


def nextState(current_state, text, cursor):
    global LINE_COUNT, COL_START
    # this is the initial state for the identifiers
    if current_state == 1:
        if re.match('[a-zA-Z]', text[cursor]):
            return 2
    elif current_state == 2:
        if re.match('[a-zA-Z0-9_]', text[cursor]):
            return 2
        else:
            return -4  # it could be valid identifier

    # this is the initial state for the Strings
    if current_state == 3:
        if text[cursor] == '"':
            return 4
    elif current_state == 4:
        if text[cursor] == '"':
            return -5  # it could be valid string
        elif text[cursor] == '\n':  # to check end of line; possibly we don't need this, we will handle boundary check
            return -6  # it could be ERROR
        else:
            return 4  # continue exploring string

    # this is the initial state for the comments (both single and multi-line)
    if current_state == 5:
        if text[cursor] == '/':
            return 6
    elif current_state == 6:
        if text[cursor] == '/':
            return 7
        elif text[cursor] == '*':
            return 8
        else:
            return -7  # it could be an arithmetic / operator
    elif current_state == 7:
        if text[cursor] == '\n':  # until new line
            LINE_COUNT = LINE_COUNT + 1
            COL_START = 1
            return -8  # its a single line comments
        else:
            return 7
    elif current_state == 8:
        if text[cursor] == '*':
            return 9
        else:
            if text[cursor] == '\n':
                LINE_COUNT = LINE_COUNT + 1  # to increment line count while any new found
                COL_START = 1
            return 8  # continue what ever you find till end of file
    elif current_state == 9:
        if text[cursor] == '/':
            return -9  # it could is a successful multi-line comments
        elif text[cursor] == '*':
            return 9
        else:
            return 8

    # this is the initial state for the numbers
    if current_state == 12:
        if re.match('[0-9]', text[cursor]):
            return 13
        elif text[cursor] == 'E' or text[cursor] == 'e':
            return 16
    elif current_state == 13:
        if re.match('[0-9]', text[cursor]):
            return 13
        elif text[cursor] == '.':
            return 14
        else:
            return -1
    elif current_state == 14:
        if re.match('[0-9]', text[cursor]):
            return 15
        elif text[cursor] == 'E' or text[cursor] == 'e':
            return 16
        else:
            return -2
    elif current_state == 15:
        if re.match('[0-9]', text[cursor]):
            return 15
        elif text[cursor] == 'E' or text[cursor] == 'e':
            return 16
        else:
            return -2
    elif current_state == 16:
        if re.match('[0-9]', text[cursor]):  # it is also allowed numbers like 1.2E67
            return 18
        elif text[cursor] == '+' or text[cursor] == '-':
            return 17
        else:
            return -3
    elif current_state == 17:
        if re.match('[0-9]', text[cursor]):
            return 18
        else:
            return -10  # to caught something like 1.23E+a or 1.23E-+
    elif current_state == 18:
        if re.match('[0-9]', text[cursor]):
            return 18
        else:
            return -3
    # case 19 and return -10 is reserved for the integer related things

    # this is the initial state for the logical operators <, >, <=, >=, =, ==, !=
    elif current_state == 20:
        if text[cursor] == '<':
            return 21
        elif text[cursor] == '>':
            return 22
        elif text[cursor] == '=':
            return 23
        elif text[cursor] == '!':
            return 24
        elif text[cursor] == '|':
            return 25
        elif text[cursor] == '&':
            return 26
    elif current_state == 21:
        if text[cursor] == '=':
            return -11  # LE
        else:
            return -12  # LT and backspace
    elif current_state == 22:
        if text[cursor] == '=':
            return -13  # GE
        else:
            return -14  # GT and backspace
    elif current_state == 23:
        if text[cursor] == '=':
            return -15  # EQ
        else:
            return -16  # Assign and backspace
    elif current_state == 24:
        if text[cursor] == '=':
            return -17  # Not Equal
        else:
            return -18  # Not and backspace
    elif current_state == 25:
        if text[cursor] == '|':
            return -19  # OR
        else:
            return -20  # Symbol and backspace
    elif current_state == 26:
        if text[cursor] == '&':
            return -21  # And
        else:
            return -20  # Symbol and backspace


line = input_text

# splitting input program into lines for further processing
initial_state = 1
next_state = 0
line_length = 0
new_token = True
start = 0
end = 0
skip = False

line_length = len(line)
i = 0
# temporary solution to handle input without having any newline at the end
lastChar = line[line_length - 1]
if lastChar != '\n':
    line = line + '\n'
    line_length = line_length + 1

# for i in range(line_length):
while i < line_length:
    skip = False

    if new_token is True and line[i] == '\n':
        LINE_COUNT = LINE_COUNT + 1
        COL_START = 1

    # states initialization goes here
    if new_token is True:
        if re.match('[a-zA-Z]', line[i]):
            initial_state = 1
            start = i
        elif line[i] == '"':
            initial_state = 3
            start = i
        elif line[i] == '/':
            initial_state = 5
            start = i
        elif re.match('[0-9]', line[i]):
            initial_state = 12
            start = i
        elif re.match('[<>=!&|]', line[i]):  # to determine logical operators
            initial_state = 20
            start = i

        # these conditions are to control for single operator or symbol
        elif re.match('[-+*/%]', line[i]):
            initial_state = 0
            start = i
            end = i
            new_token = True
            COL_END = COL_START + (end - start)
            print(line[start:end + 1], "line", LINE_COUNT, "cols", COL_START, "-", COL_END, "is '", line[start:end + 1], "'")
            skip = True  # skip others finite automata's
            i = i + 1  # increment i of while loop
            COL_START = COL_END + 1
        elif re.match('[;,.[\]{}()]', line[i]):
            initial_state = 0
            start = i
            end = i
            new_token = True
            COL_END = COL_START + (end - start)
            print(line[start:end + 1], "line", LINE_COUNT, "cols", COL_START, "-", COL_END, "is '", line[start:end + 1],
                  "'")
            skip = True
            i = i + 1  # increment i of while loop
            COL_START = COL_END + 1
        elif line[i] == ' ' or line[i] == '\n':
            initial_state = 0
            start = i
            end = i
            new_token = True
            skip = True
            if line[i] == ' ':
                COL_START = COL_START + 1
            i = i + 1  # increment i of while loop
        else:
            initial_state = 0
            start = i
            end = i
            new_token = True
            COL_END = COL_START + (end - start)
            unrecognizedCharacter(LINE_COUNT, line[start:end + 1])
            skip = True
            COL_START = COL_START + 1
            i = i + 1  # increment i of while loop

    # states initialization ends here

    if skip is False:
        next_state = nextState(initial_state, line, i)
        initial_state = next_state
        new_token = False
        end = i
        COL_END = COL_START + (end - start) - 1

        if next_state < 0:  # all the acceptance states are negative
            if next_state == -1:
                new_token = True
                i = i - 1  # backspace
                print(line[start:end], "line", LINE_COUNT, "cols", COL_START, "-", COL_END, "is T_IntConstant (value=",
                      int(line[start:end]), ")")
                COL_START = COL_END + 1
            elif next_state == -2:
                new_token = True
                i = i - 1  # backspace
                print(line[start:end], "line", LINE_COUNT, "cols", COL_START, "-", COL_END, "is T_DoubleConstant (value=",
                      float(line[start:end]), ")")
                COL_START = COL_END + 1
            elif next_state == -3:
                new_token = True
                i = i - 1  # backspace
                print(line[start:end], "line", LINE_COUNT, "cols", COL_START, "-", COL_END,
                      "is T_DoubleConstant (value=",
                      int(float(line[start:end])), ")")
                COL_START = COL_END + 1
            elif next_state == -10:
                new_token = True
                i = i - 3  # 3 backspaces #to caught something like 1.23E+ or 1.23E-
                COL_END = COL_END - 2
                print(line[start:end - 2], "line", LINE_COUNT, "cols", COL_START, "-", COL_END,
                      "is T_DoubleConstant (value=",
                      float(line[start:end - 2]), ")")
                COL_START = COL_END + 1
            elif next_state == -4:
                new_token = True
                i = i - 1  # backspace
                if len(line[start:end]) > 31:
                    longIdentifier(LINE_COUNT, line[start:end])
                    truncated_identifier = "is T_Identifier (truncated to " + line[start:start + 31] + ")"
                    print(line[start:end], "line", LINE_COUNT, "cols", COL_START, "-", COL_END, truncated_identifier)
                    COL_START = COL_END + 1

                elif isKeyword(line[start:end]) is False:
                    print(line[start:end], "line", LINE_COUNT, "cols", COL_START, "-", COL_END, "is T_Identifier")
                    COL_START = COL_END + 1
            elif next_state == -5:
                new_token = True
                COL_END = COL_END + 1
                print(line[start:end + 1], "line", LINE_COUNT, "cols", COL_START, "-",
                      COL_END, "is T_StringConstant (value = ", line[start:end + 1], ")")
                COL_START = COL_END + 1
            elif next_state == -6:
                new_token = True
                i = i - 1  # backspace
                unterminatedString(LINE_COUNT, line[start:end])
                COL_START = COL_END + 1
            elif next_state == -7:
                new_token = True
                i = i - 1  # backspace
                end = i
                print("/ line", LINE_COUNT, "cols", COL_START, "-", COL_END, "is '/'")
                COL_START = COL_END + 1
            elif next_state == -8:
                new_token = True
                # print("T_SingleLine Comment, value=", line[start:end])
                COL_START = COL_END + 1
            elif next_state == -9:
                new_token = True
                # print("T_Multi-line Comment, value=\n", line[start:end + 1])  # end+1 to show with separator '/'
                COL_START = COL_END + 1

            # following acceptance states are for logical operators
            elif next_state == -11:
                new_token = True
                COL_END = COL_END + 1
                print(line[start:end + 1], "line", LINE_COUNT, "cols", COL_START, "-", COL_END, "is",
                      operators.get(line[start:end + 1]))
                COL_START = COL_END + 1
            elif next_state == -12:
                new_token = True
                i = i - 1  # backspace
                print(line[start:end], "line", LINE_COUNT, "cols", COL_START, "-", COL_END, "is '",
                      line[start:end], "'")
                COL_START = COL_END + 1
            elif next_state == -13:
                new_token = True
                COL_END = COL_END + 1
                print(line[start:end + 1], "line", LINE_COUNT, "cols", COL_START, "-", COL_END, "is",
                      operators.get(line[start:end + 1]))
                COL_START = COL_END + 1
            elif next_state == -14:
                new_token = True
                i = i - 1  # backspace
                print(line[start:end], "line", LINE_COUNT, "cols", COL_START, "-", COL_END, "is '",
                      line[start:end], "'")
                COL_START = COL_END + 1
            elif next_state == -15:
                new_token = True
                COL_END = COL_END + 1
                print(line[start:end + 1], "line", LINE_COUNT, "cols", COL_START, "-", COL_END, "is",
                      operators.get(line[start:end + 1]))
                COL_START = COL_END + 1
            elif next_state == -16:
                new_token = True
                i = i - 1  # backspace
                print(line[start:end], "line", LINE_COUNT, "cols", COL_START, "-", COL_END, "is '",
                      line[start:end], "'")
                COL_START = COL_END + 1
            elif next_state == -17:
                new_token = True
                COL_END = COL_END + 1
                print(line[start:end + 1], "line", LINE_COUNT, "cols", COL_START, "-", COL_END, "is",
                      operators.get(line[start:end + 1]))
                COL_START = COL_END + 1
            elif next_state == -18:
                new_token = True
                i = i - 1  # backspace
                print(line[start:end], "line", LINE_COUNT, "cols", COL_START, "-", COL_END, "is '",
                      line[start:end], "'")
                COL_START = COL_END + 1
            elif next_state == -19:
                new_token = True
                COL_END = COL_END + 1
                print(line[start:end + 1], "line", LINE_COUNT, "cols", COL_START, "-", COL_END, "is",
                      operators.get(line[start:end + 1]))
                COL_START = COL_END + 1
            elif next_state == -20:
                new_token = True
                i = i - 1  # backspace
                unrecognizedCharacter(LINE_COUNT, line[start:end])
                COL_START = COL_END + 1
            elif next_state == -21:
                new_token = True
                COL_END = COL_END + 1
                print(line[start:end + 1], "line", LINE_COUNT, "cols", COL_START, "-", COL_END, "is",
                      operators.get(line[start:end + 1]))
                COL_START = COL_END + 1

        # end of acceptance states
        i = i + 1


input_file.close()
