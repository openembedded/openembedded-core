import logging
import os
import oe.classutils
import shlex
from bb.process import Popen, ExecutionError

logger = logging.getLogger('BitBake.OE.Terminal')


class UnsupportedTerminal(StandardError):
    pass

class NoSupportedTerminals(StandardError):
    pass


class Registry(oe.classutils.ClassRegistry):
    command = None

    def __init__(cls, name, bases, attrs):
        super(Registry, cls).__init__(name.lower(), bases, attrs)

    @property
    def implemented(cls):
        return bool(cls.command)


class Terminal(Popen):
    __metaclass__ = Registry

    def __init__(self, command, title=None, env=None):
        self.format_command(command, title)

        try:
            Popen.__init__(self, self.command, env=env)
        except OSError as exc:
            import errno
            if exc.errno == errno.ENOENT:
                raise UnsupportedTerminal(self.name)
            else:
                raise

    def format_command(self, command, title):
        fmt = {'title': title or 'Terminal', 'command': command}
        if isinstance(self.command, basestring):
            self.command = shlex.split(self.command.format(**fmt))
        else:
            self.command = [element.format(**fmt) for element in self.command]

class XTerminal(Terminal):
    def __init__(self, command, title=None, env=None):
        Terminal.__init__(self, command, title, env)
        if not os.environ.get('DISPLAY'):
            raise UnsupportedTerminal(self.name)

class Gnome(XTerminal):
    command = 'gnome-terminal --disable-factory -t "{title}" -x {command}'
    priority = 2

class Konsole(XTerminal):
    command = 'konsole -T "{title}" -e {command}'
    priority = 2

class XTerm(XTerminal):
    command = 'xterm -T "{title}" -e {command}'
    priority = 1

class Rxvt(XTerminal):
    command = 'rxvt -T "{title}" -e {command}'
    priority = 1

class Screen(Terminal):
    command = 'screen -D -m -t "{title}" -S devshell {command}'

    def __init__(self, command, title=None, env=None):
        Terminal.__init__(self, command, title, env)
        logger.warn('Screen started. Please connect in another terminal with '
                    '"screen -r devshell"')


def prioritized():
    return Registry.prioritized()

def spawn_preferred(command, title=None, env=None):
    """Spawn the first supported terminal, by priority"""
    for terminal in prioritized():
        try:
            spawn(terminal.name, command, title, env)
            break
        except UnsupportedTerminal:
            continue
    else:
        raise NoSupportedTerminals()

def spawn(name, command, title=None, env=None):
    """Spawn the specified terminal, by name"""
    logger.debug(1, 'Attempting to spawn terminal "%s"', name)
    try:
        terminal = Registry.registry[name]
    except KeyError:
        raise UnsupportedTerminal(name)

    pipe = terminal(command, title, env)
    output = pipe.communicate()[0]
    if pipe.returncode != 0:
        raise ExecutionError(pipe.command, pipe.returncode, output)
