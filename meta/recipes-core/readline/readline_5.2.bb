DESCRIPTION = "The GNU Readline library provides a set of functions for use by applications that allow users to edit \
command lines as they are typed in. Both Emacs and vi editing modes are available. The Readline library includes  \
additional functions to maintain a list of previously-entered command lines, to recall and perhaps reedit those   \
lines, and perform csh-like history expansion on previous commands."
SECTION = "libs"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=03b36fdd84f74b8d8189a202b980b67f"

DEPENDS += "ncurses"

PR = "r6"

SRC_URI = "${GNU_MIRROR}/readline/${BPN}-${PV}.tar.gz \
           file://configure-fix.patch \
           file://acinclude.m4"

S = "${WORKDIR}/${BPN}-${PV}"

inherit autotools

LEAD_SONAME = "libreadline.so"

do_configure_prepend () {
	install -m 0644 ${WORKDIR}/acinclude.m4 ${S}/
}

do_install_append () {
	# Make install doesn't properly install these
	oe_libinstall -so -C shlib libhistory ${D}${libdir}
	oe_libinstall -so -C shlib libreadline ${D}${libdir}
}

BBCLASSEXTEND = "native nativesdk"
