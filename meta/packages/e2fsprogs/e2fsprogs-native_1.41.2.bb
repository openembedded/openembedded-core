require e2fsprogs_${PV}.bb
inherit native

DEPENDS = "gettext-native"
PR = "r1"

do_stage () {
	oe_libinstall -a -C lib libblkid ${STAGING_LIBDIR}/
	oe_libinstall -a -C lib libe2p ${STAGING_LIBDIR}/
	oe_libinstall -a -C lib libext2fs ${STAGING_LIBDIR}/
	oe_libinstall -a -C lib libuuid ${STAGING_LIBDIR}/
	install -d ${STAGING_INCDIR}/e2p
	for h in ${e2pheaders}; do
		install -m 0644 lib/e2p/$h ${STAGING_INCDIR}/e2p/ || die "failed to install $h"
	done
	install -d ${STAGING_INCDIR}/ext2fs
	for h in ${ext2fsheaders}; do
		install -m 0644 lib/ext2fs/$h ${STAGING_INCDIR}/ext2fs/ || die "failed to install $h"
	done
	install -d ${STAGING_INCDIR}/blkid
	for h in blkid.h blkid_types.h; do
		install -m 0644 lib/blkid/$h ${STAGING_INCDIR}/blkid/ || die "failed to install $h"
	done
	install -d ${STAGING_INCDIR}/uuid
        install -m 0644 lib/uuid/uuid.h ${STAGING_INCDIR}/uuid/ || die "failed to install $h"
        
	install -d ${STAGING_LIBDIR}/pkgconfig
	for pc in lib/*/*.pc; do
		install -m 0644 $pc ${STAGING_LIBDIR}/pkgconfig/ || die "failed to install $h"
	done
}
