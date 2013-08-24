#
# Chris Lumens <clumens@redhat.com>
#
# Copyright 2009 Red Hat, Inc.
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

import gettext
import warnings
_ = lambda x: gettext.ldgettext("pykickstart", x)

class F12_GroupData(BaseData):
    removedKeywords = BaseData.removedKeywords
    removedAttrs = BaseData.removedAttrs

    def __init__(self, *args, **kwargs):
        BaseData.__init__(self, *args, **kwargs)
        self.name = kwargs.get("name", "")
        self.gid = kwargs.get("gid", None)

    def __eq__(self, y):
        return self.name == y.name

    def __str__(self):
        retval = BaseData.__str__(self)
        retval += "group"

        if self.name:
            retval += " --name=%s" % self.name
        if self.gid:
            retval += " --gid=%s" % self.gid

        return retval + "\n"

class F12_Group(KickstartCommand):
    removedKeywords = KickstartCommand.removedKeywords
    removedAttrs = KickstartCommand.removedAttrs

    def __init__(self, writePriority=0, *args, **kwargs):
        KickstartCommand.__init__(self, writePriority, *args, **kwargs)
        self.op = self._getParser()

        self.groupList = kwargs.get("groupList", [])

    def __str__(self):
        retval = ""
        for user in self.groupList:
            retval += user.__str__()

        return retval

    def _getParser(self):
        op = KSOptionParser()
        op.add_option("--name", required=1)
        op.add_option("--gid", type="int")
        return op

    def parse(self, args):
        gd = self.handler.GroupData()
        (opts, extra) = self.op.parse_args(args=args, lineno=self.lineno)
        self._setToObj(self.op, opts, gd)
        gd.lineno = self.lineno

        # Check for duplicates in the data list.
        if gd in self.dataList():
            warnings.warn(_("A group with the name %s has already been defined.") % gd.name)

        return gd

    def dataList(self):
        return self.groupList
