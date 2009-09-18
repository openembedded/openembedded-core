require qemu_git.bb
require qemu-nativesdk.inc

SRC_URI += "file://glflags.patch;patch=1"

PR = "r4"

EXTRA_OECONF +="--target-list=arm-linux-user,arm-softmmu,i386-softmmu --disable-vnc-tls --cc=${HOST_PREFIX}gcc"
