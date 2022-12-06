class ParserOutput:

    def __init__(self, parser):
        self.parser = parser

    def write_parsing_tree(self):
        if self.parser.state != "e":
            self.parser.write_in_output_file("\nParsing Tree: ")
            self.parser.write_in_output_file("Index Info Parent Left_sibling")
            for index in range(0, len(self.parser.working_stack)):
                message = str(index) + "  " + str(self.parser.tree[index])
                self.parser.write_in_output_file(message)
