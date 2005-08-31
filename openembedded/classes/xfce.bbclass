# xfce.oeclass
# Copyright (C) 2004, Advanced Micro Devices, Inc.  All Rights Reserved
# Released under the MIT license (see packages/COPYING)

# Global class to make it easier to maintain XFCE packages

HOMEPAGE = "http://www.xfce.org"
LICENSE = "LGPL-2"

SRC_URI = "http://www.us.xfce.org/archive/xfce-${PV}/src/${PN}-${PV}.tar.gz"

inherit autotools

EXTRA_OECONF += "--with-pluginsdir=${libdir}/xfce4/panel-plugins/"

# FIXME:  Put icons in their own package too?

FILES_${PN} += "${datadir}/icons/* ${datadir}/applications/* ${libdir}/xfce4/modules/*.so*"
FILES_${PN}-doc += "${datadir}/xfce4/doc"
