# ex:ts=4:sw=4:sts=4:et
# -*- tab-width: 4; c-basic-offset: 4; indent-tabs-mode: nil -*-
#
# BitBake 'Build' implementation
#
# Core code for function execution and task handling in the
# BitBake build tools.
#
# Copyright (C) 2003, 2004  Chris Larson
#
# Based on Gentoo's portage.py.
#
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License version 2 as
# published by the Free Software Foundation.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License along
# with this program; if not, write to the Free Software Foundation, Inc.,
# 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
#
#Based on functions from the base bb module, Copyright 2003 Holger Schurig

from bb import data, event, mkdirhier, utils
import os
import sys
import logging
import bb
import bb.utils
import bb.process

logger = logging.getLogger("BitBake.Build")

NULL = open('/dev/null', 'r')


# When we execute a python function we'd like certain things
# in all namespaces, hence we add them to __builtins__
# If we do not do this and use the exec globals, they will
# not be available to subfunctions.
__builtins__['bb'] = bb
__builtins__['os'] = os

class FuncFailed(Exception):
    def __init__(self, name, logfile = None):
        self.logfile = logfile
        if logfile is None:
            self.name = None
            self.message = name
        else:
            self.name = name
            self.message = "Function '%s' failed" % name

    def __str__(self):
        if self.logfile and os.path.exists(self.logfile):
            msg = ("%s (see %s for further information)" %
                   (self.message, self.logfile))
        else:
            msg = self.message
        return msg

class TaskBase(event.Event):
    """Base class for task events"""

    def __init__(self, t, d ):
        self._task = t
        self._package = bb.data.getVar("PF", d, 1)
        event.Event.__init__(self)
        self._message = "package %s: task %s: %s" % (bb.data.getVar("PF", d, 1), t, bb.event.getName(self)[4:])

    def getTask(self):
        return self._task

    def setTask(self, task):
        self._task = task

    task = property(getTask, setTask, None, "task property")

class TaskStarted(TaskBase):
    """Task execution started"""

class TaskSucceeded(TaskBase):
    """Task execution completed"""

class TaskFailed(TaskBase):
    """Task execution failed"""

    def __init__(self, task, logfile, metadata):
        self.logfile = logfile
        super(TaskFailed, self).__init__(task, metadata)

class TaskInvalid(TaskBase):

    def __init__(self, task, metadata):
        super(TaskInvalid, self).__init__(task, metadata)
        self._message = "No such task '%s'" % task


class tee(file):
    def write(self, string):
        logger.plain(string)
        file.write(self, string)

    def __repr__(self):
        return "<open[tee] file '{0}'>".format(self.name)


def exec_func(func, d, dirs = None, logfile = NULL):
    """Execute an BB 'function'"""

    body = data.getVar(func, d)
    if not body:
        if body is None:
            logger.warn("Function %s doesn't exist", func)
        return

    flags = data.getVarFlags(func, d)
    cleandirs = flags.get('cleandirs')
    if cleandirs:
        for cdir in data.expand(cleandirs, d).split():
            bb.utils.remove(cdir, True)

    if dirs is None:
        dirs = flags.get('dirs')
        if dirs:
            dirs = data.expand(dirs, d).split()

    if dirs:
        for adir in dirs:
            bb.utils.mkdirhier(adir)
        adir = dirs[-1]
    else:
        adir = data.getVar('B', d, 1)
        if not os.path.exists(adir):
            adir = None

    ispython = flags.get('python')
    if flags.get('fakeroot') and not flags.get('task'):
        bb.fatal("Function %s specifies fakeroot but isn't a task?!" % func)

    tempdir = data.getVar('T', d, 1)
    runfile = os.path.join(tempdir, 'run.{0}.{1}'.format(func, os.getpid()))

    locks = []
    lockfiles = flags.get('lockfiles')
    if lockfiles:
        for lock in data.expand(lockfiles, d).split():
            locks.append(bb.utils.lockfile(lock))

    try:
        if ispython:
            exec_func_python(func, d, runfile, logfile, cwd=adir)
        else:
            exec_func_shell(func, d, runfile, logfile, cwd=adir)

    finally:
        # Unlock any lockfiles
        for lock in locks:
            bb.utils.unlockfile(lock)

_functionfmt = """
def {function}(d):
{body}

{function}(d)
"""
#logformatter = bb.msg.BBLogFormatter("%(levelname)s: %(message)s")
def exec_func_python(func, d, runfile, logfile, cwd=None):
    """Execute a python BB 'function'"""

    bbfile = d.getVar('FILE', True)
    try:
        olddir = os.getcwd()
    except OSError:
        olddir = None
    code = _functionfmt.format(function=func, body=d.getVar(func, True))
    bb.utils.mkdirhier(os.path.dirname(runfile))
    with open(runfile, 'w') as script:
        script.write(code)

    if cwd:
        os.chdir(cwd)

    #handler = logging.StreamHandler(logfile)
    #handler.setFormatter(logformatter)
    #bblogger.addHandler(handler)

    try:
        comp = utils.better_compile(code, func, bbfile)
        utils.better_exec(comp, {"d": d}, code, bbfile)
    except:
        if sys.exc_info()[0] in (bb.parse.SkipPackage, bb.build.FuncFailed):
            raise

        raise FuncFailed(func, None)
    finally:
        #bblogger.removeHandler(handler)
        if olddir:
            os.chdir(olddir)

def exec_func_shell(function, d, runfile, logfile, cwd=None):
    """Execute a shell function from the metadata

    Note on directory behavior.  The 'dirs' varflag should contain a list
    of the directories you need created prior to execution.  The last
    item in the list is where we will chdir/cd to.
    """

    # Don't let the emitted shell script override PWD
    d.delVarFlag('PWD', 'export')

    with open(runfile, 'w') as script:
        script.write('#!/bin/sh -e\n')
        if logger.getEffectiveLevel() <= logging.DEBUG:
            script.write("set -x\n")
        data.emit_func(function, script, d)

        script.write("%s\n" % function)
        os.fchmod(script.fileno(), 0775)

    env = {
        'PATH': d.getVar('PATH', True),
        'LC_ALL': 'C',
    }

    cmd = runfile

    if logger.getEffectiveLevel() <= logging.DEBUG:
        logfile = LogTee(logger, logfile)

    try:
        bb.process.run(cmd, env=env, cwd=cwd, shell=False, stdin=NULL,
                       log=logfile)
    except bb.process.CmdError:
        raise FuncFailed(function, logfile.name)

def _task_data(fn, task, d):
    localdata = data.createCopy(d)
    localdata.setVar('BB_FILENAME', fn)
    localdata.setVar('BB_CURRENTTASK', task[3:])
    localdata.setVar('OVERRIDES', 'task-%s:%s' %
                     (task[3:], d.getVar('OVERRIDES', False)))
    localdata.finalize()
    data.expandKeys(localdata)
    return localdata

def _exec_task(fn, task, d, quieterr):
    """Execute a BB 'task'

    Execution of a task involves a bit more setup than executing a function,
    running it with its own local metadata, and with some useful variables set.
    """
    if not data.getVarFlag(task, 'task', d):
        event.fire(TaskInvalid(task, d), d)
        logger.error("No such task: %s" % task)
        return 1

    logger.debug(1, "Executing task %s", task)

    localdata = _task_data(fn, task, d)
    tempdir = localdata.getVar('T', True)
    if not tempdir:
        bb.fatal("T variable not set, unable to build")

    bb.utils.mkdirhier(tempdir)
    loglink = os.path.join(tempdir, 'log.{0}'.format(task))
    logfn = os.path.join(tempdir, 'log.{0}.{1}'.format(task, os.getpid()))
    if loglink:
        bb.utils.remove(loglink)

        try:
           os.symlink(logfn, loglink)
        except OSError:
           pass

    prefuncs = localdata.getVarFlag(task, 'prefuncs', expand=True)
    postfuncs = localdata.getVarFlag(task, 'postfuncs', expand=True)

    # Handle logfiles
    si = file('/dev/null', 'r')
    try:
        logfile = file(logfn, 'w')
    except OSError:
        logger.exception("Opening log file '%s'", logfn)
        pass

    # Dup the existing fds so we dont lose them
    osi = [os.dup(sys.stdin.fileno()), sys.stdin.fileno()]
    oso = [os.dup(sys.stdout.fileno()), sys.stdout.fileno()]
    ose = [os.dup(sys.stderr.fileno()), sys.stderr.fileno()]

    # Replace those fds with our own
    os.dup2(si.fileno(), osi[1])
    os.dup2(logfile.fileno(), oso[1])
    os.dup2(logfile.fileno(), ose[1])

    # Since we've remapped stdout and stderr, its safe for log messages to be printed there now
    # exec_func can nest so we have to save state
    origstdout = bb.event.useStdout
    bb.event.useStdout = True

    event.fire(TaskStarted(task, localdata), localdata)
    try:
        for func in (prefuncs or '').split():
            exec_func(func, localdata, logfile=logfile)
        exec_func(task, localdata, logfile=logfile)
        for func in (postfuncs or '').split():
            exec_func(func, localdata, logfile=logfile)
    except FuncFailed as exc:
        if not quieterr:
            logger.error(str(exc))
            event.fire(TaskFailed(exc.name, exc.logfile, localdata), localdata)
        return 1
    finally:
        sys.stdout.flush()
        sys.stderr.flush()

        bb.event.useStdout = origstdout

        # Restore the backup fds
        os.dup2(osi[0], osi[1])
        os.dup2(oso[0], oso[1])
        os.dup2(ose[0], ose[1])

        # Close the backup fds
        os.close(osi[0])
        os.close(oso[0])
        os.close(ose[0])
        si.close()

        logfile.close()
        if os.path.exists(logfn) and os.path.getsize(logfn) == 0:
            logger.debug(2, "Zero size logfn %s, removing", logfn)
            bb.utils.remove(logfn)
            bb.utils.remove(loglink)
    event.fire(TaskSucceeded(task, localdata), localdata)

    if not d.getVarFlag(task, 'nostamp') and not d.getVarFlag(task, 'selfstamp'):
        make_stamp(task, d)

    return 0

def exec_task(fn, task, d):
    try: 
        quieterr = False
        if d.getVarFlag(task, "quieterrors") is not None:
            quieterr = True

        return _exec_task(fn, task, d, quieterr)
    except Exception:
        from traceback import format_exc
        if not quieterr:
            logger.error("Build of %s failed" % (task))
            logger.error(format_exc())
            failedevent = TaskFailed("Task Failed", None, task, d)
            event.fire(failedevent, d)
        return 1

def extract_stamp(d, fn):
    """
    Extracts stamp format which is either a data dictionary (fn unset)
    or a dataCache entry (fn set).
    """
    if fn:
        return d.stamp[fn]
    return data.getVar('STAMP', d, 1)

def stamp_internal(task, d, file_name):
    """
    Internal stamp helper function
    Removes any stamp for the given task
    Makes sure the stamp directory exists
    Returns the stamp path+filename
    """
    stamp = extract_stamp(d, file_name)
    if not stamp:
        return
    stamp = "%s.%s" % (stamp, task)
    bb.utils.mkdirhier(os.path.dirname(stamp))
    # Remove the file and recreate to force timestamp
    # change on broken NFS filesystems
    if os.access(stamp, os.F_OK):
        os.remove(stamp)
    return stamp

def make_stamp(task, d, file_name = None):
    """
    Creates/updates a stamp for a given task
    (d can be a data dict or dataCache)
    """
    stamp = stamp_internal(task, d, file_name)
    if stamp:
        f = open(stamp, "w")
        f.close()

def del_stamp(task, d, file_name = None):
    """
    Removes a stamp for a given task
    (d can be a data dict or dataCache)
    """
    stamp_internal(task, d, file_name)

def add_tasks(tasklist, d):
    task_deps = data.getVar('_task_deps', d)
    if not task_deps:
        task_deps = {}
    if not 'tasks' in task_deps:
        task_deps['tasks'] = []
    if not 'parents' in task_deps:
        task_deps['parents'] = {}

    for task in tasklist:
        task = data.expand(task, d)
        data.setVarFlag(task, 'task', 1, d)

        if not task in task_deps['tasks']:
            task_deps['tasks'].append(task)

        flags = data.getVarFlags(task, d)
        def getTask(name):
            if not name in task_deps:
                task_deps[name] = {}
            if name in flags:
                deptask = data.expand(flags[name], d)
                task_deps[name][task] = deptask
        getTask('depends')
        getTask('deptask')
        getTask('rdeptask')
        getTask('recrdeptask')
        getTask('nostamp')
        getTask('fakeroot')
        getTask('noexec')
        task_deps['parents'][task] = []
        for dep in flags['deps']:
            dep = data.expand(dep, d)
            task_deps['parents'][task].append(dep)

    # don't assume holding a reference
    data.setVar('_task_deps', task_deps, d)

def remove_task(task, kill, d):
    """Remove an BB 'task'.

       If kill is 1, also remove tasks that depend on this task."""

    data.delVarFlag(task, 'task', d)
