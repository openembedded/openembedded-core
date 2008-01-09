require qemu_${PV}.bb
inherit native
DEPENDS = "zlib-native"

require qemu-gcc3-check.inc
