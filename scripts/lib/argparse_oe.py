import sys
import argparse

class ArgumentParser(argparse.ArgumentParser):
    """Our own version of argparse's ArgumentParser"""

    def error(self, message):
        sys.stderr.write('ERROR: %s\n' % message)
        self.print_help()
        sys.exit(2)

