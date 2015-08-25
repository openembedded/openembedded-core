import os
import sys
import errno
import datetime
import itertools
from commands import runCmd

def get_host_dumper(d):
    return HostDumper(d)


class BaseDumper(object):

    def __init__(self, d):
        self.parent_dir = d.getVar("TESTIMAGE_DUMP_DIR", True)

    def create_dir(self, dir_suffix):
        dump_subdir = ("%s_%s" % (
                datetime.datetime.now().strftime('%Y%m%d%H%M'),
                dir_suffix))
        dump_dir = os.path.join(self.parent_dir, dump_subdir)
        try:
            os.makedirs(dump_dir)
        except OSError as err:
            if err.errno != errno.EEXIST:
                raise err
        self.dump_dir = dump_dir

    def write_dump(self, command, output):
        if isinstance(self, HostDumper):
            prefix = "host"
        elif isinstance(self, TargetDumper):
            prefix = "target"
        else:
            prefix = "unknown"
        for i in itertools.count():
            filename = "%s_%02d_%s" % (prefix, i, command)
            fullname = os.path.join(self.dump_dir, filename)
            if not os.path.exists(fullname):
                break
        with open(fullname, 'w') as dump_file:
            dump_file.write(output)


class HostDumper(BaseDumper):

    def __init__(self, d):
        super(HostDumper, self).__init__(d)
        self.host_cmds = d.getVar("testimage_dump_host", True)

    def dump_host(self, dump_dir=""):
        if dump_dir:
            self.dump_dir = dump_dir
        for cmd in self.host_cmds.split('\n'):
            cmd = cmd.lstrip()
            if not cmd or cmd[0] == '#':
                continue
            result = runCmd(cmd, ignore_status=True)
            self.write_dump(cmd.split()[0], result.output)


class TargetDumper(BaseDumper):

    def __init__(self, d, qemurunner):
        super(TargetDumper, self).__init__(d)
        self.target_cmds = d.getVar("testimage_dump_target", True)
        self.runner = qemurunner

    def dump_target(self, dump_dir=""):
        if dump_dir:
            self.dump_dir = dump_dir
        for cmd in self.target_cmds.split('\n'):
            cmd = cmd.lstrip()
            if not cmd or cmd[0] == '#':
                continue
            (status, output) = self.runner.run_serial(cmd)
            self.write_dump(cmd.split()[0], output)
