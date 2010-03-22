DESCRIPTION = "Pseudo gives fake root capabilities to a normal user"
SECTION = "base"
LICENSE = "LGPL2.1"
DEPENDS = "sqlite3"

PV = "0.0+git${SRCPV}"
PR = "r2"

SRC_URI = "git://github.com/wrpseudo/pseudo.git;protocol=git \
           file://tweakflags.patch;patch=1"

S = "${WORKDIR}/git"

inherit siteinfo

do_configure () {
	${S}/configure --prefix=${prefix} --with-sqlite=${STAGING_DIR_TARGET}${exec_prefix} --bits=${SITEINFO_BITS}
}

NATIVE_INSTALL_WORKS = "1"

BBCLASSEXTEND = "native"


