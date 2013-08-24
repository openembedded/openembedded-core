#
# Chris Lumens <clumens@redhat.com>
#
# Copyright 2007 Red Hat, Inc.
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

class FC6_UserData(BaseData):
    removedKeywords = BaseData.removedKeywords
    removedAttrs = BaseData.removedAttrs

    def __init__(self, *args, **kwargs):
        BaseData.__init__(self, *args, **kwargs)
        self.groups = kwargs.get("groups", [])
        self.homedir = kwargs.get("homedir", "")
        self.isCrypted = kwargs.get("isCrypted", False)
        self.name = kwargs.get("name", "")
        self.password = kwargs.get("password", "")
        self.shell = kwargs.get("shell", "")
        self.uid = kwargs.get("uid", None)

    def __eq__(self, y):
        return self.name == y.name

    def __str__(self):
        retval = BaseData.__str__(self)

        if self.uid != "":
            retval += "user"
            retval += self._getArgsAsStr() + "\n"

        return retval

    def _getArgsAsStr(self):
        retval = ""

        if len(self.groups) > 0:
            retval += " --groups=%s" % ",".join(self.groups)
        if self.homedir:
            retval += " --homedir=%s" % self.homedir
        if self.name:
            retval += " --name=%s" % self.name
        if self.password:
            retval += " --password=%s" % self.password
        if self.isCrypted:
            retval += " --iscrypted"
        if self.shell:
            retval += " --shell=%s" % self.shell
        if self.uid:
            retval += " --uid=%s" % self.uid

        return retval

class F8_UserData(FC6_UserData):
    removedKeywords = FC6_UserData.removedKeywords
    removedAttrs = FC6_UserData.removedAttrs

    def __init__(self, *args, **kwargs):
        FC6_UserData.__init__(self, *args, **kwargs)
        self.lock = kwargs.get("lock", False)

    def _getArgsAsStr(self):
        retval = FC6_UserData._getArgsAsStr(self)

        if self.lock:
            retval += " --lock"

        return retval

class F12_UserData(F8_UserData):
    removedKeywords = F8_UserData.removedKeywords
    removedAttrs = F8_UserData.removedAttrs

    def __init__(self, *args, **kwargs):
        F8_UserData.__init__(self, *args, **kwargs)
        self.gecos = kwargs.get("gecos", "")

    def _getArgsAsStr(self):
        retval = F8_UserData._getArgsAsStr(self)

        if self.gecos:
            retval += " --gecos=\"%s\"" % (self.gecos,)

        return retval

class FC6_User(KickstartCommand):
    removedKeywords = KickstartCommand.removedKeywords
    removedAttrs = KickstartCommand.removedAttrs

    def __init__(self, writePriority=0, *args, **kwargs):
        KickstartCommand.__init__(self, writePriority, *args, **kwargs)
        self.op = self._getParser()

        self.userList = kwargs.get("userList", [])

    def __str__(self):
        retval = ""
        for user in self.userList:
            retval += user.__str__()

        return retval

    def _getParser(self):
        def groups_cb (option, opt_str, value, parser):
            for d in value.split(','):
                parser.values.ensure_value(option.dest, []).append(d)

        op = KSOptionParser()
        op.add_option("--groups", dest="groups", action="callback",
                      callback=groups_cb, nargs=1, type="string")
        op.add_option("--homedir")
        op.add_option("--iscrypted", dest="isCrypted", action="store_true",
                      default=False)
        op.add_option("--name", required=1)
        op.add_option("--password")
        op.add_option("--shell")
        op.add_option("--uid", type="int")
        return op

    def parse(self, args):
        ud = self.handler.UserData()
        (opts, extra) = self.op.parse_args(args=args, lineno=self.lineno)
        self._setToObj(self.op, opts, ud)
        ud.lineno = self.lineno

        # Check for duplicates in the data list.
        if ud in self.dataList():
            warnings.warn(_("A user with the name %s has already been defined.") % ud.name)

        return ud

    def dataList(self):
        return self.userList

class F8_User(FC6_User):
    removedKeywords = FC6_User.removedKeywords
    removedAttrs = FC6_User.removedAttrs

    def _getParser(self):
        op = FC6_User._getParser(self)
        op.add_option("--lock", action="store_true", default=False)
        op.add_option("--plaintext", dest="isCrypted", action="store_false")
        return op

class F12_User(F8_User):
    removedKeywords = F8_User.removedKeywords
    removedAttrs = F8_User.removedAttrs

    def _getParser(self):
        op = F8_User._getParser(self)
        op.add_option("--gecos", type="string")
        return op
