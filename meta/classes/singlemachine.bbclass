#
# Emulates the old mode of OE operation where only one machine can be targetted.
#

MULTIMACH_TARGET_SYS = "${TARGET_SYS}"
MULTIMACH_HOST_SYS = "${HOST_SYS}"

STAMP = "${TMPDIR}/stamps/${PF}"
WORKDIR = "${TMPDIR}/work/${PF}"
STAGING_DIR_HOST = "${STAGING_DIR}/${HOST_SYS}"
STAGING_DIR_TARGET = "${STAGING_DIR}/${TARGET_SYS}"
PKGDATA_DIR = "${STAGING_DIR}/pkgdata"
STAGING_KERNEL_DIR = "${STAGING_DIR_HOST}/kernel"
 

