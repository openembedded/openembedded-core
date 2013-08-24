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
import warnings
_ = lambda x: gettext.ldgettext("pykickstart", x)

class FC3_LogVolData(BaseData):
    removedKeywords = BaseData.removedKeywords
    removedAttrs = BaseData.removedAttrs

    def __init__(self, *args, **kwargs):
        BaseData.__init__(self, *args, **kwargs)
        self.fstype = kwargs.get("fstype", "")
        self.grow = kwargs.get("grow", False)
        self.maxSizeMB = kwargs.get("maxSizeMB", 0)
        self.name = kwargs.get("name", "")
        self.format = kwargs.get("format", True)
        self.percent = kwargs.get("percent", 0)
        self.recommended = kwargs.get("recommended", False)
        self.size = kwargs.get("size", None)
        self.preexist = kwargs.get("preexist", False)
        self.vgname = kwargs.get("vgname", "")
        self.mountpoint = kwargs.get("mountpoint", "")

    def __eq__(self, y):
        return self.vgname == y.vgname and self.name == y.name

    def _getArgsAsStr(self):
        retval = ""

        if self.fstype != "":
            retval += " --fstype=\"%s\"" % self.fstype
        if self.grow:
            retval += " --grow"
        if self.maxSizeMB > 0:
            retval += " --maxsize=%d" % self.maxSizeMB
        if not self.format:
            retval += " --noformat"
        if self.percent > 0:
            retval += " --percent=%d" % self.percent
        if self.recommended:
            retval += " --recommended"
        if self.size > 0:
            retval += " --size=%d" % self.size
        if self.preexist:
            retval += " --useexisting"

        return retval

    def __str__(self):
        retval = BaseData.__str__(self)
        retval += "logvol %s %s --name=%s --vgname=%s\n" % (self.mountpoint, self._getArgsAsStr(), self.name, self.vgname)
        return retval

class FC4_LogVolData(FC3_LogVolData):
    removedKeywords = FC3_LogVolData.removedKeywords
    removedAttrs = FC3_LogVolData.removedAttrs

    def __init__(self, *args, **kwargs):
        FC3_LogVolData.__init__(self, *args, **kwargs)
        self.bytesPerInode = kwargs.get("bytesPerInode", 4096)
        self.fsopts = kwargs.get("fsopts", "")

    def _getArgsAsStr(self):
        retval = FC3_LogVolData._getArgsAsStr(self)

        if hasattr(self, "bytesPerInode") and self.bytesPerInode != 0:
            retval += " --bytes-per-inode=%d" % self.bytesPerInode
        if self.fsopts != "":
            retval += " --fsoptions=\"%s\"" % self.fsopts

        return retval

class RHEL5_LogVolData(FC4_LogVolData):
    removedKeywords = FC4_LogVolData.removedKeywords
    removedAttrs = FC4_LogVolData.removedAttrs

    def __init__(self, *args, **kwargs):
        FC4_LogVolData.__init__(self, *args, **kwargs)
        self.encrypted = kwargs.get("encrypted", False)
        self.passphrase = kwargs.get("passphrase", "")

    def _getArgsAsStr(self):
        retval = FC4_LogVolData._getArgsAsStr(self)

        if self.encrypted:
            retval += " --encrypted"

            if self.passphrase != "":
                retval += " --passphrase=\"%s\"" % self.passphrase

        return retval

class F9_LogVolData(FC4_LogVolData):
    removedKeywords = FC4_LogVolData.removedKeywords + ["bytesPerInode"]
    removedAttrs = FC4_LogVolData.removedAttrs + ["bytesPerInode"]

    def __init__(self, *args, **kwargs):
        FC4_LogVolData.__init__(self, *args, **kwargs)
        self.deleteRemovedAttrs()

        self.fsopts = kwargs.get("fsopts", "")
        self.fsprofile = kwargs.get("fsprofile", "")
        self.encrypted = kwargs.get("encrypted", False)
        self.passphrase = kwargs.get("passphrase", "")

    def _getArgsAsStr(self):
        retval = FC4_LogVolData._getArgsAsStr(self)

        if self.fsprofile != "":
            retval += " --fsprofile=\"%s\"" % self.fsprofile
        if self.encrypted:
            retval += " --encrypted"

            if self.passphrase != "":
                retval += " --passphrase=\"%s\"" % self.passphrase

        return retval

class F12_LogVolData(F9_LogVolData):
    removedKeywords = F9_LogVolData.removedKeywords
    removedAttrs = F9_LogVolData.removedAttrs

    def __init__(self, *args, **kwargs):
        F9_LogVolData.__init__(self, *args, **kwargs)
        self.deleteRemovedAttrs()

        self.escrowcert = kwargs.get("escrowcert", "")
        self.backuppassphrase = kwargs.get("backuppassphrase", False)

    def _getArgsAsStr(self):
        retval = F9_LogVolData._getArgsAsStr(self)

        if self.encrypted and self.escrowcert != "":
            retval += " --escrowcert=\"%s\"" % self.escrowcert

            if self.backuppassphrase:
                retval += " --backuppassphrase"

        return retval

F14_LogVolData = F12_LogVolData

class F15_LogVolData(F14_LogVolData):
    removedKeywords = F14_LogVolData.removedKeywords
    removedAttrs = F14_LogVolData.removedAttrs

    def __init__(self, *args, **kwargs):
        F14_LogVolData.__init__(self, *args, **kwargs)
        self.label = kwargs.get("label", "")

    def _getArgsAsStr(self):
        retval = F14_LogVolData._getArgsAsStr(self)

        if self.label != "":
            retval += " --label=\"%s\"" % self.label

        return retval

class FC3_LogVol(KickstartCommand):
    removedKeywords = KickstartCommand.removedKeywords
    removedAttrs = KickstartCommand.removedAttrs

    def __init__(self, writePriority=133, *args, **kwargs):
        KickstartCommand.__init__(self, writePriority, *args, **kwargs)
        self.op = self._getParser()

        self.lvList = kwargs.get("lvList", [])

    def __str__(self):
        retval = ""

        for part in self.lvList:
            retval += part.__str__()

        return retval

    def _getParser(self):
        def lv_cb (option, opt_str, value, parser):
            parser.values.format = False
            parser.values.preexist = True

        op = KSOptionParser()
        op.add_option("--fstype", dest="fstype")
        op.add_option("--grow", dest="grow", action="store_true",
                      default=False)
        op.add_option("--maxsize", dest="maxSizeMB", action="store", type="int",
                      nargs=1)
        op.add_option("--name", dest="name", required=1)
        op.add_option("--noformat", action="callback", callback=lv_cb,
                      dest="format", default=True, nargs=0)
        op.add_option("--percent", dest="percent", action="store", type="int",
                      nargs=1)
        op.add_option("--recommended", dest="recommended", action="store_true",
                      default=False)
        op.add_option("--size", dest="size", action="store", type="int",
                      nargs=1)
        op.add_option("--useexisting", dest="preexist", action="store_true",
                      default=False)
        op.add_option("--vgname", dest="vgname", required=1)
        return op

    def parse(self, args):
        (opts, extra) = self.op.parse_args(args=args, lineno=self.lineno)

        if len(extra) == 0:
            raise KickstartValueError, formatErrorMsg(self.lineno, msg=_("Mount point required for %s") % "logvol")

        lvd = self.handler.LogVolData()
        self._setToObj(self.op, opts, lvd)
        lvd.lineno = self.lineno
        lvd.mountpoint=extra[0]

        # Check for duplicates in the data list.
        if lvd in self.dataList():
            warnings.warn(_("A logical volume with the name %s has already been defined in volume group %s.") % (lvd.device, lvd.vgname))

        return lvd

    def dataList(self):
        return self.lvList

class FC4_LogVol(FC3_LogVol):
    removedKeywords = FC3_LogVol.removedKeywords
    removedAttrs = FC3_LogVol.removedAttrs

    def _getParser(self):
        op = FC3_LogVol._getParser(self)
        op.add_option("--bytes-per-inode", dest="bytesPerInode", action="store",
                      type="int", nargs=1)
        op.add_option("--fsoptions", dest="fsopts")
        return op

class RHEL5_LogVol(FC4_LogVol):
    removedKeywords = FC4_LogVol.removedKeywords
    removedAttrs = FC4_LogVol.removedAttrs

    def _getParser(self):
        op = FC4_LogVol._getParser(self)
        op.add_option("--encrypted", action="store_true", default=False)
        op.add_option("--passphrase")
        return op

class F9_LogVol(FC4_LogVol):
    removedKeywords = FC4_LogVol.removedKeywords
    removedAttrs = FC4_LogVol.removedAttrs

    def _getParser(self):
        op = FC4_LogVol._getParser(self)
        op.add_option("--bytes-per-inode", deprecated=1)
        op.add_option("--fsprofile", dest="fsprofile", action="store",
                      type="string", nargs=1)
        op.add_option("--encrypted", action="store_true", default=False)
        op.add_option("--passphrase")
        return op

class F12_LogVol(F9_LogVol):
    removedKeywords = F9_LogVol.removedKeywords
    removedAttrs = F9_LogVol.removedAttrs

    def _getParser(self):
        op = F9_LogVol._getParser(self)
        op.add_option("--escrowcert")
        op.add_option("--backuppassphrase", action="store_true", default=False)
        return op

class F14_LogVol(F12_LogVol):
    removedKeywords = F12_LogVol.removedKeywords
    removedAttrs = F12_LogVol.removedAttrs

    def _getParser(self):
        op = F12_LogVol._getParser(self)
        op.remove_option("--bytes-per-inode")
        return op

class F15_LogVol(F14_LogVol):
    removedKeywords = F14_LogVol.removedKeywords
    removedAttrs = F14_LogVol.removedAttrs

    def _getParser(self):
        op = F14_LogVol._getParser(self)
        op.add_option("--label")
        return op
