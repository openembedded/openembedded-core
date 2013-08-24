#
# Chris Lumens <clumens@redhat.com>
#
# Copyright 2005, 2006, 2007, 2008 Red Hat, Inc.
#
# This copyrighted material is made available to anyone wishing to use, modify,
# copy, or redistribute it subject to the terms and conditions of the GNU
# General Public License v.2.  This program is distributed in the hope that it
# will be useful, but WITHOUT ANY WARRANTY expressed or implied, including the
# implied warranties of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
# See the GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License along with
# this program; if not, write to the Free Software Foundation, Inc., 51
# Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.  Any Red Hat
# trademarks that are incorporated in the source code or documentation are not
# subject to the GNU General Public License and may only be used or replicated
# with the express permission of Red Hat, Inc. 
#
from pykickstart.base import *
from pykickstart.errors import *
from pykickstart.options import *

import gettext
_ = lambda x: gettext.ldgettext("pykickstart", x)

class FC3_Monitor(KickstartCommand):
    removedKeywords = KickstartCommand.removedKeywords
    removedAttrs = KickstartCommand.removedAttrs

    def __init__(self, writePriority=0, *args, **kwargs):
        KickstartCommand.__init__(self, writePriority, *args, **kwargs)
        self.op = self._getParser()

        self.hsync = kwargs.get("hsync", "")
        self.monitor = kwargs.get("monitor", "")
        self.vsync = kwargs.get("vsync", "")

    def __str__(self):
        retval = KickstartCommand.__str__(self)
        retval += "monitor"

        if self.hsync != "":
            retval += " --hsync=%s" % self.hsync
        if self.monitor != "":
            retval += " --monitor=\"%s\"" % self.monitor
        if self.vsync != "":
            retval += " --vsync=%s" % self.vsync

        if retval != "monitor":
            return retval + "\n"
        else:
            return ""

    def _getParser(self):
        op = KSOptionParser()
        op.add_option("--hsync")
        op.add_option("--monitor")
        op.add_option("--vsync")
        return op

    def parse(self, args):
        (opts, extra) = self.op.parse_args(args=args, lineno=self.lineno)

        if extra:
            mapping = {"cmd": "monitor", "options": extra}
            raise KickstartValueError, formatErrorMsg(self.lineno, msg=_("Unexpected arguments to %(cmd)s command: %(options)s") % mapping)

        self._setToSelf(self.op, opts)
        return self

class FC6_Monitor(FC3_Monitor):
    removedKeywords = FC3_Monitor.removedKeywords
    removedAttrs = FC3_Monitor.removedAttrs

    def __init__(self, writePriority=0, *args, **kwargs):
        FC3_Monitor.__init__(self, writePriority, *args, **kwargs)
        self.probe = kwargs.get("probe", True)

    def __str__(self):
        retval = KickstartCommand.__str__(self)
        retval += "monitor"

        if self.hsync != "":
            retval += " --hsync=%s" % self.hsync
        if self.monitor != "":
            retval += " --monitor=\"%s\"" % self.monitor
        if not self.probe:
            retval += " --noprobe"
        if self.vsync != "":
            retval += " --vsync=%s" % self.vsync

        if retval != "monitor":
            return retval + "\n"
        else:
            return ""

    def _getParser(self):
        op = FC3_Monitor._getParser(self)
        op.add_option("--noprobe", dest="probe", action="store_false",
                      default=True)
        return op

class F10_Monitor(DeprecatedCommand):
    def __init__(self):
        DeprecatedCommand.__init__(self)
