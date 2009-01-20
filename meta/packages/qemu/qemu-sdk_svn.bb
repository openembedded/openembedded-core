require qemu_svn.bb
require qemu-sdk.inc

PR = "r1"

EXTRA_OECONF +="--target-list=arm-linux-user,arm-softmmu,i386-softmmu --disable-vnc-tls"

DEPENDS += "gcc3-native"

require qemu-gcc3-check.inc
