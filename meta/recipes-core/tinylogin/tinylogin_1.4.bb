SECTION = "base"
SUMMARY = "Tiny versions of many common login, authentication and related utilities."
DESCRIPTION = "TinyLogin is a suite of tiny UNIX \
utilities for handling logins, user authentication, \
changing passwords, and otherwise maintaining users \
and groups on an embedded system."
HOMEPAGE = "http://tinylogin.busybox.net/"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM="file://LICENSE;md5=f1060fa3a366f098b5b1d8c2077ba269"
PR = "r7"

SRC_URI = "http://www.angstrom-distribution.org/unstable/sources/tinylogin-${PV}.tar.bz2 \
	file://cvs-20040608.patch \
	file://add-system.patch \
	file://adduser-empty_pwd.patch \
	file://remove-index.patch \
	file://use_O2_option.patch \
	file://passwd_rotate_check.patch \
	file://avoid_static.patch"

SRC_URI[md5sum] = "44da0ff2b727455669890b24305e351d"
SRC_URI[sha256sum] = "5e542e4b7825305a3678bf73136c392feb0d44b8bbf926e8eda5453eea7ddd6b"

EXTRA_OEMAKE = ""

do_compile () {
	oe_runmake 'CC=${CC}' 'CROSS=${HOST_PREFIX}' 'DODEBUG=true'
}

do_install () {
	install -d ${D}${base_bindir}
	install -m 4755 tinylogin ${D}${base_bindir}/tinylogin
	for i in `cat tinylogin.links`; do
		mkdir -p ${D}/`dirname $i`
		ln -sf /bin/tinylogin ${D}$i
	done
}
