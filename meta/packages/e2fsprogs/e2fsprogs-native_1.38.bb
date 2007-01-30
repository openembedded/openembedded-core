SECTION = "base"
require e2fsprogs_${PV}.bb
inherit native

FILESPATH = "${@base_set_filespath([ '${FILE_DIRNAME}/e2fsprogs-${PV}', '${FILE_DIRNAME}/e2fsprogs', '${FILE_DIRNAME}/files', '${FILE_DIRNAME}' ], d)}"
PACKAGES = ""
DEPENDS = ""

do_stage () {
	oe_libinstall -a -C lib libe2p ${STAGING_LIBDIR}/
	oe_libinstall -a -C lib libext2fs ${STAGING_LIBDIR}/
	install -d ${STAGING_INCDIR}/e2p
	for h in ${e2pheaders}; do
		install -m 0644 lib/e2p/$h ${STAGING_INCDIR}/e2p/ || die "failed to install $h"
	done
	install -d ${STAGING_INCDIR}/ext2fs
	for h in ${ext2fsheaders}; do
		install -m 0644 lib/ext2fs/$h ${STAGING_INCDIR}/ext2fs/ || die "failed to install $h"
	done
}
