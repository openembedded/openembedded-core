require qemu_${PV}.bb
require qemu-sdk.inc

EXTRA_OECONF += "--target-list=arm-linux-user,arm-softmmu"

DEPENDS += "gcc3-native"

require qemu-gcc3-check.inc
