#
# Chris Lumens <clumens@redhat.com>
# Peter Jones <pjones@redhat.com>
#
# Copyright 2006, 2007 Red Hat, Inc.
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

class FC6_DmRaidData(BaseData):
    removedKeywords = BaseData.removedKeywords
    removedAttrs = BaseData.removedAttrs

    def __init__(self, *args, **kwargs):
        BaseData.__init__(self, *args, **kwargs)

        self.name = kwargs.get("name", "")
        self.devices = kwargs.get("devices", [])
        self.dmset = kwargs.get("dmset", None)

    def __eq__(self, y):
        return self.name == y.name and self.devices == y.devices

    def __str__(self):
        retval = BaseData.__str__(self)
        retval += "dmraid --name=%s" % self.name

        for dev in self.devices:
            retval += " --dev=\"%s\"" % dev

        return retval + "\n"

class FC6_DmRaid(KickstartCommand):
    removedKeywords = KickstartCommand.removedKeywords
    removedAttrs = KickstartCommand.removedAttrs

    def __init__(self, writePriority=60, *args, **kwargs):
        KickstartCommand.__init__(self, writePriority, *args, **kwargs)
        self.op = self._getParser()

        self.dmraids = kwargs.get("dmraids", [])

    def __str__(self):
        retval = ""
        for dm in self.dmraids:
            retval += dm.__str__()

        return retval

    def _getParser(self):
        op = KSOptionParser()
        op.add_option("--name", dest="name", action="store", type="string",
                      required=1)
        op.add_option("--dev", dest="devices", action="append", type="string",
                      required=1)
        return op

    def parse(self, args):
        dm = FC6_DmRaidData()
        (opts, extra) = self.op.parse_args(args=args, lineno=self.lineno)
        dm.name = dm.name.split('/')[-1]
        self._setToObj(self.op, opts, dm)
        dm.lineno = self.lineno

        # Check for duplicates in the data list.
        if dm in self.dataList():
            warnings.warn(_("A DM RAID device with the name %s and devices %s has already been defined.") % (dm.name, dm.devices))

        return dm

    def dataList(self):
        return self.dmraids
