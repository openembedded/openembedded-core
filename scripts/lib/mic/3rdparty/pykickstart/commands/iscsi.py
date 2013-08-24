#
# Chris Lumens <clumens@redhat.com>
# Peter Jones <pjones@redhat.com>
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

class FC6_IscsiData(BaseData):
    removedKeywords = BaseData.removedKeywords
    removedAttrs = BaseData.removedAttrs

    def __init__(self, *args, **kwargs):
        BaseData.__init__(self, *args, **kwargs)
        self.ipaddr = kwargs.get("ipaddr", "")
        self.port = kwargs.get("port", "3260")
        self.target = kwargs.get("target", "")
        self.user = kwargs.get("user", None)
        self.password = kwargs.get("password", None)

    def _getArgsAsStr(self):
        retval = ""

        if self.target != "":
            retval += " --target=%s" % self.target
        if self.ipaddr != "":
            retval += " --ipaddr=%s" % self.ipaddr
        if self.port != "3260":
            retval += " --port=%s" % self.port
        if self.user is not None:
            retval += " --user=%s" % self.user
        if self.password is not None:
            retval += " --password=%s" % self.password

        return retval

    def __str__(self):
        retval = BaseData.__str__(self)
        retval += "iscsi%s\n" % self._getArgsAsStr()
        return retval

class F10_IscsiData(FC6_IscsiData):
    removedKeywords = FC6_IscsiData.removedKeywords
    removedAttrs = FC6_IscsiData.removedAttrs

    def __init__(self, *args, **kwargs):
        FC6_IscsiData.__init__(self, *args, **kwargs)
        self.user_in = kwargs.get("user_in", None)
        self.password_in = kwargs.get("password_in", None)

    def _getArgsAsStr(self):
        retval = FC6_IscsiData._getArgsAsStr(self)

        if self.user_in is not None:
            retval += " --reverse-user=%s" % self.user_in
        if self.password_in is not None:
            retval += " --reverse-password=%s" % self.password_in

        return retval

class FC6_Iscsi(KickstartCommand):
    removedKeywords = KickstartCommand.removedKeywords
    removedAttrs = KickstartCommand.removedAttrs

    def __init__(self, writePriority=71, *args, **kwargs):
        KickstartCommand.__init__(self, writePriority, *args, **kwargs)
        self.op = self._getParser()

        self.iscsi = kwargs.get("iscsi", [])

    def __str__(self):
        retval = ""
        for iscsi in self.iscsi:
            retval += iscsi.__str__()

        return retval

    def _getParser(self):
        op = KSOptionParser()
        op.add_option("--target", dest="target", action="store", type="string")
        op.add_option("--ipaddr", dest="ipaddr", action="store", type="string",
                      required=1)
        op.add_option("--port", dest="port", action="store", type="string")
        op.add_option("--user", dest="user", action="store", type="string")
        op.add_option("--password", dest="password", action="store",
                      type="string")
        return op

    def parse(self, args):
        (opts, extra) = self.op.parse_args(args=args, lineno=self.lineno)

        if len(extra) != 0:
            mapping = {"command": "iscsi", "options": extra}
            raise KickstartValueError, formatErrorMsg(self.lineno, msg=_("Unexpected arguments to %(command)s command: %(options)s") % mapping)

        dd = self.handler.IscsiData()
        self._setToObj(self.op, opts, dd)
        dd.lineno = self.lineno
        return dd

    def dataList(self):
        return self.iscsi

class F10_Iscsi(FC6_Iscsi):
    removedKeywords = FC6_Iscsi.removedKeywords
    removedAttrs = FC6_Iscsi.removedAttrs

    def _getParser(self):
        op = FC6_Iscsi._getParser(self)
        op.add_option("--reverse-user", dest="user_in", action="store",
                      type="string")
        op.add_option("--reverse-password", dest="password_in", action="store",
                      type="string")
        return op
