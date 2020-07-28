# Copyright (C) 2020  Agilent Technologies, Inc.
# Author: Chris Laplante <chris.laplante@agilent.com>
#
# Released under the MIT license (see COPYING.MIT)

from bb.progress import ProgressHandler
from bb.build import create_progress_handler
import contextlib
import re
import os.path

# from https://stackoverflow.com/a/14693789/221061
ansi_escape = re.compile(r'\x1B\[[0-?]*[ -/]*[@-~]')


class LogColorizerProxyProgressHandler(ProgressHandler):
    """
    This is a proxy progress handler. It intercepts log output, stripping any
    ANSII color escape codes. Then the stripped output is fed to the task's
    original progress handler which will render the progress bar as usual.
    """
    def __init__(self, d, outfile=None, otherargs=None):
        self._task = d.getVar("BB_RUNTASK")
        self._color_log = None
        self._nocolor_log = None
        self._exit_stack = None
        self._original_ph = None

        self._suppress_color_output = not not d.getVar("LOG_COLORIZER_SUPPRESS_COLORIZED_OUTPUT")

        self._tempdir = d.getVar("T")
        logfmt = (d.getVar('BB_LOGFMT') or 'log.{task}.{pid}')
        self._logbasename = logfmt.format(task=self._task, pid=os.getpid())

        # Setup courtesy symlinks
        for suffix in [".nocolor", ".color"]:
            loglink = os.path.join(self._tempdir, 'log.{0}{1}'.format(self._task, suffix))
            logfn = os.path.join(self._tempdir, self._logbasename + suffix)
            if loglink:
                bb.utils.remove(loglink)

                try:
                    os.symlink(self._logbasename + suffix, loglink)
                except OSError:
                    pass

        super().__init__(d, outfile)

    def __enter__(self):
        with contextlib.ExitStack() as es:
            self._color_log = es.enter_context(open(os.path.join(self._tempdir, self._logbasename) + ".color", "w"))
            self._nocolor_log = es.enter_context(open(os.path.join(self._tempdir, self._logbasename) + ".nocolor", "w"))

            # Reconstitute the original progress handler. We will feed stripped output to it so
            # that the progress bar still shows up for the task.
            original_ph = self._data.getVarFlag(self._task, "originalprogress")
            if original_ph:
                # We don't want task output showing up on the screen twice, so we'll just have
                # the original progress handler write to /dev/null.
                # Note the progress handler itself is responsible for closing the devnull handler.
                devnull = open("/dev/null", "w")
                self._original_ph = es.enter_context(create_progress_handler(self._task, original_ph, devnull, self._data))

            self._exit_stack = es.pop_all()
        super().__enter__()

    def __exit__(self, *exc_info):
        if self._exit_stack:
            self._exit_stack.__exit__(*exc_info)
        super().__exit__(*exc_info)

    def write(self, string):
        # Filter out ANSI escape sequences using the regular expression
        filtered = ansi_escape.sub('', string)

        if self._color_log:
            self._color_log.write(string)

        if self._nocolor_log:
            self._nocolor_log.write(filtered)

        if self._original_ph:
            # Pass-through to the original progress handler so we get our progress bar
            self._original_ph.write(filtered)

        super().write(filtered if self._suppress_color_output else string)
