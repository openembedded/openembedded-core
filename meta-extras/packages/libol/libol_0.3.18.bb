PR = "r7"

SRC_URI = "http://www.balabit.com/downloads/files/libol/0.3/${P}.tar.gz"

S = "${WORKDIR}/${PN}-${PV}"

inherit autotools binconfig

