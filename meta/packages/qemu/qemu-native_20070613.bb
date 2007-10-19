require qemu_${PV}.bb
inherit native
DEPENDS = "zlib-native"
prefix = "${STAGING_DIR}/${BUILD_SYS}"

require qemu-gcc3-check.inc
