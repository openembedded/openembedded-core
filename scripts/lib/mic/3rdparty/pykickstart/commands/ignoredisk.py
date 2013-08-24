#
# Chris Lumens <clumens@redhat.com>
#
# Copyright 2005, 2006, 2007, 2009 Red Hat, Inc.
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
from pykickstart.options import *

import gettext
_ = lambda x: gettext.ldgettext("pykickstart", x)

class FC3_IgnoreDisk(KickstartCommand):
    removedKeywords = KickstartCommand.removedKeywords
    removedAttrs = KickstartCommand.removedAttrs

    def __init__(self, writePriority=0, *args, **kwargs):
        KickstartCommand.__init__(self, writePriority, *args, **kwargs)
        self.op = self._getParser()

        self.ignoredisk = kwargs.get("ignoredisk", [])

    def __str__(self):
        retval = KickstartCommand.__str__(self)

        if len(self.ignoredisk) > 0:
            retval += "ignoredisk --drives=%s\n" % ",".join(self.ignoredisk)

        return retval

    def _getParser(self):
        def drive_cb (option, opt_str, value, parser):
            for d in value.split(','):
                parser.values.ensure_value(option.dest, []).append(d)

        op = KSOptionParser()
        op.add_option("--drives", dest="ignoredisk", action="callback",
                      callback=drive_cb, nargs=1, type="string", required=1)
        return op

    def parse(self, args):
        (opts, extra) = self.op.parse_args(args=args, lineno=self.lineno)
        self._setToSelf(self.op, opts)
        return self

class F8_IgnoreDisk(FC3_IgnoreDisk):
    removedKeywords = FC3_IgnoreDisk.removedKeywords
    removedAttrs = FC3_IgnoreDisk.removedAttrs

    def __init__(self, writePriority=0, *args, **kwargs):
        FC3_IgnoreDisk.__init__(self, writePriority, *args, **kwargs)

        self.onlyuse = kwargs.get("onlyuse", [])

    def __str__(self):
        retval = KickstartCommand.__str__(self)

        if len(self.ignoredisk) > 0:
            retval += "ignoredisk --drives=%s\n" % ",".join(self.ignoredisk)
        elif len(self.onlyuse) > 0:
            retval += "ignoredisk --only-use=%s\n" % ",".join(self.onlyuse)

        return retval

    def parse(self, args, errorCheck=True):
        retval = FC3_IgnoreDisk.parse(self, args)

        if errorCheck:
            if (len(self.ignoredisk) == 0 and len(self.onlyuse) == 0) or (len(self.ignoredisk) > 0 and (len(self.onlyuse) > 0)):
                raise KickstartValueError, formatErrorMsg(self.lineno, msg=_("One of --drives or --only-use must be specified for ignoredisk command."))

        return retval

    def _getParser(self):
        def drive_cb (option, opt_str, value, parser):
            for d in value.split(','):
                parser.values.ensure_value(option.dest, []).append(d)

        op = FC3_IgnoreDisk._getParser(self)
        op.add_option("--drives", dest="ignoredisk", action="callback",
                      callback=drive_cb, nargs=1, type="string")
        op.add_option("--only-use", dest="onlyuse", action="callback",
                      callback=drive_cb, nargs=1, type="string")
        return op

class RHEL6_IgnoreDisk(F8_IgnoreDisk):
    removedKeywords = F8_IgnoreDisk.removedKeywords
    removedAttrs = F8_IgnoreDisk.removedAttrs

    def __init__(self, writePriority=0, *args, **kwargs):
        F8_IgnoreDisk.__init__(self, writePriority, *args, **kwargs)

        self.interactive = kwargs.get("interactive", False)
        if self.interactive:
            self.ignoredisk = []

    def __str__(self):
        retval = F8_IgnoreDisk.__str__(self)

        if self.interactive:
            retval = "ignoredisk --interactive\n"

        return retval

    def parse(self, args):
        retval = F8_IgnoreDisk.parse(self, args, errorCheck=False)

        howmany = 0
        if len(self.ignoredisk) > 0:
            howmany += 1
        if len(self.onlyuse) > 0:
            howmany += 1
        if self.interactive:
            howmany += 1
        if howmany != 1:
            raise KickstartValueError, formatErrorMsg(self.lineno, msg=_("One of --drives , --only-use , or --interactive must be specified for ignoredisk command."))

        return retval

    def _getParser(self):
        op = F8_IgnoreDisk._getParser(self)
        op.add_option("--interactive", dest="interactive", action="store_true",
                      default=False)
        return op

F14_IgnoreDisk = RHEL6_IgnoreDisk
