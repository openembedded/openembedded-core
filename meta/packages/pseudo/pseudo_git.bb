DESCRIPTION = "Pseudo gives fake root capabilities to a normal user"
HOMEPAGE = "http://wiki.github.com/wrpseudo/pseudo/"
LIC_FILES_CHKSUM = "file://COPYING;md5=243b725d71bb5df4a1e5920b344b86ad"
SECTION = "base"
LICENSE = "LGPL2.1"
DEPENDS = "sqlite3"

PV = "0.0+git${SRCPV}"
PR = "r11"

SRC_URI = "git://github.com/wrpseudo/pseudo.git;protocol=git \
           file://static_sqlite.patch"

FILES_${PN} = "${libdir}/libpseudo.so ${bindir}/* ${localstatedir}/pseudo"
PROVIDES += "virtual/fakeroot"

S = "${WORKDIR}/git"

inherit siteinfo

do_configure () {
	${S}/configure --prefix=${prefix} --with-sqlite=${STAGING_DIR_TARGET}${exec_prefix} --bits=${SITEINFO_BITS}
}

do_compile () {
	oe_runmake 'LIB=lib/pseudo/lib'
}

do_install () {
	oe_runmake 'DESTDIR=${D}' 'LIB=lib/pseudo/lib' install
}

BBCLASSEXTEND = "native"


