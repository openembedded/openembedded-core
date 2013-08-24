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
from pykickstart.errors import *
from pykickstart.options import *

import gettext
_ = lambda x: gettext.ldgettext("pykickstart", x)

class FC3_Upgrade(KickstartCommand):
    removedKeywords = KickstartCommand.removedKeywords
    removedAttrs = KickstartCommand.removedAttrs

    def __init__(self, writePriority=0, *args, **kwargs):
        KickstartCommand.__init__(self, writePriority, *args, **kwargs)
        self.upgrade = kwargs.get("upgrade", None)
        self.op = self._getParser()

    def __str__(self):
        retval = KickstartCommand.__str__(self)

        if self.upgrade is None:
            return retval

        if self.upgrade:
            retval += "# Upgrade existing installation\nupgrade\n"
        else:
            retval += "# Install OS instead of upgrade\ninstall\n"

        return retval

    def _getParser(self):
        op = KSOptionParser()
        return op

    def parse(self, args):
        (opts, extra) = self.op.parse_args(args=args, lineno=self.lineno)

        if len(extra) > 0:
            raise KickstartValueError, formatErrorMsg(self.lineno, msg=_("Kickstart command %s does not take any arguments") % "upgrade")

        if self.currentCmd == "upgrade":
            self.upgrade = True
        else:
            self.upgrade = False

        return self

class F11_Upgrade(FC3_Upgrade):
    removedKeywords = FC3_Upgrade.removedKeywords
    removedAttrs = FC3_Upgrade.removedAttrs

    def __init__(self, writePriority=0, *args, **kwargs):
        FC3_Upgrade.__init__(self, writePriority, *args, **kwargs)

        self.op = self._getParser()
        self.root_device = kwargs.get("root_device", None)

    def __str__(self):
        if self.upgrade and (self.root_device is not None):
            retval = KickstartCommand.__str__(self)
            retval += "# Upgrade existing installation\nupgrade --root-device=%s\n" % self.root_device
        else:
            retval = FC3_Upgrade.__str__(self)

        return retval

    def _getParser(self):
        op = KSOptionParser()
        op.add_option("--root-device", dest="root_device")
        return op

    def parse(self, args):
        (opts, extra) = self.op.parse_args(args=args, lineno=self.lineno)

        if len(extra) > 0:
            raise KickstartValueError, formatErrorMsg(self.lineno, msg=_("Kickstart command %s does not take any arguments") % "upgrade")

        if (opts.root_device is not None) and (opts.root_device == ""):
            raise KickstartValueError, formatErrorMsg(self.lineno, msg=_("Kickstart command %s does not accept empty parameter %s") % ("upgrade", "--root-device"))
        else:
            self.root_device = opts.root_device

        if self.currentCmd == "upgrade":
            self.upgrade = True
        else:
            self.upgrade = False

        return self
