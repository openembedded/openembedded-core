# Copyright (c) 2013 Intel Corporation
#
# Released under the MIT license (see COPYING.MIT)

# DESCRIPTION
# This module is mainly used by scripts/oe-selftest and modules under meta/oeqa/selftest
# It provides a class and methods for running commands on the host in a convienent way for tests.



import os
import sys
import signal
import subprocess
import threading
import logging

class Command(object):
    def __init__(self, command, bg=False, timeout=None, data=None, **options):

        self.defaultopts = {
            "stdout": subprocess.PIPE,
            "stderr": subprocess.STDOUT,
            "stdin": None,
            "shell": False,
            "bufsize": -1,
        }

        self.cmd = command
        self.bg = bg
        self.timeout = timeout
        self.data = data

        self.options = dict(self.defaultopts)
        if isinstance(self.cmd, basestring):
            self.options["shell"] = True
        if self.data:
            self.options['stdin'] = subprocess.PIPE
        self.options.update(options)

        self.status = None
        self.output = None
        self.error = None
        self.thread = None

        self.log = logging.getLogger("utils.commands")

    def run(self):
        self.process = subprocess.Popen(self.cmd, **self.options)

        def commThread():
            self.output, self.error = self.process.communicate(self.data)

        self.thread = threading.Thread(target=commThread)
        self.thread.start()

        self.log.debug("Running command '%s'" % self.cmd)

        if not self.bg:
            self.thread.join(self.timeout)
            self.stop()

    def stop(self):
        if self.thread.isAlive():
            self.process.terminate()
            # let's give it more time to terminate gracefully before killing it
            self.thread.join(5)
            if self.thread.isAlive():
                self.process.kill()
                self.thread.join()

        self.output = self.output.rstrip()
        self.status = self.process.poll()

        self.log.debug("Command '%s' returned %d as exit code." % (self.cmd, self.status))
        # logging the complete output is insane
        # bitbake -e output is really big
        # and makes the log file useless
        if self.status:
            lout = "\n".join(self.output.splitlines()[-20:])
            self.log.debug("Last 20 lines:\n%s" % lout)


class Result(object):
    pass

def runCmd(command, ignore_status=False, timeout=None, **options):

    result = Result()

    cmd = Command(command, timeout=timeout, **options)
    cmd.run()

    result.command = command
    result.status = cmd.status
    result.output = cmd.output
    result.pid = cmd.process.pid

    if result.status and not ignore_status:
        raise AssertionError("Command '%s' returned non-zero exit status %d:\n%s" % (command, result.status, result.output))

    return result


def bitbake(command, ignore_status=False, timeout=None, **options):
    if isinstance(command, basestring):
        cmd = "bitbake " + command
    else:
        cmd = [ "bitbake" ] + command

    return runCmd(cmd, ignore_status, timeout, **options)


def get_bb_env(target=None):
    if target:
        return runCmd("bitbake -e %s" % target).output
    else:
        return runCmd("bitbake -e").output

def get_bb_var(var, target=None):
    val = None
    bbenv = get_bb_env(target)
    for line in bbenv.splitlines():
        if line.startswith(var + "="):
            val = line.split('=')[1]
            val = val.replace('\"','')
            break
    return val

def get_test_layer():
    layers = get_bb_var("BBLAYERS").split()
    testlayer = None
    for l in layers:
        if "/meta-selftest" in l and os.path.isdir(l):
            testlayer = l
            break
    return testlayer
