# Copyright (C) 2012 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "kmod is a set of tools to handle common tasks with Linux kernel modules like \
               insert, remove, list, check properties, resolve dependencies and aliases."
HOMEPAGE = "http://packages.profusion.mobi/kmod/"
LICENSE = "GPL-2.0+ & LGPL-2.1+"
LICENSE_libkmod = "LGPL-2.1+"
SECTION = "base"
PV = "9"
INC_PR = "r0"

DEPENDS += "pkgconfig-native"

LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe \
                    file://libkmod/COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343 \
                   "
inherit autotools gtk-doc

SRC_URI = "git://git.kernel.org/pub/scm/utils/kernel/kmod/kmod.git \
           file://depmod-search.conf \
           file://0001-man-disable-man-page-generation-because-we-don-t-hav.patch \
          "

SRCREV = "62081c0f68905b22f375156d4532fd37fa5c8d33"

S = "${WORKDIR}/git"

EXTRA_AUTORECONF += "--install --symlink"
EXTRA_OECONF +="--enable-debug --enable-logging --enable-tools"

do_configure_prepend () {
        gtkdocize --docdir ${S}/libkmod/docs || touch ${S}/libkmod/docs/gtk-doc.make
}
