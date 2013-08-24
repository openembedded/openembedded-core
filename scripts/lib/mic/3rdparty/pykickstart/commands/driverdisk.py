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
from pykickstart.options import *

import gettext
_ = lambda x: gettext.ldgettext("pykickstart", x)

class FC3_DriverDiskData(BaseData):
    removedKeywords = BaseData.removedKeywords
    removedAttrs = BaseData.removedAttrs

    def __init__(self, writePriority=0, *args, **kwargs):
        BaseData.__init__(self, *args, **kwargs)

        self.partition = kwargs.get("partition", "")
        self.source = kwargs.get("source", "")
        self.type = kwargs.get("type", "")

    def _getArgsAsStr(self):
        retval = ""

        if self.partition:
            retval += "%s" % self.partition

            if hasattr(self, "type") and self.type:
                retval += " --type=%s" % self.type
        elif self.source:
            retval += "--source=%s" % self.source
        return retval

    def __str__(self):
        retval = BaseData.__str__(self)
        retval += "driverdisk %s\n" % self._getArgsAsStr()
        return retval

class FC4_DriverDiskData(FC3_DriverDiskData):
    removedKeywords = FC3_DriverDiskData.removedKeywords
    removedAttrs = FC3_DriverDiskData.removedAttrs

    def __init__(self, writePriority=0, *args, **kwargs):
        FC3_DriverDiskData.__init__(self, *args, **kwargs)
        self.deleteRemovedAttrs()

        self.biospart = kwargs.get("biospart", "")

    def _getArgsAsStr(self):
        retval = ""

        if self.partition:
            retval += "%s" % self.partition

            if hasattr(self, "type") and self.type:
                retval += " --type=%s" % self.type
        elif self.source:
            retval += "--source=%s" % self.source
        elif self.biospart:
            retval += "--biospart=%s" % self.biospart

        return retval

class F12_DriverDiskData(FC4_DriverDiskData):
    removedKeywords = FC4_DriverDiskData.removedKeywords + ["type"]
    removedAttrs = FC4_DriverDiskData.removedAttrs + ["type"]

    def __init__(self, *args, **kwargs):
        FC4_DriverDiskData.__init__(self, *args, **kwargs)
        self.deleteRemovedAttrs()

F14_DriverDiskData = F12_DriverDiskData

class FC3_DriverDisk(KickstartCommand):
    removedKeywords = KickstartCommand.removedKeywords
    removedAttrs = KickstartCommand.removedAttrs

    def __init__(self, writePriority=0, *args, **kwargs):
        KickstartCommand.__init__(self, writePriority, *args, **kwargs)
        self.op = self._getParser()

        self.driverdiskList = kwargs.get("driverdiskList", [])

    def __str__(self):
        retval = ""
        for dd in self.driverdiskList:
            retval += dd.__str__()

        return retval

    def _getParser(self):
        op = KSOptionParser()
        op.add_option("--source")
        op.add_option("--type")
        return op

    def parse(self, args):
        (opts, extra) = self.op.parse_args(args=args, lineno=self.lineno)

        if len(extra) > 1:
            raise KickstartValueError, formatErrorMsg(self.lineno, msg=_("Only one partition may be specified for driverdisk command."))

        if len(extra) == 1 and opts.source:
            raise KickstartValueError, formatErrorMsg(self.lineno, msg=_("Only one of --source and partition may be specified for driverdisk command."))

        if not extra and not opts.source:
            raise KickstartValueError, formatErrorMsg(self.lineno, msg=_("One of --source or partition must be specified for driverdisk command."))

        ddd = self.handler.DriverDiskData()
        self._setToObj(self.op, opts, ddd)
        ddd.lineno = self.lineno
        if len(extra) == 1:
            ddd.partition = extra[0]

        return ddd

    def dataList(self):
        return self.driverdiskList

class FC4_DriverDisk(FC3_DriverDisk):
    removedKeywords = FC3_DriverDisk.removedKeywords
    removedAttrs = FC3_DriverDisk.removedKeywords

    def _getParser(self):
        op = FC3_DriverDisk._getParser(self)
        op.add_option("--biospart")
        return op

    def parse(self, args):
        (opts, extra) = self.op.parse_args(args=args, lineno=self.lineno)

        if len(extra) > 1:
            raise KickstartValueError, formatErrorMsg(self.lineno, msg=_("Only one partition may be specified for driverdisk command."))

        if len(extra) == 1 and opts.source:
            raise KickstartValueError, formatErrorMsg(self.lineno, msg=_("Only one of --source and partition may be specified for driverdisk command."))
        elif len(extra) == 1 and opts.biospart:
            raise KickstartValueError, formatErrorMsg(self.lineno, msg=_("Only one of --biospart and partition may be specified for driverdisk command."))
        elif opts.source and opts.biospart:
            raise KickstartValueError, formatErrorMsg(self.lineno, msg=_("Only one of --biospart and --source may be specified for driverdisk command."))

        if not extra and not opts.source and not opts.biospart:
            raise KickstartValueError, formatErrorMsg(self.lineno, msg=_("One of --source, --biospart, or partition must be specified for driverdisk command."))

        ddd = self.handler.DriverDiskData()
        self._setToObj(self.op, opts, ddd)
        ddd.lineno = self.lineno
        if len(extra) == 1:
            ddd.partition = extra[0]

        return ddd

class F12_DriverDisk(FC4_DriverDisk):
    removedKeywords = FC4_DriverDisk.removedKeywords
    removedAttrs = FC4_DriverDisk.removedKeywords

    def _getParser(self):
        op = FC4_DriverDisk._getParser(self)
        op.add_option("--type", deprecated=1)
        return op

class F14_DriverDisk(F12_DriverDisk):
    removedKeywords = F12_DriverDisk.removedKeywords
    removedAttrs = F12_DriverDisk.removedKeywords

    def _getParser(self):
        op = F12_DriverDisk._getParser(self)
        op.remove_option("--type")
        return op
