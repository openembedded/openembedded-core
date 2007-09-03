require binutils_${PV}.bb
require binutils-cross.inc
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/binutils-${PV}"
