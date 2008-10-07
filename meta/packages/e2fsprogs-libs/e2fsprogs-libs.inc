DESCRIPTION = "EXT2 Filesystem Utilities"
LICENSE = "GPL"
SECTION = "base"
PRIORITY = "optional"

inherit autotools pkgconfig

SRC_URI = "${SOURCEFORGE_MIRROR}/e2fsprogs/e2fsprogs-libs-${PV}.tar.gz \
           file://configure.patch;patch=1 \
           file://compile-subst.patch;patch=1 \
           file://m4.patch;patch=1"

S = "${WORKDIR}/e2fsprogs-libs-${PV}"
FILES_e2fsprogs-libs-dev_append = " ${datadir}/et ${datadir}/ss"

do_stage () {
        for i in libcom_err libss libuuid libblkid; do
                oe_libinstall -a -C lib $i ${STAGING_LIBDIR}
        done
        install -d ${STAGING_INCDIR}/et \
                   ${STAGING_INCDIR}/ss \
                   ${STAGING_INCDIR}/uuid \
                   ${STAGING_INCDIR}/blkid
        install -m 0644 lib/et/com_err.h ${STAGING_INCDIR}/et/
        install -m 0644 lib/ss/ss.h ${STAGING_INCDIR}/ss/
        install -m 0644 lib/ss/ss_err.h ${STAGING_INCDIR}/ss/
        install -m 0644 lib/uuid/uuid.h ${STAGING_INCDIR}/uuid/
        install -m 0644 lib/uuid/uuid_types.h ${STAGING_INCDIR}/uuid/
        install -m 0644 lib/blkid/blkid.h ${STAGING_INCDIR}/blkid/
        install -m 0644 lib/blkid/blkid_types.h ${STAGING_INCDIR}/blkid/
}
