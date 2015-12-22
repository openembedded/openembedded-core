import sys
import argparse

class ArgumentUsageError(Exception):
    """Exception class you can raise (and catch) in order to show the help"""
    def __init__(self, message, subcommand=None):
        self.message = message
        self.subcommand = subcommand

class ArgumentParser(argparse.ArgumentParser):
    """Our own version of argparse's ArgumentParser"""

    def error(self, message):
        sys.stderr.write('ERROR: %s\n' % message)
        self.print_help()
        sys.exit(2)

    def error_subcommand(self, message, subcommand):
        if subcommand:
            for action in self._actions:
                if isinstance(action, argparse._SubParsersAction):
                    for choice, subparser in action.choices.items():
                        if choice == subcommand:
                            subparser.error(message)
                            return
        self.error(message)

    def add_subparsers(self, *args, **kwargs):
        ret = super(ArgumentParser, self).add_subparsers(*args, **kwargs)
        ret._parser_class = ArgumentSubParser
        return ret

class ArgumentSubParser(ArgumentParser):
    def parse_known_args(self, args=None, namespace=None):
        # This works around argparse not handling optional positional arguments being
        # intermixed with other options. A pretty horrible hack, but we're not left
        # with much choice given that the bug in argparse exists and it's difficult
        # to subclass.
        # Borrowed from http://stackoverflow.com/questions/20165843/argparse-how-to-handle-variable-number-of-arguments-nargs
        # with an extra workaround (in format_help() below) for the positional
        # arguments disappearing from the --help output, as well as structural tweaks.
        # Originally simplified from http://bugs.python.org/file30204/test_intermixed.py
        positionals = self._get_positional_actions()
        for action in positionals:
            # deactivate positionals
            action.save_nargs = action.nargs
            action.nargs = 0

        namespace, remaining_args = super(ArgumentSubParser, self).parse_known_args(args, namespace)
        for action in positionals:
            # remove the empty positional values from namespace
            if hasattr(namespace, action.dest):
                delattr(namespace, action.dest)
        for action in positionals:
            action.nargs = action.save_nargs
        # parse positionals
        namespace, extras = super(ArgumentSubParser, self).parse_known_args(remaining_args, namespace)
        return namespace, extras

    def format_help(self):
        # Quick, restore the positionals!
        positionals = self._get_positional_actions()
        for action in positionals:
            if hasattr(action, 'save_nargs'):
                action.nargs = action.save_nargs
        return super(ArgumentParser, self).format_help()
