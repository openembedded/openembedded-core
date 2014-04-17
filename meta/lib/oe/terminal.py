import logging
import oe.classutils
import shlex
from bb.process import Popen, ExecutionError

logger = logging.getLogger('BitBake.OE.Terminal')


class UnsupportedTerminal(Exception):
    pass

class NoSupportedTerminals(Exception):
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

    def __init__(self, sh_cmd, title=None, env=None, d=None):
        fmt_sh_cmd = self.format_command(sh_cmd, title)
        try:
            Popen.__init__(self, fmt_sh_cmd, env=env)
        except OSError as exc:
            import errno
            if exc.errno == errno.ENOENT:
                raise UnsupportedTerminal(self.name)
            else:
                raise

    def format_command(self, sh_cmd, title):
        fmt = {'title': title or 'Terminal', 'command': sh_cmd}
        if isinstance(self.command, basestring):
            return shlex.split(self.command.format(**fmt))
        else:
            return [element.format(**fmt) for element in self.command]

class XTerminal(Terminal):
    def __init__(self, sh_cmd, title=None, env=None, d=None):
        Terminal.__init__(self, sh_cmd, title, env, d)
        if not os.environ.get('DISPLAY'):
            raise UnsupportedTerminal(self.name)

class Gnome(XTerminal):
    command = 'gnome-terminal -t "{title}" -x {command}'
    priority = 2

class Mate(XTerminal):
    command = 'mate-terminal -t "{title}" -x {command}'
    priority = 2

class Xfce(XTerminal):
    command = 'xfce4-terminal -T "{title}" -e "{command}"'
    priority = 2

class Konsole(XTerminal):
    command = 'konsole -T "{title}" -e {command}'
    priority = 2

    def __init__(self, sh_cmd, title=None, env=None, d=None):
        # Check version
        vernum = check_konsole_version("konsole")
        if vernum:
            if vernum.split('.')[0] == "2":
                logger.debug(1, 'Konsole from KDE 4.x will not work as devshell, skipping')
                raise UnsupportedTerminal(self.name)
        XTerminal.__init__(self, sh_cmd, title, env, d)

class XTerm(XTerminal):
    command = 'xterm -T "{title}" -e {command}'
    priority = 1

class Rxvt(XTerminal):
    command = 'rxvt -T "{title}" -e {command}'
    priority = 1

class Screen(Terminal):
    command = 'screen -D -m -t "{title}" -S devshell {command}'

    def __init__(self, sh_cmd, title=None, env=None, d=None):
        s_id = "devshell_%i" % os.getpid()
        self.command = "screen -D -m -t \"{title}\" -S %s {command}" % s_id
        Terminal.__init__(self, sh_cmd, title, env, d)
        msg = 'Screen started. Please connect in another terminal with ' \
            '"screen -r %s"' % s_id
        if (d):
            bb.event.fire(bb.event.LogExecTTY(msg, "screen -r %s" % s_id,
                                              0.5, 10), d)
        else:
            logger.warn(msg)

class TmuxRunning(Terminal):
    """Open a new pane in the current running tmux window"""
    name = 'tmux-running'
    command = 'tmux split-window "{command}"'
    priority = 2.75

    def __init__(self, sh_cmd, title=None, env=None, d=None):
        if not bb.utils.which(os.getenv('PATH'), 'tmux'):
            raise UnsupportedTerminal('tmux is not installed')

        if not os.getenv('TMUX'):
            raise UnsupportedTerminal('tmux is not running')

        Terminal.__init__(self, sh_cmd, title, env, d)

class Tmux(Terminal):
    """Start a new tmux session and window"""
    command = 'tmux new -d -s devshell -n devshell "{command}"'
    priority = 0.75

    def __init__(self, sh_cmd, title=None, env=None, d=None):
        if not bb.utils.which(os.getenv('PATH'), 'tmux'):
            raise UnsupportedTerminal('tmux is not installed')

        # TODO: consider using a 'devshell' session shared amongst all
        # devshells, if it's already there, add a new window to it.
        window_name = 'devshell-%i' % os.getpid()

        self.command = 'tmux new -d -s {0} -n {0} "{{command}}"'.format(window_name)
        Terminal.__init__(self, sh_cmd, title, env, d)

        attach_cmd = 'tmux att -t {0}'.format(window_name)
        msg = 'Tmux started. Please connect in another terminal with `tmux att -t {0}`'.format(window_name)
        if d:
            bb.event.fire(bb.event.LogExecTTY(msg, attach_cmd, 0.5, 10), d)
        else:
            logger.warn(msg)

class Custom(Terminal):
    command = 'false' # This is a placeholder
    priority = 3

    def __init__(self, sh_cmd, title=None, env=None, d=None):
        self.command = d and d.getVar('OE_TERMINAL_CUSTOMCMD', True)
        if self.command:
            if not '{command}' in self.command:
                self.command += ' {command}'
            Terminal.__init__(self, sh_cmd, title, env, d)
            logger.warn('Custom terminal was started.')
        else:
            logger.debug(1, 'No custom terminal (OE_TERMINAL_CUSTOMCMD) set')
            raise UnsupportedTerminal('OE_TERMINAL_CUSTOMCMD not set')


def prioritized():
    return Registry.prioritized()

def spawn_preferred(sh_cmd, title=None, env=None, d=None):
    """Spawn the first supported terminal, by priority"""
    for terminal in prioritized():
        try:
            spawn(terminal.name, sh_cmd, title, env, d)
            break
        except UnsupportedTerminal:
            continue
    else:
        raise NoSupportedTerminals()

def spawn(name, sh_cmd, title=None, env=None, d=None):
    """Spawn the specified terminal, by name"""
    logger.debug(1, 'Attempting to spawn terminal "%s"', name)
    try:
        terminal = Registry.registry[name]
    except KeyError:
        raise UnsupportedTerminal(name)

    pipe = terminal(sh_cmd, title, env, d)
    output = pipe.communicate()[0]
    if pipe.returncode != 0:
        raise ExecutionError(sh_cmd, pipe.returncode, output)

def check_konsole_version(konsole):
    import subprocess as sub
    try:
        p = sub.Popen(['sh', '-c', '%s --version' % konsole],stdout=sub.PIPE,stderr=sub.PIPE)
        out, err = p.communicate()
        ver_info = out.rstrip().split('\n')
    except OSError as exc:
        import errno
        if exc.errno == errno.ENOENT:
            return None
        else:
            raise
    vernum = None
    for ver in ver_info:
        if ver.startswith('Konsole'):
            vernum = ver.split(' ')[-1]
    return vernum

def distro_name():
    try:
        p = Popen(['lsb_release', '-i'])
        out, err = p.communicate()
        distro = out.split(':')[1].strip().lower()
    except:
        distro = "unknown"
    return distro
