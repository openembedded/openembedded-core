require qemu_svn.bb
require qemu-native.inc

DEPENDS += "gcc3-native"

require qemu-gcc3-check.inc
