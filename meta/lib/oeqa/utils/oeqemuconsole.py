import socket
import time
import re
from telnetlib import Telnet

class oeQemuConsole(Telnet):

    """
    Override Telnet class to use unix domain sockets,
    Telnet uses AF_INET for socket, we don't want that.
    Also, provide a read_all variant with timeout, that
    returns whatever output there is.
    """

    def __init__(self, stream, logfile):

        Telnet.__init__(self, host=None)
        self.stream = stream
        self.logfile = logfile
        self.eof = 0
        self.sock = socket.socket(socket.AF_UNIX, socket.SOCK_STREAM)
        self.sock.connect(stream)

    def log(self, msg):
        if self.logfile:
            with open(self.logfile, "a") as f:
                f.write("%s\n" % msg)


    def read_all_timeout(self, match, timeout=200):
        """Read until EOF or until timeout or until match.
        """
        ret = False
        self.process_rawq()
        endtime = time.time() + timeout
        while not self.eof and time.time() < endtime:
            self.fill_rawq()
            self.process_rawq()
            if re.search(match, self.cookedq):
                ret = True
                break
        buf = self.cookedq
        self.cookedq = ''
        self.log(buf)
        return (ret, buf)
