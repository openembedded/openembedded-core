inherit openmoko-base autotools pkgconfig

DEPENDS_prepend = "${@["openmoko-libs ", ""][(bb.data.getVar('PN', d, 1) == 'openmoko-libs')]}"
