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
from pykickstart.options import *

import gettext
import warnings
_ = lambda x: gettext.ldgettext("pykickstart", x)

class FC3_ZFCPData(BaseData):
    removedKeywords = BaseData.removedKeywords
    removedAttrs = BaseData.removedAttrs

    def __init__(self, *args, **kwargs):
        BaseData.__init__(self, *args, **kwargs)
        self.devnum = kwargs.get("devnum", "")
        self.wwpn = kwargs.get("wwpn", "")
        self.fcplun = kwargs.get("fcplun", "")
        self.scsiid = kwargs.get("scsiid", "")
        self.scsilun = kwargs.get("scsilun", "")

    def __eq__(self, y):
        return self.devnum == y.devnum and self.wwpn == y.wwpn and \
               self.fcplun == y.fcplun and self.scsiid == y.scsiid and \
               self.scsilun == y.scsilun

    def __str__(self):
        retval = BaseData.__str__(self)
        retval += "zfcp"

        if self.devnum != "":
            retval += " --devnum=%s" % self.devnum
        if self.wwpn != "":
            retval += " --wwpn=%s" % self.wwpn
        if self.fcplun != "":
            retval += " --fcplun=%s" % self.fcplun
        if hasattr(self, "scsiid") and self.scsiid != "":
            retval += " --scsiid=%s" % self.scsiid
        if hasattr(self, "scsilun") and self.scsilun != "":
            retval += " --scsilun=%s" % self.scsilun

        return retval + "\n"

class F12_ZFCPData(FC3_ZFCPData):
    removedKeywords = FC3_ZFCPData.removedKeywords + ["scsiid", "scsilun"]
    removedAttrs = FC3_ZFCPData.removedAttrs + ["scsiid", "scsilun"]

    def __init__(self, *args, **kwargs):
        FC3_ZFCPData.__init__(self, *args, **kwargs)
        self.deleteRemovedAttrs()

F14_ZFCPData = F12_ZFCPData

class FC3_ZFCP(KickstartCommand):
    removedKeywords = KickstartCommand.removedKeywords
    removedAttrs = KickstartCommand.removedAttrs

    def __init__(self, writePriority=71, *args, **kwargs):
        KickstartCommand.__init__(self, writePriority, *args, **kwargs)
        self.op = self._getParser()

        self.zfcp = kwargs.get("zfcp", [])

    def __str__(self):
        retval = ""
        for zfcp in self.zfcp:
            retval += zfcp.__str__()

        return retval

    def _getParser(self):
        op = KSOptionParser()
        op.add_option("--devnum", dest="devnum", required=1)
        op.add_option("--fcplun", dest="fcplun", required=1)
        op.add_option("--scsiid", dest="scsiid", required=1)
        op.add_option("--scsilun", dest="scsilun", required=1)
        op.add_option("--wwpn", dest="wwpn", required=1)
        return op

    def parse(self, args):
        zd = self.handler.ZFCPData()
        (opts, extra) = self.op.parse_args(args=args, lineno=self.lineno)
        self._setToObj(self.op, opts, zd)
        zd.lineno = self.lineno

        # Check for duplicates in the data list.
        if zd in self.dataList():
            warnings.warn(_("A zfcp with this information has already been defined."))

        return zd

    def dataList(self):
        return self.zfcp

class F12_ZFCP(FC3_ZFCP):
    removedKeywords = FC3_ZFCP.removedKeywords
    removedAttrs = FC3_ZFCP.removedAttrs + ["scsiid", "scsilun"]

    def __init__(self, *args, **kwargs):
        FC3_ZFCP.__init__(self, *args, **kwargs)
        self.deleteRemovedAttrs()

    def _getParser(self):
        op = FC3_ZFCP._getParser(self)
        op.add_option("--scsiid", deprecated=1)
        op.add_option("--scsilun", deprecated=1)
        return op

class F14_ZFCP(F12_ZFCP):
    removedKeywords = F12_ZFCP.removedKeywords
    removedAttrs = F12_ZFCP.removedAttrs

    def _getParser(self):
        op = F12_ZFCP._getParser(self)
        op.remove_option("--scsiid")
        op.remove_option("--scsilun")
        return op
