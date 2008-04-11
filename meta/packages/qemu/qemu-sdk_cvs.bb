require qemu_${PV}.bb
require qemu-sdk.inc

EXTRA_OECONF += "--target-list=arm-linux-user,arm-softmmu"
