DESCRIPTION = "Pseudo gives fake root capabilities to a normal user"
HOMEPAGE = "http://wiki.github.com/wrpseudo/pseudo/"
LIC_FILES_CHKSUM = "file://COPYING;md5=243b725d71bb5df4a1e5920b344b86ad"
SECTION = "base"
LICENSE = "LGPL2.1"
DEPENDS = "sqlite3"

PV = "0.0+git${SRCPV}"
PR = "r10"

SRC_URI = "git://github.com/wrpseudo/pseudo.git;protocol=git \
           file://tweakflags.patch \
           file://path-munge.patch \
           file://ld_sacredness.patch \
           file://make_parallel.patch \
           file://static_sqlite.patch \
	   file://data-as-env.patch"

FILES_${PN} = "${libdir}/libpseudo.so ${bindir}/* ${localstatedir}/pseudo"
PROVIDES += "virtual/fakeroot"

S = "${WORKDIR}/git"

inherit siteinfo

do_configure () {
	${S}/configure --prefix=${prefix} --with-sqlite=${STAGING_DIR_TARGET}${exec_prefix} --bits=${SITEINFO_BITS} --data=${localstatedir}
}

do_install () {
	oe_runmake 'DESTDIR=${D}' install
}

BBCLASSEXTEND = "native"


