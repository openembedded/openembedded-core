DESCRIPTION = "GNU dbm is a set of database routines that use extensible hashing."
HOMEPAGE = "http://www.gnu.org/software/gdbm/"
SECTION = "libs"
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=241da1b9fe42e642cbb2c24d5e0c4d24"

PR = "r3"

SRC_URI = "${GNU_MIRROR}/gdbm/gdbm-${PV}.tar.gz"

SRC_URI[md5sum] = "88770493c2559dc80b561293e39d3570"
SRC_URI[sha256sum] = "23f8134c5b94bbfb06d756a6b78f074fba6e6028cf2fe01341d40b26db773441"

inherit autotools gettext lib_package

# Needed for dbm python module
EXTRA_OECONF = "-enable-libgdbm-compat"

BBCLASSEXTEND = "native nativesdk"

do_install_append () {
    # Create a symlink to ndbm.h and gdbm.h in include/gdbm to let other packages to find
    # these headers
    install -d ${D}${includedir}/gdbm
    ln -sf ../ndbm.h ${D}/${includedir}/gdbm/ndbm.h
    ln -sf ../gdbm.h ${D}/${includedir}/gdbm/gdbm.h
}

PACKAGES =+ "${PN}-compat \
            "
FILES_${PN}-compat = "${libdir}/libgdbm_compat${SOLIBS} \
                     "
