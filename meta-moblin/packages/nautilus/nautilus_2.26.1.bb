# nautilus OE build file
# Copyright (C) 2005, Advanced Micro Devices, Inc.  All Rights Reserved
# Released under the MIT license (see packages/COPYING)

inherit gnome

SRC_URI += "file://idl-sysroot.patch;patch=1 \
            file://no-try-run-strftime.diff;patch=1 \
"

export SYSROOT = "${STAGING_DIR_HOST}"

LICENSE = "GPL"

DEPENDS = "gail gtk+ pango gnome-desktop libxml2 dbus-glib libunique libexif librsvg"
#DEPENDS += "gvfs"
#RDEPENDS = "gvfs gvfsd-ftp gvfsd-sftp gvfsd-trash"

EXTRA_OECONF = " --disable-gtk-doc  --disable-update-mimedb --disable-tracker --disable-beagle"

PACKAGES += " libnautilus"

FILES_${PN} += "${datadir}/icons  /usr/libexec/ "
FILES_libnautilus = "/usr/lib/*.so*"
FILES_${PN}-dbg += "/usr/libexec/.debug"

do_configure_prepend() {
	sed -i -e /docs/d Makefile.am
}
