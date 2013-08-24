#
# Chris Lumens <clumens@redhat.com>
#
# Copyright 2005, 2006, 2007, 2008, 2011 Red Hat, Inc.
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

class FC3_RaidData(BaseData):
    removedKeywords = BaseData.removedKeywords
    removedAttrs = BaseData.removedAttrs

    def __init__(self, *args, **kwargs):
        BaseData.__init__(self, *args, **kwargs)
        self.device = kwargs.get("device", None)
        self.fstype = kwargs.get("fstype", "")
        self.level = kwargs.get("level", "")
        self.format = kwargs.get("format", True)
        self.spares = kwargs.get("spares", 0)
        self.preexist = kwargs.get("preexist", False)
        self.mountpoint = kwargs.get("mountpoint", "")
        self.members = kwargs.get("members", [])

    def __eq__(self, y):
        return self.device == y.device

    def _getArgsAsStr(self):
        retval = ""

        if self.device != "":
            retval += " --device=%s" % self.device
        if self.fstype != "":
            retval += " --fstype=\"%s\"" % self.fstype
        if self.level != "":
            retval += " --level=%s" % self.level
        if not self.format:
            retval += " --noformat"
        if self.spares != 0:
            retval += " --spares=%d" % self.spares
        if self.preexist:
            retval += " --useexisting"

        return retval

    def __str__(self):
        retval = BaseData.__str__(self)
        retval += "raid %s%s %s\n" % (self.mountpoint, self._getArgsAsStr(),
                                      " ".join(self.members))
        return retval

class FC4_RaidData(FC3_RaidData):
    removedKeywords = FC3_RaidData.removedKeywords
    removedAttrs = FC3_RaidData.removedAttrs

    def __init__(self, *args, **kwargs):
        FC3_RaidData.__init__(self, *args, **kwargs)
        self.fsopts = kwargs.get("fsopts", "")

    def _getArgsAsStr(self):
        retval = FC3_RaidData._getArgsAsStr(self)

        if self.fsopts != "":
            retval += " --fsoptions=\"%s\"" % self.fsopts

        return retval

class FC5_RaidData(FC4_RaidData):
    removedKeywords = FC4_RaidData.removedKeywords
    removedAttrs = FC4_RaidData.removedAttrs

    def __init__(self, *args, **kwargs):
        FC4_RaidData.__init__(self, *args, **kwargs)
        self.bytesPerInode = kwargs.get("bytesPerInode", 4096)

    def _getArgsAsStr(self):
        retval = FC4_RaidData._getArgsAsStr(self)

        if hasattr(self, "bytesPerInode") and self.bytesPerInode != 0:
            retval += " --bytes-per-inode=%d" % self.bytesPerInode

        return retval

class RHEL5_RaidData(FC5_RaidData):
    removedKeywords = FC5_RaidData.removedKeywords
    removedAttrs = FC5_RaidData.removedAttrs

    def __init__(self, *args, **kwargs):
        FC5_RaidData.__init__(self, *args, **kwargs)
        self.encrypted = kwargs.get("encrypted", False)
        self.passphrase = kwargs.get("passphrase", "")

    def _getArgsAsStr(self):
        retval = FC5_RaidData._getArgsAsStr(self)

        if self.encrypted:
            retval += " --encrypted"

            if self.passphrase != "":
                retval += " --passphrase=\"%s\"" % self.passphrase

        return retval

F7_RaidData = FC5_RaidData

class F9_RaidData(FC5_RaidData):
    removedKeywords = FC5_RaidData.removedKeywords + ["bytesPerInode"]
    removedAttrs = FC5_RaidData.removedAttrs + ["bytesPerInode"]

    def __init__(self, *args, **kwargs):
        FC5_RaidData.__init__(self, *args, **kwargs)
        self.deleteRemovedAttrs()

        self.fsprofile = kwargs.get("fsprofile", "")
        self.encrypted = kwargs.get("encrypted", False)
        self.passphrase = kwargs.get("passphrase", "")

    def _getArgsAsStr(self):
        retval = FC5_RaidData._getArgsAsStr(self)

        if self.fsprofile != "":
            retval += " --fsprofile=\"%s\"" % self.fsprofile
        if self.encrypted:
            retval += " --encrypted"

            if self.passphrase != "":
                retval += " --passphrase=\"%s\"" % self.passphrase

        return retval

class F12_RaidData(F9_RaidData):
    removedKeywords = F9_RaidData.removedKeywords
    removedAttrs = F9_RaidData.removedAttrs

    def __init__(self, *args, **kwargs):
        F9_RaidData.__init__(self, *args, **kwargs)
        self.deleteRemovedAttrs()

        self.escrowcert = kwargs.get("escrowcert", "")
        self.backuppassphrase = kwargs.get("backuppassphrase", False)

    def _getArgsAsStr(self):
        retval = F9_RaidData._getArgsAsStr(self)

        if self.encrypted and self.escrowcert != "":
            retval += " --escrowcert=\"%s\"" % self.escrowcert

            if self.backuppassphrase:
                retval += " --backuppassphrase"
        return retval

F13_RaidData = F12_RaidData

F14_RaidData = F13_RaidData

class F15_RaidData(F14_RaidData):
    removedKeywords = F14_RaidData.removedKeywords
    removedAttrs = F14_RaidData.removedAttrs

    def __init__(self, *args, **kwargs):
        F14_RaidData.__init__(self, *args, **kwargs)
        self.deleteRemovedAttrs()

        self.label = kwargs.get("label", "")

    def _getArgsAsStr(self):
        retval = F14_RaidData._getArgsAsStr(self)

        if self.label != "":
            retval += " --label=%s" % self.label

        return retval

class FC3_Raid(KickstartCommand):
    removedKeywords = KickstartCommand.removedKeywords
    removedAttrs = KickstartCommand.removedAttrs

    def __init__(self, writePriority=131, *args, **kwargs):
        KickstartCommand.__init__(self, writePriority, *args, **kwargs)
        self.op = self._getParser()

        # A dict of all the RAID levels we support.  This means that if we
        # support more levels in the future, subclasses don't have to
        # duplicate too much.
        self.levelMap = { "RAID0": "RAID0", "0": "RAID0",
                          "RAID1": "RAID1", "1": "RAID1",
                          "RAID5": "RAID5", "5": "RAID5",
                          "RAID6": "RAID6", "6": "RAID6" }

        self.raidList = kwargs.get("raidList", [])

    def __str__(self):
        retval = ""

        for raid in self.raidList:
            retval += raid.__str__()

        return retval

    def _getParser(self):
        def raid_cb (option, opt_str, value, parser):
            parser.values.format = False
            parser.values.preexist = True

        def device_cb (option, opt_str, value, parser):
            if value[0:2] == "md":
                parser.values.ensure_value(option.dest, value[2:])
            else:
                parser.values.ensure_value(option.dest, value)

        def level_cb (option, opt_str, value, parser):
            if self.levelMap.has_key(value):
                parser.values.ensure_value(option.dest, self.levelMap[value])

        op = KSOptionParser()
        op.add_option("--device", action="callback", callback=device_cb,
                      dest="device", type="string", nargs=1, required=1)
        op.add_option("--fstype", dest="fstype")
        op.add_option("--level", dest="level", action="callback",
                      callback=level_cb, type="string", nargs=1)
        op.add_option("--noformat", action="callback", callback=raid_cb,
                      dest="format", default=True, nargs=0)
        op.add_option("--spares", dest="spares", action="store", type="int",
                      nargs=1, default=0)
        op.add_option("--useexisting", dest="preexist", action="store_true",
                      default=False)
        return op

    def parse(self, args):
        (opts, extra) = self.op.parse_args(args=args, lineno=self.lineno)

        if len(extra) == 0:
            raise KickstartValueError, formatErrorMsg(self.lineno, msg=_("Mount point required for %s") % "raid")
        if len(extra) == 1:
            raise KickstartValueError, formatErrorMsg(self.lineno, msg=_("Partitions required for %s") % "raid")

        rd = self.handler.RaidData()
        self._setToObj(self.op, opts, rd)
        rd.lineno = self.lineno

        # --device can't just take an int in the callback above, because it
        # could be specificed as "mdX", which causes optparse to error when
        # it runs int().
        rd.device = int(rd.device)
        rd.mountpoint = extra[0]
        rd.members = extra[1:]

        # Check for duplicates in the data list.
        if rd in self.dataList():
            warnings.warn(_("A RAID device with the name %s has already been defined.") % rd.device)

        return rd

    def dataList(self):
        return self.raidList

class FC4_Raid(FC3_Raid):
    removedKeywords = FC3_Raid.removedKeywords
    removedAttrs = FC3_Raid.removedAttrs

    def _getParser(self):
        op = FC3_Raid._getParser(self)
        op.add_option("--fsoptions", dest="fsopts")
        return op

class FC5_Raid(FC4_Raid):
    removedKeywords = FC4_Raid.removedKeywords
    removedAttrs = FC4_Raid.removedAttrs

    def _getParser(self):
        op = FC4_Raid._getParser(self)
        op.add_option("--bytes-per-inode", dest="bytesPerInode", action="store",
                      type="int", nargs=1)
        return op

class RHEL5_Raid(FC5_Raid):
    removedKeywords = FC5_Raid.removedKeywords
    removedAttrs = FC5_Raid.removedAttrs

    def __init__(self, writePriority=131, *args, **kwargs):
        FC5_Raid.__init__(self, writePriority, *args, **kwargs)

        self.levelMap.update({"RAID10": "RAID10", "10": "RAID10"})

    def _getParser(self):
        op = FC5_Raid._getParser(self)
        op.add_option("--encrypted", action="store_true", default=False)
        op.add_option("--passphrase")
        return op

class F7_Raid(FC5_Raid):
    removedKeywords = FC5_Raid.removedKeywords
    removedAttrs = FC5_Raid.removedAttrs

    def __init__(self, writePriority=131, *args, **kwargs):
        FC5_Raid.__init__(self, writePriority, *args, **kwargs)

        self.levelMap.update({"RAID10": "RAID10", "10": "RAID10"})

class F9_Raid(F7_Raid):
    removedKeywords = F7_Raid.removedKeywords
    removedAttrs = F7_Raid.removedAttrs

    def _getParser(self):
        op = F7_Raid._getParser(self)
        op.add_option("--bytes-per-inode", deprecated=1)
        op.add_option("--fsprofile")
        op.add_option("--encrypted", action="store_true", default=False)
        op.add_option("--passphrase")
        return op

class F12_Raid(F9_Raid):
    removedKeywords = F9_Raid.removedKeywords
    removedAttrs = F9_Raid.removedAttrs

    def _getParser(self):
        op = F9_Raid._getParser(self)
        op.add_option("--escrowcert")
        op.add_option("--backuppassphrase", action="store_true", default=False)
        return op

class F13_Raid(F12_Raid):
    removedKeywords = F12_Raid.removedKeywords
    removedAttrs = F12_Raid.removedAttrs

    def __init__(self, writePriority=131, *args, **kwargs):
        F12_Raid.__init__(self, writePriority, *args, **kwargs)

        self.levelMap.update({"RAID4": "RAID4", "4": "RAID4"})

class F14_Raid(F13_Raid):
    removedKeywords = F13_Raid.removedKeywords
    removedAttrs = F13_Raid.removedAttrs

    def _getParser(self):
        op = F13_Raid._getParser(self)
        op.remove_option("--bytes-per-inode")
        return op

class F15_Raid(F14_Raid):
    removedKeywords = F14_Raid.removedKeywords
    removedAttrs = F14_Raid.removedAttrs

    def _getParser(self):
        op = F14_Raid._getParser(self)
        op.add_option("--label")
        return op
