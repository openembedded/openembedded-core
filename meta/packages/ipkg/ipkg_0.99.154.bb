require ipkg.inc
PR = "r10"

PACKAGES =+ "libipkg-dev libipkg"

RDEPENDS_${PN} += "${VIRTUAL-RUNTIME_update-alternatives}"
