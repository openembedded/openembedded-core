import logging
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

    def __init__(self, sh_cmd, title=None, env=None):
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
    def __init__(self, sh_cmd, title=None, env=None):
        Terminal.__init__(self, sh_cmd, title, env)
        if not os.environ.get('DISPLAY'):
            raise UnsupportedTerminal(self.name)

class Gnome(XTerminal):
    command = 'gnome-terminal --disable-factory -t "{title}" -x {command}'
    priority = 2

class Xfce(XTerminal):
    command = 'Terminal -T "{title}" -e "{command}"'
    priority = 2

    def __init__(self, command, title=None, env=None):
        # Upstream binary name is Terminal but Debian/Ubuntu use
        # xfce4-terminal to avoid possible(?) conflicts
        distro = distro_name()
        if distro == 'ubuntu' or distro == 'debian':
            cmd = 'xfce4-terminal -T "{title}" -e "{command}"'
        else:
            cmd = command
        XTerminal.__init__(self, cmd, title, env)

class Konsole(XTerminal):
    command = 'konsole -T "{title}" -e {command}'
    priority = 2

    def __init__(self, sh_cmd, title=None, env=None):
        # Check version
        vernum = check_konsole_version("konsole")
        if vernum:
            if vernum.split('.')[0] == "2":
                logger.debug(1, 'Konsole from KDE 4.x will not work as devshell, skipping')
                raise UnsupportedTerminal(self.name)
        XTerminal.__init__(self, sh_cmd, title, env)

class XTerm(XTerminal):
    command = 'xterm -T "{title}" -e {command}'
    priority = 1

class Rxvt(XTerminal):
    command = 'rxvt -T "{title}" -e {command}'
    priority = 1

class Screen(Terminal):
    command = 'screen -D -m -t "{title}" -S devshell {command}'

    def __init__(self, sh_cmd, title=None, env=None):
        s_id = "devshell_%i" % os.getpid()
        self.command = "screen -D -m -t \"{title}\" -S %s {command}" % s_id
        Terminal.__init__(self, sh_cmd, title, env)
        logger.warn('Screen started. Please connect in another terminal with '
                    '"screen -r devshell %s"' % s_id)


def prioritized():
    return Registry.prioritized()

def spawn_preferred(sh_cmd, title=None, env=None):
    """Spawn the first supported terminal, by priority"""
    for terminal in prioritized():
        try:
            spawn(terminal.name, sh_cmd, title, env)
            break
        except UnsupportedTerminal:
            continue
    else:
        raise NoSupportedTerminals()

def spawn(name, sh_cmd, title=None, env=None):
    """Spawn the specified terminal, by name"""
    logger.debug(1, 'Attempting to spawn terminal "%s"', name)
    try:
        terminal = Registry.registry[name]
    except KeyError:
        raise UnsupportedTerminal(name)

    pipe = terminal(sh_cmd, title, env)
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
