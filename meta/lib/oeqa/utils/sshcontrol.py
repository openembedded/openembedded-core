import subprocess
import time
import os
import shlex

class SSHControl(object):

    def __init__(self, host=None, timeout=200, logfile=None):
        self.host = host
        self.timeout = timeout
        self._out = ''
        self._ret = 126
        self.logfile = logfile

    def log(self, msg):
        if self.logfile:
            with open(self.logfile, "a") as f:
                f.write("%s\n" % msg)

    def _internal_run(self, cmd):
        # ssh hangs without os.setsid
        proc = subprocess.Popen(shlex.split(cmd), shell=False, stdout=subprocess.PIPE, stderr=subprocess.STDOUT, preexec_fn=os.setsid)
        return proc

    def run(self, cmd, timeout=None):
        """Run cmd and get it's return code and output.
        Let it run for timeout seconds and then terminate/kill it,
        if time is 0 will let cmd run until it finishes.
        Time can be passed to here or can be set per class instance."""



        actualcmd = "ssh -o UserKnownHostsFile=/dev/null -o StrictHostKeyChecking=no -l root %s '%s'" % (self.host, cmd)
        if self.host:
            sshconn = self._internal_run(actualcmd)
        else:
            raise Exception("Remote IP hasn't been set: '%s'" % actualcmd)

        if timeout == 0:
            self.log("[SSH run without timeout]$ %s" % actualcmd)
            self.log("  # %s" % cmd)
            self._out = sshconn.communicate()[0]
            self._ret = sshconn.poll()
        else:
            if timeout is None:
                endtime = time.time() + self.timeout
            else:
                endtime = time.time() + timeout
            while sshconn.poll() is None and time.time() < endtime:
                time.sleep(1)
            self.log("[SSH run with timeout]$ %s" % actualcmd)
            self.log("  # %s" % cmd)
            # process hasn't returned yet
            if sshconn.poll() is None:
                self._ret = 255
                sshconn.terminate()
                sshconn.kill()
                self._out = sshconn.stdout.read()
                sshconn.stdout.close()
                self.log("[!!! process killed]")
            else:
                self._out = sshconn.stdout.read()
                self._ret = sshconn.poll()
        # remove first line from output which is always smth like (unless return code is 255 - which is a ssh error):
        # Warning: Permanently added '192.168.7.2' (RSA) to the list of known hosts.
        if self._ret != 255:
            self._out = '\n'.join(self._out.splitlines()[1:])
        self.log("%s" % self._out)
        self.log("[SSH command returned]: %s" % self._ret)
        return (self._ret, self._out)

    def _internal_scp(self, cmd):
        self.log("[SCP]$ %s" % cmd)
        scpconn = self._internal_run(cmd)
        try:
            self._out = scpconn.communicate()[0]
            self._ret = scpconn.poll()
            if self._ret != 0:
                self.log("%s" % self._out)
                raise Exception("Error copying file")
        except Exception as e:
            print("Execution failed: %s :" % cmd)
            print e
        self.log("%s" % self._out)
        return (self._ret, self._out)

    def copy_to(self, localpath, remotepath):
        actualcmd = "scp -o UserKnownHostsFile=/dev/null  -o StrictHostKeyChecking=no %s root@%s:%s" % (localpath, self.host, remotepath)
        return self._internal_scp(actualcmd)


    def copy_from(self, remotepath, localpath):
        actualcmd = "scp -o UserKnownHostsFile=/dev/null  -o StrictHostKeyChecking=no root@%s:%s %s" % (self.host, remotepath, localpath)
        return self._internal_scp(actualcmd)

    def get_status(self):
        return self._ret

    def get_output(self):
        return self._out
