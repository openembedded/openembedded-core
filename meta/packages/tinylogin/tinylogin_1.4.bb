SECTION = "base"
DESCRIPTION = "TinyLogin is a suite of tiny UNIX \
utilities for handling logins, user authentication, \
changing passwords, and otherwise maintaining users \
and groups on an embedded system."
HOMEPAGE = "http://tinylogin.busybox.net/"
LICENSE = "GPL"
PR = "r3"

SRC_URI = "http://tinylogin.busybox.net/downloads/tinylogin-${PV}.tar.bz2 \
	file://cvs-20040608.patch;patch=1;pnum=1 \
	file://add-system.patch;patch=1;pnum=1 \
	file://adduser-empty_pwd.patch;patch=1"

EXTRA_OEMAKE = ""

do_compile () {
	oe_runmake 'CC=${CC}' 'CROSS=${HOST_PREFIX}'
}

do_install () {
	install -d ${D}${base_bindir}
	install -m 4755 tinylogin ${D}${base_bindir}/tinylogin
	for i in `cat tinylogin.links`; do
		mkdir -p ${D}/`dirname $i`
		ln -sf /bin/tinylogin ${D}$i
	done
}
