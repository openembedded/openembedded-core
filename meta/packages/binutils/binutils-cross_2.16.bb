FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/binutils-${PV}"
include binutils_${PV}.bb
include binutils-cross.inc
