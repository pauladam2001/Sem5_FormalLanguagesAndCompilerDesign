from grammar import Grammar
from node import Node
from parserOutput import ParserOutput


def read_sequence(sequence_file):
    sequence = []

    with open(sequence_file) as file:
        line = file.readline()
        while line:
            sequence.append(line[0:-1])
            line = file.readline()

    return sequence


def init_output_file(output_file):
    file = open(output_file, 'w')
    file.write("")
    file.close()


class RecursiveDescendentParser:
    def __init__(self, grammar_file, sequence_file, output_file):
        self.grammar = Grammar(grammar_file)
        self.sequence = read_sequence(sequence_file)
        self.output_file = output_file
        init_output_file(output_file)

        self.input_stack = [self.grammar.get_start_symbol()[0]]
        self.working_stack = []
        self.state = 'n'    # "n" - normal state, "b" - back state, "f" - final state, "e" - error state
        self.index = 0      # position of the current symbol in the input sequence
        self.tree = []

        self.parserOutput = ParserOutput(self)

    def expand(self):
        # when the head of the input stack is a non terminal
        # (q, i, alpha, A beta) ⊢ (q, i, alpha A1, gamma1 beta)

        self.write_in_output_file("Expand")
        non_terminal = self.input_stack.pop(0)
        self.working_stack.append((non_terminal, 0))
        new_production = self.grammar.get_productions_for_non_terminal(non_terminal)[0]
        self.input_stack = new_production + self.input_stack

    def advance(self):
        # when the head of the input stack is a terminal == current symbol from input
        # (q, i, alpha, a_i beta) ⊢ (q, i+1, alpha a_i, beta)

        self.write_in_output_file("Advance")
        self.working_stack.append(self.input_stack.pop(0))
        self.index += 1

    def momentary_insuccess(self):
        # when the head of the input stack is a terminal != current symbol from input

        self.write_in_output_file("Momentary Insuccess")
        self.state = 'b'

    def back(self):
        # when the head of the working stack is a terminal
        # (b, i, alpha a, beta) ⊢ (b, i-1, alpha, a beta)

        self.write_in_output_file("Back")
        new_el = self.working_stack.pop()
        self.input_stack = [new_el] + self.input_stack
        self.index -= 1
        # self.state = "b"

    def another_try(self):
        self.write_in_output_file("Another Try")
        last = self.working_stack.pop()  # (last, production_nr)

        if last[1] + 1 < len(self.grammar.get_productions_for_non_terminal(last[0])):
            self.state = 'n'

            # put working next production for the symbol
            new_tuple = (last[0], last[1] + 1)
            self.working_stack.append(new_tuple)

            # change production on top input
            length_last_production = len(self.grammar.get_productions_for_non_terminal(last[0])[last[1]])

            # delete last production from input
            self.input_stack = self.input_stack[length_last_production:]

            # put new production in input
            new_production = self.grammar.get_productions_for_non_terminal(last[0])[last[1] + 1]
            self.input_stack = new_production + self.input_stack
        elif self.index == 0 and last[0] == self.grammar.get_start_symbol()[0]:
            self.state = 'e'
        else:
            # change production on top input
            length_last_production = len(self.grammar.get_productions_for_non_terminal(last[0])[last[1]])

            # delete last production from input
            self.input_stack = self.input_stack[length_last_production:]
            self.input_stack = [last[0]] + self.input_stack

    def success(self):
        self.write_in_output_file("Success")
        self.state = 'f'

    def write_in_output_file(self, message, final=False):
        with open(self.output_file, 'a') as file:
            if final:
                file.write("\nResult:\n")
            file.write(message + "\n")

    def write_all_data(self):
        with open(self.output_file, 'a') as file:
            file.write(str(self.state) + " ")
            file.write(str(self.index) + "\n")
            file.write(str(self.working_stack) + "\n")
            file.write(str(self.input_stack) + "\n")

    def print_working_stack(self):
        self.write_in_output_file(str(self.working_stack))

    def get_length_depth(self, index):
        production = self.grammar.get_productions()[self.working_stack[index][0]][self.working_stack[index][1]]
        length_of_production = len(production)
        sum = length_of_production

        for i in range(1, length_of_production + 1):
            if type(self.working_stack[index + i]) == tuple:
                sum += self.get_length_depth(index + i)

        return sum

    def create_parsing_tree(self):
        father = -1

        for index in range(0, len(self.working_stack)):
            if type(self.working_stack[index]) == tuple:
                self.tree.append(Node(self.working_stack[index][0]))
                self.tree[index].production = self.working_stack[index][1]
            else:
                self.tree.append(Node(self.working_stack[index]))

        for index in range(0, len(self.working_stack)):
            if type(self.working_stack[index]) == tuple:
                self.tree[index].father = father
                father = index
                len_prod = len(self.grammar.get_productions()[self.working_stack[index][0]][self.working_stack[index][1]])  # computes the length of the production of a non terminal
                vector_index = []

                for i in range(1, len_prod + 1):
                    vector_index.append(index + i)

                for i in range(0, len_prod):
                    if self.tree[vector_index[i]].production != -1:
                        offset = self.get_length_depth(vector_index[i])
                        for j in range(i + 1, len_prod):
                            vector_index[j] += offset

                for i in range(0, len_prod - 1):
                    self.tree[vector_index[i]].sibling = vector_index[i + 1]
            else:
                self.tree[index].father = father
                father = -1

    def run(self, seq):
        while (self.state != 'f') and (self.state != 'e'):
            self.write_all_data()
            if self.state == 'n':
                if len(self.input_stack) == 0 and self.index == len(seq):
                    self.success()
                elif len(self.input_stack) == 0:
                    self.momentary_insuccess()
                elif self.input_stack[0] in self.grammar.get_non_terminals():
                    # when the head of the input stack is a non terminal
                    self.expand()
                elif self.index < len(seq) and self.input_stack[0] == seq[self.index]:
                    self.advance()
                else:
                    # when the head of the input stack is a terminal != current symbol from input
                    self.momentary_insuccess()
            elif self.state == 'b':
                if self.working_stack[-1] in self.grammar.get_terminals():
                    self.back()
                else:
                    self.another_try()

        if self.state == 'e':
            message = "Error at index {}!".format(self.index)
        else:
            message = "Sequence is accepted!"
            self.print_working_stack()

        self.write_in_output_file(message, True)
        self.create_parsing_tree()
        self.parserOutput.write_parsing_tree()


parser = RecursiveDescendentParser("g1.txt", "seq.txt", "out1.txt")
parser.run(parser.sequence)
