require binutils_csl-arm-2006q1.bb
require binutils-cross.inc
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/binutils-${PV}"
S = "${WORKDIR}/binutils-2.17"
