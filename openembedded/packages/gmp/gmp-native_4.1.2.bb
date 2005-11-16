include gmp_${PV}.bb

FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/gmp-${PV}"
S = "${WORKDIR}/gmp-${PV}"

inherit native

