#
# Chris Lumens <clumens@redhat.com>
#
# Copyright 2005, 2006, 2007 Red Hat, Inc.
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
from pykickstart.constants import *
from pykickstart.errors import *
from pykickstart.options import *

class FC3_Reboot(KickstartCommand):
    removedKeywords = KickstartCommand.removedKeywords
    removedAttrs = KickstartCommand.removedAttrs

    def __init__(self, writePriority=0, *args, **kwargs):
        KickstartCommand.__init__(self, writePriority, *args, **kwargs)
        self.action = kwargs.get("action", None)

    def __str__(self):
        retval = KickstartCommand.__str__(self)

        if self.action == KS_REBOOT:
            retval += "# Reboot after installation\nreboot\n"
        elif self.action == KS_SHUTDOWN:
            retval += "# Shutdown after installation\nshutdown\n"

        return retval

    def parse(self, args):
        if self.currentCmd == "reboot":
            self.action = KS_REBOOT
        else:
            self.action = KS_SHUTDOWN

        return self

class FC6_Reboot(FC3_Reboot):
    removedKeywords = FC3_Reboot.removedKeywords
    removedAttrs = FC3_Reboot.removedAttrs

    def __init__(self, writePriority=0, *args, **kwargs):
        FC3_Reboot.__init__(self, writePriority, *args, **kwargs)
        self.op = self._getParser()

        self.eject = kwargs.get("eject", False)

    def __str__(self):
        retval = FC3_Reboot.__str__(self).rstrip()

        if self.eject:
            retval += " --eject"

        return retval + "\n"

    def _getParser(self):
        op = KSOptionParser()
        op.add_option("--eject", dest="eject", action="store_true",
                      default=False)
        return op

    def parse(self, args):
        FC3_Reboot.parse(self, args)
        (opts, extra) = self.op.parse_args(args=args, lineno=self.lineno)
        self._setToSelf(self.op, opts)
        return self
